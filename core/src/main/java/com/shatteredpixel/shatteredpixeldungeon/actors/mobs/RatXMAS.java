//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.items.XMasGift;
import com.shatteredpixel.shatteredpixeldungeon.items.food.XMasSugar;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatXMASSprite;
import com.watabou.utils.Random;

public class RatXMAS extends Rat{
    public RatXMAS() {
        this.PASSIVE = new Passive();
        spriteClass = RatXMASSprite.class;
        state=PASSIVE;
        lootChance = 0f;
        EXP = 2;
        maxLvl = Hero.MAX_LEVEL;
    }

    @Override
    protected boolean act() {
        if (Dungeon.hero.armorAbility instanceof Ratmogrify){
            alignment = Alignment.ALLY;
            if (state == PASSIVE) state = WANDERING;
        }else if(alignment != Alignment.ALLY){
            if(state==HUNTING||state == WANDERING){
                state=FLEEING;
            }
        }
        return super.act();
    }

    @Override
    public void damage( int dmg, Object src ) {
        //受伤改变立场
        if (alignment != Alignment.ALLY&&state == PASSIVE) {
            state = FLEEING;
        }

        super.damage( dmg, src );
    }

    @Override
    public void add(Buff buff) {
        //获得恶性buff改变立场
        super.add(buff);
        if (alignment != Alignment.ALLY&&state == PASSIVE && buff.type == Buff.buffType.NEGATIVE){
            state = FLEEING;
        }
    }

    @Override
    public float speed() {
        //根据立场改变速度
        if (state == FLEEING) return super.speed()*2;
        else return super.speed();
    }

    @Override
    public void rollToDropLoot() {
        super.rollToDropLoot();
        if(Dungeon.LimitedDrops.XMAS_GIFT.count==0){
            if(Random.Int(5)==0){
                Dungeon.level.drop(new XMasGift(), pos).sprite.drop();
                Dungeon.LimitedDrops.XMAS_GIFT.count++;
            }
        }
        if(suagr()){
            Dungeon.level.drop(new XMasSugar(), pos).sprite.drop();
            Dungeon.LimitedDrops.XMAS_SUGAR.count++;
        }
    }
    private boolean suagr(){
        switch (Dungeon.LimitedDrops.XMAS_SUGAR.count){
            case 0:return true;
            case 1:return Random.Int(10)<6;
            case 2:return Random.Int(10)<3;
            default:return false;
        }
    }

    public void beckon(int cell) {
        if (this.state != this.PASSIVE) {
            super.beckon(cell);
        } else {
            this.target = cell;
        }
    }

    protected class Passive extends Mob.Wandering {
        public boolean act(boolean enemyInFOV, boolean justAlerted){
            if (RatXMAS.this.state==RatXMAS.this.PASSIVE){
                continueWandering();
            }
            return true;
        }
        protected boolean continueWandering() {
            return super.continueWandering();
        }
    }
}
