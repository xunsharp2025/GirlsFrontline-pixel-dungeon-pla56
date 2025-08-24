package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;

public class WndSaveSlot extends Window {
    public static float SLOT_SCALE = 3f;

    public WndSaveSlot(int slotId){
        SaveSlot saveSlotButton = new SaveSlot(slotId);
        saveSlotButton.setPos(0,0);
        PixelScene.align(saveSlotButton);
        add(saveSlotButton);

        resize((int)saveSlotButton.width(),(int)saveSlotButton.height());
    }

    private static class SaveSlot extends Button {

        protected Image portrait;
        protected Image frame;

        protected Image[] challengeMarks;
        protected Emitter[] depthEmmiters;

        private GamesInProgress.Info gameInfo;

        public static float SCALE = SLOT_SCALE;

        protected RenderedTextBlock name;
        protected RenderedTextBlock level;
        protected RenderedTextBlock score;
        protected RenderedTextBlock depth;

        public SaveSlot(int slotId) {
        	gameInfo=GamesInProgress.check(slotId);
	        if(null==gameInfo){
				gameInfo=new GamesInProgress.Info();
				gameInfo.slot=slotId;
	        }

            int order;
            switch (gameInfo.heroClass) {
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
                default:
                    order = 0;
                    break;
            }

            portrait = new Image(Assets.PORTRAIT1, (order % 6) * 38, (order / 6) * 60, 38, 60);
            frame = new Image(Assets.SAVESLOT, 0, 0, 21, 52);

            setRect(0, 0, frame.width * SCALE, frame.height * SCALE);
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            challengeMarks = new Image[Challenges.MASKS.length];
            depthEmmiters = new Emitter[6];

            for (int i=0; i<10; ++i) {
                challengeMarks[i] = new Image(Assets.SAVESLOT, 22, 2, 3, 3);
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

            //TODO Refactor as variable
            portrait.x = x + 2 * SCALE;
            portrait.y = y + 2 * SCALE;

            add( portrait );

            frame.x = x;
            frame.y = y;

            add( frame );


            add( name );

            if (gameInfo.heroClass == HeroClass.NONE) {
                name.text( gameInfo.heroClass.title() );
            } else {
                name.text( gameInfo.subClass != HeroSubClass.NONE ? gameInfo.subClass.title() :gameInfo.heroClass.title() );
            }


            name.setPos(x + 10.5f * SCALE - name.width() / 2f, y + 34.5f * SCALE - name.height() / 3f);
            name.alpha(3.0f);

            add(level);
            level.text(String.valueOf(gameInfo.level));

            level.setPos(x + 16f * SCALE - level.width() / 4f, y + 29f * SCALE - level.height() / 8f);

            add(score);
            score.text(String.valueOf(gameInfo.depth));

            score.setPos(x + 10.5f * SCALE - score.width() / 2f, y + 47f * SCALE);

            for (int i = 0; i < 10; ++i) {
                add(challengeMarks[i]);

                challengeMarks[i].scale.set(SCALE);

                challengeMarks[i].y = y + (38 + 4 *(i / 5)) * SCALE;
                challengeMarks[i].x = x + (1 + 4 * (i % 5)) * SCALE;

                challengeMarks[i].visible = false;
            }

            for (int i = 0; i < Challenges.MASKS.length; ++i) {
                if(!(Challenges.NAME_IDS[i].equals("test_mode"))){
                    challengeMarks[ i ].visible = ((gameInfo.challenges) & Challenges.MASKS[i]) != 0 ;
                }
            }

            int procTheme = Math.min( (gameInfo.maxDepth-1)/5 + 1, 6 );

            for (int i = 0; i < procTheme; ++i) {
                add(depthEmmiters[i]);
                depthEmmiters[i].pos( x + (11f + (i+1) * 8f/(procTheme+1)) * SCALE, y + 2.5f * SCALE);
                depthEmmiters[i].fillTarget = false;
                depthEmmiters[i].pour(Speck.factory( Speck.FORGE ), 0.6f);

            }

        }

        @Override
        protected void onClick() {
        	int slotId=gameInfo.slot;
        	boolean newGame=(null==GamesInProgress.check(slotId));

        	if(newGame){
        		Game.scene().add(new WndStartGame(slotId));
        	}else{
        		Game.scene().add(new WndGameInProgress(slotId));
        	}
        }
    }
}