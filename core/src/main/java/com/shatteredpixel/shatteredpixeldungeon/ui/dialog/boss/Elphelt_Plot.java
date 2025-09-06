package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.boss;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.watabou.noosa.Image;

public class Elphelt_Plot extends Plot {
    {
        processCount = 1;
    }

    @Override
    public Image getImage(int process){
        switch(process){
            case 0:return Script.BossElphelt(0);
        }
        return null;
    }

    @Override
    public String getName(int process){
        switch(process){
            case 0:return Script.Name(Script.Character.ELPHELT);
        }
        return null;
    }

    @Override
    public String getDialog(int process){
        switch(process){
            case 0:return Messages.get(this,"dialog");
        }
        return null;
    }

    public static class End extends Elphelt_Plot{
        {
            processCount = 3;
        }

        @Override
        public Image getImage(int process){
            switch(process){
                case 0:return Script.BossElphelt(1);
                case 1:return Script.AvatarUMP45(1);
                case 2:return Script.BossElphelt(2);
            }
            return null;
        }

        @Override
        public String getName(int process){
            switch(process){
                case 0:return Script.Name(Script.Character.ELPHELT);
                case 1:return Script.Name(Script.Character.UMP45);
                case 2:return Script.Name(Script.Character.ELPHELT);
            }
            return null;
        }

        @Override
        public String getDialog(int process){
            switch(process){
                case 0:return Messages.get(this,"dialog0");
                case 1:return Messages.get(this,"dialog1");
                case 2:return Messages.get(this,"dialog2");
            }
            return null;
        }
    }
}