package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DMR;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
public class AN94 extends DesignatedMarksmanRifle {
    {
        image = ItemSpriteSheet.AN94;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.5f;
        image = ItemSpriteSheet.AN94; 
        hitSound = Assets.Sounds.HIT_CRUSH; 
        hitSoundPitch = 0.5f; 

        tier = 4;
        RCH = 3;
        
    }

    @Override
    public int min(int lvl) {
        return 4 + // 基础伤害为4
                lvl;   // 每级成长1
    }

    @Override
    public int max(int lvl) {  
        return 25 + 
                lvl * 4; 
    }
}