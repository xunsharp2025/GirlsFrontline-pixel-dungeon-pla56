package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.Genoise;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.GenoiseWarn;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.GooWarn;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfGenoise extends Wand {
    {
        image = ItemSpriteSheet.WAND_TRANSFUSION;

        collisionProperties = Ballistica.PROJECTILE;

        charger = new Charger();
    }

    private static final float TIME_TO_EXPLODE = 2f;

    @Override
    public void onZap(Ballistica beam) {
        for (int n : PathFinder.NEIGHBOURS8) {
            int c = beam.collisionPos + n;
            if ( c >= 0 && Blob.volumeAt( c, GooWarn.class ) == 0 ) {
                GameScene.add( Blob.seed( c, Math.round( 1 + TIME_TO_EXPLODE ), GooWarn.class) );
            }
        }

        if (beam.collisionPos >= 0 && Blob.volumeAt( beam.collisionPos, GenoiseWarn.class ) == 0) {
            GameScene.add( Blob.seed( beam.collisionPos, Math.round( 1 + TIME_TO_EXPLODE ), GenoiseWarn.class) );
        }

        Genoise newGenoise = Genoise.newGenoise( beam.collisionPos, min(level()), max(level()) );
        Actor.addDelayed(newGenoise, TIME_TO_EXPLODE);

    }

    @Override
    public void updateLevel() {
        maxCharges = 1;
        curCharges = 1;
    }

    @Override
    protected int initialCharges() {
        return 1;
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {}

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( 0xCC0000 );
        particle.am = 0.6f;
        particle.setLifespan(1f);
        particle.speed.polar( Random.Float(PointF.PI2), 2f );
        particle.setSize(1f, 2f);
        particle.radiateXY(0.5f);
    }

    public int min(int lvl){
        return 20 + lvl;
    }

    public int max(int lvl){
        return 30 + 2*lvl;
    }

    public class Charger extends Wand.Charger {

        private static final float TURNS_TO_CHARGE = 50f;

        protected void recharge(){

            LockedFloor lock = target.buff(LockedFloor.class);
            if (lock == null || lock.regenOn())
                partialCharge += 1f/TURNS_TO_CHARGE;

            for (Recharging bonus : target.buffs(Recharging.class)){
                if (bonus != null && bonus.remainder() > 0f) {
                    partialCharge += CHARGE_BUFF_BONUS * bonus.remainder();
                }
            }
        }
    }

}
