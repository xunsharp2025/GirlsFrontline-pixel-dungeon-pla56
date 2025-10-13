package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;  // 移动到这里
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;


public class RedBook extends Artifact{
    public static final String AC_CAST="CAST";
    public static final int[] LEVEL_TO_CHARGE ={3,4,5,7,9,10,12,12,12,12};
    public static final int[] COUNT_TO_UPGRADE={1,2,3,4,5,6,7,8,9,10};

    {
        image = ItemSpriteSheet.REDBOOK;

        levelCap = 10;
        charge = LEVEL_TO_CHARGE[level()];
        chargeCap = LEVEL_TO_CHARGE[level()];

        defaultAction = AC_CAST;
        unique = true;
        bones = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped(hero) && !cursed)
            actions.add(AC_CAST);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_CAST)&& isEquipped(hero)&&!cursed) {
            GameScene.selectCell(targeter);
        }
    }

    private CellSelector.Listener targeter = new CellSelector.Listener(){
        @Override
        public void onSelect(Integer cell) {
            if (cell==null || !Dungeon.level.heroFOV[cell]){
                return;
            }

            Char target=Char.findChar(cell);
            int killed=0;
            
            // 如果选择的是自己的位置（修复条件判断）
            if (cell == curUser.pos) {
                // 消耗3点充能对自己使用（与对敌人相同）
                if (charge >= 3) {
                    charge -= 3;
                    // 调用deadBomb方法，实现伤害和恐惧效果
                    killed=deadBomb(3, curUser);
                    // 添加祝福buff
                    Buff.prolong(curUser, Bless.class, Bless.DURATION);
                    // 确保消息能显示（如果没有定义，先使用临时消息）
                    GLog.p(Messages.get(RedBook.class, "blessed"));
                } else {
                    return;
                }
            }
            // 处理其他目标
            else if (target!=null) {
                if(target.alignment==Char.Alignment.ALLY){
                    if(charge<5){
                        return;
                    }
    
                    charge-=5;
                    killed=deadBomb(level()==levelCap?3:2,target);
                }else if(charge>=3){
                    charge-=3;
                    killed=deadBomb(0,target);
                }else{
                    return;
                }
            } else {
                return;
            }

            // 统一的升级和后续处理
            if(level()<levelCap && killed>=COUNT_TO_UPGRADE[level()]){
                upgrade();
            }

            updateQuickslot();
            Dungeon.hero.spendAndNext(1f);
            Talent.onArtifactUsed(Dungeon.hero);
        }

        @Override
        public String prompt() {
            return Messages.get(RedBook.class, "prompt");
        }
    };

    public int deadBomb(int range,Char target){
        Char fear=target;
        if(target.alignment!=Char.Alignment.ALLY){
            fear=curUser;
        }

        new Flare( 5, 32 ).color( 0xFF0000, true ).show(target.sprite,2f);
        Sample.INSTANCE.play( Assets.Sounds.READ );
        Invisibility.dispel();

        int killed = 0;
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (Dungeon.level.distance(target.pos, mob.pos) <= range) {
                if(!mob.isAlive()||Mob.Alignment.ENEMY!=mob.alignment){
                    continue;
                }

                Buff.affect( mob, Terror.class, Terror.DURATION ).object = fear.id();

                int damage=Math.round(mob.HT/2f+mob.HP/2f);
                if      (mob.properties().contains(Mob.Property.MINIBOSS)){
                    damage=mob.HT/2+1;
                }else if(mob.properties().contains(Mob.Property.BOSS)){
                    damage=mob.HP/2;
                }
                mob.damage(damage,this);

                if(!mob.isAlive()){
                    killed++;
                }
            }
        }

        GameScene.flash( 0xFFFFFF );

        Sample.INSTANCE.play( Assets.Sounds.BLAST );
        Invisibility.dispel();

        return killed;
    }

    @Override
    public void charge(Hero target, float amount) {
        if (charge < chargeCap){
            partialCharge += 0.15f*amount;
            if (partialCharge >= 1){
                partialCharge--;
                charge++;
                updateQuickslot();
            }
        }
    }

    @Override
    public Item upgrade() {
        chargeCap=LEVEL_TO_CHARGE[level()+1];
        if      (level()+1 <= 1) image = ItemSpriteSheet.REDBOOK;
        else if (level()+1 <= 4) image = ItemSpriteSheet.REDBOOK2;
        else if (level()+1 >= 5) image = ItemSpriteSheet.REDBOOK3;
        return super.upgrade();
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        if      (level()   <= 1) image = ItemSpriteSheet.REDBOOK;
        else if (level()   <= 4) image = ItemSpriteSheet.REDBOOK2;
        else if (level()   >= 5) image = ItemSpriteSheet.REDBOOK3;
    }

    public String desc() {
        return Messages.get(this, "desc");
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new BookRecharge();
    }

    public class BookRecharge extends ArtifactBuff{
        @Override
        public boolean act() {
            LockedFloor lock = target.buff(LockedFloor.class);
            if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {
                //110 turns to charge at full, 30 turns to charge at 0/10
                float chargeGain = 1 / (110f - (chargeCap - charge)*8f);
                chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
                partialCharge += chargeGain;

                if (partialCharge >= 1) {
                    partialCharge --;
                    charge ++;

                    if (charge == chargeCap){
                        partialCharge = 0;
                    }
                }
            }

            updateQuickslot();
            spend( TICK );
            return true;
        }
    }
}
