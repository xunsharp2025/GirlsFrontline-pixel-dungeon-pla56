package com.gfpixel.gfpixeldungeon.items.weapon.melee;

import com.gfpixel.gfpixeldungeon.actors.Char;
import com.gfpixel.gfpixeldungeon.actors.buffs.Buff;
import com.gfpixel.gfpixeldungeon.actors.hero.Hero;
import com.gfpixel.gfpixeldungeon.effects.MagicMissile;
import com.gfpixel.gfpixeldungeon.items.Item;
import com.gfpixel.gfpixeldungeon.items.bags.Bag;
import com.gfpixel.gfpixeldungeon.items.food.Food;
import com.gfpixel.gfpixeldungeon.mechanics.Ballistica;
import com.gfpixel.gfpixeldungeon.scenes.CellSelector;
import com.gfpixel.gfpixeldungeon.scenes.GameScene;
import com.gfpixel.gfpixeldungeon.sprites.ItemSpriteSheet;
import com.gfpixel.gfpixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;

import java.util.ArrayList;

public class ShootGun extends MeleeWeapon {

    //动作名称
    public static final String AC_RELOAD = "RELOAD";
    public static final String AC_SHOOT = "SHOOT";
    //是否使用子弹贴图
    public boolean useMissileSprite = true;
    //TODO 用于子弹贴图的物品，如果有需求的话、
    public Item missileItem = new Food();
    public int curCharges = 999;
    public int maxCharges = 999;

    public int coolDown;

    public int altCooldown = 5;

    //TODO 用于渲染子弹法杖特效
    public int effectIndex = 0;

    public boolean needEquip = false;

    //充能buff
    public Buff chargeBuff;
    public int reloadTime = 2;

    {
        //贴图
        image = ItemSpriteSheet.G11;
        //阶数
        tier = 1;
        //设置到快捷栏的默认动作
        defaultAction = AC_SHOOT;
        //自动选择目标
        usesTargeting = true;
        //稀有
        unique = true;
        //遗物生成
        bones = false;
    }

    @Override
    public int STRReq(int lvl){
        //力量需求
        return 10;
    }

    @Override
    public int min(int lvl) {
        //TODO 最低伤害
        return super.min(lvl);
    }

