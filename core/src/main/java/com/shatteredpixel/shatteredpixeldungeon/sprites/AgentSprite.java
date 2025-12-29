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
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class AgentSprite extends FistSprite {

    {
        boltType = MagicMissile.SHADOW;
    }

	public AgentSprite() {
		super();
		
		texture( Assets.AGENT );
		
		TextureFilm frames = new TextureFilm( texture, 26, 24 );
		
		idle = new Animation( 3, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 1, 2 );
		
		run = new Animation( 6, true );
		run.frames( frames, 3, 4, 5, 6, 7, 8 );
		
		attack = new Animation( 16, false );
		attack.frames( frames, 9, 9, 10, 10, 13, 11, 11, 12, 11, 12, 13, 13 );

        zap = new Animation( 8, false );
        zap.frames( frames, 11, 12, 13, 13, 13, 12, 13 );

        die = new Animation( 4, false );
		die.frames( frames, 14, 15, 16, 17, 17 );
		
		play( idle );
	}


	@Override
	public void attack( int cell ) {
		super.attack( cell );

        jump(ch.pos, ch.pos, null, 0, 0.34f );
	}

    @Override
    protected int texOffset() {
        return 50;
    }

    @Override
    protected Emitter createEmitter() {
        Emitter emitter = emitter();
        emitter.pour(ShadowParticle.MISSILE, 0.06f );
        return emitter;
    }

    @Override
    public int blood() {
        return 0xFF4A2F53;
    }


    //用于更新日志显示的代理人的待机动作
    public static class AgentSpriteRe extends MobSprite {
        public AgentSpriteRe() {
            super();

            texture( Assets.AGENT );

            TextureFilm frames = new TextureFilm( texture, 26, 24 );

            idle = new Animation( 3, true );
            idle.frames( frames, 0, 0, 0, 0, 0, 1, 2 );
            play( idle );
        }
    }
}