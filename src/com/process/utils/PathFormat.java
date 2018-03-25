package com.process.utils;
public class PathFormat {
//返回项目根目录地址
	public static String rootPath(Class<?> clazz) {
		String nodepath = clazz.getClassLoader().getResource("").getPath();
		// 项目的根目录路径
		String filePath = nodepath.substring(1, nodepath.length() - 14);
		return filePath;

	}

}
