package com.sq.quota.service;

import com.sq.entity.search.MatchType;
import com.sq.entity.search.Searchable;
import com.sq.entity.search.condition.OrCondition;
import com.sq.entity.search.condition.SearchFilterHelper;
import com.sq.inject.annotation.BaseComponent;
import com.sq.quota.component.DimensionComparator;
import com.sq.quota.component.QuotaComputHelper;
import com.sq.quota.domain.QuotaConsts;
import com.sq.quota.domain.QuotaInstance;
import com.sq.quota.domain.QuotaResetRecord;
import com.sq.quota.domain.QuotaTemp;
import com.sq.quota.repository.QuotaInstanceRepository;
import com.sq.quota.repository.QuotaTempRepository;
import com.sq.quota.strategy.IQuotaComputStrategy;
import com.sq.quota.strategy.InterfaceQuotaStrategy;
import com.sq.quota.strategy.PrimaryQuotaStrategy;
import com.sq.quota.strategy.QuotaComputTask;
import com.sq.service.BaseService;
import com.sq.util.DateUtil;
import com.sq.util.StringUtils;
import net.sourceforge.jeval.EvaluationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 指标计算业务类
 * User: shuiqing
 * Date: 2015/8/25
 * Time: 9:44
 * Email: shuiqing301@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
public class QuotaComputInsService extends BaseService<QuotaInstance,Long> {

    private static final Logger log = LoggerFactory.getLogger(QuotaComputInsService.class);

    @Autowired
    @BaseComponent
    private QuotaInstanceRepository quotaInstanceRepository;

    @Autowired
    private QuotaTempRepository quotaTempRepository;

    /** 单个计算轮回中的超时时限 */
    public static Long computWaitTimeOutValue = 10l;

    /** 指标模板缓存 */
    public static Map<String,QuotaTemp> quotaTempMapCache = new HashMap<String,QuotaTemp>();

    /** 重置指标记录缓存 */
    public static Map<String,QuotaResetRecord> quotaResetRecordMap = new HashMap<String,QuotaResetRecord>();

    /**
     * 初始化操作
     *     1、缓存指标模板
     *     2、更新指标模板的指标模板基础表达式
     */
    public void init() {
        log.error("-----------------------------------------------");
        log.error("------  初始化指标模板，重新生成表达式和信号量  -------");
        cacheQuotaTemp();
        updateQuotaExp();
    }

    /**
     * 缓存指标重置记录
     */
    /*public void cacheQuotaResetRecord() {
        List<QuotaResetRecord> quotaResetRecordList = quotaResetRecordRepository.findAll();
        for (QuotaResetRecord quotaResetRecord:quotaResetRecordList) {
            log.debug("Cache quotaResetRecord code:" + quotaResetRecord.getQuotaTemp().getIndicatorCode());
            quotaResetRecordMap.put(quotaResetRecord.getQuotaTemp().getIndicatorCode(), quotaResetRecord);
        }¡
    }*/

    /**
     * 缓存指标模板
     */
    public void cacheQuotaTemp () {
        List<QuotaTemp> quotaTempList = quotaTempRepository.findAll();
        for (QuotaTemp quotaTemp:quotaTempList) {
            log.debug("Cache quotaTemp code:" + quotaTemp.getIndicatorCode());
            quotaTempMapCache.put(quotaTemp.getIndicatorCode(), quotaTemp);
        }
    }

    /**
     * 更新指标的基础表达式
     */
    public void updateQuotaExp () {
        List<QuotaTemp> quotaTempList = new LinkedList<QuotaTemp>();
        for (Map.Entry<String, QuotaTemp> entry : quotaTempMapCache.entrySet()) {
            if (entry.getValue().getDataSource() == QuotaConsts.DATASOURCE_ENTRY
                    || null == entry.getValue().getCalculateExpression()) {
                continue;
            }
            QuotaTemp quotaTemp = entry.getValue();
            log.error(quotaTemp.getIndicatorCode() + "-----" + quotaTemp.getCalculateExpression());
            String calExp = new String(quotaTemp.getCalculateExpression());
            quotaTemp.setSemaphore(0);
            String nativeExpression = generateNativeExpression(quotaTemp);
            entry.getValue().setGernaterdNativeExpression(nativeExpression);
            entry.getValue().setCalculateExpression(calExp);

            /*String mathExpression = generateMathExpression(entry.getValue().getCalculateExpression(),
                    entry.getValue().getFetchCycle());
            entry.getValue().setMathExpression(mathExpression);*/
            log.error("CalculateExpression:" + entry.getValue().getCalculateExpression()
                    + ",nativeExpression:" + nativeExpression);
            quotaTempList.add(entry.getValue());
        }
        quotaTempRepository.save(quotaTempList);
    }


