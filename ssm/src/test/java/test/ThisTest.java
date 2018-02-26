package test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThisTest {

    public static void main(String[] args) {
        String name = "用户充值:";
      String release = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
      String feature = "1.含动画渐入与渐出效果\\n2.3秒后启动动画渐出效果";
      new TipTest(name, release, feature);

    }

}
