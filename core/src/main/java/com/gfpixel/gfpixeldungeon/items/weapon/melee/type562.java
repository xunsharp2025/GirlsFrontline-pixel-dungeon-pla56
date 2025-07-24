/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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
package com.gfpixel.gfpixeldungeon.items.weapon.melee;
import static com.gfpixel.gfpixeldungeon.items.wands.Wand.zapper;

import com.gfpixel.gfpixeldungeon.actors.Actor;
import com.gfpixel.gfpixeldungeon.actors.Char;
import com.gfpixel.gfpixeldungeon.actors.buffs.Buff;
import com.gfpixel.gfpixeldungeon.actors.buffs.Invisibility;
import com.gfpixel.gfpixeldungeon.actors.buffs.Recharging;
import com.gfpixel.gfpixeldungeon.effects.SpellSprite;
import com.gfpixel.gfpixeldungeon.items.Bomb;
import com.gfpixel.gfpixeldungeon.items.wands.CursedWand;
import com.gfpixel.gfpixeldungeon.items.weapon.melee.G11;
import com.gfpixel.gfpixeldungeon.mechanics.Ballistica;
import com.gfpixel.gfpixeldungeon.messages.Messages;
import com.gfpixel.gfpixeldungeon.scenes.CellSelector;
import com.gfpixel.gfpixeldungeon.sprites.ItemSpriteSheet;
import com.gfpixel.gfpixeldungeon.ui.QuickSlotButton;
import com.gfpixel.gfpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.gfpixel.gfpixeldungeon.items.wands.Wand;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.gfpixel.gfpixeldungeon.Assets;
import com.gfpixel.gfpixeldungeon.Dungeon;
import com.gfpixel.gfpixeldungeon.actors.buffs.LockedFloor;
import com.gfpixel.gfpixeldungeon.actors.buffs.SoulMark;
import com.gfpixel.gfpixeldungeon.actors.hero.Hero;
import com.gfpixel.gfpixeldungeon.actors.hero.HeroClass;
import com.gfpixel.gfpixeldungeon.actors.hero.HeroSubClass;
import com.gfpixel.gfpixeldungeon.effects.MagicMissile;
import com.gfpixel.gfpixeldungeon.effects.particles.StaffParticle;
import com.gfpixel.gfpixeldungeon.items.Item;
import com.gfpixel.gfpixeldungeon.items.bags.Bag;
import com.gfpixel.gfpixeldungeon.items.bags.MagicalHolster;
import com.gfpixel.gfpixeldungeon.items.rings.Ring;
import com.gfpixel.gfpixeldungeon.items.rings.RingOfEnergy;
import com.gfpixel.gfpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

import java.util.ArrayList;

public class type562 extends MeleeWeapon {
	private static final int USAGES_TO_KNOW    = 20;
	protected int usagesToKnow = USAGES_TO_KNOW;

	public int maxCharges = initialCharges();
	protected static int collisionProperties1;
	private boolean curChargeKnown = false;

	public float partialCharge = 0f;
	protected Charger charger;
	private static final float TIME_TO_ZAP	= 1f;
	protected int initialCharges() {
		return 2;
	}

	protected int chargesPerCast() {
		return 1;
	}


	public void staffFx( StaffParticle particle ){
		particle.color(0xFFFFFF); particle.am = 0.3f;
		particle.setLifespan( 1f);
		particle.speed.polar( Random.Float(PointF.PI2), 2f );
		particle.setSize( 1f, 2f );
		particle.radiateXY(0.5f);
	}

