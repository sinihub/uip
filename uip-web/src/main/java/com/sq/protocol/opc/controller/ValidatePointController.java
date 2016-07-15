package com.sq.protocol.opc.controller;

import com.alibaba.fastjson.JSON;
import com.sq.protocol.opc.domain.MesuringPointData;
import com.sq.protocol.opc.service.ValidatePointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 验证mesuringPoint的controller
 * Created by zhangzuxiang on 2016/3/9.
 */
@Controller
public class ValidatePointController {

    private static final Logger log = LoggerFactory.getLogger(ValidatePointController.class);

    @Autowired
    private ValidatePointService validatePointService;

    public void setValidatePointService(ValidatePointService validatePointService) {
        this.validatePointService = validatePointService;
    }

    @ResponseBody
    @RequestMapping("validatePointController/validatePoint.do")
    public String validatePoint(HttpServletRequest request,HttpServletResponse response)throws IOException {
        String sourceCodes = request.getParameter("sourceCodes");
        List<MesuringPointData>  mesuringPointDatas = this.validatePointService.validatePoint(sourceCodes);
        return JSON.toJSONString(mesuringPointDatas);
    }

}
