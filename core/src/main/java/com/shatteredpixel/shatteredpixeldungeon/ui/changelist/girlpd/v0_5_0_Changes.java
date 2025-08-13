package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.girlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DemonSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NemeumSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RipperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpawnerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TyphoonSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_5_0_Changes {
    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_Changes(changeInfos);
    }
    public static void add_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.0", true, "");
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        Image xs = Icons.get(Icons.GIRLPDS);
        xs.scale.set(PixelScene.align(0.5f));
        changes.addButton( new ChangeButton(xs, "新版少前地牢制作组",
                "_-_ 2025新版少前地牢是基于破碎1.2.3底层进行开发\n" +
                        "_-_ 这是一个全新的开始，我们会在未来带来更多内容\n" +
                        "_-_ 感谢所有支持我们的指挥官，祝各位新版游玩愉快！"));

        Image ks = new Image(new DM300Sprite());
        ks.scale.set(PixelScene.align(0.65f));
        changes.addButton( new ChangeButton(ks, "Boss调整",
                "受益于新版本破碎底层的支持，以下Boss都为破碎新版后的状态\n\n" +
                        "_-_ 衔尾蛇-仍为两个阶段，但作战将比老版本更加困难\n" +
                        "_-_ 计量官-变为三个阶段，而不是之前的大号怪物\n" +
                        "_-_ 破坏者-变为三个阶段，而不是之前的大号怪物\n" +
                        "_-_ 主脑 伊莱莎-变为五个阶段，而不是之前的大号怪物"));

        changes.addButton( new ChangeButton(Icons.get(Icons.LIBGDX), "LibGDX引擎系统",
                "_-_ 游戏的文本渲染器现已采用libGDX Freetype。这与现有文本几乎完全相同，但略微更加清晰，且具有平台独立性，效率也大幅提升!\n\n" +
                        "_-_ 文字渲染是最后一段依赖于Android的代码，因此游戏的核心代码模块（约占其代码的98%）现在正在作为通用代码进行编译，而非Android专属代码！\n\n" +
                        "_-_ 基于LibGDX引擎，现在游戏将更加流畅，并更容易兼容高版本设备系统"));


        changes.addButton( new ChangeButton(Icons.get(Icons.WARNING), "错误日志系统",
                "老版本一遇到崩溃，游戏就闪退了？\n\n" +
                        "不用担心，新版本我们实装了自己的错误界面。\n\n" +
                        "如游玩时遭遇游戏崩溃，请将错误报告复制后@管理员进行发送。"));

        changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "音乐系统实装",
                "老版本音乐千篇一律，打起来枯燥无味？\n\n" +
                        "不用担心，新版本制作组精选了多首少前音乐，让你的游戏更加有乐趣\n\n" +
                        "P.S. : 这样会导致安装包有一点大。\n\n" +
                        "另外，如果你喜欢某首音乐，可在关于界面找到具体的歌曲名字哦。"));

        changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON), "新挑战：深入敌腹",
                "感觉Boss太简单，想挑战更高难度？\n\n" +
                        "深入敌腹会满足你的需求，所有的Boss都会被加强\n" +
                        "你将会遭遇更加困难的作战，祝你好运！\n\n" +
                        "此挑战在破碎原形为：绝命头目"));

        changes.addButton( new ChangeButton(new Image(Assets.Sprites.SNAKE, 0, 0, 16, 16), "新敌人：靶机",
                "第一大区追加了新敌人：靶机。\n\n" +
                        "其破碎怪物原形为：下水道巨蛇"));

        Image kst = new Image(new SpawnerSprite());
        kst.scale.set(PixelScene.align(0.40f));
        changes.addButton( new ChangeButton(kst, "新敌人：侦查中枢",
                "第六大区追加了新敌人：侦查中枢。\n\n" +
                        "其破碎怪物原形为：恶魔血巢"));

        Image kt = new Image(new RipperSprite());
        kt.scale.set(PixelScene.align(0.70f));
        changes.addButton( new ChangeButton(kt, "新敌人：尖兵",
                "第六大区追加了新敌人：尖兵。\n\n" +
                        "其破碎怪物原形为：恶魔撕裂者\n\n" +
                        "它通过_侦查中枢_来生成。"));

        Image gun561 = new Image(HeroSprite.avatar(HeroClass.TYPE561, 5));
        gun561.scale.set(0.75f);
        changes.addButton( new ChangeButton(gun561, "新角色：56-1式",
                ""));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GUN561, null), "新初始武器：56-1式",
                "“56-1式突击步枪，前来报到！我会坚决消灭每一个敌人！" +
                        "\n\n以AK47为基础仿制的突击步枪，解决了一定的远距离作战的问题。" +
                        "\n\n_需要手动装填枪榴弹，但填充后自身近战伤害大幅度降低，在发射完后，需要很长一段回合冷却。_"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight( CharSprite.WARNING );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(Assets.Sprites.RAT, 0, 0, 16, 15), "敌人生成",
                "众所周知，之前的老版本怪组生成极其不合理。\n\n" +
                        "制作组在新版本调整了5区，6区的怪组生成，以尽量缓解每位指挥官的作战压力\n\n" +
                        "祝各位能在新版本找到新的感觉，新的思路，新的玩法。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.UMP45, null), "UMP-45",
                "UMP45初始防御从0调整为1，成长不变。\n\n" +
                        "并且在描述中追加具体吸收伤害数值。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        Image tp = new Image(new TyphoonSprite());
        tp.scale.set(PixelScene.align(0.20f));
        changes.addButton( new ChangeButton(tp, "AI优化",
                "现在 _提丰/钢狮/九头蛇/木星_ 这四个敌人不会将蓄力激光攒着" +
                        "\n\n瞄准一个区域后必定释放(也就是和破碎原版大眼一致的AI)"));

    }
}
