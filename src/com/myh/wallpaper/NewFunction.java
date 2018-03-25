package com.myh.wallpaper;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.myh.word.MotherPicture;
import com.myh.word.WallPaper;
import com.process.utils.PathFormat;

//
@Controller
public class NewFunction {

	public String root = PathFormat.rootPath(this.getClass());
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	public static String sa = "./WebContent/meinv";
	public static String directory = sa + "/figured/";
	Configuration configuration = new Configuration().configure();
	ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
			.buildServiceRegistry();
	WallPaper wallpaper = null;

	/**
	 * @param url
	 * @param folder
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@RequestMapping("wallPaper")
	public ModelAndView wallPaper(@RequestParam(required = true) String url,
			@RequestParam(required = true) String folder)
			throws FileNotFoundException, IOException, InterruptedException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String locale = (String) request.getSession().getAttribute(url);
		System.out.println(locale);
		String path = root + "WebContent\\meinv" + folder + locale;

		// String sb = locale.substring(0, locale.length() - 4);
		String sb = null;
		if (locale.contains("/"))
			sb = locale.substring(locale.indexOf("/") + 1, locale.length() - 4);
		String img = root + "iphone/" + sb + ".jpg";
		ModelAndView modelx = new ModelAndView("wallPaper");
		System.out.println("path:--------------->" + path);
		System.out.println("img:<-----------------" + img);
		//
		String mb = "./iphone/" + sb + ".jpg";
		modelx.addObject("sb", mb);
		return modelx;
	}

	public int[] getImg(String img) throws FileNotFoundException, IOException {
		File file = new File(img);
		// URL url = new URL(img);
		// URLConnection connection = url.openConnection();
		// connection.setDoOutput(true);
		System.out.println(file.toString());
		FileInputStream fi = new FileInputStream(file);
		BufferedImage image = ImageIO.read(fi);
		int srcWidth = image.getWidth(); //
		int srcHeight = image.getHeight(); //
		// System.out.println("srcWidth = " + srcWidth);
		// System.out.println("srcHeight = " + srcHeight);
		int[] ss = { srcWidth, srcHeight };

		return ss;

	}

	public int[] pictureCut(int x, int y, String ss) throws FileNotFoundException, IOException {

		int[] cc = getImg(ss);
		int d = cc[0] * y / x;
		int[] n = new int[2];
		if (d > cc[1]) {
			d = cc[1] * x / y;
			n[0] = d;
			n[1] = cc[1];
		} else {
			n[1] = d;
			n[0] = cc[0];
		}

		return n;

	}

	public void cutImage(String src, String dest, int[] n) throws IOException {
		try {
			int w = n[0], destW = n[0];
			int h = n[1], destH = n[1];
			int[] ss = getImg(src);
			src = src.replace("\r\n", "");
			dest = dest.replace("\r\n", "");
			System.out.println("CutImage:src=" + src + ";dest=" + dest);
			int[] td = { 0, 0 };
			int[] xd = new int[2];
			xd = FaceTime.getTheXY(src, ss[0], h);
			if (xd == null)
				xd = td;
			System.out.println(xd[0]);
			File file = new File(src);
			if (file.exists()) {
				Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("jpg");
				ImageReader reader = (ImageReader) iterator.next();
				InputStream in = new FileInputStream(src);
				ImageInputStream iis = ImageIO.createImageInputStream(in);
				reader.setInput(iis, true);
				ImageReadParam param = reader.getDefaultReadParam();
				Rectangle rect = new Rectangle(xd[0], 0, w, h);
				param.setSourceRegion(rect);
				BufferedImage bi = reader.read(0, param);
				if (w != destW) {
					bi.flush();
					BufferedImage newImage = new BufferedImage(destW, destH, bi.getType());
					Graphics g = newImage.getGraphics();
					g.drawImage(bi, 0, 0, destW, destH, null, null);
					g.dispose();
					ImageIO.write(newImage, "jpg", new File(dest));
				} else {
					ImageIO.write(bi, "jpg", new File(dest));
				}
				iis.close();
				in.close();
				file.delete();
			} else {
				System.out.println("FileName=" + src);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void gets(String src, String dest, int x, int y, MotherPicture motherPicture)
			throws FileNotFoundException, IOException {
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

		this.cutImage(src, dest, pictureCut(x, y, src));
		wallpaper = new WallPaper(dest, src, new Timestamp(System.currentTimeMillis()), motherPicture);
		session.save(wallpaper);
		transaction.commit();

	}

}
