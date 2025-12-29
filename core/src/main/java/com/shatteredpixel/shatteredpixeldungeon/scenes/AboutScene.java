/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Random;

public class AboutScene extends PixelScene {

	@Override
	public void create() {
		super.create();

		final float colWidth = 120;
		final float fullWidth = colWidth * (landscape() ? 2 : 1);

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		//darkens the arches
		add(new ColorBlock(w, h, 0x88000000));

		ScrollPane list = new ScrollPane( new Component() );
		add( list );

		Component content = list.content();
		content.clear();

		//*** Shattered Pixel Dungeon Credits ***

		CreditsBlock shpx = new CreditsBlock(true, 0xEB9388,
				"Girl Front Line Pixel Dungeon",
				Icons.GIRLPD.get(),
				"新少前地牢开发团队\n"+
				"少前地牢是一款_完全开源且完全免费_的\n少女前线及破碎地牢同人游戏。\n如果您从任何渠道_花费资金购买_了\n这款游戏，您已经遭遇了盗版诈骗\n请立即尝试退款以维护您的权利\nQ群：897141018",
				null,
				null);

		shpx.setSize( fullWidth, 0);
        shpx.setPos((w - fullWidth)/2f, 6);
		content.add(shpx);
        int firstLine = 76;
		addLine(firstLine, content);

		CreditsBlock alex = new CreditsBlock(false, 0xffbfa6,
				"项目发起人",
				Icons.BAKA.get(),
				"西露库",
				null,
				null);
		alex.setSize(colWidth/2f, 0);
		alex.setPos(w/2f - colWidth/2f, shpx.bottom()+10);
		content.add(alex);

		CreditsBlock charlie = new CreditsBlock(false, 0xe1e1e1,
				"文案设计",
				Icons.LANGLING.get(),
				"言凌FMN",
				null,
				null);
		charlie.setSize( colWidth/2f, 0);
        charlie.setPos(alex.right(), alex.top());
		content.add(charlie);
		addLine(firstLine+28, content);

		//*** Art Credits ***
		CreditsBlock arcnor = new CreditsBlock(true, 0xcf3227,
				"　",
				Icons.FTER.get(),
				"Fter",
				null,
				null);
		arcnor.setSize(colWidth/3f, 0);
		arcnor.setPos(alex.left(), charlie.bottom()+10);
		content.add(arcnor);

		CreditsBlock purigro = new CreditsBlock(true,0xffd2d2,
				"美  术  设  计  ",
				Icons.CHOCOSUKI.get(),
				"Chocosuki",
				null,
				null);
		purigro.setSize( colWidth/3f, 0);
        purigro.setPos(arcnor.right(), arcnor.top());
		content.add(purigro);

		CreditsBlock doge = new CreditsBlock(true,0xc79654,
				"　",
				Icons.DOGE.get(),
				"Unknown",
				null,
				null);
		doge.setSize( colWidth/3f, 0);
        doge.setPos(purigro.right(), purigro.top());
		content.add(doge);
		addLine(purigro.top()+32, content);

		//*** Pro Credits ***
		CreditsBlock wolf = new CreditsBlock(true, 0x008ac1,
				"　",
				Icons.WOLF.get(),
				"Xunsharp",
				null,
				null);
		wolf.setSize(colWidth/3f, 0);
		wolf.setPos(arcnor.left(), purigro.bottom()+10);
		content.add(wolf);

		CreditsBlock catz = new CreditsBlock(true,0xffca18,
				"程  序  编  码  ",
				Icons.CATZS.get(),
				"Cat-Zs",
				null,
				null);
		catz.setSize( colWidth/3f, 0);
        catz.setPos(wolf.right(), wolf.top());
		content.add(catz);

		CreditsBlock sea = new CreditsBlock(true,0x25273e,
		        "　",
		        Icons.SEA.get(),
		        "Sea",
				null,
		        null);
		sea.setSize(colWidth/3f, 0);
        sea.setPos(catz.right(), catz.top());
		content.add(sea);
        /*模板从此处开始*/
		CreditsBlock onw = new CreditsBlock(true,Blink(),
                /*是否闪光、闪光颜色，闪光颜色是十六进制六位数，从0x000000到0xFFFFFF*/
		        "　",
                /*标题*/
		        Icons.ONWARD.get(),
                /*贴图*/
		        "ONWARD!", null, null);
        /*名字、null、null*/
		onw.setSize(colWidth/3f, 0);
        /*宽度看情况设置，目前要加人，宽度基本上就是该行将由多少人平分colWidth
        * 高度设置为0将会去读取贴图的height，设置非0就会只计算写进去的数值，所以height保持为0，
        * 然后引用到某人的y的时候，把要添加的高度写在引用的位置，例如下面的sea.bottom()+8*/
        onw.setPos(wolf.right(), sea.bottom()+8);
        /*设上面设置的东西所在位置，left和top是自身的x、y，
        right是left+width，width为设置的宽度，
        bottom是top+height，height为设置的高度，如果高度设置为0则为贴图的高度*/
		content.add(onw);
        /*实行上面设置的那一堆，到这里就完成加人了*/
		addLine(onw.top()+32, content);
        /*这个是分割线，只需要给每一块最底下的人的y引用一次就好了，addLine多次跟一次没啥两样
        分割线的高度并没有固定值，选定了某人的y值之后，多试几次调出来一个满意的相对y值*/
        //到此结束

		//*** Update Credits ***
		CreditsBlock ling = new CreditsBlock(true, 0xffb0ca,
		        "　",
				Icons.SHOWER.get(),
				"Shower",
		        null,
		        null);
		ling.setSize(colWidth/3f, 0);
		ling.setPos(wolf.left(), onw.bottom()+15);
		content.add(ling);

		CreditsBlock shower = new CreditsBlock(true,0xb9f0fd,
				"迭  代  协  助  ",
				 Icons.LING.get(),
				"JDSALing",
		        null,
		        null);
		shower.setSize( colWidth/3f, 0);
        shower.setPos(ling.right(), ling.top());
		content.add(shower);

		CreditsBlock cola = new CreditsBlock(true,0x46020e,
		        "　",
		        Icons.COLA.get(),
		        "Cola",
		        null,
		        null);
		cola.setSize( colWidth/3f, 0);
        cola.setPos(shower.right(), shower.top());
		content.add(cola);
		addLine(cola.top()+35, content);

		CreditsBlock awsl = new CreditsBlock(true, 0x008ac1,
		        " ",
		        Icons.AWSL.get(),
		        "Awsl",
		        null,
		        null);
		awsl.setSize(colWidth/3f, 0);
		awsl.setPos(ling.left(), cola.bottom()+14);
		content.add(awsl);

        CreditsBlock onw2 = new CreditsBlock(true,Blink(),
                " 测  试  协  力 ",
                Icons.ONWARD.get(),
                "TO ME!",
                null,
                null);
        onw2.setSize(colWidth/3f, 0);
        onw2.setPos(awsl.right(), awsl.top());
        content.add(onw2);

        CreditsBlock alex2 = new CreditsBlock(true,0xffca18,
		        " ",
		        Icons.ALEX.get(),
		        "Alex",
		        null,
		        null);
		alex2.setSize( colWidth/3f, 0);
        alex2.setPos(onw2.right(), awsl.top());
		content.add(alex2);
		addLine(awsl.top()+34, content);

		//*** Music Credits ***

		CreditsBlock freesound = new CreditsBlock(true,
				Window.TITLE_COLOR,
				null,
				null,
				"_少女前线的像素地牢使用了以下歌曲作为游戏音乐_:\n\n" +

						"唱片集：Girls Frontline Original Soundtrack Vol.1 \n\n" +
						"主界面音乐\n" +
						"_-_ Horizon\n"+
						"_-_ Make Sense\n\n"+

						"第一大区音乐\n" +
						"_-_ Safety First-a\n" +
						"_-_ Safety First-b\n\n" +
						"第一大区Boss音乐\n" +
						"_-_ Made in Heaven\n\n" +

						"第三大区音乐\n" +
						"_-_ machines are talking\n\n" +
						"第四大区Boss音乐\n" +
						"_-_ Cury\n\n" +
						"第五大区Boss音乐\n" +
						"_-_ What i am fight for\n\n" +

						"唱片集：少女前线诡疫狂潮BGM \n\n" +
						"第二大区音乐\n" +
						"_-_ m-Halloween19-host\n\n" +
						"第二大区Boss音乐\n" +
						"_-_ m-Halloween19-made in heaven\n\n" +

						"唱片集：Girls Frontline Original Soundtrack Vol.2 \n\n" +
						"第三大区Boss音乐\n" +
						"_-_ cradle of fear\n\n" +
						"第六大区Boss音乐\n" +
						"_-_ mind hack\n\n" +
						"通关音乐\n" +
						"_-_ Vacance 6.64\n\n" +
						"唱片集：少女前线碧海秘闻BGM\n\n" +
						"第四大区音乐\n" +
						"_-_ Event summer combat\n\n" +
						"唱片集：Slow Shock (游戏《少女前线》活动「慢休克」原声音乐)\n\n" +
						"第五大区音乐\n" +
						"_-_ Tactical Operation\n\n" +
						"唱片集：未知\n\n" +
						"第六大区音乐\n" +
						"_-_ See you Again",
				null,
				null);
		freesound.setSize( colWidth+20, 0);
        freesound.setPos(ling.left()-10, awsl.bottom()+14);
		content.add(freesound);

		content.setSize( fullWidth, freesound.bottom()+10 );

		list.setRect( 0, 0, w, h );
		list.scrollTo(0, 0);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}

