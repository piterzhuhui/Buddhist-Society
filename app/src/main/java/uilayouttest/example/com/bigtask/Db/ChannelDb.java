package uilayouttest.example.com.bigtask.Db;

import java.util.ArrayList;
import java.util.List;

import uilayouttest.example.com.bigtask.Entity.Channel;

public class ChannelDb {
	
	private static List<Channel>   selectedChannel=new ArrayList<Channel>();
	static{
		selectedChannel.add(new Channel("","悟茶道",0,"",""));
		selectedChannel.add(new Channel("","练身体",0,"",""));
		selectedChannel.add(new Channel("","尝世态",0,"",""));
		selectedChannel.add(new Channel("","品百味",0,"",""));
	}
	public static  List<Channel> getSelectedChannel(){
		 return selectedChannel;
	}
}
