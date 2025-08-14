package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpinnerCatSprite;

public class SpinnerCat extends Spinner {
	{
		spriteClass = SpinnerCatSprite.class;

		loot = new MysteryMeat();
		lootChance = 1f;
	}
}