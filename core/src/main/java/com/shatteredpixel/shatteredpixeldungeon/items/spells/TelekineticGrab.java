//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class TelekineticGrab extends TargetedSpell {
    public TelekineticGrab() {
        this.image = ItemSpriteSheet.TELE_GRAB;
    }

    public void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(curUser.sprite.parent, 6, curUser.sprite, bolt.collisionPos, callback);
        Sample.INSTANCE.play("sounds/zap.mp3");
    }

    protected void affectTarget(Ballistica bolt, Hero hero) {
        Char ch = Actor.findChar(bolt.collisionPos);
        if (ch == null && bolt.path.size() > bolt.dist + 1) {
            ch = Actor.findChar((Integer)bolt.path.get(bolt.dist + 1));
            if (!(ch instanceof DwarfKing) || !Dungeon.level.solid[ch.pos]) {
                ch = null;
            }
        }

        if (ch != null && ch.buff(PinCushion.class) != null) {
            while(ch.buff(PinCushion.class) != null) {
                Item item = ((PinCushion)ch.buff(PinCushion.class)).grabOne();
                if (!item.doPickUp(hero, ch.pos)) {
                    GLog.w(Messages.get(this, "cant_grab", new Object[0]), new Object[0]);
                    Dungeon.level.drop(item, ch.pos).sprite.drop();
                    return;
                }

                hero.spend(-1.0F);
                GLog.i(Messages.capitalize(Messages.get(hero, "you_now_have", new Object[]{item.name()})), new Object[0]);
            }
        } else if (Dungeon.level.heaps.get(bolt.collisionPos) != null) {
            Heap h = (Heap)Dungeon.level.heaps.get(bolt.collisionPos);
            if (h.type != Type.HEAP) {
                GLog.w(Messages.get(this, "cant_grab", new Object[0]), new Object[0]);
                h.sprite.drop();
                return;
            }

            while(!h.isEmpty()) {
                Item item = h.peek();
                if (!item.doPickUp(hero, h.pos)) {
                    GLog.w(Messages.get(this, "cant_grab", new Object[0]), new Object[0]);
                    h.sprite.drop();
                    return;
                }

                h.pickUp();
                hero.spend(-1.0F);
                GLog.i(Messages.capitalize(Messages.get(hero, "you_now_have", new Object[]{item.name()})), new Object[0]);
            }
        } else {
            GLog.w(Messages.get(this, "no_target", new Object[0]), new Object[0]);
        }

    }

    public int value() {
        return (int)(60.0F * ((float)this.quantity / 8.0F));
    }

    public int energyVal() {
        return (int)(12.0F * ((float)this.quantity / 8.0F));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{LiquidMetal.class, ArcaneCatalyst.class};
			inQuantity = new int[]{10, 1};

			cost = 2;

			output = TelekineticGrab.class;
			outQuantity = 6;
		}

	}

}
