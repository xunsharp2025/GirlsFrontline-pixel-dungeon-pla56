package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.ErrorButton;
import com.watabou.noosa.Game;
import java.io.IOException;

//temporary
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import java.util.ArrayList;

//temporary
import static com.shatteredpixel.shatteredpixeldungeon.Chrome.Type.GREY_BUTTON;
import static com.shatteredpixel.shatteredpixeldungeon.Chrome.Type.GREY_BUTTON_TR;
import static com.shatteredpixel.shatteredpixeldungeon.Chrome.Type.TOAST_TR;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.version;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.update.GDChangesButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSettings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStartGame;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndGameInProgress;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.ColorMath;
import com.watabou.utils.DeviceCompat;

public class TitleScene extends PixelScene {
	@Override
	public void create() {
		super.create();

		Music.INSTANCE.play(Assets.Music.THEME_1,true);

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

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				if (time >= 1.5f*Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );

		StyledButton btnPlay = new StyledButton(GREY_BUTTON,"进入游戏"){
			@Override
			protected void onClick() {
				if (GamesInProgress.checkAll().isEmpty()){
					TitleScene.this.add( new WndStartGame(1) );
				} else {
					TitleScene.this.add( new WndSelectGameInProgress() );
				}
			}
		};
		btnPlay.icon(Icons.get(Icons.ENTER));
		add(btnPlay);

		StyledButton btnZeroLevel=null;
		if (Badges.isUnlocked(Badges.Badge.HAPPY_END) || DeviceCompat.isDebug()){
			btnZeroLevel = new StyledButton(GREY_BUTTON,"返回地表"){
				@Override
				protected void onClick() {
					enterMainGame();
				}
			};
		}else{
			btnZeroLevel = new StyledButton(GREY_BUTTON_TR,"返回地表(未解锁)");
		}
		btnZeroLevel.icon(Icons.get(Icons.ENTER));
		add(btnZeroLevel);

		StyledButton btnRankings = new StyledButton(GREY_BUTTON,"排行榜"){
			@Override
			protected void onClick() {
				GirlsFrontlinePixelDungeon.switchNoFade( RankingsScene.class );
			}
		};
		btnRankings.icon(Icons.get(Icons.RANKINGS));
		add(btnRankings);

		StyledButton btnBadges = new StyledButton(GREY_BUTTON,"徽章"){
			@Override
			protected void onClick() {
				GirlsFrontlinePixelDungeon.switchNoFade( BadgesScene.class );
			}
		};
		btnBadges.icon(Icons.get(Icons.BADGES));
		add(btnBadges);

		StyledButton btnSettings = new SettingsButton(GREY_BUTTON,"设置");
		add(btnSettings);

		StyledButton btnAbout = new StyledButton(GREY_BUTTON,"关于"){
			@Override
			protected void onClick() {
				GirlsFrontlinePixelDungeon.switchNoFade( AboutScene.class );
			}
		};
		Image xs = Icons.get(Icons.GIRLPDS);
		xs.scale.set(PixelScene.align(0.6f));
		btnAbout.icon(xs);
		add(btnAbout);

		StyledButton btnChanges = new GDChangesButton(TOAST_TR,"更改");
		btnChanges.icon(new Image(Icons.get(Icons.CHANGESLOG)));
		btnChanges.setRect(0, h - 20, 50, 20);
		add(btnChanges);

		final int BTN_HEIGHT = 20;
		final int GAP = 2;

		if (landscape()) {
			btnPlay.setRect(title.x - 50, topRegion + GAP, title.width() + 100 - 1, BTN_HEIGHT);
			align(btnPlay);
			btnZeroLevel.setRect(btnPlay.left(),btnPlay.bottom()+GAP,btnPlay.width()  ,BTN_HEIGHT);
			btnRankings.setRect(btnZeroLevel.left()     ,btnZeroLevel.bottom()+GAP     ,btnZeroLevel.width()/2f    ,BTN_HEIGHT);
			btnBadges  .setRect(btnRankings.left() ,btnRankings.bottom()+GAP ,btnZeroLevel.width()/2f    ,BTN_HEIGHT);
			btnSettings.setRect(btnBadges.right()+2,btnRankings.top()        ,btnZeroLevel.width()/2f-GAP,BTN_HEIGHT);
			btnAbout   .setRect(btnSettings.left() ,btnSettings.bottom()+GAP ,btnZeroLevel.width()/2f-GAP,BTN_HEIGHT);
		} else {
			btnPlay.setRect(title.x, topRegion+GAP, title.width(), BTN_HEIGHT);
			align(btnPlay);
			btnZeroLevel.setRect(btnPlay.left(),btnPlay.bottom()+GAP,btnPlay.width()  ,BTN_HEIGHT);
			btnRankings.setRect(btnZeroLevel.left(),btnZeroLevel    .bottom()+GAP,btnZeroLevel.width(),BTN_HEIGHT);
			btnBadges  .setRect(btnZeroLevel.left(),btnRankings.bottom()+GAP,btnZeroLevel.width(),BTN_HEIGHT);
			btnSettings.setRect(btnZeroLevel.left(),btnBadges  .bottom()+GAP,btnZeroLevel.width(),BTN_HEIGHT);
			btnAbout   .setRect(btnZeroLevel.left(),btnSettings.bottom()+GAP,btnZeroLevel.width(),BTN_HEIGHT);
		}

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width() - 4;
		version.y = h - version.height() - 2;
		add( version );

        ErrorButton btnError = new ErrorButton();
        btnError.setPos( w-25, version.y-20 );
        add(btnError);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - 25, 0);
        add(btnExit);

