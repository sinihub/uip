package com.sq.protocol.opc.domain;

import com.sq.entity.AbstractEntity;
import com.sq.util.DateUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

/**
 * 测点同步结果集.
 * User: shuiqing
 * Date: 2015/4/6
 * Time: 19:07
 * Email: shuiqing301@gmail.com
 * GitHub: https://github.com/ShuiQing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class OriginalDataDto extends AbstractEntity<Long> implements Serializable {

    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /** 指标的CODE */
    private String itemCode;

    /** 指标的同步值 */
    private String itemValue;

    /** 获取指标实例的时间点 */
    private String instanceTime;

    /** 数据获取批次号 */
    private Long batchNum;

    /** 系统编号 */
    private int sysId;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public Long getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(Long batchNum) {
        this.batchNum = batchNum;
    }

    public int getSysId() {
        return sysId;
    }

    public OriginalDataDto() {
        super();
    }

    public OriginalDataDto(OriginalData originalData) {
        this.id = originalData.getId();
        this.itemCode = originalData.getItemCode();
        this.itemValue = originalData.getItemValue();
        this.instanceTime = originalData.getInstanceTime() != null ? DateUtil.formatCalendar(originalData.getInstanceTime(),DateUtil.DATE_FORMAT_YMDHMS) : "";
        this.batchNum = originalData.getBatchNum();
        this.sysId = originalData.getSysId();
    }

    public void setSysId(int sysId) {
        this.sysId = sysId;
    }

    @Override
    public String toString() {
        return "OriginalData{" +
                "sysId=" + sysId +
                ", itemCode='" + itemCode + '\'' +
                ", itemValue='" + itemValue + '\'' +
                ", instanceTime=" + instanceTime + '\'' +
                ", batchNum=" + batchNum +
                '}';
    }
}
