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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.IntroScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.WndTextInput;
import com.shatteredpixel.shatteredpixeldungeon.ui.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

import java.io.IOException;

public class WndStartGame extends Window {

	private static final int WIDTH	= 117;
	private static final int HEIGHT	= 150;

	public WndStartGame(final int slot){
		super();

		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 12 );
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((WIDTH - title.width())/2f,2);
		add(title);

		// 计算可见角色的数量（排除NONE）
		int numVisibleClasses = 0;
		for (HeroClass cl : HeroClass.values()) {
			if (cl != HeroClass.NONE) {
				numVisibleClasses++;
			}
		}

		// 动态计算角色按钮间距
		// 间距公式：(总宽度 - 所有按钮总宽度)/(按钮数量 + 1)
		float heroBtnSpacing = (WIDTH - numVisibleClasses * HeroBtn.WIDTH) / (numVisibleClasses + 1f);

		float curX = heroBtnSpacing;
		for (HeroClass cl : HeroClass.values()){
			if (cl != HeroClass.NONE){
				HeroBtn button = new HeroBtn(cl);
				button.setRect(curX, title.height() + 4, HeroBtn.WIDTH, HeroBtn.HEIGHT);
				curX += HeroBtn.WIDTH + heroBtnSpacing;
				add(button);
			}
		}

		ColorBlock separator = new ColorBlock(1, 1, 0xFF222222);
		separator.size(WIDTH, 1);
		separator.x = 0;
		separator.y = title.height() + 6 + HeroBtn.HEIGHT;
		add(separator);

		HeroPane ava = new HeroPane();
		ava.setRect(20, separator.y + 15, WIDTH-30, 80);
		add(ava);

		RedButton start = new RedButton(Messages.get(this, "start")){
			@Override
			protected void onClick() {
				if (GamesInProgress.selectedClass == null) return;
				super.onClick();

				Dungeon.hero = null;
				ActionIndicator.action = null;
				GamesInProgress.curSlot = slot;
				InterlevelScene.seedCode=SPDSettings.seedCode();
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

				if (SPDSettings.intro()) {
					SPDSettings.intro( false );
					Game.switchScene( IntroScene.class );
				} else {
					Game.switchScene( InterlevelScene.class );
				}
			}

			@Override
			public void update() {
				if( !visible && GamesInProgress.selectedClass != null){
					visible = true;
				}
				super.update();
			}
		};
		start.visible = false;
		start.setRect(0, HEIGHT - 20, WIDTH, 20);
		add(start);

