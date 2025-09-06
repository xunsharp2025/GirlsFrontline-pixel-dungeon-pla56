package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.mainlevel;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.watabou.noosa.Image;

//新对话框
public class LevelPlot_P1 extends Plot {
    {
        processCount = 8;
    }

    @Override
    public Image getImage(int process){
        switch(process){
            case 0:return Script.AvatarUMP45(0);
            case 1:return Script.AvatarUMP9 (1);
            case 2:return Script.AvatarUMP45(2);
            case 3:return Script.AvatarHK416(2);
            case 4:return Script.AvatarUMP45(2);
            case 5:return Script.AvatarG11  (1);
            case 6:return Script.AvatarHK416(1);
            case 7:return Script.AvatarG11  (2);
        }
        return null;
    }

    @Override
    public String getName(int process){
        switch(process){
            case 0:return Script.Name(Script.Character.UMP45);
            case 1:return Script.Name(Script.Character.UMP9 );
            case 2:return Script.Name(Script.Character.UMP45);
            case 3:return Script.Name(Script.Character.HK416);
            case 4:return Script.Name(Script.Character.UMP45);
            case 5:return Script.Name(Script.Character.G11  );
            case 6:return Script.Name(Script.Character.HK416);
            case 7:return Script.Name(Script.Character.G11  );
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
        }
        return null;
    }
}