	private void addLine( float y, Group content ){
		ColorBlock line = new ColorBlock(Camera.main.width, 1, 0xFF333333);
		line.y = y;
		content.add(line);
	}
    //转十六进制
    private int change(int j){
        if(j == 0){
            return 0x000000;
        }else if( j == 1){
            return 0x000001;
        }else if( j == 2){
            return 0x000002;
        }else if( j == 3){
            return 0x000003;
        }else if( j == 4){
            return 0x000004;
        }else if( j == 5){
            return 0x000005;
        }else if( j == 6){
            return 0x000006;
        }else if( j == 7){
            return 0x000007;
        }else if( j == 8){
            return 0x000008;
        }else if( j == 9){
            return 0x000009;
        }else if( j == 10){
            return 0x00000A;
        }else if( j == 11){
            return 0x00000B;
        }else if( j == 12){
            return 0x00000C;
        }else if( j == 13){
            return 0x00000D;
        }else if( j == 14){
            return 0x00000E;
        }else{
            return 0x00000F;
        }
    }
    //颜色是一个6位的十六进制数，这里对每一位随机一个数，然后转换成十六进制后再加到一起
    private int Blink(){
        int RP = Random.Int(16);
        int GP = Random.Int(16);
        int BP = Random.Int(16);
        int RT = Random.Int(16);
        int GT = Random.Int(16);
        int BT = Random.Int(16);
        int R = change(RP)*0x010000+change(RT)*0x100000;
        int G = change(GP)*0x000100+change(GT)*0x001000;
        int B = change(BP)*0x000001+change(BT)*0x000010;
        int i =R+G+B;
        return i;
    }