    /**
     * 生成最低级别的计算表达式
     * @return 生成的指标表达式
     */
    public String generateNativeExpression (QuotaTemp quota) {
        log.error("QuotaTemp :" + quota.getIndicatorCode() + " generateNativeExpression.Exp:" + quota.getCalculateExpression());
        if (quota.getCalculateExpression() == null) return null;

        List<String> variableList = QuotaComputHelper.getVariableList(quota.getCalculateExpression(), QuotaComputHelper.getEvaluatorInstance());

        boolean expStatusflag = true;//表达式状态，true表示已经是基础表达式，没有关联的计算指标了
        for (String variable:variableList) {
            if (variable.contains("dateTime")) {
                continue;
            }
            //生成native expression时校验关联指标编码所对应的指标模板的存在性
            //判断表达式中的编码是否存在，留到具体的计算处统一校验
            QuotaTemp quotaTemp = quotaTempMapCache.get(variable);
            if (null == quotaTemp) {
                log.error("generateNativeExpression-->>QuotaTemp :" + variable + " 不存在.");
                return null;//关联指标不存在，直接退出
            }
            if (quotaTemp.getDataSource() == QuotaConsts.DATASOURCE_CALCULATE) {
                String replaceString = quotaTemp.getCalculateExpression();
                String needReplaceString = EvaluationConstants.OPEN_VARIABLE + variable + EvaluationConstants.CLOSED_BRACE;
                if(quota != null && StringUtils.isNotBlank(quota.getCalculateExpression())
                        && StringUtils.isNotBlank(replaceString)
                        && StringUtils.isNotBlank(needReplaceString)
                        ){
                    quota.setCalculateExpression(quota.getCalculateExpression().replace(needReplaceString, replaceString));
                    expStatusflag = false;
                }
            }
            log.error("quotaTemp code:" + quota.getIndicatorCode() + ",getSemaphore:" + quota.getSemaphore());
        }

        if (expStatusflag || quota.getCalculateExpression() == null) {
            return quota.getCalculateExpression();
        } else {
            quota.setSemaphore(quota.getSemaphore() + 1);
            return generateNativeExpression(quota);
        }
    }

    /**
     * 数据获取周期性任务
     * @param computCal  汇聚时间
     */
    public void regularDataGather(Calendar computCal){
        List<QuotaTemp> quotaTempList = quotaTempRepository.listQuotaTempByMp();
        interfaceDataGather(computCal, quotaTempList);
//        Calendar curr = (Calendar) computCal.clone();
//        curr.add(Calendar.HOUR_OF_DAY,-1);
        interfaceIndicatorDataGater(computCal, quotaTempList);
    }

    /**
     * 接口数据汇集到系统的最小维度
     * @param computCal 计算时间
     */
    public void interfaceDataGather (Calendar computCal, List<QuotaTemp> quotaTempList) {
        deleteInterfaceReComputIndicator(computCal, quotaTempList);
        for (QuotaTemp quotaTemp : quotaTempList) {
            log.debug("接口指标计算 QuotaTemp:->" + quotaTemp.getIndicatorName() + "code:" + quotaTemp.getIndicatorCode());
            sendCalculateCommForInter(quotaTemp, computCal, new InterfaceQuotaStrategy());
        }
    }

