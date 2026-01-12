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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class ChangesWindowWithPages extends ChangesWindow {
    public ChangesWindowWithPages(Image iconA, String title, ArrayList<String> pageContents, int page) {
        super(iconA, title, pageContents.get(page));
        //给窗口增加高度以放置按钮而不遮挡原来的文本
        height += 10;
        resize(width, height);
        RenderedTextBlock pageNumberText; // 页面数字显示
        // 创建页面数字显示
        pageNumberText = PixelScene.renderTextBlock(page + 1 + "/" + pageContents.size(), 8);
        pageNumberText.hardlight(Window.TITLE_COLOR);
        // 设置页码文本位置（与翻页按钮在同一水平线上）
        pageNumberText.setPos(width / 2, // 页码显示在翻页按钮右侧
                height - 10 // 与翻页按钮在同一水平线上
        );
        add(pageNumberText);

        // 创建翻页按钮（缩小尺寸）
        RedButton btnPrev = new RedButton("<") {
            @Override
            protected void onClick() {
                if (page > 0) {
                    onBackPressed();
                    GirlsFrontlinePixelDungeon.scene().add(new ChangesWindowWithPages(new Image(iconA), title, pageContents, page - 1));
                }
            }
        };
        btnPrev.setSize(10, 10); // 调整界面尺寸的位置
        // 设置翻页按钮和页码文本位置（居中显示）
        btnPrev.setPos(0, height - 10);
        add(btnPrev);

        RedButton btnNext = new RedButton(">") {
            @Override
            protected void onClick() {
                if (page < pageContents.size() - 1) {
                    onBackPressed();
                    GirlsFrontlinePixelDungeon.scene().add(new ChangesWindowWithPages(new Image(iconA), title, pageContents, page + 1));
                }
            }
        };
        btnNext.setSize(10, 10); // 缩小按钮尺寸
        btnNext.setPos(width - 10, // 下一页按钮在页码右侧
                height - 10);
        add(btnNext);
    }

}