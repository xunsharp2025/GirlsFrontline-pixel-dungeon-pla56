/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elphelt;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class RabbitBossLevel extends Level {
	private static int WIDTH=32;
	private static int HEIGHT=32;
	private static int CENTER_POS=15+15*32;
	private static final Rect CENTER_CELLS = new Rect(14,14,17,17);

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}

	private enum State{
		READY,
		PHASE1,
		PHASE2,
		WON
	}
	
	private State state;
	private Elphelt elphelt=null;
	
	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.Environment.WATER_PRISON;
	}
	
	private static final String STATE	        = "state";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( STATE, state );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		state = bundle.getEnum( STATE, State.class );

		if (state!=State.READY&&state!=State.WON) {
			for (Mob mob : mobs){
				if (mob instanceof Elphelt) {
					elphelt = (Elphelt) mob;
					break;
				}
			}
		}
	}
	
	@Override
	protected boolean build() {
		
		setSize(WIDTH,HEIGHT);
		
		map = MAP_START.clone();

		buildFlagMaps();
		cleanWalls();

		state = State.READY;
		entrance = 15+12*WIDTH;
		exit = 15+17*WIDTH;

		resetTraps();

		return true;
	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}
	
	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			drop( item, randomRespawnCell(elphelt) ).type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell(Char ch) {
		return entrance + PathFinder.NEIGHBOURS8[Random.Int(8)]; //random cell adjacent to the entrance.
	}

	@Override
	public void occupyCell(Char ch) {
		super.occupyCell(ch);

		if (ch == Dungeon.hero) {
			//hero enters tengu's chamber
			if (state == State.READY) {
				progress();
			}
		}
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(PrisonLevel.class, "water_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(PrisonLevel.class, "empty_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(PrisonLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	private void resetTraps(){
		traps.clear();

		for (int i = 0; i < length(); i++){
			if (map[i] == Terrain.INACTIVE_TRAP) {
				Trap t = new GrippingTrap().reveal();
				t.active = false;
				setTrap(t, i);
				map[i] = Terrain.INACTIVE_TRAP;
			}
		}
	}

	private void changeMap(int[] map){
		this.map = map.clone();
		buildFlagMaps();
		cleanWalls();

		exit = entrance = 0;
		for (int i = 0; i < length(); i ++)
			if (map[i] == Terrain.ENTRANCE)
				entrance = i;
			else if (map[i] == Terrain.EXIT)
				exit = i;

		BArray.setFalse(visited);
		BArray.setFalse(mapped);
		
		for (Blob blob: blobs.values()){
			blob.fullyClear();
		}
		addVisuals(); //this also resets existing visuals
		resetTraps();


		GameScene.resetMap();
		Dungeon.observe();
	}

	private ArrayList<Item> clearEntities(Rect safeArea){
		ArrayList<Item> storedItems = new ArrayList<>();

		for (Heap heap : heaps.values()){
			if (safeArea == null || !safeArea.inside(cellToPoint(heap.pos))){
				for (Item item : heap.items)
					storedItems.add(item);
				heap.destroy();
			}
		}
		for (HeavyBoomerang.CircleBack b : Dungeon.hero.buffs(HeavyBoomerang.CircleBack.class)){
			if (b.activeDepth() == Dungeon.depth
					&& (safeArea == null || !safeArea.inside(cellToPoint(b.returnPos())))){
				storedItems.add(b.cancel());
			}
		}
		for (Plant plant : plants.values()){
			if (safeArea == null || !safeArea.inside(cellToPoint(plant.pos))){
				plants.remove(plant.pos);
			}
		}

		return storedItems;
	}

	public int randomCenterCellsPos(){
		return CENTER_POS+Random.IntRange(-1,1)+WIDTH*Random.IntRange(-1,1);
	}

	public void progress(){
		switch (state){
			//moving to the beginning of the fight
			case READY:
				seal();
				elphelt = new Elphelt();
				elphelt.state = elphelt.SLEEPING;
				elphelt.pos = exit; //in the middle of the fight room

				GameScene.add( elphelt );

				changeMap(MAP_ARENA);
				GameScene.updateMap(entrance);

				state = State.PHASE1;
				break;

			//halfway through, move to the maze
			case PHASE1:
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.Sounds.BLAST);

				state = State.PHASE2;
				elphelt.phase = 2;
				break;

			case PHASE2:
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.Sounds.BLAST);

				changeMap(MAP_END);

				ArrayList<Item> storedItems = clearEntities(CENTER_CELLS);

				for (Item item : storedItems) {
					drop(item, randomCenterCellsPos());
				}

				GameScene.updateMap(entrance);
				GameScene.updateMap(exit);

				Dungeon.hero.interrupt();
				Dungeon.hero.pos = entrance;
				Dungeon.hero.sprite.interruptMotion();
				Dungeon.hero.sprite.place(Dungeon.hero.pos);

				elphelt.pos = CENTER_POS;
				elphelt.die(Dungeon.hero);
				
				state = State.WON;
				unseal();
				break;
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		PrisonLevel.addPrisonVisuals(this,visuals);
		return visuals;
	}

	private static final int W = Terrain.WALL;
	private static final int D = Terrain.DOOR;
	private static final int L = Terrain.LOCKED_DOOR;
	private static final int e = Terrain.EMPTY;

	private static final int T = Terrain.INACTIVE_TRAP;

	private static final int E = Terrain.ENTRANCE;
	private static final int X = Terrain.EXIT;

	private static final int M = Terrain.WALL_DECO;
	private static final int P = Terrain.PEDESTAL;

	//TODO if I ever need to store more static maps I should externalize them instead of hard-coding
	//Especially as I means I won't be limited to legal identifiers

	private static final int[] MAP_START =
			{       W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, W, W,
					W, e, W, e, e, W, e, e, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, e, e, W, W, e, e, e, W, W,
					W, e, e, W, e, e, e, e, e, e, W, e, e, e, W, W, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, W, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, W, e, e, W, e, e, W, e, e, W, W,
					W, W, W, e, e, W, e, e, W, e, e, e, W, W, W, W, W, W, W, e, e, e, W, W, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, W, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, W, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, W, W,
					W, W, W, W, W, e, e, e, W, e, e, W, e, e, W, W, W, e, e, e, e, W, W, e, e, e, W, W, W, W, W, W,
					W, e, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, W, e, e, W, e, e, W, e, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, e, W, W, e, e, W, e, e, e, e, W, e, e, W, e, e, e, W, W, e, e, e, e, W, e, e, W, e, e, W, W,
					W, W, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, W, e, e, W, e, e, e, W, e, W, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, M, e, e, e, e, W, e, e, e, W, e, W, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, W, e, e, e, e, e, e, W, e, e, e, W, e, W, e, e, W, W, W,
					W, e, e, W, e, e, W, e, e, e, e, W, W, e, e, e, W, e, e, W, e, e, e, e, W, e, e, W, W, e, W, W,
					W, e, e, e, e, e, W, e, e, e, e, e, e, e, e, T, e, W, e, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, e, W, e, e, W, e, e, W, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, e, W, W,
					W, W, W, W, W, e, e, e, W, W, e, e, e, e, W, W, W, e, e, W, e, e, W, e, e, e, W, W, W, W, W, W,
					W, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, W, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, e, e, e, e, W, W, e, e, e, W, W, W, W, W, W, W, e, e, e, W, e, e, W, e, e, W, W, W, W,
					W, e, e, W, e, e, W, e, e, W, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, e, e, W, W,
					W, e, e, e, e, e, e, e, e, e, W, e, e, e, W, W, W, e, e, e, W, e, e, e, e, e, e, W, e, e, W, W,
					W, e, e, e, W, W, e, e, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, e, e, W, e, e, W, e, W, W,
					W, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

	private static final int[] MAP_ARENA =
			{       W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, W, W,
					W, e, W, e, e, W, e, e, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, e, e, W, W, e, e, e, W, W,
					W, e, e, W, e, e, e, e, e, e, W, e, e, e, W, W, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, W, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, W, e, e, W, e, e, W, e, e, W, W,
					W, W, W, e, e, W, e, e, W, e, e, e, W, W, W, W, W, W, W, e, e, e, W, W, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, W, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, W, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, W, W,
					W, W, W, W, W, e, e, e, W, e, e, W, e, e, W, W, W, e, e, e, e, W, W, e, e, e, W, W, W, W, W, W,
					W, e, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, W, e, e, W, e, e, W, e, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, e, W, W, e, e, W, e, e, e, e, W, e, e, W, e, e, e, W, W, e, e, e, e, W, e, e, W, e, e, W, W,
					W, W, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, W, e, e, W, e, e, e, W, e, W, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, M, e, e, e, e, W, e, e, e, W, e, W, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, W, e, e, e, e, e, e, W, e, e, e, W, e, W, e, e, W, W, W,
					W, e, e, W, e, e, W, e, e, e, e, W, W, e, e, e, W, e, e, W, e, e, e, e, W, e, e, W, W, e, W, W,
					W, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, e, W, e, e, W, e, e, W, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, e, W, W,
					W, W, W, W, W, e, e, e, W, W, e, e, e, e, W, W, W, e, e, W, e, e, W, e, e, e, W, W, W, W, W, W,
					W, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, W, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, e, e, e, e, W, W, e, e, e, W, W, W, W, W, W, W, e, e, e, W, e, e, W, e, e, W, W, W, W,
					W, e, e, W, e, e, W, e, e, W, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, e, e, W, W,
					W, e, e, e, e, e, e, e, e, e, W, e, e, e, W, W, W, e, e, e, W, e, e, e, e, e, e, W, e, e, W, W,
					W, e, e, e, W, W, e, e, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, e, e, W, e, e, W, e, W, W,
					W, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

	private static final int[] MAP_END =
			{       W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, W, W,
					W, e, W, e, e, W, e, e, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, e, e, W, W, e, e, e, W, W,
					W, e, e, W, e, e, e, e, e, e, W, e, e, e, W, W, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, W, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, W, e, e, W, e, e, W, e, e, W, W,
					W, W, W, e, e, W, e, e, W, e, e, e, W, W, W, W, W, W, W, e, e, e, W, W, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, W, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, W, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, W, W,
					W, W, W, W, W, e, e, e, W, e, e, W, e, e, W, W, W, e, e, e, e, W, W, e, e, e, W, W, W, W, W, W,
					W, e, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, W, e, e, W, e, e, W, e, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, e, e, e, W, e, E, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, e, W, W, e, e, W, e, e, e, e, W, e, e, W, e, e, e, W, W, e, e, e, e, W, e, e, W, e, e, W, W,
					W, W, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, W, e, e, W, e, e, e, W, e, W, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, P, e, e, e, e, W, e, e, e, W, e, W, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, W, e, e, e, e, e, e, W, e, e, e, W, e, W, e, e, W, W, W,
					W, e, e, W, e, e, W, e, e, e, e, W, W, e, e, e, W, e, e, W, e, e, e, e, W, e, e, W, W, e, W, W,
					W, e, e, e, e, e, W, e, e, e, e, e, e, e, e, X, e, W, e, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, e, W, e, e, W, e, e, W, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, e, W, W,
					W, W, W, W, W, e, e, e, W, W, e, e, e, e, W, W, W, e, e, W, e, e, W, e, e, e, W, W, W, W, W, W,
					W, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, W, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, e, e, e, e, W, W, e, e, e, W, W, W, W, W, W, W, e, e, e, W, e, e, W, e, e, W, W, W, W,
					W, e, e, W, e, e, W, e, e, W, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, W, e, e, W, W,
					W, e, e, e, e, e, e, e, e, e, W, e, e, e, W, W, W, e, e, e, W, e, e, e, e, e, e, W, e, e, W, W,
					W, e, e, e, W, W, e, e, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, e, e, W, e, e, W, e, W, W,
					W, e, e, W, e, e, W, e, e, e, W, e, e, W, e, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, e, e, e, e, e, W, e, e, e, W, e, e, e, W, e, e, e, e, e, W, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};
}
