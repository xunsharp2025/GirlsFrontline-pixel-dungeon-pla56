package com.gfpixel.gfpixeldungeon.items.weapon.melee;

import static com.gfpixel.gfpixeldungeon.Dungeon.hero;

import com.gfpixel.gfpixeldungeon.Assets;
import com.gfpixel.gfpixeldungeon.Dungeon;
import com.gfpixel.gfpixeldungeon.actors.Actor;
import com.gfpixel.gfpixeldungeon.actors.Char;
import com.gfpixel.gfpixeldungeon.actors.buffs.Buff;
import com.gfpixel.gfpixeldungeon.actors.hero.Hero;
import com.gfpixel.gfpixeldungeon.effects.CellEmitter;
import com.gfpixel.gfpixeldungeon.effects.particles.BlastParticle;
import com.gfpixel.gfpixeldungeon.effects.particles.SmokeParticle;
import com.gfpixel.gfpixeldungeon.items.Heap;
import com.gfpixel.gfpixeldungeon.items.wands.WandofNukeBomb;
import com.gfpixel.gfpixeldungeon.messages.Messages;
import com.gfpixel.gfpixeldungeon.scenes.GameScene;
import com.gfpixel.gfpixeldungeon.sprites.ItemSpriteSheet;
import com.gfpixel.gfpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
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
    public String defaultAction(){
        if (curCharges > 0) {
            defaultAction = AC_SHOOT;
        } else {
            defaultAction = null;
            GLog.n(Messages.get(this, "need_reload"));
        }
        activate(hero);
        return defaultAction;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_RELOAD);
        return actions;
    }

    @Override
    public void onShootComplete(int cell) {
        //播放音效
        Sample.INSTANCE.play(Assets.SND_BLAST);

        //处理地形和物品互动
        //爆炸特效
        if (Dungeon.level.heroFOV[cell]) {
            CellEmitter.center(cell).burst(BlastParticle.FACTORY,30);
        }
        //周围25格
        for (int i : PathFinder.NEIGHBOURS25) {
            if (Dungeon.level.heroFOV[cell + i]) {
                //烟雾特效
                CellEmitter.get(cell + i).burst(SmokeParticle.FACTORY,4);
            }
            //烧毁地形
            if (Dungeon.level.flamable[cell + i]) {
                Dungeon.level.destroy(cell + i);
                GameScene.updateMap(cell + i);
            }
            //烧毁物品
            Heap heap = Dungeon.level.heaps.get(cell + i);
            if (heap != null) {
                heap.explode();
            }
        }

        //查找目标这里可以考虑判定友伤
        //中心目标
        Char mainActor = Actor.findChar(cell);
        //周围八格
        ArrayList<Char> subActors = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS8) {
            Char subactor = Actor.findChar(cell + i);
            if (subactor != null) {
                subActors.add(subactor);
            }
        }
        //再外一圈
        ArrayList<Char> outerActors = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS16) {
            Char outeractor = Actor.findChar(cell + i);
            if (outeractor != null) {
                outerActors.add(outeractor);
            }
        }

        //判定伤害
        int baseDamage = curUser.HT * 2 / 3;
        if (mainActor != null) {
            mainActor.damage(baseDamage, this);
            if (mainActor == hero && !mainActor.isAlive()) {
                Dungeon.fail(getClass());
            }
        }

        if (!subActors.isEmpty()) {
            for (Char sub : subActors) {
                sub.damage(baseDamage * 3 / 4,this);
                if (sub == hero && !sub.isAlive()) {
                    Dungeon.fail(getClass());
                }
            }
        }

        if (!outerActors.isEmpty()) {
            for (Char outer : outerActors) {
                outer.damage(baseDamage / 5, this);
                if (outer == hero && !outer.isAlive()) {
                    Dungeon.fail(getClass());
                }
            }
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

        public static final int RELOAD_TIME = 200;

        @Override
        public boolean act() {
            if (Dungeon.hero.isAlive() && hero.belongings.weapon == gun562 && gun562.curCharges < gun562.maxCharges) {
                gun562.reload();
            }

            spend(RELOAD_TIME);
            return true;
        }
    }
}
