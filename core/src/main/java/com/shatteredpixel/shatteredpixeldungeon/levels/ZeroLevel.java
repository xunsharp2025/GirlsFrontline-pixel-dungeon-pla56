package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.SceneSwitcher;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.WindowTrigger;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Teleporter;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AboutScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.BadgesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSaveSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import java.io.IOException;

public class ZeroLevel extends Level {
	private static final int SIZE = 17;
	private static final int TEMP_MIN = 2;
	private static final int TEMP_MAX = SIZE-3;

	private static final int saveSlot1=TEMP_MIN*SIZE+TEMP_MIN   ;
	private static final int saveSlot2=TEMP_MIN*SIZE+TEMP_MIN+ 2;
	private static final int saveSlot3=TEMP_MIN*SIZE+TEMP_MIN+ 4;
	private static final int saveSlot4=TEMP_MIN*SIZE+TEMP_MIN+ 6;
	private static final int saveSlot5=TEMP_MIN*SIZE+TEMP_MIN+ 8;
	private static final int saveSlot6=TEMP_MIN*SIZE+TEMP_MIN+10;

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_ZERO_LEVEL;
	}
	
	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
	}
	
	@Override
	protected boolean build() {
		setSize(SIZE, SIZE);

		for(int i=1;i<SIZE-1;i++){
			for(int j=1;j<SIZE-1;j++){
				map[i*width()+j]=Terrain.EMPTY;
			}
		}
		
		int center=(TEMP_MAX+TEMP_MIN)/2;
		entrance  =center*width()+center;
		//map[entrance]=Terrain.ENTRANCE;
		exit      =center*width()+center;
		
		placeTrigger(new WindowTriggerForSaveSlot().create(saveSlot1,1));
		placeTrigger(new WindowTriggerForSaveSlot().create(saveSlot2,2));
		placeTrigger(new WindowTriggerForSaveSlot().create(saveSlot3,3));
		placeTrigger(new WindowTriggerForSaveSlot().create(saveSlot4,4));
		placeTrigger(new WindowTriggerForSaveSlot().create(saveSlot5,5));
		placeTrigger(new WindowTriggerForSaveSlot().create(saveSlot6,6));

		int about   =(TEMP_MIN+2)*width()+TEMP_MIN   ;
		int rankings=(TEMP_MIN+2)*width()+TEMP_MIN+ 2;
		int badges  =(TEMP_MIN+2)*width()+TEMP_MIN+ 4;
		int changes =(TEMP_MIN+2)*width()+TEMP_MIN+ 6;
		map[about   ]=Terrain.WATER;
		map[rankings]=Terrain.EMBERS;
		map[badges  ]=Terrain.EMPTY_SP;
		map[changes ]=Terrain.GRASS;
		placeTrigger(new SceneSwitcher().create(about   ,AboutScene   .class));
		placeTrigger(new SceneSwitcher().create(rankings,RankingsScene.class));
		placeTrigger(new SceneSwitcher().create(badges  ,BadgesScene  .class));
		placeTrigger(new SceneSwitcher().create(changes ,ChangesScene .class));

		int teleporter=changes+2;
		map[teleporter]=Terrain.DOOR;
		placeTrigger(new Teleporter().create(teleporter,-1,1000));

		int teleporter2=teleporter+2;
		map[teleporter2]=Terrain.DOOR;
		placeTrigger(new Teleporter().create(teleporter2,-1,1025));

		CustomTilemap customBottomTile=new CustomBottomTile();
		customBottomTile.setRect(0,0,width(),height());
		customTiles.add(customBottomTile);

		return true;
	}

	public static class WindowTriggerForSaveSlot extends WindowTrigger{
		public int saveSlotId;
		private static final String SAVE_SLOT_ID="save_slot_id";
		@Override
		public void storeInBundle(Bundle bundle){
			super.storeInBundle(bundle);
			bundle.put(SAVE_SLOT_ID,saveSlotId);
		}
		@Override
		public void restoreFromBundle(Bundle bundle){
			super.restoreFromBundle(bundle);
			saveSlotId=bundle.getInt(SAVE_SLOT_ID);
		}

		public WindowTrigger create(int pos,int saveSlotId){
			this.pos=pos;
			this.saveSlotId=saveSlotId;
			return this;
		}

		@Override 
		protected Window getWindow(){
			return new WndSaveSlot(saveSlotId);
		}
	}

	public static class CustomBottomTile extends CustomTilemap {
		{
			texture = Assets.Environment.ZERO_LEVEL;
		}

		@Override
		public Tilemap create() {
			super.create();
			if (vis != null){
				int[] data = new int[tileW*tileH];
				for (int i = 0; i < data.length; i++){
					if(      saveSlot1==i){
						data[i]=1;
					}else if(saveSlot2==i){
						data[i]=2;
					}else if(saveSlot3==i){
						data[i]=3;
					}else if(saveSlot4==i){
						data[i]=4;
					}else if(saveSlot5==i){
						data[i]=5;
					}else if(saveSlot6==i){
						data[i]=6;
					}else{
						data[i] = -1;
					}
				}
				vis.map(data, tileW);
			}

			return vis;
		}
	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}

	@Override
	public Actor addRespawner() {
		return null;
	}

	@Override
	protected void createItems() {
	}
}