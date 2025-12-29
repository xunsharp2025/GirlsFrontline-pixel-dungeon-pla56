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
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class ChangesWindowWithPages extends Window {

    private ArrayList<Page> pages = new ArrayList<>();
    private int currentPage = 0;
    
    private Image icon;
    private String title;
    private RenderedTextBlock contentText;
    private RenderedTextBlock titleText;
    private RenderedTextBlock pageNumberText; // 页面数字显示
    // 移除页码指示器成员变量
    
    public ChangesWindowWithPages(Image icon, String title, ArrayList<String> pageContents) {
        super();
        
        this.icon = icon;
        this.title = title;
        
        // 创建页面
        for (int i = 0; i < pageContents.size(); i++) {
            pages.add(new Page(i + 1, pageContents.get(i)));
        }
        
        // 创建标题文本
        titleText = PixelScene.renderTextBlock(title, 9);
        titleText.hardlight(Window.TITLE_COLOR);
        add(titleText);
        
        // 创建内容文本
        contentText = PixelScene.renderTextBlock(pageContents.get(0), 6);
        add(contentText);
        
        // 创建页面数字显示
        pageNumberText = PixelScene.renderTextBlock("1/" + pages.size(), 8);
        pageNumberText.hardlight(Window.TITLE_COLOR);
        add(pageNumberText);
        
        // 如果有图标，添加到窗口
        if (icon != null) {
            add(icon);
        }
        
        // 移除页面指示器创建代码
        
        // 创建翻页按钮（缩小尺寸）
        RedButton btnPrev = new RedButton("<") {
            @Override
            protected void onClick() {
                previousPage();
            }
        };
        btnPrev.setSize(10, 10); // 调整界面尺寸的位置
        add(btnPrev);
        
        RedButton btnNext = new RedButton(">") {
            @Override
            protected void onClick() {
                nextPage();
            }
        };
        btnNext.setSize(10, 10); // 缩小按钮尺寸
        add(btnNext);
        
        layout();
    }
    
    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateContent();
        }
    }
    
    private void nextPage() {
        if (currentPage < pages.size() - 1) {
            currentPage++;
            updateContent();
        }
    }
    
    private void updateContent() {
        contentText.text(pages.get(currentPage).content);
        
        // 更新页面数字显示
        pageNumberText.text((currentPage + 1) + "/" + pages.size());
        
        // 移除页面指示器更新代码
        
        layout();
    }
    
    public void onBackPressed() {
        hide(); // 使用父类的hide()方法来正确关闭窗口
    }
    
    // 不再需要@Override注解和super.layout()调用
    protected void layout() {
        // 设置内容文本最大宽度（拓宽显示范围）
        contentText.maxWidth((int)(PixelScene.uiCamera.width - 80)); // 从-150改为-80，增加文本宽度
        
        // 固定窗口大小（替代动态计算）
        float windowWidth = Math.min(200, (int)(PixelScene.uiCamera.width - 50)); // 固定最大宽度200，最小为屏幕宽度-50
        float windowHeight = Math.min(100, (int)(PixelScene.uiCamera.height - 100)); // 固定最大高度100，最小为屏幕高度-100
        
        // 调整窗口大小
        resize((int)windowWidth, (int)windowHeight);
        
        // 设置图标位置（左上角）
        if (icon != null) {
            icon.setPos(10, 10);
        }
        
        // 设置标题位置（图标右侧或居中）
        if (icon != null) {
            titleText.setPos(
                icon.x + icon.width + 10,
                10
            );
        } else {
            titleText.setPos(
                (width - titleText.width()) / 2f,
                10
            );
        }
        
        // 设置内容文本位置（从居中改为靠左）
        float contentY = icon != null ? (icon.y + icon.height) + 10 : titleText.bottom() + 15;
        contentText.setPos(
            20, // 固定左边距，不再居中
            contentY
        );
        
        // 计算底部控制区域的位置（固定居中）
        float controlY = windowHeight - 20; // 固定在底部上方20px处
        
        // 获取翻页按钮对象
        RedButton btnPrev = (RedButton)members.get(members.size() - 2);
        RedButton btnNext = (RedButton)members.get(members.size() - 1);
        
        // 计算各个底部元素的总宽度（包含页码显示和翻页按钮）
        float totalControlsWidth = btnPrev.width() + 30 + pageNumberText.width() + 30 + btnNext.width(); // 翻页按钮 + 页码 + 边距
        
        // 计算控制区域的起始X坐标（居中）
        float controlsStartX = (windowWidth - totalControlsWidth) / 2f;
        
        // 设置翻页按钮和页码文本位置（居中显示）
        btnPrev.setPos(
            controlsStartX,
            controlY
        );
        
        // 设置页码文本位置（与翻页按钮在同一水平线上）
        pageNumberText.setPos(
            btnPrev.x + btnPrev.width() + 40, // 页码显示在翻页按钮右侧
            controlY // 与翻页按钮在同一水平线上
        );
        
        btnNext.setPos(
            pageNumberText.x + pageNumberText.width() + 35, // 下一页按钮在页码右侧
            controlY
        );
    }
    
    // 页面类
    private class Page {
        int number;
        String content;
        
        public Page(int number, String content) {
            this.number = number;
            this.content = content;
        }
    }
}