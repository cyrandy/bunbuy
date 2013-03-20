package com.markupartist.android.widget.actionbar;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.markupartist.android.widget.actionbar.CustomMenu.OnMenuItemSelectedListener;
import com.markupartist.android.widget.actionbar.Facebook.*;

public class BunbuyActivity extends Activity implements OnMenuItemSelectedListener{
	private CustomMenu mMenu;
	public static final int MENU_ITEM_1 = 1;
	public static final int MENU_ITEM_2 = 2;
	public static final int MENU_ITEM_3 = 3;
	public static final int MENU_ITEM_4 = 4;
	
	ImageButton refresh;
	ListView list;
	ArrayList<HashMap<String, Object>> listItem;
	SimpleAdapter listItemAdapter;
	
	//transmit
	Socket skt;
	PrintStream p;
	ObjectInputStream in;
	Event[] events;
	int countEvents=0;
	String username;
	String userid;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
	        username = bundle.getString("username");
	        userid = bundle.getString("userid");
        }
       
        
        skt = LoginActivity.skt;
        try {
			getEvents();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
      //绑定Layout里面的ListView
        list = (ListView) findViewById(R.id.ListView01);
        
        setListView();
        
        refresh = (ImageButton)findViewById(R.id.image_button_refresh);
        refresh.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				try {
					getEvents();
				} catch (OptionalDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setListView();

			}       	
        });
		
        
        //initialize the menu
        mMenu = new CustomMenu(this, this, getLayoutInflater());
        mMenu.setHideOnSelect(true);
        mMenu.setItemsPerLineInPortraitOrientation(3);
        //mMenu.setItemsPerLineInLandscapeOrientation(8);
        //load the menu items
        loadMenuItems();

        final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.ic_title_home_demo));
        actionBar.setTitle("BunBuy");

        final Action Check_Request_Action = new IntentAction(this, new Intent(BunbuyActivity.this, CheckRequest.class), R.drawable.ic_title_share_default);
        actionBar.addAction(Check_Request_Action);
        final Action create_Event_Action = new IntentAction(this, new Intent(BunbuyActivity.this, CreateEvent.class), R.drawable.ic_title_export_default);
        actionBar.addAction(create_Event_Action);

    }
    
    /**
     * Snarf the menu key.
     */
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	doMenu();
	    	return true; //always eat it!
	    }
		return super.onKeyDown(keyCode, event); 
	} 
	
	/**
     * Load up our menu.
     */
	private void loadMenuItems() {
		//This is kind of a tedious way to load up the menu items.
		//Am sure there is room for improvement.
		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
		CustomMenuItem cmi = new CustomMenuItem();
		cmi.setCaption("查看朋友動向");
		cmi.setImageResourceId(R.drawable.ic_menu_checkout);
		cmi.setId(1);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption("建立動向");
		cmi.setImageResourceId(R.drawable.ic_menu_create_edit);
		cmi.setId(2);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption("晚餐吃什麼？");
		cmi.setImageResourceId(R.drawable.ic_menu_dinner);
		cmi.setId(3);
		menuItems.add(cmi);
		if (!mMenu.isShowing())
		try {
			mMenu.setMenuItems(menuItems);
		} catch (Exception e) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Egads!");
			alert.setMessage(e.getMessage());
			alert.show();
		}
	}
	
	/**
     * Toggle our menu on user pressing the menu key.
     */
	private void doMenu() {
		if (mMenu.isShowing()) {
			mMenu.hide();
		} else {
			//Note it doesn't matter what widget you send the menu as long as it gets view.
			mMenu.show(findViewById(R.id.any_old_widget));
		}
	}

	/**
     * For the demo just toast the item selected.
     */
	/*
	public void MenuItemSelectedEvent(CustomMenuItem selection) {
		Toast t = Toast.makeText(this, "You selected item #"+Integer.toString(selection.getId()), Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}*/
	/*Menu END*/
	public void MenuItemSelectedEvent(CustomMenuItem selection) {
		switch(selection.getId()){
			case 2:
				Intent intent2 = new Intent();
				intent2.setClass(BunbuyActivity.this, CreateTrend.class);
				startActivity(intent2);
				break;
			case 3:
				Intent intent3 = new Intent();
				intent3.setClass(BunbuyActivity.this, WhatForDinner.class);
				startActivity(intent3);
				break;
		}
		
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
    
    
    public void getEvents() throws OptionalDataException, ClassNotFoundException, IOException{
    	countEvents = 0;
    	p = new PrintStream(skt.getOutputStream());
    	p.println("check_trend");
		p.flush();
		in = new ObjectInputStream(skt.getInputStream());  	
		events = (Event[])in.readObject();
		
		for(int i=0;;i++){
			if(events[i] != null){			
				countEvents++;
			}
			else
				break;
		}					
		
		Arrays.sort(events,0,countEvents);
    }
    
    void setListView(){
    	//生成动态数组，加入数据
        listItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<countEvents;i++)
        {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	switch(events[i].type){
    		case 0:
    			map.put("ItemTitle", events[i].owner);
            	map.put("ItemText", "建立了新事件"+events[i].name);
    			break;
    		default:
    			map.put("ItemTitle", events[i].owner);
            	map.put("ItemText", events[i].content);
    	}
        	listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this,listItem,//数据源 
            R.layout.list_items,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"ItemTitle", "ItemText"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.ItemTitle,R.id.ItemText}
        );
       
        //添加并且显示
        list.setAdapter(listItemAdapter);
        
        list.setOnItemClickListener(new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
        		Intent intent = new Intent();
        		Bundle bundle = new Bundle();
				switch(events[arg2].type){
					case 0:					
                        intent.setClass(BunbuyActivity.this, SendRequest.class);
                        bundle = new Bundle();
                        bundle.putString("username",username);
                        bundle.putString("userid",userid);
                        bundle.putString("name", events[arg2].name);
                        bundle.putString("store", events[arg2].location);
                        bundle.putString("ownerIp", events[arg2].ownerIp);
                        intent.putExtras(bundle);
                        startActivity(intent);
						break;
					case 1:
						break;
					case 2:
						Toast.makeText(BunbuyActivity.this, events[arg2].owner+"正從"+events[arg2].content, Toast.LENGTH_SHORT).show();
						break;
				}
			}
		});
    }
}