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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GolyatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GolyatFactorySprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GolyatFactory extends Mob {
	
	{
		spriteClass = GolyatFactorySprite.class;
		
		HP = HT = 40;
		defenseSkill = 14;
		baseSpeed=1;
		EXP = 7;
		maxLvl = 14;
		
		loot = new PotionOfHealing();
		lootChance = 0.2f; //see lootChance()
		
		properties.add(Property.UNDEAD);
		
		HUNTING = new Hunting();
	}
	
	public boolean summoning = false;
	public int summoningPos = -1;
	
	protected boolean firstSummon = true;
	
	private FactoryGolyat myGolyat;
	private int storedGolyatID = -1;

	@Override
	protected boolean act() {
		if (summoning && state != HUNTING){
			summoning = false;
			if (sprite instanceof GolyatFactorySprite) ((GolyatFactorySprite) sprite).cancelSummoning();
		}
		return super.act();
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}
	
	@Override
	public float lootChance() {
		return super.lootChance() * ((6f - Dungeon.LimitedDrops.FACTORY_HP.count) / 6f);
	}
	
	@Override
	public Item createLoot(){
		Dungeon.LimitedDrops.FACTORY_HP.count++;
		return super.createLoot();
	}
	
	@Override
	public void die(Object cause) {
		if (storedGolyatID != -1){
			Actor ch = Actor.findById(storedGolyatID);
			storedGolyatID = -1;
			if (ch instanceof FactoryGolyat){
				myGolyat = (FactoryGolyat) ch;
			}
		}
		
		if (myGolyat != null && myGolyat.isAlive()){
			myGolyat.die(null);
		}
		
		super.die(cause);
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return false;
	}

	private static final String SUMMONING = "summoning";
	private static final String FIRST_SUMMON = "first_summon";
	private static final String SUMMONING_POS = "summoning_pos";
	private static final String MY_Golyat = "my_Golyat";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( SUMMONING, summoning );
		bundle.put( FIRST_SUMMON, firstSummon );
		if (summoning){
			bundle.put( SUMMONING_POS, summoningPos);
		}
		if (myGolyat != null){
			bundle.put( MY_Golyat, myGolyat.id() );
		} else if (storedGolyatID != -1){
			bundle.put( MY_Golyat, storedGolyatID );
		}
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		summoning = bundle.getBoolean( SUMMONING );
		if (bundle.contains(FIRST_SUMMON)) firstSummon = bundle.getBoolean(FIRST_SUMMON);
		if (summoning){
			summoningPos = bundle.getInt( SUMMONING_POS );
		}
		if (bundle.contains( MY_Golyat )){
			storedGolyatID = bundle.getInt( MY_Golyat );
		}
	}
	
	public void onZapComplete(){
		if (myGolyat == null || myGolyat.sprite == null || !myGolyat.isAlive()){
			return;
		}
		
		//heal Golyat first
		if (myGolyat.HP < myGolyat.HT){

			if (sprite.visible || myGolyat.sprite.visible) {
				sprite.parent.add(new Beam.HealthRay(sprite.center(), myGolyat.sprite.center()));
			}
			
			myGolyat.HP = Math.min(myGolyat.HP + 5, myGolyat.HT);
			if (myGolyat.sprite.visible) myGolyat.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			
		//otherwise give it adrenaline
		} else if (myGolyat.buff(Adrenaline.class) == null) {

			if (sprite.visible || myGolyat.sprite.visible) {
				sprite.parent.add(new Beam.HealthRay(sprite.center(), myGolyat.sprite.center()));
			}
			
			Buff.affect(myGolyat, Adrenaline.class, 3f);
		}
		
		next();
	}

	public void summonMinion(){
		if (Actor.findChar(summoningPos) != null) {
			int pushPos = pos;
			for (int c : PathFinder.NEIGHBOURS8) {
				if (Actor.findChar(summoningPos + c) == null
						&& Dungeon.level.passable[summoningPos + c]
						&& (Dungeon.level.openSpace[summoningPos + c] || !hasProp(Actor.findChar(summoningPos), Property.LARGE))
						&& Dungeon.level.trueDistance(pos, summoningPos + c) > Dungeon.level.trueDistance(pos, pushPos)) {
					pushPos = summoningPos + c;
				}
			}

			//push enemy, or wait a turn if there is no valid pushing position
			if (pushPos != pos) {
				Char ch = Actor.findChar(summoningPos);
				Actor.addDelayed( new Pushing( ch, ch.pos, pushPos ), -1 );

				ch.pos = pushPos;
				Dungeon.level.occupyCell(ch );

			} else {

				Char blocker = Actor.findChar(summoningPos);
				if (blocker.alignment != alignment){
					blocker.damage( Random.NormalIntRange(2, 10), this );
				}

				spend(TICK);
				return;
			}
		}

		summoning = firstSummon = false;

		myGolyat = new FactoryGolyat();
		myGolyat.pos = summoningPos;
		GameScene.add( myGolyat );
		Dungeon.level.occupyCell( myGolyat );
		((GolyatFactorySprite)sprite).finishSummoning();

		for (Buff b : buffs(AllyBuff.class)){
			Buff.affect(myGolyat, b.getClass());
		}
		for (Buff b : buffs(ChampionEnemy.class)){
			Buff.affect( myGolyat, b.getClass());
		}
	}
	
	private class Hunting extends Mob.Hunting{
		
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;
			
			if (storedGolyatID != -1){
				Actor ch = Actor.findById(storedGolyatID);
				storedGolyatID = -1;
				if (ch instanceof FactoryGolyat){
					myGolyat = (FactoryGolyat) ch;
				}
			}
			
			if (summoning){
				summonMinion();
				return true;
			}
			
			if (myGolyat != null &&
					(!myGolyat.isAlive()
					|| !Dungeon.level.mobs.contains(myGolyat)
					|| myGolyat.alignment != alignment)){
				myGolyat = null;
			}
			
			//if enemy is seen, and enemy is within range, and we haven no Golyat, summon a Golyat!
			if (enemySeen && Dungeon.level.distance(pos, enemy.pos) <= 4 && myGolyat == null){
				
				summoningPos = -1;
				for (int c : PathFinder.NEIGHBOURS8){
					if (Actor.findChar(enemy.pos+c) == null
							&& Dungeon.level.passable[enemy.pos+c]
							&& fieldOfView[enemy.pos+c]
							&& Dungeon.level.trueDistance(pos, enemy.pos+c) < Dungeon.level.trueDistance(pos, summoningPos)){
						summoningPos = enemy.pos+c;
					}
				}
				
				if (summoningPos != -1){
					
					summoning = true;
					sprite.zap( summoningPos );
					
					spend( firstSummon ? TICK : 2*TICK );
				} else {
					//wait for a turn
					spend(TICK);
				}
				
				return true;
			//otherwise, if enemy is seen, and we have a Golyat...
			} else if (enemySeen && myGolyat != null){
				
				target = enemy.pos;
				spend(TICK);
				
				if (!fieldOfView[myGolyat.pos]){
					
					//if the Golyat is not next to the enemy
					//teleport them to the closest spot next to the enemy that can be seen
					if (!Dungeon.level.adjacent(myGolyat.pos, enemy.pos)){
						int telePos = -1;
						for (int c : PathFinder.NEIGHBOURS8){
							if (Actor.findChar(enemy.pos+c) == null
									&& Dungeon.level.passable[enemy.pos+c]
									&& fieldOfView[enemy.pos+c]
									&& Dungeon.level.trueDistance(pos, enemy.pos+c) < Dungeon.level.trueDistance(pos, telePos)){
								telePos = enemy.pos+c;
							}
						}
						
						if (telePos != -1){
							
							ScrollOfTeleportation.appear(myGolyat, telePos);
							myGolyat.teleportSpend();
							
							if (sprite != null && sprite.visible){
								sprite.zap(telePos);
								return false;
							} else {
								onZapComplete();
							}
						}
					}
					
					return true;
					
				} else {
					
					//zap Golyat
					if (myGolyat.HP < myGolyat.HT || myGolyat.buff(Adrenaline.class) == null) {
						if (sprite != null && sprite.visible){
							sprite.zap(myGolyat.pos);
							return false;
						} else {
							onZapComplete();
						}
					}
					
				}
				
				return true;
				
			//otherwise, default to regular hunting behaviour
			} else {
				return super.act(enemyInFOV, justAlerted);
			}
		}
	}
	
	public static class FactoryGolyat extends Golyat {
		
		{
			state = WANDERING;
			
			spriteClass = FactoryGolyatSprite.class;
			
			//no loot or exp
			maxLvl = -5;
			
			//20/25 health to start
			HP = 20;
		}

		@Override
		public float spawningWeight() {
			return 0;
		}

		private void teleportSpend(){
			spend(TICK);
		}
		
		public static class FactoryGolyatSprite extends GolyatSprite{
			
			public FactoryGolyatSprite(){
				super();
				brightness(0.75f);
			}
			
			@Override
			public void resetColor() {
				super.resetColor();
				brightness(0.75f);
			}
		}
		
	}
}
