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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle; 
import java.util.ArrayList;

public class SakuraBlade extends MeleeWeapon {

    private static final String AC_IAIDO = "IAIDO";
    private static final int IAIDO_COST = 2; // 消耗2回合
    private static final int MAX_TARGET_DISTANCE = 2; // 目标距离限制
    private static final int BASE_COOLDOWN_TURNS = 100; // 基础冷却时间100回合   
    private int cooldownLeft = 0; // 当前剩余冷却时间
    private static final String COOLDOWN_LEFT = "cooldownLeft";
    private static final int MAX_BUFFED_LEVELS = 5; // 最多获得5级buff等级
    private int skillBuffedLevels = 0; // 记录通过技能获得的buff等级
    private static final String SKILL_BUFFED_LEVELS = "skillBuffedLevels"; // 用于保存/加载
    
    {
        image = ItemSpriteSheet.GREATAXE;
        tier = 5;
        defaultAction = AC_IAIDO;
    }

    // 保存冷却状态
    // 修改保存方法，添加升级次数保存
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COOLDOWN_LEFT, cooldownLeft);
        bundle.put(SKILL_BUFFED_LEVELS, skillBuffedLevels); // 保存buff等级
    }
    
    // 修改恢复方法，添加buff等级恢复
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        cooldownLeft = bundle.getInt(COOLDOWN_LEFT);
        skillBuffedLevels = bundle.getInt(SKILL_BUFFED_LEVELS); // 恢复buff等级
    }
    
    // 重写buffedLvl方法，返回基础等级加上技能获得的buff等级
    @Override
    public int buffedLvl() {
        return super.buffedLvl() + skillBuffedLevels;
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
            // 检查武器是否在英雄手中
            if (this != hero.belongings.weapon) {
                GLog.w(Messages.get(this, "must_hold"));
            // 检查英雄力量是否达到武器要求
            } else if (hero.STR() < STRReq()) {
                GLog.w(Messages.get(Weapon.class, "too_heav"));
            } else if (cooldownLeft > 0) {
                GLog.w(Messages.get(this, "cooldown", cooldownLeft));
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
                            // 计算35%的武器伤害，并乘以武器当前等级
                            int damage = Math.round(blade.damageRoll(hero) * 0.35f * (1 + blade.level()));
                            enemy.damage(damage, blade);
                            
                            //如果敌人被技能击杀，根据当前已获得buff等级有不同概率增加buff等级
                                // 阶梯概率：50%, 40%, 30%, 20%, 10%，随着buff等级增加而递减
                                if (!enemy.isAlive() && skillBuffedLevels < MAX_BUFFED_LEVELS) {
                                    // 根据当前buff等级计算增加概率
                                    float upgradeChance = 0.6f - (skillBuffedLevels + 1) * 0.1f; // 50%, 40%, 30%, 20%, 10%
                                     
                                    if (Random.Float() < upgradeChance) { 
                                        skillBuffedLevels++; // 增加技能获得的buff等级
                                        // 更新快捷栏显示
                                        updateQuickslot();
                                        GLog.p(Messages.get(blade, "level_up"));
                                        // 添加升级特效
                                        hero.sprite.emitter().burst(EnergyParticle.FACTORY, 10);
                                     
                                    // 如果达到最大buff等级，显示提示信息
                                    if (skillBuffedLevels >= MAX_BUFFED_LEVELS) {
                                        GLog.i(Messages.get(blade, "max_upgrades_reached"));
                                    }
                                }
                            }
                            
                            // 触发击退或其他效果
                            if (enemy.isAlive()) {
                                enemy.sprite.bloodBurstA(enemy.sprite.center(), damage);
                            }
                        }
                    }
                }
                
                // 消耗2个回合
                hero.spendAndNext(IAIDO_COST);
                
                // 设置冷却时间（固定为基础冷却时间，不受天赋影响）
                cooldownLeft = BASE_COOLDOWN_TURNS;
                
                // 附加冷却Buff以处理冷却时间递减
                Buff.affect(hero, CooldownTracker.class);
                
                // 更新快捷栏显示
                updateQuickslot();
            }
        }
        
        @Override
        public String prompt() {
            return Messages.get(SakuraBlade.class, "select_target");
        }
    };
    
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
            type = buffType.NEGATIVE;
        }
        
        @Override
        public boolean act() {
            if (target instanceof Hero) {
                Hero hero = (Hero) target;
                Item weapon = hero.belongings.weapon;
                
                if (weapon instanceof SakuraBlade) {
                    SakuraBlade blade = (SakuraBlade) weapon;
                    
                    if (blade.cooldownLeft > 0) {
                        blade.cooldownLeft--;
                        blade.updateQuickslot();
                    }
                    
                    // 当冷却时间结束时，移除Buff
                    if (blade.cooldownLeft <= 0) {
                        detach();
                    }
                } else {
                    // 如果武器不是SakuraBlade或已不再装备，移除Buff
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
                Buff.affect((Hero)container.owner, CooldownTracker.class);
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
            Buff.affect((Hero)owner, CooldownTracker.class);
        }
    }
}
