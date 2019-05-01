package ltd.coffeeworks.smsblast;

import android.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.provider.*;
import android.support.v7.app.*;
import android.telephony.*;
import android.view.*;
import android.widget.*;
import com.google.android.gms.ads.*;
import java.util.*;


public class MainActivity extends AppCompatActivity
{
	android.widget.Spinner replicate_spinner, delay_spinner;
	List<String> replicate_list, delay_list;
    ArrayAdapter<String> replicateAdapter, delayAdapter;
	private int delay_picked, replicate_picked;
	private String Mobilenumber, ContentMessage;
	private TextView tabula_title, tabula_message;
	private String replicate_pick, delay_pick;
	public static final String signature = "30820270308201d9a003020102020101300d06092a864886f70d01010b0500307d310b30090603550406130250483111300f0603550408130842756b69646e6f6e311830160603550407130f4d616c617962616c61792043697479310f300d060355040b13064465764f707331143012060355040a130b436f66666565776f726b73311a30180603550403131141647269616e204a616e6e204f637461743020170d3138303932313038343433355a180f32313138303832383038343433355a307d310b30090603550406130250483111300f0603550408130842756b69646e6f6e311830160603550407130f4d616c617962616c61792043697479310f300d060355040b13064465764f707331143012060355040a130b436f66666565776f726b73311a30180603550403131141647269616e204a616e6e204f6374617430819f300d06092a864886f70d010101050003818d0030818902818100d45cf1d0385da3295563ec1dab01b0ef65d2f918b4293866e337b840fb0bbd5f3e6b6f0155fec5c60478e6ac6a3c24c3a200b960154ef7dd6323d874a8f1eb0e147cd218688e7719155da4a15f5ad787e5914fe3b39910f92c805308ef9a39f826c63654e9522b8827a7f7ee68cd72fb0721eb8bdd007df9ce3707ee8462dc550203010001300d06092a864886f70d01010b050003818100a9c48185c0bade23141a6616f8005d3c4f3e4e44bcfac0bd1efa671ff9befb97ed844c5d72a1be4e0356a9d6bad1370cdd7c560e8a7cc92267a68c68d897925d4597878f7b5ab7077f09d1281de3ad6c9250b436903e882e15ce54aa69f384adb92ae35ddad3c2cd2c84bad7295ae23fc8b34a05883fb211920a7126c1fa7d32";
	
