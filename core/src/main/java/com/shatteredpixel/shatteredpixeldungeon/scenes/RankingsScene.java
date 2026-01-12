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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRanking;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.GameMath;

public class RankingsScene extends PixelScene {
	
	private static final float ROW_HEIGHT_MAX	= 20;
	private static final float ROW_HEIGHT_MIN	= 12;

	private static final float MAX_ROW_WIDTH    = 160;

	private static final float GAP	= 4;
	
	private Archs archs;
	// 每页显示的记录数
	private static final int RECORDS_PER_PAGE = 11;
	
	private int currentPage = 0;

	@Override
	public void create() {
		
		super.create();

		Music.INSTANCE.play(Assets.Music.THEME_1,true);

		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		Rankings.INSTANCE.load();

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
				(w - title.width()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);
		
		if (Rankings.INSTANCE.records.size() > 0) {

			// 计算总页数
			int totalPages = (int) Math.ceil((float) Rankings.INSTANCE.records.size() / RECORDS_PER_PAGE);

			// 确保当前页码有效
			currentPage = Math.max(0, Math.min(currentPage, totalPages - 1));

			//attempts to give each record as much space as possible, ideally as much space as portrait mode
			float rowHeight = GameMath.gate(ROW_HEIGHT_MIN, (uiCamera.height - 26)/RECORDS_PER_PAGE, ROW_HEIGHT_MAX);

			float left = (w - Math.min( MAX_ROW_WIDTH, w )) / 2 + GAP;
			float top = (h - rowHeight  * RECORDS_PER_PAGE) / 2;
			
			int startIndex = currentPage * RECORDS_PER_PAGE;
			int endIndex = Math.min(startIndex + RECORDS_PER_PAGE, Rankings.INSTANCE.records.size());
			int pos = 0;
			
			for (int i = startIndex; i < endIndex; i++) {
				Rankings.Record rec = Rankings.INSTANCE.records.get(i);
				Record row = new Record( i, i == Rankings.INSTANCE.lastRecord, rec );
				float offset = 0;
				if (rowHeight <= 14){
					offset = (pos % 2 == 1) ? 5 : -5;
				}
				row.setRect( left+offset, top + pos * rowHeight, w - left * 2, rowHeight );
				add(row);
				
				pos++;
			}
			
			// 游戏次数文本
			RenderedTextBlock label = PixelScene.renderTextBlock( 8 );
			label.hardlight( 0xCCCCCC );
			label.setHightlighting(true, Window.SHPX_COLOR);
			label.text( Messages.get(this, "total") + " _" + Rankings.INSTANCE.wonNumber + "_/" + Rankings.INSTANCE.totalNumber );
			add( label );
			
			// 只有当记录数超过一页时才显示翻页按钮
			if (totalPages > 1) {
				// 上一页按钮
				StyledButton prevButton = new StyledButton(Chrome.Type.GREY_BUTTON, "<"){
					@Override
					protected void onClick() {
						if (currentPage > 0) {
							currentPage--;
							create();
						}
					}
				};
				add(prevButton);
				
				// 下一页按钮
				StyledButton nextButton = new StyledButton(Chrome.Type.GREY_BUTTON, ">"){
					@Override
					protected void onClick() {
						if (currentPage < totalPages - 1) {
							currentPage++;
							create();
						}
					}
				};
				add(nextButton);
				
				// 设置按钮和文本位置 - 按钮大小缩小为原来的1/2
				float btnWidth = 10;
				float btnHeight = 8;
				float centerX = w / 2;
				float bottomY = h - btnHeight - 2*GAP;
				
				label.setPos(
						centerX - label.width() / 2,
						bottomY
				);
				align(label);
				
				prevButton.setRect(
						label.left() - btnWidth - GAP * 3,
						bottomY,
						btnWidth,
						btnHeight
				);
				
				nextButton.setRect(
						label.right() + GAP * 3,
						bottomY,
						btnWidth,
						btnHeight
				);
				
				// 禁用不可用的按钮
				prevButton.enable(currentPage > 0);
				nextButton.enable(currentPage < totalPages - 1);
			} else {
				// 只有一页记录时，只显示游戏次数文本
				float centerX = w / 2;
				float bottomY = h - label.height() - 2*GAP;
				
				label.setPos(
						centerX - label.width() / 2,
						bottomY
				);
				align(label);
			}
			
		} else {

			RenderedTextBlock noRec = PixelScene.renderTextBlock(Messages.get(this, "no_games"), 8);
			noRec.hardlight( 0xCCCCCC );
			noRec.setPos(
					(w - noRec.width()) / 2,
					(h - noRec.height()) / 2
			);
			align(noRec);
			add(noRec);
			
		}

		//自定义退出按钮，使用与onBackPressed相同的返回逻辑
		ExitButton btnExit = new ExitButton() {
			@Override
			protected void onClick() {
				//如果上一个场景是游戏场景，返回到游戏场景，否则返回到标题场景
				if (GirlsFrontlinePixelDungeon.previousSceneClass == GameScene.class) {
					GirlsFrontlinePixelDungeon.switchNoFade(GameScene.class);
				} else {
					GirlsFrontlinePixelDungeon.switchNoFade(TitleScene.class);
				}
			}
		};
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		//如果上一个场景是游戏场景，返回到游戏场景，否则返回到标题场景
		if (GirlsFrontlinePixelDungeon.previousSceneClass == GameScene.class) {
			GirlsFrontlinePixelDungeon.switchNoFade(GameScene.class);
		} else {
			GirlsFrontlinePixelDungeon.switchNoFade(TitleScene.class);
		}
	}
	
