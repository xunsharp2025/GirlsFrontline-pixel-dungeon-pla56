package com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class TestLevelSetting extends TestItem {
    {
        image = ItemSpriteSheet.TOKEN;
        defaultAction = AC_SELECT;
    }

    private static final String AC_SET = "set";
    private static final String AC_SELECT = "select";

    private int level ;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SET);
        actions.add(AC_SELECT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SET)){
            setLevel();;
        }
        if(action.equals(AC_SELECT)){
            GameScene.selectItem( itemSelector );
        }
    }
    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(this, "select_title");
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
                if(!(level > 0)){
                    level = 0;
                }
                if(levela>level){
                    item.degrade(levela-level);
                }
                if(levela<level){
                    item.upgrade(level - levela);
                }
                Sample.INSTANCE.play( Assets.Sounds.READ );
            }
        }
    };

    @Override
    public void storeInBundle(Bundle b){
        super.storeInBundle(b);
        b.put("level", level);
    }

    @Override
    public void restoreFromBundle(Bundle b){
        super.restoreFromBundle(b);
        level = b.getInt("level");
    }

    /**
     * 武器设置窗口类，继承自Window类。
     */
    private void setLevel(){
        Game.runOnRenderThread(()->GameScene.show(
                new WndTextNumberInput(
                        Messages.get(TestLevelSetting.class, "level"),
                        Messages.get(TestLevelSetting.class, "level_desc"),
                        Integer.toString(level),
                        4,
                        false,
                        Messages.get(TestLevelSetting.class, "yes"),
                        Messages.get(TestLevelSetting.class, "no"),
                        false){
                    public void onSelect(boolean check, String text){
                        if (check && text.matches("\\d+")){
                            level = Math.min(Integer.parseInt(text), 6666);
                        }
                    }
                }));
    }
}

