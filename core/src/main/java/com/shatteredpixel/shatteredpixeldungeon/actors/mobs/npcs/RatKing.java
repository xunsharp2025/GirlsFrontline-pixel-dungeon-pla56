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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.XMasGift;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Choco;
//import com.shatteredpixel.shatteredpixeldungeon.items.SALTYMOONCAKE;
import com.shatteredpixel.shatteredpixeldungeon.items.food.XMasSugar;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FncSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

// 在导入部分添加CounterBuff类的导入
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.utils.Holidays;

public class RatKing extends NPC {

    {        
        spriteClass = FncSprite.class;
        
        state =Dungeon.depth==0?PASSIVE: SLEEPING;
    }
    
    // 添加统一的礼物计数器，用于跟踪鼠王是否已经给予过玩家物品
    public static class GiftTracker extends CounterBuff {
        { revivePersists = true; }
    }
    public static class ChocoTracker extends CounterBuff {
        { revivePersists = true; }
    }
    
    // 添加提示计数器，用于跟踪已显示的提示次数
    public static class HintTracker extends CounterBuff {
        { revivePersists = true; }
    }

    //对FNC最后使用的物品的记录
    public static class LastTracker extends CounterBuff {
        { revivePersists = true; }
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }
    
    @Override
    public float speed() {
        return 2f;
    }
    
    @Override
    protected Char chooseEnemy() {
        return null;
    }
    
    @Override
    public void damage( int dmg, Object src ) {
    }
    
    @Override
    public void add( Buff buff ) {
    }
    
    @Override
    public boolean reset() {
        return true;
    }

    //***This functionality is for when rat king may be summoned by a distortion trap

    @Override
    protected void onAdd() {
        super.onAdd();
        if (Dungeon.depth != 5){
            yell(Messages.get(this, "confused"));
        }
    }

    protected  boolean exit =false;

    @Override
    protected boolean act() {
        if(Dungeon.depth==0){
            state = PASSIVE;
        }else
        if(Dungeon.depth!=5){
            state = WANDERING;
        }
        return super.act();
    }