		fadeIn();
	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private static class SettingsButton extends StyledButton {

		public SettingsButton( Chrome.Type type, String label ){
			super(type, label);
			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				icon(Icons.get(Icons.LANGS));
				icon.hardlight(1.5f, 0, 0);
			} else {
				icon(Icons.get(Icons.SETTINGS));
			}
		}

		@Override
		public void update() {
			super.update();

			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				textColor(ColorMath.interpolate( 0xFFFFFF, CharSprite.NEGATIVE, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				WndSettings.last_index = 4;
			}
			GirlsFrontlinePixelDungeon.scene().add(new WndSettings());
		}
	}

	private static void enterMainGame(){
		Dungeon.hero=null;
		ActionIndicator.action  = null;
		GamesInProgress.curSlot = 0;
		GamesInProgress.selectedClass=HeroClass.TYPE561;
		boolean	newGame = null == GamesInProgress.check(GamesInProgress.curSlot);
		if(newGame){
			InterlevelScene.start();
		}else{
			if(version<=643){
            //643版本在生成器中添加了生成后又移除，导致在添加生成的情况下进入过的存档，在移除生成之后会崩档。
            //由于只在内测版本出现这种情况，所以不对正常存档做改动，正常存档崩了就删档吧
            //这里对643及以前版本生成的返回地表存档进行删档以处理崩档情况。
			//因为地表的存档还可能会有错误所以我先加回来。
            Dungeon.deleteGame(GamesInProgress.curSlot, true);
			}else {
            try{InterlevelScene.restore();}
            catch(IOException e){Game.reportException(e);}
			}
		}
		Game.switchScene(GameScene.class);
	}

	@Override
	protected void onBackPressed() {
		//Do nothing
	}

	//Temporary
	public static class WndSelectGameInProgress extends Window {

		public static float SLOT_SCALE = 105 / 45f;

		public static int DISPWIDTH;
		public static int DISPHEIGHT;

		private ArrayList<SaveSlot> Slots = new ArrayList<>(GamesInProgress.MAX_SLOTS);

		protected static final int MARGIN = SPDSettings.landscape() ? 7:5;

		protected static Point SlotsToDisplay;
		protected static ScrollPane squad;


