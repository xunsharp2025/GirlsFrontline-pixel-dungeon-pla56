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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfGenoise;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.RabbitWeaponGenoise;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Cypros extends MeleeWeapon {
    {
        mode = Mode.TRAVAILLER;
        image = ItemSpriteSheet.TRAVAILLER;
        defaultAction = AC_ZAP;
        usesTargeting = true;

        unique = true;
        bones = false;

        tier= 2;
        ACC = 1.1f; //10% boost to accuracy

        name = Messages.get(this, "name", mode.title());
    }

    @Override
    public String name() {
        return Messages.get(this, "name", mode.title());
    }

    public Wand wand;

    public Item identifyA(){
        return identify();
    }


    boolean canShowDEF = true;
    public enum Mode {
        TRAVAILLER,/*shotgun*/
        CONFIRE,/*rifle*/
        MAGNUM;/*pistol*/

        public String desc(){
            return Messages.get(this, name());
        }
        public String title(){
            return Messages.get(this,name()+".title");
        }
    }

    private static Mode mode;
    public Mode getMode() { return mode; }

    public static final String AC_ZAP	= "ZAP";
    public static final String AC_SWITCH= "SWITCH";
    private static final float STAFF_SCALE_FACTOR = 0.75f;

    public Cypros(){
        Wand wand = new RabbitWeaponGenoise();
        Wand wandold = new WandOfGenoise();

        wand.cursed = false;
        wand.maxCharges = 1;
        wand.curCharges = wand.maxCharges;
        wand.identify(false);
        
        this.wand = wand;
    }

    public void setMode(Mode newMode,boolean doShow) {
        if (newMode == mode) {
            return;
        }

        float timeChange = (mode == Mode.TRAVAILLER ? 3.0f :
                            mode == Mode.CONFIRE    ? 1.0f :
                            mode == Mode.MAGNUM     ? 0.5f :
                            0f);

        mode = newMode;

        switch (mode) {
            case TRAVAILLER: default:
                image = ItemSpriteSheet.TRAVAILLER;
                RCH = 1;
                DLY = 1f;
                DEF = 5;
                DEFUPGRADE = 2;
                timeChange = 2f;
                canShowDEF = true;
                ACC = 1.1f;
                timeChange += 3.0f;
                break;
            case CONFIRE:
                image = ItemSpriteSheet.CONFIRE;
                RCH = 3;
                DLY = 3f;
                ACC = 1.5f;
                canShowDEF = false;
                timeChange += 1f;
                break;
            case MAGNUM:
                image = ItemSpriteSheet.MAGNUMWEDDING;
                RCH = 1;
                DLY = 1f;
                timeChange = 1f;
                ACC = 1.25f;
                canShowDEF = false;
                timeChange += 0.5f;
                break;
        }
        updateQuickslot();
        if (doShow && curUser != null) {
            curUser.spend(timeChange);
            curUser.busy();
            curUser.sprite.operate( curUser.pos );
            curUser.next();
        }
    }

    public final static int CHARMCHANCE = 30;

    @Override
    public int damageRoll(Char owner) {

        Hero hero = (Hero)owner;
        Char enemy = (hero != null) ? hero.enemy() : null;

        if (hero != null && enemy != null) {
            switch (mode) {
                case MAGNUM:
                    if (Random.Int( CHARMCHANCE ) == 0) {
                        Buff.affect( enemy, Charm.class, Random.IntRange( 3, 7 ) ).object = hero.id();
                        enemy.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
                        Sample.INSTANCE.play( Assets.Sounds.CHARMS );
                    }
                    break;
                case CONFIRE:
                    Sample.INSTANCE.play(Assets.Sounds.ZAP);
                    hero.sprite.parent.add(new Beam.DeathRay(hero.sprite.center(), enemy.sprite.center()));
                break;
                case TRAVAILLER: default:
                    break;
            }
        }


        if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
            //deals 85/50/0% toward max to max on surprise with pistol/rifle/shotgun, instead of min to max.
            int diff = max() - min();
            float surpriseMultiplier =  (mode == Mode.MAGNUM)  ? 0.85f :
                                        (mode == Mode.CONFIRE) ? 0.5f :
                                                                 0;
            int damage = augment.damageFactor(Random.NormalIntRange(
                    min() + Math.round(diff * surpriseMultiplier),
                    max()));
            int exStr = hero.STR() - STRReq();
            if (exStr > 0) {
                damage += Random.IntRange(0, exStr);
            }
            return damage;
        }

        return super.damageRoll(owner);
    }

    @Override
    public int min(int lvl) {
        switch (mode) {
            case TRAVAILLER: default:
                return  Math.round(1.5f*tier) +         //base
                        lvl;                            //level scaling
            case CONFIRE:
                return 3*tier + lvl;
            case MAGNUM:
                return tier + lvl;
        }
    }

    @Override
    public int max(int lvl) {

        switch (mode) {
            case TRAVAILLER:
                return  Math.round(2.0f*(tier+1)) +        // 6 base
                        lvl*Math.round(1.1f*(tier+1));    //+3(3.3) per level
            case CONFIRE:
                return  Math.round(6.0f*(tier+1)) +        // 18 base
                        lvl*Math.round(2.5f*(tier+1));    //+8(7.5)= per level
            case MAGNUM:
                return  Math.round(4.5f*(tier+1)) +        // 14 base
                        lvl*Math.round(2.0f*(tier+1));    //+6 per level
            default:
                return super.max(lvl);
        }
    }


    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ZAP);
        actions.add(AC_SWITCH);

        return actions;
    }

    @Override
    public void activate( Char ch ) {
        if(wand != null) wand.charge( ch, STAFF_SCALE_FACTOR );
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_ZAP)){
            wand.execute(hero, AC_ZAP);
        }
        if (action.equals(AC_SWITCH)) {
            WndOptions wndOptions = new WndOptions( Messages.get(Cypros.class, "options.title"),
                                                    Messages.get(this, "options.message", mode.title()),
                                                    Mode.TRAVAILLER.title(), Mode.CONFIRE.title(), Mode.MAGNUM.title()
            ) {
                @Override
                protected void onSelect( int index ) {
                    Mode newMode;
                    switch (index) {
                        case 0: default:
                            newMode = Mode.TRAVAILLER;
                            break;
                        case 1:
                            newMode = Mode.CONFIRE;
                            break;
                        case 2:
                            newMode = Mode.MAGNUM;
                            break;
                    }
                    setMode(newMode,true);
                }
            };
            GameScene.show(wndOptions);
        }
    }

    @Override
    public Emitter emitter() {
        if (wand == null) return null;
        Emitter emitter = new Emitter();
        emitter.pos(12.5f, 3);
        emitter.fillTarget = false;
        emitter.pour(StaffParticleFactory, 0.1f);
        return emitter;
    }

    @Override
    public boolean collect( Bag container ) {
        if (super.collect(container)) {
            if (container.owner != null) {
                wand.charge(container.owner, STAFF_SCALE_FACTOR);
            }
            return true;
        } else {
            return false;
        }
    }

    public void gainCharge( float amt ){
        if (wand != null){
            wand.gainCharge(amt);
        }
    }

    @Override
    public Item upgrade(boolean enchant) {
        super.upgrade( enchant );
        if (wand != null) {
            wand.upgrade();
            wand.gainCharge(0.5f);
            updateQuickslot();
        }

        return this;
    }

    @Override
    public String status() {
        return wand.status();
    }

    @Override
    public String info() {

        String info = super.info();

        if(mode.desc()!="")
        // 모드 별 설명 추가
            info += "\n\n" + mode.desc();
        if(canShowDEF)
            info += "\n\n" + DEFGAIN();

        if(Dungeon.WandLock||wand.lockcharge)
            info += "\n";
        if(Dungeon.WandLock)
            info += "\n_草莓派手雷_的充能已被_锁定为满充能_。";
        if(wand.lockcharge)
            info += "\n_草莓派手雷_的充能已被锁定为_ " + wand.chargeRem + " _点。";

        return info;
    }
    private String DEFGAIN(){
        int DEF = 5;
        int DEFUPGRADE = 2;
        int baseDEF=DEF+DEFUPGRADE*buffedLvl();
        int REM=-2*Math.max(0,STRReq()-hero.STR);
        int DEFGAIN=Math.max(0,baseDEF+REM);
        return Messages.get(Cypros.class,"DEF",DEFGAIN);
    }

    @Override
    public int value() {
        return 0;
    }

    private static final String WAND        = "wand";
    private static final String MODE        = "mode";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(WAND, wand);
        bundle.put(MODE, mode.ordinal());
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        wand = (Wand) bundle.get(WAND);
        Mode newMode;
        switch (bundle.getInt(MODE)) {
            case 0: default:
                newMode = Mode.TRAVAILLER;
                break;
            case 1:
                newMode = Mode.CONFIRE;
                break;
            case 2:
                newMode = Mode.MAGNUM;
                break;
        }
        setMode(newMode,false);
    }

    private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
        @Override
        //reimplementing this is needed as instance creation of new staff particles must be within this class.
        public void emit(Emitter emitter, int index, float x, float y ) {
            MagesStaff.StaffParticle c = (MagesStaff.StaffParticle)emitter.getFirstAvailable(MagesStaff.StaffParticle.class);
            if (c == null) {
                c = new MagesStaff.StaffParticle();
                emitter.add(c);
            }
            c.reset(x,y,wand);
        }

        @Override
        //some particles need light mode, others don't
        public boolean lightMode() {
            return wand.curCharges != 0;
        }
    };
}