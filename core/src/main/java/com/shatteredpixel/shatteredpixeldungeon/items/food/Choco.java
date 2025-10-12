package com.shatteredpixel.shatteredpixeldungeon.items.food;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Speed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.Holidays;

public class Choco extends Food {
    {
        reset();
        bones = false;
    }
    
    @Override
    public void reset() {
        super.reset();
        if (Holidays.holiday == Holidays.Holiday.midAutumnFestival) {
            image = ItemSpriteSheet.NUTSMOONCAKE;
        } else {
            image = ItemSpriteSheet.CHOCO;
        }
    }
    
    @Override
    public String name() {
        if (Holidays.holiday == Holidays.Holiday.midAutumnFestival) {
            return Messages.get(this, "nutty_moon_cake");
        } else {
            return Messages.get(this, "name");
        }
    }
    
    @Override
    public String info() {
        if (Holidays.holiday == Holidays.Holiday.midAutumnFestival) {
            return Messages.get(this, "nutty_moon_cake_desc");
        } else {
            return Messages.get(this, "desc");
        }
    }
    
    @Override
    protected void satisfy( Hero hero ) {
        super.satisfy(hero);
        
        if (Holidays.holiday == Holidays.Holiday.midAutumnFestival) {
            // 为nutsmooncake添加300回合的饱腹效果
            WellFed wellFed = Buff.affect(hero, WellFed.class);
            wellFed.left = 300; // 直接设置left属性
            
            // 添加速度增益效果
            Buff.affect(hero, Speed.class, 3.f);
        }
    }
    
    @Override
    public int value() {
        if(Holidays.holiday != Holidays.Holiday.NONE){
            return 100*quantity;
        } else {
            return 20*quantity;
        }
    }
    
}
