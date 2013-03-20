package com.markupartist.android.widget.actionbar;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateEvent extends Activity {
	Button generateButton;
	EditText eventname;
	EditText store;
	EditText endtime;
	EditText destination;
	
	Socket skt = LoginActivity.skt;
	PrintStream p;
	String name;
	String location;
	String deadline;
	String dest;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        findViews();
        setListener();
        setActionbar();
	}
	
	private void findViews(){
		generateButton = (Button)findViewById(R.id.generate_button);
		eventname = (EditText)findViewById(R.id.text_field_event_name);
		store = (EditText)findViewById(R.id.text_field_store);
		endtime = (EditText)findViewById(R.id.text_field_duetime);
		destination = (EditText)findViewById(R.id.text_field_place);
	};
	
	private void setListener(){
		generateButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				name = eventname.getText().toString();
				location = store.getText().toString();
				deadline = endtime.getText().toString();
				dest = destination.getText().toString();
				
				try {
					p = new PrintStream(skt.getOutputStream());
					p.println("create_event "+name+" "+location+" "+deadline+" "+dest);
					p.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finish();
			}	
		});
	}

	private void setActionbar() {
		// TODO Auto-generated method stub
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.ic_title_home_demo));
        actionBar.setTitle("BunBuy");

        final Action shareAction = new IntentAction(this, createShareIntent(), R.drawable.ic_title_share_default);
        actionBar.addAction(shareAction);
        final Action otherAction = new IntentAction(this, new Intent(this, CreateEvent.class), R.drawable.ic_title_export_default);
        actionBar.addAction(otherAction);
	}
	public static Intent createIntent(Context context) {
        Intent i = new Intent(context, BunbuyActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    private Intent createShareIntent() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Shared from the ActionBar widget.");
        return Intent.createChooser(intent, "Share");
    }
}
