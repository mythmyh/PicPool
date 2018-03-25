package com.myh.wallpaper;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import com.process.utils.PathFormat;

//人脸识别地址
public class FaceTime {
	public static int[] getTheXY(String arg, int imgWidth, int imgHeight) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String root = PathFormat.rootPath(FaceTime.class);
		System.out.println(new File(root + "opencv\\build\\etc\\lbpcascades\\lbpcascade_frontalface.xml").toString());
		CascadeClassifier faceDetector = new CascadeClassifier(
				root + "WebContent\\etc\\lbpcascades\\lbpcascade_frontalface.xml");
		Mat image = Imgcodecs.imread(arg);
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);
		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
		int width = 0, height = 0, x = 0, y = 0;
		// 如果识别的面部图像大于3,则返回null
		System.out.println(faceDetections.toArray().length);
		if (faceDetections.toArray().length == 0 || faceDetections.toArray().length >= 3) {
			return null;
		}
		// 根据第一个识别到的脸来确定要截取的位置
		Rect[] rects = faceDetections.toArray();
		x = rects[0].x;
		y = rects[0].y;
		width = rects[0].width;
		height = rects[0].height;
		// 求得识别图像的中间x坐标
		int d = (x + x + width) / 2;
		System.out.println("---识别中心-" + d);
		int cultWidth = 0, movePixels = 0;
		float dx = (float) imgWidth / (float) imgHeight;
		System.out.println(dx + "---------->" + ScreenP.getK());
		System.out.println(imgWidth + " " + imgHeight);
		if (dx > ScreenP.getK()) {
			cultWidth = (int) (imgHeight * ScreenP.getK());
			System.out.println(" cultwidth " + cultWidth);
			movePixels = imgWidth - cultWidth;
			System.out.println("从何处裁剪" + movePixels);
		}
		int n = imgWidth / 2 < d ? movePixels : 0;
		return new int[] { n, cultWidth };
	}
}
