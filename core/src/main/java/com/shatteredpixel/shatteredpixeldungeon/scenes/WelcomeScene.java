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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHardNotification;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStartGame;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.FileUtils;

public class WelcomeScene extends PixelScene {

	private static final int LATEST_UPDATE = GirlsFrontlinePixelDungeon.v1_2_0;

	@Override
	public void create() {
		super.create();

		Badges.loadGlobal();
		Journal.loadGlobal();

		final int previousVersion = SPDSettings.version();

		if (FileUtils.cleanTempFiles()){
			add(new WndHardNotification(Icons.get(Icons.WARNING),
					Messages.get(WndError.class, "title"),
					Messages.get(this, "save_warning"),
					Messages.get(this, "continue"),
					5){
				@Override
				public void hide() {
					super.hide();
					GirlsFrontlinePixelDungeon.resetScene();
				}
			});
			return;
		}

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.THEME_1, Assets.Music.THEME_2},
				new float[]{1, 1},
				false);

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );
		float topRegion = Math.max(title.height - 6, h*0.45f);
		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;
		align(title);
		placeTorch(title.x + 22, title.y + 46);
		placeTorch(title.x + title.width - 22, title.y + 46);
		
		StyledButton okay = new StyledButton(Chrome.Type.GREY_BUTTON_TR,null){
			@Override
			protected void onClick() {
				super.onClick();
				if (previousVersion == 0 || SPDSettings.intro()){
					SPDSettings.version(GirlsFrontlinePixelDungeon.versionCode);
				} else {
					updateVersion(previousVersion);
				}
				GirlsFrontlinePixelDungeon.switchScene(TitleScene.class);
			}
		};
		float buttonY = Math.min(topRegion + (PixelScene.landscape() ? 60 : 120), h - 24);
		okay.text(Messages.get(this,"enter"));
		okay.setRect(title.x, buttonY, title.width(), 20);
		okay.icon(Icons.get(Icons.ENTER));
		add(okay);

		RenderedTextBlock text = PixelScene.renderTextBlock(6);
		String message;
		if(GirlsFrontlinePixelDungeon.versionCode == previousVersion && !SPDSettings.intro()){
			message = Messages.get(this, "coutinue_msg");
		}else if (previousVersion == 0 || SPDSettings.intro()) {
			message = Messages.get(this, "welcome_msg");
		} else if (previousVersion <= GirlsFrontlinePixelDungeon.versionCode) {
			message = Messages.get(this, "update_msg");
		} else {
			message = Messages.get(this, "what_msg");
		}
		text.text(message, Math.min(w-20, 300));
		float textSpace = okay.top() - topRegion - 4;
		text.setPos((w - text.width()) / 2f, (topRegion + 2) + (textSpace - text.height())/2);
		add(text);
	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private void updateVersion(int previousVersion){

		//update rankings, to update any data which may be outdated
		if (previousVersion < LATEST_UPDATE){
			try {
				Rankings.INSTANCE.load();
				for (Rankings.Record rec : Rankings.INSTANCE.records.toArray(new Rankings.Record[0])){
					try {
						Rankings.INSTANCE.loadGameData(rec);
						Rankings.INSTANCE.saveGameData(rec);
					} catch (Exception e) {
						//if we encounter a fatal per-record error, then clear that record
						Rankings.INSTANCE.records.remove(rec);
						GirlsFrontlinePixelDungeon.reportException(e);
					}
				}
				Rankings.INSTANCE.save();
			} catch (Exception e) {
				//if we encounter a fatal error, then just clear the rankings
				FileUtils.deleteFile( Rankings.RANKINGS_FILE );
				GirlsFrontlinePixelDungeon.reportException(e);
			}

		}

		//if the player has beaten Goo, automatically give all guidebook pages
		Badges.loadGlobal();
		if (Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)){
			for (String page : Document.ADVENTURERS_GUIDE.pageNames()){
				Document.ADVENTURERS_GUIDE.readPage(page);
			}
		}
	}
}
