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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class Bestiary {

	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ) {

		ArrayList<Class<? extends Mob>> mobs = getRotation( depth );
		addRareMobs(depth, mobs);
		swapMobAlts(mobs);
		swapRareMobAlts(mobs);
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
				if (Random.Float() < 0.025f) rotation.add(Guard.class);
				return;
            case 24:
                if (Random.Float() < 0.025f) rotation.add(Succubus.class);
                return;
		}
	}

	//switches out regular mobs for their alt versions when appropriate
	private static void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){
		for (int i = 0; i < rotation.size(); i++){

            Class<? extends Mob> cl = rotation.get(i);
            int j =Random.Int(100);
			if (j <Dungeon.mobRan) {
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
				} else if (cl == Golyat.class) {
                    cl = GolyatPlus.class;
                }else if (cl == Gnoll.class) {
                    cl = GnollSWAP.class;
                }
			}
//            if (j==1){
//                //抢占本体概率的精英怪变种
//                if (cl == Rat.class&&
//                        (Dungeon.lockXMAS||Dungeon.isXMAS())) {
//                    cl = RatXMAS.class;
//                }
//            }
            rotation.set(i, cl);
		}
	}

	private static void swapRareMobAlts(ArrayList<Class<?extends Mob>> rotation) {
		for (int i = 0; i < rotation.size(); i++){
			Class<? extends Mob> cl = rotation.get(i);
			if (Random.Int( 500 ) == 0) {
				if (cl == Spinner.class) {
					cl = SpinnerCat.class;
				}
				rotation.set(i, cl);
			}
			if (Random.Int( 100 ) == 0) {
				if (cl == Hydra.class) {
					cl = Typhoon.class;
				}
				rotation.set(i, cl);
			}
		}
	}
    public static ArrayList<Class<? extends Mob>> NumMob(Class mob, int num){
        //输入想要加入的怪的class，以及权重数量，即可返回对应数量的怪的列表，将其addAll到getRotation的列表就等效于原本逐个填写了
        ArrayList<Class<? extends Mob>> List = new ArrayList<>();
        for(int i=0 ; i< num ; i++){
            List.add(mob);
        }
        return List;
    }


	public static ArrayList<Class<? extends Mob>> getRotation(int floor) {
        ArrayList<Class<? extends Mob>> List = new ArrayList<>();
		switch ( floor ) {
			default:
                List.addAll(NumMob(Rat.class,1));
				return List;
			case 1:
                List.addAll(NumMob(Rat.class,6));
                List.addAll(NumMob(Snake.class,2));
				return List;
            case 2:
                List.addAll(NumMob(Rat.class,3));
                List.addAll(NumMob(Gnoll.class,3));
                return List;
			case 3:
                List.addAll(NumMob(Rat.class,2));
                List.addAll(NumMob(Gnoll.class,4));
                List.addAll(NumMob(Crab.class,1));
                List.addAll(NumMob(Swarm.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Rat.class, Rat.class,
//						Gnoll.class, Gnoll.class, Gnoll.class, Gnoll.class,
//						Crab.class, Swarm.class));
			case 4: case 5:
                List.addAll(NumMob(Rat.class,1));
                List.addAll(NumMob(Gnoll.class,2));
                List.addAll(NumMob(Crab.class,3));
                List.addAll(NumMob(Swarm.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Rat.class,
//						Gnoll.class, Gnoll.class,
//						Crab.class, Crab.class, Crab.class,
//						Swarm.class));
			case 6:
                List.addAll(NumMob(Golyat.class,3));
                List.addAll(NumMob(Thief.class,1));
                List.addAll(NumMob(Swarm.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Golyat.class, Golyat.class, Golyat.class,
//						Thief.class,
//						Swarm.class));
			case 7:
                List.addAll(NumMob(Golyat.class,3));
                List.addAll(NumMob(Thief.class,1));
                List.addAll(NumMob(Shaman.random(),1));
                List.addAll(NumMob(Mg5.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Golyat.class, Golyat.class, Golyat.class,
//						Thief.class,
//						Shaman.random(),
//						Mg5.class));
			case 8:
                List.addAll(NumMob(Golyat.class,2));
                List.addAll(NumMob(Thief.class,1));
                List.addAll(NumMob(Shaman.random(),2));
                List.addAll(NumMob(Mg5.class,2));
                List.addAll(NumMob(GolyatFactory.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Golyat.class, Golyat.class, Golyat.class,
//						Thief.class,
//						Shaman.random(), Shaman.random(),
//						Mg5.class, Mg5.class));
			case 9: case 10:
                List.addAll(NumMob(Golyat.class,1));
                List.addAll(NumMob(Thief.class,1));
                List.addAll(NumMob(Shaman.random(),2));
                List.addAll(NumMob(Mg5.class,2));
                List.addAll(NumMob(GolyatFactory.class,2));
                return List;
//                return List;
//				return new ArrayList<>(Arrays.asList(
//						Golyat.class, Golyat.class, Golyat.class,
//						Thief.class,
//						Shaman.random(), Shaman.random(),
//						Mg5.class, Mg5.class, Mg5.class));
			case 11:
                List.addAll(NumMob(Bat.class,5));
                List.addAll(NumMob(Brute.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Bat.class, Bat.class, Bat.class, Bat.class, Bat.class,
//						Brute.class));
			case 12:
                List.addAll(NumMob(Bat.class,4));
                List.addAll(NumMob(Brute.class,4));
                List.addAll(NumMob(Spinner.class,2));
                List.addAll(NumMob(Nemeum.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Bat.class, Bat.class, Bat.class, Bat.class,
//						Brute.class, Brute.class, Brute.class, Brute.class,
//						Spinner.class,Spinner.class,
//						Nemeum.class));
			case 13:
                List.addAll(NumMob(Bat.class,2));
                List.addAll(NumMob(Brute.class,3));
                List.addAll(NumMob(Spinner.class,2));
                List.addAll(NumMob(Nemeum.class,2));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Bat.class, Bat.class,
//						Brute.class, Brute.class, Brute.class,
//						Spinner.class,Spinner.class,
//						Nemeum.class, Nemeum.class));
			case 14: case 15:
                List.addAll(NumMob(Bat.class,1));
                List.addAll(NumMob(Brute.class,3));
                List.addAll(NumMob(Spinner.class,2));
                List.addAll(NumMob(Nemeum.class,3));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Bat.class,
//						Brute.class, Brute.class, Brute.class,
//						Spinner.class,Spinner.class,
//						Nemeum.class,Nemeum.class,Nemeum.class));
			case 16:
                List.addAll(NumMob(Elemental.random(),3));
                List.addAll(NumMob(Ghoul.class,2));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Elemental.random(),Elemental.random(),Elemental.random(),
//						Ghoul.class,Ghoul.class));
			case 17:
                List.addAll(NumMob(Elemental.random(),2));
                List.addAll(NumMob(Ghoul.class,3));
                List.addAll(NumMob(Monk.class,2));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Elemental.random(), Elemental.random(),
//						Ghoul.class, Ghoul.class, Ghoul.class,
//						Monk.class, Monk.class));
			case 18:
                List.addAll(NumMob(Monk.class,3));
                List.addAll(NumMob(Warlock.class,2));
                List.addAll(NumMob(Golem.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Monk.class, Monk.class, Monk.class,
//						Warlock.class, Warlock.class,
//						Golem.class));
			case 19: case 20:
                List.addAll(NumMob(Monk.class,1));
                List.addAll(NumMob(Warlock.class,3));
                List.addAll(NumMob(Golem.class,2));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Monk.class,
//						Warlock.class, Warlock.class,Warlock.class,
//						Golem.class,Golem.class));
			case 21:
                List.addAll(NumMob(Guard.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Guard.class, Guard.class));
			case 22:
                List.addAll(NumMob(Guard.class,3));
                List.addAll(NumMob(Dragun.class,1));
                List.addAll(NumMob(Jupiter.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Dragun.class,
//                        Jupiter.class,
//                        Guard.class, Guard.class, Guard.class));
			case 23:
                List.addAll(NumMob(Guard.class,4));
                List.addAll(NumMob(Dragun.class,2));
                List.addAll(NumMob(Jupiter.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Dragun.class, Dragun.class,
//                        Jupiter.class,
//						Guard.class, Guard.class, Guard.class, Guard.class));
			case 24: case 25:
                List.addAll(NumMob(Guard.class,4));
                List.addAll(NumMob(Dragun.class,2));
                List.addAll(NumMob(Jupiter.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Dragun.class, Dragun.class,
//                        Jupiter.class,
//						Guard.class, Guard.class, Guard.class, Guard.class));
			case 26:
                List.addAll(NumMob(Cyclops.class,4));
                List.addAll(NumMob(Succubus.class,2));
                List.addAll(NumMob(Jupiter.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Cyclops.class, Cyclops.class,Cyclops.class, Cyclops.class,
//						Succubus.class,Succubus.class,
//						Jupiter.class));
			case 27:
                List.addAll(NumMob(Cyclops.class,3));
                List.addAll(NumMob(Succubus.class,2));
                List.addAll(NumMob(Jupiter.class,2));
                List.addAll(NumMob(Hydra.class,1));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Cyclops.class, Cyclops.class,Cyclops.class,
//						Succubus.class,Succubus.class,
//						Jupiter.class, Jupiter.class,
//						Hydra.class));
			case 28:
                List.addAll(NumMob(Cyclops.class,2));
                List.addAll(NumMob(Succubus.class,1));
                List.addAll(NumMob(Jupiter.class,1));
                List.addAll(NumMob(Hydra.class,6));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Cyclops.class, Cyclops.class,
//						Succubus.class,
//                        Jupiter.class,
//						Hydra.class, Hydra.class, Hydra.class,
//						Hydra.class, Hydra.class, Hydra.class));
			case 29: case 30:
                List.addAll(NumMob(Cyclops.class,1));
                List.addAll(NumMob(Hydra.class,9));
                return List;
//				return new ArrayList<>(Arrays.asList(
//						Cyclops.class,
//						Hydra.class, Hydra.class, Hydra.class,
//						Hydra.class, Hydra.class, Hydra.class,
//						Hydra.class, Hydra.class, Hydra.class));
		}
	}
}