package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Empulse;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShootGun extends MeleeWeapon {
    private static final String AC_RELOAD = "RELOAD";
    private static final String AC_SHOOT = "SHOOT";
    private int effectIndex = 2;
    private   int     reloadTime    = 2;
    private   Charger charger       = null;
    protected boolean needReload    = true;
    protected boolean hasCharge     = true;
    protected int cooldownTurns = 200;
    private   int     cooldownLeft  = 0;
    protected float rate = 1;
    protected float EMPduration = 0;

    private static final String HAS_CHARGE    = "hasCharge";
    private static final String COOLDOWN_LEFT = "cooldownLeft";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(HAS_CHARGE   ,hasCharge   );
        bundle.put(COOLDOWN_LEFT,cooldownLeft);
    }
    
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        hasCharge    = bundle.getBoolean(HAS_CHARGE   );
        cooldownLeft = bundle.getInt    (COOLDOWN_LEFT);
    }

    {
        image = ItemSpriteSheet.MAGESSTAFF;
        tier = 1;
        defaultAction = AC_SHOOT;
        usesTargeting = true;
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (needReload && !hasCharge){
            actions.add(AC_RELOAD);
        } else
            actions.add(AC_SHOOT);
        return actions;
    }

    @Override
    public String defaultAction(){
        if (hasCharge) {
            defaultAction = AC_SHOOT;
        } else if(needReload){
            defaultAction = AC_RELOAD;
        }

        return defaultAction;
    }

    @Override
    public void execute(Hero hero,String action) {
        super.execute(hero,action);

        if (action.equals(AC_RELOAD)) {
            reload();
        } else if (action.equals(AC_SHOOT)) {
            shoot();
        }
    }

    private void reload() {
        if (0 == cooldownLeft) {
            hasCharge=true;

            curUser.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "reload") );
            curUser.sprite.operate( curUser.pos );
            updateQuickslot();

            curUser.spendAndNext(reloadTime);
        } else {
            GLog.n(Messages.get(this, "cd_status", cooldownLeft));
        }

    }

    private void shoot(){
        if (hasCharge){
            GameScene.selectCell(zapper);
        }else{
            GLog.n(Messages.get(this, "empty",cooldownLeft));
        }
    }

    public void onShootComplete(int cell, int lvl) {
        BombDestory(cell);
        BombAttack(cell, lvl);
        if(!Dungeon.hero.isAlive()){
            Dungeon.fail(getClass());
        }
        hasCharge=false;
        int down = 0;
        if(Dungeon.hero.hasTalent(Talent.Type56Three_Bomb)){
            switch (Dungeon.hero.pointsInTalent(Talent.Type56Three_Bomb)){
                case 1:
                    down=15;
                    break;
                case 2:
                    down=35;
                    break;
                case 3:
                    down=50;
                    break;
                default:
                    down=0;
                    break;
            }
        }
        cooldownLeft=cooldownTurns-down;
        updateQuickslot();
        curUser.spendAndNext(1f);
    }
    protected void BombDestory(int cell){
        if(rate>0){
            //伤害倍率大于0
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            if (Dungeon.level.heroFOV[cell]) {
                CellEmitter.get(cell).burst(BlastParticle.FACTORY,30);
                //爆炸粒子
            }
        }
        for(int n : PathFinder.NEIGHBOURS9) {
            //对九格先执行一遍破坏
            int c =cell + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if(rate<=0){
                    //伤害倍率为0时不执行对地形和物品的破坏
                    continue;
                }
                if (Dungeon.level.flamable[c]) {
                    Dungeon.level.destroy(c);
                    GameScene.updateMap(c);
                }

                // destroys items / triggers bombs caught in the blast.
                Heap heap = Dungeon.level.heaps.get(c);
                if (heap != null)
                    heap.explode();

                if (Dungeon.level.heroFOV[c]) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY,4);
                }
            }
        }
    }
    protected void BombAttack(int cell, int lvl){
        resetEMP();
        //重置EMP回合数
        int attack = 0;
        for(int m : PathFinder.NEIGHBOURS9) {
            //再执行伤害，以完整保留掉落物
            int d =cell + m;
            if (d >= 0 && d < Dungeon.level.length()) {

                Char target = Actor.findChar(d);

                if (target != null) {
                    if(Dungeon.hero.hasTalent(Talent.EMP_Three))
                        attack+=Dungeon.hero.pointsInTalent(Talent.EMP_Three);
                    //天赋3计数
                    int damage= BombDamage(lvl);
                    target.damage((int)(damage*rate),this);

                    if(target.isAlive()&&EMPduration>0){
                        //EMP
                        Buff.prolong(target, Empulse.class,EMPduration);
                        CellEmitter.get(d).burst(EnergyParticle.FACTORY, 10);
                        //EMP粒子
                    }
                }

            }
        }
        if (attack>0){
            attack = Math.min(attack, 6);
            for (Buff b : Dungeon.hero.buffs()){
                if (b instanceof Artifact.ArtifactBuff){
                    if (!((Artifact.ArtifactBuff) b).isCursed()) ((Artifact.ArtifactBuff) b).charge(Dungeon.hero, attack);
                }
            }
            ScrollOfRecharging.chargeParticle(Dungeon.hero);
        }
    }
    protected void resetEMP(){
        if (Dungeon.hero.subClass== HeroSubClass.EMP_BOMB){
            EMPduration = 3;
            if(Dungeon.hero.hasTalent(Talent.EMP_One)){
                EMPduration+=Dungeon.hero.pointsInTalent(Talent.EMP_One);
            }
        }
    }
    protected int BombDamage(int lvl){
        return 0;
    }

    @Override
    public String status() {
        if(hasCharge){
            return "1/1";
        }else if(0 == cooldownLeft){
            return "0/1";
        }else{
            return "CD:" + cooldownLeft;
        }
    }

    public static CellSelector.Listener zapper = new  CellSelector.Listener() {
        //格子选择监听器
        @Override
        public void onSelect(Integer target) {
            if (target != null) {//由于底层原因会调用两次，所以必须判断非null
                ShootGun curShootGun = (ShootGun)curItem;

                final Ballistica shot = new Ballistica( curUser.pos, target, Ballistica.PROJECTILE);
                int cell = shot.collisionPos;

                if (target == curUser.pos || cell == curUser.pos) {
                    GLog.i( Messages.get(ShootGun.class, "self_target") );
                    return;
                }

                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));

                curUser.sprite.zap(cell);
                MagicMissile.boltFromChar(
                    curUser.sprite.parent,
                    curShootGun.effectIndex,
                    curUser.sprite,
                    cell,
                    new Callback(){
                        @Override
                        public void call() {
                            curShootGun.onShootComplete(cell, curShootGun.buffedLvl());
                        }
                    }
                );
            }
        }

        @Override
        public String prompt() {
            return Messages.get(ShootGun.class, "prompt");
        }
    };

    public String desc() {
        return Messages.get(this, "desc", cooldownLeft);
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
        @Override
        public boolean act() {
            LockedFloor lock = target.buff(LockedFloor.class);
            if((lock == null || lock.regenOn())
            && !hasCharge){
                if (cooldownLeft>0){
                    cooldownLeft--;
                }

                if (0==cooldownLeft && !needReload){
                    hasCharge=true;
                    updateQuickslot();
                }
            }
            
            spend( TICK );
            return true;
        }
    }
}
