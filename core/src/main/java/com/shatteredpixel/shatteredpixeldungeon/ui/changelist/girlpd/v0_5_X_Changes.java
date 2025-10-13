package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.girlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RipperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpawnerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpinnerCatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TyphoonSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_5_X_Changes {
    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
    	add_0_5_4_x_Changes(changeInfos);
    	add_0_5_4_Changes(changeInfos);
    	add_0_5_3_Changes(changeInfos);
		add_0_5_2_Changes(changeInfos);
		add_0_5_1_Changes(changeInfos);
		add_0_5_0_Changes(changeInfos);
    }

    public static void add_0_5_4_x_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.4", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

    changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);
        
        Image imageTemp = new Image(Assets.Sprites.TROLL, 0 ,0 ,12 ,21);
        imageTemp.scale.x=0.8f;
        imageTemp.scale.y=0.8f;
        changes.addButton(new ChangeButton(imageTemp, "优化游戏体验", 
            "_-_ 修复了点击排行榜导致存档损坏的BUG。\n"+
			"_-_ 修复了AR15和M16A1对话文本错乱的BUG。\n"+
			"_-_ 修复了移动端击败艾尔菲尔特可能出现崩溃的BUG。\n"
        ));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LAR, null), "角色调整",
            "_-_ 简单增强了灰熊\n"
        ));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.NTW20, null), "武器增强",
            "_-_ 增强了Ntw20\n"
        ));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FOOD_POUCH, null), "功能优化",
            "_-_ 增加了食物包，优化pc端背包按钮显示\n"+
            "_-_ 优化了文本显示\n"
        ));
    }    


    public static void add_0_5_4_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.4", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GSH18,null), "新增游戏内容",
			"_-_ 在测试模式加入了_地块编辑器_，虽然有些繁琐，但玩家可以自定义地形了。\n"+
			"_-_ 将_【少女前线X罪恶装备/苍翼默示录】_相关联动内容作为彩蛋加入游戏，玩家在开启深入敌腹挑战后进行闯关即可体验！\n"+
			"_-_ 增加了角色饱食度指示UI，现在角色的饱食度数值可视化了。\n"+
			"_-_ FNC带着格里芬的补给来到了地牢！与其对话将能获取一些有趣的补给！\n"+
			"_-_ 新增了一份节日彩蛋！\n"+
			"_-_ 全新武器_GSh-18_加入。\n"+
			"_-_ 为_灵刀·樱吹雪_添加了新技能，使其不再只是一把‘刀’\n"
		));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);
        
        Image imageTemp = new Image(Assets.Sprites.TROLL, 0 ,0 ,12 ,21);
        imageTemp.scale.x=0.8f;
        imageTemp.scale.y=0.8f;
        changes.addButton(new ChangeButton(imageTemp, "优化游戏体验",
			"_-_ 修复了部分情况下新建游戏会闪退的BUG。\n"+
			"_-_ 优化了_泛黄的袖珍本_，增强了其与其它遗物的相关性。\n"+
			"_-_ 修复了第6大区_远处的井_贴图错误的BUG。\n"+
			"_-_ 因为一些特殊目的，删除了_塌方陷阱_。\n"+
			"_-_ 强化了_Type 56-2_武器榴弹的伤害。\n"+
			"_-_ 修正了大量文本错误，对部分文本进行了优化。\n"
        ));
    }    


    public static void add_0_5_3_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.5.3", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GUN562, null), "游戏改动",
			"_-_ 字体渲染：现已可使用系统字体和像素字体。\n"+
			"_-_ 主界面图标调整\n"+
			"_-_ 调整了宝箱怪的贴图，现在更容易辨识了\n"+
			"_-_ 新功能：在线更新系统\n"+
			"_-_ 减小了56-2榴弹的爆炸和伤害范围\n"+
			"_-_ 调整了56-1和56-2的弹道逻辑\n"+
			"_-_ 调整了56-1快捷栏的CD显示：现在更加明了了\n"+
			"_-_ 修改了\"咸派的认可\"天赋：提高了其后期收益\n"+
			"_-_ 增强了鉴定符石，现在不需要猜对也能使用两次\n"+
			"_-_ 现在炼化物品时能顺便将其鉴定了\n"+
			"_-_ 修改了矮人层怪组，加入了蚁群，强度更合理了\n"+
			"_-_ 将坠落音效和死亡音效换成原来的\n"+
			"_-_ 修改了军方小队boss层地形，防止\"空间信标\"返回进去"
		));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
			"_-_ 更换了第三boss层遗漏的地块贴图\n"+
			"_-_ 修复了56-2在楼层封锁时CD显示不正确的问题\n"+
			"_-_ 恢复G11的初始伤害为1-8\n"+
			"_-_ 修复了楼层封锁时56-1的CD不受限制的问题\n"+
			"_-_ 更正了56-1的介绍文本\n"+
			"_-_ 补充了被猎鸥远程杀死时缺少的文本\n"+
			"_-_ 修复了眼镜(先见护符)相关的文本问题\n"+
			"_-_ 修复了\"不动如山\"的文本问题(现在不再是小南娘了^-^)\n"+
			"_-_ 修复了徽章\"全能大师\"的文本问题\n"+
			"_-_ 修复了\"多面手\"与\"全面手\"的文本问题\n"+
			"_-_ 修复了徽章文本问题:“戒指研究员”-->“瞄具研究员”,“瞄具研究员”-->“药水研究员”\n"+
			"_-_ 修复了魔法免疫时使用卷轴的文本问题\n"+
			"_-_ 修复了虚弱buff的文本问题\n"+
			"_-_ 修复了HK416两个绒布袋的问题\n"+
			"_-_ 修复了HK416浆果的文本问题\n"+
			"_-_ 修复了\"斥候\"的动量在角色死亡后没有清除的bug\n"+
			"_-_ 修改了矮人城地块贴图，现在不会混淆能种草的地和不能种草的地了\n"+
			"_-_ 修复了木星和钢狮的粒子效果在某些情况下不能被正常消除的问题\n"+
			"_-_ 修改了矮人城草地贴图不对的问题\n"+
			"_-_ 补充、修改了元素风暴的文本\n"+
			"_-_ 修复了军方小队boss层两个梯子的问题"
		));
	};

    public static void add_0_5_2_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.5.2", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
		"_-_ 现在鼠王护甲能正常使用了，561也可以使用鼠王护甲\n"+
				"_-_ 修改了Ppsh-41的贴图\n"+
				"_-_ 更改了侦查中枢的显示偏移，现在视觉上不会阻挡其他怪物了\n"+
				"_-_ 修复了G11 虹卫，镜像 攻击崩溃的bug\n"+
				"_-_ 修复了骷髅文本错误问题\n"+
				"_-_ 修复了Vector冲锋枪buff未生效的问题\n"+
				"_-_ 修复了元素风暴不能使用的问题\n"+
				"_-_ 修复了法杖(子弹)鉴定时的文本\n"+
				"_-_ 修复了部分文本问题\n"+
				"_-_ 删除了G11，将其功能合并到magesstaff，修复了”法杖回收“天赋不能正常发挥作用的问题\n"+
				"_-_ 调整了坠落音效的音量大小为原来的2/3\n"+
				"_-_ 修复了G11的装弹强化天赋不能正常触发的bug，同时调小了死亡音效的音量\n"+
				"_-_ 修复了561饭是钢天赋不能正常触发的问题\n"+
				"_-_ 现在鼠王护甲能正常使用了，561也可以使用鼠王护甲了\n"+
				"_-_ 修复了护卫者无限掉落血瓶的问题，现在上限为5"
		));
	};

    public static void add_0_5_1_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.5.1", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(BadgeBanner.image(Badges.Badge.KILL_CALC.image), "游戏优化",
			"现在只需要击败15层计量官Boss，即可解锁挑战。"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		Image tp = new Image(new SpinnerCatSprite());
		tp.scale.set(PixelScene.align(0.80f));
		changes.addButton( new ChangeButton(tp, "特别稀有怪",
			"现在 _提丰_仅在27~29层有1%的生成，且生成时会有文本提示，并且限制生成一个。\n\n" +
				"而_耄耋_生成概率仅有0.2%，在矿洞层有概率生成。"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GUN562, null), "56-2式 武器优化",
			"现在装填冷却会显示在快捷栏中，且提示文本也有具体的回合冷却显示。"));

		Image sss = new Image(Assets.Sprites.GHOUL, 0, 0, 21, 16);
		sss.scale.set(PixelScene.align(0.7f));
		changes.addButton( new ChangeButton(sss, "五区敌人生成调整",
			"_-_ 21层 2护卫者\n" +
				"_-_ 22层 3护卫者1龙骑\n" +
				"_-_ 23层 4护卫者2龙骑1木星\n" +
				"_-_ 24层 4护卫者3龙骑2木星\n\n" +
				"注意此代表敌人生成权重，实际数量可能会更高"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
			"以下Bug已被修复:\n" +
				"_-_ 修复了所有无味果相关的文本bug\n" +
				"_-_ 修复了飞槌图标错误问题\n" +
				"_-_ 修复梦夜花(未使用的老版本)，使其成为魔皇草(当前版本正在使用)\n" +
				"_-_ 修改了.50子弹贴图缺失问题\n" +
				"_-_ 修复了所有.50子弹文本错误问题\n" +
				"_-_ 修复了所有投掷武器耐久度相关的文本问题\n" +
				"_-_ 修复了所有not cursed相关的文本问题\n" +
				"_-_ 修复了所有未鉴定的瞄准镜的文本问题\n" +
				"_-_ 修复了所有奥术聚酯相关的文本(顺带修复了法杖和魔法免疫的文本)\n" +
				"_-_ 检查了所有卷轴，戒指，子弹，阅读，充能相关文本，修改了其中不合理的部分\n" +
				"_-_ 检查了所有老魔杖，能量相关文本，修改了其中不合理的部分\n" +
				"_-_ 检查了所有\"战斗G11\"\"术士\"相关文本，修改了其中不合理的部分\n" +
				"_-_ 为痛击者添加了关于精神集中buff的介绍\n" +
				"_-_ 修复了耄耋蜘蛛的bug\n" +
				"_-_ 优化了猎头蟹和耄耋蜘蛛的贴图位置，现在更难被墙之类的挡住了\n" +
				"_-_ 修复了投石索和苦无的贴图错误问题\n" +
				"_-_ 修复了提丰死亡崩溃的异常\n" +
				"_-_ 修复了投石索贴图大小不对的问题\n" +
				"_-_ 修复了炊事妖精的文本问题\n" +
				"_-_ 修复了能使用未解锁角色的bug\n" +
				"_-_ 修复562充能异常，以及修复投掷武器描述显示错误" ));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

		Image s = new Image(Assets.Sprites.WARRIOR, 0, 94, 21, 23);
		s.scale.set(PixelScene.align(0.7f));
		changes.addButton( new ChangeButton(s , HeroClass.WARRIOR.title(),
			"UMP-45 初始物品调整：\n\n" +
				"_-_ 初始获得一瓶_力量药水_"));
    }

    public static void add_0_5_0_Changes( ArrayList<ChangeInfo> changeInfos ){
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
			"由于少前地牢新版本基于的破碎地牢底层版本改变，大部分原有BOSS的战斗机制也发生了改变\n" +
				"\n" +
				"_-_ 衔尾蛇：一阶段陷阱会逐渐隐形，二阶段将利用更多样化的投掷物攻击\n\n" +
				"_-_ 计量官：改为三段式战斗，每一阶段结束会进入狂暴，获得强化的同时需要破坏特定单位才能进入下一阶段\n\n" +
				"_-_ 破坏者：改为三段式战斗，所能召唤的敌人更加多样化，且召唤的敌人与破坏者本身有着某种链接，不断击败召唤的敌人才能最终击败破坏者\n\n" +
				"_-_ 军方小队：它还是大号敌人，但是在挑战模式下获得额外增强\n\n" +
				"_-_ 主脑：替换M4成为了新BOSS，改为五段式战斗，所能召唤的精锐铁血变得更加多样化，且自身攻击能力也得到了加强"));

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
				"深入敌腹会满足你的需求，所有的Boss都会被加强。\n" +
				"你将会遭遇更加困难的作战，祝你好运！\n\n" +
				"此挑战在破碎原型为：绝命头目"));

		changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON), "新挑战：精英强敌",
			"感觉敌人太简单，想挑战更高难度？\n\n" +
				"精英强敌会满足你的需求，部分敌人都会被加强\n" +
				"你将会遭遇更加困难的作战，祝你好运！\n\n" +
				"此挑战在破碎原型为：精英强敌"));

		changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON), "特殊挑战：测试时间",
			"玩累了？想体验上帝模式？\n\n" +
				"测试时间让你深度探索游戏，拥有完整的测试工具，\n\n" +
				"但是此挑战不计入挑战数量，也无法在这里面获得成就和计入到排行榜。"));

		changes.addButton( new ChangeButton(new Image(Assets.Sprites.SNAKE, 0, 0, 16, 16), "新敌人：靶机",
			"第一大区追加了新敌人：靶机。\n\n" +
				"其破碎怪物原型为：下水道巨蛇"));

		Image kst = new Image(new SpawnerSprite());
		kst.scale.set(PixelScene.align(0.40f));
		changes.addButton( new ChangeButton(kst, "新敌人：侦查中枢",
			"第六大区追加了新敌人：侦查中枢。\n\n" +
				"其破碎怪物原型为：恶魔血巢"));

		Image kt = new Image(new RipperSprite());
		kt.scale.set(PixelScene.align(0.70f));
		changes.addButton( new ChangeButton(kt, "新敌人：尖兵",
			"第六大区追加了新敌人：尖兵。\n\n" +
				"其破碎怪物原型为：恶魔撕裂者\n\n" +
				"它通过_侦查中枢_来生成。"));

		Image gun561 = new Image(HeroSprite.avatar(HeroClass.TYPE561, 5));
		gun561.scale.set(0.75f);
		changes.addButton( new ChangeButton(gun561, "新角色：56-1式",
			"增加了新角色战术人形56-1式。\n" +
				"\n" +
				"56-1自带强力专属遗物，并且会根据饱食度改变战斗力。\n" +
				"\n" +
				"56-1拥有两种转职。"));

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

		Image tp = new Image(new TyphoonSprite.TyphoonSpriteRe());
		tp.scale.set(PixelScene.align(0.30f));
		changes.addButton( new ChangeButton(tp, "AI优化",
			"现在 _提丰/钢狮/九头蛇/木星_ 这四个敌人不会将蓄力激光攒着" +
				"\n\n瞄准一个区域后必定释放(也就是和破碎原版大眼一致的AI)"));

    }
}
