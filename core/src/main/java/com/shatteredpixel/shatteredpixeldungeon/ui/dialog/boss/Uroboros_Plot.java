package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.boss;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.watabou.noosa.Image;

public class Uroboros_Plot extends Plot {
    {
        processCount = 1;
    }

    @Override
    public Image getImage(int process){
        switch(process){
            case 0:return Script.BossUroboros();
        }
        return null;
    }

    @Override
    public String getName(int process){
        switch(process){
            case 0:return Script.Name(Script.Character.UROBOROS);
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

