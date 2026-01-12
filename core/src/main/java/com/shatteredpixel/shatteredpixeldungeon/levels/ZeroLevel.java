package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.levels.ZeroLevelSub;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.WindowTrigger;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Teleporter;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Trigger;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SnakeScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SavesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.BadgesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;

import java.io.IOException;

// 导入Messages类
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

// 零层(ZeroLevel) - 游戏的起始房间或特殊房间
public class ZeroLevel extends Level {
	// 房间尺寸常量，宽16格，高10格
	private static final int WIDTH  = 16;
	private static final int HEIGHT = 11;
	// 临时最小和最大位置常量，用于定位房间内的元素
	private static final int TEMP_MIN = 2;
	private static final int TEMP_MAX = 9;

	// 定义了传送触发器的位置
	public static final int toZeroLevelSub = 9*WIDTH+13;

	// 定义了六个电脑地块的位置
	private static final int computerPos0 = 6*WIDTH+6;
	private static final int computerPos1 = 6*WIDTH+7;
	private static final int computerPos2 = 6*WIDTH+8;
	private static final int computerPos3 = 7*WIDTH+6;
	private static final int computerPos4 = 7*WIDTH+7;
	private static final int computerPos5 = 7*WIDTH+8;
	// 定义了消毒通道的位置
	private static final int decontaminationCorridorPos = 5*WIDTH+13;
	// 定义了成就按钮的位置（消毒通道上方一格向左移动两格，再向上移动两格）
private static final int achievementButtonPos = 2*WIDTH+11;

	// 定义地形常量，用于构建硬编码地图
	private static final int W = Terrain.WALL;      // 墙
	private static final int e = Terrain.EMPTY;     // 空地
	private static final int S = Terrain.STATUE;    // 雕像
	private static final int Z = Terrain.SIGN;    // 其他碰撞体
	private static final int D = Terrain.DOOR;      // 门
	private static final int C = Terrain.SIGN;    // 电脑（用于切换到标题场景的地块）

	// 硬编码的地图数据
	private static final int[] MAP = {
		W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
		W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
		W, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, W,
		W, e, e, Z, Z, Z, Z, Z, e, e, e, e, Z, Z, Z, W,
		W, e, e, e, e, e, e, e, e, e, e, e, e, Z, Z, W,
		W, e, e, e, e, e, e, e, e, e, e, e, e, Z, Z, W,
		W, e, e, e, Z, Z, C, C, C, Z, Z, e, e, e, e, W,
		W, e, e, e, e, e, C, C, C, e, e, e, e, e, e, W,
		W, Z, Z, e, e, e, e, e, e, e, e, e, e, e, Z, W,
		W, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, e, Z, W,
		W, W, W, W, W, W, W, W, W, W, W, W, W, Z, W, W,
	};

	// 获取地块的瓦片纹理
	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_ZERO_LEVEL;
	}
	
	// 获取地块的水纹理
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
	
	// 电脑触发器 - 用于切换到标题场景
	public static class ComputerTriger extends WindowTrigger{
		// 检查角色是否可以与电脑交互
		@Override
		public boolean canInteract(Char ch){
			return Dungeon.hero==ch && Dungeon.level.adjacent(pos,ch.pos);
		}

		public class WndComputer extends Window {
			private static final int WIDTH		= 120;
			private static final int BTN_HEIGHT	= 20;
			private static final int GAP		= 2;
			
			private int pos;
			
			public WndComputer() {
				//settings
				RedButton curBtn;

				addButton(curBtn = new RedButton("贪吃蛇-DEMO"){/*mark*/
					@Override
					protected void onClick() {
						try{Dungeon.saveAll();
						}catch(IOException e){Game.reportException(e);}
						Game.switchScene(SnakeScene.class);
					}
				});
				curBtn.icon(Icons.get(Icons.DISPLAY));


				addButton(curBtn = new RedButton("主菜单"){/*mark*/
					@Override
					protected void onClick() {
						try{Dungeon.saveAll();
						}catch(IOException e){Game.reportException(e);}
						Game.switchScene(TitleScene.class);
					}
				});
				curBtn.icon(Icons.get(Icons.DISPLAY));

				resize( WIDTH, pos );
			}
			
			private void addButton( RedButton btn ) {
				add( btn );
				btn.setRect( 0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT );
				pos += BTN_HEIGHT;
			}
		}

		@Override
		protected Window getWindow(){
			return new WndComputer();
		}
	}

	// 消毒通道 - 用于展示存档页面
	public static class DecontaminationCorridor extends WindowTrigger{
		@Override
		public boolean canInteract(Char ch){
			//仅在左边才能触发
			return Dungeon.hero==ch && Dungeon.level.adjacent(pos,ch.pos) && (ch.pos+1)==pos;
		}

		public class WndDecontaminationCorridor extends Window {
			private static final int WIDTH		= 120;
			private static final int BTN_HEIGHT	= 20;
			private static final int GAP		= 2;
			
			private int pos;
			
			public WndDecontaminationCorridor() {
				//settings
				RedButton curBtn;

				addButton(curBtn = new RedButton("进入"){/*mark*/
					@Override
					protected void onClick() {
						try{Dungeon.saveAll();
						}catch(IOException e){Game.reportException(e);}
						Game.switchScene(SavesScene.class);
					}
				});
				curBtn.icon(Icons.get(Icons.DISPLAY));

				resize( WIDTH, pos );
			}
			
			private void addButton( RedButton btn ) {
				add( btn );
				btn.setRect( 0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT );
				pos += BTN_HEIGHT;
			}
		}

		@Override
		protected Window getWindow(){
			return new WndDecontaminationCorridor();
		}
	}
	
	// 成就按钮 - 用于打开成就系统