	protected void wandUsed() {
		usagesToKnow -= cursed ? 1 : chargesPerCast();
		curCharges -= cursed ? 1 : chargesPerCast();
		if (!isIdentified() && usagesToKnow <= 0) {
			identify();
			GLog.w( Messages.get(Wand.class, "identify", name()) );
		}
		curUser.spendAndNext( TIME_TO_ZAP );
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (curCharges > 0 || !curChargeKnown) {
			actions.add( AC_ZAP );
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_ZAP )) {

			curUser = hero;
			curItem = this;
			GameScene.selectCell( zapper );

		}
	}
	public static final String AC_ZAP	= "ZAP";

	{
		image = ItemSpriteSheet.type562;
		bones = false;
		usesTargeting = true;
		collisionProperties1 = Ballistica.STOP_TERRAIN | Ballistica.STOP_CHARS;
		unique = true;
		tier = 3;
		RCH = 3;
		ACC = 1f;
	}
	public int curCharges = maxCharges;
	{
		defaultAction = AC_ZAP;
	}
	@Override
	public int max(int lvl) {
		return  Math.round(1*(tier+2.5f)) + lvl*Math.round(tier+4);
	}
	private float damageStack = 0;
	private static final String STACK = "DamageStack";

	public int damageRoll(){
		return Random.NormalIntRange(min(), max());
	}

	public int damageRoll(int lvl){
		return Random.NormalIntRange(min(lvl), max(lvl));
	}

	public boolean collect( Bag container ) {
		if (super.collect( container )) {
			if (container.owner != null) {
				if (container instanceof MagicalHolster)
					charge( container.owner, ((MagicalHolster) container).HOLSTER_SCALE_FACTOR );
				else
					charge( container.owner );
			}
			return true;
		} else {
			return false;
		}
	}

	public void gainCharge( float amt ){
		partialCharge += amt;
		while (partialCharge >= 1) {
			curCharges = Math.min(maxCharges, curCharges+1);
			partialCharge--;
			updateQuickslot();
		}
	}

	public void charge( Char owner ) {
		if (charger == null) charger = new type562.Charger();
		charger.attachTo( owner );
	}

	public void charge( Char owner, float chargeScaleFactor ){
		charge( owner );
		charger.setScaleFactor( chargeScaleFactor );
	}

	public void onHit(G11 staff, Char attacker, Char defender, int damage) {
		Buff.prolong( attacker, Recharging.class, 1 + staff.level()/2f);
		SpellSprite.show(attacker, SpellSprite.CHARGE);
	}


	protected void onZap(Ballistica bolt) {

		Char ch = Actor.findChar( bolt.collisionPos );

		if (ch != null) {
			ch.damage(damageRoll(), this);
			ch.sprite.burst(0xFFFFFFFF, level() / 2 + 2);
		}

		new Bomb().explode(bolt.collisionPos, damageStack);
		damageStack = 0;
	}

	public int returnPlusDamage(){
		return (int)(damageStack * 100 + 100);
	}

	public class Charger extends Buff {

		protected static final float BASE_CHARGE_DELAY = 10f;
		protected static final float SCALING_CHARGE_ADDITION = 40f;
		protected static final float NORMAL_SCALE_FACTOR = 0.875f;

		protected static final float CHARGE_BUFF_BONUS = 0.25f;

		float scalingFactor = NORMAL_SCALE_FACTOR;

		@Override
		public boolean attachTo( Char target ) {
			super.attachTo( target );

			return true;
		}

		@Override
		public boolean act() {
			if (curCharges < maxCharges)
				recharge();

			if (partialCharge >= 1 && curCharges < maxCharges) {
				partialCharge--;
				curCharges++;
				updateQuickslot();
			}

			spend( TICK );

			return true;
		}

		protected void recharge(){
			int missingCharges = maxCharges - curCharges;
			missingCharges += Ring.getBonus(target, RingOfEnergy.Energy.class);
			missingCharges = Math.max(0, missingCharges);

			float turnsToCharge = (float) (BASE_CHARGE_DELAY
					+ (SCALING_CHARGE_ADDITION * Math.pow(scalingFactor, missingCharges)));

			LockedFloor lock = target.buff(LockedFloor.class);
			if (lock == null || lock.regenOn())
				partialCharge += 1f/turnsToCharge;

			for (Recharging bonus : target.buffs(Recharging.class)){
				if (bonus != null && bonus.remainder() > 0f) {
					partialCharge += CHARGE_BUFF_BONUS * bonus.remainder();
				}
			}
		}

		protected int initialCharges() {
			return 1;
		}
		public void updateLevel() {
			maxCharges = Math.min( initialCharges() + level(), 10 );
			curCharges = Math.min( curCharges, maxCharges );
		}
		private void setScaleFactor(float value){
			this.scalingFactor = value;
		}
	}
}
