package com.markupartist.android.widget.actionbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class LoginActivity extends Activity{
	static Facebook mFacebook;
	static AsyncFacebookRunner mAsyncRunner;
	
    static ServerSocket svrSkt;
    static Socket sktForReqeust;
    static int countRequest=0;
    static String[] request = new String[100];
    static Thread waitForClient;
    static Socket skt;
    
    PrintStream p;
	ObjectInputStream in;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        waitForClient = new Thread(new Runnable(){
			@Override
			public void run() {
				BufferedReader sktReader;
				while(true){
					try {
						sktForReqeust = svrSkt.accept();
						sktReader = new BufferedReader(new InputStreamReader(LoginActivity.sktForReqeust.getInputStream(),"utf-8"));
						request[countRequest] = sktReader.readLine();	
						countRequest++;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}       	
        });
        
 
        mFacebook = new Facebook("490331584314220");
       	mAsyncRunner = new AsyncFacebookRunner(mFacebook);
        
       	mFacebook.authorize( this, new String[] {"publish_stream" },Facebook.FORCE_DIALOG_AUTH,
       	        new Facebook.DialogListener() {
       		
			@Override
			public void onComplete(Bundle values) {
				// TODO Auto-generated method stub
				mAsyncRunner.request("me", new SampleRequestListener());
			}
			@Override
			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
			}
        });
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }
	
	public class SampleRequestListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            try {
                // process the response here: executed in background thread
                Log.d("Facebook-Example", "Response: " + response.toString());
                JSONObject json = Util.parseJson(response);
                final String name = json.getString("name");
                final String id = json.getString("id");

                // then post the processed result back to the UI thread
                // if we do not do this, an runtime exception will be generated
                // e.g. "CalledFromWrongThreadException: Only the original
                // thread that created a view hierarchy can touch its views."
                LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, BunbuyActivity.class);
                        Bundle bundle = new Bundle();
                        try {
                        	svrSkt = new ServerSocket(10101);
                        	waitForClient.start();
                        	skt = new Socket("140.115.50.38",9999);
                			p = new PrintStream(skt.getOutputStream());
                			p.println(id+" "+name);
                			p.flush();
                		} catch (UnknownHostException e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		} catch (IOException e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		}
                        bundle.putString("username",name);
                        bundle.putString("userid",id);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                Log.w("Facebook-Example", "JSON Error in response");
            } catch (FacebookError e) {
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            }
        }
    }
}
