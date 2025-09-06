package com.shatteredpixel.shatteredpixeldungeon.utils.diglog;

import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public abstract class Plot{
    public int process;
    public int processCount;

    public WndDialog diagulewindow;

    public void initial(WndDialog wndDialog){
        diagulewindow=wndDialog;

        diagulewindow.hideAll();

        diagulewindow.setMainAvatar(getImage (0));
        diagulewindow.setLeftName  (getName  (0));
        diagulewindow.changeText   (getDialog(0));
        process=1;
    }

    public boolean end(){
        return process>=processCount;
    }

    public void process(){
        if (diagulewindow != null) {
            diagulewindow.setMainAvatar(getImage (process));
            diagulewindow.setLeftName  (getName  (process));
            diagulewindow.changeText   (getDialog(process));
            diagulewindow.update();
            process++;
        }
    }

    public abstract Image getImage(int process);
    public abstract String getName(int process);
    public abstract String getDialog(int process);
}
