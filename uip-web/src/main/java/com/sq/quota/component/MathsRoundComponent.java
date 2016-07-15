package com.sq.quota.component;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 四舍五入
 * @User:yaowenjie
 * @Date:2016-06-21
 * @Time:14:46
 */
public class MathsRoundComponent {
    /**
     * 处理BigDecimal数据，保留scale位小数
     *
     * @Author yaowenjie
     * @data 2016年6月21日
     * @param value
     * @param scale
     * @return
     */
    public static BigDecimal getValue(BigDecimal value, int scale) {
        if (!isEmpty(value)) {
            return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
        return value;
    }

    /**
     * 检验对象是否为空,String 中只有空格在对象中也算空.
     * @Author yaowenjie
     * @param object
     * @return 为空返回true,否则false.
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object object) {
        if (null == object)
            return true;
        else if (object instanceof String)
            return "".equals(object.toString().trim());
        else if (object instanceof Iterable)
            return !((Iterable) object).iterator().hasNext();
        else if (object.getClass().isArray())
            return Array.getLength(object) == 0;
        else if (object instanceof Map)
            return ((Map) object).size() == 0;
        else if (Number.class.isAssignableFrom(object.getClass()))
            return false;
        else if (Date.class.isAssignableFrom(object.getClass()))
            return false;
        else
            return false;
    }
}
