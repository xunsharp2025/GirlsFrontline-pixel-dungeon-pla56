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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GolyatFactory;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class GolyatFactorySprite extends MobSprite {

	private Animation charging;
	private Emitter summoningBones;

	public GolyatFactorySprite(){
		super();
		
		texture( Assets.Sprites.GOLYATFACTORY );
		TextureFilm film = new TextureFilm( texture, 34, 28 );
		
		idle = new Animation( 2, false );
		idle.frames( film, 0,1 );
		
		run = new Animation( 3, false );
		run.frames( film, 2,3,4,5,6,7 );
		
		zap = new Animation( 10, false );
		zap.frames( film, 5, 6, 7, 8 );
		
		charging = new Animation( 3, false );
		charging.frames( film, 8, 9,10,11 );
		
		die = new Animation( 5, false );
		die.frames( film, 12, 13, 14, 15,16,17 );
		
		attack = zap.clone();
		
		idle();
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (ch instanceof GolyatFactory && ((GolyatFactory) ch).summoning){
			zap(((GolyatFactory) ch).summoningPos);
		}
	}

	@Override
	public void update() {
		super.update();
		if (summoningBones != null && ((GolyatFactory) ch).summoningPos != -1){
			summoningBones.visible = Dungeon.level.heroFOV[((GolyatFactory) ch).summoningPos];
		}
	}

	@Override
	public void die() {
		super.die();
		if (summoningBones != null){
			summoningBones.on = false;
		}
	}

	@Override
	public void kill() {
		super.kill();
		if (summoningBones != null){
			summoningBones.killAndErase();
		}
	}

	public void cancelSummoning(){
		if (summoningBones != null){
			summoningBones.on = false;
		}
	}

	public void finishSummoning(){
		if (summoningBones.visible) {
			Sample.INSTANCE.play(Assets.Sounds.BONES);
			summoningBones.burst(Speck.factory(Speck.RATTLE), 5);
		} else {
			summoningBones.on = false;
		}
		idle();
	}

	public void charge(){
		play(charging);
	}

	@Override
	public void zap(int cell) {
		super.zap(cell);
		if (ch instanceof GolyatFactory && ((GolyatFactory) ch).summoning){
			if (summoningBones != null){
				summoningBones.on = false;
			}
			summoningBones = CellEmitter.get(((GolyatFactory) ch).summoningPos);
			summoningBones.pour(Speck.factory(Speck.RATTLE), 0.2f);
			summoningBones.visible = Dungeon.level.heroFOV[((GolyatFactory) ch).summoningPos];
			if (visible || summoningBones.visible ) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP, 1f, 0.8f );
		}
	}

	@Override
	public void onComplete(Animation anim) {
		super.onComplete(anim);
		if (anim == zap){
			if (ch instanceof GolyatFactory){
				if (((GolyatFactory) ch).summoning){
					charge();
				} else {
					((GolyatFactory)ch).onZapComplete();
					idle();
				}
			} else {
				idle();
			}
		}
	}
}
