package community.tina.demo.contoller;

import community.tina.demo.mapper.UserMapper;
import community.tina.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 主页
 * */
@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies=request.getCookies();

        if (cookies!=null)
        {
            for (Cookie cookie:cookies)
            {
                if(cookie.getName().equals("token"))
                {

                    String token=cookie.getValue();
                    User user=userMapper.findByToken(token);
                    if (user !=null)
                    {
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        return "index";
    }


}
