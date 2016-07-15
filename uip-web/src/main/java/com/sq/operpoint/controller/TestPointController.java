package com.sq.operpoint.controller;
import com.alibaba.fastjson.JSON;
import com.sq.operpoint.domain.Constant;
import com.sq.operpoint.domain.LeafData;
import com.sq.operpoint.domain.PagaData;
import com.sq.operpoint.domain.PointResults;
import com.sq.operpoint.service.OpePointService;
import com.sq.protocol.opc.component.OpcRegisterFactory;
import com.sq.protocol.opc.component.UtgardOpcHelper;
import com.sq.protocol.opc.domain.MeaType;
import com.sq.protocol.opc.domain.MesuringPoint;
import com.sq.protocol.opc.domain.OpcServerInfomation;
import com.sq.protocol.opc.repository.MesuringPointRepository;
import com.sq.protocol.opc.service.MesuringPointService;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by ywj on 2016/1/12.
 * 测点控制模块控制器
 *  *_{___{__}\
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
@Controller
public class TestPointController {

    private static final Logger log = LoggerFactory.getLogger(TestPointController.class);

    @Autowired
    private OpePointService opePointService;

    @Autowired
    private MesuringPointRepository mesuringPointRepository;

    @Autowired
    private MesuringPointService mesuringPointService;

    /**
     * 转向到测点测试页面
     * @param model
     * @return
     */
    @RequestMapping("/testPoint/toTestPoint.do")
    public String toTestPoint(Model model){
        return "mepoint/testpoint";
    }

    /**
     * 转向到测点管理首页面
     * @param request
     * @return
     */
    @RequestMapping("/testPoint/FirstMepoint-list.do")
    public String toFIrstManage(HttpServletRequest request){
        //获取所有的测点
        List<MesuringPoint> mesuringPointList = opePointService.MesuringPointPage(Constant.PAGE_LAST_ADD_ONE,"");
        List<PointResults> pointResultsList = new ArrayList<PointResults>();
        //将获取的所有测点进行通信监测判断
        for(MesuringPoint m : mesuringPointList){
            String info = opePointService.testPointTwoResult(m.getSysId(),m.getSourceCode());
            String []s = info.split("K");
            PointResults p = new PointResults();
            p.setId(m.getId());
            p.setStatus(s[1]);
            p.setValues(s[0]);
            pointResultsList.add(p);
        }
        //用于计算得到最后一页pageNo
        Integer countPage = opePointService.getLastPageNo("");
        //将每个测点的通讯情况信息传给界面
        request.setAttribute(Constant.RE_LIST, pointResultsList);
        //将信息传给界面
        request.setAttribute(Constant.MP_LIST, mesuringPointList);
        //传回页面总记录数
        request.setAttribute(Constant.COUNT_LIST , opePointService.countMesuringPoint(""));
        //传回总页数
        request.setAttribute(Constant.LAST_PAGE, countPage);
        //传回当前页
        request.setAttribute(Constant.FLAG_PAGE, Constant.PAGE_LAST_ADD_ONE);
        //传回条件查询搜索记录
        request.setAttribute(Constant.SOURCE_NAME,"");
        return "mepoint/mepoint-list";
    }
    /**
     * 转向到测点管理任意页面
     * @param request
     * @return
     */
    @RequestMapping("/testPoint/AnyMepoint-list.do")
    public String toAnyManage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String page = request.getParameter(Constant.PAGE);
        if(page == null){
            page = Constant.PAGE_LAST_ADD_ONE.toString();
        }
        String sourceName = request.getParameter(Constant.SOURCE_NAME);
        //获取所有的测点
        List<MesuringPoint> mesuringPointList = opePointService.MesuringPointPage(Integer.parseInt(page), sourceName);
        List<PointResults> pointResultsList = new ArrayList<PointResults>();
        //将获取的所有测点进行通信监测判断
        for(MesuringPoint m : mesuringPointList){
            String info = opePointService.testPointTwoResult(m.getSysId(),m.getSourceCode());
            String []s=info.split("K");
            PointResults p = new PointResults();
            p.setId(m.getId());
            p.setSourceCode(m.getSourceCode());
            p.setTargetCode(m.getTargetCode());
            p.setPointName(m.getPointName());
            p.setSysId(m.getSysId());
            p.setStatus(s[Constant.ONE]);
            p.setValues(s[Constant.ZERO]);
            pointResultsList.add(p);
        }
        //用于计算得到最后一页pageNo
        Integer countPage = opePointService.getLastPageNo(sourceName);
        //填充dto
        PagaData p = new PagaData();
        p.setCountList(opePointService.countMesuringPoint(sourceName));
        p.setBeforePage((Integer.parseInt(page) - Constant.ONE));
        p.setNextPage((Integer.parseInt(page) + Constant.ONE));
        p.setFlatPage(Integer.parseInt(page));
        p.setItems(pointResultsList);
        p.setLastPage(countPage);
        p.setSourceNameCode(sourceName);
        PrintWriter out = response.getWriter();
        //解决出现Ill异常
        response.reset();
        String js = JSON.toJSONString(p);
        out.print(js.toString());
        out.flush();
        out.close();
        request.setAttribute("fordpage",page);
        return "mepoint/mepoint-list";
    }

    /**
     * 根据Id获取某条信息进行修改
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/testPoint/modifyAndAddPoint.do")
    public String modifyAndAddPoint(Model model,Integer id){
        MesuringPoint mesuringPoint = opePointService.findOne(Long.valueOf(id));
        model.addAttribute(Constant.ONE_POINT, mesuringPoint);
        return "mepoint/modifyAndAddPoint";
    }

    /**
     * 转向新增页面
     * @param model
     * @return
     */
    @RequestMapping("/testPoint/toAddPoint.do")
    public String toAddPoint(Model model){
        return "mepoint/modifyAndAddPoint";
    }

    /**
     * 修改和新增测点共享方法
     * @param model
     * @param mesuringPoint
     * @return
     */
    @RequestMapping("/testPoint/modifyPointWell.do")
    public String modifyPointWell(Model model,MesuringPoint mesuringPoint){
        mesuringPoint.setCalculateExp(Constant.YIN_HAO);
        mesuringPoint.setMeaType(MeaType.LogicCalMea);
        mesuringPointRepository.save(mesuringPoint);
        return "redirect:/testPoint/FirstMepoint-list.do";
    }

    /**
     * 删除测点
     * @param request
     * @return
     */
    @RequestMapping("/testPoint/deletePoint.do")
    public String deletePoint(HttpServletRequest request){
        //取得选中的数据的Id
        String s = request.getParameter(Constant.SECOND);
        opePointService.deleteSomePoint(s);
        return "redirect:/testPoint/FirstMepoint-list.do";
    }

    /**
     * 测试修改或者添加的测点是否通讯成功
     * @param model
     * @param sysId
     * @param sourceCode
     * @return
     */
    @RequestMapping("/testPoint/checkModifyAndAddIsNotWell.do")
    public String checkModifyAndAddIsNotWell(Model model, String sourceCode,Integer sysId){
        String info = opePointService.checkPointSeverReturnInfomation(sourceCode, sysId);
        model.addAttribute(Constant.RESULT, info);
        return "mepoint/mepoint-list";
    }

    /**
     * 测点测试结果方法
     * @param model
     * @return
     */
    @RequestMapping("/testPoint/testPointResult.do")
    public String testPointResult(Model model,Integer id,String pointInfo){
        //传入系统编号获取服务连接
        OpcServerInfomation opcServerInfomation = OpcRegisterFactory.fetchOpcInfo(id);
        if(opcServerInfomation != null){
            Server server = opcServerInfomation.getServer();
            //没有获取到服务信息代表连接失败
            if(null == server){
                server = UtgardOpcHelper.connect(id);
                opcServerInfomation.setServer(server);
                String info = opePointService.checkPoint(server, pointInfo);
                model.addAttribute(Constant.INFO,Constant.CON_SUCCESS);
                model.addAttribute(Constant.RESULT,info);
            }else{
                //传入测点编码进行测试
                String info = opePointService.checkPoint(server,pointInfo);
                model.addAttribute(Constant.INFO,Constant.CON_SUCCESS);
                model.addAttribute(Constant.RESULT,info);
            }
        }else{
            model.addAttribute(Constant.INFO,Constant.CON_FAIL);
            model.addAttribute(Constant.RESULT,Constant.NO_RESULT);
        }
        return "mepoint/testpoint";
    }

    /**
     * 转向到opc服务信息页面
     * @param model
     * @return
     */
    @RequestMapping("/testPoint/toOpcServerInfomation.do")
    public String toOpcServerInfomation(Model model){
        return "mepoint/opcServerInfomation";
    }

    /**
     * 接收前端的sysId获取opc服务信息
     * @param request
     * @return
     */
    @RequestMapping("/testPoint/getSysIdAndReveiveOpc.do")
    public String getSysIdAndReveiveOpc(HttpServletRequest request){
        String sysId = request.getParameter("submitopc");
        String [] opcInfo = sysId.split(",");
        Collection<ClassDetails> classDetails = null;
        try {
            ConnectionInformation connectionInformation = new ConnectionInformation();
            connectionInformation.setHost(opcInfo[0]);
            connectionInformation.setUser(opcInfo[1]);
            connectionInformation.setPassword(opcInfo[2]);
            connectionInformation.setDomain("");
            ServerList serverList = new ServerList(connectionInformation.getHost(),
                    connectionInformation.getUser(), connectionInformation.getPassword(),
                    connectionInformation.getDomain());
            classDetails = serverList
                    .listServersWithDetails(new Category[]{
                            Categories.OPCDAServer10, Categories.OPCDAServer20,
                            Categories.OPCDAServer30}, new Category[]{});
      } catch (Exception e) {
            log.error("没有发现opc服务信息");
            request.setAttribute("opcException",Constant.ZERO);
        }
        request.setAttribute("opc",classDetails);
        request.setAttribute("opcId",sysId);
        return "mepoint/opcServerInfomation";
    }

    /**
     * 导出测点表的数据到excel表格中
     * @param request
     * @return
     */
    @RequestMapping("/testPoint/exportToExcel.do")
    public void exportToExcel(HttpServletRequest request,HttpServletResponse response){
        List<MesuringPoint> list = mesuringPointRepository.findAll();
        Map<String, String> excelMap = new HashMap<String, String>();
        excelMap.put("0id", "Id");
        excelMap.put("1sourceCode", "接口编码");//属性前边的数字代表字段的先后顺序。
        excelMap.put("2targetCode", "目标编码");//最好将源码中判别顺序的格式改为"序号-字段"。
        excelMap.put("3pointName", "测点名称");
        excelMap.put("4meaType", "计算类型");
        excelMap.put("5calculateExp", "计算表达式");
        excelMap.put("6sysId", "系统编号");
        Map data = new HashMap();
        data.put("listData", list);
        data.put("excelMap", excelMap);
        String filePath =  request.getSession().getServletContext().getRealPath("/downdata") + "\\";
        String path = filePath + "MesuringPoint.xls";
        opePointService.objListToExcel("测点数据",data,path);
        //添加响应头,浏览器显示下载窗口
        response.addHeader("content-disposition", "attachment;filename=" + "MesuringPoint.xls");
        //源文件
        File sourceFile = new File(filePath,"MesuringPoint.xls");
        try {
            InputStream inStream = new FileInputStream(sourceFile);
            //获得响应的字节流
            ServletOutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[Constant.BYTES_READRATE];//缓冲数组
            int length = Constant.NEGATIVE_ONE;
            //通过循环将输入流中所有数据写入输出流
            while ((length = inStream.read(buffer, Constant.ZERO, buffer.length)) != Constant.NEGATIVE_ONE) {
                outStream.write(buffer, Constant.ZERO, length);
            }
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            log.error("下载失败!");
        }
    }

    /**
     * 上传文件并检测数据存入数据库
     * @param request
     * @param upfile
     * @return
     */
    @ResponseBody
    @RequestMapping("/testPoint/upFileAndAnalysis.do")
    public String upFileAndAnalysis(HttpServletRequest request,MultipartFile upfile){
        if (!upfile.isEmpty()) {
            String oldName = upfile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + oldName;
            // 文件保存路径
            String filePath =  request.getSession().getServletContext().getRealPath("/data") + "\\";
            File file = new File(filePath,newFileName);
            try {
                // 转存文件
                upfile.transferTo(file);
                log.error("文件上传成功");
                //取得excel表格里的数据组成集合
                List<MesuringPoint> list = opePointService.addDataForPoint(filePath + newFileName);
                //逐个遍历监测是否可用从而存入数据库中
                for(MesuringPoint p : list){
                    String info = opePointService.checkPointSeverReturnInfomation(p.getSourceCode(),p.getSysId());
                    if(!info.equals("fail")){
                        mesuringPointRepository.save(p);
                    }
                }
                return "文件上传成功，真牛比！<a href="+"/uip-web/testPoint/FirstMepoint-list.do"+">返回</a>";
            } catch (Exception e) {
                log.error("文件上传失败");
                return "文件上传失败！<a href="+"/uip-web/testPoint/FirstMepoint-list.do"+">返回</a>";
            }
        }
        return "redirect:/testPoint/FirstMepoint-list.do";
    }

    /**
     * 导出opc的所有leaf节点
     * @param request
     * @param response
     */
    @RequestMapping("/testPoint/exportOpcPoint.do")
    public void exportOpcPoint(HttpServletRequest request,HttpServletResponse response) throws JIException {
        //传入系统编号获取服务连接
        OpcServerInfomation opcServerInfomation = OpcRegisterFactory.fetchOpcInfo(Constant.ONE);
        Server server = opcServerInfomation.getServer();
        server = UtgardOpcHelper.connect(Constant.ONE);
        List<LeafData> leafDataList = null;
        //获得OPC连接下所有的Group和Item
        try {
            leafDataList = opePointService.dumpFlat(server.getFlatBrowser());
        } catch (UnknownHostException e) {
            log.error("未获得主机Host");
        }
        Map<String, String> excelMap = new HashMap<String, String>();
        excelMap.put("0brance", "brance");
        excelMap.put("1leaf", "leaf");//属性前边的数字代表字段的先后顺序。
        Map data = new HashMap();
        data.put("listData", leafDataList);
        data.put("excelMap", excelMap);
        String filePath =  request.getSession().getServletContext().getRealPath("/data") + "\\";
        String path = filePath + "leaf.xls";
        opePointService.LeafListToExcel("节点数据",data,path);
        //添加响应头,浏览器显示下载窗口
        response.addHeader("content-disposition", "attachment;filename=" + "leaf.xls");
        //源文件
        File sourceFile = new File(filePath,"leaf.xls");
        try {
            InputStream inStream = new FileInputStream(sourceFile);
            //获得响应的字节流
            ServletOutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[Constant.BYTES_READRATE];//缓冲数组
            int length = Constant.NEGATIVE_ONE;
            //通过循环将输入流中所有数据写入输出流
            while ((length = inStream.read(buffer, Constant.ZERO, buffer.length)) != Constant.NEGATIVE_ONE) {
                outStream.write(buffer, Constant.ZERO, length);
            }
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            log.error("下载失败!");
        }
    }
}


