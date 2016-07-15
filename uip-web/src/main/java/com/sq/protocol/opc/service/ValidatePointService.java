package com.sq.protocol.opc.service;

/**
 * 验证mesuringPoint是否正确
 * Created by zzx on 2016/3/9.
 */

import com.sq.entity.search.MatchType;
import com.sq.entity.search.Searchable;
import com.sq.inject.annotation.BaseComponent;
import com.sq.operpoint.domain.Constant;
import com.sq.protocol.opc.component.BaseConfiguration;
import com.sq.protocol.opc.component.OpcRegisterFactory;
import com.sq.protocol.opc.component.UtgardOpcHelper;
import com.sq.protocol.opc.domain.MesuringPoint;
import com.sq.protocol.opc.domain.MesuringPointData;
import com.sq.protocol.opc.domain.OpcServerInfomation;
import com.sq.protocol.opc.repository.MesuringPointRepository;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIUnsignedInteger;
import org.jinterop.dcom.core.JIUnsignedShort;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.*;

/**
 * 验证测点code的服务层
 * Created by zhangzuxiang on 2016/3/9.
 */
@Service("validatePointService")
public class ValidatePointService {

    private static final Logger log = LoggerFactory.getLogger(ValidatePointService.class);

    private static Map<String,MesuringPointData> allPointMap = new HashMap<String,MesuringPointData>();

    @Autowired
    @BaseComponent
    private MesuringPointRepository mesuringPointRepository;

    public List<MesuringPointData> validatePoint(String sourceCodes){

        allPointMap.clear();
        //MIS测点管理测试中测点的正确性检测接口
        for (int cid=1;cid<= BaseConfiguration.CONFIG_CLIENT_MAX;cid++) {
            OpcServerInfomation opcServerInfomation = OpcRegisterFactory.fetchOpcInfo(cid);
            String sid = opcServerInfomation.getSysId();
            Searchable searchable = Searchable.newSearchable()
                    .addSearchFilter("sysId", MatchType.EQ, sid);
            List<MesuringPoint> mesuringPoints = this.mesuringPointRepository.findAll(searchable).getContent();
            Server server = opcServerInfomation.getServer();
            if(null == server){
                server = UtgardOpcHelper.connect(cid);
                opcServerInfomation.setServer(server);
            }
            server.setDefaultUpdateRate(Constant.UpdateRate);
            try {
                //为服务添加组
                Group group = server.addGroup();
                Set<Item> rightPointSet = new HashSet<Item>();
                for(MesuringPoint mesuringPoint : mesuringPoints){
                    if(null != mesuringPoint && null != mesuringPoint.getSourceCode() && !"".equals(mesuringPoint.getSourceCode())){
                        String sourceCode = mesuringPoint.getSourceCode();
                        Item checkResult = checkPoint(sourceCode,group);
                        if(null != checkResult){
                            rightPointSet.add(checkResult);
                        }else {
                            MesuringPointData mesuringPointData = new MesuringPointData();
                            mesuringPointData.setSourceCode(sourceCodes);
                            mesuringPointData.setState(false);
                            mesuringPointData.setDataValue(null);
                            allPointMap.put(sourceCode,mesuringPointData);
                        }
                    }
                }
                if(null != rightPointSet && rightPointSet.size() > 0){
                    Item[] itemArr = new Item[rightPointSet.size()];
                    int i = 0;
                    for(Item item : rightPointSet){
                        itemArr[i] = item;
                        i++;
                    }
                    Map<Item, ItemState> syncItems = null;
                    try {
                        /** arg1 false 读取缓存数据 OPCDATASOURCE.OPC_DS_CACHE  */
                        syncItems = group.read(true, itemArr);
                    } catch (JIException e) {
                        log.error("Read item error.",e);
                    }

                    if(null != syncItems && syncItems.size() > 0){
                        readItemStateMap(syncItems);
                    }
                }

            } catch (UnknownHostException e) {
                log.error("未获得主机Host");
            } catch (NotConnectedException e) {
                log.error("失去连接");
            } catch (JIException e) {
                log.error("JIException");
            } catch (DuplicateGroupException e) {
                log.error("组重复异常");
            }
        }


        List<MesuringPointData> mesuringPointDatas = new ArrayList<MesuringPointData>();

        if(null == sourceCodes || "".equals(sourceCodes)){
            Searchable searchable = Searchable.newSearchable();
            List<MesuringPoint> mesuringPoints = this.mesuringPointRepository.findAll(searchable).getContent();
            if(null != mesuringPoints && mesuringPoints.size() > 0){
                for(MesuringPoint mesuringPoint : mesuringPoints){
                    MesuringPointData mesuringPointData = null;
                    if(null != mesuringPoint.getSourceCode() && !"".equals(mesuringPoint.getSourceCode())){
                        mesuringPointData = allPointMap.get(mesuringPoint.getSourceCode());
                        mesuringPointDatas.add(mesuringPointData);
                    }
                }
            }
        }else{
            String[] sourceCodeArray = sourceCodes.split("%%");
            if(null == sourceCodeArray || sourceCodeArray.length <= 0){
                Searchable searchable = Searchable.newSearchable();
                List<MesuringPoint> mesuringPoints = this.mesuringPointRepository.findAll(searchable).getContent();
                if(null != mesuringPoints && mesuringPoints.size() > 0){
                    for(MesuringPoint mesuringPoint : mesuringPoints){
                        MesuringPointData mesuringPointData = null;
                        if(null != mesuringPoint.getSourceCode() && !"".equals(mesuringPoint.getSourceCode())){
                            mesuringPointData = allPointMap.get(mesuringPoint.getSourceCode());
                            if (null != mesuringPointData){
                                mesuringPointData.setState(true);
                            }else{
                                mesuringPointData.setState(false);
                            }
                            mesuringPointDatas.add(mesuringPointData);
                        }
                    }
                }
            }else{
                for(int i = 0 ; i < sourceCodeArray.length ; i++){
                    MesuringPointData mesuringPointData = null;
                    String key = sourceCodeArray[i];
                    if(null != key && !"".equals(key)){
                        mesuringPointData = (MesuringPointData)(allPointMap.get(key));
                        if (null != mesuringPointData){
                            mesuringPointData.setState(true);
                        }else{
                            mesuringPointData = new MesuringPointData();
                            mesuringPointData.setState(false);
                        }
                        mesuringPointData.setSourceCode(key);
                        mesuringPointDatas.add(mesuringPointData);
                    }
                }
            }
        }

        return mesuringPointDatas;
    }

