package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.ZeroLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Room404;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Teleporter;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;

public class ZeroLevelSub extends Level {
    private static final int SIZE = 17;
    private static final int WIDTH = SIZE;
    private static final int HEIGHT = SIZE;
    private static final int TEMP_MIN = 2;
    private static final int TEMP_MAX = SIZE - 3;

    public static final int toForwardCamp = (TEMP_MIN + 1) * WIDTH + (SIZE - 3);
    public static final int toRoom404     = (TEMP_MIN + 1) * WIDTH + (TEMP_MAX - 1);

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_ZERO_LEVEL;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    // 获取地块的名字
    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.SIGN:
                return "";
            default:
                return super.tileName( tile );
        }
    }

    // 获取地块的描述
    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.SIGN:
                return "";
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    protected boolean build() {
        setSize(SIZE, SIZE);

        for (int i = 1; i < SIZE - 1; i++) {
            for (int j = 1; j < SIZE - 1; j++) {
                map[i * width() + j] = Terrain.EMPTY;
            }
        }

        int center = (TEMP_MAX + TEMP_MIN) / 2;
        entrance = center * width() + center;
        exit = center * width() + center;

        // 添加向上的楼梯
        map[toForwardCamp] = Terrain.DOOR;
        placeTrigger(new Teleporter().create(toForwardCamp,ZeroLevel.toZeroLevelSub,0));

        // 添加向下的楼梯到404房间(右上角位置)
        map[toRoom404] = Terrain.DOOR;
        placeTrigger(new Teleporter().create(toRoom404,Room404.toZeroLevelSub,2000));

        CustomTilemap customBottomTile = new CustomBottomTile();
        customBottomTile.setRect(0, 0, width(), height());
        customTiles.add(customBottomTile);

        return true;
    }

    public static class CustomBottomTile extends CustomTilemap {
        {
            texture = Assets.Environment.ZERO_LEVEL;
            tileW = SIZE;
            tileH = SIZE;
        }

        @Override
        public Tilemap create() {
            super.create();
            mapSimpleImage(0, 0, 24);
            return vis;
        }
    }

    @Override
    public Mob createMob() {
        return null;
    }

    @Override
    protected void createMobs() {
    }

    @Override
    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
    }

    // 初始化locked变量为true以禁用饥饿值增加
    {
        locked = true;
    }

    // 重写updateFieldOfView方法，实现永久视野
    @Override
    public void updateFieldOfView(Char c, boolean[] fieldOfView) {
        // 对于0-1层，设置所有单元格为可见
        for (int i = 0; i < fieldOfView.length; i++) {
            fieldOfView[i] = true;
        }

        // 同时将所有单元格标记为已映射和已访问，确保完全可见
        if (mapped != null) {
            for (int i = 0; i < mapped.length; i++) {
                mapped[i] = true;
            }
        }
        if (visited != null) {
            for (int i = 0; i < visited.length; i++) {
                visited[i] = true;
            }
        }
    }
}