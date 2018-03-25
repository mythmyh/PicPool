package com.junit.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.myh.word.MotherPicture;

public class Test1 {


	@Test
	public void check() {
		String nodepath = MotherPicture.class.getClassLoader().getResource("").getPath();  
		// 项目的根目录路径  
		String filePath = nodepath.substring(1, nodepath.length() - 16);  
		System.out.println(nodepath);
	}

}