    public Item checkPoint(String sourceCode,Group group ){
        Item item = null;
        try {
            JIVariant jiVariant = null;
            //添加点的信息
            item = group.addItem(sourceCode);
        } catch (JIException e) {
            log.error("JIException");
        } catch (AddFailedException e) {
            log.error("测点添加失败");
            return null;
        }
        return item;
    }

    /**
     * group读取item的同步值 mysql
     */
    public void readItemStateMap (Map<Item, ItemState> syncItems) {
        for (Map.Entry<Item, ItemState> entry : syncItems.entrySet()) {
            String itemValue = entry.getValue().getValue().toString();
            String itemCode = entry.getKey().getId();
            try {
                log.debug("key= " + entry.getKey().getId()
                        + " and value= " + entry.getValue().getValue().toString()
                        + " and type= " + entry.getValue().getValue().getType());
                JIVariant jiVariant = entry.getValue().getValue();
                if (jiVariant.getType() == 8) {
                    JIString jiString = (JIString)jiVariant.getObject();
                    itemValue = jiString.getString();
                }else if (jiVariant.getType() == 18) {
                    JIUnsignedShort jiUnsignedShort = (JIUnsignedShort)jiVariant.getObject();
                    itemValue = jiUnsignedShort.getValue().toString();
                } else if (jiVariant.getType() == 19) {
                    JIUnsignedInteger jiUnsignedInteger = (JIUnsignedInteger)jiVariant.getObject();
                    itemValue = jiUnsignedInteger.getValue().toString();
                } else {
                    itemValue = jiVariant.getObject().toString();
                }
            } catch (JIException e) {
                log.error("获取JIVariant数据出错.",e);
            }
            if (itemValue.contains("org.jinterop.dcom.core.VariantBody$EMPTY")) {
                continue;
            }
            MesuringPointData mesuringPointData = new MesuringPointData();
            mesuringPointData.setDataValue(itemValue);
            mesuringPointData.setState(true);
            mesuringPointData.setSourceCode(itemCode);
            allPointMap.put(itemCode,mesuringPointData);
        }
    }
}
