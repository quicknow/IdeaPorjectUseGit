package net.xdclass.demoproject.controller;

import com.test.AddSum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/")
public class TestXml {

    @PostMapping("testXml")
    public Object testXml(@RequestBody Map<String,String> map) {
        int a= AddSum.taddsum(4,6);
        String str="{\"Hello\":\""+a+"ccc132rwesdf\"}";

        return  map;
    }



}
