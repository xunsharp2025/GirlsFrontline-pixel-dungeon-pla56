package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
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
    protected Charger charger=null;
    public float partialCharge = 0f;

    {
        curCharges = 1;
        maxCharges = 1;
        tier = 3;
        useMissileSprite = false;
        RCH = 3;
        effectIndex = 2;
        image = ItemSpriteSheet.GUN562;
        needEquip = false;
    }

    private static final String CUR_CHARGES         = "curCharges";
    private static final String PARTIALCHARGE       = "partialCharge";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( CUR_CHARGES, curCharges );
        bundle.put( PARTIALCHARGE , partialCharge );
    }
    
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        curCharges = bundle.getInt( CUR_CHARGES );
        partialCharge = bundle.getFloat( PARTIALCHARGE );
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
            if (mainActor == Dungeon.hero && !mainActor.isAlive()) {
                Dungeon.fail(getClass());
            }
        }

        if (!subActors.isEmpty()) {
            for (Char sub : subActors) {
                sub.damage(baseDamage * 3 / 4,this);
                if (sub == Dungeon.hero && !sub.isAlive()) {
                    Dungeon.fail(getClass());
                }
            }
        }

        if (!outerActors.isEmpty()) {
            for (Char outer : outerActors) {
                outer.damage(baseDamage / 5, this);
                if (outer == Dungeon.hero && !outer.isAlive()) {
                    Dungeon.fail(getClass());
                }
            }
        }

        super.onShootComplete(cell);
    }

    @Override
    public boolean collect( Bag container ) {
        if (super.collect( container )) {
            if (container.owner != null) {
                if (charger == null) {
                    charger = new Charger();
                }
                charger.attachTo(container.owner);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_RELOAD);
        return actions;
    }

    @Override
    public void execute(Hero hero,String action) {
        if(action.equals(AC_SHOOT)){
            if(curCharges < 1){
                GLog.n(Messages.get(this, "empty"));
            }else{
                curUser = hero;
                curItem = this;
                GameScene.selectCell(zapper);
            }
        }else{
            super.execute(hero,action);
        }
    }

    @Override
    public void activate(Char owner) {
        if (charger == null) {
            charger = new Charger();
        }
        charger.attachTo(owner);
    }

    @Override
    public void onDetach( ) {
        if(charger != null){
            charger.detach();
            charger = null;
        }
    }

    public class Charger extends Buff {
        private static final float BASE_CHARGE_DELAY = 10f;
        private static final float SCALING_CHARGE_ADDITION = 40f;
        private static final float NORMAL_SCALE_FACTOR = 0.875f;

        public static final float CHARGE_BUFF_BONUS = 0.25f;

        float scalingFactor = NORMAL_SCALE_FACTOR;
        
        @Override
        public boolean act() {
            if (curCharges < maxCharges){
                recharge();
            }
            
            while (partialCharge >= 1 && curCharges < maxCharges) {
                partialCharge--;
                curCharges++;
                updateQuickslot();
            }
            
            if (curCharges == maxCharges){
                partialCharge = 0;
            }
            
            spend( TICK );
            
            return true;
        }

        private void recharge(){
            int missingCharges = maxCharges - curCharges;
            missingCharges = Math.max(0, missingCharges);

            float turnsToCharge =100;

            LockedFloor lock = target.buff(LockedFloor.class);
            if (lock == null || lock.regenOn()){
                partialCharge += (1f/turnsToCharge);
            }
        }

        public void gainCharge(float charge){
            if (curCharges < maxCharges) {
                partialCharge += charge;
                while (partialCharge >= 1f) {
                    curCharges++;
                    partialCharge--;
                }
                curCharges = Math.min(curCharges, maxCharges);
                updateQuickslot();
            }
        }
    }

}
