package com.sq.protocol.opc.domain;

/**
 * 测试测点的结果对象
 * Created by zhangzuxiang on 2016/3/9.
 */
public class MesuringPointData {


    private String sourceCode;

    private String dataValue;

    /**
     * true : 正确
     * false : 错误
     */
    private boolean state;

    public MesuringPointData() {

    }

    public MesuringPointData(MesuringPoint mesuringPoint) {
        if(null != mesuringPoint && !"".equals(mesuringPoint.getSourceCode())){
            this.sourceCode = mesuringPoint.getSourceCode();
        }else{
            this.sourceCode = "";
        }
    }

    public String getSourceCode() {
        return sourceCode;
    }

    @Override
    public String toString() {
        return "MesuringPointData{" +
                "sourceCode='" + sourceCode + '\'' +
                ", dataValue='" + dataValue + '\'' +
                ", state=" + state +
                '}';
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
