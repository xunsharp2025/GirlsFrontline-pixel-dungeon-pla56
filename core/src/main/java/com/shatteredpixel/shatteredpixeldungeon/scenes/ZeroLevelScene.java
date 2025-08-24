package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.ZeroLevel;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.GameLog;
import com.watabou.noosa.Scene;
import com.watabou.noosa.Game;
import java.io.IOException;

public class ZeroLevelScene extends Scene {
	@Override
	public void create() {
		Badges.loadGlobal();
		Journal.loadGlobal();

		Dungeon.hero=null;
		enterMainGame();
	}

	private void enterMainGame(){
		ActionIndicator.action  = null;
		GamesInProgress.curSlot = 0;
		GamesInProgress.selectedClass=HeroClass.TYPE561;
		boolean	newGame = null == GamesInProgress.check(GamesInProgress.curSlot);
		if(newGame){
			Mob.clearHeldAllies();
			Dungeon.init();
			GameLog.wipe();
			Actor.clear();
			Dungeon.depth=Statistics.deepestFloor=0;
			Level level=new ZeroLevel();
			level.create();
			Dungeon.switchLevel(level,level.entrance);
		}else{
			InterlevelScene.mode=InterlevelScene.Mode.CONTINUE;
			try{InterlevelScene.restore();}
			catch(IOException e){Game.reportException(e);}
		}
		Game.switchScene(GameScene.class);
	}

}
