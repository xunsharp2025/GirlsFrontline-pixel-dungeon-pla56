/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CyclopsSprite;
import com.watabou.utils.Random;

public class Cyclops extends Mob {

    {
        spriteClass = CyclopsSprite.class;

        HP = HT = 120;
        EXP = 17;
        defenseSkill = 60;
        baseSpeed = 1f;
        maxLvl = 36;

        properties.add(Property.ARMO);

        loot = Generator.Category.POTION;
        lootChance = 0.5f;
    }

    private boolean enraged = false;

    @Override
    public int damageRoll() {
        return enraged ?
                Random.NormalIntRange( 40, 70 ) :
                Random.NormalIntRange( 30, 60 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 60;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    @Override
    public void damage( int dmg, Object src ) {
        super.damage( dmg, src );

        if (isAlive() && !enraged && HP < HT / 4) {
            enraged = true;
            spend( TICK );
            if (Dungeon.level.heroFOV[pos]) {
                sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "enraged") );
            }
        }
    }
    @Override
    public Item createLoot(){

        if (Random.Int(3) < 1 && Random.Int(6) >= Dungeon.LimitedDrops.CYCLOPS_HP.count ){
            //照搬的术士Warlock
            Dungeon.LimitedDrops.CYCLOPS_HP.count++;
            return new PotionOfHealing();
        } else {
            Item i = Generator.random(Generator.Category.POTION);
            int healingTried = 0;
            while (i instanceof PotionOfHealing){
                healingTried++;
                i = Generator.random(Generator.Category.POTION);
            }
            //进行随机药水生成，但是从中去除治疗并计数

            //将前面随机药水随机到后去除的治疗归还到随机池子
            if (healingTried > 0){
                for (int j = 0; j < Generator.Category.POTION.classes.length; j++){
                    if (Generator.Category.POTION.classes[j] == PotionOfHealing.class){
                        Generator.Category.POTION.probs[j] += healingTried;
                    }
                }
            }

            return i;
        }

    }

}
