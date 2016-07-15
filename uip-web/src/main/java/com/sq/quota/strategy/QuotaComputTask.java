package com.sq.quota.strategy;

import com.sq.entity.search.MatchType;
import com.sq.entity.search.Searchable;
import com.sq.logging.domain.QuotaLogDomain;
import com.sq.logging.service.QuotaLogDomainService;
import com.sq.quota.component.MathsRoundComponent;
import com.sq.quota.domain.QuotaConsts;
import com.sq.quota.domain.QuotaInstance;
import com.sq.quota.domain.QuotaTemp;
import com.sq.quota.service.QuotaComputInsService;
import com.sq.util.DateUtil;
import com.sq.util.SpringUtils;
import net.sourceforge.jeval.EvaluationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * 指标计算独立线程.
 * 不同指标不同的计算方法的优先级通过线程内部定义的权重来确定.
 * User: shuiqing
 * Date: 2015/4/17
 * Time: 14:30
 * Email: shuiqing301@gmail.com
 * GitHub: https://github.com/ShuiQing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class QuotaComputTask implements Runnable {

    private Logger log = LoggerFactory.getLogger(QuotaComputTask.class);

    /**
     * 由于Thread非spring启动时实例化，而是根据具体的逻辑动态实例化，所以需要通过此方式从spring的context中获取相应的bean.
     */
    private QuotaComputInsService quotaComputService = SpringUtils.getBean(QuotaComputInsService.class);

    /** 指标日志服务类 */
    private QuotaLogDomainService quotaLogDomainService = SpringUtils.getBean(QuotaLogDomainService.class);

    /** 信号量 指定来自具体某一算法 */
    private String semaphore;

    /** 分配该线程时的秒数 */
    private long assignMillions;

    public QuotaLogDomain quotaLogDomain = new QuotaLogDomain();

    /** 权重 用来在线程池中确定线程执行的优先级 */
    private int weight;

    private Calendar computCal;

    /** 指标计算真正执行的算法接口，具体实现需要在实例化指标计算线程的时候指定 */
    private IQuotaComputStrategy iQuotaComputStrategy;

    private QuotaTemp quotaTemp;

    public QuotaComputTask(){}

    public QuotaComputTask(IQuotaComputStrategy iQuotaComputStrategy, Calendar computCal) {
        this.iQuotaComputStrategy = iQuotaComputStrategy;
        this.computCal = computCal;
    }

    public String getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(String semaphore) {
        this.semaphore = semaphore;
    }

    public long getAssignMillions() {
        return assignMillions;
    }

    public void setAssignMillions(long assignMillions) {
        this.assignMillions = assignMillions;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Calendar getComputCal() {
        return computCal;
    }

    public void setComputCal(Calendar computCal) {
        this.computCal = computCal;
    }

    public IQuotaComputStrategy getiQuotaComputStrategy() {
        return iQuotaComputStrategy;
    }

    public void setiQuotaComputStrategy(IQuotaComputStrategy iQuotaComputStrategy) {
        this.iQuotaComputStrategy = iQuotaComputStrategy;
    }

    public QuotaTemp getQuotaTemp() {
        return quotaTemp;
    }

    public void setQuotaTemp(QuotaTemp quotaTemp) {
        this.quotaTemp = quotaTemp;
    }

    @Override
    public void run() {
        log.error("Module Comput 计算指标 指标编码 "
                + quotaTemp.getIndicatorCode()
                + " ---- " + "指标名称" + quotaTemp.getIndicatorName() + "----"
                + DateUtil.formatCalendar(computCal,DateUtil.DATE_FORMAT_YMDH)
                + " start comput comd.");
        QuotaInstance quotaInstance = new QuotaInstance(quotaTemp);

        Calendar tempCal = (Calendar) computCal.clone();
        Object computResult = null;
        try {
            computResult = iQuotaComputStrategy.execIndiComput(quotaTemp, tempCal);
        } catch (EvaluationException e) {
            // 持久化计算错误的日志
            QuotaLogDomain quotaLogDomain = quotaLogDomainService.addWrongQuotaLog(quotaTemp,computCal);
            quotaLogDomain.setComputeResult(null);
            quotaLogDomain.setStatus(false);
            quotaLogDomain.setLegend("指标" + quotaTemp.getIndicatorCode() + "计算错误！");
            quotaLogDomainService.save(quotaLogDomain);
            return;
        }
        log.error("指标名称 计算结果" + quotaTemp.getIndicatorCode() + "，"
                + DateUtil.formatCalendar(computCal,DateUtil.DATE_FORMAT_YMDH)
                + " 计算结果为：-- " + (null != computResult ? computResult.toString() : "null"));

        if (computResult instanceof String) {
            quotaInstance.setValueType(QuotaConsts.VALUE_TYPE_STRING);
            quotaInstance.setStringValue(computResult.toString());
        } else {
            quotaInstance.setValueType(QuotaConsts.VALUE_TYPE_DOUBLE);
            Double resulet = computResult != null ? Double.parseDouble(computResult.toString()):null;
            if(resulet != null){
                /** 将存在double类型的resulet根据该指标的DecimalNum属性做处理 */
                BigDecimal bigDecimal = new BigDecimal(resulet);
                resulet = MathsRoundComponent.getValue(new BigDecimal(bigDecimal.toPlainString()),quotaTemp.getDecimalNum()).doubleValue();
                log.error("根据指标的DemicalNum换算后的值为: " + resulet);
            }
            quotaInstance.setFloatValue(resulet);
            String stringResulet = resulet != null ? resulet.toString() : null;
            quotaInstance.setStringValue(stringResulet);
        }
        Calendar tempComputCal = (Calendar) computCal.clone();
        if(iQuotaComputStrategy instanceof InterfaceQuotaStrategy) {
            if (null == computResult){
                return;
            }
//            tempComputCal.add(Calendar.HOUR_OF_DAY, -1);
        }
        quotaInstance.setInstanceTime(tempComputCal.getTime());
        quotaInstance.setCreateTime(Calendar.getInstance());
        quotaInstance.setStatDateNum(Integer.parseInt(DateUtil.formatCalendar(tempComputCal, DateUtil.DATE_FORMAT_DAFAULT)));
        if(quotaTemp.getCalFrequency() == QuotaConsts.CAL_FREQUENCY_Month && quotaTemp.getFetchCycle() == QuotaConsts.FETCH_CYCLE_Month){
            Calendar startComputDate = (Calendar) computCal.clone();
            int startComputDateNum = Integer.parseInt(DateUtil.formatCalendar(startComputDate, DateUtil.DATE_FORMAT_MONTH) + "01");
            Searchable searchable = Searchable.newSearchable()
                    .addSearchFilter("statDateNum", MatchType.GTE, startComputDateNum)
                    .addSearchFilter("indicatorCode", MatchType.EQ, quotaTemp.getIndicatorCode());
            quotaComputService.deleteInBatch(searchable);

        }else if(quotaTemp.getCalFrequency() == QuotaConsts.CAL_FREQUENCY_Year && quotaTemp.getFetchCycle() == QuotaConsts.FETCH_CYCLE_Year){
            Calendar startComputDate = (Calendar) computCal.clone();
            int startComputDateNum = Integer.parseInt(DateUtil.formatCalendar(startComputDate, "yyyy") + "0101");
            Searchable searchable = Searchable.newSearchable()
                    .addSearchFilter("statDateNum", MatchType.GTE, startComputDateNum)
                    .addSearchFilter("indicatorCode", MatchType.EQ, quotaTemp.getIndicatorCode());
            quotaComputService.deleteInBatch(searchable);
        }
        quotaComputService.save(quotaInstance);
        // 持久化计算成功的日志
        QuotaLogDomain quotaLogDomain = quotaLogDomainService.addWrongQuotaLog(quotaTemp,tempComputCal);
        quotaLogDomain.setComputeResult(computResult != null ? computResult.toString():null);
        quotaLogDomain.setLegend(null);
        quotaLogDomain.setStatus(true);
        quotaLogDomainService.save(quotaLogDomain);
    }
}
