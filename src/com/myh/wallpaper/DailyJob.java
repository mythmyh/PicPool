package com.myh.wallpaper;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.myh.word.MotherPicture;
import com.myh.word.NewImg;
import com.myh.word.WallPaper;

@Controller
public class DailyJob {
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	@Autowired
	private HttpServletRequest request;
       //查看该模特所有壁纸
	@RequestMapping(value = "wallPaper1")
	public ModelAndView wallPaper(@RequestParam(value = "id", required = false, defaultValue = "1") Integer id) {
		Configuration configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		String hql = "from WallPaper c where c.motherPicture.motherid=?";
		Query query = session.createQuery(hql).setInteger(0, id);
		@SuppressWarnings("unchecked")
		List<WallPaper> list = query.list();
		ModelAndView model1 = new ModelAndView("wallPaper-1");
		// ModelAndView model1 = new ModelAndView("fotuQueryX");

		Iterator<WallPaper> iterator = list.iterator();
		WallPaper as = null;
		while (iterator.hasNext()) {
			as = iterator.next();

			// request.getSession().setAttribute(as.getUrl(),as.getUrl());
		}
		model1.addObject("words", list);
		transaction.commit();
		session.close();
		sessionFactory.close();
		// for(int i=8;i<100;i++){
		// System.out.println(i/5);
		//
		// }
		return model1;
	}
	
    //查看该模特所有图片
	@RequestMapping(value = "jpg")
	public ModelAndView jsp(@RequestParam(value = "id", required = false, defaultValue = "1") Integer id) {
		Configuration configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
		System.out.println("id------------------======="+id);
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		String hql = "from NewImg c where c.motherPicture.motherid=?";
		Query query = session.createQuery(hql).setInteger(0, id);
		@SuppressWarnings("unchecked")
		List<NewImg> list = query.list();
		ModelAndView model1 = new ModelAndView("newimg-1");
		// ModelAndView model1 = new ModelAndView("fotuQueryX");

		Iterator<NewImg> iterator = list.iterator();
		NewImg as = null;
		while (iterator.hasNext()) {
			as = iterator.next();

			// request.getSession().setAttribute(as.getUrl(),as.getUrl());
		}
		model1.addObject("words", list);
		transaction.commit();
		session.close();
		sessionFactory.close();
		// for(int i=8;i<100;i++){
		// System.out.println(i/5);
		//
		// }
		return model1;
	}

	/**
	 * @param id
	 * @return
	 * @throws ClassNotFoundException
	 */
	//查看所有模特详情
	@RequestMapping(value = "/fotuQuery")
	public ModelAndView query(@RequestParam(value = "id", required = false, defaultValue = "1") Integer id)
			throws ClassNotFoundException {
		int pagesize = 7;// 每页显示链接页面数量
		int pageUnit = 5;// 每页显示数据库条目数量
		Configuration configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		String sql = "select count(*) from MotherPicture";
		String sql2 = "select column_name from information_schema.columns where table_schema='hibernate' and table_name='company'";
		Query sqlQuery2 = session.createSQLQuery(sql2);
		@SuppressWarnings("unchecked")
		List<String> list2 = sqlQuery2.list();
		for (String str : list2) {
			System.out.println(str);
		}
		Query sqlquery = session.createSQLQuery(sql);
		int begin, maxPage;
		BigInteger sum = (BigInteger) sqlquery.uniqueResult();
		double dx = sum.doubleValue() / pageUnit;
		dx = Math.ceil(dx);
		maxPage = (int) dx;
		int pagenum = id;
		System.out.println(id > maxPage);
		if (id > maxPage)
			pagenum = 1;
		int[] params = sortMethod(pagenum, pagesize, maxPage);
		// li第一页，li最后一页，物理最后一页
		begin = params[0];// 分页首页
		int sortPage = params[1];
		// 分页末页
		int end = params[2];
		String hql = "from MotherPicture";
		Query query = session.createQuery(hql);
		@SuppressWarnings("unchecked")
		List<MotherPicture> list = query.setFirstResult((pagenum - 1) * pageUnit).setMaxResults(pageUnit).list();
		ModelAndView model1 = new ModelAndView("final");
		// ModelAndView model1 = new ModelAndView("fotuQueryX");

		Iterator<MotherPicture> iterator = list.iterator();
		MotherPicture as = null;
		while (iterator.hasNext()) {
			as = iterator.next();

			// request.getSession().setAttribute(as.getUrl(),as.getUrl());
			request.getSession().setAttribute(as.getMotherid().toString(), as);
		}
		model1.addObject("words", list);
		model1.addObject("total", begin);// ul li开始
		model1.addObject("max", end);
		model1.addObject("sortPage", sortPage);
		model1.addObject("maxPage", maxPage);// ul li最大值
		model1.addObject("pagesize", pagesize);
		transaction.commit();
		session.close();
		sessionFactory.close();
		// for(int i=8;i<100;i++){
		// System.out.println(i/5);
		//
		// }
		return model1;
	}

	/**
	 * @param pageid
	 *            传递过来的requestParam
	 * @param pagesize
	 *            li a 的个数
	 * @param sortPage
	 *            总页数
	 * @return
	 */
	/**
	 * 分页方法，返回一组数据
	 * 
	 * @param pageid
	 * @param pagesize
	 * @param sortPage
	 * @return
	 */
	
	
	public static int[] sortMethod(int pageid, int pagesize, int sortPage) {

		int total = sortPage;
		int end;
		// 底部链接li 的开始
		if (pageid % pagesize == 0) {

			total = pageid - (pagesize - 1);

		} else {
			total = (pageid / pagesize) * pagesize + 1;
		}

		// 底部链接li 的开始
		if (sortPage % pagesize == 0) {

			sortPage = sortPage - (pagesize - 1);

		} else {
			sortPage = (sortPage / pagesize) * pagesize + 1;
		}
		end = total + pagesize - 1;
		int[] params = { total, sortPage, end };
		// li第一页，li最后一组，li最后一页
		System.out.print("----->" + sortPage);
		return params;
	}
	// 最后一页会相等，所以加c:when
}
