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

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Level.set;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeepCaveBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.RabbitBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

public class GolyatBomb extends Bomb {
	
	{
		image = ItemSpriteSheet.GOLYAT_BOMB;
	}

    private boolean outMap(int cell){
        Point p = Dungeon.level.cellToPoint(cell);
        return p.x <= 0 || p.y <= 0 || p.x >= Dungeon.level.width() - 1 || p.y >= Dungeon.level.height() - 1;
    }
	
	@Override
	public void explode(int cell) {
		super.explode(cell);
        if(!(Dungeon.bossLevel())||(Dungeon.level instanceof CavesBossLevel)||(Dungeon.level instanceof SewerBossLevel)||(Dungeon.level instanceof RabbitBossLevel)||(Dungeon.level instanceof DeepCaveBossLevel)){
            boolean terrainAffected = false;
            Rect gate = CavesBossLevel.gate;
            for (int i : PathFinder.NEIGHBOURS4){
                //直向邻格的破坏
                int c = cell + i;
                if (c >= 0 && c < Dungeon.level.length()-1){
                    if (Dungeon.level.breakable[c]){
                        if ((Dungeon.level instanceof CavesBossLevel)&&(Dungeon.level.map[c] == Terrain.WALL || Dungeon.level.map[c] == Terrain.WALL_DECO)){
                            Point p = Dungeon.level.cellToPoint(c);
                            if (p.y < gate.bottom && p.x > gate.left-2 && p.x < gate.right+2){
                                continue;
                                //搬运的DM300代码，现在仍然保留对15楼地形的破坏，但是gate周围的墙被禁止破坏，以免以低代价逃课15楼
                            }
                        }
                        if (outMap(c)) {
                            continue;
                        }
                        set(c, Terrain.EMBERS);
                        GameScene.updateMap(c);
                        terrainAffected = true;
                    }
                    Heap heap = Dungeon.level.heaps.get(c);
                    if (heap != null)
                        heap.explodeLOCK();
                }
            }
            if(Dungeon.level.breakable[cell]){
                //中心破坏
                set(cell, Terrain.EMBERS);
                GameScene.updateMap(cell);
                terrainAffected = true;
            }
            for (int i : PathFinder.NEIGHBOURS9){
                //对陷阱的破坏提升到3*3范围
                int d = cell + i;
                if (outMap(d)) {
                    continue;
                }
                if ((Dungeon.level.map[d] == Terrain.TRAP) || (Dungeon.level.map[d] == Terrain.INACTIVE_TRAP) || (Dungeon.level.map[d] == Terrain.SECRET_TRAP)) {
                    set(d, Terrain.EMBERS);
                    GameScene.updateMap(d);
                    terrainAffected = true;
                }
            }
            for (int i : PathFinder.NEIGHBOURS25){
                //对失效陷阱的破坏提升到5*5范围
                int e = cell + i;
                if (outMap(e)) {
                    continue;
                }
                if (Dungeon.level.map[e] == Terrain.INACTIVE_TRAP) {
                    set(e, Terrain.EMBERS);
                    GameScene.updateMap(e);
                    terrainAffected = true;
                }
            }
            if (terrainAffected) {
                Dungeon.observe();
            }
            //从DM300处复制来的更新墙体的阴影
            Dungeon.level.cleanWalls();
        }
        Heap heap = Dungeon.level.heaps.get(cell);
        if (heap != null)
            heap.explodeLOCK();
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 30);
	}
}
