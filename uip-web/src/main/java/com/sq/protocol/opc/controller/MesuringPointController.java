package
        com.sq.protocol.opc.controller;

import com.alibaba.fastjson.JSON;
import com.sq.protocol.opc.component.OpcRegisterFactory;
import com.sq.protocol.opc.domain.OriginalData;
import com.sq.protocol.opc.domain.OriginalDataDto;
import com.sq.protocol.opc.service.MesuringPointService;
import com.sq.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取缓存中的opc数据
 * Created by zzx on 2016/6/28.
 */
@Controller
public class MesuringPointController {

        private static final Logger log = LoggerFactory.getLogger(MesuringPointController.class);

        @Autowired
        private MesuringPointService mesuringPointService;

        @ResponseBody
        @RequestMapping("mesuringPointController/opcRealTimeData.do")
        public String opcRealTimeData(HttpServletRequest request,HttpServletResponse response){
                String mesuringPoints = request.getParameter("mesuringPoints");
                log.error("获取测点实时数据   opcRealTimeData ：" + mesuringPoints);
                log.error("获取测点缓存中的数据 ：" );
                for(ConcurrentHashMap.Entry<String,OriginalData> entry : OpcRegisterFactory.mesuringPointCacheMap.entrySet()){
                        log.error("获取测点缓存中的数据 ：" + entry.getValue().toString());
                }
                String[] mesuringPointArray = mesuringPoints.split(",");
                String result = "errorsuccess";
                if(null != mesuringPointArray && mesuringPointArray.length > 0){
                        List<OriginalDataDto> originalDataDtoList = new ArrayList<OriginalDataDto>();
                        for(String mesuringPoint : mesuringPointArray){
                                OriginalData originalData = OpcRegisterFactory.mesuringPointCacheMap.get(mesuringPoint);
                                if(null != originalData ){
                                        OriginalDataDto originalDataDto = new OriginalDataDto(originalData);
                                        log.error("获取测点实时数据   OriginalDataDto：" + originalDataDto.toString());
                                        originalDataDtoList.add(originalDataDto);
                                }else{
                                        OriginalDataDto originalDataResult = new OriginalDataDto();
                                        originalDataResult.setItemCode(mesuringPoint);
                                        originalDataDtoList.add(originalDataResult);
                                }
                        }
                        if(null != originalDataDtoList && originalDataDtoList.size() > 0){
                                result = JSON.toJSONString(originalDataDtoList);
                        }else{
                                result = "";
                        }
                }else{
                        result = "";
                }
                log.error("获取测点实时数据   结束" + result);
                return  result;
        }

}
