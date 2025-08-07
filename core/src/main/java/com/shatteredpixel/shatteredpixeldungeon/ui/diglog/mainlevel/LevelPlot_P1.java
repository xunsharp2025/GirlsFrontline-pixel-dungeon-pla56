package com.shatteredpixel.shatteredpixeldungeon.ui.diglog.mainlevel;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

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
                case 2:
                    process_to_2();
                    break;
                case 3:
                    process_to_3();
                    break;
                case 4:
                    process_to_4();
                    break;
                case 5:
                    process_to_5();
                    break;
                case 6:
                    process_to_6();
                    break;
                case 7:
                    process_to_7();
                    break;
                case 8:
                    process_to_8();
                    break;
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
        diagulewindow.cancel();
        WndDialog.settedPlot = null;
    }

    private void process_to_1() {
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(Script.AvatarUMP45(0));
        diagulewindow.setLeftName(Script.Name(Script.Character.UMP45));
        diagulewindow.changeText(Messages.get(this,"dialog0"));
    }

    private void process_to_2() {
        diagulewindow.setMainAvatar(Script.AvatarUMP9(1));
        diagulewindow.setLeftName(Script.Name(Script.Character.UMP9));
        diagulewindow.changeText(Messages.get(this,"dialog1"));
    }

    private void process_to_3() {
        diagulewindow.setMainAvatar(Script.AvatarUMP45(2));
        diagulewindow.setLeftName(Script.Name(Script.Character.UMP45));
        diagulewindow.changeText(Messages.get(this,"dialog2"));
    }

    private void process_to_4() {
        diagulewindow.setMainAvatar(Script.AvatarHK416(2));
        diagulewindow.setLeftName(Script.Name(Script.Character.HK416));
        diagulewindow.changeText(Messages.get(this,"dialog3"));
    }

    private void process_to_5() {
        diagulewindow.setMainAvatar(Script.AvatarUMP45(2));
        diagulewindow.setLeftName(Script.Name(Script.Character.UMP45));
        diagulewindow.changeText(Messages.get(this,"dialog4"));
    }

    private void process_to_6() {
        diagulewindow.setMainAvatar(Script.AvatarG11(1));
        diagulewindow.setLeftName(Script.Name(Script.Character.G11));
        diagulewindow.changeText(Messages.get(this,"dialog5"));
    }

    private void process_to_7() {
        diagulewindow.setMainAvatar(Script.AvatarHK416(1));
        diagulewindow.setLeftName(Script.Name(Script.Character.HK416));
        diagulewindow.changeText(Messages.get(this,"dialog6"));
    }

    private void process_to_8() {
        diagulewindow.setMainAvatar(Script.AvatarG11(2));
        diagulewindow.setLeftName(Script.Name(Script.Character.G11));
        diagulewindow.changeText(Messages.get(this,"dialog7"));
    }
}






