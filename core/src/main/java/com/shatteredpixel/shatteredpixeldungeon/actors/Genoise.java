package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Genoise extends Actor {

    {
        actPriority = BUFF_PRIO;
    }

    private int target = -1;
    private int minDmg = 0;
    private int maxDmg = 0;

    public Genoise() {
        Level level = Dungeon.level;

        if (level != null) {
            //level.genoises.add(this);
        }
    }

    public static Genoise newGenoise(int cell, int min, int max) {
        Genoise genoise = new Genoise();
        genoise.target = cell;
        genoise.minDmg = min;
        genoise.maxDmg = max;
        return genoise;
    }

    private static final String TARGET = "target";
    private static final String MINDMG = "mindmg";
    private static final String MAXDMG = "maxdmg";
    private static final String FUSETIME = "fusetime";

    public final int getTarget() { return target; }

    @Override
    protected boolean act() {

        /*
        if (getTime() < fuseTime)
        {
            spend(TICK);
            return true;
        }
        */

        Sample.INSTANCE.play( Assets.Sounds.BLAST );

        if (target >= 0 && Dungeon.level.heroFOV[target]) {
            CellEmitter.center( target ).burst( BlastParticle.FACTORY, 30 );
        }

        boolean terrainAffected = false;
        for (int n : PathFinder.NEIGHBOURS9) {
            int c = target + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if (Dungeon.level.heroFOV[c]) {
                    CellEmitter.get( c ).burst( SmokeParticle.FACTORY, 4 );
                }

                if (Dungeon.level.flamable[c]) {
                    Dungeon.level.destroy( c );
                    GameScene.updateMap( c );
                    terrainAffected = true;
                }

                //destroys items / triggers bombs caught in the blast.
                Heap heap = Dungeon.level.heaps.get( c );
                if(heap != null)
                    heap.explode();

                Char ch = Actor.findChar( c );
                if (ch != null) {
                    //those not at the center of the blast take damage less consistently.
                    int dmg = Random.NormalIntRange(minDmg, maxDmg) * (c == target ? 2 : 1) - ch.drRoll();
                    if (dmg > 0) {
                        ch.damage( dmg , Dungeon.hero );
                    }

                    if (ch == Dungeon.hero && !ch.isAlive()) {
                        Dungeon.fail( getClass() );
                    }
                }
            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        Actor.remove(this);
        //Dungeon.level.genoises.remove(this);
        return true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(TARGET, target);
        bundle.put(MINDMG, minDmg);
        bundle.put(MAXDMG, maxDmg);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        target = bundle.getInt(TARGET);
        minDmg = bundle.getInt(MINDMG);
        maxDmg = bundle.getInt(MAXDMG);
    }
}