	public static class Record extends Button {
		
		private static final float GAP	= 4;
		
		private static final int[] TEXT_WIN	= {0xFFFF88, 0xB2B25F};
		private static final int[] TEXT_LOSE= {0xDDDDDD, 0x888888};
		private static final int FLARE_WIN	= 0x888866;
		private static final int FLARE_LOSE	= 0x666666;
		
		private Rankings.Record rec;
		
		protected ItemSprite shield;
		private Flare flare;
		private BitmapText position;
		private RenderedTextBlock desc;
		private Image steps;
		private BitmapText depth;
		private Image classIcon;
		private BitmapText level;
		
		public Record( int pos, boolean latest, Rankings.Record rec ) {
			super();
			
			this.rec = rec;
			
			if (latest) {
				flare = new Flare( 6, 24 );
				flare.angularSpeed = 90;
				flare.color( rec.win ? FLARE_WIN : FLARE_LOSE );
				addToBack( flare );
			}

			if (pos != Rankings.TABLE_SIZE-1) {
				position.text(Integer.toString(pos + 1));
			} else
				position.text(" ");
			position.measure();
			
			desc.text( Messages.titleCase(rec.desc()) );

			int odd = pos % 2;
			
			if (rec.win) {
				shield.view( ItemSpriteSheet.AMULET, null );
				position.hardlight( TEXT_WIN[odd] );
				desc.hardlight( TEXT_WIN[odd] );
				depth.hardlight( TEXT_WIN[odd] );
				level.hardlight( TEXT_WIN[odd] );
			} else {
				position.hardlight( TEXT_LOSE[odd] );
				desc.hardlight( TEXT_LOSE[odd] );
				depth.hardlight( TEXT_LOSE[odd] );
				level.hardlight( TEXT_LOSE[odd] );

				if (rec.depth != 0){
					depth.text( Integer.toString(rec.depth) );
					depth.measure();
					steps.copy(Icons.STAIRS.get());

					add(steps);
					add(depth);
				}

			}

			if (rec.herolevel != 0){
				level.text( Integer.toString(rec.herolevel) );
				level.measure();
				add(level);
			}
			
			classIcon.copy( Icons.get( rec.heroClass ) );
			if (rec.heroClass == HeroClass.ROGUE){
				//cloak of shadows needs to be brightened a bit
				classIcon.brightness(2f);
			}
		}
		
		@Override
		protected void createChildren() {
			
			super.createChildren();
			
			shield = new ItemSprite( ItemSpriteSheet.TOMB, null );
			add( shield );
			
			position = new BitmapText( PixelScene.pixelFont);
			add( position );
			
			desc = renderTextBlock( 7 );
			add( desc );

			depth = new BitmapText( PixelScene.pixelFont);

			steps = new Image();
			
			classIcon = new Image();
			add( classIcon );

			level = new BitmapText( PixelScene.pixelFont);
		}
		
		@Override
		protected void layout() {
			
			super.layout();
			
			shield.x = x;
			shield.y = y + (height - shield.height) / 2f;
			align(shield);
			
			position.x = shield.x + (shield.width - position.width()) / 2f;
			position.y = shield.y + (shield.height - position.height()) / 2f + 1;
			align(position);
			
			if (flare != null) {
				flare.point( shield.center() );
			}

			classIcon.x = x + width - 16 + (16 - classIcon.width())/2f;
			classIcon.y = shield.y + (16 - classIcon.height())/2f;
			align(classIcon);

			level.x = classIcon.x + (classIcon.width - level.width()) / 2f;
			level.y = classIcon.y + (classIcon.height - level.height()) / 2f + 1;
			align(level);

			steps.x = x + width - 32 + (16 - steps.width())/2f;
			steps.y = shield.y + (16 - steps.height())/2f;
			align(steps);

			depth.x = steps.x + (steps.width - depth.width()) / 2f;
			depth.y = steps.y + (steps.height - depth.height()) / 2f + 1;
			align(depth);

			desc.maxWidth((int)(steps.x - (shield.x + shield.width + GAP)));
			desc.setPos(shield.x + shield.width + GAP, shield.y + (shield.height - desc.height()) / 2f + 1);
			align(desc);
		}
		
		@Override
		protected void onClick() {
			if (rec.gameData != null) {
				parent.add( new WndRanking( rec ) );
			} else {
				parent.add( new WndError( Messages.get(RankingsScene.class, "no_info") ) );
			}
		}
	}
}
