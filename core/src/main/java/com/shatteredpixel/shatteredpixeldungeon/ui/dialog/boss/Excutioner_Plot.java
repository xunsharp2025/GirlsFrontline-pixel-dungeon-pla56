package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.boss;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

/**
 * 执行者剧情类，继承自Plot类，用于处理执行者相关的游戏剧情
 */
public class Excutioner_Plot extends Plot {
    // 最大处理阶段数，设为1表示该剧情只有一个阶段
    private final static int maxprocess = 1;

    /**
     * 实例初始化块，用于设置初始处理阶段为1
     * 这里的process变量继承自父类Plot
     */
    {
        process = 1;
    }

    /**
     * 获取当前剧情的名称
     * @return 返回SEWER_NAME，表示下水道场景的剧情
     */
    protected String getPlotName() {
        return SEWER_NAME;
    }

    /**
     * 当剧情进展到某个阶段时调用
     * @param wndDialog 对话窗口对象，用于显示剧情内容
     */
    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        // 循环处理，直到当前处理阶段达到所需阶段
        while (this.process < needed_process) {
            this.process();
        }
    }

    /**
     * 处理剧情的主要逻辑方法
     */
    @Override
    public void process() {
        // 如果对话窗口存在，则执行剧情
        if (diagulewindow != null) {
            // 根据当前处理阶段执行不同的剧情
            if (process == 1) {
                process_to_1();
            }
            // 更新对话窗口显示
            diagulewindow.update();
            // 处理阶段递增
            process++;
        }
    }

    /**
     * 初始化剧情
     * @param wndDialog 对话窗口对象
     */
    @Override
    public void initial(WndDialog wndDialog) {
        diagulewindow = wndDialog;
        process = 2;
        process_to_1();
    }

    /**
     * 判断剧情是否结束
     * @return 如果当前处理阶段超过最大阶段，则返回true，表示剧情结束
     */
    @Override
    public boolean end() {
        return process > maxprocess;
    }

    /**
     * 跳过当前剧情
     */
    @Override
    public void skip() {
        diagulewindow.cancel();
        WndDialog.settedPlot = null;
    }

    /**
     * 处理第一阶段的具体剧情内容
     * 设置对话窗口的显示内容，包括头像、名称和对话文本
     */
    private void process_to_1() {
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(Script.BossExcutioner());
        diagulewindow.setLeftName(Script.Name(Script.Character.EXCU_TIONER));
        diagulewindow.changeText(Messages.get(this,"dialog"));
    }
}
