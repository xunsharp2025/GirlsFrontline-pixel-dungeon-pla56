package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.SceneSwitcher;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.WindowTrigger;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AboutScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.BadgesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStartGame;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.Rect;

import java.util.Arrays;

public class ZeroLevel extends Level {
	//private static final int SIZE = 17;
	private static final int WIDTH = 16;
	private static final int HEIGHT = 8;
	private static final int TEMP_MIN = 2;
	private static final int TEMP_MAX = HEIGHT-3;
	private static final int saveSlot = TEMP_MIN * WIDTH + TEMP_MIN + 26;
	public static int badges = (TEMP_MIN + 2) * WIDTH + TEMP_MIN - 27;
	public static int rankings = (TEMP_MIN + 2) * WIDTH + TEMP_MIN - 22;
	public static int about = (TEMP_MIN + 2) * WIDTH + TEMP_MIN + 16;
	public static int changes = (TEMP_MIN + 2) * WIDTH + TEMP_MIN + 38;
	public static final int[] fill_arg = {
			1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
			45, 46, 47, 61, 62, 63, 68, 69, 70, 71, 72, 73, 74, 75, 86,
			87, 88, 89, 97, 98, 99
	};
	public static final int[] fill_wall = {
	};
	@Override
	public String tilesTex() {
		return Assets.Environment.ZERO_LEVEL2;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
	}

	@Override
	protected boolean build() {
		final int WIDTH = 16;
		final int HEIGHT = 8;
		setSize(WIDTH,HEIGHT);
		/*for(int i = 1; i < HEIGHT - 1; i++) {
			for(int j = 1; j < WIDTH - 1; j++) {
				map[i*width() + j] = Terrain.EMPTY;
			}
		}*/

		int centerY = (TEMP_MAX+TEMP_MIN)/2;
		int centerX = WIDTH/2;
		entrance = centerY*width()+centerX;
		exit = centerY*width()+centerX;

		placeTrigger(new StartGameTrigger().create(saveSlot,1));

		Arrays.fill(map, Terrain.EMPTY);
		for(int i: fill_arg) map[i] = Terrain.WALL;
		placeTrigger(new SceneSwitcher().create(about , AboutScene.class));
		placeTrigger(new SceneSwitcher().create(rankings, RankingsScene.class));
		placeTrigger(new SceneSwitcher().create(badges , BadgesScene.class));
		placeTrigger(new SceneSwitcher().create(changes , ChangesScene.class));
		/*int teleporter = changes + 2;
		map[teleporter] = Terrain.DOOR;
		placeTrigger(new Teleporter().create(teleporter, -1, 1000));

		int teleporter2 = teleporter + 2;
		map[teleporter2] = Terrain.DOOR;
		placeTrigger(new Teleporter().create(teleporter2, -1, 1025));*/

		CustomTilemap customBottomTile = new CustomBottomTile();
		customBottomTile.setRect(0, 0, width(), height());
		customTiles.add(customBottomTile);

		return true;
	}



	public static class StartGameTrigger extends WindowTrigger {
		@Override
		protected Window getWindow() {
			return new com.shatteredpixel.shatteredpixeldungeon.windows.WndSaveSlot(1);
		}
	}
	/*public static class WindowTriggerForSaveSlot extends WindowTrigger{
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
			saveSlotId = bundle.getInt(SAVE_SLOT_ID);
		}

		public WindowTrigger create(int pos,int saveSlotId){
			this.pos = pos;
			this.saveSlotId = saveSlotId;
			return this;
		}

		@Override
		protected Window getWindow(){
			return new WndSaveSlot(saveSlotId);
		}
	}*/

	public static class CustomBottomTile extends CustomTilemap {
		{
			texture = Assets.Environment.ZERO_LEVEL2;
			TextureFilm film = new TextureFilm(texture, 24, 24);
		}
		@Override
		public Tilemap create() {
			super.create();
			if(vis != null) {
				int[] data = new int[WIDTH * HEIGHT];
				for(int i = 0; i < data.length; i++) {
					data[i] = i;
					if(i == saveSlot) data[i] = 0;
					else if(i == changes) data[i] = 0;
					else if(i == about) data[i] = 0;
					else if(i == badges) data[i] = 0;
					else if(i == rankings) data[i] = 0;
				}
				vis.width = WIDTH;
				vis.height = HEIGHT;
				vis.map(data, 16);
				//oh shit
				/*vis.map(data, HEIGHT);*/
				/*vis.width = 14;
				vis.height = 7;
				vis.map(data, 14);*/
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