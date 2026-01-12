package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;


import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Bundle;

public class FoodShield extends ShieldBuff {

    {
        type = buffType.POSITIVE;
    }
    private int count = 0;

    @Override
    public synchronized boolean act() {
        if (shielding() <= 0 ){
            detach();
            count = 0;
        }
        spend(TICK);
        return true;
    }
    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.SHIELDED);
        else target.sprite.remove(CharSprite.State.SHIELDED);
    }
    @Override
    public void incShield( int amt ){
        super.incShield(amt);
        count+=amt;
    }
    private static final String FoodCount = "FoodCount";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( FoodCount, count );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        count = bundle.getInt( FoodCount );
    }

    @Override
    //logic edited slightly as buff should not detach
    public int absorbDamage(int dmg) {
        if (shielding() <= 0) return dmg;

        if (shielding() >= dmg){
            decShield(dmg);
            dmg = 0;
        } else {
            dmg -= shielding();
            decShield(shielding());
        }
        return dmg;
    }

}