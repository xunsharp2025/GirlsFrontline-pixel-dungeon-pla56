package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpinnerCatSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SpinnerCat extends Spinner {

	{
		spriteClass = SpinnerCatSprite.class;
		HP = HT = 35;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(5, 15);
	}
	@Override
	protected void applyWebToCell(int cell) {
		GameScene.add(Blob.seed(cell, 40, ConfusionGas.class));
	}

	@Override
	public void move(int step, boolean travelling) {
		if(state == FLEEING){
			baseSpeed = 2.5f;
		} else {
			baseSpeed = 1.0f;
		}
		super.move(step, travelling);
	}

	@Override
	public void die( Object cause ) {

		super.die(cause);
		MysteryMeat mysteryMeat = new MysteryMeat();
		mysteryMeat.quantity(Random.Int(1,3));
		Dungeon.level.drop(mysteryMeat, pos).sprite.drop();

		if(Random.Int(10)==1){
			ScrollOfTransmutation scrollOfTransmutation = new ScrollOfTransmutation();
			Dungeon.level.drop(scrollOfTransmutation, pos).sprite.drop();
		}
		
		if(Random.Int(20)==1){
			ScrollOfMetamorphosis scrollOfMetamorphosis = new ScrollOfMetamorphosis();
			Dungeon.level.drop(scrollOfMetamorphosis, pos).sprite.drop();
		}
	}

	@Override
	public void damage(int dmg, Object src) {
		int grassCells = 0;
		for (int i : PathFinder.NEIGHBOURS9) {
			if (Dungeon.level.map[pos+i] == Terrain.FURROWED_GRASS
					|| Dungeon.level.map[pos+i] == Terrain.HIGH_GRASS){
				grassCells++;
			}
		}
		if (grassCells > 0) dmg = Math.round(dmg * (8-grassCells)/10f);

		super.damage(dmg, src);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		webCoolDown = 0;
		state = FLEEING;
		return damage;
	}

	{
		immunities.add(ConfusionGas.class);
	}

}
