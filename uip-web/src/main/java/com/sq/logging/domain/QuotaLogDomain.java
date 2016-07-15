package com.sq.logging.domain;

import com.sq.entity.AbstractEntity;
import com.sq.quota.domain.QuotaTemp;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by ywj
 * Date: 2016/4/19.
 * 指标计算日志
 *
 */
@Entity
@Table(name="t_quotacurrentlog")
public class QuotaLogDomain extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /** 指标编码  */
    private String indicatorCode;

    /** 指标名称  */
    private String indicatorName;

    /** 计算时间  */
    private Calendar computeTime;

    /** 创建时间  */
    private Calendar createTime;

    /** 计算结果  */
    private String computeResult;

    /*  记录指标类型  */
    private Integer dataSource;

    /**  备注     */
    private String legend;

    /*  是否计算成功  */
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long aLong) {

    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public Calendar getComputeTime() {
        return computeTime;
    }

    public void setComputeTime(Calendar computeTime) {
        this.computeTime = computeTime;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public String getComputeResult() {
        return computeResult;
    }

    public void setComputeResult(String computeResult) {
        this.computeResult = computeResult;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public Integer getDataSource() {
        return dataSource;
    }

    public void setDataSource(Integer dataSource) {
        this.dataSource = dataSource;
    }

    public QuotaLogDomain(){
    }

}