public static class AchievementButton extends WindowTrigger{
	@Override
	public boolean canInteract(Char ch){
		//可以从任何相邻位置触发
		return Dungeon.hero==ch && Dungeon.level.adjacent(pos,ch.pos);
	}

		public class WndAchievementButton extends Window {
			private static final int WIDTH		= 120;
			private static final int BTN_HEIGHT	= 20;
			private static final int GAP		= 2;
			
			private int pos;
			
			public WndAchievementButton() {
				//settings
				RedButton curBtn;

				addButton(curBtn = new RedButton("成就"){/*mark*/
					@Override
					protected void onClick() {
						try{Dungeon.saveAll();
						}catch(IOException e){Game.reportException(e);}
						GirlsFrontlinePixelDungeon.switchNoFade(BadgesScene.class);
					}
				});
				curBtn.icon(Icons.get(Icons.BADGES));

				addButton(curBtn = new RedButton("排行榜"){/*mark*/
					@Override
					protected void onClick() {
						try{Dungeon.saveAll();
						}catch(IOException e){Game.reportException(e);}
						GirlsFrontlinePixelDungeon.switchNoFade(RankingsScene.class);
					}
				});
				curBtn.icon(Icons.get(Icons.RANKINGS));

				resize( WIDTH, pos );
			}
			
			private void addButton( RedButton btn ) {
				add( btn );
				btn.setRect( 0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT );
				pos += BTN_HEIGHT;
			}
		}

