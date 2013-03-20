package com.markupartist.android.widget.actionbar;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class SendRequest extends Activity { 
	private ArrayAdapter<CharSequence> adapter;  
	private ArrayAdapter<CharSequence> adapter2;
	
	Button sendButton;
	TextView storeT;
	EditText mealE;
	
	
	Socket skt = LoginActivity.skt;
	Socket sktOwner;
	PrintStream toServer;
	PrintStream toOwner;
	
	String ownerIp;
	String name;
	String store;
	String dest;
	String meal;
	String username;
	String userid;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_request);
        
        try {
        	sktOwner = new Socket(ownerIp,10101);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        Bundle bundle = this.getIntent().getExtras();
        username = bundle.getString("username");
        userid = bundle.getString("userid");
        name = bundle.getString("name");
        store = bundle.getString("store");
        
        findViews();
        setSpinner();
        setActionBar();
	}
	private void findViews() {
		// TODO Auto-generated method stub
		storeT = (TextView)findViewById(R.id.store);
		storeT.setText(store);
		mealE = (EditText)findViewById(R.id.text_field_order);
		sendButton = (Button)findViewById(R.id.send_button);
	}
	private void setSpinner() {
		// TODO Auto-generated method stub
		adapter = ArrayAdapter.createFromResource(this, R.array.shop, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		sendButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					toServer = new PrintStream(skt.getOutputStream());
					toServer.println("join_event "+name);
					toServer.flush();
					
					meal = mealE.getText().toString();
					
					toOwner = new PrintStream(sktOwner.getOutputStream());
					toOwner.println(username+" "+meal+" "+dest);
					toOwner.flush();
					
					finish();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		});
	}

	private void setActionBar() {
		// TODO Auto-generated method stub
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.ic_title_home_default));
        actionBar.setTitle("BunBuy");

	}
	public static Intent createIntent(Context context) {
        Intent i = new Intent(context, BunbuyActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}
