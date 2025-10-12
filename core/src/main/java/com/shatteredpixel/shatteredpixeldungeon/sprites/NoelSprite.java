package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.noosa.Halo;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

public class NoelSprite extends MobSprite {
    public NoelSprite() {
        super();

        texture( Assets.Sprites.NOEL );

        TextureFilm frames = new TextureFilm( texture, 14, 16 );

        idle = new Animation( 4, true );
        idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3 );

        run = new Animation( 20, true );
        run.frames( frames, 0 );

        die = new Animation( 20, false );
        die.frames( frames, 0 );

        play( idle );
    }

    @Override
    public void die() {
        super.die();

        emitter().start( ElmoParticle.FACTORY, 0.03f, 60 );

        if (visible) {
            Sample.INSTANCE.play( Assets.Sounds.BURNING );
        }
    }
}