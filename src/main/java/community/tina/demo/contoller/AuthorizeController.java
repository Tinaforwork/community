package community.tina.demo.contoller;

import community.tina.demo.dto.AccessTokenDTO;
import community.tina.demo.dto.GithubUser;
import community.tina.demo.provider.Githubprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @RequestMapping("/callback")
    private String callback(@RequestParam(name="code") String code,
                            @RequestParam(name="state")String state)
    {

        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(uri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientid);
        accessTokenDTO.setClient_secret(clinetsecret);
        String assessToken=githubProvider.getaccesstoken(accessTokenDTO);
        GithubUser user=githubProvider.getUser(assessToken);
        System.out.println(user.getName());
        return "index";
    }
}
