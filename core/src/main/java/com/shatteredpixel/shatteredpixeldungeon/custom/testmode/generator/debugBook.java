package com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LostBackpack;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.Brew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.Elixir;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Cypros;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.WndTextInput;
import com.shatteredpixel.shatteredpixeldungeon.ui.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStartGame;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class debugBook extends TestItem {
    {
        image = ItemSpriteSheet.SCROLL_HOLDER;
        defaultAction =AC_SETMODE;
    }
    private static final String AC_SETMODE = "setmode";
    private static final String AC_APPLY = "apply";
    private static int modeA = 0;
    private static String AC_EXP = "EXP";
    private static String AC_STR = "STR";
    private static String AC_LVL = "LVL";
    private static String AC_MOB = "MOB";
    private static String AC_CHA = "CHA";
    @Override
    public String name(){
        return Messages.get(this, "name") + modeName();
    }
    private String modeName(){
        switch(modeA)
        {
            case 1:
                return "-"+Messages.get(this, "ac_exp");
            case 2:
                return "-"+Messages.get(this, "ac_str");
            case 3:
                return "-"+Messages.get(this, "ac_lvl");
            case 4:
                return "-"+Messages.get(this, "ac_ign");
            case 5:
                return "-"+Messages.get(this, "ac_mob");
            case 6:
                return "-"+Messages.get(this, "ac_cha");
            case 7:
                return "-"+Messages.get(this, "ac_reset");
            default:
                return "";
        }
    }
    private String modedesc(){
        switch(modeA)
        {
            case 1:
                return Messages.get(this, "exp");
            case 2:
                return Messages.get(this, "str");
            case 3:
                return Messages.get(this, "lvl");
            case 4:
                return Messages.get(this, "ign");
            case 5:
                return Messages.get(this, "mob");
            case 6:
                return Messages.get(this, "cha",chadesc());
            case 7:
                return Messages.get(this, "reset");
            default:
                return "";
        }
    }
    private String chadesc(){
        switch (chargemode){
            case 1:
                return Messages.get(this,"cha_rem");
            case 2:
                return Messages.get(this,"cha_lock");
            case 0: default:
                return Messages.get(this,"cha_full");
        }
    }
    @Override
    public String desc(){
        String desc =super.desc();
        desc+=modedesc();
        return desc;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SETMODE);
        if(modeA!=0)
            actions.add(AC_APPLY);
        switch (modeA){
            case 0: default:
                defaultAction=AC_SETMODE;
                break;
            case 1:
                actions.add(AC_EXP);
                defaultAction=AC_EXP;
                break;
            case 2:
                actions.add(AC_STR);
                defaultAction=AC_STR;
                break;
            case 3:
                actions.add(AC_LVL);
                defaultAction=AC_LVL;
                break;
            case 4:
                defaultAction=AC_APPLY;
                break;
            case 5:
                actions.add(AC_MOB);
                defaultAction=AC_MOB;
            case 6:
                actions.add(AC_CHA);
                defaultAction=AC_CHA;
            case 7:
                defaultAction=AC_APPLY;
                break;

        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SETMODE)) {
            setMode();
        } else if (action.equals(AC_EXP)) {
            setEXP();
        } else if (action.equals(AC_STR)) {
            setSTR();
        } else if (action.equals(AC_LVL)) {
            setLVL();
        } else if (action.equals(AC_MOB)) {
            setMOB();
        } else if (action.equals(AC_CHA)) {
            setCHA();
        }else if(action.equals(AC_APPLY)){
            switch (modeA){
                case 1:
                    updateEXP();
                    break;
                case 2:
                    updateSTR();
                    break;
                case 3:
                    GameScene.selectItem(itemLVL);
                    break;
                case 4:
                    GameScene.selectItem(itemIGN);
                    break;
                case 5:
                    mobAPPLY();
                    break;
                case 6:
                    GameScene.selectItem(itemCHA);
                    break;
                case 7:
                    resetLevel();
                    break;
                case 0: default:
                    break;
            }
        }
    }

    private void setMode(){
        GirlsFrontlinePixelDungeon.scene().addToFront(
                new WndTextNumberInput(
                        Messages.get(this, "mode_title"),
                        Messages.get(this, "mode_body"),
                        Integer.toString(modeA),
                        20,
                        false,
                        Messages.get(this, "yes"),
                        Messages.get(this, "no"),
                        false
                ){
                    @Override
                    public void onSelect(boolean check, String text) {
                        if (check && text.matches("\\d+")){
                            modechange(Integer.parseInt(text));
                        }
                    }
                }
        );
    }
    private void modechange(int mode){
        if(mode == 1) {
            modeA = 1;
            defaultAction = AC_EXP;
            GLog.p(Messages.get(this,"modetext",Messages.get(this,"ac_exp")));
            updateQuickslot();
        }
        else if(mode==2){
            modeA = 2;
            defaultAction = AC_STR;
            GLog.p(Messages.get(this,"modetext",Messages.get(this,"ac_str")));
            updateQuickslot();
        }
        else if(mode==3){
            modeA = 3;
            defaultAction = AC_LVL;
            GLog.p(Messages.get(this,"modetext",Messages.get(this,"ac_lvl")));
            updateQuickslot();
        }
        else if(mode==4){
            modeA = 4;
            defaultAction = AC_APPLY;
            GLog.p(Messages.get(this,"modetext",Messages.get(this,"ac_ign")));
            updateQuickslot();
        }
        else if(mode==5){
            modeA = 5;
            defaultAction = AC_MOB;
            GLog.p(Messages.get(this,"modetext",Messages.get(this,"ac_mob")));
            updateQuickslot();
        }
        else if(mode==6){
            modeA = 6;
            defaultAction = AC_CHA;
            GLog.p(Messages.get(this,"modetext",Messages.get(this,"ac_cha")));
            updateQuickslot();
        }
        else if(mode == 7){
            modeA = 7;
            defaultAction = AC_APPLY;
            GLog.p(Messages.get(this,"modetext",Messages.get(this,"ac_reset")));
            updateQuickslot();
        }
        else {
            modeA = 0;
            defaultAction = AC_SETMODE;
            GLog.p(Messages.get(this,"modetext",Messages.get(this,"ac_setmode")));
            updateQuickslot();
        }
    }

    private int exp;
    private void setEXP(){
        Game.runOnRenderThread(()-> GameScene.show(
                new WndTextNumberInput(
                        Messages.get(this, "exp_title"),
                        Messages.get(this, "exp_body"),
                        Integer.toString(exp),
                        4,
                        false,
                        Messages.get(this, "yes"),
                        Messages.get(this, "no"),
                        false){
                    public void onSelect(boolean check, String text){
                        if (check && text.matches("\\d+")){
                            exp = Math.min(Integer.parseInt(text), 40);
                            defaultAction = AC_APPLY;
                            updateQuickslot();
                        }
                    }
                }));
    }
    private void updateEXP(){
        if(exp<=0)
            exp=1;
        if(exp-hero.lvl!=0){
            new PotionOfExperience().apply(hero);
            hero.lvl = exp;
            hero.attackSkill = 10 + exp - 1;
            hero.defenseSkill = 5 + exp - 1;
            Sample.INSTANCE.play( Assets.Sounds.READ );
            hero.updateHT( true );
        }
    }

    private int str;
    private void setSTR(){
        Game.runOnRenderThread(()-> GameScene.show(
                new WndTextNumberInput(
                        Messages.get(this, "str_title"),
                        Messages.get(this, "str_body"),
                        Integer.toString(str),
                        4,
                        false,
                        Messages.get(this, "yes"),
                        Messages.get(this, "no"),
                        false){
                    public void onSelect(boolean check, String text){
                        if (check && text.matches("\\d+")){
                            str = Integer.parseInt(text);
                            defaultAction = AC_APPLY;
                            updateQuickslot();
                        }
                    }
                }));
    }
    private void updateSTR(){
            new PotionOfStrength().apply(hero);
            hero.STR = str;
            Sample.INSTANCE.play( Assets.Sounds.READ );
    }
    private int lvl;
    private void setLVL(){
        Game.runOnRenderThread(()->GameScene.show(
                new WndTextNumberInput(
                        Messages.get(this, "lvl_title"),
                        Messages.get(this, "lvl_body"),
                        Integer.toString(lvl),
                        4,
                        false,
                        Messages.get(this, "yes"),
                        Messages.get(this, "no"),
                        false){
                    public void onSelect(boolean check, String text){
                        if (check && text.matches("\\d+")){
                            lvl = Math.min(Integer.parseInt(text), 6666);
                            defaultAction = AC_APPLY;
                            updateQuickslot();
                        }
                    }
                }));
    }
    protected WndBag.ItemSelector itemLVL = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(this, "lvl_select");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item.isUpgradable()||item instanceof BrokenSeal;
        }

        @Override
        public void onSelect( Item item ) {
            if(item != null){
                if(!(lvl > 0)){
                    lvl = 0;
                }
                item.level(lvl);
                Sample.INSTANCE.play( Assets.Sounds.READ );
            }
        }
    };
    protected WndBag.ItemSelector itemIGN = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(this, "ign_select");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return (item instanceof EquipableItem &&!(item instanceof MissileWeapon))||item instanceof Wand||item instanceof Scroll ||(item instanceof Potion &&!(item instanceof Brew)&&!(item instanceof Elixir)&&!(item instanceof AlchemicalCatalyst));
        }

        @Override
        public void onSelect( Item item ) {

            if(item != null){
                if (item instanceof Ring&&!item.levelKnown&&!item.cursedKnown){
                    Ring.initGems();
                }
                if(item instanceof EquipableItem ||item instanceof Wand){
                    item.levelKnown = false;
                    item.cursedKnown = false;
                }else if(item instanceof Scroll){
                    Scroll.initLabels();
                }else if(item instanceof Potion){
                    Potion.initColors();
                }
                Sample.INSTANCE.play( Assets.Sounds.READ );
            }
        }
    };
    private int mobA = 2;
    private void setMOB(){
        Game.runOnRenderThread(()-> GameScene.show(
                new WndTextNumberInput(
                        Messages.get(this, "mob_title"),
                        Messages.get(this, "mob_body"),
                        String.valueOf(Dungeon.mobRan),
                        4,
                        false,
                        Messages.get(this, "yes"),
                        Messages.get(this, "no"),
                        false){
                    public void onSelect(boolean check, String text){
                        if (check && text.matches("\\d+")){
                            mobA = Integer.parseInt(text);
                            defaultAction = AC_APPLY;
                            updateQuickslot();
                        }
                    }
                }));
    }
    private void mobAPPLY(){
        Dungeon.mobRan=mobA;
    }
    private int chargemode = 0;
    private void setCHA(){
        Game.runOnRenderThread(()->GameScene.show(
                new WndTextNumberInput(
                        Messages.get(this, "cha_title"),
                        Messages.get(this, "cha_body"),
                        Integer.toString(0),
                        4,
                        false,
                        Messages.get(this, "yes"),
                        Messages.get(this, "no"),
                        false){
                    public void onSelect(boolean check, String text){
                        if (check && text.matches("\\d+")){
                            chargemode = Math.min(Integer.parseInt(text), 2);
                            chargemode = Math.max(chargemode, 0);
                            GLog.p(Messages.get(this, "changecha"),chadesc());
                            defaultAction = AC_APPLY;
                            updateQuickslot();
                        }
                    }
                }));
    }
    protected WndBag.ItemSelector itemCHA = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(this, "cha_select");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof ClassArmor||item instanceof Wand||(item instanceof Artifact&&item.getClass()!= ChaliceOfBlood.class)
                    || item instanceof MagesStaff || item instanceof Cypros;
        }

        @Override
        public void onSelect( Item item ) {
            if(item != null){
                switch (chargemode){
                    case 1:
                        lockcharge(item);
                        break;
                    case 2:
                        lockClass(item);
                        break;
                    case 0: default:
                        fullcharge(item);
                        break;
                }
                Sample.INSTANCE.play( Assets.Sounds.READ );
            }
        }
    };
    private void fullcharge(Item item){
        Item itemB;
        if (item instanceof MagesStaff){
            itemB = ((MagesStaff) item).wand;
        }
        else if(item instanceof Cypros){
            itemB = ((Cypros) item).wand;
        }
        else {
            itemB = item;
        }

        if (itemB instanceof ClassArmor){
            ((ClassArmor) itemB).charge = 100;
        }
        else if (itemB instanceof Wand){
            ((Wand) itemB).curCharges = ((Wand) itemB).maxCharges;
        }
        else if (itemB instanceof Artifact){
            if(((Artifact) itemB).chargeCap>0)
                ((Artifact) itemB).charge = ((Artifact) itemB).chargeCap;
        }
        updateQuickslot();
    }
    private void lockcharge(Item item){
        Item itemB;
        if (item instanceof MagesStaff){
            itemB = ((MagesStaff) item).wand;
        }
        else if(item instanceof Cypros){
            itemB = ((Cypros) item).wand;
        }else {
            itemB = item;
        }

        if (itemB instanceof ClassArmor){
            if(((ClassArmor) itemB).lockcharge){
                ((ClassArmor) itemB).lockcharge = false;
                ((ClassArmor) itemB).chargeRem = 0;
                GLog.n(Messages.get(this,"cha_singleunlock"));
                //先解除后清除登记，以免改变当前充能
            }
            else {
                ((ClassArmor) itemB).chargeRem = ((ClassArmor) itemB).charge;
                ((ClassArmor) itemB).lockcharge = true;
                GLog.p(Messages.get(this,"cha_singlelock"));
                //先等级后启动，以免丢失当前充能
            }
        }
        else if (itemB instanceof Wand){
            if(((Wand) itemB).lockcharge){
                ((Wand) itemB).lockcharge = false;
                ((Wand) itemB).chargeRem = 0;
                GLog.n(Messages.get(this,"cha_singleunlock"));
            }else {
                ((Wand) itemB).chargeRem = ((Wand) itemB).curCharges;
                ((Wand) itemB).lockcharge = true;
                GLog.p(Messages.get(this,"cha_singlelock"));
            }
        }
        else if (itemB instanceof Artifact){
            if(((Artifact) itemB).lockcharge){
                ((Artifact) itemB).lockcharge=false;
                ((Artifact) itemB).chargeRem= 0;
                GLog.n(Messages.get(this,"cha_singleunlock"));
            }else {
                ((Artifact) itemB).chargeRem= ((Artifact) itemB).charge;
                ((Artifact) itemB).lockcharge=true;
                GLog.p(Messages.get(this,"cha_singlelock"));
            }
        }
        updateQuickslot();
    }
    private void lockClass(Item item){
        Item itemB;
        if (item instanceof MagesStaff){
            itemB = ((MagesStaff) item).wand;
        }
        else if(item instanceof Cypros){
            itemB = ((Cypros) item).wand;
        }
        else {
            itemB = item;
        }

        if (itemB instanceof ClassArmor){
            if(Dungeon.ArmorLock){
                Dungeon.ArmorLock = false;
                GLog.n(Messages.get(this, "cha_armorunlock"));
            }else {
                Dungeon.ArmorLock = true;
                GLog.p(Messages.get(this, "cha_armorlock"));
            }
        }
        else if (itemB instanceof Wand){
            if(Dungeon.WandLock){
                Dungeon.WandLock = false;
                GLog.n(Messages.get(this, "cha_wandunlock"));
            }else {
                Dungeon.WandLock = true;
                GLog.p(Messages.get(this, "cha_wandlock"));
            }
        }
        else if (itemB instanceof Artifact){
            if(Dungeon.ArtifactLock){
                Dungeon.ArtifactLock = false;
                GLog.n(Messages.get(this, "cha_artifactunlock"));
            }else {
                Dungeon.ArtifactLock = true;
                GLog.p(Messages.get(this, "cha_artifactlock"));
            }
        }
        updateQuickslot();
    }
    private void resetLevel(){
        InterlevelScene.returnDepth = Dungeon.depth;
        InterlevelScene.mode = InterlevelScene.Mode.RESET;
        Game.switchScene( InterlevelScene.class );
    }
}