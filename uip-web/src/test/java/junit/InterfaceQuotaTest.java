package
        junit;

import com.sq.entity.search.MatchType;
import com.sq.entity.search.Searchable;
import com.sq.entity.search.condition.OrCondition;
import com.sq.entity.search.condition.SearchFilterHelper;
import com.sq.loadometer.service.TradeDataService;
import com.sq.quota.component.QuotaComputHelper;
import com.sq.quota.domain.QuotaConsts;
import com.sq.quota.domain.QuotaTemp;
import com.sq.quota.repository.QuotaTempRepository;
import com.sq.quota.service.QuotaComputInsService;
import com.sq.quota.service.QuotaTempService;
import com.sq.quota.strategy.InterfaceQuotaStrategy;
import com.sq.util.DateUtil;
import junit.base.TestCase;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class InterfaceQuotaTest extends TestCase {

//        @Autowired
//        private QuotaComputInsService quotaComputInsService;

        @Autowired
        private QuotaTempRepository quotaTempRepository;

        @Autowired
        private TradeDataService tradeDataService;

//        @Test
//        public void testInterfaceQuota(){
//                System.out.println("指标名称：");
//                Searchable searchable = Searchable.newSearchable().
//                        addSearchFilter("indicatorCode", MatchType.EQ, "B1ZFLDS_Dnum");
//                List<QuotaTemp> quotaTempList = quotaTempRepository.findAll(searchable).getContent();
//                if(null != quotaTempList && quotaTempList.size() > 0){
//                        QuotaTemp quotaTemp = quotaTempList.get(0);
//                        System.out.println("指标名称："+quotaTempList.get(0).getIndicatorName()+"指标编码：" + quotaTemp.getIndicatorCode()
//                                +"计算表达式：" + quotaTemp.getCalculateExpression());
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.set(Calendar.DAY_OF_MONTH,12);
//                        calendar.set(Calendar.MONTH,4);
//                        calendar.set(Calendar.YEAR,2016);
//                        calendar.set(Calendar.HOUR_OF_DAY,6);
//                        calendar.set(Calendar.SECOND,00);
//                        calendar.set(Calendar.MINUTE,30);
//                        quotaComputInsService.sendCalculateCommForInter(quotaTemp, calendar, new InterfaceQuotaStrategy());
//                }
//
//        }
//
//        @Test
//        public void testFindByIndicatorCode(){
//                QuotaTemp quotaTemp = quotaTempRepository.findByIndicatorCode("B3ZQLL_Hnum");
//                System.out.println(quotaTemp.getIndicatorName()+","+quotaTemp.getIndicatorCode());
//        }
//
//        @Test
//        public void testEvaluator(){
//                Evaluator evaluator = new Evaluator();
//                QuotaComputHelper.loadLocalFunctions(evaluator);
//                String exp = "sum(1/3,2,3,4,5,6,7,8,9,10,11)";
//                exp = "roundup(sum(2/3,2,3,4,5,6,7,8,9,10,11),2)";
////                exp = "abs(-123.864)";
//                try {
//                        System.out.println("-----------------------------");
//                        String result = evaluator.evaluate(exp);
//                        System.out.println("result:" + result);
//                } catch (EvaluationException e) {
//                        e.printStackTrace();
//                }
//        }

//        @Test
//        public void testTradeDataService(){
//                String result = tradeDataService.refreshLoadometerTrade("20160510-20160512");
//                System.out.println(result);
//        }

        @Autowired
        private QuotaComputInsService quotaComputInsService;

        @Autowired
        private QuotaTempService quotaTempService;

//        @Test
//        public void test() throws ParseException {
////                quotaComputInsService.init();20160616
//                Calendar cal = DateUtil.stringToCalendar("20160616", DateUtil.DATE_FORMAT_DAFAULT);
//                Searchable searchable = Searchable.newSearchable();
//                List<QuotaTemp> itemCodeList = new ArrayList<QuotaTemp>();
//                OrCondition orCondition = new OrCondition();
//                orCondition.add(SearchFilterHelper.newCondition("indicatorCode", MatchType.EQ, "PUAHJKC_Msum"));
//                searchable.or(orCondition);
//                itemCodeList = quotaTempService.findAll(searchable).getContent();
//                quotaComputInsService.reComputQuota(cal, itemCodeList);
//        }
}
