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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Empulse;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Gun561 extends ShootGun {
	{
		image = ItemSpriteSheet.GUN561;
		RCH = 2;
	}

	@Override
	public int min(int lvl) {
		return hasCharge ? 1 :2+lvl  ;
	}
	@Override
	public int max(int lvl) {
		return hasCharge ? 2 :6+lvl*2;
	}

	@Override
	public void onShootComplete(int cell){
		float rate=1f;
		float duration=0f;

		if(Dungeon.hero.subClass==HeroSubClass.PULSETROOPER){
			rate=Dungeon.hero.pointsInTalent(Talent.MORE_POWER)/6f;

			Sample.INSTANCE.play( Assets.Sounds.LIGHTNING);

			switch(Dungeon.hero.pointsInTalent(Talent.ENDURE_EMP)){
				case 0:default:duration=5f;break;
				case 1:        duration=8f;break;
				case 2:        duration=11f;break;
				case 3:        duration=15f;break;
			}
		}

		if(rate>0.01f){
			Sample.INSTANCE.play(Assets.Sounds.BLAST);
			if (Dungeon.level.heroFOV[cell]) {
				CellEmitter.get(cell).burst(BlastParticle.FACTORY,30);
			}
		}

		for(int n : PathFinder.NEIGHBOURS9) {
			int c =cell + n;
			if (c >= 0 && c < Dungeon.level.length()) {
				Char target = Actor.findChar(c);

				if(target!=null&&duration>0.01f){
					Buff.prolong(target,Empulse.class,duration);
					CellEmitter.get(c).burst(EnergyParticle.FACTORY, 10);
				}

				if(rate<=0.01f){
					continue;
				}

				if (Dungeon.level.flamable[c]) {
					Dungeon.level.destroy(c);
					GameScene.updateMap(c);
				}

				// destroys items / triggers bombs caught in the blast.
				Heap heap = Dungeon.level.heaps.get(c);
				if (heap != null)
					heap.explode();

				if (Dungeon.level.heroFOV[c]) {
					CellEmitter.get(c).burst(SmokeParticle.FACTORY,4);
				}

				if (target != null) {
					int damage=Random.NormalIntRange(Dungeon.hero.HT/2+5,Dungeon.hero.HT/2+8);
					rate*=(n==0?1f:0.75f);
					target.damage((int)(damage*rate),this);
				}
			}
		}

		if(!Dungeon.hero.isAlive()){
			Dungeon.fail(getClass());
		}

		Hero hero=Dungeon.hero;
		cooldownTurns=200-20*hero.pointsInTalent(Talent.FAST_RELOAD);

		if(hero.hasTalent(Talent.SIMPLE_RELOAD)){
			cooldownTurns-=-5+25*hero.pointsInTalent(Talent.SIMPLE_RELOAD);
		}

		super.onShootComplete(cell);
	}
}
