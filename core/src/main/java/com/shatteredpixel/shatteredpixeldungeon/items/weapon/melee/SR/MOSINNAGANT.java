/*
 *试试看自己制作一把武器
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SR;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class MOSINNAGANT extends SniperRifle {
	  {
	  	  image = ItemSpriteSheet.MOSINNAGANT;

	  	  tier = 3;
		  ACC = 1.75f;
		  DLY = 5f;
		  RCH = 60;
	  }




	  @Override
      public int max(int lvl) {
        return  Math.round(15.4f*(tier+1)) +    //40 base, up from 35
                lvl*Math.round(2.1f*(tier+3)); //+4 per level, up from +3
    }
}