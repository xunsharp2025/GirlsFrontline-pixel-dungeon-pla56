package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.buffType;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.State;
import java.util.ArrayList;

public class ImmortalShieldAffecter extends TestItem {
    private static final String AC_SWITCH = "switch";

    public ImmortalShieldAffecter() {
        this.image = ItemSpriteSheet.GREATSHIELD;
        this.defaultAction = "switch";
    }

    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add("switch");
        return actions;
    }

    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals("switch")) {
            if (this.isImmortal(hero)) {
                Buff.detach(hero, ImmortalShield.class);
            } else {
                Buff.affect(hero, ImmortalShield.class);
            }
        }

    }

    private boolean isImmortal(Char target) {
        return target.buff(ImmortalShield.class) != null;
    }

    public static class ImmortalShield extends Buff {
        public ImmortalShield() {
            this.type = buffType.NEUTRAL;
            this.announced = false;
            this.revivePersists = true;
        }

        public boolean act() {
            this.spend(1.0F);
            return true;
        }

        public void fx(boolean on) {
            if (on) {
                this.target.sprite.add(State.SHIELDED);
            } else {
                this.target.sprite.remove(State.SHIELDED);
            }

        }
    }
}
