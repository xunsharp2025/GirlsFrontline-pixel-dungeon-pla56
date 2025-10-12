package com.shatteredpixel.shatteredpixeldungeon.utils;
import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import java.util.Calendar;

/**
 * 农历节日工具类，基于6Tail的农历Java库实现
 * 功能：计算中国传统节日，使地牢能够自动根据农历日期调整节日状态
 *
 * 已实现的节日：
 * - 中秋节
 *
 * 使用前需在core级gradle中导入依赖：
 * implementation 'cn.6tail:lunar:1.7.4'
 *
 * 示例用法：在RegularLevel.java中调用 Gregorian.checkLunarDates();
 *
 * @see <a href="https://mvnrepository.com/artifact/cn.6tail/lunar">Lunar Maven</a>
 * @see <a href="https://github.com/6tail/lunar-java">Lunar Github</a>
 * @since 2024.1.9 加入NTP验证系统时间
 */
public class Gregorian {
    // 事件结束时间戳（毫秒）
    private static long eventEndTime = 0;

    public static void LunarCheckDate() {
        Calendar calendar = Calendar.getInstance();
        Solar solarDate = Solar.fromDate(calendar.getTime());
        Lunar lunarDate = solarDate.getLunar();
        int gregorianMonth = calendar.get(Calendar.MONTH) + 1; // 转换为1-12月
        int gregorianDay = calendar.get(Calendar.DAY_OF_MONTH);
        eventEndTime = 0;

        checkMidAutumnFestival(lunarDate);
        checkChinaBirthday(gregorianMonth, gregorianDay);
    }

    /**
     * 检查是否为元宵节期间（农历1月15日至15+7天）
     */

    /**
     * 检查是否为中秋节期间
     */
    private static void checkMidAutumnFestival(Lunar lunar) {
        boolean isRegularMidAutumn = lunar.getMonth() == 8 &&
                (lunar.getDay() >= 15 &&
                        lunar.getDay() < 22);

        if (isRegularMidAutumn) {
            Holidays.holiday= Holidays.Holiday.midAutumnFestival;
            //eventEndTime = calculateLunarEventEndTime(lunar, 8, 21);//eventEndTime那一行先注释 下次加游戏内显示的时候有用
        }
    }
    /**
     * 检查是否为国庆节期间（阳历10月1日至10.5）
     */
    private static void checkChinaBirthday(int month, int day) {
        if (month == 10) {
            if (day >= 1 && day < 6) {
                //chinaHoliday = RegularLevel.ChinaHoliday.GQJ;
                //eventEndTime = calculateSolarEventEndTime(2025, 10, 6);//eventEndTime那一行先注释 下次加游戏内显示的时候有用
            }
        }
    }
}