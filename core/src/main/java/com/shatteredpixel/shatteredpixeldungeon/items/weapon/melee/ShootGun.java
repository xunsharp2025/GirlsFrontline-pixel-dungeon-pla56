package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class ShootGun extends MeleeWeapon {
    private static final String AC_RELOAD = "RELOAD";
    private static final String AC_SHOOT = "SHOOT";
    private int effectIndex = 2;
    private   int     reloadTime    = 2;
    private   Charger charger       = null;
    protected boolean needReload    = true;
    protected boolean hasCharge     = true;
    protected int     cooldownTurns = 200;
    private   int     cooldownLeft  = 0;

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
        }
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

    public void onShootComplete(int cell) {
        hasCharge=false;
        cooldownLeft=cooldownTurns;
        updateQuickslot();
        curUser.spendAndNext(1f);
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
                    GLog.i( Messages.get(this, "self_target") );
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
                            curShootGun.onShootComplete(cell);
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
