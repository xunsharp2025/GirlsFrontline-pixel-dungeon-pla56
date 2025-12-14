/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

//import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.ExtractSummoned;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ExtractUpgrade;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Objects;

public abstract class NPC extends Mob {

	{
		HP = HT = 1;
		EXP = 0;

		alignment = Alignment.NEUTRAL;
		state = PASSIVE;
	}

/*    public float lootChance(){
        return this.lootChance;
    }

    public void rollToDropLoot(){
        if (Random.Float() < lootChance()) {
            Item loot = createLoot();
            if (loot != null) {
                int ofs = PathFinder.NEIGHBOURS9[Random.Int(9)];
                if (!Dungeon.level.pit[this.pos + ofs] &&!Dungeon.level.solid[this.pos + ofs] && Dungeon.level.passable[this.pos + ofs]) {
                    Dungeon.level.drop(loot, pos).sprite.drop();
                }
            }
//            if(Objects.equals(loot, new ExtractUpgrade())){
//                ExtractSummoned = true;
//            }
            //提取升级是否已生成的计数变更
        }
    }*///离场NPC完成任务时的掉落物

	@Override
	public void beckon( int cell ) {
	}
	
}