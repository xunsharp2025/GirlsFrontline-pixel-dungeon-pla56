package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.quest;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.watabou.noosa.Image;

public class Ppsh_Plot_Misc extends Plot {
    {
        processCount = 1;
    }

    @Override
    public Image getImage(int process){
        switch(process){
            case 0:return Script.NpcPpsh47(0);
        }
        return null;
    }

    @Override
    public String getName(int process){
        switch(process){
            case 0:return Script.Name(Script.Character.PPSH_47);
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

    public static class Kill extends Ppsh_Plot_Misc { }
    public static class Gold extends Ppsh_Plot_Misc { }
    public static class L1 extends Ppsh_Plot_Misc { }
    public static class L2 extends Ppsh_Plot_Misc { }
}
