package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SR;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by Kirsi on 2017-11-21.
 * Girls Piexl Dungeon , Weapon NTW20
 */

public class Ntw20 extends SniperRifle {

    private static final String AC_AIM_SHOOT = "AIM_SHOOT";
    private boolean aimMode = false;

    {
        image = ItemSpriteSheet.NTW20;

        tier = 6;
        ACC = 1.75f;
        DLY = 9f;
        RCH = 50;
    }

    @Override
    public int max(int lvl) {
        return Math.round(17.4f*(tier+1)) +    //40 base, up from 35
                lvl*Math.round(5.6f*(tier+5)); //+4 per level, up from +3
    }

    // 添加模式切换选项
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_AIM_SHOOT);
        return actions;
    }

    // 处理模式切换逻辑
    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_AIM_SHOOT)) {
            // 切换到瞄准模式
            aimMode = true;
            // 显示消息
            hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "aim_mode_on"));
            // 消耗10个回合
            hero.spendAndNext(10f);
        } else {
            super.execute(hero, action);
        }
    }

    // 为模式切换操作提供显示名称
    @Override
    public String actionName(String action, Hero hero) {
        if (action.equals(AC_AIM_SHOOT)) {
            return Messages.get(this, "ac_aim_shoot");
        } else {
            return super.actionName(action, hero);
        }
    }

    // 实现瞄准射击的效果
    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (aimMode && attacker instanceof Hero) {
            // 攻击后自动切换回普通模式
            aimMode = false;
            ((Hero)attacker).sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, "aim_mode_off"));
            // 瞄准模式下，直接返回伤害值以无视敌方防御
            return damage;
        }
        return super.proc(attacker, defender, damage);
    }
    
    // 重写accuracyFactor方法，在瞄准模式下提高武器命中率5倍
    @Override
    public float accuracyFactor(Char owner) {
        float factor = super.accuracyFactor(owner);
        // 在瞄准模式下，命中率提高5倍
        if (aimMode && owner instanceof Hero) {
            factor *= 5f;
        }
        return factor;
    }
    
    // 重写damageRoll方法，在瞄准模式下锁定伤害范围在85%-120%
    @Override
    public int damageRoll(Char owner) {
        if (aimMode && owner instanceof Hero) {
            // 获取基础伤害范围
            int min = min();
            int max = max();
            // 计算85%-120%的伤害范围
            int minLocked = Math.round(min * 0.85f);
            int maxLocked = Math.round(max * 1.2f);
            // 在锁定范围内随机取值
            return damageRollWithRange(owner, minLocked, maxLocked);
        }
        return super.damageRoll(owner);
    }

    // 自定义方法，支持指定最小和最大伤害
    private int damageRollWithRange(Char owner, int min, int max) {
        int damage = augment.damageFactor(Random.NormalIntRange(min, max));

        if (owner instanceof Hero) {
            int exStr = ((Hero)owner).STR() - STRReq();
            if (exStr > 0) {
                damage += Random.IntRange(0, exStr);
            }
        }
        
        return damage;
    }

    // 更新描述信息，显示当前模式
    @Override
    public String info() {
        String info = super.info();
        if (aimMode) {
            info += Messages.get(this, "aim_mode_active");
        } else {
            info += Messages.get(this, "aim_mode_inactive");
        }
        return info;
    }

    // 保存模式状态
    private static final String AIM_MODE = "aim_mode";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(AIM_MODE, aimMode);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        aimMode = bundle.getBoolean(AIM_MODE);
    }
}