package com.junit.test;

import com.myh.word.MotherPicture;

public class F {
	public static void main(String[] args) {
		String nodepath = MotherPicture.class.getClassLoader().getResource("").getPath();  
		// 项目的根目录路径  
		String filePath = nodepath.substring(1, nodepath.length() - 14);  
		System.out.println(nodepath);
	}

}