    /**
     * 发送指标计算请求
     * @param quotaTemp 指标模板
     * @param computCal 计算时间点
     * @param iComputStrategy 计算策略
     */
    public synchronized void sendCalculateComm (QuotaTemp quotaTemp,
                                                Calendar computCal, IQuotaComputStrategy iComputStrategy) {
        QuotaComputTask quotaComputTask = new QuotaComputTask();
        quotaComputTask.setComputCal(computCal);
        quotaComputTask.setAssignMillions(System.currentTimeMillis());
        quotaComputTask.setiQuotaComputStrategy(iComputStrategy);
        quotaComputTask.setQuotaTemp(quotaTemp);
        QuotaComputHelper._instance.execute(quotaComputTask);
    }

    public synchronized void sendCalculateCommForInter (QuotaTemp quotaTemp,
                                                        Calendar computCal, IQuotaComputStrategy iComputStrategy) {
        QuotaComputTask quotaComputTask = new QuotaComputTask();
        quotaComputTask.setComputCal(computCal);
        quotaComputTask.setAssignMillions(System.currentTimeMillis());
        quotaComputTask.setiQuotaComputStrategy(iComputStrategy);
        quotaComputTask.setQuotaTemp(quotaTemp);
        quotaComputTask.run();
    }

    /**
     * 接口指标日数据汇集
     * @param computCal 计算时间
     */
    public void interfaceIndicatorDataGater (Calendar computCal) {
        List<QuotaTemp> quotaTempList = quotaTempRepository.listQuotaTempByMp();
        interfaceIndicatorDataGater(computCal, quotaTempList);
    }

    /**
     * 接口指标日数据汇集
     * @param computCal 计算日期
     */
    public void interfaceIndicatorDataGater (Calendar computCal, List<QuotaTemp> assQuotaTempList) {

        LinkedBlockingQueue<QuotaTemp> waitComputQuotaQueue = new LinkedBlockingQueue<QuotaTemp>();

        Searchable searchable = Searchable.newSearchable();

        OrCondition orCondition = new OrCondition();
        for (QuotaTemp assQuotaTemp:assQuotaTempList) {
            orCondition.add(
                    SearchFilterHelper.newCondition("gernaterdNativeExpression",
                            MatchType.LIKE, "%" + assQuotaTemp.getIndicatorCode() + "%"));
        }
        searchable.addSearchFilter(orCondition);

        List<QuotaTemp> quotaTempList = quotaTempRepository.findAll(searchable).getContent();
        List<QuotaTemp> quotaTempListSort = new ArrayList<QuotaTemp>(quotaTempList);

        Collections.sort(quotaTempListSort, new DimensionComparator());

        //删除计算指标的关联指标
        deleteNeedReComputIndicator(computCal, quotaTempList);

        for (QuotaTemp quotaTemp:quotaTempListSort) {
            log.error("指标定时计算-> 指标名称" + quotaTemp.getIndicatorName()
                    + ",IndicatorCode->" + quotaTemp.getIndicatorCode()
                    );
            waitComputQuotaQueue.add(quotaTemp);
        }

        stepSendComputRequest(waitComputQuotaQueue, QuotaConsts.CAL_FREQUENCY_DAY,QuotaConsts.FETCH_CYCLE_HALF_HOUR, computCal);
    }

