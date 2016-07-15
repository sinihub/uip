package
        com.sq.quota.function.math;

import com.sq.util.StringUtils;
import net.sourceforge.jeval.EvaluationConstants;
import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.*;

import java.util.ArrayList;

/**
 * 四舍五入
 * Created by zhangzuxiang on 2016/5/24.
 */
public class RoundUpFunction implements Function {
        @Override
        public String getName() {
                return "roundup";
        }
        @Override
        public FunctionResult execute(Evaluator evaluator, String arguments) throws FunctionException {
                if(StringUtils.isBlank(arguments) || arguments.contains("null")){
                        return new FunctionResult(null,
                                FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
                }
                Double result = null;
                ArrayList<Double> numbers = FunctionHelper.getDoubles(arguments,
                        EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR);
                if (numbers.size() < 1) {
                        throw new FunctionException("arguments -- " + arguments + " should more than one.");
                }

                try {
                        if(numbers.size() == 1){
                                result = Double.parseDouble(String.format("%.5f", numbers.get(0)));
                        }else{
                                String roundNum = "%." + numbers.get(1).intValue() + "f";
                                result = Double.parseDouble(String.format(roundNum, numbers.get(0)));
                        }
                        if (null == result){
                                result = Double.valueOf(0);
                        }

                }catch (Exception e) {
                        throw new FunctionException("参数列表格式或数量出错!", e);
                }

                return new FunctionResult(result.toString(),
                        FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
        }

}
