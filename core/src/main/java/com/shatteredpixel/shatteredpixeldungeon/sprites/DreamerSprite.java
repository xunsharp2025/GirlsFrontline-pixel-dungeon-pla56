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
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class DreamerSprite extends FistSprite {
	
	public DreamerSprite() {
		super();
		
		texture( Assets.DREAMER );
		
		TextureFilm frames = new TextureFilm( texture, 24, 24 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 1, 2, 1, 0 );
		
		run = new Animation( 3, true );
		run.frames( frames, 0, 1, 2, 1, 0 );
		
		attack = new Animation( 8, false );
		attack.frames( frames, 0, 3, 4, 0 );

        zap = new Animation( 8, false );
        zap.frames( frames, 0, 3, 4, 0 );

        die = new Animation( 8, false );
		die.frames( frames, 0, 3, 5, 6, 7 );
		
		play( idle );
	}
	
	private int posToShoot;
	
	@Override
	public void attack( int cell ) {
		posToShoot = cell;
		super.attack( cell );

        jump(ch.pos, ch.pos, null, 0, 0.34f );
	}
	
	@Override
	public void onComplete( Animation anim ) {
		if (anim == attack) {

			Sample.INSTANCE.play( Assets.Sounds.ZAP );
			MagicMissile.boltFromChar( parent,
					MagicMissile.SHADOW,
					this,
					posToShoot,
					new Callback() {
						@Override
						public void call() {
							ch.onAttackComplete();
						}
					} );

			idle();
			
		} else {
			super.onComplete( anim );
		}
	}

    @Override
    protected int texOffset() {
        return 0;
    }

    @Override
    protected Emitter createEmitter() {
        Emitter emitter = emitter();
        emitter.pour( FlameParticle.FACTORY, 0.06f );
        return emitter;
    }

    @Override
    public int blood() {
        return 0xFFFFDD34;
    }

}
