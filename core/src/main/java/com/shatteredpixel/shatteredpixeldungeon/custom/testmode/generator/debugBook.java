package com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
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
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
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
            default:
                return "";
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
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SETMODE)){
            setMode();
        }else if(action.equals(AC_EXP)){
            setEXP();
        }else if(action.equals(AC_STR)){
            setSTR();
        }else if(action.equals(AC_LVL)){
            setLVL();
        }else if(action.equals(AC_MOB)){
            setMOB();
        }else if(action.equals(AC_APPLY)){
            switch (modeA){
                case 1:
                    updateEXP();
                    break;
                case 2:
                    updateSTR();
                    break;
                case 3:
                    GameScene.selectItem( itemLVL);
                    break;
                case 4:
                    GameScene.selectItem( itemIGN);
                    break;
                case 5:
                    mobAPPLY();
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
            updateQuickslot();
        }
        else if(mode==2){
            modeA = 2;
            defaultAction = AC_STR;
            updateQuickslot();
        }
        else if(mode==3){
            modeA = 3;
            defaultAction = AC_LVL;
            updateQuickslot();
        }
        else if(mode==4){
            modeA = 4;
            defaultAction = AC_APPLY;
            updateQuickslot();
        }
        else if(mode==5){
            modeA = 5;
            defaultAction = AC_MOB;
            updateQuickslot();
        }else {
            modeA = 0;
            defaultAction = AC_SETMODE;
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
                int levela = item.level();
                if(!(lvl > 0)){
                    lvl = 0;
                }
                if(levela>lvl){
                    item.degrade(levela-lvl);
                }
                if(levela<lvl){
                    item.upgrade(lvl - levela);
                }
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

            if (item instanceof Ring&&!item.levelKnown&&!item.cursedKnown){
                Ring.initGems();
            }
            if(item != null){
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
}