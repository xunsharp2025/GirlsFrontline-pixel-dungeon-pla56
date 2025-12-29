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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.type561;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Wound;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gun562;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Type56FourOne extends ArmorAbility {

	{
		baseChargeUse = 60f;
	}


	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {

		if (target == null){
			return;
		}


		Char ch = Actor.findChar(target);

		if (ch == null) {
			GLog.w(Messages.get(this, "no_target"));
			return;
		} else if (ch == hero){
            GLog.w(Messages.get(this, "self_target"));
            return;
		} else if (ch.alignment != Char.Alignment.ENEMY || !(ch instanceof Mob) ){
			GLog.w(Messages.get(this, "cant_attack"));
			return;
		}else if(Dungeon.level.distance(hero.pos, target)>1+ hero.pointsInTalent(Talent.Type56FourOneOne)){
            GLog.w(Messages.get(this, "cant_reach"));
            return;
        }/* else if (Char.hasProp(ch, Char.Property.MINIBOSS) || Char.hasProp(ch, Char.Property.BOSS)){
			GLog.w(Messages.get(this, "too_strong"));
			return;
		}*/ else {
            if(Dungeon.level.distance(hero.pos, target)>1){
                tpTarget(target);
            }

            int time = 1+ hero.pointsInTalent(Talent.Type56FourOneTwo);
            Wound.hit( ch );
            ch.damage(damageRoll(),this);
            Buff.affect( ch, Bleeding.class ).set( 5*(1+time) );
            Buff.prolong( ch, Cripple.class, time );
		}
        if(hero.pointsInTalent(Talent.Type56FourOneThree)>0){
            if(Random.Int(100)<(5+10*hero.pointsInTalent(Talent.Type56FourOneThree))){
                ch.damage(3*hero.pointsInTalent(Talent.Type56FourOneThree),this);
//                Buff.affect( ch, Terror.class, 10 ).object = hero.id();
            }
        }
		armor.charge -= chargeUse(hero);
        //消耗充能
		armor.updateQuickslot();
        //更新快捷栏
		Invisibility.dispel();
        //去除隐形
		hero.spendAndNext(0);
        //消耗回合
	}
    private int damageRoll() {
        KindOfWeapon type562 = hero.belongings.getItem(Gun562.class);
        KindOfWeapon wep = hero.belongings.weapon();
        int dmg;
        if(type562!=null){
            dmg = type562.damageRoll( hero );
        }
        else if (wep != null) {
            dmg = wep.damageRoll( hero);
        } else {
            dmg = RingOfForce.damageRoll(hero);
        }
        if (dmg < 0) dmg = 0;

        return dmg;
    }
    private void tpTarget(int cell){

        PathFinder.buildDistanceMap(Dungeon.hero.pos, BArray.not(Dungeon.level.solid, null), 1+ hero.pointsInTalent(Talent.Type56FourOneOne));
        int dest = -1;
        for (int i : PathFinder.NEIGHBOURS8){
            //cannot blink into a cell that's occupied or impassable, only over them
            if (Actor.findChar(cell+i) != null)     continue;
            if (!Dungeon.level.passable[cell+i])    continue;

            if (dest == -1 || PathFinder.distance[dest] > PathFinder.distance[cell+i]){
                dest = cell+i;
                //if two cells have the same pathfinder distance, prioritize the one with the closest true distance to the hero
            } else if (PathFinder.distance[dest] == PathFinder.distance[cell+i]){
                if (Dungeon.level.trueDistance(Dungeon.hero.pos, dest) > Dungeon.level.trueDistance(Dungeon.hero.pos, cell+i)){
                    dest = cell+i;
                }
            }

        }

        if (dest == -1 || PathFinder.distance[dest] == Integer.MAX_VALUE || Dungeon.hero.rooted){
            GLog.w(Messages.get(this, "out_of_reach"));
            return;
        }

        Dungeon.hero.pos = dest;
        Dungeon.level.occupyCell(Dungeon.hero);
        //prevents the hero from being interrupted by seeing new enemies
        Dungeon.observe();
        GameScene.updateFog();
        Dungeon.hero.checkVisibleMobs();

        Dungeon.hero.sprite.place( Dungeon.hero.pos );
        Dungeon.hero.sprite.turnTo( Dungeon.hero.pos, cell);
        CellEmitter.get( Dungeon.hero.pos ).burst( Speck.factory( Speck.WOOL ), 6 );
        Sample.INSTANCE.play( Assets.Sounds.PUFF );

    }

	@Override
	public int icon() {
		return HeroIcon.RATMOGRIFY;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{ Talent.Type56FourOneOne, Talent.Type56FourOneTwo, Talent.Type56FourOneThree, Talent.HEROIC_ENERGY};
	}

}
