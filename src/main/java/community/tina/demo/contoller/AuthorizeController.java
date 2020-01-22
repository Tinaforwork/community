package community.tina.demo.contoller;

import community.tina.demo.dto.AccessTokenDTO;
import community.tina.demo.dto.GithubUser;
import community.tina.demo.mapper.UserMapper;
import community.tina.demo.model.User;
import community.tina.demo.provider.Githubprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 当点击登录后，github会跳转返回callback请求并返回code和state信息，信息处理
 * */
@Controller
public class AuthorizeController {

    @Autowired
    private Githubprovider githubProvider;//自动注入githubprovider
    //注入GitHub的uri,clientid,clientsecret值(用于发送许可请求给GitHub服务器)
    @Value("${github.redirect.uri}")
    private String uri;
    @Value("${github.client.id}")
    private String clientid;
    @Value("${github.client.secret}")
    private String clinetsecret;

    @Autowired
    private UserMapper userMapper;//自动注入数据库接口

    @RequestMapping("/callback")//当github回调时，会返回许可的信息
    private String callback(@RequestParam(name="code") String code,//接收返回的code
                            @RequestParam(name="state")String state,//接收返回的state值，表示这是该应用的第几个进程？？？
                            HttpServletRequest request,//请求
                            HttpServletResponse response)//回应
    {
        //创建一个access token对象，存储的所有从GitHub接收到的许可信息
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(uri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientid);
        accessTokenDTO.setClient_secret(clinetsecret);
        String assessToken=githubProvider.getaccesstoken(accessTokenDTO);
        //通过获取到的access_token字符信息获取user的信息
        GithubUser githubUser=githubProvider.getUser(assessToken);
        if (githubUser!=null)
        {
            User user=new User();
            String token=UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            //把接收到的http的post获取的user的信息存入数据库
            userMapper.insert(user);
            //再把token信息放入response浏览器的cookie的token值中，以便下次直接读取cookie
            response.addCookie(new Cookie("token",token));
            //登陆成功,写入session和cookie
            //request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }
        else
        {
            //登陆失败
            return "redirect:/";
        }
    }
}
