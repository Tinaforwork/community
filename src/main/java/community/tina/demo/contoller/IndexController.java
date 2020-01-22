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
    @Autowired//自动注入数据库的接口
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies=request.getCookies();
        //获取请求中的cookie信息
        if (cookies!=null)
        {
            for (Cookie cookie:cookies)
            {
                if(cookie.getName().equals("token"))
                {//遍历cookies,获取token信息
                    String token=cookie.getValue();//取出cookie中的token的具体值
                    User user=userMapper.findByToken(token);//通过cookie中的token值查找数据库内的用户信息
                    if (user !=null)
                    {
                        request.getSession().setAttribute("user",user);//session中设置user的信息，以便在前端显示出来
                    }
                    break;
                }
            }
        }
        return "index";
    }


}
