package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.quest;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.watabou.noosa.Image;

public class P7_Plot_L1 extends Plot {
    {
        processCount = 5;
    }

    @Override
    public Image getImage(int process){
        switch(process){
            case 0:return Script.NpcP7    (1);
            case 1:return Script.AvatarUMP9(0);
            case 2:return Script.NpcP7    (0);
            case 3:return Script.NpcP7    (1);
            case 4:return Script.NpcP7    (0);
        }
        return null;
    }

    @Override
    public String getName(int process){
        switch(process){
            case 0:return Script.Name(Script.Character.P7  );
            case 1:return Script.Name(Script.Character.UMP9);
            case 2:return Script.Name(Script.Character.P7  );
            case 3:return Script.Name(Script.Character.P7  );
            case 4:return Script.Name(Script.Character.P7  );
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
            processCount = 1;
        }

        @Override
        public Image getImage(int process){
            switch(process){
                case 0:return Script.NpcP7(0);
            }
            return null;
        }

        @Override
        public String getName(int process){
            switch(process){
                case 0:return Script.Name(Script.Character.P7);
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
    }
}






