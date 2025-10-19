package com.shatteredpixel.shatteredpixeldungeon.levels.triggers;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.Scene;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;
import java.io.IOException;

public class SceneSwitcher extends Trigger{
	private Class<?extends Scene> scene;

	public SceneSwitcher create(int pos,Class<?extends Scene> scene){
		this.scene=scene;
		this.pos=pos;
		return this;
	}

	private static final String SCENE = "scene";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(SCENE,scene);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		scene=bundle.getClass(SCENE);
	}
	
	@Override
	public void activate(Char ch){
		if(ch==Dungeon.hero){
			try{Dungeon.saveAll();
			}catch(IOException e){Game.reportException(e);}
			Game.switchScene(scene);
		}
	}
}