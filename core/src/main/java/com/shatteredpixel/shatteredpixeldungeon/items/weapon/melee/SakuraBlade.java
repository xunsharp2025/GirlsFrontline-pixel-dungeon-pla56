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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

import java.util.ArrayList;

public class SakuraBlade extends MeleeWeapon {

    private static final String AC_IAIDO = "IAIDO";
    private static final int IAIDO_COST = 2; // 消耗2回合
    private static final int MAX_TARGET_DISTANCE = 2; // 目标距离限制
    
    {
        image = ItemSpriteSheet.GREATAXE;
        tier = 5;
        defaultAction = AC_IAIDO;
    }

    @Override
    public int max(int lvl) {
        return  5*(tier+5) +    //50 base, up from 30
                lvl*(tier+1);   //scaling unchanged
    }

    @Override
    public int STRReq(int lvl) {
        lvl = Math.max(0, lvl);
        //20 base strength req, up from 18
        return (10 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
    }
    
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_IAIDO);
        return actions;
    }
    
    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        
        if (action.equals(AC_IAIDO)) {
            // 检查武器是否在英雄手中
            if (this != hero.belongings.weapon) {
                GLog.w(Messages.get(this, "must_hold"));
            // 检查英雄力量是否达到武器要求
            } else if (hero.STR() < STRReq()) {
                GLog.w(Messages.get(Weapon.class, "too_heavy"));
            } else if (hero.buff(Cooldown.class) != null) {
                GLog.w(Messages.get(this, "cooldown"));
            } else {
                executeIaiDo();
            }
        }
    }
    
    private void executeIaiDo() {
        GameScene.selectCell(iaidoSelector);
    }
    
    private CellSelector.Listener iaidoSelector = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                final Hero hero = Dungeon.hero;
                final SakuraBlade blade = (SakuraBlade)curItem;
                
                if (target == hero.pos) {
                    GLog.w(Messages.get(blade, "cannot_target_self"));
                    return;
                }
                
                // 检查目标是否在角色2格范围内
                int distance = Dungeon.level.distance(hero.pos, target);
                if (distance > MAX_TARGET_DISTANCE) {
                    GLog.w(Messages.get(blade, "target_too_far"));
                    return;
                }
                
                // 播放音效
                Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
                
                // 显示特效
                hero.sprite.showStatus(CharSprite.NEUTRAL, Messages.get(blade, "iaido_text"));
                
                // 获取从英雄到目标的方向
                int[] dirs = {PathFinder.CIRCLE8[PathFinder.direction(hero.pos, target)]};
                
                // 处理1*3范围内的攻击（英雄面前一条直线上的3个格子）
                int[] cellsInLine = new int[3];
                cellsInLine[0] = hero.pos + dirs[0];
                cellsInLine[1] = target;
                cellsInLine[2] = target + dirs[0];
                
                for (int cell : cellsInLine) {
                    if (cell >= 0 && cell < Dungeon.level.length() && Dungeon.level.passable[cell]) {
                        // 添加视觉效果
                        if (Dungeon.level.heroFOV[cell]) {
                            CellEmitter.get(cell).burst(BlastParticle.FACTORY, 3);
                            CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                        }
                        
                        // 攻击敌人
                        Char enemy = Actor.findChar(cell);
                        if (enemy != null && enemy.alignment != Char.Alignment.ALLY) {
                            // 计算50%的武器伤害
                            int damage = Math.round(blade.damageRoll(hero) * 0.35f);
                            enemy.damage(damage, blade);
                            
                            // 触发击退或其他效果
                            if (enemy.isAlive()) {
                                enemy.sprite.bloodBurstA(enemy.sprite.center(), damage);
                            }
                        }
                    }
                }
                
                // 消耗2个回合
                hero.spendAndNext(IAIDO_COST);
                
                // 添加冷却时间
               // Buff.affect(hero, Cooldown.class, IAIDO_COST);
                
                // 更新快捷栏显示
                updateQuickslot();
            }
        }
        
        @Override
        public String prompt() {
            return Messages.get(SakuraBlade.class, "select_target");
        }
    };
    
    // 用于处理武器冷却的Buff类
    public static class Cooldown extends Buff {
        {
            type = buffType.NEGATIVE;
        }
    }
}