    @Override
    public int max(int lvl) {
        //TODO 最高伤害
        return super.max(lvl);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        //动作列表
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_RELOAD);
        actions.add(AC_SHOOT);
        return actions;
    }

    @Override
    public void activate(Char ch) {
        //装备或读档时调用,一般是buff
    }

    @Override
    public void execute(Hero hero,String action) {
        //按下按钮时检查动作
        super.execute(hero,action);
        if (action.equals(AC_RELOAD)) {
            if (reload()) {
                curUser.spendAndNext(reloadTime);
            }
        } else if (action.equals(AC_SHOOT)) {
            if (curUser.belongings.weapon != this && needEquip) {
                //TODO 必须装备才能射击，可以提示装备
//                GLog.i("You need to equip this item first.");
                return;
            }

            if (!hasenoughCharges()) {
                reload();
                curUser.spendAndNext(1f);
                return;
            }

            curUser = hero;
            curItem = this;
            GameScene.selectCell(zapper);
        }
    }

    public boolean hasenoughCharges() {
        return curCharges >= 1;
    }

    public boolean reload() {
        //实现装填
        if (curCharges < maxCharges) {
            //TODO 可以加个播放动作
            curCharges++;
            //更新快捷栏
            updateQuickslot();
            return true;
        } else {
            //TODO 输出日志提示已经满了
            return false;
        }
    }

    public int MagicEffect(int index){
        switch (index){
            case 0: // MAGIC_MISSILE
                break;
            case 1: // FROST
                break;
            case 2: // FIRE
                break;
            case 3: // CORROSION
                break;
            case 4: // FOLIAGE
                break;
            case 5: // FORCE
                break;
            case 6: // BEACON
                break;
            case 7: // SHADOW
                break;
            case 8: // RAINBOW
                break;
            default:
                break;
        }
        return index;
    }


    public void shoot(int cell) {
        //实现射击
        if (useMissileSprite && missileItem != null) {
            //如果使用子弹贴图
            //TODO 记得去设置速度SPEED和ANGULAR_SPEEDS
            ((MissileSprite)curUser.sprite.parent.recycle(MissileSprite.class)).reset(curUser.pos, cell, missileItem, new Callback() {
                @Override
                public void call() {
                    //子弹到达，动画结束，调用onShootComplete
                    onShootComplete(cell);
                }
            });
        } else {
            //否则使用法杖特效
            //TODO 更改MAGIC_MISSILE
            MagicMissile.boltFromChar(curUser.sprite.parent,
                    MagicEffect(effectIndex),curUser.sprite,cell,
                    new Callback() {
                        @Override
                        public void call() {
                            onShootComplete(cell);
                        }
                    });
        }
    }

    public boolean canShoot(int cell) {
        //检查是否可射击，在子类重写以追加条件
        Ballistica ba = new Ballistica(curUser.pos, cell, Ballistica.PROJECTILE);
        //创建弹道
        return ba.collisionPos == cell;
        //畅通无阻
    }

    public void onShootComplete(int cell) {
        //攻击完成时调用，减少充能，子类里重写计算伤害和结束回合
        reduceCharges(1);
        curUser.spendAndNext(1f);
    }

    public void reduceCharges(int amount) {
        //减少充能
        curCharges -= amount;
        //更新快捷栏
        updateQuickslot();
    }

    @Override
    public int proc(Char attacker,Char defender,int damage) {
        //近战攻击时调用，一般是附魔
        return super.proc(attacker,defender,damage);
    }

    @Override
    public int reachFactor(Char owner) {
        //攻击距离
        return super.reachFactor(owner);
    }

    @Override
    public boolean collect(Bag container) {
        //拾取时调用
        return super.collect(container);
    }

    @Override
    public void onDetach( ) {
        //移除时调用
        super.onDetach();
    }

    @Override
    public Item upgrade(boolean enchant) {
        //升级时调用
        return super.upgrade(enchant);
    }

    @Override
    public Item degrade() {
        //降级时调用
        return super.degrade();
    }

    @Override
    public String status() {
        //状态描述
        if (levelKnown) {
            return curCharges + "/" + maxCharges;
        } else {
            return null;
        }
    }

    @Override
    public Emitter emitter() {
        //贴图粒子效果
        //不要使用这玩意，除非你想用代码画像素画。我也不知道为什么会有几把枪加了这个，还不写粒子
        return super.emitter();
    }

    @Override
    public Item random() {
        //随机生成等级
        return super.random();
    }

    public static final String CURCHAGE = "curchage";
    public static final String MAXCHARGE = "maxcharge";

    public static final String COOLDOWN = "cooldown";

    public static final String ALTCOODWON = "altcoodwon";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(CURCHAGE,curCharges);
        bundle.put(MAXCHARGE,maxCharges);
        bundle.put(COOLDOWN,coolDown);
        bundle.put(ALTCOODWON,altCooldown);
        //存储数据
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        curCharges = bundle.getInt(CURCHAGE);
        maxCharges = bundle.getInt(MAXCHARGE);
        coolDown = bundle.getInt(COOLDOWN);
        altCooldown = bundle.getInt(ALTCOODWON);
        //读取数据
        super.restoreFromBundle(bundle);
    }

    @Override
    public int price() {
        //TODO 商店价格
        return super.price();
    }

    public static String shootPrompt = "123";//TODO 自己改，选择格子时的提示
    public static CellSelector.Listener zapper = new  CellSelector.Listener() {
        //格子选择监听器
        @Override
        public void onSelect(Integer cell) {
            //射击时调用
            if (cell != null) {//由于底层原因会调用两次，所以必须判断非null

                //安全检查，也不是不能省略
                final ShootGun shootGun;
                if (curItem instanceof ShootGun) {
                    shootGun = (ShootGun) curItem;
                } else {
                    return;
                }

                if (shootGun.canShoot(cell)) {
                    curUser.sprite.zap(cell);
                    curUser.busy();
                    shootGun.shoot(cell);
                } else {
//                    GLog.i("You need to reload first.");//TODO 自己改，提示无法射击
                }
            }
        }

        @Override
        public String prompt() {
            //射击提示
            return shootPrompt;
        }
    };
}
