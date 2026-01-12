package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;


import static com.shatteredpixel.shatteredpixeldungeon.items.Item.updateQuickslot;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class EquipLevelUp extends FlavourBuff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public float DURATION = 10f;

    @Override
    public void detach() {
        super.detach();
        updateQuickslot();
    }

    @Override
    public int icon() {
        return BuffIndicator.UPGRADE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0.2f, 0.6f, 1f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc",Dungeon.hero.hasTalent(Talent.Type56FourTwoTwo)?Dungeon.hero.pointsInTalent(Talent.Type56FourTwoTwo):1,
                1+Dungeon.hero.pointsInTalent(Talent.Type56FourTwoTwo),dispTurns());
    }
    //buff介绍要留出两个接口以接受各自提升的等级
}