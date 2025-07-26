package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

public class RedBook extends Artifact{

    public static final String AC_CAST       = "CAST";

    {
        image = ItemSpriteSheet.REDBOOK;

        levelCap = 0;
        charge = 0;
        chargeCap = 0;
        cooldown = 0;
        defaultAction = AC_CAST;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Cooldowncharge();
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped(hero) && !cursed)
            actions.add(AC_CAST);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_CAST) && cooldown == 0 && isEquipped(hero) && !cursed) {
            deadBomb();
            cooldown = 350;
        } else if(cooldown>0 && isEquipped(hero) && !cursed) {
            GLog.w(Messages.get(this, "colddown") );
        }
    }

    public void deadBomb() {

        new Flare( 5, 32 ).color( 0xFF0000, true ).show( curUser.sprite, 2f );
        Sample.INSTANCE.play( Assets.SND_READ );
        Invisibility.dispel();

        int count = 0;
        Mob affected = null;
        int heroPos = curUser.pos;
        int range = 3;

        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (Dungeon.level.distance(heroPos, mob.pos) <= range) {
                Buff.affect( mob, Terror.class, Terror.DURATION ).object = curUser.id();

                if (mob.buff(Terror.class) != null){
                    count++;
                    affected = mob;
                }
            }
        }

        switch (count) {
            case 0:
                GLog.i( Messages.get(ScrollOfTerror.class, "none") );
                break;
            case 1:
                GLog.i( Messages.get(ScrollOfTerror.class, "one", affected.name) );
                break;
            default:
                GLog.i( Messages.get(ScrollOfTerror.class, "many") );
        }

        GameScene.flash( 0xFFFFFF );

        Sample.INSTANCE.play( Assets.SND_BLAST );
        Invisibility.dispel();

        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (Dungeon.level.distance(heroPos, mob.pos) <= range) {
                mob.damage(mob.HP, this);
            }
        }
    }


    public class Cooldowncharge extends ArtifactBuff{
        @Override
        public boolean act() {

            if (cooldown > 0)
                cooldown--;

            updateQuickslot();
            spend(TICK);
            return true;
        }
    }

}
