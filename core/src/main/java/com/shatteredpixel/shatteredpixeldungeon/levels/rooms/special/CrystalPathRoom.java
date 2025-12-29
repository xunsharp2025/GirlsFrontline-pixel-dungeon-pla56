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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CrystalPathRoom extends SpecialRoom {
    @Override
    public int minWidth() { return 7; }
    public int minHeight() { return 7; }

    boolean old = ( Random.Int(100)<50 );
    //old的作用为随机取代，后面的random的写法是为了方便转换成百分比来理解，此时的(100)<50的概率是50/100=50%
    //此处的命名规则为，A是旧的三水晶房，B是六选三
    @Override
    public void paint(Level level){
        if(old){
            paintA(level);
        }else {
            paintB(level);
        }
    }
    private void paintA(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY_SP );

        Door entrance = entrance();

        Point center;
        do {
            center = center();
        } while (center.x == entrance.x || center.y == entrance.y);

        //draw walls between internal rooms
        Painter.drawLine(level, new Point(center.x, top+1), new Point(center.x, bottom-1), Terrain.WALL);
        Painter.drawLine(level, new Point(left+1, center.y), new Point(right-1, center.y), Terrain.WALL);

        Point door = new Point(entrance);

        //determine if the door and loot order should be clockwise or counterclockwise
        boolean clockwise;
        if (entrance.x == left || entrance.x == right){
            door.x = center.x;
            clockwise = entrance.y < center.y;
            if (entrance.x  == right) clockwise = !clockwise;
        } else {
            door.y = center.y;
            clockwise = entrance.x > center.x;
            if (entrance.y == bottom) clockwise = !clockwise;
        }

        //define the four sub-rooms. clockwise from top-left
        Room[] rooms = new EmptyRoom[4];
        rooms[0] = new EmptyRoom();
        rooms[0].set(left+1, top+1, center.x-1, center.y-1);
        rooms[1] = new EmptyRoom();
        rooms[1].set(center.x+1, top+1, right-1, center.y-1);
        rooms[2] = new EmptyRoom();
        rooms[2].set(center.x+1, center.y+1, right-1, bottom-1);
        rooms[3] = new EmptyRoom();
        rooms[3].set(left+1, center.y+1, center.x-1, bottom-1);

        //place 3 crystal doors in the center between rooms,
        // forming a clockwise or counterclockwise pattern
        for (int i = 0; i < 3; i++){
            if (door.x == center.x){
                if (door.y < center.y){
                    door.y = rooms[0].center().y;
                } else {
                    door.y = rooms[2].center().y;
                }
            } else {
                if (door.x < center.x){
                    door.x = rooms[0].center().x;
                } else {
                    door.x = rooms[1].center().x;
                }
            }
            Painter.set(level, door, Terrain.CRYSTAL_DOOR);
            door.x -= center.x;
            door.y -= center.y;
            int tmp = door.x;
            door.x = door.y;
            door.y = tmp;
            if (clockwise)  door.x = -door.x;
            else            door.y = -door.y;
            door.x += center.x;
            door.y += center.y;
        }

        //figure out room order for loot, and start generating it!
        int idx = 0;
        for (int i = 0; i < rooms.length; i++){
            rooms[i].set(rooms[i].shrink(-2)); //we grow/shrink the room to increase intersection bounds
            if (rooms[i].inside(entrance)){
                idx = i;
            }
            rooms[i].set(rooms[i].shrink(2));
        }

        for (int i = 0; i < 4; i++){
            int pos = level.pointToCell(rooms[idx].center());
            Item item;
            switch (i){
                case 0: default:
                    item = new Gold().random();
                    break;
                case 1:
                    item = Generator.random(Random.oneOf(Generator.Category.POTION));
                    break;
                case 2:
                    item = Generator.random(Random.oneOf(Generator.Category.SCROLL));
                    break;
                case 3:
                    int roll = Random.Int(3);
                    if (roll == 0) {
                        item = new StoneOfEnchantment();
                    } else if (roll == 1) {
                        item = new PotionOfExperience();
                    } else {
                        item = new ScrollOfTransmutation();
                    }
                    break;
            }
            level.drop(item, pos);
            if (clockwise){
                idx++;
                if (idx > 3) idx = 0;
            } else {
                idx--;
                if (idx < 0) idx = 3;
            }
        }

        level.addItemToSpawn( new CrystalKey( Dungeon.depth ) );
        level.addItemToSpawn( new CrystalKey( Dungeon.depth ) );
        level.addItemToSpawn( new CrystalKey( Dungeon.depth ) );

        entrance().set( Door.Type.LOCKED );
        level.addItemToSpawn( new IronKey( Dungeon.depth ) );

    }
    private void paintB(Level level) {
        Painter.fill(level, this, 4);
        EmptyRoom[] rooms = new EmptyRoom[6];

        for(int i = 0; i < rooms.length; ++i) {
            rooms[i] = new EmptyRoom();
        }

        Point entry = new Point(this.entrance());
        int prize1 = 0;
        int prize2 = 0;
        if (entry.x != this.left && entry.x != this.right) {
            Painter.drawInside(level, this, entry, this.height() > 8 ? 5 : 3, 1);
            int roomW = this.width() >= 9 ? 2 : 1;
            int roomH1 = this.height() >= 9 ? 2 : 1;
            int roomH2 = this.height() % 2 == 0 ? 2 : 1;
            if (entry.y == this.top) {
                rooms[0].setPos(entry.x - roomW - 1, this.top + 1).resize(roomW - 1, roomH1 - 1);
                Painter.set(level, rooms[0].right + 1, rooms[0].top, 31);
                rooms[1].setPos(entry.x + 2, this.top + 1).resize(roomW - 1, roomH1 - 1);
                Painter.set(level, rooms[1].left - 1, rooms[1].top, 31);
                rooms[2].setPos(entry.x - roomW - 1, rooms[1].bottom + 2).resize(roomW - 1, roomH1 - 1);
                Painter.set(level, rooms[2].right + 1, rooms[2].top, 31);
                rooms[3].setPos(entry.x + 2, rooms[1].bottom + 2).resize(roomW - 1, roomH1 - 1);
                Painter.set(level, rooms[3].left - 1, rooms[3].top, 31);
                rooms[4].setPos(entry.x - roomW - 1, rooms[3].bottom + 2).resize(roomW, roomH2 - 1);
                Painter.set(level, rooms[4].right - 1, rooms[4].top - 1, 31);
                rooms[5].setPos(entry.x + 1, rooms[3].bottom + 2).resize(roomW, roomH2 - 1);
                Painter.set(level, rooms[5].left + 1, rooms[5].top - 1, 31);
                prize1 = level.pointToCell(new Point(rooms[4].right, rooms[4].top));
                prize2 = level.pointToCell(new Point(rooms[5].left, rooms[5].top));
            }
            else {
                rooms[0].setPos(entry.x - roomW - 1, this.bottom - roomH1).resize(roomW - 1, roomH1 - 1);
                Painter.set(level, rooms[0].right + 1, rooms[0].bottom, 31);
                rooms[1].setPos(entry.x + 2, this.bottom - roomH1).resize(roomW - 1, roomH1 - 1);
                Painter.set(level, rooms[1].left - 1, rooms[1].bottom, 31);
                rooms[2].setPos(entry.x - roomW - 1, rooms[1].top - roomH1 - 1).resize(roomW - 1, roomH1 - 1);
                Painter.set(level, rooms[2].right + 1, rooms[2].bottom, 31);
                rooms[3].setPos(entry.x + 2, rooms[1].top - roomH1 - 1).resize(roomW - 1, roomH1 - 1);
                Painter.set(level, rooms[3].left - 1, rooms[3].bottom, 31);
                rooms[4].setPos(entry.x - roomW - 1, rooms[3].top - roomH2 - 1).resize(roomW, roomH2 - 1);
                Painter.set(level, rooms[4].right - 1, rooms[4].bottom + 1, 31);
                rooms[5].setPos(entry.x + 1, rooms[3].top - roomH2 - 1).resize(roomW, roomH2 - 1);
                Painter.set(level, rooms[5].left + 1, rooms[5].bottom + 1, 31);
                prize1 = level.pointToCell(new Point(rooms[4].right, rooms[4].bottom));
                prize2 = level.pointToCell(new Point(rooms[5].left, rooms[5].bottom));
            }
        }
        else {
            Painter.drawInside(level, this, entry, this.width() > 8 ? 5 : 3, 1);
            int roomW1 = this.width() >= 9 ? 2 : 1;
            int roomW2 = this.width() % 2 == 0 ? 2 : 1;
            int roomH = this.height() >= 9 ? 2 : 1;
            if (entry.x == this.left) {
                rooms[0].setPos(this.left + 1, entry.y - roomH - 1).resize(roomW1 - 1, roomH - 1);
                Painter.set(level, rooms[0].left, rooms[0].bottom + 1, 31);
                rooms[1].setPos(this.left + 1, entry.y + 2).resize(roomW1 - 1, roomH - 1);
                Painter.set(level, rooms[1].left, rooms[1].top - 1, 31);
                rooms[2].setPos(rooms[1].right + 2, entry.y - roomH - 1).resize(roomW1 - 1, roomH - 1);
                Painter.set(level, rooms[2].left, rooms[2].bottom + 1, 31);
                rooms[3].setPos(rooms[1].right + 2, entry.y + 2).resize(roomW1 - 1, roomH - 1);
                Painter.set(level, rooms[3].left, rooms[3].top - 1, 31);
                rooms[4].setPos(rooms[3].right + 2, entry.y - roomH - 1).resize(roomW2 - 1, roomH);
                Painter.set(level, rooms[4].left - 1, rooms[4].bottom - 1, 31);
                rooms[5].setPos(rooms[3].right + 2, entry.y + 1).resize(roomW2 - 1, roomH);
                Painter.set(level, rooms[5].left - 1, rooms[5].top + 1, 31);
                prize1 = level.pointToCell(new Point(rooms[4].left, rooms[4].bottom));
                prize2 = level.pointToCell(new Point(rooms[5].left, rooms[5].top));
            }
            else {
                rooms[0].setPos(this.right - roomW1, entry.y - roomH - 1).resize(roomW1 - 1, roomH - 1);
                Painter.set(level, rooms[0].right, rooms[0].bottom + 1, 31);
                rooms[1].setPos(this.right - roomW1, entry.y + 2).resize(roomW1 - 1, roomH - 1);
                Painter.set(level, rooms[1].right, rooms[1].top - 1, 31);
                rooms[2].setPos(rooms[1].left - roomW1 - 1, entry.y - roomH - 1).resize(roomW1 - 1, roomH - 1);
                Painter.set(level, rooms[2].right, rooms[2].bottom + 1, 31);
                rooms[3].setPos(rooms[1].left - roomW1 - 1, entry.y + 2).resize(roomW1 - 1, roomH - 1);
                Painter.set(level, rooms[3].right, rooms[3].top - 1, 31);
                rooms[4].setPos(rooms[3].left - roomW2 - 1, entry.y - roomH - 1).resize(roomW2 - 1, roomH);
                Painter.set(level, rooms[4].right + 1, rooms[4].bottom - 1, 31);
                rooms[5].setPos(rooms[3].left - roomW2 - 1, entry.y + 1).resize(roomW2 - 1, roomH);
                Painter.set(level, rooms[5].right + 1, rooms[5].top + 1, 31);
                prize1 = level.pointToCell(new Point(rooms[4].right, rooms[4].bottom));
                prize2 = level.pointToCell(new Point(rooms[5].right, rooms[5].top));
            }
        }

        for(EmptyRoom room : rooms) {
            Painter.fill(level, room, 14);
        }
        //药水物品链表
        ArrayList<Item> potions = new ArrayList();
        //磁盘物品链表
        ArrayList<Item> scrolls = new ArrayList();
        //填充药水链表
        potions.add(Generator.random(Random.oneOf(Generator.Category.POTION)));//potion[0]
        potions.add(Generator.random(Random.oneOf(Generator.Category.POTION)));//potion[1]
        //填充磁盘链表
        scrolls.add(Generator.random(Random.oneOf(Generator.Category.SCROLL)));//scroll[0]
        scrolls.add(Generator.random(Random.oneOf(Generator.Category.SCROLL)));//scroll[1]
        Potion p = new PotionOfExperience();
        Scroll s = new ScrollOfTransmutation();
        boolean isEXP = false;
        if(p.isKnown()&&s.isKnown()){
            potions.add(p);
            scrolls.add(s);
        } else {
            if (Random.Int(2) == 0) {
                scrolls.add(new ScrollOfTransmutation());
                potions.add(Generator.random(Random.oneOf(Generator.Category.POTION)));
                isEXP = false;
            } else {
                scrolls.add(Generator.random(Random.oneOf(Generator.Category.SCROLL)));
                potions.add(new PotionOfExperience());
                isEXP = true;
            }
        }
        if ((p.isKnown() && s.isKnown()) || (!p.isKnown() && !s.isKnown())) {
            Painter.set(level, prize1, 11);
            Painter.set(level, prize2, 11);
        }else {
            if(isEXP){
                Painter.set(level, prize1, 11);
                Painter.set(level, prize2, 9);
            }else {
                Painter.set(level, prize1, 9);
                Painter.set(level, prize2, 11);
            }
        }
        /*以下为物品生成并掉落，读取对应链表的第0位，将其生成并移除（移除后后续物品填充上来）
        然后在对应的room将生成的物品drop到地上*/
        //以下是药水的生成，同侧room为0,2,4
        level.drop((Item)potions.remove(0), level.pointToCell(rooms[0].center()));
        level.drop((Item)potions.remove(0), level.pointToCell(rooms[2].center()));
        level.drop((Item)potions.remove(0), prize1);
        /*prize为破碎的机制，意为六选三的基座所在格，让保底经验/升级放置在基座上
        这里移除与否都没有影响，而且不移除可以让物品显得不那么凌乱*/
        //以下是磁盘的生成，同侧room为1,3,5
        level.drop((Item)scrolls.remove(0), level.pointToCell(rooms[1].center()));
        level.drop((Item)scrolls.remove(0), level.pointToCell(rooms[3].center()));
        level.drop((Item)scrolls.remove(0), prize2);
        level.addItemToSpawn(new CrystalKey(Dungeon.depth));
        level.addItemToSpawn(new CrystalKey(Dungeon.depth));
        level.addItemToSpawn(new CrystalKey(Dungeon.depth));
        this.entrance().set(Door.Type.UNLOCKED);
    }
    @Override
    public boolean canConnect(Point p){
        if(old){
            return canConnectA(p);
        }else {
            return canConnectB(p);
        }
    }
    private boolean canConnectA(Point p) {
        if (!super.canConnect(p)){
            return false;
        }
        //don't place door in the exact center, if that exists
        if (width() % 2 == 1 && p.x == center().x){
            return false;
        }
        if (height() % 2 == 1 && p.y == center().y){
            return false;
        }
        return true;
    }
    private boolean canConnectB(Point p) {
        if (!super.canConnect(p)) {
            return false;
        } else if (Math.abs((float)p.x - ((float)this.right - (float)(this.width() - 1) / 2.0F)) < 1.0F) {
            return true;
        } else {
            return Math.abs((float)p.y - ((float)this.bottom - (float)(this.height() - 1) / 2.0F)) < 1.0F;
        }
    }
//下面的三个是否可以放置【地形】是六选三有定义，三水晶无定义
    public boolean canPlaceGrass(Point p) {
        if(old){
            return super.canPlaceGrass(p);
        }else {
            return false;
        }
    }

    public boolean canPlaceWater(Point p) {
        if(old){
            return super.canPlaceWater(p);
        }else {
            return false;
        }
    }

    public boolean canPlaceTrap(Point p) {
        if(old){
            return super.canPlaceTrap(p);
        }else {
            return false;
        }
    }
}