	public static boolean isTimeAutomatic(Context c) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
		} else {
			return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
		}
	}
	protected void initializeSign(Context con)
	{
		if (getSign(con).equals(signature)) {
			return;
		} else {
			finish();
		}
	}
	public String getSign(Context con)
	{
		StringBuilder str = new StringBuilder();
		try {
			PackageInfo pi = con.getPackageManager().getPackageInfo(con.getPackageName(), con.getPackageManager().GET_SIGNATURES);
			for (Signature signs: pi.signatures) {
				str.append(signs.toCharsString());
			}
		} catch (Exception e) {

		}
		return str.toString();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smsblast);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		TextView AppName = (TextView)findViewById(R.id.appname);
		AppName.setText("SmsBlast");
		Typeface type = Typeface.createFromAsset(getAssets(),"crazy.otf"); 
		AppName.setTypeface(type);
		AppName.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					View v = getLayoutInflater().inflate(R.layout.abc_activity_tabula_rasa, null);
					String title_str = "ABOUT SMSBLAST";
					String mes_str = "Developed by _Dreyannz_" + "\n\n" + "SmsBlast Uses The SmsManager Of Android To Perform Bulk Sending Of Text Messages To A Designated Reciever" +"\n" + "The Developer Of This Application Wont Take Any Responsibilities Of Any Damages Or Any Sorts Of Misuse To This App" + "\n" + "Use At Your Own Risk";
					Button button = (Button) v.findViewById(R.id.sendnow);
					button.setVisibility(8);
					tabula_title = (TextView) v.findViewById(R.id.tabula_rasa_title);
					tabula_title.setText(title_str);
					tabula_message = (TextView) v.findViewById(R.id.tabula_rasa_message);
					tabula_message.setText(mes_str);
					AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
					ab.setCancelable(true);	
					ab.setView(v);
					ab.create().show();
				}
			});
		
			
			
			
		initializeSign(this);
		AdMob();
		if(isTimeAutomatic(this)){}
		else{finish();}
		Button send = (Button)findViewById(R.id.send);
		send.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
						// use this checkSelfPermission method to check whether this permission is already granted or not
						int smsPermission= checkSelfPermission(Manifest.permission.SEND_SMS);
						if (smsPermission!= PackageManager.PERMISSION_GRANTED) {
							// sms permission is not granted then invoke the user to allow this permission
							requestPermissions(new String[]{Manifest.permission.SEND_SMS},0);
						} else if (smsPermission== PackageManager.PERMISSION_GRANTED) {
							// if this permission is granted then call sms sending method
							SmsBlast();
						}
					}else{
						// Below Marshmallow android version no need to handle run time permission so call sms sending method
						SmsBlast();
					}
				}
			});
		
		TextView MobileNumberTitle = (TextView)findViewById(R.id.mobile_number_title);
		MobileNumberTitle.setText("MOBILE NUMBER");
		
		TextView ReplicateTitle = (TextView)findViewById(R.id.replicate_title);
		ReplicateTitle.setText("REPLICATE");
		
		TextView DelayTitle = (TextView)findViewById(R.id.delay_title);
		DelayTitle.setText("DELAY");
		
		TextView MessageTitle = (TextView)findViewById(R.id.message_title);
		MessageTitle.setText("MESSAGE");
		
		replicate_spinner = (android.widget.Spinner)findViewById(R.id.replicate);
		replicate_list = new ArrayList<String>();
		replicate_list.add("5 texts");
		replicate_list.add("10 texts");
		replicate_list.add("15 texts");
		replicate_list.add("20 texts");
		replicate_list.add("30 texts");
		replicate_list.add("50 texts");
		replicateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, replicate_list){
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((android.widget.TextView) v).setTextColor(Color.parseColor("#FFFFFF"));
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#303030"));
                ((android.widget.TextView) v).setTextColor(Color.parseColor("#FFFFFF"));
                return v;
            }
        };
		replicateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        replicate_spinner.setAdapter(replicateAdapter);
        replicate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					replicate_pick  = parentView.getItemAtPosition(position).toString();
					if(replicate_pick.contains("5 texts")){
						replicate_picked = 5;
					}else if(replicate_pick.contains("10 texts")){
						replicate_picked = 10;
					}else if(replicate_pick.contains("15 texts")){
						replicate_picked = 15;
					}else if(replicate_pick.contains("20 texts")){
						replicate_picked = 20;
					}else if(replicate_pick.contains("30 texts")){
						replicate_picked = 30;
					}else if(replicate_pick.contains("50 texts")){
						replicate_picked = 50;
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parentView) {

				}
			});
					
		delay_spinner = (android.widget.Spinner)findViewById(R.id.delay);
		delay_list = new ArrayList<String>();
		delay_list.add("No Delay");
		delay_list.add("2s");
		delay_list.add("3s");
		delay_list.add("5s");
		delay_list.add("10s");
		delay_list.add("15s");
		delay_list.add("30s");
		delay_list.add("60s");
		delayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, delay_list){
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((android.widget.TextView) v).setTextColor(Color.parseColor("#FFFFFF"));
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#303030"));
                ((android.widget.TextView) v).setTextColor(Color.parseColor("#FFFFFF"));
                return v;
            }
        };
		delayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        delay_spinner.setAdapter(delayAdapter);
        delay_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					delay_pick  = parentView.getItemAtPosition(position).toString();
					if(delay_pick.contains("No Delay")){
						delay_picked = 0;
					}else if(delay_pick.contains("2s")){
						delay_picked = 2;
					}else if(delay_pick.contains("3s")){
						delay_picked = 3;
					}else if(delay_pick.contains("5s")){
						delay_picked = 5;
					}else if(delay_pick.contains("10s")){
						delay_picked = 10;
					}else if(delay_pick.contains("15s")){
						delay_picked = 15;
					}else if(delay_pick.contains("30s")){
						delay_picked = 30;
					}else if(delay_pick.contains("60s")){
						delay_picked = 60;
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parentView) {

				}
			});
	}
	@Override
	public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 0: {
					if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						// if user grants the permission on invoking call sms sending method
						SmsBlast();
					} else {
						//This permission is not granted
						return;
					}
				}
		}
	}
	
	private void AdMob(){
		// Admob User ID
		MobileAds.initialize(this, "ca-app-pub-4444654779912706~7874253576");

		// Admob Banner ads
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest); 
		mAdView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {}
				@Override
				public void onAdFailedToLoad(int errorCode) {}
				@Override
				public void onAdOpened() {}
				@Override
				public void onAdLeftApplication() {}
				@Override
				public void onAdClosed() {}
			});

		
		
	}
	
	
	
	
	
	public void SmsBlast(){
        final SmsManager smsManager = SmsManager.getDefault();
		EditText MobileNumber = (EditText)findViewById(R.id.mobile_number);
		Mobilenumber = MobileNumber.getText().toString();
		EditText Message = (EditText)findViewById(R.id.message);
		ContentMessage = Message.getText().toString();
		final int delay_format = delay_picked * 1000;
		
		if(Mobilenumber.isEmpty() && ContentMessage.isEmpty()){
			View v = getLayoutInflater().inflate(R.layout.abc_activity_tabula_rasa, null);
			String title_str = "ERROR";
			String mes_str = "Please Enter A Mobile Number And Message";
			Button button = (Button) v.findViewById(R.id.sendnow);
			button.setVisibility(8);
			tabula_title = (TextView) v.findViewById(R.id.tabula_rasa_title);
			tabula_title.setText(title_str);
			tabula_message = (TextView) v.findViewById(R.id.tabula_rasa_message);
			tabula_message.setText(mes_str);
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setCancelable(true);	
			ab.setView(v);
			ab.create().show();
		}else if(Mobilenumber.isEmpty()){
			View v = getLayoutInflater().inflate(R.layout.abc_activity_tabula_rasa, null);
			String title_str = "ERROR";
			String mes_str = "Please Enter A Mobile Number";
			Button button = (Button) v.findViewById(R.id.sendnow);
			button.setVisibility(8);
			tabula_title = (TextView) v.findViewById(R.id.tabula_rasa_title);
			tabula_title.setText(title_str);
			tabula_message = (TextView) v.findViewById(R.id.tabula_rasa_message);
			tabula_message.setText(mes_str);
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setCancelable(true);	
			ab.setView(v);
			ab.create().show();
		}else if(ContentMessage.isEmpty()){
			View v = getLayoutInflater().inflate(R.layout.abc_activity_tabula_rasa, null);
			String title_str = "ERROR";
			String mes_str = "Please Enter A Message";
			Button button = (Button) v.findViewById(R.id.sendnow);
			button.setVisibility(8);
			tabula_title = (TextView) v.findViewById(R.id.tabula_rasa_title);
			tabula_title.setText(title_str);
			tabula_message = (TextView) v.findViewById(R.id.tabula_rasa_message);
			tabula_message.setText(mes_str);
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setCancelable(true);	
			ab.setView(v);
			ab.create().show();
		}else{
			View v = getLayoutInflater().inflate(R.layout.abc_activity_tabula_rasa, null);
			String title_str = "CONFIRMATION";
			String mes_str = "Mobile Number: " + Mobilenumber + "\n" + "Replicates: " + replicate_pick + "\n" + "Time Delay: " + delay_pick;
			Button button = (Button) v.findViewById(R.id.sendnow);
			tabula_title = (TextView) v.findViewById(R.id.tabula_rasa_title);
			tabula_title.setText(title_str);
			tabula_message = (TextView) v.findViewById(R.id.tabula_rasa_message);
			tabula_message.setText(mes_str);
			final AlertDialog ab = new AlertDialog.Builder(this).create();
			ab.setCancelable(true);	
			ab.setView(v);
			ab.show();
			button.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v){
						ab.dismiss();
						int j=0;
						while( j < replicate_picked ){
							final Handler handler = new Handler();
							handler.postDelayed(new Runnable() {
									@Override
									public void run() {
										smsManager.sendTextMessage(Mobilenumber, null, ContentMessage, null, null);
									}
								}, delay_format);
							j++;
						}
					}
				});
		}
		
    }
}
