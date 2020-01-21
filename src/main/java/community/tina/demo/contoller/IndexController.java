package community.tina.demo.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 主页
 * */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }


}
