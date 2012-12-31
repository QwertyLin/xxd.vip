package cn.xxd.vip;

import android.content.Context;
import q.util.CodeUtil;
import q.util.WifiUtil;
import cn.xxd.vip.util.Config;

public class HomeExS {
	
	public static final boolean countVerify(String str){
		return str.startsWith(Config.PREFIX_COUNT);
	}
	
	public static final String countDecode(String str){
		return str.substring(Config.PREFIX_COUNT.length());
	}
	
	public static final boolean exchangeVerify(String str){
		return str.startsWith(Config.PREFIX_EXCHANGE);
	}
	
	public static final String exchangeDecode(String str){
		return str.substring(Config.PREFIX_EXCHANGE.length());
	}
	
	public static final String verifyUrl(Context ctx, String str) throws Exception{
		return Config.URL_VERIFY + "?p=" + str + "&u=" + CodeUtil.desDecode(Config.PW_ENCODE, str).split("___")[0] + "&mac=" + WifiUtil.getMac(ctx);
	}
	
	public static final boolean verifyVerify(String str){
		return str.startsWith(Config.PREFIX_EXCHANGE_YES);
	}
	
	public static final String[] decodeUserAndPw(String str) throws Exception{
		return CodeUtil.desDecode(Config.PW_ENCODE, str).split("___");
	}

}
