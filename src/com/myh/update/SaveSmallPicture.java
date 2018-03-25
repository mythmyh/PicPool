package com.myh.update;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import com.myh.wallpaper.NewFunction;
import com.myh.word.MotherPicture;
import com.myh.word.NewImg;
import com.process.utils.PathFormat;
/**
 * @author Administrator
 *  可以单独运行的更新图片库的类
 */ 
/**
 * @author Administrator
 *
 */
public class SaveSmallPicture {
	String dir2 = PathFormat.rootPath(this.getClass());

	int[] jk = new int[2];
	MotherPicture motherPicture = null;
	NewFunction fs = new NewFunction();
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	public static String sa = "./WebContent/meinv";
	public static String directory = sa + "/figured/";
	private static int count = 0;
	public String se;
	public String str;
	String fileDir = null;
	String smallPrefix = null;
	String ti = "/figured/";// 目录类别
	String finalUrl = null;// 要保存到数据库的图片地址
	Configuration configuration = new Configuration().configure();
	ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
			.buildServiceRegistry();

	public static String getOneHtml(String urlString) {
		InputStreamReader in = null;
		;
		try {
			in = new InputStreamReader(new URL(urlString).openStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			return null;
		}
		// read contents into string buffer
		StringBuilder input = new StringBuilder();
		int ch;
		try {
			while ((ch = in.read()) != -1)
				input.append((char) ch);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		// System.out.println(input);
		return input.toString();
	}

	/**
	 * 完整采集这个栏目的十个list页面 无论传给的参数是第几页，都会从第一页开始
	 * 
	 * @param tt
	 *            要采集的网址
	 * @throws Exception
	 */
	public void cyclicCopy(String tt) throws Exception {
		String ss = null;
		this.newFilter2(tt);
		if (tt.contains("zt")) {// 采集10个List页面
			for (int i = 2; i < 400; i++) {
				System.out.println("准备扫描第" + i + "页");
				ss = tt.substring(0, tt.length() - 5) + "/" + i + ".html";
				this.newFilter2(ss);
			}

		} else {
			for (int i = 2; i < 400; i++) {
				System.out.println("准备扫描第" + i + "页");
				ss = tt + "list_" + i + ".html";
				this.newFilter2(ss);
			}
		}

	}

	/**
	 * @param ss
	 *            url网址，纯图片网址
	 * @param newName
	 *            要保存的文件名,即*.jpg;
	 * @throws Exception
	 */
	// 在这里加入壁纸制作功能
	public void saveSmallPicture(String ss, String newName) throws Exception {

		URL url = new URL(ss);
		// 打开链接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (conn == null) {

			return;
		}

		// 设置请求方式为"GET"

		// 超时响应时间为5秒
		conn.setRequestMethod("GET");
		if (conn.getContentType().equals("text/html")) {

			return;
		}
		conn.setConnectTimeout(25 * 1000);
		InputStream inStream = conn.getInputStream();

		// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
		byte[] data = readInputStream(inStream);
		// new一个文件对象用来保存图片，默认保存当前工程根目录
		File imageFile = new File(newName);
		// 创建输出流
		if (imageFile.exists()) {
			// System.out.println("发现重复字段!");
			return;
		} else {
			FileOutputStream outStream = new FileOutputStream(imageFile);
			outStream.write(data);

			// 关闭输出流
			outStream.close();
		}

	}

	// 写入数据

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流s
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}

	/**
	 * @param z1要过滤的字符串
	 * @return过滤掉字符串的双引号，即纯网址
	 */
	private String shuangYinHao(String z1) {
	
		String c = null;
		Pattern x = Pattern.compile("img src=\"(.*?)\"");
		Matcher m = x.matcher(z1);
		while (m.find()) {
			c = m.group();
		}
		
		return shuangYinHaoFinal(c);

	}

	private String shuangYinHaoFinal(String z1) {

		String c = null;
		Pattern x = Pattern.compile("\"(.*?)\"");
		if (z1 == null)
			return null;
		Matcher m = x.matcher(z1);
		while (m.find()) {
			c = m.group();
		}
		return c;

	}

	public static void main(String[] args) throws Exception {
		SaveSmallPicture ssp = new SaveSmallPicture();
		if (args.length != 0) {
			if (args[0].contains("list"))
				ssp.newFilter2(args[0]);
			else {
				String p = ssp.processPrefix(args[0]);

				if (p == null) {

					ssp.newFilter2(args[0]);

				} else {
					Integer t = Integer.parseInt(p);
		
					if (t < 100) {
						ssp.newFilter2(args[0]);
					}

					else

						ssp.processHtmlJPG(args[0]);
				}

			}

		} else
			ssp.cyclicCopy("https://www.uumnt.com/meinv/");

	}

