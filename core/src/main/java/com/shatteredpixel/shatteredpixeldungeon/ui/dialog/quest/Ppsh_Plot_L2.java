package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

public class Ppsh_Plot_L2 extends Plot {

    private final static int maxprocess = 5;

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
            switch (process) {
                default:
                case 1:
                    process_to_1();
                    break;
                case 2:
                    process_to_2();
                    break;
                case 3:
                    process_to_3();
                    break;
                case 4:
                    process_to_4();
                    break;
                case 5:
                    process_to_5();
                    break;
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
        Blacksmith.Quest.given = true;
        Blacksmith.Quest.completed = false;
        Notes.add( Notes.Landmark.TROLL );

        Pickaxe pick = new Pickaxe();
        if (pick.doPickUp( Dungeon.hero )) {
            GLog.i( Messages.get(Dungeon.hero, "you_now_have", pick.name() ));
        } else {
            Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
        }
    }

    private void process_to_1() {
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(Script.AvatarHK416(0));
        diagulewindow.setLeftName(Script.Name(Script.Character.HK416));
        diagulewindow.changeText(Messages.get(this, "dialog0"));
    }

    private void process_to_2() {
        diagulewindow.setMainAvatar(Script.NPC_Ppsh_47(1));
        diagulewindow.setLeftName(Script.Name(Script.Character.PPSH_47));
        diagulewindow.changeText(Messages.get(this, "dialog1"));
    }

    private void process_to_3() {
        diagulewindow.setMainAvatar(Script.AvatarUMP9(0));
        diagulewindow.setLeftName(Script.Name(Script.Character.UMP9));
        diagulewindow.changeText(Messages.get(this, "dialog2"));
    }

    private void process_to_4() {
        diagulewindow.setMainAvatar(Script.NPC_Ppsh_47(0));
        diagulewindow.setLeftName(Script.Name(Script.Character.PPSH_47));
        diagulewindow.changeText(Messages.get(this, "dialog3"));
    }

    private void process_to_5() {
        diagulewindow.setMainAvatar(Script.NPC_Ppsh_47(1));
        diagulewindow.setLeftName(Script.Name(Script.Character.PPSH_47));
        diagulewindow.changeText(Messages.get(this, "dialog4"));
        Blacksmith.Quest.given = true;
        Blacksmith.Quest.completed = false;
        Notes.add( Notes.Landmark.TROLL );

        Pickaxe pick = new Pickaxe();
        if (pick.doPickUp( Dungeon.hero )) {
            GLog.i( Messages.get(Dungeon.hero, "you_now_have", pick.name() ));
        } else {
            Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
        }
    }

}


