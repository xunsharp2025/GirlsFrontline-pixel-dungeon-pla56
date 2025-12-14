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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SMG;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Ump45 extends SubMachineGun {

    private static final String AC_SMOKE = "SMOKE";
    private static final int SMOKE_COST = 1; // 消耗X回合
    private static final int BASE_COOLDOWN_TURNS = 300; // 基础冷却时间X回合
    private int cooldownLeft = 0; // 当前剩余冷却时间
    private static final String COOLDOWN_LEFT = "cooldownLeft";
    {
        image = ItemSpriteSheet.UMP45;

        tier = 1;
        RCH = 1;
        ACC = 0.7f;
        DEF = 1;
        DEFUPGRADE = 1;

        defaultAction = AC_SMOKE;
        usesTargeting = true;
    }
/**/
    @Override
    public int max(int lvl) {
        return  5*(tier+1) +    //8 base, down from 10
                lvl*(tier+1);   //scaling unchanged
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
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SMOKE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_SMOKE)) {
            //检查是否装备，复制的TimekeepersHourglass
            if (!isEquipped( hero )) {
                GLog.w(Messages.get(this, "must_hold"));
            }
            //检查是否有毒
            else if (cursed){
                GLog.i( Messages.get(this, "curse") );
            }
            //检查是否超力
            else if (hero.STR() < STRReq()) {
                GLog.w(Messages.get(Weapon.class, "too_heav"));
            }
            //检查是否cd
            else if (cooldownLeft > 0) {
                GLog.w(Messages.get(this, "cooldown", cooldownLeft));
            }
            //没有进入上述if，即满足全部要求之后，进入此处executeSMOKE动作
            else {
                executeSMOKE();
            }
        }
    }
    //SMOKEA转SMOKEB
    public SMOKEB SMOKEA(){
        return new SMOKEB();
    }
    /*Selector的目标不为空的时候，对改位置引用SMOKEA，SMOKEA转SMOKEB，
      SMOKEB使用Honeypot的方法，对目标位置使用弹道学的cast，cast有丢弃物品的动画，
      Honeypot有shatter方法，这里进行了覆写shatter*/
    private final CellSelector.Listener SMOKESelector = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                //使用SMOKEB中的cast操作，Potion的cast不会保留掉落物
                SMOKEA().cast(curUser, target);
                //对冲掉物品的丢弃时间
                hero.spendAndNext(-TIME_TO_THROW);
                //往下的是复制灵刀的
                //固定消耗时间
                hero.spendAndNext(SMOKE_COST);
                // 设置冷却时间（固定为基础冷却时间，不受天赋影响）
                cooldownLeft = BASE_COOLDOWN_TURNS;
                // 附加冷却Buff以处理冷却时间递减
                Buff.affect(hero, Ump45.CooldownTracker.class);
                // 更新快捷栏显示
                updateQuickslot();
            }
        }
        //这里要加文案
        @Override
        public String prompt() {
            return Messages.get(Ump45.class, "select_target");
        }
    };
    //executeSMOKE动作进入Selector界面
    private void executeSMOKE() {
        GameScene.selectCell(SMOKESelector);
    }
    //SMOKEB有丢出物品的贴图及文案
    private class SMOKEB extends Honeypot {

        {
            image = ItemSpriteSheet.SMOKEUmp45;
        }
        //shatter为从大隐身处复制过来的扩散逻辑，但生成的气体复制电击

        public Item shatter( Char owner, int cell ) {
            //气体音效
            if (Dungeon.level.heroFOV[cell]) {
                Sample.INSTANCE.play( Assets.Sounds.GAS );
            }

            int centerVolume = 50;
            //中心格非固体
            if (!Dungeon.level.solid[cell]){
                GameScene.add( Blob.seed( cell, centerVolume, SmokeScreen.class ) );
            }
            //中心格为固体
            else {
                int j =0;
                for (int i : PathFinder.NEIGHBOURS8){
                    if (!Dungeon.level.solid[cell+i]){
                        //累计邻格非固体格子数量
                        j++;
                    }
                }
                for (int i : PathFinder.NEIGHBOURS8){
                    if (!Dungeon.level.solid[cell+i]){
                        //给予邻格非固体格子均分的气体量
                        GameScene.add( Blob.seed( cell+i, centerVolume/j, SmokeScreen.class ) );
                    }
                }
            }
            //返回值为掉落物品，此处设置为空
            return null;
        }

        public void cast(final Hero user, final int dst){
            final int cell = throwPos( user, dst );
            super.cast( user, cell );
//          Potion的cast自带使用shatter
//          shatter(cell);
        }

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

                if (weapon instanceof Ump45) {
                    Ump45 blade = (Ump45) weapon;

                    if (blade.cooldownLeft > 0) {
                        blade.cooldownLeft--;
                        blade.updateQuickslot();
                    }

                    // 当冷却时间结束时，移除Buff
                    if (blade.cooldownLeft <= 0) {
                        detach();
                    }
                } else {
                    // 如果武器不是Ump45或已不再装备，移除Buff
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
                Buff.affect((Hero)container.owner, Ump45.CooldownTracker.class);
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
            Buff.affect((Hero)owner, Ump45.CooldownTracker.class);
        }
    }

}

