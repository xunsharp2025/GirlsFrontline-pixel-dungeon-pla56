package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;


import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import java.util.ArrayList;
import java.util.Arrays;

public class MapEditor extends TestItem {
    public int chosen;
    public RenderedTextBlock name;
    private static final String AC_SET = "set";
    private final ArrayList<Integer> X;

    public MapEditor() {
        this.image = ItemSpriteSheet.PICKAXE;
        this.defaultAction = "put";
        this.X = new ArrayList(Arrays.asList(DungeonTileSheet.CHASM, DungeonTileSheet.FLOOR, DungeonTileSheet.GRASS, DungeonTileSheet.EMPTY_WELL, DungeonTileSheet.FLAT_WALL, DungeonTileSheet.FLAT_DOOR, DungeonTileSheet.FLAT_DOOR_OPEN, DungeonTileSheet.ENTRANCE, DungeonTileSheet.EXIT, DungeonTileSheet.EMBERS, DungeonTileSheet.FLAT_DOOR_LOCKED, DungeonTileSheet.PEDESTAL, DungeonTileSheet.FLAT_WALL_DECO, DungeonTileSheet.FLAT_BARRICADE, DungeonTileSheet.FLOOR_SP, DungeonTileSheet.FLAT_HIGH_GRASS, DungeonTileSheet.FLAT_DOOR, -1, -1, -1, DungeonTileSheet.FLOOR_DECO, DungeonTileSheet.LOCKED_EXIT, DungeonTileSheet.UNLOCKED_EXIT, -1, DungeonTileSheet.WELL, DungeonTileSheet.FLAT_STATUE, DungeonTileSheet.FLAT_STATUE_SP, DungeonTileSheet.FLAT_BOOKSHELF, DungeonTileSheet.FLAT_ALCHEMY_POT, DungeonTileSheet.WATER, DungeonTileSheet.FLAT_FURROWED_GRASS, DungeonTileSheet.FLAT_DOOR_CRYSTAL));
    }

    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add("put");
        actions.add("set");
        return actions;
    }

    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals("put")) {
            GameScene.selectCell(new CellSelector.Listener() {
                public void onSelect(Integer cell) {
                    if (MapEditor.this.chosen != 17 && MapEditor.this.chosen != 18 && MapEditor.this.chosen != 19 && cell != null) {
                        Level.set(cell, MapEditor.this.chosen);
                        GameScene.updateMap(cell);
                    }

                    MapEditor.curUser.next();
                }

                public String prompt() {
                    return M.L(MapEditor.class, "prompt", new Object[0]);
                }
            });
        }

        if (action.equals("set")) {
            GameScene.show(new SettingsWindow());
        }

    }

    private class SettingsWindow extends Window {
        public SettingsWindow() {
            for(int i = 0; i < 32; ++i) {
                TerrainButton terrainButton = MapEditor.this.new TerrainButton(i);
                int x = i % 6 * 18 + 2;
                int y = i / 6 * 18 + 2;
                terrainButton.setRect((float)x, (float)y, 16.0F, 16.0F);
                this.add(terrainButton);
            }

            MapEditor.this.name = PixelScene.renderTextBlock("深渊", 9);
            MapEditor.this.name.setPos((110.0F - MapEditor.this.name.width()) / 2.0F, 112.0F);
            this.add(MapEditor.this.name);
            this.resize(110, 114 + (int)MapEditor.this.name.height());
        }
    }

    private class TerrainButton extends IconButton {
        public int terrain = -1;

        public TerrainButton(int terrain) {
            this.terrain = terrain;
            switch (terrain) {
                case 17:
                case 18:
                case 19:
                case 23:
                    break;
                case 20:
                case 21:
                case 22:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                default:
                    int x = (Integer)MapEditor.this.X.get(terrain) % 16;
                    int y = (Integer)MapEditor.this.X.get(terrain) / 16;
                    this.icon(new Image(Dungeon.level.tilesTex(), x * 24, y * 24, 24, 24));
                    break;
                case 29:
                    this.icon(new Image(Dungeon.level.waterTex(), 0, 0, 24, 24));
            }

        }

        public void onClick() {
            MapEditor.this.chosen = this.terrain;
            switch (this.terrain) {
                case 16:
                    MapEditor.this.name.text("隐藏门");
                    break;
                case 17:
                    MapEditor.this.name.text("隐藏陷阱");
                    break;
                case 18:
                    MapEditor.this.name.text("陷阱");
                    break;
                case 19:
                    MapEditor.this.name.text("已触发陷阱");
                    break;
                case 20:
                case 21:
                case 22:
                default:
                    MapEditor.this.name.text(Dungeon.level.tileName(this.terrain));
                    break;
                case 23:
                    MapEditor.this.name.text("被移除的告示牌");
            }

            MapEditor.this.name.setPos((110.0F - MapEditor.this.name.width()) / 2.0F, 112.0F);
        }
    }
}