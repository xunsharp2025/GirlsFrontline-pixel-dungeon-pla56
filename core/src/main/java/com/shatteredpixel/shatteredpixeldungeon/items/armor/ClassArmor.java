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

package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChooseAbility;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.text.DecimalFormat;
import java.util.ArrayList;

abstract public class ClassArmor extends Armor {

	private static final String AC_ABILITY = "ABILITY";
    private static final String AC_TRANSFER = "TRANSFER";
    private static final String ARMOR_TIER	= "armortier";
    private static final String CHARGE	    = "charge";


    {
		levelKnown = true;
		cursedKnown = true;
		defaultAction = AC_ABILITY;

		bones = false;
	}

	private int armorTier;

    @Override
    public String info(){
        String info =super.info();
        if(Dungeon.ArmorLock||lockcharge)
            info += "\n";
        if(Dungeon.ArmorLock)
            info += "\n改造护甲的充能已被_锁定为满充能_。";
        if(lockcharge)
            info += "\n改造护甲的充能已被锁定为_ " + chargeRem + " _点。";

        return info;
    }
	private Charger charger;
	public float charge = 0;
    public boolean lockcharge = false;
    public float chargeRem = 0;
	
	public ClassArmor() {
		super( 5 );
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		charger = new Charger();
		charger.attachTo(ch);
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {
			if (charger != null){
				charger.detach();
				charger = null;
			}
			return true;

		} else {
			return false;

		}
	}