    //***
    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );

        // 检查是否已经给予过玩家巧克力
        ChocoTracker trackerA = hero.buff(ChocoTracker.class);
        boolean hasGivenChoco = (trackerA != null) ;

        // 检查是否已经给予过玩家拐杖糖
        GiftTracker trackerB = hero.buff(GiftTracker.class);
        boolean hasGivenSugar = (trackerB != null) ;

        boolean isMidAutumn = Holidays.holiday == Holidays.Holiday.midAutumnFestival;
        // 检查玩家是否持有月饼
        boolean hasMooncake = hasPlayerMooncake();

        // 已经给予过物品，根据提示次数显示不同的消息
        HintTracker hintTracker = hero.buff(HintTracker.class);
        float hintCount = (hintTracker != null) ? hintTracker.count() : 0;

        if (c != hero){
            return super.interact(c);
        }

        KingsCrown crown = hero.belongings.getItem(KingsCrown.class);
        XMasGift gift = hero.belongings.getItem(XMasGift.class);
        if (state == SLEEPING) {
            notice();
            yell( Messages.get(this, "not_sleeping") );
            state = WANDERING;
        } else
        if (crown != null){
            if (hero.belongings.armor() == null) {
                yell(Messages.get(RatKing.class, "crown_clothes"));
            } else {
                Badges.validateRatmogrify();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndOptions(
                                sprite(),
                                Messages.titleCase(name()),
                                Messages.get(RatKing.class, "crown_desc"),
                                Messages.get(RatKing.class, "crown_yes"),
                                Messages.get(RatKing.class, "crown_info"),
                                Messages.get(RatKing.class, "crown_no")
                        ) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    SetLast(1);
                                    crown.upgradeArmor(hero, hero.belongings.armor(), new Ratmogrify());
                                    ((FncSprite) sprite).resetAnims();
                                    yellgood(Messages.get(RatKing.class, "crown_thankyou"));
                                } else if (index == 1) {
                                    GameScene.show(new WndInfoArmorAbility(hero.heroClass, new Ratmogrify()));
                                } else {
                                    yell(Messages.get(RatKing.class, "crown_fine"));
                                }
                            }
                        });
                    }
                });
            }
        } else
        if (gift!=null&&!hasGivenSugar){
            SetLast(2);
            gift.GiftCost();
            if(!hasGivenChoco){
                yellgood(Messages.get(this, "both") );
                hero.spendAndNext( -1 );
                GetChock();
            }
            else {
                yellgood(Messages.get(this, "onlysugar") );
            }
            GetSugar();
            ((FncSprite) sprite).resetAnims();
            Badges.validateXMASGift();
            if(hero.buff(HintTracker.class)!=null)
                hero.buff(HintTracker.class).detach();
            //重置点击次数以获取特色文案
        } else
        if (!hasGivenChoco) {
            // FNC尚未给予Choco
            GetChock();
            if (isMidAutumn && !hasMooncake) {
                // 中秋节期间，且玩家没有月饼，显示中秋节对话
                yellgood(Messages.get(this, "mid_autumn_greeting"));
            }
            if(hero.buff(HintTracker.class)!=null)
                hero.buff(HintTracker.class).detach();
            //重置点击次数以获取特色文案
        } else
        if (hintCount < 3) {
            if(hasGivenSugar){
                // FNC获得礼物后的诚挚祝福
                yellgood(Messages.get(this,"wish_"+hintCount));
//                if (hintCount == 0) {
//                    yell(Messages.get(this, "no_more"));
//                } else if (hintCount == 1) {
//                    yell(Messages.get(this, "no_more_2"));
//                } else {
//                    yell(Messages.get(this, "no_more_3"));
//                }
            }else {
                // FNC护食哈气了
                yell(Messages.get(this,"argue_"+hintCount));
//                if (hintCount == 0) {
//                    yell(Messages.get(this, "no_more"));
//                } else if (hintCount == 1) {
//                    yell(Messages.get(this, "no_more_2"));
//                } else {
//                    yell(Messages.get(this, "no_more_3"));
//                }
            }
                // 增加提示计数器
                Buff.count(hero, HintTracker.class, 1);
        } else
        if (hero.armorAbility instanceof Ratmogrify) {
            yellgood( Messages.get(this, "crown_after") );
        } else
        if (hasGivenSugar) {
            // 给予了礼物后，循环文案是祝福而非护食哈气
            yellgood(Messages.get(this, "wish_cyc"));
        } else
        {
            // 没有交易鼠王护甲，并且没有给予礼物，还在获取巧克力后点击多次，触发护食哈气
            yell(Messages.get(this, "what_is_it"));
        }
        return true;
    }

    private void SetLast(int i) {
        if(hero.buff(LastTracker.class)!=null){
            hero.buff(LastTracker.class).detach();
        }
        Buff.count(hero,LastTracker.class,i);
    }

    private void GetChock(){
        Choco t1 = new Choco(); // Choco在中秋节期间会自动显示为月饼
        t1.identify();
        if (t1.doPickUp(hero)) {
            Messages.get(hero, "you_now_have", t1.name());
        } else {
            Dungeon.level.drop(t1, hero.pos).sprite.drop();
        }
        // 标记为已给予物品
        Buff.count(hero, ChocoTracker.class, 1);
    }
    private void GetSugar(){
        XMasSugar t1 = new XMasSugar(); // 拐杖糖
        t1.identify();
        if (t1.doPickUp(hero)) {
            Messages.get(hero, "you_now_have", t1.name());
        } else {
            Dungeon.level.drop(t1, hero.pos).sprite.drop();
        }
        // 标记为已给予物品
        Buff.count(hero, GiftTracker.class, 1);
    }
    
    // 检查玩家是否持有巧克力
    private boolean hasPlayerMooncake() {
        return hero.belongings.getItem(Choco.class) != null;
    }
    
    @Override
    public String description() {
        return ((FncSprite)sprite).XMAS ?
                Messages.get(this, "desc_festive")
                : super.description();
    }
}
