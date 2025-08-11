package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gun561;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gun562;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Gun562Accessories extends Item{
	public static final String AC_MODIFY="MODIFY";

	{
		image = ItemSpriteSheet.GUN562ACCESSORIES;

		cursedKnown = levelKnown = true;
		unique = true;
		bones = false;

		defaultAction = AC_MODIFY;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions =  super.actions(hero);
		actions.add(AC_MODIFY);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (action.equals(AC_MODIFY)){
			UpgradeGun561(hero);
		}
	}

	private void UpgradeGun561(Hero hero){
		if(hero.belongings.weapon instanceof Gun561){
			Gun561 gun561=(Gun561)hero.belongings.weapon;
			Gun562 gun562=new Gun562();

			(hero.belongings.weapon=gun562).identify();
			int id=Dungeon.quickslot.getSlot(gun561);
			if(id>=0){
				Dungeon.quickslot.setSlot(id,gun562);
			}
			
			gun562.level(gun561.trueLevel());

			hero.spendAndNext(3f);
			detach(hero.belongings.backpack);

			GLog.i(Messages.get(this,"succeed!"));
		}else{
			GLog.i(Messages.get(this,"failed"));
		}
	}
}