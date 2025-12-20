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

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfLightning extends DamageWand {

	{
		image = ItemSpriteSheet.WAND_LIGHTNING;
	}
	
	private ArrayList<Char> affected = new ArrayList<>();

	private ArrayList<Lightning.Arc> arcs = new ArrayList<>();

	public int min(int lvl){
		return 5+lvl;
	}

	public int max(int lvl){
		return 10+5*lvl;
	}
	
	@Override
	public void onZap(Ballistica bolt) {

        for(Char ch : this.affected.toArray(new Char[0])) {
            if (ch != curUser && ch.alignment == curUser.alignment && ch.pos != bolt.collisionPos) {
                this.affected.remove(ch);
            } else if (ch.buff(LightningCharge.class) != null) {
                this.affected.remove(ch);
            }
        }

		//lightning deals less damage per-target, the more targets that are hit.
		float multipler = 0.4f + (0.6f/affected.size());
		//if the main target is in water, all affected take full damage
		if (Dungeon.level.water[bolt.collisionPos]) multipler = 1f;

		for (Char ch : affected){
			if (ch == Dungeon.hero) Camera.main.shake( 2, 0.3f );
			ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
			ch.sprite.flash();

			wandProc(ch, chargesPerCast());
			if (ch == curUser) {
				ch.damage(Math.round(damageRoll() * multipler * 0.5f), this);
			} else {
				ch.damage(Math.round(damageRoll() * multipler), this);
			}
		}

		if (!curUser.isAlive()) {
			Dungeon.fail( getClass() );
			GLog.n(Messages.get(this, "ondeath"));
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        float procChance = ((float)this.buffedLvl() + 1.0F) / ((float)this.buffedLvl() + 4.0F) * procChanceMultiplier(attacker);
        if (Random.Float() < procChance) {
            float powerMulti = Math.min(1.0F, procChance);
            FlavourBuff.prolong(attacker, LightningCharge.class, powerMulti * WandOfLightning.LightningCharge.DURATION);
            //战法获得对电杖的增益buff，这里的注释暂且沿用破碎的命名“起电”
            attacker.sprite.centerEmitter().burst(SparkParticle.FACTORY, 10);
            attacker.sprite.flash();
            Sample.INSTANCE.play("sounds/lightning.mp3");
        }

    }

	private void arc( Char ch ) {
        //增加内容：战法获取“起电”之后释放电杖的增益
        int dist = Dungeon.level.water[ch.pos] ? 2 : 1;
        if (curUser.buff(LightningCharge.class) != null) {
            ++dist;
        }

        ArrayList<Char> hitThisArc = new ArrayList();
        PathFinder.buildDistanceMap(ch.pos, BArray.not(Dungeon.level.solid, (boolean[])null), dist);

        for(int i = 0; i < PathFinder.distance.length; ++i) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                Char n = Actor.findChar(i);
                if ((n != Dungeon.hero || PathFinder.distance[i] <= 1) && n != null && !this.affected.contains(n)) {
                    hitThisArc.add(n);
                }
            }
        }

        this.affected.addAll(hitThisArc);

        for(Char hit : hitThisArc) {
            this.arcs.add(new Lightning.Arc(ch.sprite.center(), hit.sprite.center()));
            this.arc(hit);
        }

    }
	
	@Override
	public void fx(Ballistica bolt, Callback callback) {

		affected.clear();
		arcs.clear();

		int cell = bolt.collisionPos;

		Char ch = Actor.findChar( cell );
		if (ch != null) {
			affected.add( ch );
			arcs.add( new Lightning.Arc(curUser.sprite.center(), ch.sprite.center()));
			arc(ch);
		} else {
			arcs.add( new Lightning.Arc(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(bolt.collisionPos)));
			CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
		}

		//don't want to wait for the effect before processing damage.
		curUser.sprite.parent.addToFront( new Lightning( arcs, null ) );
		Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color(0xFFFFFF);
		particle.am = 0.6f;
		particle.setLifespan(0.6f);
		particle.acc.set(0, +10);
		particle.speed.polar(-Random.Float(3.1415926f), 6f);
		particle.setSize(0f, 1.5f);
		particle.sizeJitter = 1f;
		particle.shuffleXY(1f);
		float dst = Random.Float(1f);
		particle.x -= dst;
		particle.y += dst;
	}

    public static class LightningCharge extends FlavourBuff {
        //“起电”buff
        public static float DURATION = 10.0F;

        public LightningCharge() {
            this.type = buffType.POSITIVE;
        }

        public int icon() {
            //“起电”buff的图标，沿用破碎的隐身图标变色
            // 这里根据龙血、毒粹的图标我有想过使用充能的图标，但这可能会误导玩家，所以沿用了破碎的方案
            return BuffIndicator.INVISIBLE;
        }

        public void tintIcon(Image icon) {
            icon.hardlight(1.0F, 1.0F, 0.0F);
        }
    }
}
