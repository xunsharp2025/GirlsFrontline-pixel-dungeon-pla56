package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.G11;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class WandofNukeBomb extends DamageWand {

    {
        image = ItemSpriteSheet.WAND_MAGIC_MISSILE;
        maxCharges = 1;
    }

    public int min(int lvl){
        return Dungeon.hero.HT/2+5;
    }

    public int max(int lvl){
        return Dungeon.hero.HT/2+8;
    }

    @Override
    public void onZap( Ballistica bolt ) {

        Char ch = Actor.findChar( bolt.collisionPos );

        if (ch != null) {
            // 中心地格和非中心地格的伤害处理
            for (int n : PathFinder.NEIGHBOURS9) {
                int c = bolt.collisionPos + n;
                if (c >= 0 && c < Dungeon.level.length()) {
                    if (Dungeon.level.heroFOV[c]) {
                        CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                    }

                    if (Dungeon.level.flamable[c]) {
                        Dungeon.level.destroy(c);
                        GameScene.updateMap(c);
                    }

                    // destroys items / triggers bombs caught in the blast.
                    Heap heap = Dungeon.level.heaps.get(c);
                    if (heap != null)
                        heap.explode();

                    Char target = Actor.findChar(c);
                    if (target != null) {
                        // 如果是中心地格，则施加完整伤害
                        // 如果不是中心地格，则施加四分之三的伤害
                        int damage = n == 0 ? damageRoll() : (damageRoll() * 3 / 4);
                        target.damage(damage, this);

                        if (target == Dungeon.hero && !target.isAlive())
                            Dungeon.fail(getClass());
                    }
                    if(Dungeon.hero.buff(Cooldown.class)==null){
                        Buff.affect(Dungeon.hero,Cooldown.class,200f);
                    }
                }
            }
        } else {
            //Dungeon.level.seal(bolt.collisionPos, null, true);
        }
    }


    @Override
    public void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent,
                MagicMissile.FIRE_CONE,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    public void onHit(G11 staff, Char attacker, Char defender, int damage) {

    }

    public static class Cooldown extends FlavourBuff {

        {
            type = buffType.NEGATIVE;
        }

        public static final float DURATION = 10f;

        /*@Override
        public int icon() {
            //TODO
            return BuffIndicator.DEGRADE;
        }*/

        @Override
        public String toString() {
            return Messages.get(Cooldown.class, "name");
        }

        @Override
        public String desc() {
            return Messages.get(Cooldown.class, "desc", dispTurns());
        }

    }




    protected int initialCharges() {
        return 1;
    }

}

