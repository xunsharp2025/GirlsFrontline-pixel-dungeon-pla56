package com.shatteredpixel.shatteredpixeldungeon.items.food;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.Holidays;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class Choco extends Food {
    {
        reset();
        bones = false;
    }
    
    @Override
    public void reset() {
        super.reset();
        if (Holidays.holiday == Holidays.Holiday.midAutumnFestival) {
            image = ItemSpriteSheet.SALTYMOONCAKE;
        } else {
            image = ItemSpriteSheet.CHOCO;
        }
    }
    
    @Override
    public String name() {
        if (Holidays.holiday == Holidays.Holiday.midAutumnFestival) {
            return Messages.get(this, "salty_moon_cake");
        } else {
            return Messages.get(this, "name");
        }
    }
    
    @Override
    public String info() {
        if (Holidays.holiday == Holidays.Holiday.midAutumnFestival) {
            return Messages.get(this, "salty_moon_cake_desc");
        } else {
            return Messages.get(this, "desc");
        }
    }
    
    @Override
    public int value() {
        if(Holidays.holiday == Holidays.Holiday.midAutumnFestival){
            return 100*quantity;
        } else {
            return 50*quantity;
        }
    }
    
}
