package com.markupartist.android.widget.actionbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CheckRequest extends Activity {
	ImageButton freshButton;
	ArrayList<HashMap<String, Object>> listItem;
	SimpleAdapter listItemAdapter;
	ListView list;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_request);
        
        setActionBar();

        list = (ListView) findViewById(R.id.ListView_join_list);
        
        setList();
        
        freshButton = (ImageButton)findViewById(R.id.request_refresh_button);
        freshButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				setList();
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
	
	void setList(){
		listItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<LoginActivity.countRequest;i++){
        	if(LoginActivity.request[i] != null){
	        	StringTokenizer st = new StringTokenizer(LoginActivity.request[i]," ");
	        	HashMap<String, Object> map = new HashMap<String, Object>();
	        	map.put("ItemTitle", st.nextToken());
	        	map.put("ItemText", st.nextToken());
	        	listItem.add(map);
        	}
        }

        listItemAdapter = new SimpleAdapter(this,listItem,
            R.layout.list_items,

            new String[] {"ItemTitle", "ItemText"}, 
            
            new int[] {R.id.ItemTitle,R.id.ItemText}
        );

        list.setAdapter(listItemAdapter);
	}
}
