package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.mainlevel;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.watabou.noosa.Image;

public class LevelPlot_P5 extends Plot {
    {
        processCount = 10;
    }

    @Override
    public Image getImage(int process){
        switch(process){
            case 0:return Script.BossDestroyer(1);
            case 1:return Script.BossDreamer  (2);
            case 2:return Script.BossDestroyer(1);
            case 3:return Script.BossDreamer  (1);
            case 4:return Script.BossDestroyer(2);
            case 5:return Script.BossDreamer  (1);
            case 6:return Script.BossDreamer  (0);
            case 7:return Script.BossDestroyer(0);
            case 8:return Script.AvatarG11    (1);
            case 9:return Script.AvatarUMP45  (0);
        }
        return null;
    }

    @Override
    public String getName(int process){
        switch(process){
            case 0:return Script.Name(Script.Character.DESTROYER);
            case 1:return Script.Name(Script.Character.DREAMER  );
            case 2:return Script.Name(Script.Character.DESTROYER);
            case 3:return Script.Name(Script.Character.DREAMER  );
            case 4:return Script.Name(Script.Character.DESTROYER);
            case 5:return Script.Name(Script.Character.DREAMER  );
            case 6:return Script.Name(Script.Character.DREAMER  );
            case 7:return Script.Name(Script.Character.DESTROYER);
            case 8:return Script.Name(Script.Character.G11      );
            case 9:return Script.Name(Script.Character.UMP45    );
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





