package com.sq.operpoint.domain;

/**
 * Created by ywj on 2016/1/21.
 */
public interface Constant {
    //设置每页显示的条数
    public static final Integer PAGE_INDEX = 15;
    //用于计算最后一页
    public static final Integer PAGE_LAST_ADD_ONE = 1;
    //设置server更新速率
    public static final Integer UpdateRate = 6000;
    //设置字节流读入速率
    public static final Integer BYTES_READRATE = 1024;
    //代表-1
    public static final Integer NEGATIVE_ONE = -1;
    //代表常用数字
    public static final Integer ZERO = 0;

    public static final Integer ONE = 1;

    public static final Integer TWo = 2;

    public static final Integer THREE = 3;

    public static final Integer FOUR = 4;

    public static final Integer FIVE = 5;

    public static final Integer eight = 8;
    //page
    public static final String PAGE = "page";
    //sourceName
    public static final String SOURCE_NAME = "sourceName";
    //source
    public static final String SOURCE = "source";
    //""
    public static final String YIN_HAO = "";
    //删除Id"second"
    public static final String SECOND = "second";
    //connect ssucess
    public static final String CON_SUCCESS = "good";
    //connect fail
    public static final String CON_FAIL = "bad";
    //no result
    public static final String NO_RESULT = "fail";
    //消息提示
    public static final String INFO = "info";
    //测点测试结果
    public static final String RESULT = "resul";
    //MesuringPoint传给视图的集合
    public static final String MP_LIST = "mpList";
    //测点的通讯结果信息集合
    public static final String RE_LIST = "reList";
    //MesuringPoint查询的记录总数
    public static final String COUNT_LIST = "countList";
    //MesuringPoint查询的总页数
    public static final String LAST_PAGE = "lastPage";
    //MesuringPoint查询的当前页
    public static final String FLAG_PAGE= "flatPage";
    //查询某一条MesuringPoint的信息key
    public static final String ONE_POINT = "onePoint";
}