	{
		File s = new File(sa);
		if (!s.exists()) {
			s.mkdirs();
		}
		if (!s.exists())
			throw new RuntimeException("不能创建文件");
		File b = new File(directory);
		if (!b.exists()) {
			b.mkdirs();
		}
		if (!b.exists())
			throw new RuntimeException("不能创建文件");
	}

	public SaveSmallPicture() {
		super();
		// TODO Auto-generated constructor stub

	}

	public void newFilter2(String s1) throws Exception {

		String chinese = null, es = null;

		String ss = getOneHtml(s1);
		
		
		Pattern p1 = Pattern.compile("\t|\r|\n");
		Matcher m2 = p1.matcher(ss);
		String dest = m2.replaceAll("");
		// <a href=(.*?)title(.*?)(?=.*(img src)).*?</a>
		Pattern p = Pattern.compile("<a href=\"(?=.*(title))(?=.*(img src)).*?</a>");
		Matcher m = p.matcher(dest);
		while (m.find()) {
			str = m.group();
			if (str.contains("img src")) {
			
				Pattern p3 = Pattern.compile("\\\"[/\\w.:/\\u4e00-\\u9fa5]+\\\"");
				Matcher m3 = p3.matcher(str);
				List<String> list = new LinkedList<String>();
				String cs = null;
				while (m3.find()) {

					String bs = m3.group().replaceAll("\"", "");

					list.add(bs);
				}

				for (String bs : list) {
					if (bs.endsWith(".html")) {
						es = "https://www.uumnt.com" + bs;
						String pi = es;
						ti = processTitle(pi);
						se = sa + ti;

					}

				}

				for (String bs : list) {
					if (bs.startsWith("http") && bs.endsWith(".jpg")) {

						list.remove(bs);
						smallPrefix = bs.substring(bs.length() - 36, bs.length());
						cs = se + "/" + smallPrefix;

						fileDir = cs.substring(0, cs.length() - 4);

						File file = new File(fileDir);
                        //生产文件路径
						if (!file.exists()) {
							file.mkdirs();

						} else {
							count++;
							//判断是否重复采集
							if (count >= 3) {
								System.out.println("内容已经采集过！");
								System.exit(1);
							}
						}
						saveSmallPicture(bs, cs);
						sessionFactory = configuration.buildSessionFactory(serviceRegistry);
						session = sessionFactory.openSession();
						transaction = session.beginTransaction();
						motherPicture = new MotherPicture(cs, new Timestamp(System.currentTimeMillis()));
						
						//存入数据库
						session.save(motherPicture);
						transaction.commit();
					}

				}
				for (String bs : list) {
					if (chineseHave(bs)) {

						chinese = chinese(bs);
					}

				}
				directory = fileDir;
				if (es != null) {
					processHtmlJPG(es);
				}
				list = null;

			}
		}
		directory = dir2+"meitu/figured/";
	}

