package com.sq.loadometer.domain;

import com.sq.entity.AbstractEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 垃圾称重(标准)
 * User: shuiqing
 * Date: 15/11/24
 * Time: 上午10:36
 * Email: shuiqing301@gmail.com
 * GitHub: https://github.com/ShuiQing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name="T_Trade")
public class Trade extends AbstractEntity<Long> implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String ticketno1;
    private String ticketno2;
    private String station1;
    private String station2;
    private String scaleno1;
    private String scaleno2;
    private String truckno;
    private String cardno;
    private String contractno;
    private String productcode;
    private String product;
    private String specification;
    private String sender;
    private String receiver;
    private String transporter;
    private String firstdatetime;
    private String seconddatetime;
    private String grossdatetime;
    private String taredatetime;
    private String firstweight;
    private String secondweight;
    private String gross;
    private String tare;
    private String net;
    private String productnet;
    private String exceptwater;
    private String exceptother;
    private String userid1;
    private String username1;
    private String userid2;
    private String username2;
    private String bc1;
    private String bc2;
    private String scaleweightflag;
    private String uploadflag;
    private String dataeditflag;
    private String datastatus;
    private String manualinputflag;
    private String scalemode;
    private String finalflag;
    private String leftweight;
    private String autosaveflag;

    public Trade(HashMap<String, Object> map)
    {
        this.ticketno1 = (map.get("ticketno1") != null ? map.get("ticketno1").toString() : null);
        this.ticketno2 = (map.get("ticketno2") != null ? map.get("ticketno2").toString() : null);
        this.station1 = (map.get("station1") != null ? map.get("station1").toString() : null);
        this.station2 = (map.get("station2") != null ? map.get("station2").toString() : null);
        this.scaleno1 = (map.get("scaleno1") != null ? map.get("scaleno1").toString() : null);
        this.scaleno2 = (map.get("scaleno2") != null ? map.get("scaleno2").toString() : null);
        this.truckno = (map.get("truckno") != null ? map.get("truckno").toString() : null);
        this.cardno = (map.get("cardno") != null ? map.get("cardno").toString() : null);
        this.contractno = (map.get("contractno") != null ? map.get("contractno").toString() : null);
        this.productcode = (map.get("productcode") != null ? map.get("productcode").toString() : null);
        this.product = (map.get("product") != null ? map.get("product").toString() : null);
        this.specification = (map.get("specification") != null ? map.get("specification").toString() : null);
        this.sender = (map.get("sender") != null ? map.get("sender").toString() : null);
        this.receiver = (map.get("receiver") != null ? map.get("receiver").toString() : null);
        this.transporter = (map.get("transporter") != null ? map.get("transporter").toString() : null);
        this.firstdatetime = (map.get("firstdatetime") != null ? map.get("firstdatetime").toString() : null);
        this.seconddatetime = (map.get("seconddatetime") != null ? map.get("seconddatetime").toString() : null);
        this.grossdatetime = (map.get("grossdatetime") != null ? map.get("grossdatetime").toString() : null);
        this.taredatetime = (map.get("taredatetime") != null ? map.get("taredatetime").toString() : null);
        this.firstweight = (map.get("firstweight") != null ? map.get("firstweight").toString() : null);
        this.secondweight = (map.get("secondweight") != null ? map.get("secondweight").toString() : null);
        this.gross = (map.get("gross") != null ? map.get("gross").toString() : null);
        this.tare = (map.get("tare") != null ? map.get("tare").toString() : null);
        this.net = (map.get("net") != null ? map.get("net").toString() : null);
        this.productnet = (map.get("productnet") != null ? map.get("productnet").toString() : null);
        this.exceptwater = (map.get("exceptwater") != null ? map.get("exceptwater").toString() : null);
        this.exceptother = (map.get("exceptother") != null ? map.get("exceptother").toString() : null);
        this.userid1 = (map.get("userid1") != null ? map.get("userid1").toString() : null);
        this.username1 = (map.get("username1") != null ? map.get("username1").toString() : null);
        this.userid2 = (map.get("userid2") != null ? map.get("userid2").toString() : null);
        this.username2 = (map.get("username2") != null ? map.get("username2").toString() : null);
        this.bc1 = (map.get("bc1") != null ? map.get("bc1").toString() : null);
        this.bc2 = (map.get("bc2") != null ? map.get("bc2").toString() : null);
        this.scaleweightflag = (map.get("scaleweightflag") != null ? map.get("scaleweightflag").toString() : null);
        this.uploadflag = (map.get("uploadflag") != null ? map.get("uploadflag").toString() : null);
        this.dataeditflag = (map.get("dataeditflag") != null ? map.get("dataeditflag").toString() : null);
        this.datastatus = (map.get("datastatus") != null ? map.get("datastatus").toString() : null);
        this.manualinputflag = (map.get("manualinputflag") != null ? map.get("manualinputflag").toString() : null);
        this.scalemode = (map.get("scalemode") != null ? map.get("scalemode").toString() : null);
        this.finalflag = (map.get("finalflag") != null ? map.get("finalflag").toString() : null);
        this.leftweight = (map.get("leftweight") != null ? map.get("leftweight").toString() : null);
        this.autosaveflag = (map.get("autosaveflag") != null ? map.get("autosaveflag").toString() : null);
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTicketno1() {
        return this.ticketno1;
    }

    public void setTicketno1(String ticketno1) {
        this.ticketno1 = ticketno1;
    }

    public String getTicketno2() {
        return this.ticketno2;
    }

    public void setTicketno2(String ticketno2) {
        this.ticketno2 = ticketno2;
    }

    public String getStation1() {
        return this.station1;
    }

    public void setStation1(String station1) {
        this.station1 = station1;
    }

    public String getStation2() {
        return this.station2;
    }

    public void setStation2(String station2) {
        this.station2 = station2;
    }

    public String getScaleno1() {
        return this.scaleno1;
    }

    public void setScaleno1(String scaleno1) {
        this.scaleno1 = scaleno1;
    }

    public String getScaleno2() {
        return this.scaleno2;
    }

    public void setScaleno2(String scaleno2) {
        this.scaleno2 = scaleno2;
    }

    public String getTruckno() {
        return this.truckno;
    }

    public void setTruckno(String truckno) {
        this.truckno = truckno;
    }

    public String getCardno() {
        return this.cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getContractno() {
        return this.contractno;
    }

    public void setContractno(String contractno) {
        this.contractno = contractno;
    }

    public String getProductcode() {
        return this.productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getProduct() {
        return this.product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSpecification() {
        return this.specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTransporter() {
        return this.transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public String getFirstdatetime() {
        return this.firstdatetime;
    }

    public void setFirstdatetime(String firstdatetime) {
        this.firstdatetime = firstdatetime;
    }

    public String getSeconddatetime() {
        return this.seconddatetime;
    }

    public void setSeconddatetime(String seconddatetime) {
        this.seconddatetime = seconddatetime;
    }

    public String getGrossdatetime() {
        return this.grossdatetime;
    }

    public void setGrossdatetime(String grossdatetime) {
        this.grossdatetime = grossdatetime;
    }

    public String getTaredatetime() {
        return this.taredatetime;
    }

    public void setTaredatetime(String taredatetime) {
        this.taredatetime = taredatetime;
    }

    public String getFirstweight() {
        return this.firstweight;
    }

    public void setFirstweight(String firstweight) {
        this.firstweight = firstweight;
    }

    public String getSecondweight() {
        return this.secondweight;
    }

    public void setSecondweight(String secondweight) {
        this.secondweight = secondweight;
    }

    public String getGross() {
        return this.gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getTare() {
        return this.tare;
    }

    public void setTare(String tare) {
        this.tare = tare;
    }

    public String getNet() {
        return this.net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getProductnet() {
        return this.productnet;
    }

    public void setProductnet(String productnet) {
        this.productnet = productnet;
    }

    public String getExceptwater() {
        return this.exceptwater;
    }

    public void setExceptwater(String exceptwater) {
        this.exceptwater = exceptwater;
    }

    public String getExceptother() {
        return this.exceptother;
    }

    public void setExceptother(String exceptother) {
        this.exceptother = exceptother;
    }

    public String getUserid1() {
        return this.userid1;
    }

    public void setUserid1(String userid1) {
        this.userid1 = userid1;
    }

    public String getUsername1() {
        return this.username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUserid2() {
        return this.userid2;
    }

    public void setUserid2(String userid2) {
        this.userid2 = userid2;
    }

    public String getUsername2() {
        return this.username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getBc1() {
        return this.bc1;
    }

    public void setBc1(String bc1) {
        this.bc1 = bc1;
    }

    public String getBc2() {
        return this.bc2;
    }

    public void setBc2(String bc2) {
        this.bc2 = bc2;
    }

    public String getScaleweightflag() {
        return this.scaleweightflag;
    }

    public void setScaleweightflag(String scaleweightflag) {
        this.scaleweightflag = scaleweightflag;
    }

    public String getUploadflag() {
        return this.uploadflag;
    }

    public void setUploadflag(String uploadflag) {
        this.uploadflag = uploadflag;
    }

    public String getDataeditflag() {
        return this.dataeditflag;
    }

    public void setDataeditflag(String dataeditflag) {
        this.dataeditflag = dataeditflag;
    }

    public String getDatastatus() {
        return this.datastatus;
    }

    public void setDatastatus(String datastatus) {
        this.datastatus = datastatus;
    }

    public String getManualinputflag() {
        return this.manualinputflag;
    }

    public void setManualinputflag(String manualinputflag) {
        this.manualinputflag = manualinputflag;
    }

    public String getScalemode() {
        return this.scalemode;
    }

    public void setScalemode(String scalemode) {
        this.scalemode = scalemode;
    }

    public String getFinalflag() {
        return this.finalflag;
    }

    public void setFinalflag(String finalflag) {
        this.finalflag = finalflag;
    }

    public String getLeftweight() {
        return this.leftweight;
    }

    public void setLeftweight(String leftweight) {
        this.leftweight = leftweight;
    }

    public String getAutosaveflag() {
        return this.autosaveflag;
    }

    public void setAutosaveflag(String autosaveflag) {
        this.autosaveflag = autosaveflag;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ticketno1:" + this.ticketno1 + ",ticketno2:" + this.ticketno2);
        return sb.toString();
    }}
