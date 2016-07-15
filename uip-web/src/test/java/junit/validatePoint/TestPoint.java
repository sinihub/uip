package junit.validatePoint;

import com.alibaba.fastjson.JSON;
import com.sq.protocol.opc.controller.ValidatePointController;
import com.sq.protocol.opc.domain.MesuringPointData;
import com.sq.protocol.opc.service.ValidatePointService;
import junit.base.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2016/3/10.
 */
public class TestPoint extends TestCase {

    @Autowired
    public ValidatePointController validatePointController;

    @Autowired
    public ValidatePointService validatePointService;

    public void setValidatePointService(ValidatePointService validatePointService) {
        this.validatePointService = validatePointService;
    }

    public void setValidatePointController(ValidatePointController validatePointController) {
        this.validatePointController = validatePointController;
    }

    @Test
    public void testPoint(){
        System.out.println("aaa");
//        validatePointController.validatePoint();
        List<MesuringPointData> mesuringPointData = validatePointService.validatePoint("qqq.a1%%qqq.b1%%asdff%%");
        System.out.println(mesuringPointData.get(0));
        System.out.println(mesuringPointData.get(1));
        Object data = JSON.toJSON(mesuringPointData);
        System.out.println(data.toString());
    }

}
