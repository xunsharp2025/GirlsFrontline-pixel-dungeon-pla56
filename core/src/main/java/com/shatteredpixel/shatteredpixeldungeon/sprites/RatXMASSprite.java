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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class RatXMASSprite extends RatSprite {

	public RatXMASSprite() {
        super();

        texture( Assets.Sprites.RAT );

        TextureFilm frames = new TextureFilm( texture, 16, 15 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0+16*2, 0+16*2, 0+16*2, 1+16*2 );

        run = new Animation( 10, true );
        run.frames( frames, 6+16*2, 7+16*2, 8+16*2, 9+16*2, 10+16*2 );

        attack = new Animation( 15, false );
        attack.frames( frames, 2+16*2, 3+16*2, 4+16*2, 5+16*2, 0+16*2 );

        die = new Animation( 10, false );
        die.frames( frames, 11+16*2, 12+16*2, 13+16*2, 14+16*2 );

        play( idle );
	}
}
