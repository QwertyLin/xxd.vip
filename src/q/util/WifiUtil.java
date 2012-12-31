package q.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiUtil {
	
	/**
	 * 获得Mac地址
	 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
	 * @param ctx
	 * @return
	 */
	public static final String getMac(Context ctx) {
		WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

}
