package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Typhootin;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TrapPlacer;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class DeepCaveBossLevel extends Level {

    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;

        viewDistance = Math.min(6, viewDistance);
    }

    @Override
    public void playLevelMusic() {
        if (locked){
            Music.INSTANCE.play(Assets.Music.RECAVES_BOSS, true);
        } else if (!enteredArena){
            Music.INSTANCE.end();
        } else {
            Music.INSTANCE.play(Assets.Music.RECAVES_1, true);
        }
    }

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    private static final int ROOM_LEFT		= WIDTH / 2 - 3;
    private static final int ROOM_RIGHT		= WIDTH / 2 + 1;
    private static final int ROOM_TOP		= HEIGHT / 2 - 2;
    private static final int ROOM_BOTTOM	= HEIGHT / 2 + 2;

    private boolean enteredArena = false;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_RECAVES;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    private static final String ENTERED	= "entered";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( ENTERED, enteredArena );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        enteredArena = bundle.getBoolean( ENTERED );
    }

    @Override
    protected boolean build() {

        setSize(WIDTH, HEIGHT);

        Rect space = new Rect();

        space.set(
                Random.IntRange(2, 6),
                Random.IntRange(2, 6),
                Random.IntRange(width-6, width-2),
                Random.IntRange(height-6, height-2)
        );

        Painter.fillEllipse( this, space, Terrain.EMPTY );

        exit = space.left + space.width()/2 + (space.top - 1) * width();

        map[exit] = Terrain.LOCKED_EXIT;

        Painter.fill( this,ROOM_LEFT-1,ROOM_TOP-1,
                ROOM_RIGHT-ROOM_LEFT+3,ROOM_BOTTOM-ROOM_TOP+3,Terrain.WALL );
        Painter.fill( this,ROOM_LEFT  ,ROOM_TOP  ,
                ROOM_RIGHT-ROOM_LEFT+1,ROOM_BOTTOM-ROOM_TOP+1,Terrain.EMPTY);
        Painter.fill( this,ROOM_LEFT-1,ROOM_TOP+1,
                ROOM_RIGHT-ROOM_LEFT+3,ROOM_BOTTOM-ROOM_TOP-1,Terrain.EMPTY);

        entrance = Random.Int( ROOM_LEFT + 1, ROOM_RIGHT - 1 ) +
                Random.Int( ROOM_TOP + 1, ROOM_BOTTOM - 1 ) * width();
        map[entrance] = Terrain.ENTRANCE;

        boolean[] patch = Patch.generate( width, height, 0.30f, 6, true );
        for (int i=0; i < length(); i++) {
            if (map[i] == Terrain.EMPTY && patch[i]) {
                map[i] = Terrain.WATER;
            }
        }

        for (int i=0; i < length(); i++) {
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
                if (map[i] == Terrain.EMPTY && Random.Int( 6 ) == 0) {
                    map[i] = Terrain.INACTIVE_TRAP;
                    Trap t;
                    if (Random.Int( 8 ) == 0) {
                        t = new CorrosionTrap().reveal();
                        t.active = true;
                        map[i] = Terrain.TRAP;
                    } else if(Random.Int(3) ==0) {
                        t = new ToxicTrap().reveal();
                        t.active = true;
                        map[i] = Terrain.TRAP;
                    } else {
                        DisintegrationTrap emptyTrap = new DisintegrationTrap();
                        emptyTrap.active = false;
                        emptyTrap.reveal();
                        t = emptyTrap;
                        map[i] = Terrain.INACTIVE_TRAP;
                    }
                    setTrap(t, i);
                }
            } else {
                if (map[i] == Terrain.EMPTY && Random.Int( 6 ) == 0) {
                    map[i] = Terrain.INACTIVE_TRAP;
                    Trap t = new ToxicTrap().reveal();
                    t.active = false;
                    setTrap(t, i);
                }
            }

        }

        for (int i=width() + 1; i < length() - width(); i++) {
            if (map[i] == Terrain.EMPTY) {
                int n = 0;
                if (map[i+1] == Terrain.WALL) {
                    n++;
                }
                if (map[i-1] == Terrain.WALL) {
                    n++;
                }
                if (map[i+width()] == Terrain.WALL) {
                    n++;
                }
                if (map[i-width()] == Terrain.WALL) {
                    n++;
                }
                if (Random.Int( 8 ) <= n) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }

        for (int i=0; i < length() - width(); i++) {
            if (map[i] == Terrain.WALL
                    && DungeonTileSheet.floorTile(map[i + width()])
                    && Random.Int( 3 ) == 0) {
                map[i] = Terrain.WALL_DECO;
            }
        }

        return true;
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Item item = Bones.get();
        if (item != null) {
            int pos;
            do {
                pos = Random.IntRange( ROOM_LEFT, ROOM_RIGHT ) + Random.IntRange( ROOM_TOP + 1, ROOM_BOTTOM ) * width();
            } while (pos == entrance);
            drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
        }
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int cell;
        do {
            cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }

    @Override
    public void occupyCell( Char ch ) {
        super.occupyCell( ch );
        if (!enteredArena && outsideEntraceRoom( ch.pos ) && ch == Dungeon.hero) {
            enteredArena = true;
            seal();
        }
    }

    @Override
    public void seal(){
        super.seal();

        for (Mob m : mobs){
            //bring the first ally with you
            if (m.alignment == Char.Alignment.ALLY){
                m.pos = Dungeon.hero.pos + (Random.Int(2) == 0 ? +1 : -1);
                m.sprite.place(m.pos);
                break;
            }
        }

        Typhootin boss = new Typhootin();
        boss.state = boss.WANDERING;
        do {
            boss.pos = Random.Int( length() );
        } while (
           !passable[boss.pos]
        || !outsideEntraceRoom( boss.pos )
        ||  heroFOV[boss.pos]);
        GameScene.add( boss );

        set(entrance ,Terrain.EMPTY);
        GameScene.updateMap(entrance );
        Dungeon.observe();

        Camera.main.shake( 3, 0.7f );
        Sample.INSTANCE.play( Assets.Sounds.ROCKS );
        playLevelMusic();
    }

    @Override
    public void unseal() {
        super.unseal();

        set(entrance ,Terrain.ENTRANCE  );
        GameScene.updateMap(entrance );
        Dungeon.observe();

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                Music.INSTANCE.end();
            }
        });
    }

    private boolean outsideEntraceRoom( int cell ) {
        int cx = cell % width();
        int cy = cell / width();
        return cx < ROOM_LEFT-1 || cx > ROOM_RIGHT+1 || cy < ROOM_TOP-1 || cy > ROOM_BOTTOM+1;
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.GRASS:
                return Messages.get(CavesLevel.class, "grass_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(CavesLevel.class, "high_grass_name");
            case Terrain.WATER:
                return Messages.get(CavesLevel.class, "water_name");
            case Terrain.STATUE:case Terrain.STATUE_SP:
                return Messages.get(DeepCaveLevel.class, "statue_name2");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc( int tile ) {
        switch (tile) {
            case Terrain.ENTRANCE:
                return Messages.get(CavesLevel.class, "entrance_desc");
            case Terrain.EXIT:
                return Messages.get(CavesLevel.class, "exit_desc");
            case Terrain.HIGH_GRASS:
                return Messages.get(CavesLevel.class, "high_grass_desc");
            case Terrain.WALL_DECO:
                return Messages.get(CavesLevel.class, "wall_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(CavesLevel.class, "bookshelf_desc");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(DeepCaveLevel.class, "statue_desc2");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        CavesLevel.addCavesVisuals(this, visuals);
        return visuals;
    }
}
