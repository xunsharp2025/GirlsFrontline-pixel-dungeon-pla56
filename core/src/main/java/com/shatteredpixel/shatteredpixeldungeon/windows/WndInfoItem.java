/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.WndTextInput;
import com.watabou.noosa.Game;

public class WndInfoItem extends Window {
	
	private static final float GAP	= 2;

	private static final int WIDTH_MIN = 120;
	private static final int WIDTH_MAX = 220;
    protected IconButton noteButton;

	//only one WndInfoItem can appear at a time
	private static WndInfoItem INSTANCE;

	public WndInfoItem( Heap heap ) {

		super();

		if (INSTANCE != null){
			INSTANCE.hide();
		}
		INSTANCE = this;

		if (heap.type == Heap.Type.HEAP) {
			fillFields( heap.peek() );

		} else {
			fillFields( heap );

		}
	}
	
	public WndInfoItem( Item item ) {
		super();

		if (INSTANCE != null){
			INSTANCE.hide();
		}
		INSTANCE = this;
		
		fillFields( item );
	}

	@Override
	public void hide() {
		super.hide();
		if (INSTANCE == this){
			INSTANCE = null;
		}
	}

	private void fillFields(Heap heap ) {
		
		IconTitle titlebar = new IconTitle( heap );
		titlebar.color( TITLE_COLOR );
		
		RenderedTextBlock txtInfo = PixelScene.renderTextBlock( heap.info(), 6 );

        if(heap.type == Heap.Type.FOR_SALE) {
            noteButton = Tradenote(heap);
            noteButton.enable(heap.peek().canNote);
            noteButton.visible = heap.peek().canNote;
            layoutFields(titlebar, txtInfo, noteButton);
        }
        else
            layoutFields(titlebar, txtInfo);
	}
    private void layoutFields(IconTitle title, RenderedTextBlock info){
        int width = WIDTH_MIN;

        info.maxWidth(width);

        //window can go out of the screen on landscape, so widen it as appropriate
        while (PixelScene.landscape()
                && info.height() > 100
                && width < WIDTH_MAX){
            width += 20;
            info.maxWidth(width);
        }

        title.setRect( 0, 0, width, 0 );
        add( title );

        info.setPos(title.left(), title.bottom() + GAP);
        add( info );

        resize( width, (int)(info.bottom() + 2) );
    }
	private void fillFields( Item item ) {
		
		int color = TITLE_COLOR;
		if (item.levelKnown && item.level() > 0) {
			color = ItemSlot.UPGRADED;
		} else if (item.levelKnown && item.level() < 0) {
			color = ItemSlot.DEGRADED;
		}

		IconTitle titlebar = new IconTitle( item );
		titlebar.color( color );
		
		RenderedTextBlock txtInfo = PixelScene.renderTextBlock( item.info(), 6 );
        noteButton=Itemnote(item);
        noteButton.enable(item.canNote);
        noteButton.visible=item.canNote;
		
		layoutFields(titlebar, txtInfo, noteButton);
	}

	private void layoutFields(IconTitle title, RenderedTextBlock info, IconButton noteButton){
		int width = WIDTH_MIN;

		info.maxWidth(width);

		//window can go out of the screen on landscape, so widen it as appropriate
		while (PixelScene.landscape()
				&& info.height() > 100
				&& width < WIDTH_MAX){
			width += 20;
			info.maxWidth(width);
		}

		title.setRect( 0, 0, width, 0 );
		add( title );

		info.setPos(title.left(), title.bottom() + GAP);
		add( info );

        noteButton.setRect(width-16,0,16,16);
        add(noteButton);

		resize( width, (int)(info.bottom() + 2) );
	}
    protected IconButton Itemnote(Item item){
        return new IconButton(Icons.RENAME_ON.get()){
            @Override
            protected void onClick() {
                super.onClick();
                String note =Item.ClassNoteToItem(item);
                String noteAdd="";
                if(item.stackable){
                    if(item instanceof Scroll||item instanceof Potion){
                        noteAdd=Messages.get(Item.class, "noteclassb");
                    }else {
                        noteAdd=Messages.get(Item.class, "noteclassa");
                    }
                }
                GirlsFrontlinePixelDungeon.scene().addToFront(
                        new WndTextInput(
                                item.name(),
                                Messages.get(Item.class, "note_desc",noteAdd,note),
                                note,
                                40,
                                false,
                                Messages.get(Item.class, "set_note_yes"),
                                Messages.get(Item.class, "set_note_no")
                        ){
                            @Override
                            public void onSelect(boolean check, String text) {
                                if(check){
                                    item.notedSet(text);
                                    hide();
                                    Game.scene().add(new WndInfoItem(item));
                                }
                            }
                        }
                );
            }
        };
    }
    protected IconButton Tradenote(Heap heap){
        Item item=heap.peek();
        return new IconButton(Icons.RENAME_ON.get()){
            @Override
            protected void onClick() {
                super.onClick();
                String note =Item.ClassNoteToItem(item);
                String noteAdd="";
                if(item.stackable){
                    if(item instanceof Scroll||item instanceof Potion){
                        noteAdd=Messages.get(Item.class, "noteclassb");
                    }else {
                        noteAdd=Messages.get(Item.class, "noteclassa");
                    }
                }
                GirlsFrontlinePixelDungeon.scene().addToFront(
                        new WndTextInput(
                                item.name(),
                                Messages.get(Item.class, "note_desc",noteAdd,note),
                                note,
                                40,
                                false,
                                Messages.get(Item.class, "set_note_yes"),
                                Messages.get(Item.class, "set_note_no")
                        ){
                            @Override
                            public void onSelect(boolean check, String text) {
                                if(check){
                                    item.notedSet(text);
                                    hide();
                                    Game.scene().add(new WndInfoItem(heap));
                                }
                            }
                        }
                );
            }
        };
    }
}