    /**
     * 逐步发送指标计算请求
     * @param waitComputQuotaQueue
     * @param currCalFrequency
     * @param computCal
     */
    public synchronized void stepSendComputRequest (LinkedBlockingQueue<QuotaTemp> waitComputQuotaQueue,
                                                    int currCalFrequency,
                                                    int fetchCycle,
                                                    Calendar computCal) {
        int initSemaphore = 0;
        while (!waitComputQuotaQueue.isEmpty()) {
            QuotaTemp quotaTemp = waitComputQuotaQueue.poll();
            log.error("指标请求队列中poll出一个指标 -- WaitComputQuotaQueue poll || code->" + quotaTemp.getIndicatorCode()
                    + ",currCalFrequency->" + quotaTemp.getCalFrequency()
                    + ",currFetchCycle->" + quotaTemp.getFetchCycle()
                    + ",Semaphore->" + quotaTemp.getSemaphore());
            if (quotaTemp.getCalFrequency() != currCalFrequency
                    || quotaTemp.getFetchCycle() != fetchCycle
                    || quotaTemp.getSemaphore() != initSemaphore) {
                currCalFrequency = quotaTemp.getCalFrequency();
                fetchCycle = quotaTemp.getFetchCycle();
                initSemaphore = null == quotaTemp.getSemaphore() ? 0 : quotaTemp.getSemaphore();
                QuotaComputHelper._instance.shutdown();

                while (true) {
                    log.error("等待线程池计算完成 -- Wait for Computcal:" + DateUtil.formatCalendar(computCal,DateUtil.DATE_FORMAT_DAFAULTYMDHMS)
                            + ",Active count:" + QuotaComputHelper._instance.getActiveCount()
                            + ",currCalFrequency:" + currCalFrequency
                            + ",currFetchCycle:" + fetchCycle
                            + ",Semaphore:" + initSemaphore
                            + ",isTerminating:" + QuotaComputHelper._instance.isTerminating()
                            + ",isTerminating:" + QuotaComputHelper._instance.isTerminated());
                    if (!QuotaComputHelper._instance.isTerminating() && QuotaComputHelper._instance.isTerminated()) {
                        log.error("前次计算已完成，重新初始化线程池。ReInstance thread pool!");
                        QuotaComputHelper.fetchThreadPooSingleInstance();
                        break;
                    }
                    try {
                        Thread.sleep(100l);
                    } catch (InterruptedException e) {
                        log.error("Thread sleep error!");
                    }
                }
            }
            if (quotaTemp.getCalFrequency() == QuotaConsts.CAL_FREQUENCY_HALF_HOUR) {
                List<Calendar> hourCalList = DateUtil.get48HalfHours(computCal);
                for (Calendar hourCal:hourCalList) {
                    sendCalculateComm(quotaTemp, hourCal, new PrimaryQuotaStrategy());
                }
            }else if (quotaTemp.getCalFrequency() == QuotaConsts.CAL_FREQUENCY_HOUR) {
                List<Calendar> hourCalList = DateUtil.get24Hours(computCal);
                for (Calendar hourCal:hourCalList) {
                    sendCalculateComm(quotaTemp, hourCal, new PrimaryQuotaStrategy());
                }
            } else {
                sendCalculateComm(quotaTemp, computCal, new PrimaryQuotaStrategy());
            }
        }
    }

    /**
     * 找出指标集合的关联指标
     */
    /**
     * 找出指标集合的关联指标
     */
    public List<QuotaTemp> searchAssociatedQuotaTemp (List<String> calculateStringList) {
        if (calculateStringList.isEmpty() || calculateStringList.size() == 0) {
            log.error("searchAssociatedQuotaTemp需要查询关联指标的参数指标不存在.");
            return null;
        }

        Integer calculateStringListSizeCache = calculateStringList.size();

        Searchable searchable = Searchable.newSearchable()
                .addSearchFilter("dataSource", MatchType.EQ, QuotaConsts.DATASOURCE_CALCULATE);
        OrCondition orCondition = new OrCondition();
        for (String quotaTempStr:calculateStringList) {
            orCondition.add(
                    SearchFilterHelper.newCondition("gernaterdNativeExpression",
                            MatchType.LIKE, "%" + quotaTempStr + "%"));
        }
        searchable.addSearchFilter(orCondition);

        List<QuotaTemp> quotaTempList = quotaTempRepository.findAll(searchable).getContent();
        for (QuotaTemp quotaTemp:quotaTempList) {
            calculateStringList.add(quotaTemp.getIndicatorCode());
        }
        Set<String> quotaSet = new HashSet<String>();
        quotaSet.addAll(calculateStringList);
        calculateStringList.clear();
        calculateStringList.addAll(quotaSet);
        if (calculateStringListSizeCache == calculateStringList.size())
            return quotaTempList;

        return searchAssociatedQuotaTemp(calculateStringList);
    }

