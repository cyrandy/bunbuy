package com.markupartist.android.widget.actionbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheckCreatedActivity extends Activity{
	Button join;
	TextView event;
	BufferedReader sktReader;
	Socket skt;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_createdevent);
        
        try {
			skt = new Socket("127.0.0.1",10101);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        findViews();
        setListener();
        
        /*new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					try {
						sktReader = new BufferedReader(new InputStreamReader(LoginActivity.sktForReqeust.getInputStream(),"utf-8"));
						Log.d("asd",sktReader.readLine());	
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
			}        	
        }).start();   */
	}
	
	private void findViews(){
		 join = (Button)findViewById(R.id.button1);
	     event = (TextView)findViewById(R.id.textView1);
	}
	
	private void setListener(){
		join.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				try {				
					PrintStream p = new PrintStream(skt.getOutputStream());
					p.println("testtttt");
					p.flush();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
		});
	}
}