		@Override
		protected Window getWindow(){
			return new WndAchievementButton();
		}
	}

	// 构建零层房间
	@Override
	protected boolean build() {
		// 设置房间尺寸
		setSize(WIDTH, HEIGHT);

		// 应用硬编码的地图数据
		map = MAP.clone();

		// 计算房间中心位置
		int center=(TEMP_MAX+TEMP_MIN)/2;
		// 设置入口和出口位置（均为中心）
		entrance  =center*width()+center;
		//map[entrance]=Terrain.ENTRANCE;
		exit      =center*width()+center;

		// 创建并放置鼠王NPC
        RatKing king = new RatKing();
        king.pos = (TEMP_MIN+2)*width()+TEMP_MIN+1;
        mobs.add( king );

		// 放置电脑触发器(用于回到标题页面)
		placeTrigger(new ComputerTriger().create(computerPos0));
		placeTrigger(new ComputerTriger().create(computerPos1));
		placeTrigger(new ComputerTriger().create(computerPos2));
		placeTrigger(new ComputerTriger().create(computerPos3));
		placeTrigger(new ComputerTriger().create(computerPos4));
		placeTrigger(new ComputerTriger().create(computerPos5));

		// 放置消毒通道触发器
		placeTrigger(new DecontaminationCorridor().create(decontaminationCorridorPos));
		
		// 放置成就按钮触发器
		placeTrigger(new AchievementButton().create(achievementButtonPos));

		// 添加向下的楼梯触发器
		placeTrigger(new Teleporter().create(toZeroLevelSub,ZeroLevelSub.toForwardCamp,1000));

		// 创建并添加自定义底部瓦片
		CustomTilemap customBottomTile=new CustomBottomTile();
		customBottomTile.setRect(0,0,width(),height());
		customTiles.add(customBottomTile);
		
		// 添加墙体覆盖贴图
		CustomTilemap customWallTile = new CustomWallTile();
		customWallTile.setRect(0, 0, width(), height());
		customWalls.add(customWallTile);

		return true;
	}

	// 自定义底部瓦片 - 用于绘制零层的特殊底部纹理
	public static class CustomBottomTile extends CustomTilemap {
		// 初始化纹理和尺寸
		{
			texture = Assets.Environment.FORWARD_CAMP;
			tileW = WIDTH;
			tileH = HEIGHT;
		}

		// 创建瓦片映射
		@Override
		public Tilemap create() {
			super.create();
			if (vis != null){
				// 使用mapSimpleImage方法生成数据数组并应用到瓦片映射
				// texW参数设置为贴图的总宽度（像素）：16*24=384
				int[] data = mapSimpleImage(0, 0, 384);
				vis.map(data, tileW);
			}
			return vis;
		}

		// 自定义地块名字
		@Override
		public String name(int tileX, int tileY) {
			if      ((tileY*WIDTH+tileX)==computerPos0){
				return Messages.get(this, "computer0.name");
			}else if((tileY*WIDTH+tileX)==computerPos1){
				return Messages.get(this, "computer1.name");
			}else if((tileY*WIDTH+tileX)==computerPos2){
				return Messages.get(this, "computer2.name");
			}else if((tileY*WIDTH+tileX)==computerPos3){
				return Messages.get(this, "computer3.name");
			}else if((tileY*WIDTH+tileX)==computerPos4){
				return Messages.get(this, "computer4.name");
			}else if((tileY*WIDTH+tileX)==computerPos5){
				return Messages.get(this, "computer5.name");
			}else if((tileY*WIDTH+tileX)==decontaminationCorridorPos){
				return Messages.get(this, "decontamination_corridor.name");
			}else if((tileY*WIDTH+tileX)==achievementButtonPos){
				return Messages.get(this, "achievement_button.name");
			}

			return super.name(tileX, tileY);
		}

		// 自定义地块描述
		@Override
		public String desc(int tileX, int tileY) {
			if      ((tileY*WIDTH+tileX)==computerPos0){
				return Messages.get(this, "computer0.desc");
			}else if((tileY*WIDTH+tileX)==computerPos1){
				return Messages.get(this, "computer1.desc");
			}else if((tileY*WIDTH+tileX)==computerPos2){
				return Messages.get(this, "computer2.desc");
			}else if((tileY*WIDTH+tileX)==computerPos3){
				return Messages.get(this, "computer3.desc");
			}else if((tileY*WIDTH+tileX)==computerPos4){
				return Messages.get(this, "computer4.desc");
			}else if((tileY*WIDTH+tileX)==computerPos5){
				return Messages.get(this, "computer5.desc");
			}else if((tileY*WIDTH+tileX)==decontaminationCorridorPos){
				return Messages.get(this, "decontamination_corridor.desc");
			}else if((tileY*WIDTH+tileX)==achievementButtonPos){
				return Messages.get(this, "achievement_button.desc");
			}

			return super.desc(tileX, tileY);
		}
	}
	
	// 自定义墙体瓦片 - 用于绘制零层的特殊墙体纹理
	public static class CustomWallTile extends CustomTilemap {
		// 初始化纹理和尺寸
		{
			texture = Assets.Environment.FORWARD_CAMP_1;
			tileW = WIDTH;
			tileH = HEIGHT;
		}

		// 创建瓦片映射
		@Override
		public Tilemap create() {
			super.create();
			if (vis != null){
				// 使用mapSimpleImage方法生成数据数组并应用到瓦片映射
				// texW参数设置为贴图的总宽度（像素）：16*24=384
				int[] data = mapSimpleImage(0, 0, 384);
				vis.map(data, tileW);
			}
			return vis;
		}
	}
	
	// 创建怪物（零层不自动生成怪物）
	@Override
	public Mob createMob() {
		return null;
	}
	
	// 创建怪物（零层仅手动添加特殊NPC）
	@Override
	protected void createMobs() {
	}

	// 添加重生点（零层不添加重生点）
	@Override
	public Actor addRespawner() {
		return null;
	}

	// 创建物品（零层不自动生成物品）
	@Override
	protected void createItems() {
	}
	
	// 初始化锁定变量为true，禁用饥饿值增加
	{
		locked = true;
	}
	
	// 重写视野更新方法，实现零层的永久视野
	@Override
	public void updateFieldOfView(Char c, boolean[] fieldOfView) {
		// 对于零层，设置所有单元格为可见
		for (int i = 0; i < fieldOfView.length; i++) {
			fieldOfView[i] = true;
		}
		
		// 同时将所有单元格标记为已映射，确保完全可见
		if (mapped != null) {
			for (int i = 0; i < mapped.length; i++) {
				mapped[i] = true;
			}
		}
	}
}