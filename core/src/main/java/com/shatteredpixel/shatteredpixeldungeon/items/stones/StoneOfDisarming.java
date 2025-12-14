/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StoneOfDisarming extends InventoryStone {
	
	private static final int DIST = 8;

	{
		image = ItemSpriteSheet.STONE_DISARM;

		//so traps do not activate before the effect
		pressesCell = false;
	}

//usableOnItem和onItemSelected为从探魔符石处复制过来
    public boolean usableOnItem(Item item) {
        return (item instanceof EquipableItem || item instanceof Wand) && !(item instanceof MissileWeapon) && (!item.isIdentified() || !item.cursedKnown);
    }
    protected void onItemSelected(Item item) {
        item.cursedKnown = true;
        boolean negativeMagic = false;
        boolean positiveMagic = false;
        useAnimation();
        negativeMagic = item.cursed;
        if (!negativeMagic) {
            if (item instanceof Weapon && ((Weapon)item).hasCurseEnchant()) {
                negativeMagic = true;
            } else if (item instanceof Armor && ((Armor)item).hasCurseGlyph()) {
                negativeMagic = true;
            }
        }

        positiveMagic = item.trueLevel() > 0;
        if (!positiveMagic) {
            if (item instanceof Weapon && ((Weapon)item).hasGoodEnchant()) {
                positiveMagic = true;
            } else if (item instanceof Armor && ((Armor)item).hasGoodGlyph()) {
                positiveMagic = true;
            }
        }

        if (!positiveMagic && !negativeMagic) {
            GLog.i(Messages.get(this, "detected_none", new Object[0]), new Object[0]);
        } else if (positiveMagic && negativeMagic) {
            GLog.h(Messages.get(this, "detected_both", new Object[0]), new Object[0]);
        } else if (positiveMagic) {
            GLog.p(Messages.get(this, "detected_good", new Object[0]), new Object[0]);
        } else if (negativeMagic) {
            GLog.w(Messages.get(this, "detected_bad", new Object[0]), new Object[0]);
        }


    }

//activate为原拆除符石效果，onThrow复制的RuneStone中的onThrow并去除对InventoryStone的限制
	@Override
    protected void onThrow(int cell) {
        if (Dungeon.level.pit[cell]){
            super.onThrow( cell );
        } else {
            if (pressesCell) Dungeon.level.pressCell( cell );
            activateB(cell);
            Invisibility.dispel();
        }
    }
	protected void activateB(final int cell) {
		boolean[] FOV = new boolean[Dungeon.level.length()];
		Point c = Dungeon.level.cellToPoint(cell);
		ShadowCaster.castShadow(c.x, c.y, FOV, Dungeon.level.losBlocking, DIST);

		int sX = Math.max(0, c.x - DIST);
		int eX = Math.min(Dungeon.level.width()-1, c.x + DIST);

		int sY = Math.max(0, c.y - DIST);
		int eY = Math.min(Dungeon.level.height()-1, c.y + DIST);

		ArrayList<Trap> disarmCandidates = new ArrayList<>();

		for (int y = sY; y <= eY; y++){
			int curr = y*Dungeon.level.width() + sX;
			for ( int x = sX; x <= eX; x++){

				if (FOV[curr]){

					Trap t = Dungeon.level.traps.get(curr);
					if (t != null && t.active){
						disarmCandidates.add(t);
					}

				}
				curr++;
			}
		}

		Collections.sort(disarmCandidates, new Comparator<Trap>() {
			@Override
			public int compare(Trap o1, Trap o2) {
				float diff = Dungeon.level.trueDistance(cell, o1.pos) - Dungeon.level.trueDistance(cell, o2.pos);
				if (diff < 0){
					return -1;
				} else if (diff == 0){
					return Random.Int(2) == 0 ? -1 : 1;
				} else {
					return 1;
				}
			}
		});

		//disarms at most nine traps
		while (disarmCandidates.size() > 9){
			disarmCandidates.remove(9);
		}

		for ( Trap t : disarmCandidates){
			t.reveal();
			t.disarm();
			CellEmitter.get(t.pos).burst(Speck.factory(Speck.STEAM), 6);
		}

		Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
	}
}
