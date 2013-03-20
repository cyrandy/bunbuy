package com.markupartist.android.widget.actionbar;

import java.io.Serializable;

public class Event  implements Serializable,Comparable<Object>{
	private static final long serialVersionUID = 1L;
	int type; //0:bunbuy 1:join //2:move
	
	long time;
	String owner;
	String ownerIp;
	String name;
	String location;
	String deadline;
	String dest;
	String from;
	String to;
	String meal;
	String content;
	
	
	Event(int type,String name,String location,String deadline,String dest,String owner,String ownerIp){
		time = System.currentTimeMillis();
		this.type = type;
		this.name = name;
		this.location = location;
		this.deadline = deadline;
		this.owner = owner;
		this.ownerIp = ownerIp;
	}
	
	Event(int type,String name,String location,String meal){
		time = System.currentTimeMillis();
		this.type = type;
		this.name = name;
		this.location = location;
		this.meal = meal;
	}
	
	Event(int type,String owner,String content){
		time = System.currentTimeMillis();
		this.type = type;
		this.owner = owner;
		this.content = content;
	}

	@Override
	public int compareTo(Object arg0) {
		Event obj = (Event)arg0;
		if(this.time > obj.time) return -1;
		else if(this.time < obj.time) return 1;
		else return 0;
	}
}
