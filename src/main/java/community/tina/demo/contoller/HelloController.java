package community.tina.demo.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Tina on 2020/1/20
 * */
@Controller
public class HelloController {

    @GetMapping("/")
    public String hello(){
        return "index";
    }


}
