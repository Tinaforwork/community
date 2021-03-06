package community.tina.demo.provider;

import com.alibaba.fastjson.JSON;
import community.tina.demo.dto.AccessTokenDTO;
import community.tina.demo.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * 这是为了Github返回账号信息提供支持
 * **/
@Component
public class Githubprovider {
    public String getaccesstoken(AccessTokenDTO accessTokenDTO)
    {//获取github返回的access_token，并输出token
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String s=response.body().string();
            String token= s.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public GithubUser getUser(String accessToken){
        //根据access_token获取用户信息
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();
            GithubUser gUser= JSON.parseObject(string,GithubUser.class);
            return gUser;
        } catch (IOException e) {
        }
        return null;
    }
}
