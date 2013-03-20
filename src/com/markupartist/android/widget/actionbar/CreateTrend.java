package com.markupartist.android.widget.actionbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class CreateTrend extends Activity {
	private Spinner spinner;  
	private Spinner spinner2;  
	private Button generateButton;
	private CheckBox toFB;
	private ArrayAdapter<CharSequence> adapter;  
	private ArrayAdapter<CharSequence> adapter2;  
	
	//transmit
	Socket skt = LoginActivity.skt;
	PrintStream p;
	String from="";
	String to="";
	boolean toFacebook=false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_trend);
        findViews();
        setListener();
        setSpinner();
        setActionbar();
	}
	
	private void setListener(){
		generateButton.setOnClickListener(new Button.OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					p = new PrintStream(skt.getOutputStream());
					p.println("moving "+from+" "+to);
					p.flush();
					
					if(toFacebook){
						//LoginActivity.mFacebook.dialog(CreateTrend.this, "feed",new SampleDialogListener());
						
						Bundle params = new Bundle();
						  params.putString("message", "正在從 "+from+" 到 "+to+" 的路上");
						  try {
							  LoginActivity.mFacebook.request("/me/feed", params, "POST");
						  } catch (FileNotFoundException e1) {
						   // TODO Auto-generated catch block
						   e1.printStackTrace();
						  } catch (MalformedURLException e1) {
						   // TODO Auto-generated catch block
						   e1.printStackTrace();
						  } catch (IOException e1) {
						   // TODO Auto-generated catch block
						   e1.printStackTrace();
						  }
					}
					
					finish();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		});
		
		toFB.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(toFB.isChecked())
					toFacebook=true;
				else
					toFacebook=false;
			}
			
		});
	}
	
	private void setSpinner() {
		// TODO Auto-generated method stub
		adapter = ArrayAdapter.createFromResource(this, R.array.plantes, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView adapterView, View view, int position, long id){
				from = adapterView.getSelectedItem().toString();
			}
			@Override
        		public void onNothingSelected(AdapterView arg0) {
			}
		});
		spinner.setVisibility(View.VISIBLE);
		adapter2 = ArrayAdapter.createFromResource(this, R.array.plantes, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView adapterView, View view, int position, long id){
				to = adapterView.getSelectedItem().toString();
			}
			@Override
        	public void onNothingSelected(AdapterView arg0) {
			}
		});
		spinner2.setVisibility(View.VISIBLE);
	}
	private void findViews() {
		// TODO Auto-generated method stub
		spinner = (Spinner) findViewById(R.id.Spinner01);
		spinner2 = (Spinner) findViewById(R.id.Spinner02);
		generateButton = (Button)findViewById(R.id.generate_button);
		toFB = (CheckBox)findViewById(R.id.checkBoxFB);
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
