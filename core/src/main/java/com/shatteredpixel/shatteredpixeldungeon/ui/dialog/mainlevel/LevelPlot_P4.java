package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.mainlevel;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.watabou.noosa.Image;

public class LevelPlot_P4 extends Plot {
    {
        processCount = 10;
    }

    @Override
    public void process(){
        if (diagulewindow != null) {
            if     (7==process){diagulewindow.setMainColor(0x0);}
            else if(8==process){diagulewindow.setMainResetColor();}
            diagulewindow.setMainAvatar(getImage (process));
            diagulewindow.setLeftName  (getName  (process));
            diagulewindow.changeText   (getDialog(process));
            diagulewindow.update();
            process++;
        }
    }

    @Override
    public Image getImage(int process){
        switch(process){
            case 0:return Script.AvatarHK416  (2);
            case 1:return Script.AvatarUMP45  (2);
            case 2:return Script.AvatarUMP9   (2);
            case 3:return Script.AvatarG11    (1);
            case 4:return Script.AvatarHK416  (1);
            case 5:return Script.AvatarUMP9   (0);
            case 6:return Script.AvatarUMP45  (0);
            case 7:return Script.BossDestroyer(2);
            case 8:return Script.AvatarUMP45  (2);
            case 9:return Script.AvatarUMP9   (0);
        }
        return null;
    }

    @Override
    public String getName(int process){
        switch(process){
            case 0:return Script.Name(Script.Character.HK416);
            case 1:return Script.Name(Script.Character.UMP45);
            case 2:return Script.Name(Script.Character.UMP9 );
            case 3:return Script.Name(Script.Character.G11  );
            case 4:return Script.Name(Script.Character.HK416);
            case 5:return Script.Name(Script.Character.UMP9 );
            case 6:return Script.Name(Script.Character.UMP45);
            case 7:return "? ? ?"                            ;//special
            case 8:return Script.Name(Script.Character.UMP45);
            case 9:return Script.Name(Script.Character.UMP9 );
        }
        return null;
    }

    @Override
    public String getDialog(int process){
        switch(process){
            case 0:return Messages.get(this,"dialog0");
            case 1:return Messages.get(this,"dialog1");
            case 2:return Messages.get(this,"dialog2");
            case 3:return Messages.get(this,"dialog3");
            case 4:return Messages.get(this,"dialog4");
            case 5:return Messages.get(this,"dialog5");
            case 6:return Messages.get(this,"dialog6");
            case 7:return Messages.get(this,"dialog7");
            case 8:return Messages.get(this,"dialog8");
            case 9:return Messages.get(this,"dialog9");
        }
        return null;
    }
}