		public WndSelectGameInProgress(){
			Badges.loadGlobal();
			Journal.loadGlobal();

			SlotsToDisplay = SPDSettings.landscape()? new Point( 5, 1)  : new Point( 2, 2 );

			ArrayList<GamesInProgress.Info> games = GamesInProgress.checkAll();

			squad = new ScrollPane( new Component() ) {
				@Override
				public void onClick( float x, float y ) {
					for ( SaveSlot slot : Slots ) {
						if (slot.inside( x, y )) {
							slot.onClick();
						}
					}
				}
			};

			Component content = squad.content();

			int slotNumber = 0;

			for (GamesInProgress.Info info : games) {
				SaveSlot newSlot = new SaveSlot( info ) {
					@Override
					public void onClick() {
						GirlsFrontlinePixelDungeon.scene().add( new WndGameInProgress( this.slot ) );
					}
				};
				Slots.add( newSlot );
				content.add( newSlot );
				newSlot.setPos( 5 + (newSlot.width() + MARGIN) * ((slotNumber) % SlotsToDisplay.x),
						MARGIN + (newSlot.height() + MARGIN) * ((slotNumber) / SlotsToDisplay.x) );
				++slotNumber;
			}

			final int newSlotIndex = GamesInProgress.firstEmpty();

			if (newSlotIndex > 0) {
				SaveSlot newSlot = new SaveSlot( new GamesInProgress.Info() ) {
					@Override
					public void onClick() {
						GirlsFrontlinePixelDungeon.scene().add( new WndStartGame( newSlotIndex ) );
					}
				};
				Slots.add( newSlot );
				content.add( newSlot );
				newSlot.setPos( 5 + (Slots.get(0).width() + MARGIN) * ((games.size()) % SlotsToDisplay.x),
						MARGIN + (newSlot.height() + MARGIN) * ((games.size()) / SlotsToDisplay.x) );
			}

			add(squad);

			DISPWIDTH = SlotsToDisplay.x * (int)Slots.get(0).width() + (SlotsToDisplay.x + 1) * MARGIN;
			DISPHEIGHT = SlotsToDisplay.y * (int)Slots.get(0).height() + (SlotsToDisplay.y + 1) * MARGIN;
			resize(DISPWIDTH, DISPHEIGHT);



			Point TotalSlots = SPDSettings.landscape() ? new Point( 5, 2 ) : new Point( 2, 5 );
			int REALWIDTH = (int)Slots.get(0).width() * TotalSlots.x + MARGIN * (TotalSlots.x+1);
			int REALHEIGHT = (int)Slots.get(0).height() * TotalSlots.y  + MARGIN * (TotalSlots.y + 1);
			content.setRect(0, 0, REALWIDTH, REALHEIGHT);

			squad.setSize( DISPWIDTH, DISPHEIGHT );
			squad.scrollTo(0, 0);
		}

		private static class SaveSlot extends Component {
			protected Image portrait;
			protected Image frame;

			protected Image[] challengeMarks;
			protected Emitter[] depthEmmiters;

			private GamesInProgress.Info Info;

			public static float SCALE = SLOT_SCALE;

			protected RenderedTextBlock name;
			protected RenderedTextBlock level;
			protected RenderedTextBlock score;
			protected RenderedTextBlock depth;

			private HeroClass cls;

			public int slot;

			public SaveSlot( GamesInProgress.Info info ) {

				Info = info;
				cls = Info.heroClass;
				slot = info.slot;

				int order;

				switch (cls) {
					case WARRIOR:
						order = 1;
						break;
					case MAGE:
						order = 2;
						break;
					case ROGUE:
						order = 3;
						break;
					case HUNTRESS:
						order = 4;
						break;
					case TYPE561:
						order = 5;
						break;
					case GSH18:
						order = 6;
						break;
					default:
						order = 0;
						break;
				}

				portrait = new Image(Assets.Interfaces.PORTRAIT1, (order % 7) * 38, (order / 7) * 60, 38, 60);
				frame = new Image(Assets.Interfaces.SAVESLOT, 0, 0, 21, 52);

				setRect(0, 0, frame.width * SCALE, frame.height * SCALE);


			}
			private Image testModeMark;
			@Override
			protected void createChildren() {
				super.createChildren();

				challengeMarks = new Image[Challenges.MASKS.length];
				depthEmmiters = new Emitter[6];

				// 创建test_mode专用挑战标记
				testModeMark = new Image(Assets.Interfaces.SAVESLOT, 22, 4, 3, 3);

				for (int i=0; i<10; ++i) {
					challengeMarks[i] = new Image(Assets.Interfaces.SAVESLOT, 22, 0, 3, 3);
				}
				for (int i=0; i<6; ++i) {
					depthEmmiters[i] = new Emitter();
				}

				name =  PixelScene.renderTextBlock( 7 );
				level = PixelScene.renderTextBlock( 7 );
				score = PixelScene.renderTextBlock( 8 );
				depth = PixelScene.renderTextBlock( 7 );
			}

