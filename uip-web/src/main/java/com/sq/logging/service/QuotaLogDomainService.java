package com.sq.logging.service;

import com.sq.inject.annotation.BaseComponent;
import com.sq.logging.domain.QuotaLogDomain;
import com.sq.logging.repository.QuotaLogDomainRepository;
import com.sq.quota.domain.QuotaTemp;
import com.sq.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * Created by ywj on 2016/4/19.
 */
@Service("quotaLogDomainService")
public class QuotaLogDomainService extends BaseService<QuotaLogDomain,Long> {

    @BaseComponent
    @Autowired
    private QuotaLogDomainRepository quotaLogDomainRepository;

    /**
     * 填充日志信息
     * @param quotaTemp
     * @param computCal
     * @return
     */
    public QuotaLogDomain addWrongQuotaLog(QuotaTemp quotaTemp, Calendar computCal){
        QuotaLogDomain quotaLogDomain = new QuotaLogDomain();
        quotaLogDomain.setComputeTime(computCal);
        quotaLogDomain.setIndicatorName(quotaTemp.getIndicatorName());
        quotaLogDomain.setIndicatorCode(quotaTemp.getIndicatorCode());
        quotaLogDomain.setDataSource(quotaTemp.getDataSource());
        quotaLogDomain.setCreateTime(Calendar.getInstance());
        return quotaLogDomain;
    }
}
