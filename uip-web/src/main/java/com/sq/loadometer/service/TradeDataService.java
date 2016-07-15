package com.sq.loadometer.service;

import com.sq.entity.search.MatchType;
import com.sq.entity.search.Searchable;
import com.sq.inject.annotation.BaseComponent;
import com.sq.loadometer.component.DblinkConnecter;
import com.sq.loadometer.component.JdbcHelper;
import com.sq.loadometer.domain.LoadometerIndicatorDto;
import com.sq.loadometer.domain.Trade;
import com.sq.loadometer.repository.TradeDataRepository;
import com.sq.quota.component.MathsRoundComponent;
import com.sq.quota.domain.QuotaConsts;
import com.sq.quota.domain.QuotaInstance;
import com.sq.quota.domain.QuotaTemp;
import com.sq.quota.repository.QuotaInstanceRepository;
import com.sq.quota.repository.QuotaTempRepository;
import com.sq.quota.service.QuotaComputInsService;
import com.sq.service.BaseService;
import com.sq.util.DateUtil;
import com.sq.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 地磅业务类--负责数据同步和指标生成
 * User: shuiqing
 * Date: 2015/9/15
 * Time: 10:20
 * Email: shuiqing301@gmail.com
 * GitHub: https://github.com/ShuiQing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
public class TradeDataService extends BaseService<Trade, Long> {

    private static final Logger log = LoggerFactory.getLogger(TradeDataService.class);

    @Autowired
    @BaseComponent
    private TradeDataRepository tradeDataRepository;

    @Autowired
    private QuotaTempRepository quotaTempRepository;

    @Autowired
    private QuotaInstanceRepository quotaInstanceRepository;

    @Autowired
    private QuotaComputInsService quotaComputInsService;

    /** 地磅转换倍率*/
    private Integer DB_LOAD_CURRENT = 1000;
    /**
     * 地磅流水数据同步
     */
    public void syncLoadometerTrade (String syncCal) {
        removeCurrDayTradeData(syncCal);
        insertCurrDayTradeData(syncCal);
        generateLoadometerIndicator(syncCal);
    }

