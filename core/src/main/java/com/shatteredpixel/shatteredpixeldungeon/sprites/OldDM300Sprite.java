package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OldDM300;
import com.watabou.noosa.TextureFilm;

public class OldDM300Sprite extends MobSprite {

    public OldDM300Sprite() {
        super();

        texture( Assets.GAGER );

        TextureFilm frames = new TextureFilm( texture, 26, 22 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 0 );

        run = new Animation( 10, true );
        run.frames( frames, 1, 2, 3, 4, 5, 6 );

        attack = new Animation( 24, false );
        attack.frames( frames, 7, 8, 7, 8, 7, 8 );

        die = new Animation( 10, false );
        die.frames( frames, 9, 10, 11 );

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        turnTo( ch.pos, cell );
        ((OldDM300)ch).magnum();
        play( attack );
    }
}
