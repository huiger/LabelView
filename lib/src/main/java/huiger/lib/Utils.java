package huiger.lib;

import android.content.Context;
import android.util.TypedValue;

/****************************************************************
 * *     *  * * * *     Created by <huiGer>
 * *     *  *           Time : 2018/4/8 15:01.
 * * * * *  *   * *     Email: zhihuiemail@163.com
 * *     *  *     *     blog : huiGer.top
 * *     *  * * * *     Desc : 工具转换类
 ****************************************************************/
public class Utils {

   public static int dp2px(Context ctx, float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context ctx, float sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ctx.getResources().getDisplayMetrics());
    }
}
