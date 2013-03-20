package com.markupartist.android.widget.actionbar;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.markupartist.android.widget.actionbar.CustomMenu.OnMenuItemSelectedListener;


public class WhatForDinner extends Activity implements OnMenuItemSelectedListener{
	
	private TextView view_result;
	private Button generate_button;
	private CustomMenu mMenu;
	public static final int MENU_ITEM_1 = 1;
	public static final int MENU_ITEM_2 = 2;
	public static final int MENU_ITEM_3 = 3;
	public static final int MENU_ITEM_4 = 4;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whatfordinner);
        findViews();
        setListeners();
        setActionbar();
      //initialize the menu
        mMenu = new CustomMenu(this, this, getLayoutInflater());
        mMenu.setHideOnSelect(true);
        mMenu.setItemsPerLineInPortraitOrientation(3);
        //mMenu.setItemsPerLineInLandscapeOrientation(8);
        //load the menu items
        loadMenuItems();
    }
    private void setActionbar() {
		// TODO Auto-generated method stub

        final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.ic_title_home_demo));
        actionBar.setTitle("BunBuy");

        final Action shareAction = new IntentAction(this, createShareIntent(), R.drawable.ic_title_share_default);
        actionBar.addAction(shareAction);
        final Action otherAction = new IntentAction(this, new Intent(this, OtherActivity.class), R.drawable.ic_title_export_default);
        actionBar.addAction(otherAction);
	}
	private void setListeners() {
		// TODO Auto-generated method stub
    	generate_button.setOnClickListener(listener_f);
	}
	private void findViews(){
    	view_result = (TextView)findViewById(R.id.result);
    	generate_button = (Button)findViewById(R.id.generate_button);
    }
	private OnClickListener listener_f = new OnClickListener() {
		public void onClick(View v) {
			view_result.setText(generate());
		}
	};
	public CharSequence generate() {
		// TODO Auto-generated method stub
		CharSequence result="0";
		Random randomGenerator = new Random();
	    int randomInt = randomGenerator.nextInt(10);
	    switch(randomInt){
	    	case 0:
	    		result= "歐姆萊斯";
	    		break;
	    	case 1:
	    		result= "紅油抄手";
	    		break;
	    	case 2:
	    		result= "照燒排骨";
	    		break;
	    	case 3:
	    		result= "竹香快餐";
	    		break;
	    	case 4:
	    		result= "香城燒臘";
	    		break;
	    	case 5:
	    		result= "捷捷早餐";
	    		break;
	    	case 6:
	    		result= "阿默早餐店";
	    		break;
	    	case 7:
	    		result= "八方雲集";
	    		break;
	    	case 8:
	    		result= "拉亞早餐";
	    		break;
	    	case 9:
	    		result= "韓林泡菜";
	    		break;
	    }
	    return result;
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
			mMenu.show(findViewById(R.id.any_old_widget2));
		}
	}
	public void MenuItemSelectedEvent(CustomMenuItem selection) {
		Toast t = Toast.makeText(this, "You selected item #"+Integer.toString(selection.getId()), Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
		
	}
}
