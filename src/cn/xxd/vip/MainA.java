package cn.xxd.vip;

import java.io.IOException;

import q.util.CodeUtil;
import q.util.HttpUtil;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainA extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try {
			String result = HttpUtil.get("http://www.xxd.cn/vip_ex.php");
			if(result.startsWith("r:")){
				result = result.substring("r:".length());
				System.out.println(result);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
       /* try {
			System.out.println(CodeUtil.desEncode("qqqqqvip", "linhq___123456789"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }

}
