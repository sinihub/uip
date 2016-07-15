package com.sq.operpoint.service;

import com.sq.inject.annotation.BaseComponent;
import com.sq.operpoint.domain.Constant;
import com.sq.operpoint.domain.LeafData;
import com.sq.protocol.opc.component.OpcRegisterFactory;
import com.sq.protocol.opc.component.UtgardOpcHelper;
import com.sq.protocol.opc.domain.MeaType;
import com.sq.protocol.opc.domain.MesuringPoint;
import com.sq.protocol.opc.domain.OpcServerInfomation;
import com.sq.protocol.opc.repository.MesuringPointRepository;
import com.sq.service.BaseService;
import org.apache.poi.hssf.usermodel.*;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.openscada.opc.lib.da.browser.Branch;
import org.openscada.opc.lib.da.browser.FlatBrowser;
import org.openscada.opc.lib.da.browser.Leaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ywj on 2016/1/25.
 * 测点页面管理服务类
 *_{___{__}\
 {_}      `\)
 {_}        `            _.-''''--.._
 {_}                    //'.--.  \___`.
 { }__,_.--~~~-~~~-~~-::.---. `-.\  `.)
 `-.{_{_{_{_{_{_{_{_//  -- 0;=- `
 `-:,_.:,_:,_:,.`\\._ ..'0- ,
 // // // //`-.`\`   .-'/
 << << << <<    \ `--'  /----)
 ^  ^  ^  ^     `-.....--'''
 */
@Service("opePointService")
public class OpePointService extends BaseService<MesuringPoint,Long> {
    private static final Logger log = LoggerFactory.getLogger(OpePointService.class);

    @Autowired
    @BaseComponent
    private MesuringPointRepository mesuringPointRepository;

    private EntityManagerFactory emf;
    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }
    //用于接收测点
    public static StringBuffer strBuffer = new StringBuffer("");

    private static Map<Integer, String> map = new HashMap<Integer, String>();
    //用于存储生成的OpcServerInfomation
    private static Map<Integer, Server> opcServerInfomationmap = new HashMap<Integer, Server>();

    /**
     * 测试测点是否连通服务
     * @param server
     * @param sourceCode
     */
    public String checkPoint(Server server,String sourceCode){
        //接收测试结果
        JIVariant info = null;
        String fail = Constant.NO_RESULT;
        server.setDefaultUpdateRate(Constant.UpdateRate);
        try {
            //为服务添加组
            Group group = server.addGroup();
            //添加点的信息
            Item item = group.addItem(sourceCode);
            Map<String, Item> items = group.addItems(sourceCode);
            for (Map.Entry<String, Item> temp : items.entrySet()) {
                info =  item.read(false).getValue();
            }
        } catch (UnknownHostException e) {
            log.error("未获得主机Host");
        } catch (NotConnectedException e) {
            log.error("失去连接");
        } catch (JIException e) {
            log.error("JIException");
        } catch (DuplicateGroupException e) {
            log.error("组重复异常");
        } catch (AddFailedException e) {
            log.error("测点添加失败");
            return fail;
        }
        return info.toString();
    }

    public String checkPointSeverReturnInfomation(String sourceCode,Integer sysId){
        //传入系统编号获取服务连接
        OpcServerInfomation opcServerInfomation = OpcRegisterFactory.fetchOpcInfo(sysId);
        String info = null;
        if(opcServerInfomation != null){
            Server server = opcServerInfomation.getServer();
            //没有获取到服务信息代表连接失败
            if(null == server){
                server = UtgardOpcHelper.connect(sysId);
                opcServerInfomation.setServer(server);
                info = this.checkPoint(server, sourceCode);
            }else{
                //传入测点编码进行测试
                info = this.checkPoint(server, sourceCode);
            }
        }else{
            info = "fail";
        }
        return info;
    }

    /**
     * 获取总页数
     * @param sourceName
     * @return
     */
    public Integer getLastPageNo(String sourceName){
        Integer countPage = 0;
        if((this.countMesuringPoint(sourceName) % Constant.eight) != Constant.ZERO){
            //不足一页的条数也占有一页
            countPage = ((this.countMesuringPoint(sourceName) / Constant.eight) + Constant.PAGE_LAST_ADD_ONE);
        } else {
            countPage = ((this.countMesuringPoint(sourceName) / Constant.eight));
        }
        return countPage;
    }

    /**
     * 获取所有记录数
     * @param sourceName
     * @return
     */
    public Integer countMesuringPoint(String sourceName) {
        Integer count = mesuringPointRepository.countMesuringPoint(sourceName);
        return count;
    }


    /**
     * 分页查询
     * @param pageNo
     * @return
     */
    public List<MesuringPoint> MesuringPointPage(Integer pageNo,String sourceName) {
        List<MesuringPoint> MesuringPoint = mesuringPointRepository.MesuringPointPage(pageNo, sourceName);
        return MesuringPoint;
    }

    /**
     * 检测测点通讯活动返回2个结果
     * @param id
     * @param pointInfo
     * @return
     */
    public String testPointTwoResult(Integer id,String pointInfo) {
        //传入系统编号获取服务连接
        OpcServerInfomation opcServerInfomation = OpcRegisterFactory.fetchOpcInfo(id);
        String result = null;
        if (opcServerInfomation != null) {
            Server server = opcServerInfomation.getServer();
            //没有获取到服务信息代表连接失败
            if (null == server) {
                server = UtgardOpcHelper.connect(id);
                opcServerInfomation.setServer(server);
                result = (this.checkPoint(server, pointInfo)+"K"+ Constant.CON_SUCCESS);
            } else {
                //传入测点编码进行测试
                String info = this.checkPoint(server, pointInfo);
                result = (this.checkPoint(server, pointInfo)+"K"+ Constant.CON_SUCCESS);
            }
        } else {
            result = (Constant.NO_RESULT+"K"+Constant.CON_FAIL);
        }
        return result;
    }
    /**
     * 删除数据
     * @param s
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSomePoint(String s){
        mesuringPointRepository.delete(Long.valueOf(s));
        log.error("删除了ID为"+s+"的数据");
    }

    /**
     * 生成excel表格方法
     * @param name
     * @param data
     * @return
     */
    public static String[] objListToExcel(String name,Map data,String path) {
        Map<String, String> ziDuan = (Map<String, String>) data.get("excelMap");
        List listData = (List) data.get("listData");
        Object[] keys = ziDuan.keySet().toArray();
        String[] ziDuanKeys = new String[keys.length];
        for (int k = Constant.ZERO; k < keys.length; k++) {
            String temp = keys[k].toString();
            Integer xuHao = Integer.parseInt(temp.substring(Constant.ZERO, Constant.ONE));
            ziDuanKeys[xuHao] = temp.substring(Constant.ONE);
        }
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            for (int i = Constant.ZERO; i < listData.size(); i++) {
                HSSFRow row = sheet.createRow(i);
                Object obj = listData.get(i);
                for (int j = Constant.ZERO; j < ziDuanKeys.length; j++) {
                    HSSFCell cell = row.createCell(j);
                    if (i == Constant.ZERO) {
                        sheet.setColumnWidth(j, Constant.UpdateRate);
                        cell.setCellValue(new HSSFRichTextString(ziDuan.get(j
                                + ziDuanKeys[j])));
                    } else {
                        String ziDuanName = (String) ziDuanKeys[j];
                        ziDuanName = ziDuanName.replaceFirst(ziDuanName
                                .substring(Constant.ZERO, Constant.ONE), ziDuanName.substring(Constant.ZERO, Constant.ONE)
                                .toUpperCase());
                        ziDuanName = "get" + ziDuanName;
                        Class clazz = Class.forName(obj.getClass().getName());
                        Method[] methods = clazz.getMethods();
                        Pattern pattern = Pattern.compile(ziDuanName);
                        Matcher mat = null;
                        for (Method m : methods) {
                            mat = pattern.matcher(m.getName());
                            if (mat.find()) {
                                Object shuXing = m.invoke(obj, null);
                                if (shuXing != null) {
                                    if(shuXing.equals(MeaType.LogicCalMea)){
                                        cell.setCellValue(Constant.TWo);
                                    } else if (shuXing.equals(MeaType.OriginalMea)){
                                        cell.setCellValue(Constant.ONE);
                                    } else {
                                        cell.setCellValue(shuXing.toString());//这里可以做数据格式处理
                                    }
                                } else {
                                    cell.setCellValue("");
                                }
                                break;
                            }
                        }
                    }
                }
            }
            OutputStream out = new FileOutputStream(path);
            wb.write(out);
            out.flush();
            out.close();
            log.error("创建文件成功：" + path);
            return null;
        } catch (Exception e) {
            log.error("创建文件失败!"+ path);
            return null;
        }
    }

    /**
     * 解析上传的表格文档进行数据新增
     * add by ywj on 2016/2/4
     * @param filePath
     */
    public List<MesuringPoint> addDataForPoint(String filePath){
        List<MesuringPoint> list = new ArrayList<MesuringPoint>();
        // 创建对Excel工作簿文件的引用
        HSSFWorkbook workbook = null; // 在Excel文档中，第一张工作表的缺省索引是
        try {
            workbook = new HSSFWorkbook(new FileInputStream(filePath));
        } catch (IOException e) {
            log.error("引用文件异常无法新增测点数据！");
        }
        // 其语句为：
        HSSFSheet sheet = workbook.getSheet("Sheet1");
        // 获取到Excel文件中的所有行数
        int rows = sheet.getPhysicalNumberOfRows();
        // 遍历行
        for (int i = Constant.ZERO; i < rows; i++) {
            // 读取左上端单元格
            HSSFRow row = sheet.getRow(i);
            // 行不为空
            if (row != null) {
                // 获取到Excel文件中的所有的列
                int cells = row.getPhysicalNumberOfCells();
                String value = "";
                // 遍历列
                for (int j = Constant.ZERO; j < cells; j++) {
                    // 获取到列的值
                    HSSFCell cell = row.getCell(j);
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_FORMULA:
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            value += cell.getNumericCellValue() + ",";
                            break;
                        case HSSFCell.CELL_TYPE_STRING:
                            value += cell.getStringCellValue() + ",";
                            break;
                        default:
                            value += "null,";//解决出现空格问题
                            break;
                    }
                }
                if(i!=0){
                    // 解析数据组装对象
                    String[] val = value.split(",");
                    MesuringPoint entity = new MesuringPoint();
                    entity.setSourceCode(val[Constant.ZERO]);
                    entity.setTargetCode(val[Constant.PAGE_LAST_ADD_ONE]);
                    entity.setPointName(val[Constant.TWo]);
                    if(val[Constant.THREE].equals("2")){
                        entity.setMeaType(MeaType.LogicCalMea);
                    }else if(val[Constant.THREE].equals("1")){
                        entity.setMeaType(MeaType.OriginalMea);
                    }
                    entity.setCalculateExp(val[Constant.FOUR]);
                    entity.setSysId(Integer.parseInt(val[Constant.FIVE]));
                    list.add(entity);
                }
            }
        }
        return list;
    }

    /**
     * 打印所有的叶子节点
     * @param browser
     */
    public static List<LeafData> dumpFlat(final FlatBrowser browser)
            throws IllegalArgumentException, UnknownHostException, JIException {
        List<LeafData> leafDataList = new ArrayList<LeafData>();
        int count = Constant.ZERO;
        for (String name : browser.browse()) {
            LeafData leafData = new LeafData();
            if (name.contains(".")) {
                String[] s = name.split("\\.");
                map.put(++count, s[Constant.ZERO]);
                if (count >= Constant.TWo) {
                    if (map.get(count).equals(map.get(count - Constant.ONE))) {
                        leafData.setBrance("");
                        leafData.setLeaf(s[Constant.ONE]);
                    } else {
                        leafData.setBrance(s[Constant.ZERO]);
                        leafData.setLeaf(s[Constant.ONE]);
                    }
                } else {
                    leafData.setBrance(name);
                    leafData.setLeaf("");
                }
            }else {
                leafData.setBrance(name);
                leafData.setLeaf("");
            }
            leafDataList.add(leafData);
        }
        return leafDataList;
    }



    /**
     * 生成excel表格方法
     * @param name
     * @param data
     * @return
     */
    public static String[] LeafListToExcel(String name,Map data,String path) {
        Map<String, String> ziDuan = (Map<String, String>) data.get("excelMap");
        List listData = (List) data.get("listData");
        Object[] keys = ziDuan.keySet().toArray();
        String[] ziDuanKeys = new String[keys.length];
        for (int k = Constant.ZERO; k < keys.length; k++) {
            String temp = keys[k].toString();
            Integer xuHao = Integer.parseInt(temp.substring(Constant.ZERO, Constant.ONE));
            ziDuanKeys[xuHao] = temp.substring(Constant.ONE);
        }
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet();
            for (int i = Constant.ZERO; i < listData.size(); i++) {
                HSSFRow row = sheet.createRow(i);
                Object obj = listData.get(i);
                for (int j = Constant.ZERO; j < ziDuanKeys.length; j++) {
                    HSSFCell cell = row.createCell(j);
                    if (i == Constant.ZERO) {
                        sheet.setColumnWidth(j, Constant.UpdateRate);
                        cell.setCellValue(new HSSFRichTextString(ziDuan.get(j
                                + ziDuanKeys[j])));
                    } else {
                        String ziDuanName = (String) ziDuanKeys[j];
                        ziDuanName = ziDuanName.replaceFirst(ziDuanName
                                .substring(Constant.ZERO, Constant.ONE), ziDuanName.substring(Constant.ZERO, Constant.ONE)
                                .toUpperCase());
                        ziDuanName = "get" + ziDuanName;
                        Class clazz = Class.forName(obj.getClass().getName());
                        Method[] methods = clazz.getMethods();
                        Pattern pattern = Pattern.compile(ziDuanName);
                        Matcher mat = null;
                        for (Method m : methods) {
                            mat = pattern.matcher(m.getName());
                            if (mat.find()) {
                                Object shuXing = m.invoke(obj, null);
                                if (shuXing != null) {
                                    cell.setCellValue(shuXing.toString());//这里可以做数据格式处理
                                } else {
                                    cell.setCellValue("");
                                }
                                break;
                            }
                        }
                    }
                }
            }
            OutputStream out = new FileOutputStream(path);
            wb.write(out);
            out.flush();
            out.close();
            log.error("创建文件成功：" + path);
            return null;
        } catch (Exception e) {
            log.error("创建文件失败!" + path);
            return null;
        }
    }
}
