package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.ui.WndTextNumberInput;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class MapEditor extends TestItem {
	private int terrain=Terrain.EMPTY;

    {
        image = ItemSpriteSheet.AQUA_BLAST;
        defaultAction = AC_PUT;
    }

    private static final String AC_PUT = "put";
    private static final String AC_SET = "set";
    //private static final String AC_GET = "ac_get";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_PUT);
        actions.add(AC_SET);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {
        super.execute( hero, action );
        if      (action.equals(AC_PUT)){
        	putTerrain();
        }else if(action.equals(AC_SET)){
        	setTerrain();
        }
    }

    private void putTerrain(){
    	GameScene.selectCell(new CellSelector.Listener() {
            @Override
            public void onSelect(final Integer cell) {
                if (cell == null) {
                    return;
                }

                int x=cell%Dungeon.level.width();
                int y=cell/Dungeon.level.width();
                if(x<1||x>=(Dungeon.level.width ()-1)
            	|| y<1||y>=(Dungeon.level.height()-1)){
                	return;
            	}
                
                Dungeon.level.set(cell,terrain);
                GameScene.updateMap(cell);
                Dungeon.observe();
            }

            @Override
            public String prompt() {
            	return "choose a place";
            }
        });
    }

    private void setTerrain(){
    	Game.runOnRenderThread(()->GameScene.show(
    		new WndTextNumberInput("title","desc",Integer.toString(terrain),5, false,"yes","no",false){
		        @Override
		        public void onSelect(boolean check, String text){
		            if (check && text.matches("\\d+")){
		                terrain = Integer.parseInt(text);
		            }
		        }
	    	}
		));
    }
}