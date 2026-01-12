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

import static com.shatteredpixel.shatteredpixeldungeon.items.Item.updateQuickslot;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EquipLevelUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;

public class Type56FourTwo extends ArmorAbility {

	{
		baseChargeUse = 60f;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
        Food.satisfy(hero, 150);
        if (hero.hasTalent(Talent.Type56FourTwoOne))
            Food.satisfy(hero, 10 + 10 * hero.pointsInTalent(Talent.Type56FourTwoOne));

        int left = 50+10*hero.pointsInTalent(Talent.Type56FourTwoOne);
        if (Dungeon.isChallenged(Challenges.NO_FOOD)){
            //150 turns if on diet is enabled
            left /= 3;
        }

        WellFed wellFed = Buff.affect(hero, WellFed.class);
//        if(wellFed.left<left)//这个判断是在饱腹剩余buff小于要添加的回合数时，把回合数加到饱腹上面
        wellFed.left += left; // 当前是不做判断直接增加饱腹回合

        ArtifactRecharge buff = Buff.affect(hero, ArtifactRecharge.class);
        if (buff.left() < 3 + (hero.pointsInTalent(Talent.Type56FourTwoThree))) {
            Buff.affect(hero, ArtifactRecharge.class).set(3 + (hero.pointsInTalent(Talent.Type56FourTwoThree))).ignoreHornOfPlenty = false;
        }
        //当前大充能的代码是玩家身上剩余回合小于要设置的回合时，才给予大充能并设置为对应的回合，如有需要可以改为在现有的基础上增加
        ScrollOfRecharging.chargeParticle(hero);
        //充能的粒子效果，如有需要可移除

//        EquipLevelUp lvlup = hero.buff(EquipLevelUp.class);
//        if(lvlup != null)
//            lvlup.detach();
        Buff.affect(hero, EquipLevelUp.class, 10);
        //FlavourBuff比buff用着手顺，可以直接设置生效的回合数，但是会累计回合数
        //当前连续使用会叠加回合数，如果不想叠加，把上面那三行代码取消注释即可

		armor.charge -= chargeUse(hero);
        //消耗充能
		updateQuickslot();
        //更新快捷栏
		Invisibility.dispel();
        //去除隐形
		hero.spendAndNext(Actor.TICK);
        //消耗回合
	}

	@Override
	public int icon() {
		return HeroIcon.RATMOGRIFY;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{ Talent.Type56FourTwoOne, Talent.Type56FourTwoTwo, Talent.Type56FourTwoThree, Talent.HEROIC_ENERGY};
	}

}
