/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class Bestiary {

	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ) {

		ArrayList<Class<? extends Mob>> mobs = getRotation( depth );
		addRareMobs(depth, mobs);
		swapMobAlts(mobs);
		Random.shuffle(mobs);
		return mobs;
	}

	public static void addRareMobs( int depth, ArrayList<Class<?extends Mob>> rotation ){

		switch (depth){

			// Sewers
			default:
				return;
			case 4:
				if (Random.Float() < 0.025f) rotation.add(Thief.class);
				return;

			// Prison
			case 9:
				if (Random.Float() < 0.025f) rotation.add(Bat.class);
				return;

			// Caves
			case 14:
				if (Random.Float() < 0.025f) rotation.add(Ghoul.class);
				return;

			// City
			case 19:
				if (Random.Float() < 0.025f) rotation.add(Succubus.class);
				return;
		}
	}

	//switches out regular mobs for their alt versions when appropriate
	private static void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){
		for (int i = 0; i < rotation.size(); i++){
			if (Random.Int( 50 ) == 0) {
				Class<? extends Mob> cl = rotation.get(i);
				if (cl == Rat.class) {
					cl = Albino.class;
				} else if (cl == Slime.class) {
					cl = CausticSlime.class;
				} else if (cl == Thief.class) {
					cl = Bandit.class;
				} else if (cl == Necromancer.class){
					cl = SpectralNecromancer.class;
				} else if (cl == Brute.class) {
					cl = ArmoredBrute.class;
				} else if (cl == DM200.class) {
					cl = DM201.class;
				} else if (cl == Monk.class) {
					cl = Senior.class;
				} else if (cl == Scorpio.class) {
					cl = Acidic.class;
				}
				rotation.set(i, cl);
			}
		}
	}


	public static ArrayList<Class<? extends Mob>> getRotation(int floor) {
		switch ( floor ) {
			default:
				return new ArrayList<>(Arrays.asList(
                        Rat.class));
			case 1:
				//10x rat
				return new ArrayList<>(Arrays.asList(
						Rat.class, Rat.class, Rat.class, Rat.class, Rat.class,
						Rat.class, Rat.class, Rat.class, Rat.class, Rat.class));
            case 2:
				//3x rat, 3x gnoll
				return new ArrayList<>(Arrays.asList(
						Rat.class, Rat.class, Rat.class,
						Gnoll.class, Gnoll.class, Gnoll.class));
			case 3:
				//2x rat, 4x gnoll, 1x crab, 1x swarm
				return new ArrayList<>(Arrays.asList(
						Rat.class, Rat.class,
						Gnoll.class, Gnoll.class, Gnoll.class, Gnoll.class,
						Crab.class, Swarm.class));
			case 4:
				//1x rat, 2x gnoll, 3x crab, 1x swarm
				return new ArrayList<>(Arrays.asList(
						Rat.class,
						Gnoll.class, Gnoll.class,
						Crab.class, Crab.class, Crab.class,
						Swarm.class));
			case 6:
				//3x skeleton, 1x thief, 1x swarm
				return new ArrayList<>(Arrays.asList(
						Golyat.class, Golyat.class, Golyat.class,
						Thief.class,
						Swarm.class));
			case 7:
				//3x skeleton, 1x thief, 1x shaman, 1x guard
				return new ArrayList<>(Arrays.asList(
						Golyat.class, Golyat.class, Golyat.class,
						Thief.class,
						Shaman.class,
						Mg5.class));
			case 8:
				//3x skeleton, 1x thief, 2x shaman, 2x guard
				return new ArrayList<>(Arrays.asList(
						Golyat.class, Golyat.class, Golyat.class,
						Thief.class,
						Shaman.class, Shaman.class,
						Mg5.class, Mg5.class));
			case 9:
				//3x skeleton, 1x thief, 2x shaman, 3x guard
				return new ArrayList<>(Arrays.asList(
						Golyat.class, Golyat.class, Golyat.class,
						Thief.class,
						Shaman.class, Shaman.class,
						Mg5.class, Mg5.class, Mg5.class));
			case 11:
				return new ArrayList<>(Arrays.asList(
						GolyatPlus.class, GolyatPlus.class, GolyatPlus.class,
						Ripper.class, Ripper.class,
						Jager.class));
			case 12:
				return new ArrayList<>(Arrays.asList(
						GolyatPlus.class, GolyatPlus.class, GolyatPlus.class,
						Ripper.class, Ripper.class,
						Jager.class,
						Jaguar.class));
			case 13:
				return new ArrayList<>(Arrays.asList(
						GolyatPlus.class, GolyatPlus.class, GolyatPlus.class,
						Ripper.class, Ripper.class,
						Jager.class,
						Jaguar.class, Jaguar.class));
			case 14:
				return new ArrayList<>(Arrays.asList(
						GolyatPlus.class, GolyatPlus.class, GolyatPlus.class,
						Ripper.class, Ripper.class,Ripper.class,
						Jager.class,
						Jaguar.class, Jaguar.class));
			case 16:
				//5x bat, 1x brute
				return new ArrayList<>(Arrays.asList(
						Bat.class, Bat.class, Bat.class, Bat.class, Bat.class,
						Brute.class));
			case 17:
				//5x bat, 5x brute, 1x spinner
				return new ArrayList<>(Arrays.asList(
						Bat.class, Bat.class, Bat.class, Bat.class,
						Brute.class, Brute.class, Brute.class, Brute.class,
						Spinner.class,Spinner.class,
						Nemeum.class));
			case 18:
				//1x bat, 3x brute, 1x shaman, 1x spinner
				return new ArrayList<>(Arrays.asList(
						Bat.class, Bat.class,
						Brute.class, Brute.class, Brute.class,
						Spinner.class,Spinner.class,
						Nemeum.class, Nemeum.class));
			case 19:
				return new ArrayList<>(Arrays.asList(
						Bat.class,
						Brute.class, Brute.class, Brute.class,
						Spinner.class,Spinner.class,
						Nemeum.class,Nemeum.class,Nemeum.class));
			case 21:
				//5x elemental, 5x warlock, 1x monk
				return new ArrayList<>(Arrays.asList(
						Elemental.class, Elemental.class, Elemental.class, Elemental.class, Elemental.class,
						Warlock.class, Warlock.class, Warlock.class, Warlock.class, Warlock.class,
						Monk.class));
			case 22:
				//2x elemental, 2x warlock, 2x monk
				return new ArrayList<>(Arrays.asList(
						Elemental.class, Elemental.class,
						Warlock.class, Warlock.class,
						Monk.class, Monk.class));
			case 23:
				//1x elemental, 1x warlock, 2x monk, 1x golem
				return new ArrayList<>(Arrays.asList(
						Warlock.class,
						Monk.class, Monk.class, Monk.class,
						Golem.class));
			case 24:
				//1x elemental, 1x warlock, 2x monk, 3x golem
				return new ArrayList<>(Arrays.asList(
						Warlock.class, Warlock.class,
						Monk.class, Golem.class,
						Golem.class, Golem.class, Golem.class));
		}
	}
}