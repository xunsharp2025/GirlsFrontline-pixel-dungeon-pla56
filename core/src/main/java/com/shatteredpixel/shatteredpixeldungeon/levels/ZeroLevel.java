package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ZeroLevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AboutScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.BadgesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSaveSlot;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Callback;
import com.watabou.utils.Bundle;
import java.io.IOException;

public class ZeroLevel extends Level {
	private static int saveSlot1;
	private static int saveSlot2;
	private static int saveSlot3;
	private static int saveSlot4;
	private static int saveSlot5;
	private static int saveSlot6;
	private static int about    ;
	private static int rankings ;
	private static int badges   ;
	private static int changes  ;
	private static final String SAVE_SLOT1="save_slot1";
	private static final String SAVE_SLOT2="save_slot2";
	private static final String SAVE_SLOT3="save_slot3";
	private static final String SAVE_SLOT4="save_slot4";
	private static final String SAVE_SLOT5="save_slot5";
	private static final String SAVE_SLOT6="save_slot6";
	private static final String ABOUT     ="about     ";
	private static final String RANKINGS  ="rankings  ";
	private static final String BADGES    ="badges    ";
	private static final String CHANGES   ="changes   ";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(SAVE_SLOT1,saveSlot1);
		bundle.put(SAVE_SLOT2,saveSlot2);
		bundle.put(SAVE_SLOT3,saveSlot3);
		bundle.put(SAVE_SLOT4,saveSlot4);
		bundle.put(SAVE_SLOT5,saveSlot5);
		bundle.put(SAVE_SLOT6,saveSlot6);
		bundle.put(ABOUT     ,about    );
		bundle.put(RANKINGS  ,rankings );
		bundle.put(BADGES    ,badges   );
		bundle.put(CHANGES   ,changes  );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		saveSlot1=bundle.getInt(SAVE_SLOT1);
		saveSlot2=bundle.getInt(SAVE_SLOT2);
		saveSlot3=bundle.getInt(SAVE_SLOT3);
		saveSlot4=bundle.getInt(SAVE_SLOT4);
		saveSlot5=bundle.getInt(SAVE_SLOT5);
		saveSlot6=bundle.getInt(SAVE_SLOT6);
		about    =bundle.getInt(ABOUT     );
		rankings =bundle.getInt(RANKINGS  );
		badges   =bundle.getInt(BADGES    );
		changes  =bundle.getInt(CHANGES   );
	}

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
		final int SIZE = 17;

		setSize(SIZE, SIZE);

		for(int i=1;i<SIZE-1;i++){
			for(int j=1;j<SIZE-1;j++){
				map[i*width()+j]=Terrain.EMPTY;
			}
		}
		
		int max=(SIZE-2);
		int min=1;
		int center=(max+min)/2;
		
		entrance  =center*width()+center;
		//map[entrance]=Terrain.ENTRANCE;
		exit      =center*width()+center;
		
		max-=1;
		min+=1;

		saveSlot1=min*width()+min   ;
		saveSlot2=min*width()+min+ 2;
		saveSlot3=min*width()+min+ 4;
		saveSlot4=min*width()+min+ 6;
		saveSlot5=min*width()+min+ 8;
		saveSlot6=min*width()+min+10;
		map[saveSlot1]=Terrain.EMPTY_DECO;
		map[saveSlot2]=Terrain.EMPTY_DECO;
		map[saveSlot3]=Terrain.EMPTY_DECO;
		map[saveSlot4]=Terrain.EMPTY_DECO;
		map[saveSlot5]=Terrain.EMPTY_DECO;
		map[saveSlot6]=Terrain.EMPTY_DECO;
		about    =(min+2)*width()+min   ;
		rankings =(min+2)*width()+min+ 2;
		badges   =(min+2)*width()+min+ 4;
		changes  =(min+2)*width()+min+ 6;
		map[about    ]=Terrain.WATER;
		map[rankings ]=Terrain.EMBERS;
		map[badges   ]=Terrain.EMPTY_SP;
		map[changes  ]=Terrain.GRASS;

		CustomTilemap customBottomTile=new CustomBottomTile();
		customBottomTile.setRect(0,0,width(),height());
		customTiles.add(customBottomTile);

		return true;
	}

	public static class CustomBottomTile extends CustomTilemap {
		{
			texture = Assets.Environment.ZERO_LEVEL;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			updateState( );

			return v;
		}

		public void updateState( ){
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
		}
	}

	@Override
	public void occupyCell(Char ch){
		super.occupyCell(ch);
		if(ch!=Dungeon.hero){
			return;
		}

		int pos=Dungeon.hero.pos;
		int curSaveSlot=0;
		boolean onSaveSlot =false;
		if      (saveSlot1==pos){
			curSaveSlot=1;
			onSaveSlot=true;
		}else if(saveSlot2==pos){
			curSaveSlot=2;
			onSaveSlot=true;
		}else if(saveSlot3==pos){
			curSaveSlot=3;
			onSaveSlot=true;
		}else if(saveSlot4==pos){
			curSaveSlot=4;
			onSaveSlot=true;
		}else if(saveSlot5==pos){
			curSaveSlot=5;
			onSaveSlot=true;
		}else if(saveSlot6==pos){
			curSaveSlot=6;
			onSaveSlot=true;
		}
		if(onSaveSlot){
			int finalSaveSlot=curSaveSlot;
			Game.runOnRenderThread(()->GameScene.show(new WndSaveSlot(finalSaveSlot)));
		}

		if(about    ==pos){
			Game.switchScene(AboutScene.class);
		}

		if(rankings ==pos){
			Game.switchScene(BadgesScene.class);
		}

		if(badges   ==pos){
			Game.switchScene(ChangesScene.class);
		}

		if(changes  ==pos){
			Game.switchScene(RankingsScene.class);
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