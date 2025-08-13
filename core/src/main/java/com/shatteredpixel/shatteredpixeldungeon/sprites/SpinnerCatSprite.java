package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpinnerSprite;
import com.watabou.noosa.TextureFilm;

public class SpinnerCatSprite extends SpinnerSprite {

	public SpinnerCatSprite() {
		super();

		perspectiveRaise = 0f;

		texture( Assets.Sprites.SPINNERCAT );
		
		TextureFilm frames = new TextureFilm( texture, 18, 18 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 1, 0, 1 );
		
		run = new Animation( 15, true );
		run.frames( frames, 0, 2, 0, 3 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 0, 4, 5, 0 );
		
		zap = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, 6, 7, 8, 9 );
		
		play( idle );
	}
}