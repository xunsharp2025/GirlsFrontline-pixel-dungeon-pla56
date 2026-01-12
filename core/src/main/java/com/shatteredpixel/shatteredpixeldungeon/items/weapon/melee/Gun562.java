package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Gun562 extends ShootGun {
    {
        image = ItemSpriteSheet.GUN562;
        needReload=false;
        tier = 3;
        RCH = 2;
        cooldownTurns=150;
    }

    @Override
    public int min(int lvl) {
        return 3+lvl;
        //初始3成长1
    }
    @Override
    public int max(int lvl) {
        return 12+lvl*3;
        //初始12成长3
    }
    @Override
    protected int BombDamage(int lvl){
        return Random.NormalIntRange(6+lvl,24+3*lvl);
    }
    @Override
    public void onShootComplete(int cell, int lvl) {
        Hero hero=Dungeon.hero;
        super.onShootComplete(cell, lvl);
    }
}
