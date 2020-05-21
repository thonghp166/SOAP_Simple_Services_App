package com.example.soa;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ws.client.core.WebServiceTemplate;
import uet.soa.soap.GetSumRequest;
import uet.soa.soap.GetSumResponse;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/sum")
    @ResponseBody
    public String sum(@RequestParam(name="num1") String num1, @RequestParam(name = "num2") String num2, @RequestParam(name = "num3") String num3) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan(ClassUtils.getPackageName(GetSumRequest.class));
        try {
            marshaller.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        GetSumRequest request = new GetSumRequest();
        request.setFirstNumber(Integer.parseInt(num1));
        request.setSecondNumber(Integer.parseInt(num2));
        GetSumResponse response = (GetSumResponse) ws.marshalSendAndReceive("http://localhost:"+ 8080 + "/ws", request);

        GetSumRequest request2 = new GetSumRequest();
        request2.setFirstNumber(response.getResult());
        request2.setSecondNumber(Integer.parseInt(num3));
        GetSumResponse response2 = (GetSumResponse) ws.marshalSendAndReceive("http://localhost:"+ 8080 + "/ws", request2);

        return String.valueOf(response2.getResult());
    }

}