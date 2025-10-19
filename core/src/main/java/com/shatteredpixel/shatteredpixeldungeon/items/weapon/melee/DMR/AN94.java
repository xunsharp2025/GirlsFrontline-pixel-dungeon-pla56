package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DMR;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

//这里说明AN94这把武器属于DMR这一武器大类
public class AN94 extends DesignatedMarksmanRifle {

    // 定义射击模式枚举
    public enum FireMode {
        NORMAL,   // 普通模式
        BURST;    // 双发模式
		//抄的兔子武器，因为有人说GSH18另一个模式不能用附魔，所以gsh18并不好
		//看不懂的地方直接问ai什么意思就行
        public String title() {
            return Messages.get(AN94.class, name().toLowerCase() + "_mode");
        }
    }

    // 常量定义
    private static final String AC_SWITCH_MODE = "SWITCH_MODE"; //切换模式的按钮
    private static final String MODE = "mode";               // 用于保存模式的键
    
    // 成员变量
    private FireMode mode = FireMode.NORMAL; // 默认普通模式

	{
		image = ItemSpriteSheet.AN94; //该武器使用AN94的图像
		hitSound = Assets.Sounds.HIT_CRUSH; //该武器使用这个攻击音效
		hitSoundPitch = 0.5f;  //音调降低半度

		tier = 4; // 阶数为4
		RCH = 3; // 射程为3格　　削弱1格
		//射程4已经可以无升级白嫖地牢绝大多数甚至99%的怪了，我感觉给2射程就已经很无敌了
		
	}

    //这里决定了AN94的基础伤害数值和升级成长

	@Override
	public int min(int lvl) {
		return 4 + // 基础伤害为4
				lvl;   // 每级成长1
	}

	@Override
	public int max(int lvl) {
		// 基础最大值25
		int base = 25;
		// 每升一级，在7的基础上乘以0.9
		for (int i = 0; i < lvl; i++) {
			base += Math.round(7 * Math.pow(0.9f, i));
		}
		return base;
	}
    
	
//重写了an94的升级max伤害数值
//最大伤害：max()方法实现了渐进递减的伤害成长
//基础最大值为25点
//每升一级，增加的伤害为7 × 0.9^i（i为当前等级）
//例如：
//等级1：+7 × 0.9^0 = +7
//等级2：+7 × 0.9^1 = +6.3 → 向下取整为+6
//等级3：+7 × 0.9^2 = +5.67 → 向下取整为+5
//以此类推，形成递减的成长曲线
//防止an94在+10之后远程一枪一个

    // 设置武器模式
    public void setMode(FireMode newMode, boolean doShow) {
        if (newMode == mode) {
            return;
        }

        mode = newMode;

        updateQuickslot();
        if (doShow && curUser != null) {
            curUser.spend(1f); // 切换模式消耗1回合
            curUser.busy();
            curUser.sprite.operate(curUser.pos);
            Sample.INSTANCE.play(Assets.Sounds.EVOKE);
            curUser.next();
        }
    }
    
    // 重写proc方法，实现双发模式功能
    @Override
    public int proc(Char attacker, Char defender, int damage) {
        // 调用父类proc方法获取初始伤害
        damage = super.proc(attacker, defender, damage);
        
        // 如果是双发模式且攻击者是英雄
        if (mode == FireMode.BURST && attacker instanceof Hero && defender.isAlive()) {
            // 双发模式下，第一次伤害为点射模式的60%
            int firstDamage = Math.round(damage * 0.6f);
            // 第二次伤害为点射模式的40%
            int secondDamage = Math.round(damage * 0.4f);
            
            // 对敌人造成第二次伤害
            defender.damage(secondDamage, this);
            
            // 播放额外的攻击音效
            Sample.INSTANCE.play(hitSound, hitSoundPitch);
            
            // 返回修改后的第一次伤害
            return firstDamage;
        }
        
        // 普通模式下返回正常伤害
        return damage;
    }
    
    // 添加模式切换动作
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SWITCH_MODE);
        return actions;
    }
    
    // 处理动作执行
    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        
        if (action.equals(AC_SWITCH_MODE)) {
            WndOptions wndOptions = new WndOptions(
                Messages.get(AN94.class, "switch_mode_title"),
                Messages.get(AN94.class, "switch_mode_message", mode.title()),
                FireMode.NORMAL.title(),
                FireMode.BURST.title()
            ) {
                @Override
                protected void onSelect(int index) {
                    FireMode newMode = (index == 0) ? FireMode.NORMAL : FireMode.BURST;
                    setMode(newMode, true);
                }
            };
            GameScene.show(wndOptions);
        }
    }
    
    // 保存模式状态
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MODE, mode.ordinal());
    }
    
    // 恢复模式状态
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        FireMode newMode = (bundle.getInt(MODE) == 0) ? FireMode.NORMAL : FireMode.BURST;
        setMode(newMode, false);
    }
    
    // 更新物品信息，显示当前模式
    @Override
    public String info() {
        String info = super.info();
        
        // 添加当前模式信息
        info += "\n\n" + Messages.get(AN94.class, "current_mode", mode.title());
        
        // 添加模式说明
        info += "\n" + Messages.get(AN94.class, mode.name().toLowerCase() + "_mode_desc");
        
        return info;
    }
}

//HK416作为一把强化属性高的武器，每升一级基础伤害才+8，HK416这把武器1格射程
//我拿着HK416在无挑情况下基本不喝药就轻松通关了，这东西1格射程，我拿着这个武器+6之后可以轻松站撸所有怪，对是所有怪
//有这个武器可以全程天赋不点，药水不喝，磁盘基本不用就轻松通关，在049我用+6之后的416就可以这样干，049也没天赋
//你的an94在4格射程的基础上居然敢+7最大伤害，我去不是我质疑，这礼崩乐坏了吧
//造武器可以弱然后加强，你这数据都不看一眼直接写的吗？？？
//你直接给个过热打5次就冷却20回合我觉得都很超模
//你前面还质疑ak47，现在这东西领先ak47至少20倍吧
//我都好奇这把武器是以谁作为模型做出来的，冰岛人做卡都不敢给这么超模...
//即使是他生成率很低，但这个真得改吧