	public String clearHtml(String s1) {
		try {
			String ss;
			ss = getOneHtml(s1);
			if (ss == null)
				return null;
			Pattern p1 = Pattern.compile("\t|\r|\n");
			Matcher m2 = p1.matcher(ss);
			String dest = m2.replaceAll("");
			return dest;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 加入了中文判断，使得正则表达式更加精准
	 * 
	 * @param ss
	 * @param prefix
	 * @return
	 */

	public void Filter3(String ss, String prefix) throws Exception {

		smallPrefix = smallPrefix.substring(0, smallPrefix.length() - 4);
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

		String se = "", x = "", j = "", t = "";
		if (this.chineseHave(ss)) {
			se = this.chinese(ss);
		
		}
		t = processPrefix(prefix);
		// String js = "<a href=\"(.*?)" + t + "(.*?)(img src)(.*?)" + se +
		// "(.*?).*?</a>";
		// String js = "(<a href=\"(.*?)" + t + "(?=.*(img src))(.*?)(?=.*(" +
		// se + ")).*?</a>)"; 原版
		String js = "(<a href=\"(.*?)" + t + "(?=.*(img src))(.*?)(?=.*(" + se + ")).*?</a>)";
		// String js = "((<a href=\"){1}.*?" + t + "(?=.*(img src))(.*?)(?=.*("
		// + se + ")).*?</a>)";
		Pattern p = Pattern.compile(js);
		Matcher m = p.matcher(ss);
		if (m.find()) {
			x = shuangYinHao(m.group()).replaceAll("\"", "");
			finalUrl = t + "1.jpg";
			NewImg im = new NewImg(ti, smallPrefix + "/" + finalUrl, new Timestamp(System.currentTimeMillis()),
					motherPicture);
			session.save(im);
			transaction.commit();
			String newName = directory + "/" + finalUrl;
			saveSmallPicture(x, newName);

			String sb = null;

			String img = dir2+"WebContent\\iphone\\" + finalUrl;
			File file = new File(newName);
			if (file.exists()) {
				jk = fs.getImg(newName);
				if (jk[1] > jk[0])
					fs.gets(dir2 + newName.substring(2, newName.length()), img, 750, 1334, motherPicture);
			}
			c: for (int i = 2; i < 200; i++) {
				j = rech(i, prefix);
				String url = getPictureUrl(j, se);
				if (url == null) {
					break c;
				}

				String jpgurl = shuangYinHao(url).replaceAll("\"", "");
				String oldName = directory + "/" + t + i + ".jpg";
				saveSmallPicture(jpgurl, oldName);

				sb = null;

				sb = t + i;
			
				img = dir2+"WebContent\\iphone\\" + sb + ".jpg";
				File file2 = new File(oldName);
				if (file2.exists()) {
					jk = fs.getImg(oldName);
					if (jk[1] > jk[0])
						fs.gets(dir2 + oldName.substring(2, oldName.length()), img, 750, 1334, motherPicture);
				}
				transaction = session.beginTransaction();
				finalUrl = t + i + ".jpg";
				im = new NewImg(ti, smallPrefix + "/" + finalUrl, new Timestamp(System.currentTimeMillis()),
						motherPicture);
				session.save(im);
				transaction.commit();

			}

		}
		session.close();
		sessionFactory.close();
	}

	/**
	 * 把得到的图片过滤为-行
	 * 
	 * @param ss
	 * @throws Exception
	 */
	public void processHtmlJPG(String ss) throws Exception {

		String prefix = null;
		// System.out.println(371 + ss);
		if (ss.contains("_")) {
			prefix = processString(ss);
			// System.out.println(374 + prefix);
		} else
			prefix = ss;
		String js = getOneHtml(prefix);
		Pattern p1 = Pattern.compile("\t|\r|\n");
		Matcher m2 = p1.matcher(js);
		String dest = m2.replaceAll("");//
		Filter3(dest, prefix);
	}

	public String chinese(String ss) {
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher m = p.matcher(ss);
		while (m.find()) {

			return m.group();
		}
		return null;
	}

	public boolean chineseHave(String ss) {
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher m = p.matcher(ss);
		if (m.find()) {
			return m.find();
		}
		return false;

	}

	public String rech(int i, String es) {

		StringBuffer s = new StringBuffer(es);
		s.insert(s.length() - 5, "_");
		s.insert(s.length() - 5, i);
		// 获取并整理s网址
		return s.toString();

	}

	/**
	 * @param s1
	 *            被清洗过的数据流（去掉换行空格）
	 * @param se中文标题，用来确定图片位置
	 * @return 纯图片网址
	 */
	public String getPictureUrl(String s1, String se) {
		String ki = s1;
		String kt = processTitle(ki);
		String kp = clearHtml(s1);
		if (kp == null)
			return null;
		String js = "<a href=\"" + kt + "(.*?)img src(.*?)" + se + "(.*?)</a>";
		Pattern p = Pattern.compile(js);
		Matcher m = p.matcher(kp);
		while (m.find()) {
			return m.group();
		}
		return null;
	}

	// 返回纯数字，从内容里检索数字
	public String processPrefix(String ss) {
		if (ss == null)
			return null;

		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(ss);
		while (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	public String processPrefix2(String ss) {
		if (ss == null)
			return null;

		Pattern pattern = Pattern.compile("/\\d+");
		Matcher matcher = pattern.matcher(ss);
		while (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	public static String processTitle(String ss) {
		String kk = ss;
		String j = null;
		if (ss == null)
			return null;
		Pattern pattern = Pattern.compile("\\/\\w+\\/\\d+");

		Matcher matcher = pattern.matcher(kk);
		if (matcher.find()) {

			j = matcher.group();
			Pattern p = Pattern.compile("\\/[\\w]+\\/");
			Matcher m = p.matcher(j);
			if (m.find()) {
				return m.group();
			}
		} else {

			Pattern p = Pattern.compile("\\/[a-zA-z]+\\.html");
			Matcher m = p.matcher(ss);
			while (m.find()) {

				return m.group().substring(0, m.group().length() - 5) + "/";
			}

			Pattern p2 = Pattern.compile("\\/[a-zA-z]+\\/");
			Matcher m3 = p2.matcher(ss);
			while (m3.find()) {
				return m3.group();
			}
		}
		return null;

	}

	/**
	 * @param ss对给定的JPG页面处理
	 * @return
	 */
	public String processString(String ss) {
		String tr = ss;
		Pattern p = Pattern.compile("_\\d+");
		Matcher m = p.matcher(tr);

		return m.replaceAll("");

	}
}
