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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LR;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class GSH18 extends LongRange {

	// 添加模式切换的常量
	private static final String AC_SWITCH_MODE = "SWITCH_MODE";
	// 存储当前模式状态，默认为false（普通模式）
	private boolean armorPenetrationMode = false;

	{
		image = ItemSpriteSheet.GSH18;

		tier = 1; // 设置为1级武器
		RCH = 2; // 默认射程2格
	}

	@Override
	public int min(int lvl) {
		return 1 + // 基础最小值1
				lvl;   // 每级成长1
	}

	@Override
	public int max(int lvl) {
		return 8 +  // 基础最大值8
				lvl * 2;   // 每级成长2
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		// 根据当前模式决定是否无视护甲
		if (armorPenetrationMode) {
			// 无视敌方防御进行攻击
			return damage;
		} else {
			// 普通模式，不无视护甲
			return super.proc(attacker, defender, damage);
		}
	}

	// 重写actions方法，添加模式切换选项
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_SWITCH_MODE);
		return actions;
	}

	// 重写execute方法，处理模式切换逻辑
	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_SWITCH_MODE)) {
			// 切换模式
			armorPenetrationMode = !armorPenetrationMode;
			// 根据模式改变射程
			RCH = armorPenetrationMode ? 1 : 2;
			// 显示模式切换的消息
			hero.spendAndNext(1f);
		} else {
			super.execute(hero, action);
		}
	}

	// 重写actionName方法，为模式切换操作提供显示名称
	@Override
	public String actionName(String action, Hero hero) {
		if (action.equals(AC_SWITCH_MODE)) {
			return Messages.get(this, "ac_switch_mode");
		} else {
			return super.actionName(action, hero);
		}
	}

	// 更新描述信息，显示当前模式
	@Override
	public String info() {
		String info = super.info();
		info += Messages.get(this, "mode_info",
				Messages.get(this, armorPenetrationMode ? "ap_ammo" : "hp_ammo"));
		return info;
	}

	// 确保模式状态在游戏存档和读档时正确保存和恢复
	private static final String ARMOR_PENETRATION_MODE = "armor_penetration_mode";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ARMOR_PENETRATION_MODE, armorPenetrationMode);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		armorPenetrationMode = bundle.getBoolean(ARMOR_PENETRATION_MODE);
		// 恢复对应的射程设置
		RCH = armorPenetrationMode ? 1 : 2;
	}
}