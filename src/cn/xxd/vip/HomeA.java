package cn.xxd.vip;

import java.util.Date;

import cn.xxd.vip.util.Config;
import q.util.AsyncHttpHelper;
import ad.AdManager;
import android.app.Activity;
import android.os.Bundle;	
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class HomeA extends Activity implements OnClickListener {
	
	private String mCode;
	
	private boolean mIsOn;
	private int mOfferCount;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
		mOfferCount = AdManager.offerPointQuery(HomeA.this);
		//		
		findViewById(R.id.exchange_exchange).setOnClickListener(this);
		
		findViewById(R.id.home_offer).setOnClickListener(this);
		//
		findViewById(R.id.test).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AdManager.offerPointAward(HomeA.this, 10);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refreshOfferCount();
		refreshExCount();
		//
		mIsOn = true;
		//
		new Thread(){
			public void run() {
				while(mIsOn){
					SystemClock.sleep(3000);
					//
					int temp = AdManager.offerPointQuery(HomeA.this);
					if(mOfferCount != temp){
						mOfferCount = temp;
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								refreshOfferCount();
							}
						});
					}
				}
			};
		}.start();
		//
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mIsOn = false;
	}
	
	private void refreshOfferCount(){
		((TextView)findViewById(R.id.home_offer_count)).setText(String.valueOf(mOfferCount));
	}
	
	private void refreshExCount(){
		new AsyncHttpHelper().get(this, Config.URL_COUNT, AsyncHttpHelper.NO_CACHE, new AsyncHttpHelper.OnAsyncHttpListener() {
			
			@Override
			public boolean onAsyncHttpVerify(String content) {
				return HomeExS.countVerify(content);
			}
			
			@Override
			public void onAsyncHttpSuccess(String content) {
				((TextView)findViewById(R.id.home_ex_count)).setText(HomeExS.countDecode(content));
			}
			
			@Override
			public void onAsyncHttpFailure(Throwable error, String content) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_offer:
			AdManager.offerOpen(this);
			break;
		case R.id.exchange_exchange:
			onClickExchange();
			break;
		}
	}
	
	private void onClickExchange(){
		new AsyncHttpHelper().get(this, HomeExS.getUrlEx(this), AsyncHttpHelper.NO_CACHE, new AsyncHttpHelper.OnAsyncHttpListener() {
			
			@Override
			public boolean onAsyncHttpVerify(String content) {
				return HomeExS.exchangeVerify(content);
			}
			
			@Override
			public void onAsyncHttpSuccess(String content) {
				//TODO 记录进兑换记录里
				mCode = HomeExS.exchangeDecode(content);
				Toast.makeText(HomeA.this, mCode, Toast.LENGTH_SHORT).show();
				onExchangeVerify();
			}
			
			@Override
			public void onAsyncHttpFailure(Throwable error, String content) {
				if(content.startsWith(Config.PREFIX_EXCHANGE_OUT)){
					Toast.makeText(HomeA.this, "今天已兑换完了，请明天再试", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void onExchangeVerify(){
		String url = null;
		try {
			url = HomeExS.verifyUrl(this, mCode);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println(url);
		new AsyncHttpHelper().get(this, url, AsyncHttpHelper.NO_CACHE, new AsyncHttpHelper.OnAsyncHttpListener() {
			
			@Override
			public boolean onAsyncHttpVerify(String content) {
				return HomeExS.verifyVerify(content);
			}
			
			@Override
			public void onAsyncHttpSuccess(String content) {
				try {
					String[] str = HomeExS.decodeUserAndPw(mCode);
					System.out.println(str[0] + " " + str[1]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onAsyncHttpFailure(Throwable error, String content) {
			}
		});
	}

}