		if (Badges.isUnlocked(Badges.Badge.KILL_CALC) || DeviceCompat.isDebug()){
			IconButton challengeButton = new IconButton(
					Icons.get( SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
				@Override
				protected void onClick() {
					GirlsFrontlinePixelDungeon.scene().addToFront(new WndChallenges(SPDSettings.challenges(), true) {
						public void onBackPressed() {
							super.onBackPressed();
							icon( Icons.get( SPDSettings.challenges() > 0 ?
									Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF ) );
						}
					} );
				}

				@Override
				public void update() {
					if( !visible && GamesInProgress.selectedClass != null){
						visible = true;
					}
					super.update();
				}
			};
			challengeButton.setRect(WIDTH - 20, HEIGHT - 20, 20, 20);
			challengeButton.visible = false;
			add(challengeButton);
		} else {
			Dungeon.challenges = 0;
			SPDSettings.challenges(0);
		}

		if (Badges.isUnlocked(Badges.Badge.KILL_CALC)||DeviceCompat.isDebug()){
			IconButton seedButton = new IconButton(new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS)){
				@Override
				protected void onClick() {
					GirlsFrontlinePixelDungeon.scene().addToFront(
						new WndTextInput(
							Messages.get(WndStartGame.class, "set_seed_title"),
							Messages.get(WndStartGame.class, "set_seed_desc"),
							SPDSettings.seedCode(),
							20,
							false,
							Messages.get(WndStartGame.class, "set_seed_confirm"),
							Messages.get(WndStartGame.class, "set_seed_cancel")
						){
							@Override
							public void onSelect(boolean check, String text) {
								if(check){
                                    text = DungeonSeed.formatText(text);
                                    long seed = DungeonSeed.convertFromText(text);
                                    if (seed != -1){
                                        SPDSettings.seedCode(text);
                                    } else {
                                        SPDSettings.seedCode(SPDSettings.SEED_CODE_RANDOM);
                                    }
								}
							}
						}
					);
				}

				@Override
				public void update() {
					if( !visible && GamesInProgress.selectedClass != null){
						visible = true;
					}
                    icon(!SPDSettings.seedCode().equals(SPDSettings.SEED_CODE_RANDOM) ? new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS) :new ItemSprite(ItemSpriteSheet.SEED_FADELEAF));
					super.update();
				}
			};
			seedButton.setRect(0,HEIGHT-20,20,20);
			seedButton.visible = false;
			add(seedButton);
		} else {
			SPDSettings.seedCode(null);
		}

		resize(WIDTH, HEIGHT);

	}

	private static class HeroBtn extends Button {

		private HeroClass cl;

		private Image heroIcon;

		private static final int WIDTH = HeroSprite.FRAME_WIDTH;
		private static final int HEIGHT = HeroSprite.FRAME_HEIGHT;

		HeroBtn ( HeroClass cl ){
			super();

			this.cl = cl;
			heroIcon = new Image(cl.spritesheet(), 0, HeroSprite.FRAME_HEIGHT * 2 /*tier*/, HeroSprite.FRAME_WIDTH, HeroSprite.FRAME_HEIGHT);
			add(heroIcon);
		}

		@Override
		protected void layout() {
			super.layout();
			if (heroIcon != null){
				heroIcon.x = x + (width - heroIcon.width()) / 2f;
				heroIcon.y = y + (height - heroIcon.height()) / 2f;
				PixelScene.align(heroIcon);
			}
		}

		@Override
		public void update() {
			super.update();
			if (cl != GamesInProgress.selectedClass){
				if (!cl.isUnlocked()){
					heroIcon.brightness(0.1f);
				} else {
					heroIcon.brightness(0.6f);
				}
			} else {
				heroIcon.brightness(1f);
			}
		}

		@Override
		protected void onClick() {
			super.onClick();

			if( !cl.isUnlocked() ){
				GirlsFrontlinePixelDungeon.scene().addToFront( new WndMessage(cl.unlockMsg()));
			} else if (GamesInProgress.selectedClass == cl) {
				GirlsFrontlinePixelDungeon.scene().add(new WndHeroInfo(cl));
			} else {
				GamesInProgress.selectedClass = cl;
			}
		}
	}

	private class HeroPane extends Component {

		private HeroClass cl;

		private Image avatar;

		private IconButton heroItem;
		private IconButton heroLoadout;
		private IconButton heroMisc;
		private IconButton heroSubclass;

		private RenderedText name;

		private static final int BTN_SIZE = 17;

		@Override
		protected void createChildren() {
			super.createChildren();

			avatar = new Image(Assets.Sprites.AVATARS);
			avatar.scale.set(2.093f);
			add(avatar);

			heroItem = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					GirlsFrontlinePixelDungeon.scene().add(new WndMessage(Messages.get(cl, cl.name() + "_desc_item")));
				}
			};
			heroItem.setSize(BTN_SIZE, BTN_SIZE);
			add(heroItem);

			heroLoadout = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					GirlsFrontlinePixelDungeon.scene().add(new WndMessage(Messages.get(cl, cl.name() + "_desc_loadout")));
				}
			};
			heroLoadout.setSize(BTN_SIZE, BTN_SIZE);
			add(heroLoadout);

			heroMisc = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					GirlsFrontlinePixelDungeon.scene().add(new WndMessage(Messages.get(cl, cl.name() + "_desc_misc")));
				}
			};
			heroMisc.setSize(BTN_SIZE, BTN_SIZE);
			add(heroMisc);

			heroSubclass = new IconButton(new ItemSprite(ItemSpriteSheet.MASTERY, null)){
				@Override
				protected void onClick() {
					if (cl == null) return;
					String msg = Messages.get(cl, cl.name() + "_desc_subclasses");
					for (HeroSubClass sub : cl.subClasses()){
						msg += "\n\n" + sub.desc();
					}
					GirlsFrontlinePixelDungeon.scene().add(new WndMessage(msg));
				}
			};
			heroSubclass.setSize(BTN_SIZE, BTN_SIZE);
			add(heroSubclass);

			name = PixelScene.renderText(12);
			add(name);

			visible = false;
		}

		@Override
		protected void layout() {
			super.layout();

			avatar.x = x;
			avatar.y = y + (height - avatar.height() - name.baseLine() - 0)/2f;
			PixelScene.align(avatar);

			name.x = x + (avatar.width() - name.width())/2f;
			name.y = avatar.y + avatar.height() + -77;
			PixelScene.align(name);

			heroItem.setPos(x + width - BTN_SIZE, y);
			heroLoadout.setPos(x + width - BTN_SIZE, heroItem.bottom());
			heroMisc.setPos(x + width - BTN_SIZE, heroLoadout.bottom());
			heroSubclass.setPos(x + width - BTN_SIZE, heroMisc.bottom());
		}

		@Override
		public synchronized void update() {
			super.update();
			if (GamesInProgress.selectedClass != cl){
				cl = GamesInProgress.selectedClass;
				if (cl != null) {
					// subtract 1 for NONE class
					//avatar.frame((cl.ordinal()) * 24, 0, 24, 32);
					// 每行显示4个角色，多出来的换行
					int row = cl.ordinal() / 4;
					int col = cl.ordinal() % 4;
					avatar.frame(col * 24, row * 32, 24, 32);

					name.text(Messages.capitalize(cl.title()));

					switch(cl){
						case WARRIOR:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.SEAL, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.UMP45, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.POTION_AMBER, null));
							break;
						case MAGE:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.MAGESSTAFF, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.HOLDER, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE, null));
							break;
						case ROGUE:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.DAGGER, null));
							heroMisc.icon(Icons.get(Icons.DEPTH));
							break;
						case HUNTRESS:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.SPIRIT_BOW, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.M9, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS, null));
							break;
						case TYPE561:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.SALTYZONGZI, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.GUN561, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.REDBOOK, null));
							break;
					}

					layout();

					visible = true;
				} else {
					visible = false;
				}
			}
		}
	}

}