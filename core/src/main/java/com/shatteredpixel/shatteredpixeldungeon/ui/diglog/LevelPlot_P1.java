package com.shatteredpixel.shatteredpixeldungeon.ui.diglog;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

//新对话框
public class LevelPlot_P1 extends Plot {

    private final static int maxprocess = 8;

    {
        process = 1;
    }

    protected String getPlotName() {
        return SEWER_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while (this.process < needed_process) {
            this.process();
        }
    }

    @Override
    public void process() {
        if (diagulewindow != null) {
            switch (process) {
                default:
                case 1:
                    process_to_1();
                    break;
//                case 2:
//                    process_to_2();
//                    break;
//                case 3:
//                    process_to_3();
//                    break;
//                case 4:
//                    process_to_4();
//                    break;
//                case 5:
//                    process_to_5();
//                    break;
//                case 6:
//                    process_to_6();
//                    break;
//                case 7:
//                    process_to_7();
//                    break;
//                case 8:
//                    process_to_8();
//                    break;
            }
            diagulewindow.update();
            process++;
        }
    }

    @Override
    public void initial(WndDialog wndDialog) {
        diagulewindow = wndDialog;
        process = 2;
        process_to_1();
    }

    @Override
    public boolean end() {
        return process > maxprocess;
    }

    @Override
    public void skip() {
    }

    private void process_to_1() {
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(AvatarMain(1));
        diagulewindow.setLeftName("Test");
        diagulewindow.changeText("This is Dialog");
    }
    /**
     * 根据index选择头像<br>
     * 0为UMP45 1为G11
     * @param index 索引值
     * @return 返回图像
     */
    private Image AvatarMain(int index) {
        Image i;
        switch (index){
            //G11
            case 1:
                i = new Image(Assets.Sprites.AVATARS,25,0,24,32);
                break;
            //UMP45
            default:
                i = new Image(Assets.Sprites.AVATARS,0,0,24,32);
                break;
        }
        return i;
    }

//    private void process_to_2() {
//        diagulewindow.setLeftName(Messages.get(Typhon.class, "name"));
//        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message2"));
//    }
//
//    private void process_to_3() {
//        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message3"));
//    }
//
//    private void process_to_4() {
//        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message4"));
//
//    }
//
//    private void process_to_5() {
//        diagulewindow.hideAll();
//        diagulewindow.setLeftName(hero.name());
//        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message5", hero.name()));
//    }
//
//    private void process_to_6() {
//        diagulewindow.setLeftName(Messages.get(Typhon.class, "name"));
//        diagulewindow.setMainAvatar(new Image(Assets.Splashes.TYPHON));
//        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message6"));
//    }
//
//    private void process_to_7() {
//        MorphsNPC typhonn = new MorphsNPC();
//        typhonn.pos = 358;
//        GameScene.add(typhonn);
//        diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "name"));
//        diagulewindow.setMainAvatar(new Image(Assets.Splashes.MOSRDX_1));
//        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message7", hero.name()));
//    }
//
//    private void process_to_8() {
//        diagulewindow.setLeftName(Messages.get(Typhon.class, "name"));
//        diagulewindow.setMainAvatar(new Image(Assets.Splashes.TYPHON));
//        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message8", hero.name()));
//    }



}






