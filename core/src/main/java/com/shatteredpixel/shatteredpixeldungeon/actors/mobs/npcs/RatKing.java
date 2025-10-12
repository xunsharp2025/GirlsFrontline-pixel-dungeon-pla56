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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Choco;
//import com.shatteredpixel.shatteredpixeldungeon.items.SALTYMOONCAKE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FncSprite;
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
        
        state = SLEEPING;
    }
    
    // 添加统一的礼物计数器，用于跟踪鼠王是否已经给予过玩家物品
    public static class GiftTracker extends CounterBuff {
        { revivePersists = true; }
    }
    
    // 添加提示计数器，用于跟踪已显示的提示次数
    public static class HintTracker extends CounterBuff {
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

    @Override
    protected boolean act() {
        if (Dungeon.depth < 5){
            if (pos == Dungeon.level.exit){
                destroy();
                sprite.killAndErase();
            } else {
                target = Dungeon.level.exit;
            }
        } else if (Dungeon.depth > 5){
            if (pos == Dungeon.level.entrance){
                destroy();
                sprite.killAndErase();
            } else {
                target = Dungeon.level.entrance;
            }
        }
        return super.act();
    }

    //***

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );

        if (c != Dungeon.hero){
            return super.interact(c);
        }

        KingsCrown crown = Dungeon.hero.belongings.getItem(KingsCrown.class);
        if (state == SLEEPING) {
            notice();
            yell( Messages.get(this, "not_sleeping") );
            state = WANDERING;
        } else if (crown != null){
            if (Dungeon.hero.belongings.armor() == null){
                yell( Messages.get(RatKing.class, "crown_clothes") );
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
                        ){
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0){
                                    crown.upgradeArmor(Dungeon.hero, Dungeon.hero.belongings.armor(), new Ratmogrify());
                                    ((FncSprite)sprite).resetAnims();
                                    yell(Messages.get(RatKing.class, "crown_thankyou"));
                                } else if (index == 1) {
                                    GameScene.show(new WndInfoArmorAbility(Dungeon.hero.heroClass, new Ratmogrify()));
                                } else {
                                    yell(Messages.get(RatKing.class, "crown_fine"));
                                }
                            }
                        });
                    }
                });
            }
        } else if (Dungeon.hero.armorAbility instanceof Ratmogrify) {
            yell( Messages.get(this, "crown_after") );
        } else {
            // 检查是否已经给予过玩家物品
            GiftTracker tracker = Dungeon.hero.buff(GiftTracker.class);
            boolean hasGivenGift = (tracker != null) && tracker.count() >= 1;
            
            if (!hasGivenGift) {
                // 玩家尚未获得过物品
                boolean isMidAutumn = Holidays.holiday == Holidays.Holiday.midAutumnFestival;
                
                // 检查玩家是否持有月饼
                boolean hasMooncake = hasPlayerMooncake();
                
                if (isMidAutumn && !hasMooncake) {
                    // 中秋节期间，且玩家没有月饼，给予月饼并显示中秋对话
                    Choco t1 = new Choco(); // Choco在中秋节期间会自动显示为月饼
                    t1.identify();
                    if (t1.doPickUp(Dungeon.hero)) {
                        Messages.get(Dungeon.hero, "you_now_have", t1.name());
                    } else {
                        Dungeon.level.drop(t1, Dungeon.hero.pos).sprite.drop();
                    }
                    
                    // 显示中秋节对话
                    yell(Messages.get(this, "mid_autumn_greeting"));
                } else {
                    // 非中秋节或玩家已有月饼，给予巧克力
                    Choco t1 = new Choco();
                    t1.identify();
                    if (t1.doPickUp(Dungeon.hero)) {
                        Messages.get(Dungeon.hero, "you_now_have", t1.name());
                    } else {
                        Dungeon.level.drop(t1, Dungeon.hero.pos).sprite.drop();
                    }
                    
                }
                
                // 标记为已给予物品
                Buff.count(Dungeon.hero, GiftTracker.class, 1);
            } else {
                // 已经给予过物品，根据提示次数显示不同的消息
                HintTracker hintTracker = Dungeon.hero.buff(HintTracker.class);
                float hintCount = (hintTracker != null) ? hintTracker.count() : 0;
                
                if (hintCount < 3) {
                    // 根据提示次数显示不同的消息
                    if (hintCount == 0) {
                        yell(Messages.get(this, "no_more"));
                    } else if (hintCount == 1) {
                        yell(Messages.get(this, "no_more_2"));
                    } else {
                        yell(Messages.get(this, "no_more_3"));
                    }
                    // 增加提示计数器
                    Buff.count(Dungeon.hero, HintTracker.class, 1);
                } else {
                    // 已经提示了3次，显示默认对话
                    yell(Messages.get(this, "what_is_it"));
                }
            }
        }
        return true;
    }
    
    // 检查玩家是否持有月饼
    private boolean hasPlayerMooncake() {
        return Dungeon.hero.belongings.getItem(Choco.class) != null;
    }
    
    @Override
    public String description() {
        return ((FncSprite)sprite).festive ?
                Messages.get(this, "desc_festive")
                : super.description();
    }
}
