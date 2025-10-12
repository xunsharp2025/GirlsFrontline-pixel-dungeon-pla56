package com.shatteredpixel.shatteredpixeldungeon.levels.triggers;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;


public abstract class Trigger implements Bundlable {
	public int pos;

	private static final String POS	= "pos";

	public Trigger create(int pos){
		this.pos=pos;
		return this;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put(POS,pos);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		pos=bundle.getInt(POS);
	}

	public boolean canBeTouched(){
		return true;
	}

	public boolean canBePressed(){
		return false;
	}

	public boolean canInteract(Char ch){
		if(pos==ch.pos){
			return true;
		}

		return false;
	}

	public boolean interact(Char ch){
		activate(ch);
		return true;
	}

	public abstract void activate(Char ch);
}