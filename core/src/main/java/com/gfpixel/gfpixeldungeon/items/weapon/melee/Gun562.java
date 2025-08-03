package com.gfpixel.gfpixeldungeon.items.weapon.melee;

import static com.gfpixel.gfpixeldungeon.Dungeon.hero;

import com.gfpixel.gfpixeldungeon.Assets;
import com.gfpixel.gfpixeldungeon.Dungeon;
import com.gfpixel.gfpixeldungeon.QuickSlot;
import com.gfpixel.gfpixeldungeon.actors.Actor;
import com.gfpixel.gfpixeldungeon.actors.Char;
import com.gfpixel.gfpixeldungeon.actors.buffs.Buff;
import com.gfpixel.gfpixeldungeon.actors.hero.Hero;
import com.gfpixel.gfpixeldungeon.actors.mobs.Rat;
import com.gfpixel.gfpixeldungeon.effects.CellEmitter;
import com.gfpixel.gfpixeldungeon.effects.particles.BlastParticle;
import com.gfpixel.gfpixeldungeon.effects.particles.SmokeParticle;
import com.gfpixel.gfpixeldungeon.items.Heap;
import com.gfpixel.gfpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.gfpixel.gfpixeldungeon.items.wands.WandofNukeBomb;
import com.gfpixel.gfpixeldungeon.messages.Messages;
import com.gfpixel.gfpixeldungeon.scenes.GameScene;
import com.gfpixel.gfpixeldungeon.sprites.ItemSpriteSheet;
import com.gfpixel.gfpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

public class Gun562 extends ShootGun {

    {
        curCharges = 1;
        maxCharges = 1;
        tier = 3;
        useMissileSprite = false;
        effectIndex = 2;
        image = ItemSpriteSheet.Gun562;
        needEquip = false;
    }

    @Override
    public int image() {
        if(hero != null){
            activate(hero);
        }
        return super.image();
    }

    @Override
    public String defaultAction(){
        if (curCharges > 0) {
            defaultAction = AC_SHOOT;
        } else {
            defaultAction = null;
            GLog.n(Messages.get(this, "need_reload"));
        }
        return defaultAction;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_RELOAD);
        if (curCharges <= 0) {
            actions.remove(AC_SHOOT);
        }
        return actions;
    }

    @Override
    public void onShootComplete(int cell) {
        //播放音效
        Sample.INSTANCE.play(Assets.SND_BLAST);

        //处理地形和物品互动
        //爆炸特效
        if (Dungeon.level.heroFOV[cell]) {
            CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
        }

        //周围5x5格
        for (int i : PathFinder.NEIGHBOURS9) { //中心点及周边延伸一格
            int targetCell = cell + i;
            if (Dungeon.level.heroFOV[targetCell]) {
                //烟雾特效
                CellEmitter.get(targetCell).burst(SmokeParticle.FACTORY, 4);
            }
            //烧毁地形
            if (Dungeon.level.flamable[targetCell]) {
                Dungeon.level.destroy(targetCell);
                GameScene.updateMap(targetCell);
            }
            //烧毁物品
            Heap heap = Dungeon.level.heaps.get(targetCell);
            if (heap != null) {
                heap.explode();
            }
        }

        //再外一圈4格
        for (int i : PathFinder.NEIGHBOURS4) {
            int targetCell = cell + i * 2; //这里乘以2是为了扩展到5x5范围的最外层一格
            if (Dungeon.level.heroFOV[targetCell]) {
                //烟雾特效
                CellEmitter.get(targetCell).burst(SmokeParticle.FACTORY, 4);
            }
            //烧毁地形
            if (Dungeon.level.flamable[targetCell]) {
                Dungeon.level.destroy(targetCell);
                GameScene.updateMap(targetCell);
            }
            //烧毁物品
            Heap heap = Dungeon.level.heaps.get(targetCell);
            if (heap != null) {
                heap.explode();
            }
        }

        //查找目标这里可以考虑判定友伤
        //中心目标
        Char mainActor = Actor.findChar(cell);
        //中心点及周边延伸一格
        ArrayList<Char> subActors = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS9) {
            Char subactor = Actor.findChar(cell + i);
            if (subactor != null) {
                subActors.add(subactor);
            }
        }
        //再外一圈4格
        ArrayList<Char> outerActors = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS4) {
            Char outeractor = Actor.findChar(cell + i * 2);
            if (outeractor != null) {
                outerActors.add(outeractor);
            }
        }

        //判定伤害
        int baseDamage = curUser.lvl * curUser.HT / 2; // baseDamage修改为当前等级最大血量的3/2
        baseDamage = Math.min(Math.max(baseDamage + 5, curUser.HT / 4), baseDamage + 8); // 限制伤害范围

        if (mainActor != null) {
            mainActor.damage(baseDamage, this);
            if (mainActor == hero && !mainActor.isAlive()) {
                Dungeon.fail(getClass());
            }
        }

        if (!subActors.isEmpty()) {
            for (Char sub : subActors) {
                sub.damage(baseDamage * 3 / 4, this);
                if (sub == hero && !sub.isAlive()) {
                    Dungeon.fail(getClass());
                }
            }
        }

        if (!outerActors.isEmpty()) {
            for (Char outer : outerActors) {
                outer.damage(baseDamage / 2, this); // 修改为baseDamage的3/2
                if (outer == hero && !outer.isAlive()) {
                    Dungeon.fail(getClass());
                }
            }
        }

        if(altCooldown == 5){
            altCooldown = 10;
            curCharges--;
        }


        super.onShootComplete(cell);
    }


    @Override
    public void activate(Char ch) {
        if (chargeBuff == null) {
            chargeBuff = new ChargeBuff(this);
        }
        chargeBuff.attachTo(ch);
    }

    @Override
    public void onDetach( ) {
        ChargeBuff chargeBuff = curUser.buff(ChargeBuff.class);
        if (chargeBuff != null && chargeBuff == this.chargeBuff) {
            chargeBuff.detach();
        }
    }


    public static class ChargeBuff extends Buff {
        public Gun562 gun562;

        public ChargeBuff(Gun562 gun562) {
            this.gun562 = gun562;
        }

        @Override
        public boolean act() {
            if (gun562.altCooldown == 10 && gun562.coolDown == 0 && Dungeon.hero.isAlive() && gun562.curCharges < gun562.maxCharges) {
                gun562.reload();
                gun562.coolDown = 200;
                gun562.altCooldown = 0;
            } else if(gun562.coolDown == 0 && gun562.altCooldown == 0) {
                Math.min(gun562.curCharges + 1, gun562.maxCharges);
                gun562.updateQuickslot();
                gun562.altCooldown = 10;
            } else if(gun562.coolDown > 0) {
                gun562.coolDown--;
            }
            spend(1f);
            return true;
        }
    }
}