	public static ClassArmor upgrade (Hero owner, Armor armor ) {
		
		ClassArmor classArmor = null;
		
		switch (owner.heroClass) {
		case WARRIOR:
			classArmor = new WarriorArmor();
			BrokenSeal seal = armor.checkSeal();
			if (seal != null) {
				classArmor.affixSeal(seal);
			}
			break;
		case ROGUE:default:
			classArmor = new RogueArmor();
			break;
		case MAGE:
			classArmor = new MageArmor();
			break;
		case HUNTRESS:
			classArmor = new HuntressArmor();
			break;
		case TYPE561:
			classArmor = new Type561Armor();
		}
		
		classArmor.level(armor.trueLevel());
		classArmor.tier = armor.tier;
		classArmor.augment = armor.augment;
		classArmor.inscribe( armor.glyph );
        if (armor.seal != null) {
            classArmor.seal = armor.seal;
        }
		classArmor.cursed = armor.cursed;
		classArmor.curseInfusionBonus = armor.curseInfusionBonus;
		classArmor.masteryPotionBonus = armor.masteryPotionBonus;
        if (armor.levelKnown && armor.cursedKnown) {
            classArmor.identify();
        } else {
            classArmor.levelKnown = armor.levelKnown;
            classArmor.cursedKnown = true;
        }

		classArmor.charge = 50;
		
		return classArmor;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ARMOR_TIER, tier );
		bundle.put( CHARGE, charge );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		tier = bundle.getInt( ARMOR_TIER );
		charge = bundle.getFloat(CHARGE);
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero )) {
			actions.add( AC_ABILITY );
		}

        actions.add(AC_TRANSFER);
		return actions;
	}

	@Override
	public String actionName(String action, Hero hero) {
		if (hero.armorAbility != null && action.equals(AC_ABILITY)){
			return hero.armorAbility.name().toUpperCase();
		} else {
			return super.actionName(action, hero);
		}
	}

	@Override
	public String status() {
		return Messages.format( "%.0f%%", Math.floor(charge) );
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_ABILITY)){

			//for pre-0.9.3 saves
			if (hero.armorAbility == null){
				GameScene.show(new WndChooseAbility(null, this, hero));
			} else if (!isEquipped( hero )) {
				usesTargeting = false;
				GLog.w( Messages.get(this, "not_equipped") );
			} else if (charge < hero.armorAbility.chargeUse(hero)) {
				usesTargeting = false;
				GLog.w( Messages.get(this, "low_charge") );
			} else  {
				usesTargeting = hero.armorAbility.useTargeting();
				hero.armorAbility.use(this, hero);
			}
			
		}else if (action.equals(AC_TRANSFER)) {
            GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.CROWN), Messages.get(ClassArmor.class, "transfer_title", new Object[0]), Messages.get(ClassArmor.class, "transfer_desc", new Object[0]), new String[]{Messages.get(ClassArmor.class, "transfer_prompt", new Object[0]), Messages.get(ClassArmor.class, "transfer_cancel", new Object[0])}) {
                protected void onSelect(int index) {
                    if (index == 0) {
                        GameScene.selectItem(new WndBag.ItemSelector() {
                            public String textPrompt() {
                                return Messages.get(ClassArmor.class, "transfer_prompt", new Object[0]);
                            }

                            public boolean itemSelectable(Item item) {
                                return (item instanceof Armor)&&(item != ClassArmor.this);
                            }

                            public void onSelect(Item item) {
                                if (item != null && item != ClassArmor.this) {
                                    Armor armor = (Armor)item;
                                    armor.detach(hero.belongings.backpack);
                                    if (hero.belongings.armor == armor) {
                                        hero.belongings.armor = null;
                                        if (hero.sprite instanceof HeroSprite) {
                                            ((HeroSprite)hero.sprite).updateArmor();
                                        }
                                    }

                                    ClassArmor.this.level(armor.trueLevel());
                                    ClassArmor.this.tier = armor.tier;
                                    ClassArmor.this.augment = armor.augment;
                                    ClassArmor.this.cursed = armor.cursed;
                                    ClassArmor.this.curseInfusionBonus = armor.curseInfusionBonus;
                                    ClassArmor.this.masteryPotionBonus = armor.masteryPotionBonus;
                                    if (armor.checkSeal() != null) {
                                        ClassArmor.this.inscribe(armor.glyph);
                                        ClassArmor.this.seal = armor.checkSeal();
                                    } else if (ClassArmor.this.checkSeal() != null) {
                                        if (ClassArmor.this.seal.level() > 0) {
                                            int newLevel = ClassArmor.this.trueLevel() + 1;
                                            ClassArmor.this.level(newLevel);
                                            Badges.validateItemLevelAquired(ClassArmor.this);
                                        }

                                        if (armor.glyph != null || !ClassArmor.this.seal.canTransferGlyph()) {
                                            ClassArmor.this.inscribe(armor.glyph);
                                            ClassArmor.this.seal.setGlyph((Armor.Glyph)null);
                                        }
                                    } else {
                                        ClassArmor.this.inscribe(armor.glyph);
                                    }

                                    ClassArmor.this.identify();
                                    GLog.p(Messages.get(ClassArmor.class, "transfer_complete", new Object[0]), new Object[0]);
                                    hero.sprite.operate(hero.pos);
                                    hero.sprite.emitter().burst(Speck.factory(104), 12);
                                    Sample.INSTANCE.play("sounds/evoke.mp3");
                                    hero.spend(1.0F);
                                    hero.busy();
                                }
                            }
                        });
                    }

                }
            });
        }
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if (Dungeon.hero.belongings.contains(this)) {
			ArmorAbility ability = Dungeon.hero.armorAbility;
			if (ability != null) {
				desc += "\n\n" + ability.shortDesc();
				float chargeUse = ability.chargeUse(Dungeon.hero);
				desc += " " + Messages.get(this, "charge_use", new DecimalFormat("#.##").format(chargeUse));
			} else {
				desc += "\n\n" + "_" + Messages.get(this, "no_ability") + "_";
			}
		}

		return desc;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int value() {
		return 0;
	}

	public class Charger extends Buff {
		@Override
		public boolean act() {
			LockedFloor lock = target.buff(LockedFloor.class);
			if (lock == null || lock.regenOn()) {
				charge += 100 / 500f; //500 turns to full charge
				updateQuickslot();
				if (charge > 100) {
					charge = 100;
				}
			}
            if(Dungeon.ArmorLock){
                charge = 100;
            }
            if (lockcharge) {
                charge = chargeRem;
            }
			spend(TICK);
			return true;
		}
	}
}
