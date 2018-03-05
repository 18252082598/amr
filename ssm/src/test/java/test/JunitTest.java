package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.joymeter.utils.HttpClient;

public class JunitTest {

    @Test
    public void testMessage() {
        String url = "http://localhost:8080/ssm/test/get";
        Map<String,String> params = new HashMap<>();
        params.put("name", "用户充值:");
        params.put("release", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        params.put("feature", "消息提示测试：success");
        String res = HttpClient.sendPost(url, params);
        System.out.println("sendMessage.do"+res);
    }
}
