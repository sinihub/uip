package com.sq.quota.function.math;

import net.sourceforge.jeval.EvaluationConstants;
import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.*;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 乘法函数.
 * User: shuiqing
 * Date: 2015/5/21
 * Time: 12:04
 * Email: shuiqing301@gmail.com
 * GitHub: https://github.com/ShuiQing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class MultiplyFunction implements Function {

    public String getName() {
        return "mul";
    }

    @SuppressWarnings("unchecked")
    public FunctionResult execute(final Evaluator evaluator, final String arguments)
            throws FunctionException {
        Double result = null;

        ArrayList<Double> numbers = FunctionHelper.getDoubles(arguments,
                EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR);

        if (numbers.size() != 2) {
            throw new FunctionException("arguments should be two.");
        }

        try {
            Double argumentOne = (Double) numbers.get(0);
            Double argumentTwo = (Double) numbers.get(numbers.size()-1);
            BigDecimal bigDecimal = new BigDecimal(argumentTwo.toString()).multiply(new BigDecimal(argumentOne.toString()));
            result = bigDecimal.setScale(10, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            throw new FunctionException("参数列表格式或数量出错!", e);
        }

        return new FunctionResult(result.toString(),
                FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
    }
}
