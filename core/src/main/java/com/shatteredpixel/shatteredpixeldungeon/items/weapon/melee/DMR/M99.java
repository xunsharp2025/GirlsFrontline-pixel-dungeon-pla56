/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DMR;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class M99 extends DesignatedMarksmanRifle {

	{
		image = ItemSpriteSheet.GLAIVE;

		tier = 5;
		ACC = 2f;	// 60% additional accuracy
		DLY = 1.5f;	// 攻击延迟设置为1.5f
		RCH = 3;
	}

	@Override
    public boolean canReach(Char owner, int target) {
        // 15级及以上时无视墙体，只检查距离；15级以下使用默认的可达性检查
        //if (level() >= 15) {
          //  return Dungeon.level.distance(owner.pos, target) <= reachFactor(owner);
        //} else {
            return super.canReach(owner, target);
        }
    //}

	@Override
	public int max(int lvl) {
		return  Math.round(6.67f*(tier+1)) +    //40 base, up from 30
				lvl*Math.round(1.33f*(tier+1)); //+8 per level, up from +6
	}

}
