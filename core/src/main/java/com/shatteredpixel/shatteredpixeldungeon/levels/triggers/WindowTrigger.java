package com.shatteredpixel.shatteredpixeldungeon.levels.triggers;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;

public abstract class WindowTrigger extends Trigger{
	public WindowTrigger create(int pos, int i){
		this.pos=pos;
		return this;
	}
	
	@Override
	public void activate(Char ch){
		if(ch==Dungeon.hero){
			Game.runOnRenderThread(()->GameScene.scene.add(getWindow()));
		}
	}

	protected abstract Window getWindow();
}