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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpectralBlades;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.ElementalBlast;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.type561.Type56FourOne;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.type561.Type56FourTwo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.AttributeViewer;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.BackpackCleaner;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.CustomWeapon;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.ImmortalShieldAffecter;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.LevelTeleporter;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.MobPlacer;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestBag;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TimeReverser;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TrapPlacer;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.WandOfReflectDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.MapEditor;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.LazyTest;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.TestArmor;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.TestArtifact;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.TestExperience;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.TestIdentify;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.TestLevelSetting;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.TestMelee;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.TestMissile;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.TestPotion;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.TestRing;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.debugBook;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.FoodPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SaltyZongzi;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Choco;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SugarZongzi;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gun561;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SA.Welrod;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SMG.M9;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SMG.Ump45;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LR.GSH18;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.UG.Cannon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	WARRIOR( HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( HeroSubClass.SNIPER, HeroSubClass.WARDEN ),
	TYPE561(HeroSubClass.EMP_BOMB, HeroSubClass.GUN_MASTER),
	GSH18(HeroSubClass.THE_HEART_OF_SIRIUS, HeroSubClass.MOBILE_MEDICALTABLE),
	NONE(  HeroSubClass.NONE );
	private HeroSubClass[] subClasses;

	HeroClass( HeroSubClass...subClasses ) {
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		
		new FoodPouch().collect();
		
		Waterskin waterskin = new Waterskin();
		waterskin.collect();

//		if (DeviceCompat.isDebug()){
//			new MapEditor().collect();
//		}

		if (DeviceCompat.isDebug() || Dungeon.isChallenged(Challenges.TEST_MODE)){
			CustomWeapon customWeapon = new CustomWeapon();
			customWeapon.adjustStatus();
			customWeapon.identify().collect();
			
			new TestBag().collect();
            new debugBook().collect();
            
            new ImmortalShieldAffecter().collect();
            
			new TrapPlacer().collect();
			
			new MobPlacer().collect();
			
			new Cannon().identify().collect();
			
			new TimeReverser().collect();
			
			new BackpackCleaner().collect();
			
			new LevelTeleporter().collect();
			
			new LazyTest().collect();
            new MapEditor().collect();
            
			new TestArmor().collect();
			new TestArtifact().collect();
			new TestMelee().collect();
			new TestMissile().collect();
			new TestRing().collect();
			new TestPotion().collect();
			new AttributeViewer().collect();
			
			new ScrollHolder().collect();
			Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
			
			new PotionBandolier().collect();
			Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
			
			new MagicalHolster().collect();
			Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();
			
			waterskin.fill();
			
			new WandOfReflectDisintegration().identify().collect();
		}

		new ScrollOfIdentify().identify();

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;

			case TYPE561:
				initType561( hero );
				break;

			case GSH18:
				initGSH18( hero );
				break;

			}

		for (int s = 0; s < QuickSlot.SIZE; s++){
			if (Dungeon.quickslot.getItem(s) == null){
				Dungeon.quickslot.setSlot(s, waterskin);
				break;
			}
		}

		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			new SmallRation().collect();
		}

	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
			case TYPE561:
				return Badges.Badge.MASTERY_TYPE561;
			case GSH18:
				return Badges.Badge.MASTERY_GSH18;
	}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new Ump45()).identify();
		new PotionOfHealing().identify().collect();
		new PotionOfStrength().identify().collect();
		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}

		ThrowingStone stone = new ThrowingStone();
		stone.identify().quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stone);
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;
		staff = new MagesStaff(new WandOfMagicMissile());
		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);
		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfRecharging().identify().collect();

		new ScrollOfUpgrade().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Welrod()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new PotionOfInvisibility().identify().collect();
		new ScrollOfMagicMapping().identify();
	}

	private static void initHuntress( Hero hero ) {
		(hero.belongings.weapon = new M9()).identify();
		
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();
		Dungeon.quickslot.setSlot(0, bow);
		
		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
	}

	private static void initType561( Hero hero ) {
		Gun561 gun561=new Gun561();
		(hero.belongings.weapon=gun561).identify();
		hero.belongings.weapon.activate(hero);

		RedBook redBook = new RedBook();
		(hero.belongings.artifact=redBook).identify();
		hero.belongings.artifact.activate(hero);

		Dungeon.quickslot.setSlot(0,redBook);
		Dungeon.quickslot.setSlot(1,gun561);

        new ScrollOfTerror().identify().collect();
		new SaltyZongzi().collect();
        new SugarZongzi().collect();
	}
	
	private static void initGSH18( Hero hero ) {
		// 使用GSH18作为初始武器
		(hero.belongings.weapon = new GSH18()).identify();
		new PotionOfHealing().identify().collect();

		// 开局获得疫苗磁盘
		new ScrollOfRemoveCurse().identify().collect();
		Dungeon.quickslot.setSlot(0, hero.belongings.getItem(ScrollOfRemoveCurse.class));
	}

	public String title() {
		return Messages.get(HeroClass.class, name());
	}

	public String desc(){
		return Messages.get(HeroClass.class, name()+"_desc");
	}

	public HeroSubClass[] subClasses() {
		return subClasses;
	}

	public ArmorAbility[] armorAbilities(){
		switch (this) {
			case WARRIOR: default:
				return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()};
			case MAGE:
				return new ArmorAbility[]{new ElementalBlast(), new WildMagic(), new WarpBeacon()};
			case ROGUE:
				return new ArmorAbility[]{new SmokeBomb(), new DeathMark(), new ShadowClone()};
			case HUNTRESS:
				return new ArmorAbility[]{new SpectralBlades(), new NaturesPower(), new SpiritHawk()};
			case TYPE561:
				return new ArmorAbility[]{new Type56FourOne(), new Type56FourTwo()};
			case GSH18:
				return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()}; // 使用战士的技能
	}
	}

	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Assets.Sprites.WARRIOR;
			case MAGE:
				return Assets.Sprites.MAGE;
			case ROGUE:
				return Assets.Sprites.ROGUE;
			case HUNTRESS:
				return Assets.Sprites.HK416;
				//return Assets.Sprites.HUNTRESS;
			case TYPE561:
				return Assets.Sprites.TYPE561;
			case GSH18:
				return Assets.Sprites.GSH18; // 使用正确的GSH18精灵
		}
	}

	public String splashArt(){
		switch (this) {
			case WARRIOR: default:
				return Assets.Splashes.WARRIOR;
			case MAGE:
				return Assets.Splashes.MAGE;
			case ROGUE:
				return Assets.Splashes.ROGUE;
			case HUNTRESS:
				return Assets.Splashes.HUNTRESS;
			case TYPE561:
				return Assets.Splashes.HUNTRESS; // 暂时使用HUNTRESS的溅落效果
			case GSH18:
				return Assets.Splashes.HUNTRESS; // 暂时使用HUNTRESS的溅落效果
		}
	}
	
	public String[] perks() {
		switch (this) {
			case WARRIOR: default:
				return new String[]{
						Messages.get(HeroClass.class, "warrior_perk1"),
						Messages.get(HeroClass.class, "warrior_perk2"),
						Messages.get(HeroClass.class, "warrior_perk3"),
						Messages.get(HeroClass.class, "warrior_perk4"),
						Messages.get(HeroClass.class, "warrior_perk5"),
				};
			case MAGE:
				return new String[]{
						Messages.get(HeroClass.class, "mage_perk1"),
						Messages.get(HeroClass.class, "mage_perk2"),
						Messages.get(HeroClass.class, "mage_perk3"),
						Messages.get(HeroClass.class, "mage_perk4"),
						Messages.get(HeroClass.class, "mage_perk5"),
				};
			case ROGUE:
				return new String[]{
						Messages.get(HeroClass.class, "rogue_perk1"),
						Messages.get(HeroClass.class, "rogue_perk2"),
						Messages.get(HeroClass.class, "rogue_perk3"),
						Messages.get(HeroClass.class, "rogue_perk4"),
						Messages.get(HeroClass.class, "rogue_perk5"),
				};
			case HUNTRESS:
				return new String[]{
						Messages.get(HeroClass.class, "huntress_perk1"),
						Messages.get(HeroClass.class, "huntress_perk2"),
						Messages.get(HeroClass.class, "huntress_perk3"),
						Messages.get(HeroClass.class, "huntress_perk4"),
						Messages.get(HeroClass.class, "huntress_perk5"),
				};
			case TYPE561:
			return new String[]{
					Messages.get(HeroClass.class, "type561_perk1"),
					Messages.get(HeroClass.class, "type561_perk2"),
					Messages.get(HeroClass.class, "type561_perk3"),
					Messages.get(HeroClass.class, "type561_perk4"),
					Messages.get(HeroClass.class, "type561_perk5"),
			};
		case GSH18:
			return new String[]{
					Messages.get(HeroClass.class, "warrior_perk1"),
					Messages.get(HeroClass.class, "warrior_perk2"),
					Messages.get(HeroClass.class, "warrior_perk3"),
					Messages.get(HeroClass.class, "warrior_perk4"),
					"初始获得两瓶治疗药水", // 天赋效果
			};
	}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;
		
		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
			case TYPE561:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_TYPE561);
			case GSH18:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_GSH18);
	}
	}
	
	public String unlockMsg() {
		switch (this){
			case WARRIOR: default:
				return "";
			case MAGE:
				return Messages.get(HeroClass.class, "mage_unlock");
			case ROGUE:
				return Messages.get(HeroClass.class, "rogue_unlock");
			case HUNTRESS:
				return Messages.get(HeroClass.class, "huntress_unlock");
			case TYPE561:
				return Messages.get(HeroClass.class, "type561_unlock");
			case GSH18:
				return Messages.get(HeroClass.class, "gsh18_unlock");
	}
	}
}
