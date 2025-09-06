package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Choice;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.ChoiceButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.SkipIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.GameAction;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class WndDialog extends Window {
    private static final int BUTTON_HEIGHT	= 18;

    private static final float AVATAR_SCALE=2f;

    private Image mainAvatar;
    private Image secondAvatar;
    public Image thirdAvatar;

    private RenderedTextBlock leftname;
    public RenderedTextBlock rightname;

    private RenderedTextBlock text;

    public RedButton dialogButton;

    private String script;

    public Plot settedPlot = null;

    public PointerArea area = null;

    public SkipIndicator skip = null;

    public boolean readed;
    private final int color = 0xFF2F2F2F;

    public int nowAt;

    private boolean haveChoice = false;
    private Choice storedChoice[];

    private float timeLeft = 0f;
    private int times = 0;

    private Callback onCloseCallBack;

    public WndDialog(Plot plot){
        this(plot,null);
    }

    public WndDialog(Plot plot,Callback onCloseCallBack) {
        super(0, 0, Chrome.get(Chrome.Type.DIALOG));

        resize(PixelScene.uiCamera.width, PixelScene.uiCamera.height);

        settedPlot = plot;
        this.onCloseCallBack=onCloseCallBack;

        boolean landscape = SPDSettings.landscape();

        int fontSize = landscape ? 13 : 10;
        int textSize = landscape ? 10 : 8;

        int chromeHeight = PixelScene.uiCamera.height / 5;
        int chromeWidth = PixelScene.uiCamera.width;

        shadow.am = 0;

        chrome.size(chromeWidth, chromeHeight);
        chrome.x = 0;
        chrome.y = PixelScene.uiCamera.height-chromeHeight;
        add(chrome);

        mainAvatar = Script.AvatarUMP45(0);
        mainAvatar.scale.set(AVATAR_SCALE);
        mainAvatar.x = 10;
        mainAvatar.y = chrome.y - mainAvatar.height();
        add(mainAvatar);

        secondAvatar = Script.AvatarUMP45(0);
        secondAvatar.x = PixelScene.uiCamera.width - secondAvatar.width();
        add(secondAvatar);

        thirdAvatar = Script.AvatarUMP45(0);
        thirdAvatar.x = secondAvatar.x - 16;
        add(thirdAvatar);

        float height = mainAvatar.height;
        secondAvatar.y = thirdAvatar.y = chrome.y - height;

        leftname = PixelScene.renderTextBlock(Script.Name(Script.Character.UMP45), fontSize);
        leftname.hardlight(0x2B7BB9);

        rightname = PixelScene.renderTextBlock(Script.Name(Script.Character.UMP45), fontSize);

        leftname.setPos(mainAvatar.x + mainAvatar.width(), chrome.y-fontSize);
        rightname.setPos(thirdAvatar.x - rightname.width(), thirdAvatar.y + thirdAvatar.height() - rightname.height() - 2);

        add(leftname);
        add(rightname);

        text = PixelScene.renderTextBlock("", textSize);
        text.maxWidth(PixelScene.uiCamera.width - textSize);
        text.setPos(5,chrome.y+5);
        add(text);

        area = makeArea();
        add(area);

        skip = makeSkip();
        add(skip);

        settedPlot.initial(this);
    }

    public PointerArea makeArea() {
        return new PointerArea(0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height) {
            @Override
            protected void onClick(PointerEvent event) {
                if (readed) {
                    if (settedPlot.end()) {
                        hide();
                    } else {
                        timeLeft = 0.02f;
                        times = 0;
                        readed = false;
                        settedPlot.process();
                    }
                } else {
                    skipWait();
                }
            }

            @Override
            public GameAction keyAction() {
                return SPDAction.WAIT;
            }

            @Override
            protected void onClick() {
                if (readed) {
                    if (settedPlot != null) {
                        if (settedPlot.end()) {
                            hide();
                        } else {
                            timeLeft = 0.02f;
                            times = 0;
                            readed = false;
                            settedPlot.process();
                        }
                    }
                } else {
                    skipWait();
                }
            }
        };
    }

    public void removeArea() {
        area.destroy();
        area.killAndErase();
        update();
    }

    public SkipIndicator makeSkip() {
        SkipIndicator skipIndicator = new SkipIndicator();
        skipIndicator.setPos(width - skipIndicator.width() - 5, 40);
        return skipIndicator;
    }

    private void removeSkip() {
        skip.destroy();
        skip.killAndErase();
        update();
    }

    public void setLeftName(String n) {
        if (leftname != null) {
            leftname.text(n);
            leftname.visible = true;
            rightname.visible = false;
        }
    }

    public void setDialogButton(String n) {
        if (dialogButton != null) {
            dialogButton.text(n);
            dialogButton.visible = true;
        }
    }

    public void setRightName(String n) {
        if (rightname != null) {
            rightname.text(n);
            rightname.visible = true;
            leftname.visible = false;

            if (thirdAvatar.visible) {
                rightname.setPos(thirdAvatar.x - rightname.width(), mainAvatar.y + thirdAvatar.height() - rightname.height() - 2);
            } else {
                rightname.setPos(secondAvatar.x - rightname.width(), mainAvatar.y + secondAvatar.height() - rightname.height() - 2);
            }
        }
    }

    public void setMainAvatar(Image mainAvatar) {
        mainAvatar.scale.set(AVATAR_SCALE);
        this.mainAvatar.copy(mainAvatar);
        this.mainAvatar.visible = true;
    }

    public void setMainColor(int alpha) {
        this.mainAvatar.color(alpha);
    }

    public void setMainResetColor() {
        this.mainAvatar.resetColor();
    }

    public void setSecondAvatar(Image secondAvatar) {
        this.secondAvatar.copy(secondAvatar);
        this.secondAvatar.visible = true;
    }

    public void setSecondColor(int alpha) {
        this.secondAvatar.color(alpha);
    }

    public void setSecondResetColor() {
        this.secondAvatar.resetColor();
    }

    public void setThirdAvatar(Image thirdAvatar) {
        this.thirdAvatar.copy(thirdAvatar);
        this.thirdAvatar.visible = true;
    }

    public void secondAvatarToFront() {
        bringToFront(secondAvatar);
    }

    public void thirdAvatarToFront() {
        bringToFront(thirdAvatar);
    }

    public void darkenMainAvatar() {
        mainAvatar.visible = true;
        mainAvatar.tint(0x000000, 0.5f);
    }

    public void darkenSecondAvatar() {
        secondAvatar.visible = true;
        secondAvatar.tint(0x000000, 0.5f);
    }

    public void darkenThirdAvatar() {
        thirdAvatar.visible = true;
        thirdAvatar.tint(0x000000, 0.5f);
    }

    public void lightenMainAvatar() {
        mainAvatar.visible = true;
        mainAvatar.resetColor();
    }

    public void lightenSecondAvatar() {
        secondAvatar.visible = true;
        secondAvatar.resetColor();
    }

    public void lightenThirdAvatar() {
        thirdAvatar.visible = true;
        thirdAvatar.resetColor();
    }

    public void hideMainAvatar() {
        mainAvatar.visible = false;
    }

    public void hideSkip() {
        skip.visible = false;
        skip.active = false;
    }

    public void hideSecondAvatar() {
        secondAvatar.visible = false;
    }

    public void hideThirdAvatar() {
        thirdAvatar.visible = false;
    }

    public void changeText(String txt) {
        script = txt;
        resetBlock();
    }

    public void resetBlock() {
        // 重置文本块的方法
    }

    public void showBackground(String txt) {
        if (mainAvatar.visible) {
            darkenMainAvatar();
        }

        if (secondAvatar.visible) {
            darkenSecondAvatar();
        }

        if (thirdAvatar.visible) {
            darkenThirdAvatar();
        }

        leftname.visible = false;
        rightname.visible = false;
        changeText(txt);
    }

    public void hideAll(){
        hideMainAvatar();
        hideSecondAvatar();
        hideThirdAvatar();
    }

    public void hide(){
        super.hide();

        if(null!=onCloseCallBack){
            onCloseCallBack.call();
        }
    }

    public void haveChoice(Choice... existing) {
        storedChoice = existing;
        haveChoice = true;
    }

    public void showChoice(Choice... existing) {
        resizeBeforeChoice();

        haveChoice = false;

        int width = PixelScene.uiCamera.width;

        float pos;
        ArrayList<ChoiceButton> buttons = new ArrayList<ChoiceButton>();

        boolean landscape = SPDSettings.landscape();

        if (landscape) {
            pos = GameScene.StatusHeight();
        } else {
            int half = existing.length / 2;
            float offset = (PixelScene.uiCamera.height - GameScene.ToolbarHeight() - GameScene.StatusHeight()) / 2;
            pos = offset - half * BUTTON_HEIGHT - (half - 1) * 1f - (existing.length % 2 == 0 ? 0.5f : BUTTON_HEIGHT / 2f);
        }

        for (int i = 0; i < existing.length; i++) {
            final int finalI = i;
            ChoiceButton btn = new ChoiceButton(existing[i].text, 6) {
                @Override
                protected void onClick() {
                    resizeAfterChoice();
                    existing[finalI].react();
                    for (ChoiceButton button : buttons) {
                        button.destroy();
                        button.killAndErase();
                    }
                }
            };
            buttons.add(btn);
            btn.setRect(0, pos, width, BUTTON_HEIGHT);
            btn.layout();
            add(btn);
            pos += 1f+BUTTON_HEIGHT;
        }
    }

    public void resizeBeforeChoice() {
        removeArea();
        removeSkip();
    }

    public void resizeAfterChoice() {
        area = makeArea();
        if (this != null) add(area);

        skip = makeSkip();
        if (this != null) add(skip);
    }

    @Override
    public void update() {
        if (!readed) {
            if ((timeLeft -= Game.elapsed) <= 0) {
                timeLeft = 0.02f;
                if (times++ < script.length()) {
                    text.text(script.substring(0, times), text.maxWidth());
                } else {
                    readed = true;
                    if (haveChoice) {
                        haveChoice = false;
                        showChoice(storedChoice);
                    }
                }
            }
        }
    }

    public void skipWait() {
        times = script.length();
        text.text(script.substring(0, times), text.maxWidth());
        readed = true;
        if (haveChoice) {
            haveChoice = false;
            showChoice(storedChoice);
        }
    }

    public void skipText() {
        hide();
    }
}
