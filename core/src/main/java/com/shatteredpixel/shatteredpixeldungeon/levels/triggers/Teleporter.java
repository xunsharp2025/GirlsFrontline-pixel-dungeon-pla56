package com.shatteredpixel.shatteredpixeldungeon.levels.triggers;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;


public class Teleporter extends Trigger{
	public int targetPos;
	public int targetLevelId;

	private static final String TARGET_POS     ="target_pos";
	private static final String TARGET_LEVEL_ID="target_level_id";

	@Override
	public void storeInBundle(Bundle bundle){
		super.storeInBundle(bundle);
		bundle.put(TARGET_POS     ,targetPos    );
		bundle.put(TARGET_LEVEL_ID,targetLevelId);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle){
		super.restoreFromBundle(bundle);
		targetPos    =bundle.getInt(TARGET_POS     );
		targetLevelId=bundle.getInt(TARGET_LEVEL_ID);
	}
	
	@Override
	public void activate(Char ch){

	}
}