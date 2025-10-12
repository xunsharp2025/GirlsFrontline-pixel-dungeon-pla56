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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.UG;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Lar extends UniversaleGun {
//做了灰熊相关注释，用于改动武器数值
    {
        image = ItemSpriteSheet.LAR; // 设置武器贴图为LAR

        tier=5; // 武器等级为5级
        ACC = 1.475f; // 命中率提升47.5%
        DLY = 0.5f; // 攻击间隔为0.5(比普通武器更快)
        RCH = 3; // 射程为3格
    }
    
    // 我加的，重写STRReq方法，让初始力量需求减少1点
    @Override
    public int STRReq(int lvl) {
        // 调用父类方法计算标准力量需求，然后减1
        return super.STRReq(lvl) - 1;
    }
    
    @Override
    public int max(int lvl) {
        return  2*(tier+1) +    //5-12点基础伤害
                lvl*Math.round(0.5f*(tier+1));   //每升一级增加约3点伤害
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) { // 当持有者是英雄时
            Hero hero = (Hero)owner;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) { // 当敌人是怪物且被突袭时
                int diff = max() - min();
                // 造成75%-100%的最大伤害区间
                int damage = augment.damageFactor(Random.NormalIntRange(
                        min() + Math.round(diff*0.75f),
                        max()));
                int exStr = hero.STR() - STRReq(); // 额外力量计算
                if (exStr > 0) {
                    damage += Random.IntRange(0, exStr);
                }
                return damage;
            }
        }
        return super.damageRoll(owner); // 其他情况使用父类伤害计算
    }
}

