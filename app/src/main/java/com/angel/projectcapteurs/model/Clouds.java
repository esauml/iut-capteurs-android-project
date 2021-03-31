package com.angel.projectcapteurs.model;

import com.google.gson.annotations.SerializedName;

public class Clouds{

	@SerializedName("all")
	private int all;

	public void setAll(int all){
		this.all = all;
	}

	public int getAll(){
		return all;
	}

	@Override
 	public String toString(){
		return 
			"com.angel.projectcapteurs.model.Clouds{" +
			"all = '" + all + '\'' + 
			"}";
		}
}
