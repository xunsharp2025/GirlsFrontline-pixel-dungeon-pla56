package com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class TestExperience extends TestItem {
    {
        image = ItemSpriteSheet.BLOB;
        defaultAction = AC_SET;
    }

    private static final String AC_SET = "set";

    private int level ;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SET);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SET)){
            setLevel();
        }
    }
    private void updateLevel(){
        if(level>0&&level-hero.lvl!=0){
            new PotionOfExperience().apply(hero);
            hero.lvl = level;
            Sample.INSTANCE.play( Assets.Sounds.READ );
            hero.updateHT( true );
        }
    }

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
                        Messages.get(TestExperience.class, "level"),
                        Messages.get(TestExperience.class, "level_desc"),
                        Integer.toString(level),
                        4,
                        false,
                        Messages.get(TestExperience.class, "yes"),
                        Messages.get(TestExperience.class, "no"),
                        false){
                    public void onSelect(boolean check, String text){
                        if (check && text.matches("\\d+")){
                            level = Math.min(Integer.parseInt(text), 40);
                            updateLevel();
                        }
                    }
                }));
    }
}