    /**
     * 清除当日的已同步的流水数据
     * @param removeTradeDay 删除日期
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeCurrDayTradeData (String removeTradeDay) {
        tradeDataRepository.deleteDataBySecondTime(removeTradeDay);
    }

    /**
     * 填充当日的流水数据
     * @param fillTradeData 填充日期
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertCurrDayTradeData (String fillTradeData) {
        List<Trade> tradeList = new ArrayList<Trade>();

        StringBuilder insertTradeBuilder = new StringBuilder();
        insertTradeBuilder.append(" select * from Trade where productNet is not null and datastatus = 1 and CONVERT(varchar(12) , seconddatetime, 112 ) = ")
                          .append(fillTradeData);
        try {
            List<HashMap<String,String>> resultList = JdbcHelper.query(insertTradeBuilder.toString());
            for (HashMap tradeMap:resultList) {
                Trade trade = new Trade(tradeMap);
                Double gross = Double.parseDouble(trade.getGross())/DB_LOAD_CURRENT;
                trade.setGross(gross.toString());

                Double tare = Double.parseDouble(trade.getTare())/DB_LOAD_CURRENT;
                trade.setTare(tare.toString());

                Double net = Double.parseDouble(trade.getNet())/DB_LOAD_CURRENT;
                trade.setNet(net.toString());
                tradeList.add(trade);
            }
        } catch (SQLException e) {
            log.error("执行query error：" + insertTradeBuilder.toString());
        }
        tradeDataRepository.save(tradeList);
    }

    /**
     * 生成日地磅指标
     * @param generateDate 生成日期
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void generateLoadometerIndicator (String generateDate) {
        List<QuotaInstance> quotaInstanceList = new ArrayList<QuotaInstance>();

        //查询地磅指标数据
        List<LoadometerIndicatorDto> loadometerIndicatorDtoList = tradeDataRepository.queryForLoadometerIndicator(generateDate);
        List<String> loadometerCodeList = new ArrayList<String>();
        for (LoadometerIndicatorDto loadometerIndicatorDto:loadometerIndicatorDtoList) {
            loadometerCodeList.add(loadometerIndicatorDto.getIndicatorCode());
        }

        //删除已经存在的当日的地磅指标数据
        Searchable removeLoadometerCodeSearchable = Searchable.newSearchable()
                .addSearchFilter("indicatorCode", MatchType.IN, loadometerCodeList)
                .addSearchFilter("statDateNum", MatchType.EQ, generateDate);
        quotaInstanceRepository.deleteInBatch(quotaInstanceRepository.findAll(removeLoadometerCodeSearchable));

        log.error("----生成地磅原始指标开始----");
        //保存查询到的当日地磅指标数据
        for(LoadometerIndicatorDto loadometerIndicatorDto:loadometerIndicatorDtoList) {
            QuotaTemp quotaTemp = quotaTempRepository.findByIndicatorCode(loadometerIndicatorDto.getIndicatorCode());
            QuotaInstance quotaInstance = new QuotaInstance(quotaTemp);
            try {
                quotaInstance.setFloatValue(MathsRoundComponent.getValue(new BigDecimal(loadometerIndicatorDto.getTotalAmount()),quotaTemp.getDecimalNum()).doubleValue());
                quotaInstance.setValueType(QuotaConsts.VALUE_TYPE_DOUBLE);
                quotaInstance.setStatDateNum(Integer.parseInt(generateDate));
                quotaInstance.setInstanceTime(DateUtil.stringToDate(generateDate, DateUtil.DATE_FORMAT_DAFAULT));
                quotaInstance.setCreateTime(Calendar.getInstance());
            } catch (ParseException e) {
                log.error("stringToCalendar error:", e);
            }
            quotaInstanceList.add(quotaInstance);
        }
        log.error("----生成地磅原始指标结束----");
        quotaInstanceRepository.save(quotaInstanceList);

        log.error("----生成地磅扩展指标开始----");
        computExtendLoadoQuota(loadometerCodeList, generateDate);
        log.error("----生成地磅扩展指标结束----");
    }

    /**
     * 计算地磅的扩展指标
     * @param loadometerCodeList 原始地磅指标
     * @param generateDate 生成日期
     */
    public void computExtendLoadoQuota(List<String> loadometerCodeList, String generateDate) {
        Calendar computCal = null;
        try {
            computCal = DateUtil.stringToCalendar(generateDate, DateUtil.DATE_FORMAT_DAFAULT);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Searchable searchable = Searchable.newSearchable()
                .addSearchFilter("indicatorCode", MatchType.IN, loadometerCodeList);
        List<QuotaTemp> quotaTempList = quotaTempRepository.findAll(searchable).getContent();
        quotaComputInsService.reComputQuota(computCal, quotaTempList);
    }

    /**
     * 刷新地磅数据
     * zzx
     * @param betTwoDay
     * @return
     */
    public String refreshLoadometerTrade(String betTwoDay){
        if(StringUtils.isBlank(betTwoDay)) {
            log.error("刷新地磅数据的参数不存在或者为null！");
            return null;
        }

        String[] betTwoDayArr = new String[2];
        if (betTwoDay.contains("-")) {
            betTwoDayArr = betTwoDay.split("-");
        } else {
            betTwoDayArr[0] = betTwoDay;
            betTwoDayArr[1] = betTwoDay;
        }

        List<String> dayList = DateUtil.dayListBetweenDate(Integer.parseInt(betTwoDayArr[0]),Integer.parseInt(betTwoDayArr[1]));

        int refreshAllNum = 0;
        for (String syncDay:dayList) {
            log.error("准备刷新" + syncDay + "的地磅数据。");
            int refreshDayNum = 0;
            refreshDayNum = judgeRefreshTradeInfoByDay(syncDay);
            if( refreshDayNum == -1){
                return "error:mis地磅数据异常," + "refreshNum:"+refreshAllNum;
            }else if(refreshDayNum == -2){
                return "error:地磅数据异常," + "refreshNum:"+refreshAllNum;
            }else if(refreshDayNum >= 0){
                refreshAllNum = refreshAllNum + refreshDayNum;

                if(refreshDayNum > 0){
                    log.error("开始刷新" + syncDay + "的地磅数据。");
                    removeCurrDayTradeData(syncDay);
                    insertCurrDayTradeData(syncDay);
                    log.error("刷新" + syncDay + "的地磅数据结束。");
                }
                log.error("刷新" + syncDay + "的地磅数据结束，共刷新" + refreshDayNum + "条。");

            }
        }
        return "refreshNum:"+refreshAllNum;
    }

    /**
     * 判断某一天的数据是否需要刷新
     * zzx
     * @param time
     * @return
     */
    public int judgeRefreshTradeInfoByDay(String time){

        int initialTotalNum = 0;
        int localTotalNum = 0;
        StringBuilder insertTradeBuilder = new StringBuilder();
        insertTradeBuilder
                .append(" select ")
                .append("   COUNT(*) as totalNum ")
                .append(" FROM   ")
                .append("   czb t ")
                .append(" WHERE ")
                .append("   CONVERT (VARCHAR(12), t.cprs, 112) =  ")
                .append(time);

        try {
            System.out.println("SqlServer SQL:" + insertTradeBuilder.toString());
            Object initialResult = JdbcHelper.getSingle(insertTradeBuilder.toString());
            if(null != initialResult){
                initialTotalNum = Integer.parseInt(initialResult.toString());
            }else{
                initialTotalNum = -1;
            }
            List<Object> localResultList = tradeDataRepository.countDataBySecondTime(time);
            if(null != localResultList && localResultList.size() > 0){
                Object localResultObject = localResultList.get(0);
                localTotalNum = Integer.parseInt(localResultObject.toString());
            }else{
                localTotalNum = -1;
            }
        } catch (SQLException e) {
            log.error("执行query error：" + insertTradeBuilder.toString());
        }
        if(initialTotalNum == -1){
            if(localTotalNum == -1){
                log.error("----" + time + "mis与地磅系统中都没有数据----");
                return 0;
            }else{
                log.error("----" + time + "mis中的地磅数据异常----");
                return -1;
            }
        }else{
            if(localTotalNum == -1){
                if(initialTotalNum > 0){
                    log.error("----" + time + "mis中没有地磅数据，但是地磅系统中存在数据，所以刷新" + initialTotalNum + "条数据----");
                    return initialTotalNum;
                }else{
                    log.error("----" + time + "mis中没有地磅数据，但是地磅系统数据存在异常，所以未刷新数据----");
                    return -2;
                }
            }else{
                int refreshNum = initialTotalNum - localTotalNum;
                if(refreshNum >= 0){
                    return refreshNum;
                }else{
                    if(initialTotalNum > 0){
                        log.error("----" + time + "mis地磅数据异常，但是地磅系统中存在数据，所以刷新" + initialTotalNum + "条数据----");
                        return initialTotalNum;
                    }else{
                        log.error("----" + time + "地磅系统数据存在异常，所以未刷新数据----");
                        return -2;
                    }
                }
            }
        }
    }
}
