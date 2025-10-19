/*
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
    private static final float SCALE = 3f;

    public WndSaveSlot(int slotId){
        SaveSlot saveSlotButton = new SaveSlot(slotId);
        add(saveSlotButton);
        
        resize((int)saveSlotButton.width(),(int)saveSlotButton.height());
    }

    private class SaveSlot extends Button {
        private int saveSlotId;

        public SaveSlot(int slotId) {
            saveSlotId=slotId;

            GamesInProgress.Info gameInfo=GamesInProgress.check(saveSlotId);
            if(null==gameInfo){
                gameInfo=new GamesInProgress.Info();
                gameInfo.slot=saveSlotId;
            }

            int order;
            switch (gameInfo.heroClass) {
                case WARRIOR :order = 1;break;
                case MAGE    :order = 2;break;
                case ROGUE   :order = 3;break;
                case HUNTRESS:order = 4;break;
                case TYPE561 :order = 5;break;
                default      :order = 0;break;
            }

            float bias=0.5f;//do not delete this,otherwise you will be angry.

            Image portrait = new Image(Assets.Interfaces.PORTRAIT1,(order%6)*38,(order/6)*60,38,60);
            portrait.scale.set(19f/38f*SCALE);
            portrait.x=bias+SCALE;
            portrait.y=bias+SCALE;
            add(portrait);

            Image frame    = new Image(Assets.Interfaces.SAVESLOT,0,0,21,52);
            frame.scale.set( SCALE );
            frame.x=bias;
            frame.y=bias;
            add(frame   );

            Image[] challengeMarks = new Image[Challenges.MASKS.length-1];
            for (int i=0; i<challengeMarks.length; ++i) {
                challengeMarks[i] = new Image(Assets.Interfaces.SAVESLOT, 22, 0, 3, 3);
                challengeMarks[i].scale.set(SCALE);
                challengeMarks[i].y = bias + (38 + 4*(i/5)) * SCALE;
                challengeMarks[i].x = bias + (1  + 4*(i%5)) * SCALE;
                challengeMarks[i].visible = ((gameInfo.challenges)&Challenges.MASKS[i]) != 0 ;
                add(challengeMarks[i]);
            }

            RenderedTextBlock name =PixelScene.renderTextBlock( 7 );
            name.text( gameInfo.subClass != HeroSubClass.NONE ? gameInfo.subClass.title() :gameInfo.heroClass.title() );
            name.setPos(x + 10.5f * SCALE - name.width() / 2f, y + 34.5f * SCALE - name.height() / 3f);
            add( name );
            
            RenderedTextBlock level=PixelScene.renderTextBlock( 7 );
            level.text(String.valueOf(gameInfo.level));
            level.setPos(x + 16f * SCALE - level.width() / 4f, y + 29f * SCALE - level.height() / 8f);
            add(level);
            
            RenderedTextBlock depth=PixelScene.renderTextBlock( 8 );
            depth.text(String.valueOf(gameInfo.depth));
            depth.setPos(x + 10.5f * SCALE - depth.width() / 2f, y + 47f * SCALE);
            add(depth);

            setSize(frame.width(),frame.height());
        }

        @Override
        protected void onClick() {
            boolean newGame=(null==GamesInProgress.check(saveSlotId));

            if(newGame){
                Game.scene().add(new WndStartGame(saveSlotId));
            }else{
                Game.scene().add(new WndGameInProgress(saveSlotId));
            }
        }
    }
}
*/