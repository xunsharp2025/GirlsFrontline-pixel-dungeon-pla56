package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.quest;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.watabou.noosa.Image;

public class M16A1_Plot_L1 extends Plot {
    {
        processCount = 5;
    }

    @Override
    public Image getImage(int process){
        switch(process){
            case 0:return Script.AvatarUMP45(1);
            case 1:return Script.NpcM16A1  (1);
            case 2:return Script.NpcM16A1  (2);
            case 3:return Script.AvatarHK416(1);
            case 4:return Script.AvatarUMP45(0);
        }
        return null;
    }

    @Override
    public String getName(int process){
        switch(process){
            case 0:return Script.Name(Script.Character.UMP45);
            case 1:return Script.Name(Script.Character.M16A1);
            case 2:return Script.Name(Script.Character.M16A1);
            case 3:return Script.Name(Script.Character.HK416);
            case 4:return Script.Name(Script.Character.UMP45);
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
        }
        return null;
    }

    public static class End extends Plot {
        {
            processCount = 2;
        }

        @Override
        public Image getImage(int process){
            switch(process){
                case 0:return Script.NpcM16A1(0);
                case 1:return Script.NpcM16A1(0);
            }
            return null;
        }

        @Override
        public String getName(int process){
            switch(process){
                case 0:return Script.Name(Script.Character.M16A1);
                case 1:return Script.Name(Script.Character.M16A1);
            }
            return null;
        }

        @Override
        public String getDialog(int process){
            switch(process){
                case 0:return Messages.get(this,"dialog" );
                case 1:return Messages.get(this,"dialog1");
            }
            return null;
        }
    }
}






