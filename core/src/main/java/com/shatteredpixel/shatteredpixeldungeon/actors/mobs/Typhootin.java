package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ToxicImbue;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CyclopsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HydraSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TyphootinSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class Typhootin extends Mob {

    {
        spriteClass = TyphootinSprite.class;

        HP = HT = 700;
        EXP = 70;
        defenseSkill = 5;

        loot = new Ankh();
        lootChance = 1f;

        properties.add(Property.BOSS);
    }

    public static int[] ArmyPos = new int[]{
            815,551,566,302
    };
    @Override
    public boolean act() {
        boolean bleeding = (HP*2 <= HT);
        if(bleeding && Dungeon.isChallenged(Challenges.STRONGER_BOSSES) && buff(Cooldown.class) == null){
            Mob army =Army();
            army.state = army.HUNTING;
            army.maxLvl = -1;
            if(army instanceof Hydra){
                army.HP = army.HT = 140;
            }
            GameScene.add(army);
            int randomPosIndex = Random.Int(ArmyPos.length);
            int randomPos = ArmyPos[randomPosIndex];
            ScrollOfTeleportation.appear(army,randomPos);

            if(Random.Int(4)==0){
                Cyclops cyclops = new Cyclops();
                cyclops.state = cyclops.HUNTING;
                cyclops.maxLvl = -1;
                GameScene.add(cyclops);
                int cyclopsrandomPosIndex = Random.Int(ArmyPos.length);
                int cyclopsrandomPos = ArmyPos[cyclopsrandomPosIndex];
                ScrollOfTeleportation.appear(cyclops,cyclopsrandomPos);
            }

            sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
            yell(Messages.get(this,"army"));
            Buff.affect(this, Cooldown.class, 20f);
            Buff.affect(this, Barrier.class).setShield(20);
        }

        return super.act();
    }

    public static class Cooldown extends FlavourBuff { }

    private Mob Army() {
        List<Class<? extends Mob>> mobTypes = new ArrayList<>();
        mobTypes.add(Hydra.class);
        mobTypes.add(CyclopsArmy.class);

        Random.shuffle(mobTypes);
        Class<? extends Mob> selectedMobType = mobTypes.get(0);
        Mob mob = null;
        try {
            mob = selectedMobType.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {}
        return mob;
    }

    public static class CyclopsArmy extends Cyclops{
        {
            HP = HT = 100;
            EXP = 17;
            defenseSkill = 30;
            baseSpeed = 1f;
            maxLvl = -1;
            properties.add(Property.ARMO);
        }

        @Override
        public void damage( int dmg, Object src ) {
            super.damage( dmg, src );

            if (isAlive() && !enraged && HP < HT / 4) {
                enraged = true;
                spend( TICK );
                if (Dungeon.level.heroFOV[pos]) {
                    sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "enraged") );
                }
            }
        }

        private boolean enraged = false;

        @Override
        public int damageRoll() {
            return enraged ?
                    Random.NormalIntRange( 30, 65 ) :
                    Random.NormalIntRange( 25, 50 );
        }
    }



    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 5, 60 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 50;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public void move( int step ) {
        super.move( step );

        if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && HP < HT) {

            HP += Random.Int( 1, HT - HP );
            sprite.emitter().burst( ElmoParticle.FACTORY, 5 );

            if (Dungeon.level.heroFOV[step] && Dungeon.hero.isAlive()) {
                GLog.n( Messages.get(this, "repair") );
            }
        }

        int[] cells = {
                step-1, step+1, step-Dungeon.level.width(), step+Dungeon.level.width(),
                step-1-Dungeon.level.width(),
                step-1+Dungeon.level.width(),
                step+1-Dungeon.level.width(),
                step+1+Dungeon.level.width()
        };
        int cell = cells[Random.Int( cells.length )];

        if (Dungeon.level.heroFOV[cell]) {
            CellEmitter.get( cell ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
            Camera.main.shake( 3, 0.7f );
            Sample.INSTANCE.play( Assets.Sounds.ROCKS ); 

            if (Dungeon.level.water[cell]) {
                GameScene.ripple( cell );
            } else if (Dungeon.level.map[cell] == Terrain.EMPTY) {
                Level.set( cell, Terrain.EMPTY_DECO );
                GameScene.updateMap( cell );
            }
        }

        Char ch = Actor.findChar( cell );
        if (ch != null && ch != this) {
            Buff.prolong( ch, Paralysis.class, 2 );
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        super.damage(dmg, src);
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg*1.5f);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Dungeon.level.unseal();
        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey( Dungeon.depth  ), pos ).sprite.drop();


        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof CyclopsArmy || mob instanceof Hydra) {
                mob.die( cause );
            }
        }

        yell( Messages.get(this, "defeated") );
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell( Messages.get(this, "notice") );
    }

    {
        resistances.add( Grim.class );
        resistances.add( ScrollOfPsionicBlast.class );
    }

    {
        immunities.add(Freezing.class);
        immunities.add(Terror.class);
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
             immunities.add(ToxicGas.class);
        }
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);
    }


}
