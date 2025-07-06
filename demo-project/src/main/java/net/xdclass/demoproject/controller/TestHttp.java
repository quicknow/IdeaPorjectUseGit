package net.xdclass.demoproject.controller;

import net.xdclass.demoproject.domain.Video;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.test.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/testhttp/")
public class TestHttp {

    @PostMapping ("list")
    public String list() {
        int a= AddSum.taddsum(4,6);
        String str="{\"Hello\":\""+a+"ccc132rwesdf\"}";

        return  str;
    }

}