	private static class CreditsBlock extends Component {

		boolean large;
		RenderedTextBlock title;
		Image avatar;
		Flare flare;
		RenderedTextBlock body;

		RenderedTextBlock link;
		ColorBlock linkUnderline;
		PointerArea linkButton;

		//many elements can be null, but body is assumed to have content.
		private CreditsBlock(boolean large, int highlight, String title, Image avatar, String body, String linkText, String linkUrl){
			super();

			this.large = large;

			if (title != null) {
				this.title = PixelScene.renderTextBlock(title,6);
				if (highlight != -1) this.title.hardlight(highlight);
				add(this.title);
			}

			if (avatar != null){
				this.avatar = avatar;
				add(this.avatar);
			}

			if (large && highlight != -1 && this.avatar != null){
				this.flare = new Flare( 7, 24 ).color( highlight, true ).show(this.avatar, 0);
				this.flare.angularSpeed = 20;
			}

			this.body = PixelScene.renderTextBlock(body, 6);
			if (highlight != -1) this.body.setHightlighting(true, highlight);
			if (large) this.body.align(RenderedTextBlock.CENTER_ALIGN);
			add(this.body);

			if (linkText != null && linkUrl != null){

				int color = 0xFFFFFFFF;
				if (highlight != -1) color = 0xFF000000 | highlight;
				this.linkUnderline = new ColorBlock(1, 1, color);
				add(this.linkUnderline);

				this.link = PixelScene.renderTextBlock(linkText, 6);
				if (highlight != -1) this.link.hardlight(highlight);
				add(this.link);

				linkButton = new PointerArea(0, 0, 0, 0){
					@Override
					protected void onClick( PointerEvent event ) {
						Game.platform.openURI( linkUrl );
					}
				};
				add(linkButton);
			}

		}

		@Override
		protected void layout() {
			super.layout();

			float topY = top();

			if (title != null){
				title.maxWidth((int)width());
				title.setPos( x + (width() - title.width())/2f, topY);
				topY += title.height() + (large ? 2 : 1);
			}

			if (large){

				if (avatar != null){
					avatar.x = x + (width()-avatar.width())/2f;
					avatar.y = topY;
					PixelScene.align(avatar);
					if (flare != null){
						flare.point(avatar.center());
					}
					topY = avatar.y + avatar.height() + 2;
				}

				body.maxWidth((int)width());
				body.setPos( x + (width() - body.width())/2f, topY);
				topY += body.height() + 2;

			} else {

				if (avatar != null){
					avatar.x = x;
					body.maxWidth((int)(width() - avatar.width - 1));

					float fullAvHeight = Math.max(avatar.height(), 16);
					if (fullAvHeight > body.height()){
						avatar.y = topY + (fullAvHeight - avatar.height())/2f;
						PixelScene.align(avatar);
						body.setPos( avatar.x + avatar.width() + 1, topY + (fullAvHeight - body.height())/2f);
						topY += fullAvHeight + 1;
					} else {
						avatar.y = topY + (body.height() - fullAvHeight)/2f;
						PixelScene.align(avatar);
						body.setPos( avatar.x + avatar.width() + 1, topY);
						topY += body.height() + 2;
					}

				} else {
					topY += 1;
					body.maxWidth((int)width());
					body.setPos( x, topY);
					topY += body.height()+2;
				}

			}

			if (link != null){
				if (large) topY += 1;
				link.maxWidth((int)width());
				link.setPos( x + (width() - link.width())/2f, topY);
				topY += link.height() + 2;

				linkButton.x = link.left()-1;
				linkButton.y = link.top()-1;
				linkButton.width = link.width()+2;
				linkButton.height = link.height()+2;

				linkUnderline.size(link.width(), PixelScene.align(0.49f));
				linkUnderline.x = link.left();
				linkUnderline.y = link.bottom()+1;

			}

			topY -= 2;

			height = Math.max(height, topY - top());
		}
	}
}
