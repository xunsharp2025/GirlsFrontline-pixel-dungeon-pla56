package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SaltyZongzi extends Food {
	{
		image = ItemSpriteSheet.SALTYZONGZI;
	}

	@Override
	protected float eatingTime(){
		if(Dungeon.hero.hasTalent(Talent.BARGAIN_SKILLS)){
			return 0;
		}else{
			return super.eatingTime();
		}
	}

	@Override
    public int value() {
        return 32*quantity;
    }
}
