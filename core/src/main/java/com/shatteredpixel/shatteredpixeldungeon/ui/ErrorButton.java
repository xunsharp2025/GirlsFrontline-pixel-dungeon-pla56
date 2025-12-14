package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AboutScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CrashReportScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;
import com.watabou.input.GameAction;
import com.watabou.noosa.Game;

public class ErrorButton extends IconButton {

    public ErrorButton() {
        super(Icons.WARNING.get());

        width = 20;
        height = 20;
    }

    @Override
    protected void onClick() {
        GirlsFrontlinePixelDungeon.switchNoFade( CrashReportScene.class );
    }

    @Override
    protected String hoverText() {
        return Messages.titleCase(Messages.get(WndKeyBindings.class, "error"));
    }
}

