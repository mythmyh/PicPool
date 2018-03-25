package com.myh.wallpaper;

//屏幕宽高比
public class ScreenP {
	private float height = 1334f;
	private float width = 750f;
	private static float k = 750f/ 1334f;

	public ScreenP() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ScreenP(int height, int width) {
		super();
		this.height = height;
		this.width = width;
		k = width / height;
	}

	public float getHeight() {
		return height;
	}
	public float getWidth() {
		return width;
	}
	public static float getK() {
		return k;
	}


	

}
