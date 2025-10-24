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

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Food extends Item {

	public static final float TIME_TO_EAT	= 3f;
	
	public static final String AC_EAT	= "EAT";
	
	public float energy = Hunger.HUNGRY;
	
	{
		stackable = true;
		image = ItemSpriteSheet.RATION;

		defaultAction = AC_EAT;

		bones = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_EAT )) {
			
			detach( hero.belongings.backpack );

			satisfy(hero);
			GLog.i( Messages.get(this, "eat_msg") );
			
			hero.sprite.operate( hero.pos );
			hero.busy();
			SpellSprite.show( hero, SpellSprite.FOOD );
			Sample.INSTANCE.play( Assets.Sounds.EAT );
			
			hero.spend( eatingTime() );

			Talent.onFoodEaten(hero, energy, this);
			
			Statistics.foodEaten++;
			Badges.validateFoodEaten();
			
		}
	}

	public static float eatingTimeStatic(){
		if(Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
		|| Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
		|| Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
		|| Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)
		|| Dungeon.hero.hasTalent(Talent.BARGAIN_SKILLS)
		|| Dungeon.hero.hasTalent(Talent.GSH18_ENERGIZING_MEAL)){
			return TIME_TO_EAT - 2;
		} else {
			return TIME_TO_EAT;
		}
	}

	protected float eatingTime(){
		return eatingTimeStatic();
	}
	
	protected void satisfy( Hero hero ){
		float buffedEnergy=energy;

		if(hero.hasTalent(Talent.NICE_FOOD)&&hero.isStarving()){
			buffedEnergy+=50f*hero.pointsInTalent(Talent.NICE_FOOD);
		}

		if(hero.hasTalent(Talent.BETTER_FOOD)&&this instanceof SaltyZongzi){
			int heal=(int)(hero.HT*(0.08f*hero.pointsInTalent(Talent.BETTER_FOOD)-0.06f));
			hero.HP=Math.min(hero.HP+heal,hero.HT);
			hero.sprite.emitter().burst(Speck.factory(Speck.HEALING),hero.pointsInTalent(Talent.BETTER_FOOD));
			buffedEnergy+=30f+10f*hero.pointsInTalent(Talent.BETTER_FOOD);
		}

		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			buffedEnergy/=3f;
		}

		Buff.affect(hero, Hunger.class).satisfy(buffedEnergy);
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public int value() {
		return 20*quantity;
	}
}
