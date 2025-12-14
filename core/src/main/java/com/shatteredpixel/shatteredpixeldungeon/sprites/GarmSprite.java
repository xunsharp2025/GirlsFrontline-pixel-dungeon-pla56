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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.CorrosionParticle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class GarmSprite extends FistSprite {
//    private static final float SLAM_TIME	= 0.33f;

	public GarmSprite() {
		super();

		texture( Assets.GARM );
		
		TextureFilm frames = new TextureFilm( texture, 36, 27 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0);
		
		run = new Animation( 12, true );
		run.frames( frames, 1, 2, 3, 4 );

		attack = new Animation( 5, false );
		attack.frames( frames, 2, 2, 4, 4 );

        zap = new Animation( 8, false );
        zap.frames( frames, 0, 3, 4, 0 );


        die = new Animation( 8, false );
		die.frames( frames, 5, 6, 7 );
		
		play( idle );
	}
	
	@Override
	public void attack( int cell ) {
		super.attack( cell );

        jump(ch.pos, ch.pos, null, 14, 0.8f );
	}
	
	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim );
		if (anim == attack) {
			speed.set( 0 );
			acc.set( 0 );
			place( ch.pos );
			
			Camera.main.shake( 1, 0.2f );
		}
	}

    @Override
    protected int texOffset() {
        return 0;
    }

    @Override
    protected Emitter createEmitter() {
        Emitter emitter = emitter();
        emitter.pour(CorrosionParticle.MISSILE, 0.06f );
        return emitter;
    }

    @Override
    public int blood() {
        return 0xFF7F7F7F;
    }

}
