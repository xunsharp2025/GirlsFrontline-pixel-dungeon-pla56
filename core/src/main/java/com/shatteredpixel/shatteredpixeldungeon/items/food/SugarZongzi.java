package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SugarZongzi extends Food {
	{
		image = ItemSpriteSheet.SUGARZONGZI;
		energy = Hunger.HUNGRY/2f;
	}

    @Override
    protected void satisfy( Hero hero ){
        energy+=30;
        super.satisfy(hero);
        energy = Hunger.HUNGRY/2f;
        //仅在进食前改变提供饱食度，进食后恢复，以实现不对号角生效
    }
    @Override
    protected float eatingTime(){
        if(Dungeon.hero.hasTalent(Talent.Type56Two_FOOD)){
            return 0;
        }else{
            return super.eatingTime();
        }
    }
	@Override
    public int value() {
        return 16*quantity;
    }
}
