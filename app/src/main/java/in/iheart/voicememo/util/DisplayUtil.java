package in.iheart.voicememo.util;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;

import java.text.DecimalFormat;

/**
 * Author：weiwenhuaming on 2016/7/22 17:40
 * E-mail: weiwenhuaming@hotmail.com
 */
public class DisplayUtil {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static float dip2pxF(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int getWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取Text高度
     *
     * @param tp
     * @return
     */
    public static int getTextHeight(TextPaint tp) {
        Rect rect = new Rect();
        tp.getTextBounds("0", 0, 1, rect);
        return rect.height();
    }


    /**
     * 根据位置获取时间(最大59:59)
     *
     * @param position
     * @return
     */
    public static String getTimeByPosition(int position) {
        DecimalFormat df = new DecimalFormat("00");
        if (position < 60)
            return "00:" + df.format(position);
        else {
            return df.format(position / 60) + ":" + df.format(position % 60);
        }
    }
}
