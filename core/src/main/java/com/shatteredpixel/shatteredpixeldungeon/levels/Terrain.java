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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;

public class Terrain {

	public static final int CHASM			= 0;
	public static final int EMPTY			= 1;
	public static final int GRASS			= 2;
	public static final int EMPTY_WELL		= 3;
	public static final int WALL			= 4;
	public static final int DOOR			= 5;
	public static final int OPEN_DOOR		= 6;
	public static final int ENTRANCE		= 7;
	public static final int EXIT			= 8;
	public static final int EMBERS			= 9;
	public static final int LOCKED_DOOR		= 10;
	public static final int PEDESTAL		= 11;
	public static final int WALL_DECO		= 12;
	public static final int BARRICADE		= 13;
	public static final int EMPTY_SP		= 14;
	public static final int HIGH_GRASS		= 15;
	public static final int SECRET_DOOR	    = 16;
	public static final int SECRET_TRAP     = 17;
	public static final int TRAP            = 18;
	public static final int INACTIVE_TRAP   = 19;
	public static final int EMPTY_DECO		= 20;
	public static final int LOCKED_EXIT		= 21;
	public static final int UNLOCKED_EXIT	= 22;
	public static final int SIGN			= 23;
	public static final int WELL			= 24;
	public static final int STATUE			= 25;
	public static final int STATUE_SP		= 26;
	public static final int BOOKSHELF		= 27;
	public static final int ALCHEMY			= 28;
	public static final int WATER		    = 29;
	public static final int FURROWED_GRASS	= 30;
	public static final int CRYSTAL_DOOR	= 31;
	
	public static final int PASSABLE		= 0x001;
	public static final int LOS_BLOCKING	= 0x002;
	public static final int FLAMABLE		= 0x004;
	public static final int SECRET			= 0x008;
	public static final int SOLID			= 0x010;
	public static final int AVOID			= 0x020;
	public static final int LIQUID			= 0x040;
	public static final int PIT				= 0x080;
    public static final int BREAKABLE       = 0x100;

	
	public static final int[] flags = new int[512];
	static {
		flags[CHASM]		= AVOID	| PIT;
		flags[EMPTY]		= PASSABLE | BREAKABLE ;
		flags[GRASS]		= PASSABLE | FLAMABLE | BREAKABLE ;
		flags[EMPTY_WELL]	= PASSABLE | BREAKABLE ;
		flags[WATER]		= PASSABLE | LIQUID | BREAKABLE ;
		flags[WALL]			= LOS_BLOCKING | SOLID | BREAKABLE ;
		flags[DOOR]			= PASSABLE | LOS_BLOCKING | FLAMABLE | SOLID | BREAKABLE ;
		flags[OPEN_DOOR]	= PASSABLE | FLAMABLE | BREAKABLE ;
		flags[ENTRANCE]		= PASSABLE/* | SOLID*/;
		flags[EXIT]			= PASSABLE;
		flags[EMBERS]		= PASSABLE | BREAKABLE;
		flags[LOCKED_DOOR]	= LOS_BLOCKING | SOLID;
		flags[CRYSTAL_DOOR]	= SOLID;
		flags[PEDESTAL]		= PASSABLE;
		flags[WALL_DECO]	= flags[WALL] | BREAKABLE;
		flags[BARRICADE]	= FLAMABLE | SOLID | LOS_BLOCKING | BREAKABLE ;
		flags[EMPTY_SP]		= flags[EMPTY] | BREAKABLE;
		flags[HIGH_GRASS]	= PASSABLE | LOS_BLOCKING | FLAMABLE | BREAKABLE ;
		flags[FURROWED_GRASS]= flags[HIGH_GRASS] | BREAKABLE;

		flags[SECRET_DOOR]  = flags[WALL]  | SECRET | BREAKABLE ;
		flags[SECRET_TRAP]  = flags[EMPTY] | SECRET | BREAKABLE ;
		flags[TRAP]         = AVOID | BREAKABLE ;
		flags[INACTIVE_TRAP]= flags[EMPTY] | BREAKABLE ;

		flags[EMPTY_DECO]	= flags[EMPTY] | BREAKABLE ;
		flags[LOCKED_EXIT]	= SOLID;
		flags[UNLOCKED_EXIT]= PASSABLE;
		flags[SIGN]			= SOLID; //Currently these are unused except for visual tile overrides where we want terrain to be solid with no other properties
		flags[WELL]			= AVOID;
		flags[STATUE]		= SOLID | BREAKABLE ;
		flags[STATUE_SP]	= flags[STATUE] | BREAKABLE ;
		flags[BOOKSHELF]	= flags[BARRICADE] | BREAKABLE ;
		flags[ALCHEMY]		= SOLID | BREAKABLE ;
	}

	public static int discover( int terr ) {
		switch (terr) {
		case SECRET_DOOR:
			return DOOR;
		case SECRET_TRAP:
			return TRAP;
		default:
			return terr;
		}
	}

	//removes signs, places floors instead
	public static int[] convertTilesFrom0_6_0b(int[] map){
		for (int i = 0; i < map.length; i++){
			if (map[i] == 23){
				map[i] = 1;
			}
		}
		return map;
	}

}
