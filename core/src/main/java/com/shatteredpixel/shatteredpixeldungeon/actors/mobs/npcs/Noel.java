package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Teleporter;
import com.shatteredpixel.shatteredpixeldungeon.ui.dialog.quest.Noel_Plot_L1;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NoelSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

public class Noel extends NPC {

	{
		spriteClass = NoelSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	private int diaLogState = 0;

	private static final String DIALOG_STATE = "dialog_state"; 

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(DIALOG_STATE,diaLogState);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		diaLogState=bundle.getInt(DIALOG_STATE);
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}

	@Override
	public void damage( int dmg, Object src ) {
	}

	@Override
	public void add( Buff buff ) {
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public boolean interact(Char c) {
		sprite.turnTo(pos,c.pos);

		if (c!=Dungeon.hero){
			return super.interact(c);
		}
		
		if(0==diaLogState){
			diaLogState=1;
			Game.runOnRenderThread(()->GameScene.show(new WndDialog(new Noel_Plot_L1())));
		}else{
			Noel noel=this;

			Game.runOnRenderThread(()->GameScene.show(new WndOptions(
				sprite(),
				Messages.titleCase(name()),
				Messages.get(Noel.class,"teleport_desc"),
				Messages.get(Noel.class,"teleport_yes"),
				Messages.get(Noel.class,"teleport_no")
			){
				@Override
				protected void onSelect(int index) {
					if(index==0){
						new Teleporter().create(0,-1,1010).activate(c);
						noel.destroy();
						noel.sprite.killAndErase();
					}else{
						yell(Messages.get(Noel.class,"teleport_fine"));
					}
				}
			}));
		}

		return true;
	}
}