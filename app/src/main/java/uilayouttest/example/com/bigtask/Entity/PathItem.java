package uilayouttest.example.com.bigtask.Entity;

public class PathItem {
	public String name;
	public int imageResId;
	public int backgroundResId = -1;
	public PathItem name(String name) {
		this.name = name;
		return this;
	}
	public PathItem imageResId(int imageResId){
		this.imageResId = imageResId;
		return this;
	}
	public PathItem backgroundResId(int backgroundResId){
		this.backgroundResId = backgroundResId;
		return this;
	}
}