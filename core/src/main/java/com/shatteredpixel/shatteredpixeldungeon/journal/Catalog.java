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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.HuntressArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MageArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RogueArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.WarriorArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.AssassinsBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BattleAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dirk;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Flail;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gauntlet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greataxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatshield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HandAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Mace;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Whip;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public enum Catalog {
	
	WEAPONS,
	ARMOR,
	WANDS,
	RINGS,
	ARTIFACTS,
	POTIONS,
	SCROLLS;
	
	private LinkedHashMap<Class<? extends Item>, Boolean> seen = new LinkedHashMap<>();
	
	public Collection<Class<? extends Item>> items(){
		return seen.keySet();
	}
	
	public boolean allSeen(){
		for (Class<?extends Item> item : items()){
			if (!seen.get(item)){
				return false;
			}
		}
		return true;
	}
	
	static {
		WEAPONS.seen.put( WornShortsword.class,             true);
		WEAPONS.seen.put( Gloves.class,                     true);
		WEAPONS.seen.put( Dagger.class,                     true);
		WEAPONS.seen.put( MagesStaff.class,                 true);
		WEAPONS.seen.put( Shortsword.class,                 true);
		WEAPONS.seen.put( HandAxe.class,                    true);
		WEAPONS.seen.put( Spear.class,                      true);
		WEAPONS.seen.put( Quarterstaff.class,               true);
		WEAPONS.seen.put( Dirk.class,                       true);
		WEAPONS.seen.put( Sword.class,                      true);
		WEAPONS.seen.put( Mace.class,                       true);
		WEAPONS.seen.put( Scimitar.class,                   true);
		WEAPONS.seen.put( RoundShield.class,                true);
		WEAPONS.seen.put( Sai.class,                        true);
		WEAPONS.seen.put( Whip.class,                       true);
		WEAPONS.seen.put( Longsword.class,                  true);
		WEAPONS.seen.put( BattleAxe.class,                  true);
		WEAPONS.seen.put( Flail.class,                      true);
		WEAPONS.seen.put( RunicBlade.class,                 true);
		WEAPONS.seen.put( AssassinsBlade.class,             true);
		WEAPONS.seen.put( Crossbow.class,                   true);
		WEAPONS.seen.put( Greatsword.class,                 true);
		WEAPONS.seen.put( WarHammer.class,                  true);
		WEAPONS.seen.put( Glaive.class,                     true);
		WEAPONS.seen.put( Greataxe.class,                   true);
		WEAPONS.seen.put( Greatshield.class,                true);
		WEAPONS.seen.put( Gauntlet.class,                   true);
	
		ARMOR.seen.put( ClothArmor.class,                   true);
		ARMOR.seen.put( LeatherArmor.class,                 true);
		ARMOR.seen.put( MailArmor.class,                    true);
		ARMOR.seen.put( ScaleArmor.class,                   true);
		ARMOR.seen.put( PlateArmor.class,                   true);
		ARMOR.seen.put( WarriorArmor.class,                 true);
		ARMOR.seen.put( MageArmor.class,                    true);
		ARMOR.seen.put( RogueArmor.class,                   true);
		ARMOR.seen.put( HuntressArmor.class,                true);
	
		WANDS.seen.put( WandOfMagicMissile.class,           true);
		WANDS.seen.put( WandOfLightning.class,              true);
		WANDS.seen.put( WandOfDisintegration.class,         true);
		WANDS.seen.put( WandOfFireblast.class,              true);
		WANDS.seen.put( WandOfCorrosion.class,              true);
		WANDS.seen.put( WandOfBlastWave.class,              true);
		WANDS.seen.put( WandOfLivingEarth.class,            true);
		WANDS.seen.put( WandOfFrost.class,                  true);
		WANDS.seen.put( WandOfPrismaticLight.class,         true);
		WANDS.seen.put( WandOfWarding.class,                true);
		WANDS.seen.put( WandOfTransfusion.class,            true);
		WANDS.seen.put( WandOfCorruption.class,             true);
		WANDS.seen.put( WandOfRegrowth.class,               true);
	
		RINGS.seen.put( RingOfAccuracy.class,               true);
		RINGS.seen.put( RingOfEnergy.class,                 true);
		RINGS.seen.put( RingOfElements.class,               true);
		RINGS.seen.put( RingOfEvasion.class,                true);
		RINGS.seen.put( RingOfForce.class,                  true);
		RINGS.seen.put( RingOfFuror.class,                  true);
		RINGS.seen.put( RingOfHaste.class,                  true);
		RINGS.seen.put( RingOfMight.class,                  true);
		RINGS.seen.put( RingOfSharpshooting.class,          true);
		RINGS.seen.put( RingOfTenacity.class,               true);
		RINGS.seen.put( RingOfWealth.class,                 true);
	
		ARTIFACTS.seen.put( AlchemistsToolkit.class,        true);
		//ARTIFACTS.seen.put( CapeOfThorns.class,             true);
		ARTIFACTS.seen.put( ChaliceOfBlood.class,           true);
		ARTIFACTS.seen.put( CloakOfShadows.class,           true);
		ARTIFACTS.seen.put( DriedRose.class,                true);
		ARTIFACTS.seen.put( EtherealChains.class,           true);
		ARTIFACTS.seen.put( HornOfPlenty.class,             true);
		//ARTIFACTS.seen.put( LloydsBeacon.class,             true);
		ARTIFACTS.seen.put( MasterThievesArmband.class,     true);
		ARTIFACTS.seen.put( SandalsOfNature.class,          true);
		ARTIFACTS.seen.put( TalismanOfForesight.class,      true);
		ARTIFACTS.seen.put( TimekeepersHourglass.class,     true);
		ARTIFACTS.seen.put( UnstableSpellbook.class,        true);
	
		POTIONS.seen.put( PotionOfHealing.class,            true);
		POTIONS.seen.put( PotionOfStrength.class,           true);
		POTIONS.seen.put( PotionOfLiquidFlame.class,        true);
		POTIONS.seen.put( PotionOfFrost.class,              true);
		POTIONS.seen.put( PotionOfToxicGas.class,           true);
		POTIONS.seen.put( PotionOfParalyticGas.class,       true);
		POTIONS.seen.put( PotionOfPurity.class,             true);
		POTIONS.seen.put( PotionOfLevitation.class,         true);
		POTIONS.seen.put( PotionOfMindVision.class,         true);
		POTIONS.seen.put( PotionOfInvisibility.class,       true);
		POTIONS.seen.put( PotionOfExperience.class,         true);
		POTIONS.seen.put( PotionOfHaste.class,              true);
	
		SCROLLS.seen.put( ScrollOfIdentify.class,           true);
		SCROLLS.seen.put( ScrollOfUpgrade.class,            true);
		SCROLLS.seen.put( ScrollOfRemoveCurse.class,        true);
		SCROLLS.seen.put( ScrollOfMagicMapping.class,       true);
		SCROLLS.seen.put( ScrollOfTeleportation.class,      true);
		SCROLLS.seen.put( ScrollOfRecharging.class,         true);
		SCROLLS.seen.put( ScrollOfMirrorImage.class,        true);
		SCROLLS.seen.put( ScrollOfTerror.class,             true);
		SCROLLS.seen.put( ScrollOfLullaby.class,            true);
		SCROLLS.seen.put( ScrollOfRage.class,               true);
		SCROLLS.seen.put( ScrollOfRetribution.class,        true);
		SCROLLS.seen.put( ScrollOfTransmutation.class,      true);
	}
	
	public static LinkedHashMap<Catalog, Badges.Badge> catalogBadges = new LinkedHashMap<>();
	static {
		catalogBadges.put(WEAPONS, Badges.Badge.ALL_WEAPONS_IDENTIFIED);
		catalogBadges.put(ARMOR, Badges.Badge.ALL_ARMOR_IDENTIFIED);
		catalogBadges.put(WANDS, Badges.Badge.ALL_WANDS_IDENTIFIED);
		catalogBadges.put(RINGS, Badges.Badge.ALL_RINGS_IDENTIFIED);
		catalogBadges.put(ARTIFACTS, Badges.Badge.ALL_ARTIFACTS_IDENTIFIED);
		catalogBadges.put(POTIONS, Badges.Badge.ALL_POTIONS_IDENTIFIED);
		catalogBadges.put(SCROLLS, Badges.Badge.ALL_SCROLLS_IDENTIFIED);
	}
	
	public static boolean isSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass)) {
				return cat.seen.get(itemClass);
			}
		}
		return false;
	}
	
	public static void setSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass) && !cat.seen.get(itemClass)) {
				cat.seen.put(itemClass, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateItemsIdentified();
	}
	
	private static final String CATALOG_ITEMS = "catalog_items";
	
	public static void store( Bundle bundle ){
		
		Badges.loadGlobal();
		
		ArrayList<Class> seen = new ArrayList<>();
		
		//if we have identified all items of a set, we use the badge to keep track instead.
		if (!Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)) {
			for (Catalog cat : values()) {
				if (!Badges.isUnlocked(catalogBadges.get(cat))) {
					for (Class<? extends Item> item : cat.items()) {
						if (cat.seen.get(item)) seen.add(item);
					}
				}
			}
		}
		
		bundle.put( CATALOG_ITEMS, seen.toArray(new Class[0]) );
		
	}
	
	public static void restore( Bundle bundle ){
		
		Badges.loadGlobal();
		
		//logic for if we have all badges
		if (Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)){
			for ( Catalog cat : values()){
				for (Class<? extends Item> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
			return;
		}
		
		//catalog-specific badge logic
		for (Catalog cat : values()){
			if (Badges.isUnlocked(catalogBadges.get(cat))){
				for (Class<? extends Item> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
		}
		
		//general save/load
		if (bundle.contains(CATALOG_ITEMS)) {
			List<Class> seenClasses = new ArrayList<>();
			if (bundle.contains(CATALOG_ITEMS)) {
				seenClasses = Arrays.asList(bundle.getClassArray(CATALOG_ITEMS));
			}
			
			for (Catalog cat : values()) {
				for (Class<? extends Item> item : cat.items()) {
					if (seenClasses.contains(item)) {
						cat.seen.put(item, true);
					}
				}
			}
		}
	}
	
}
