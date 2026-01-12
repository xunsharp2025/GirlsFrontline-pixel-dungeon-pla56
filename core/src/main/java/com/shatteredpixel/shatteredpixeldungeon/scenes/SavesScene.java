package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStartGame;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSelectGameInProgress;
import com.watabou.noosa.Game;

public class SavesScene extends PixelScene {
	{
		inGameScene = true;
	}

	@Override
	public void create() {
		super.create();

		if (GamesInProgress.checkAll().isEmpty()){
			add( new WndStartGame(1){
				@Override
				public void onBackPressed() {
					super.onBackPressed();
					SavesScene.this.onBackPressed();
				}
			});
		} else {
			add( new WndSelectGameInProgress(){
				@Override
				public void onBackPressed() {
					super.onBackPressed();
					SavesScene.this.onBackPressed();
				}
			});
		}

		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
		Game.switchScene( InterlevelScene.class );
	}
}