    /**
     * 找出指标集合的关联指标(被依赖的指标以及当前指标)
     * zzx
     * @param quotaTempParameterList
     * @return
     */
    public List<QuotaTemp> searchAssociatedQuotaTempByQuotaTempMapCache(List<QuotaTemp> quotaTempParameterList){
        if(null == quotaTempParameterList || quotaTempParameterList.size() <= 0){
            return null;
        }
        Searchable searchable = Searchable.newSearchable()
                .addSearchFilter("dataSource", MatchType.EQ, QuotaConsts.DATASOURCE_CALCULATE);
        OrCondition orCondition = new OrCondition();
        for (QuotaTemp quotaTemp:quotaTempParameterList) {
            if(null != quotaTemp.getIndicatorCode() && !"".equals(quotaTemp.getIndicatorCode()) ){
                orCondition.add(
                        SearchFilterHelper.newCondition("calculateExpression",
                                MatchType.LIKE, "%" + quotaTemp.getIndicatorCode() + "%"));
            }
        }
        if(null == orCondition || null == orCondition.getOrFilters() || orCondition.getOrFilters().size() <= 0){
            return quotaTempParameterList;
        }
        searchable.addSearchFilter(orCondition);
        List<QuotaTemp> quotaTempList = quotaTempRepository.findAll(searchable).getContent();
        if(null != quotaTempList && quotaTempList.size() > 0){
            List<QuotaTemp> quotaTempsParameter = new ArrayList<QuotaTemp>();

            for(QuotaTemp quotaTemp : quotaTempList){
                boolean temp = true;
                for(QuotaTemp quotaParameterTemp : quotaTempParameterList){
                    if(quotaTemp.getIndicatorCode().equals(quotaParameterTemp.getIndicatorCode())){
                        temp = false;
                        continue;
                    }
                }
                if(temp){
                    quotaTempsParameter.add(quotaTemp);
                }
            }

            Set<QuotaTemp> quotaTempSet = new HashSet<QuotaTemp>();
            List<QuotaTemp> quotaTempAlls = new ArrayList<QuotaTemp>();
            if(null != quotaTempsParameter && quotaTempsParameter.size() > 0){
                List<QuotaTemp> quotaTemps = new ArrayList<QuotaTemp>();
                quotaTemps = searchAssociatedQuotaTempByQuotaTempMapCache(quotaTempsParameter);
                quotaTempSet.addAll(quotaTemps);
            }
            quotaTempSet.addAll(quotaTempParameterList);
            quotaTempSet.addAll(quotaTempList);
            quotaTempAlls.addAll(quotaTempSet);
            return quotaTempAlls;
        }else{
            return quotaTempParameterList;
        }
    }

    /**
     * 删除接口的指标
     * @param computCal 计算时间
     * @param associatedQuotaTempList 接口指标列表
     */
    public void deleteInterfaceReComputIndicator(Calendar computCal, List<QuotaTemp> associatedQuotaTempList){
        if (associatedQuotaTempList.isEmpty())
            return;

        List<String> indicatorCodeList = new ArrayList<String>();
        for(QuotaTemp quotaTemp:associatedQuotaTempList) {
            indicatorCodeList.add(quotaTemp.getIndicatorCode());
        }

        Searchable deleteSearchable = Searchable.newSearchable()
                .addSearchFilter("instanceTime", MatchType.EQ, computCal)
                .addSearchFilter("indicatorCode",MatchType.IN, indicatorCodeList);
        quotaInstanceRepository.deleteInBatch(quotaInstanceRepository.findAll(deleteSearchable).getContent());
    }

    /**
     * 删除需要重计算的指标以及关联指标实例
     */
    public void deleteNeedReComputIndicator(Calendar computCal, List<QuotaTemp> associatedQuotaTempList){
        if (associatedQuotaTempList.isEmpty())
            return;

        List<String> indicatorCodeList = new ArrayList<String>();
        for(QuotaTemp quotaTemp:associatedQuotaTempList) {
            indicatorCodeList.add(quotaTemp.getIndicatorCode());
        }

        int startComputDateNum = Integer.parseInt(DateUtil.formatCalendar(computCal, DateUtil.DATE_FORMAT_DAFAULT));
        Searchable deleteSearchable = Searchable.newSearchable()
                .addSearchFilter("statDateNum", MatchType.GTE, startComputDateNum)
                .addSearchFilter("indicatorCode",MatchType.IN, indicatorCodeList);
        quotaInstanceRepository.deleteInBatch(quotaInstanceRepository.findAll(deleteSearchable).getContent());
    }

