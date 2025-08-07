package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.quest;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

public class Ppsh_Plot_Misc extends Plot {
    private final static int maxprocess = 1;

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
            if (process == 1) {
                process_to_1();
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
        diagulewindow.setMainAvatar(Script.NPC_Ppsh_47(0));
        diagulewindow.setLeftName(Script.Name(Script.Character.PPSH_47));
        diagulewindow.changeText(Messages.get(this,"dialog"));
    }

    public static class Kill extends Ppsh_Plot_Misc { }
    public static class Gold extends Ppsh_Plot_Misc { }
    public static class L1 extends Ppsh_Plot_Misc { }
    public static class L2 extends Ppsh_Plot_Misc { }
}
