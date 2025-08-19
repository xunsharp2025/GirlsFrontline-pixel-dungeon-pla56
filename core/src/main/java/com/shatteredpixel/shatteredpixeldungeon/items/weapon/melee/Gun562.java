package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;

import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Gun562 extends ShootGun {
    {
        image = ItemSpriteSheet.GUN562;
        needReload=false;
        tier = 3;
        RCH = 3;
    }

    @Override
    public void onShootComplete(int cell) {
        //播放音效
        Sample.INSTANCE.play(Assets.Sounds.BLAST);

        //处理地形和物品互动
        //爆炸特效
        if (Dungeon.level.heroFOV[cell]) {
            CellEmitter.center(cell).burst(BlastParticle.FACTORY,30);
        }

        for (int i : PathFinder.NEIGHBOURS8) {
            int targetCell = cell + i;
            // 检查是否在地图范围内
            if (targetCell >= 0 && targetCell < Dungeon.level.length()) {
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

        //判定伤害
        float baseDamage = curUser.HT * 2 / 3;
        float damage=baseDamage;
        if (mainActor != null) {
            mainActor.damage(Math.round(damage),this);
            if(Dungeon.hero.hasTalent(Talent.ENHANCE_GRENADE)){
                Buff.affect(mainActor,Bleeding.class ).set( Math.round(damage*0.4f));
                if(Dungeon.hero.pointsInTalent(Talent.ENHANCE_GRENADE)>=2){
                    Buff.affect(mainActor,Cripple.class,3f);
                }
            }
        }

        damage=baseDamage*0.75f;
        for(Char sub : subActors) {
            sub.damage(Math.round(damage),this);
            if(Dungeon.hero.hasTalent(Talent.ENHANCE_GRENADE)){
                Buff.affect(sub,Bleeding.class ).set( Math.round(damage*0.3f));
                if(Dungeon.hero.pointsInTalent(Talent.ENHANCE_GRENADE)>=2){
                    Buff.affect(sub,Cripple.class,3f);
                }
            }
        }

        if(!Dungeon.hero.isAlive()){
            Dungeon.fail(getClass());
        }

        cooldownTurns = (Dungeon.hero.pointsInTalent(Talent.ENHANCE_GRENADE)>=3? 170: 200);

        super.onShootComplete(cell);
    }
}
