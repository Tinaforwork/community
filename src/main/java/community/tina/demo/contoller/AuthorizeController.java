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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 当点击登录后，github会跳转返回callback请求并返回code和state信息，信息处理
 * */
@Controller
public class AuthorizeController {

    @Autowired
    private Githubprovider githubProvider;

    @Value("${github.redirect.uri}")
    private String uri;
    @Value("${github.client.id}")
    private String clientid;
    @Value("${github.client.secret}")
    private String clinetsecret;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/callback")
    private String callback(@RequestParam(name="code") String code,
                            @RequestParam(name="state")String state,
                            HttpServletRequest request)
    {

        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(uri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientid);
        accessTokenDTO.setClient_secret(clinetsecret);
        String assessToken=githubProvider.getaccesstoken(accessTokenDTO);
        GithubUser githubUser=githubProvider.getUser(assessToken);
        if (githubUser!=null)
        {
            User user=new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登陆成功,写入session和cookie
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }
        else
        {
            //登陆失败
            return "redirect:/";
        }
    }
}
