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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Gun561 extends ShootGun {
	public static final String AC_CD = "CD";
	{
		curCharges = 0;
		maxCharges = 1;
		useMissileSprite = false;
		effectIndex = 2;
		RCH = 2;
		shootPrompt = Messages.get(this, "prompt");
		needEquip = false;
		image = ItemSpriteSheet.GUN561;
	}

	@Override
	public int min(int lvl) {
		return curCharges == 0 ?  2 + lvl : 1;
	}
	@Override
	public int max(int lvl) {
		return curCharges == 0 ?  6 + lvl * 2 : 2;
	}

	@Override
	public boolean reload() {
		if (curCharges < maxCharges) {
			curUser.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "reload") );
			curCharges++;

			curUser.busy();
			curUser.sprite.operate( curUser.pos );
			updateQuickslot();
			return true;
		} else {
			GLog.w(Messages.get(this, "full"));
			return false;
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		//动作列表
		ArrayList<String> actions = super.actions(hero);
		if (Dungeon.hero.buff(Cooldown.class) != null) {
			actions.remove(AC_RELOAD);
			actions.remove(AC_SHOOT);
			actions.add(AC_CD);
		} else {
			actions.remove(AC_CD);
		}
		return actions;
	}

	@Override
	public String defaultAction(){
		if (Dungeon.hero.buff(Cooldown.class) != null && curCharges > 0 || curCharges > 0) {
            defaultAction = AC_SHOOT;
        } else if (Dungeon.hero.buff(Cooldown.class) != null) {
			defaultAction = AC_CD;
		} else {
			defaultAction = AC_RELOAD;
        }
		return defaultAction;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_CD)) {
			Cooldown cooldown = hero.buff(Cooldown.class);
			int timeLeft = (cooldown != null) ? (int)cooldown.cooldown() : 0;
			GLog.n(Messages.get(this, "cd_status", timeLeft));
		}
	}


	@Override
	public void onShootComplete(int cell) {
		//播放音效
		Sample.INSTANCE.play(Assets.Sounds.BLAST);

		//处理地形和物品互动
		//爆炸特效
		if (Dungeon.level.heroFOV[cell]) {
			CellEmitter.center(cell).burst(BlastParticle.FACTORY,30);
		}
		for (int n : PathFinder.NEIGHBOURS9) {
			int c =cell + n;
			if (c >= 0 && c < Dungeon.level.length()) {
				if (Dungeon.level.heroFOV[c]) {
					CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
				}

				if (Dungeon.level.flamable[c]) {
					Dungeon.level.destroy(c);
					GameScene.updateMap(c);
				}

				// destroys items / triggers bombs caught in the blast.
				Heap heap = Dungeon.level.heaps.get(c);
				if (heap != null)
					heap.explode();

				Char target = Actor.findChar(c);
				if (target != null) {
					// 如果是中心地格，则施加完整伤害
					// 如果不是中心地格，则施加四分之三的伤害
					int damage = n == 0 ?
							Random.NormalIntRange(Dungeon.hero.HT/2+5,Dungeon.hero.HT/2+8) :
							(int) (Random.NormalIntRange(Dungeon.hero.HT / 2 + 5, Dungeon.hero.HT / 2 + 8) * 0.75f);
					target.damage(damage, this);

					if (target == Dungeon.hero && !target.isAlive())
						Dungeon.fail(getClass());
				}
				if(Dungeon.hero.buff(Cooldown.class)==null){
					Hero hero=Dungeon.hero;
					float coolDownTurns=200f;
					if(hero.hasTalent(Talent.FAST_RELOAD)){
						coolDownTurns-=20*hero.pointsInTalent(Talent.FAST_RELOAD);
					}
					Buff.affect(hero, Cooldown.class,coolDownTurns);
				}
			}
		}

		super.onShootComplete(cell);
	}


	@Override
	public void activate(Char ch) {
		//
	}

	@Override
	public void onDetach( ) {
		//
	}

	public String desc(){
		// 获取当前实际冷却剩余时间
		int currentCooldown = 0;
		Cooldown cooldownBuff = Dungeon.hero.buff(Cooldown.class);
		if (Dungeon.hero != null && cooldownBuff != null) {
			currentCooldown = (int)cooldownBuff.cooldown();
		}
		int displayTime = currentCooldown > 0 ? currentCooldown : 0;
		return Messages.get(this, "desc", displayTime);
	};

	public static class Cooldown extends FlavourBuff {
        {
            type = buffType.NEGATIVE;
        }
    }
}
