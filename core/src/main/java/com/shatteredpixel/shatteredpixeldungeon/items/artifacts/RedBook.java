package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
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
    public static final float[] LEVEL_TO_DAMAGE ={0.1f,0.2f,0.3f,0.4f,0.5f,0.6f};

    {
        image = ItemSpriteSheet.REDBOOK;

        levelCap = 5;
        exp = 0;
        charge = 3+level();
        chargeCap = 3+level();

        defaultAction = AC_CAST;
        unique = true;
        bones = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        if ((isEquipped( hero ) || hero.hasTalent(Talent.Type56Three_Book))
                && !cursed && (charge > 0 || activeBuff != null)){
            actions.add(AC_CAST);
        }
        return actions;
    }
    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)){
            if (!collect || !hero.hasTalent(Talent.Type56Three_Book)){
                if (activeBuff != null){
                    activeBuff.detach();
                    activeBuff = null;
                }
            } else {
                activate(hero);
            }

            return true;
        } else
            return false;
    }
    @Override
    public boolean collect( Bag container ) {
        if (super.collect(container)){
            if (container.owner instanceof Hero
                    && passiveBuff == null
                    && ((Hero) container.owner).hasTalent(Talent.Type56Three_Book)){
                activate((Hero) container.owner);
            }
            return true;
        } else{
            return false;
        }
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_CAST)) {
            if (!isEquipped(hero) && !hero.hasTalent(Talent.Type56Three_Book)) GLog.i( Messages.get(Artifact.class, "need_to_equip") );
            else if (cursed)       GLog.i( Messages.get(this, "cursed") );
            else if (charge <= 0)  GLog.i( Messages.get(this, "no_charge") );
            else {
                GameScene.selectCell(targeter);
            }
        }
        lockchB();
    }

    private CellSelector.Listener targeter = new CellSelector.Listener(){
        @Override
        public void onSelect(Integer cell) {
            if (cell==null || !Dungeon.level.heroFOV[cell]){
                return;
            }

            Char target=Char.findChar(cell);
            if (target==null || !(target instanceof Mob || target instanceof Hero)){
                return;
            }

            if(target.alignment==Char.Alignment.ALLY){
                // 如果目标是友方，则目标添加祝福、自身获得照明
                Buff.affect(target, Bless.class, 5f);
                Buff.affect(Dungeon.hero, Light.class, 10f);
                if(Dungeon.hero.hasTalent(Talent.Type56Two_Exclusive)){
                    int shield = 5 + 5 * Dungeon.hero.pointsInTalent(Talent.Type56Two_Exclusive);
                    if(target == Dungeon.hero) {
                        //目标是自身，则自身获得护盾
                        Buff.affect(Dungeon.hero, Barrier.class).setShield(shield);
                    }else {
                        if(Dungeon.hero.shielding()<5){
                            shield-=5-Dungeon.hero.shielding();
                            Buff.affect(Dungeon.hero, Barrier.class).setShield(5);
                        }
                        //作用在友方其他单位，则玩家先获得最多5点盾，然后目标获得收益
                        if(shield<=target.HT-target.HP) {
                            //已损失血量大于护盾值，则护盾值转为治疗
                            target.HP += shield;
                        }else {
                            //治疗后溢出的数值转化为盾
                            shield-=target.HT-target.HP;
                            target.HP=target.HT;
                            Buff.affect(target, Barrier.class).setShield(shield);
                        }
                    }
                }
            }else if(target.alignment==Char.Alignment.ENEMY){
                deadBomb(target);
            }else if(target.alignment==Char.Alignment.NEUTRAL&&(target instanceof Mimic||target instanceof Bee)){
                deadBomb(target);
            }else {
                GLog.n("这是不可选中的单位");
                return;
            }

            charge-=1;
            updateQuickslot();
            Dungeon.hero.spendAndNext(1f);
            Talent.onArtifactUsed(Dungeon.hero);
        }

        @Override
        public String prompt() {
            return Messages.get(RedBook.class, "prompt");
        }
    };

    public void deadBomb(Char target){
        Char fear=Dungeon.hero;
        if(!target.isAlive()||Mob.Alignment.ALLY==target.alignment){
            //并非存活或者目标是友方单位，属于误入这个函数，将直接退出
            GLog.n("如果看到这里，请联系制作组反馈");
            return;
        }

        if (Dungeon.hero.hasTalent(Talent.Type56Two_Item)) {
            int dur = 5 + 5*Dungeon.hero.pointsInTalent(Talent.Type56Two_Item);
            Buff.append(Dungeon.hero, TalismanOfForesight.CharAwareness.class, dur).charID = target.id();
        }
        new Flare( 5, 32 ).color( 0xFF0000, true ).show(target.sprite,2f);
        Sample.INSTANCE.play( Assets.Sounds.READ );
        Invisibility.dispel();

        int damage = (int) Math.ceil(target.HP * LEVEL_TO_DAMAGE[level()]);
        if (target.properties().contains(Mob.Property.MINIBOSS)) {
            damage = Math.min(damage, (int) (target.HP * 0.5f));
        } else if (target.properties().contains(Mob.Property.BOSS)) {
            damage = Math.min(damage, (int) (target.HP * 0.3f));
        }
        target.damage(damage, this);

        Buff.affect(target, Terror.class, 3).object = fear.id();

        GameScene.flash( 0xFFFFFF );

        Sample.INSTANCE.play( Assets.Sounds.BLAST );
        Invisibility.dispel();

    }

    @Override
    public void charge(Hero target, float amount) {
        if (charge < chargeCap){
            if (!isEquipped(target)) amount *= 0.75f*target.pointsInTalent(Talent.Type56Three_Book)/3f;
            partialCharge += 0.15f*amount;
            while (partialCharge >= 1){
                partialCharge--;
                charge++;
                updateQuickslot();
            }
        }
    }

    @Override
    public Item upgrade() {
        chargeCap++;
        if      (level() <= 2) image = ItemSpriteSheet.REDBOOK;
        else if (level() <= 4) image = ItemSpriteSheet.REDBOOK2;
        else if (level() >= 5) image = ItemSpriteSheet.REDBOOK3;
        return super.upgrade();
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        if      (level() <= 2) image = ItemSpriteSheet.REDBOOK;
        else if (level() <= 4) image = ItemSpriteSheet.REDBOOK2;
        else if (level() >= 5) image = ItemSpriteSheet.REDBOOK3;
    }

    public String desc() {
        return Messages.get(this, "desc");
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new BookRecharge();
    }

    public void overCharge(int amount, boolean over) {
        if (over)
            charge = Math.min(charge + amount, chargeCap + amount);
        else
            charge = Math.min(charge+amount, chargeCap);
        updateQuickslot();
    }

    public class BookRecharge extends ArtifactBuff{
        @Override
        public boolean act() {
            lockcha();
            LockedFloor lock = target.buff(LockedFloor.class);
            if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {
                //110 turns to charge at full, 30 turns to charge at 0/10
                float chargeGain = 1 / (110f - (chargeCap - charge)*10f);
                chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
                if (!isEquipped(Dungeon.hero)){
                    chargeGain *= 0.75f*Dungeon.hero.pointsInTalent(Talent.Type56Three_Book)/3f;
                }
                partialCharge += chargeGain;

                while (partialCharge >= 1) {
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
        public void gainExp( float levelPortion ) {
            if (cursed || levelPortion == 0) return;

            exp += Math.round(levelPortion*100);

            int per_exp = 120;
            if (exp > (level()+1)*per_exp && level() < levelCap){
                exp -= (level()+1)*per_exp;
                GLog.p( Messages.get(this, "levelup") );
                upgrade();
                chargeCap = 3+level();
            }

        }
    }
}