			@Override
			protected void layout() {
				super.layout();

				// 设置frame的缩放比例
				frame.scale.set( SCALE );

				// 设置portrait的缩放比例
				portrait.scale.set( 8.0f / 17.0f * SCALE );

				// 2 는 프레임 좌측 상단에서 margin 넣는거, x는 변할 일 없는데 y는 높낮이 조절용으로 좀 건드릴듯
				// 하드코딩이니 나중에 한 곳에 다 몰아넣고 조절하도록 바꾸자
				//TODO Refactor as variable
				portrait.x = x + 2 * SCALE;
				portrait.y = y + 2 * SCALE;

				add( portrait );

				frame.x = x;
				frame.y = y;

				add( frame );


				add( name );

				if (Info.heroClass == HeroClass.NONE) {
					name.text( Info.heroClass.title() );
				} else {
					name.text( Info.subClass != HeroSubClass.NONE ? Info.subClass.title() :Info.heroClass.title() );
				}


				name.setPos(x + 10.5f * SCALE - name.width() / 2f, y + 34.5f * SCALE - name.height() / 3f);
				name.alpha(3.0f);

				//add(depth);
				//depth.text(String.valueOf(Info.depth));
				//depth.x = x + 15f * SCALE - depth.width() / 4f;
				//depth.y = y + 2.5f * SCALE - depth.height() / 3f;

				add(level);
				level.text(String.valueOf(Info.level));

				level.setPos(x + 16f * SCALE - level.width() / 4f, y + 29f * SCALE - level.height() / 8f);

				add(score);
				score.text(String.valueOf(Info.depth));

				score.setPos(x + 10.5f * SCALE - score.width() / 2f, y + 47f * SCALE);

				// 添加普通挑战标记
				for (int i = 0; i < 10; ++i) {
					add(challengeMarks[i]);

					challengeMarks[i].scale.set(SCALE);

					challengeMarks[i].y = y + (38 + 4 *(i / 5)) * SCALE;
					challengeMarks[i].x = x + (1 + 4 * (i % 5)) * SCALE;

					challengeMarks[i].visible = false;
				}

				// 添加并设置test_mode挑战标记
				add(testModeMark);
				testModeMark.scale.set(SCALE);
				// 定位在第一个挑战标记的位置上
				testModeMark.y = y + (38 + 4 * (0 / 5)) * SCALE;
				testModeMark.x = x + (1 + 4 * (0 % 5)) * SCALE;
				// 检查是否开启test_mode，只有开启时才显示
				testModeMark.visible = (Info.challenges & Challenges.TEST_MODE) != 0;

				for (int i = 0; i < Challenges.MASKS.length; ++i) {
					if(!(Challenges.NAME_IDS[i].equals("test_mode"))){
						challengeMarks[ i ].visible = ((Info.challenges) & Challenges.MASKS[i]) != 0 ;
					}
				}

				int procTheme = Math.min( (Info.maxDepth-1)/5 + 1, 6 );

				for (int i = 0; i < procTheme; ++i) {
					add(depthEmmiters[i]);
					depthEmmiters[i].pos( x + (11f + (i+1) * 8f/(procTheme+1)) * SCALE, y + 2.5f * SCALE);
					depthEmmiters[i].fillTarget = false;
					depthEmmiters[i].pour(Speck.factory( Speck.FORGE ), 0.6f);

				}

			}

			protected void onClick() {

			}
		}
	}
}