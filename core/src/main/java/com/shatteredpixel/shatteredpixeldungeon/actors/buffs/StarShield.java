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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;


public class StarShield extends ShieldBuff {
    
    {
        type = buffType.POSITIVE;
    }

    private int turnsPassed = 0;

    @Override
    public void incShield(int amt) {
        //对非GSH18角色添加30点上限限制
        if (target instanceof Hero) {
            Hero hero = (Hero) target;
            // 如果不是GSH18角色且当前护盾加上新增量超过30，则只加到30
            if (hero.heroClass != HeroClass.GSH18 && shielding() + amt > 30) {
                amt = 30 - shielding();
                if (amt <= 0) return; // 已经达到上限，不再增加
            }
        }
        
        super.incShield(amt);
        
        // 当护盾超过30层时，每获得1点护盾，同时回复1点血量
        if (shielding() - amt >= 30) {
            //计算新增的护盾中超过30层的部分
            if (target instanceof Hero) {
                // 使用Healing buff进行治疗
                Buff.affect(target, Healing.class).setHeal(amt, 1f, 0);
                target.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "heal", amt));
            }
        } else if (shielding() > 30) {
            // 计算从低于30层到超过30层的部分
            int overThirty = shielding() - 30;
            if (overThirty > 0) {
                if (target instanceof Hero) {
                    // 使用Healing buff进行治疗
                    Buff.affect(target, Healing.class).setHeal(overThirty, 1f, 0);
                    target.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "heal", overThirty));
                }
            }
        }
    }

    @Override
    public boolean act() {
        turnsPassed++;
        
        // 每2回合减少1点护盾值
        if (turnsPassed >= 2) {
            if (shielding() > 0) {
                decShield(1);
            }
            turnsPassed = 0;
        }
        
        if (shielding() <= 0) {
            detach();
        }
        
        spend(TICK);
        
        return true;
    }
    
    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.SHIELDED);
        else target.sprite.remove(CharSprite.State.SHIELDED);
    }
    
    @Override
    public int icon() {
        return BuffIndicator.STAR_SHIELD;
    }
    
    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 1f, 1f); //白色
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(shielding());
    }
    
    @Override
    public String toString() {
        return Messages.get(this, "name");
    }
    
    @Override
    public String desc() {
        return Messages.get(this, "desc", shielding());
    }

    private static final String TURNS_PASSED = "turns_passed";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TURNS_PASSED, turnsPassed);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        turnsPassed = bundle.getInt(TURNS_PASSED);
    }
}