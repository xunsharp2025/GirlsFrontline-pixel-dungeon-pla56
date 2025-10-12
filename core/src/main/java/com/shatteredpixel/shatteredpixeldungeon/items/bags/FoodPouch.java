package com.shatteredpixel.shatteredpixeldungeon.items.bags;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class FoodPouch extends Bag {

    {
        image = ItemSpriteSheet.FOOD_POUCH;
    }

    @Override
    public boolean canHold( Item item ) {
        if (item instanceof Food || item instanceof com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat) {
            return super.canHold(item);
        }
        return false;
    }

    @Override
    public int capacity() {
        return 19;
    }

    @Override
    public int value() {
        return 30 * quantity;
    }
}
