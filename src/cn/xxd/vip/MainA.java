package cn.xxd.vip;

import java.io.IOException;

import net.youmi.android.appoffers.YoumiOffersManager;

import q.util.CodeUtil;
import q.util.HttpUtil;
import ad.AdManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainA extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //
        //startActivity(new Intent(this, TaskA.class));
        
        AdManager.init(this);
        
        startActivity(new Intent(this, HomeA.class));
        
        
        
        /*try {
        	String str = CodeUtil.desEncode("qqqqqvip", "linhq___123456789");
			System.out.println(str.length());
			str = CodeUtil.desDecode("qqqqqvip", str);
			System.out.println(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        finish();
    }

}
