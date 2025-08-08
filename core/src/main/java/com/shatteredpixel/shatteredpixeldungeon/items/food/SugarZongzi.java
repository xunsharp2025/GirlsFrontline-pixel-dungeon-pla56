package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SugarZongzi extends Food {
	{
		image = ItemSpriteSheet.SUGARZONGZI;
		energy = Hunger.HUNGRY/2f;
	}

	
}
