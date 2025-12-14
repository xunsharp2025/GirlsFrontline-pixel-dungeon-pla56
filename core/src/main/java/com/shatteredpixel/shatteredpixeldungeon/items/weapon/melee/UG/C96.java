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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Speed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class C96 extends UniversaleGun {
    private static final String AC_LIGHT = "LIGHT";
    private static final int LIGHT_COST = 1; // 消耗X回合
    private static final int BASE_COOLDOWN_TURNS = 150; // 基础冷却时间X回合
    private int cooldownLeft = 0; // 当前剩余冷却时间
    private static final String COOLDOWN_LEFT = "cooldownLeft";
    {
        image = ItemSpriteSheet.C96;

        tier = 3;
        DLY = 1.5f; //1.25x speed
        RCH = 5;    //lots of extra reach

        defaultAction = AC_LIGHT;
    }
    // 保存冷却状态
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COOLDOWN_LEFT, cooldownLeft);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        cooldownLeft = bundle.getInt(COOLDOWN_LEFT);
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_LIGHT );
        return actions;
    }
    @Override
    public void execute( Hero hero, String action ) {

        super.execute(hero, action);
        if (action.equals(AC_LIGHT)) {
            //检查是否装备，复制的TimekeepersHourglass
            if (!isEquipped(hero)) {
                GLog.w(Messages.get(this, "must_hold"));
            }
            //检查是否有毒，同上
            else if (cursed) {
                GLog.i(Messages.get(this, "curse"));
            }
            //检查是否超力，灵刀
            else if (hero.STR() < STRReq()) {
                GLog.w(Messages.get(Weapon.class, "too_heav"));
            }
            //检查是否cd，灵刀
            else if (cooldownLeft > 0) {
                GLog.w(Messages.get(this, "cooldown", cooldownLeft));
            }
            //没有进入上述if，即满足全部要求之后，进入此处executeSMOKE动作
            else {
                executeLIGHT();
            }
        }
    }
    //C96发光动作执行内容
    private void executeLIGHT() {
        Buff.affect(hero, Light.class, 50.0f);
        //往下的是复制灵刀的
        // 消耗固定回合
        hero.spendAndNext(LIGHT_COST);
        // 设置冷却时间（固定为基础冷却时间，不受天赋影响）
        cooldownLeft = BASE_COOLDOWN_TURNS;
        // 附加冷却Buff以处理冷却时间递减
        Buff.affect(hero, C96.CooldownTracker.class);
        // 更新快捷栏显示
        updateQuickslot();
    }

    @Override
    public int max(int lvl) {
        return  2*(tier+1) +    //8 base, down from 10
                lvl*(tier+1);   //scaling unchanged
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        if (defender instanceof Hero && defender.buffs(Speed.class).isEmpty()) {
            Buff.prolong(defender, Light.class, 9.5f);
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero)owner;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                //deals 67% toward max to max on surprise, instead of min to max.
                int diff = max() - min();
                int damage = augment.damageFactor(Random.NormalIntRange(
                        min() + Math.round(diff*0.7f),
                        max()));
                int exStr = hero.STR() - STRReq();
                if (exStr > 0) {
                    damage += Random.IntRange(0, exStr);
                }
                return damage;
            }
        }
        return super.damageRoll(owner);
    }
    //下面的全都是直接复制灵刀的代码
    // 添加冷却状态显示方法
    @Override
    public String status() {
        if (cooldownLeft > 0) {
            return "CD:" + cooldownLeft;
        } else {
            return super.status();
        }
    }

    // 修改现有Cooldown类为CooldownTracker，负责冷却时间递减
    public static class CooldownTracker extends AllyBuff {
        {
            type = buffType.POSITIVE;
        }

        @Override
        public boolean act() {
            if (target instanceof Hero) {
                Hero hero = (Hero) target;
                Item weapon = hero.belongings.weapon;

                if (weapon instanceof C96) {
                    C96 blade = (C96) weapon;

                    if (blade.cooldownLeft > 0) {
                        blade.cooldownLeft--;
                        blade.updateQuickslot();
                    }

                    // 当冷却时间结束时，移除Buff
                    if (blade.cooldownLeft <= 0) {
                        detach();
                    }
                } else {
                    // 如果武器不是C96或已不再装备，移除Buff
                    detach();
                }
            }

            spend(TICK);
            return true;
        }
    }

    // 确保在收集武器时检查冷却状态
    @Override
    public boolean collect(Bag container) {
        if (super.collect(container)) {
            if (container.owner instanceof Hero && cooldownLeft > 0) {
                Buff.affect((Hero)container.owner, C96.CooldownTracker.class);
            }
            return true;
        } else {
            return false;
        }
    }

    // 确保在装备武器时检查冷却状态
    @Override
    public void activate(Char owner) {
        super.activate(owner);
        if (owner instanceof Hero && cooldownLeft > 0) {
            Buff.affect((Hero)owner, C96.CooldownTracker.class);
        }
    }

}