    /**
     * 计算指标重计算
     *     在指定计算日期至当前日期的时间范围内，对传入的指标的所有关联指标做重计算
     * @param computCal
     * @param quotaTempList
     */
    public void reComputQuota (Calendar computCal, List<QuotaTemp> quotaTempList) {
        LinkedBlockingQueue<QuotaTemp> waitComputQuotaQueue = new LinkedBlockingQueue<QuotaTemp>();
        Calendar computCalClone = (Calendar) computCal.clone();

        List<QuotaTemp> associatedQuotaTempList = searchAssociatedQuotaTempByQuotaTempMapCache(quotaTempList);
        if (null == associatedQuotaTempList)
            return;

        List<QuotaTemp> quotaTempListRemove = new ArrayList<QuotaTemp>();
        List<QuotaTemp> interfaceQuotaTempList = new ArrayList<QuotaTemp>();
        List<QuotaTemp> needRecalculate = null;
        for(QuotaTemp quotaTemp : associatedQuotaTempList){
            if(quotaTemp.getDataSource() == QuotaConsts.DATASOURCE_INTERFACE){
                interfaceQuotaTempList.add(quotaTemp);
            }
            if(quotaTemp.getDataSource() != QuotaConsts.DATASOURCE_CALCULATE){
                quotaTempListRemove.add(quotaTemp);
            }
        }
        if(null != quotaTempListRemove && quotaTempListRemove.size() > 0){
            if(quotaTempListRemove.size() == associatedQuotaTempList.size()){
                if(null != interfaceQuotaTempList && interfaceQuotaTempList.size() > 0){
                    //删除计算指标的关联指标(接口指标)
                    deleteNeedReComputIndicator(computCalClone, interfaceQuotaTempList);
                    List<Calendar> calendarList = DateUtil.dayListSinceCal(computCalClone);
                    for(Calendar computCalTemp:calendarList) {
                        log.error("发送指标(接口指标)计算请求 - Start send comput request computCal: " + DateUtil.formatCalendar(computCalTemp));
                        LinkedBlockingQueue<QuotaTemp> waitComputQuotaInterfaceQueue = new LinkedBlockingQueue<QuotaTemp>();
                        for(QuotaTemp quotaTemp : interfaceQuotaTempList){
                            waitComputQuotaInterfaceQueue.add(quotaTemp);
                        }
                        stepSendComputRequestInterface(waitComputQuotaInterfaceQueue,computCalTemp);
                    }
                }
                return;
            }

            needRecalculate = new ArrayList<QuotaTemp>(associatedQuotaTempList);
            needRecalculate.removeAll(quotaTempListRemove);
        }

        List<QuotaTemp> monthQuotaTempList = new ArrayList<QuotaTemp>();
        List<QuotaTemp> yearQuotaTempList = new ArrayList<QuotaTemp>();
        List<QuotaTemp> sortQuotaTempList = new ArrayList<QuotaTemp>();
        for(QuotaTemp quotaTemp : needRecalculate){
            log.error("需要重计算的指标 - QuotaTemp IndicatorName: " +quotaTemp.getIndicatorName());
            if(quotaTemp.getCalFrequency() == QuotaConsts.CAL_FREQUENCY_Month && quotaTemp.getFetchCycle() == QuotaConsts.FETCH_CYCLE_Month){
                monthQuotaTempList.add(quotaTemp);
            } else if(quotaTemp.getCalFrequency() == QuotaConsts.CAL_FREQUENCY_Year && quotaTemp.getFetchCycle() == QuotaConsts.FETCH_CYCLE_Year){
                yearQuotaTempList.add(quotaTemp);
            } else{
                sortQuotaTempList.add(quotaTemp);
            }
        }

        //删除计算指标的关联指标
        deleteNeedReComputIndicator(computCal, associatedQuotaTempList);

        try {
            List<Calendar> calendarList = DateUtil.dayListSinceCal(computCal);

            if(null != interfaceQuotaTempList && interfaceQuotaTempList.size() > 0){
                //删除计算指标的关联指标(接口指标)
                deleteNeedReComputIndicator(computCalClone, interfaceQuotaTempList);
                for(Calendar computCalTemp:calendarList) {
                    log.error("发送指标(接口指标)计算请求 - Start send comput request computCal: " + DateUtil.formatCalendar(computCalTemp));
                    LinkedBlockingQueue<QuotaTemp> waitComputQuotaInterfaceQueue = new LinkedBlockingQueue<QuotaTemp>();
                    for(QuotaTemp quotaTemp : interfaceQuotaTempList){
                        waitComputQuotaInterfaceQueue.add(quotaTemp);
                    }
                    stepSendComputRequestInterface(waitComputQuotaInterfaceQueue,computCalTemp);
                }
            }

            if (null != sortQuotaTempList && sortQuotaTempList.size() > 0){

                Collections.sort(sortQuotaTempList, new DimensionComparator());

                for(Calendar computCalTemp:calendarList) {
                    log.error("需要重计算的排序列表 - SortQuotaTempList size: " + sortQuotaTempList.size());
                    log.error("发送指标计算请求 - Start send comput request computCal: " + DateUtil.formatCalendar(computCalTemp));

                    for (QuotaTemp quotaTemp:sortQuotaTempList) {

                        waitComputQuotaQueue.put(quotaTemp);

                        log.error("     --- Sort quotaTemp: " + quotaTemp.getIndicatorCode()
                                + ",CAL_FREQUENCY->" + quotaTemp.getCalFrequency()
                                + ",FETCH_CYCLE->" + quotaTemp.getFetchCycle()
                                + ",Semaphore->" + quotaTemp.getSemaphore());
                    }

                    stepSendComputRequest(waitComputQuotaQueue, QuotaConsts.CAL_FREQUENCY_HOUR, QuotaConsts.FETCH_CYCLE_HOUR, computCalTemp);
                }
            }

            if(null != monthQuotaTempList && monthQuotaTempList.size() > 0){
                Map<Integer,Calendar> allTime = new TreeMap<Integer,Calendar>();
                for(Calendar computCalTemp:calendarList) {
                    Date date = DateUtil.getLastDayOfMonth(computCalTemp.getTime());
                    String time = DateUtil.formatDate(date);
                    Integer timeNum = Integer.parseInt(time);
                    allTime.put(timeNum,DateUtil.dateToCalendar(date));
                }

                Calendar nowTime = Calendar.getInstance();
                Date date = DateUtil.getLastDayOfMonth(nowTime.getTime());
                String time = DateUtil.formatDate(date);
                Integer timeNum = Integer.parseInt(time);
                allTime.remove(timeNum);

                nowTime.set(Calendar.HOUR_OF_DAY, 0);
                nowTime.set(Calendar.MINUTE,0);
                nowTime.set(Calendar.SECOND, 0);
                String time1 = DateUtil.formatCalendar(nowTime);
                Integer timeNum1 = Integer.parseInt(time1);
                allTime.put(timeNum1, nowTime);

                Collections.sort(monthQuotaTempList, new DimensionComparator());
                LinkedBlockingQueue<QuotaTemp> waitComputQuotaMonthQueue = new LinkedBlockingQueue<QuotaTemp>();
                for(Map.Entry<Integer,Calendar> entry : allTime.entrySet()){
                    log.error("需要重计算的日期 月" +DateUtil.formatCalendar(entry.getValue(), DateUtil.DATE_FORMAT_YMDHMS));
                    for (QuotaTemp quotaTemp:monthQuotaTempList) {
//                        sendCalculateComm(quotaTemp, entry.getValue(), new PrimaryQuotaStrategy());
                        waitComputQuotaMonthQueue.add(quotaTemp);
                    }
                    stepSendComputRequest(waitComputQuotaMonthQueue, QuotaConsts.CAL_FREQUENCY_HOUR, QuotaConsts.FETCH_CYCLE_HOUR, entry.getValue());
                }

            }
            if(null != yearQuotaTempList && yearQuotaTempList.size() > 0){

                Map<Integer,Calendar> allTime = new TreeMap<Integer,Calendar>();
                for(Calendar computCalTemp:calendarList) {
                    System.out.println("year time " + DateUtil.formatCalendar(computCalTemp,DateUtil.DATE_FORMAT_YMDHMS));
                    Calendar date = this.getLastDayOfYear(computCalTemp);
                    String time = DateUtil.formatCalendar(date);
                    Integer timeNum = Integer.parseInt(time);
                    allTime.put(timeNum,date);
                }

                Calendar nowTime = Calendar.getInstance();
                Calendar date = this.getLastDayOfYear(nowTime);
                String time = DateUtil.formatCalendar(date);
                Integer timeNum = Integer.parseInt(time);
                allTime.remove(timeNum);

//                nowTime = Calendar.getInstance();
                nowTime.set(Calendar.HOUR_OF_DAY, 0);
                nowTime.set(Calendar.MINUTE,0);
                nowTime.set(Calendar.SECOND, 0);
                String time1 = DateUtil.formatCalendar(nowTime);
                Integer timeNum1 = Integer.parseInt(time1);
                allTime.put(timeNum1, nowTime);

                Collections.sort(yearQuotaTempList, new DimensionComparator());
                LinkedBlockingQueue<QuotaTemp> waitComputQuotaYearQueue = new LinkedBlockingQueue<QuotaTemp>();
                for(Map.Entry<Integer,Calendar> entry : allTime.entrySet()){
                    log.error("需要重计算的日期 年" +DateUtil.formatCalendar(entry.getValue(), DateUtil.DATE_FORMAT_YMDHMS));
                    for (QuotaTemp quotaTemp:yearQuotaTempList) {
//                        sendCalculateComm(quotaTemp, entry.getValue(), new PrimaryQuotaStrategy());
                        waitComputQuotaYearQueue.add(quotaTemp);
                    }
                    stepSendComputRequest(waitComputQuotaYearQueue, QuotaConsts.CAL_FREQUENCY_HOUR, QuotaConsts.FETCH_CYCLE_HOUR, entry.getValue());
                }
            }
        } catch (InterruptedException e) {
            log.error("waitComputQuotaQueue add quotaTemp ", e);
        }
    }


