package com.angel.projectcapteurs.model;

import com.google.gson.annotations.SerializedName;

public class Wind{

	@SerializedName("deg")
	private int deg;

	@SerializedName("speed")
	private double speed;

	public void setDeg(int deg){
		this.deg = deg;
	}

	public int getDeg(){
		return deg;
	}

	public void setSpeed(double speed){
		this.speed = speed;
	}

	public double getSpeed(){
		return speed;
	}

	@Override
 	public String toString(){
		return 
			"com.angel.projectcapteurs.model.Wind{" +
			"deg = '" + deg + '\'' + 
			",speed = '" + speed + '\'' + 
			"}";
		}
}
