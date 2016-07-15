package com.sq.operpoint.domain;

import java.util.List;

/**
 * Created by ywj on 2016/1/26.
 */
public class PagaData {

    private Integer nextPage;

    private Integer beforePage;
    //最后一页页数，总页数
    private Integer lastPage;
    //当前页
    private Integer flatPage;
    //查询结果集
    private List<PointResults> items;
    //查询对象总数
    private Integer countList;

    public String getSourceNameCode() {
        return sourceNameCode;
    }

    public void setSourceNameCode(String sourceNameCode) {
        this.sourceNameCode = sourceNameCode;
    }

    private String sourceNameCode;

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getBeforePage() {
        return beforePage;
    }

    public void setBeforePage(Integer beforePage) {
        this.beforePage = beforePage;
    }

    public Integer getLastPage() {
       return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public Integer getCountList() {
        return countList;
    }

    public void setCountList(Integer countList) {
        this.countList = countList;
    }

    public Integer getFlatPage() {
        return flatPage;
    }

    public void setFlatPage(Integer flatPage) {
        this.flatPage = flatPage;
    }

    public List<PointResults> getItems() {
        return items;
    }

    public void setItems(List<PointResults> items) {
        this.items = items;
    }
}