    /**
     * 逐步发送指标计算请求（接口指标）
     * @param waitComputQuotaQueue
     * @param computCal
     */
    public synchronized void stepSendComputRequestInterface (LinkedBlockingQueue<QuotaTemp> waitComputQuotaQueue,Calendar computCal) {
        while (!waitComputQuotaQueue.isEmpty()) {
            QuotaTemp quotaTemp = waitComputQuotaQueue.poll();
            log.error("指标（接口指标）请求队列中poll出一个指标 -- WaitComputQuotaQueue poll || code->" + quotaTemp.getIndicatorCode()
                    + ",currCalFrequency->" + quotaTemp.getCalFrequency()
                    + ",currFetchCycle->" + quotaTemp.getFetchCycle()
                    + ",Semaphore->" + quotaTemp.getSemaphore());
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE,-30);
            if (quotaTemp.getCalFrequency() == QuotaConsts.CAL_FREQUENCY_HALF_HOUR) {
                List<Calendar> hourCalList = DateUtil.get48HalfHours(computCal);
                for (Calendar hourCal:hourCalList) {
                    if(hourCal.before(nowTime)){
                        sendCalculateCommForInter(quotaTemp, hourCal, new InterfaceQuotaStrategy());
                    }
                }
            }else if (quotaTemp.getCalFrequency() == QuotaConsts.CAL_FREQUENCY_HOUR) {
                List<Calendar> hourCalList = DateUtil.get24Hours(computCal);
                for (Calendar hourCal:hourCalList) {
                    if(hourCal.before(nowTime)){
                        sendCalculateCommForInter(quotaTemp, hourCal, new InterfaceQuotaStrategy());
                    }
                }
            } else {
                sendCalculateCommForInter(quotaTemp, computCal, new InterfaceQuotaStrategy());
            }
        }
    }

    /**
     * 计算指定日期的年最后一天
     * 2014年2月13日 下午7:45:21 shuiqing添加此方法
     * @param calendar
     * @return
     */
    public static Calendar getLastDayOfYear(Calendar calendar) {
        Calendar cal = (Calendar)calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.MONTH, 11);
        return cal;
    }


    /**
     * 根据条件删除数据
     * zzx
     * @param searchable
     */
    public void deleteInBatch(Searchable searchable){
        searchable.removePageable();
        searchable.removeSort();
        baseRepository.deleteInBatch(baseRepository.findAll(searchable).getContent());
    }
}
