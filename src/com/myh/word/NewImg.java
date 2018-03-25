package com.myh.word;

import java.sql.Timestamp;

/**
 * @author Administrator
 *      单个实体的所有图片
 */
public class NewImg {
	private String desc1;
	private Integer id;
	private String url;
	private Timestamp timeStamp;
	private MotherPicture motherPicture;

	public MotherPicture getMotherPicture() {
		return motherPicture;
	}

	public void setMotherPicture(MotherPicture motherPicture) {
		this.motherPicture = motherPicture;
	}

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Integer getId() {
		return id;
	}

	public NewImg(String desc1, String url, Timestamp timeStamp, MotherPicture motherPicture) {
		super();
		this.desc1 = desc1;
		this.url = url;
		this.timeStamp = timeStamp;
		this.motherPicture = motherPicture;
	}

	public NewImg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
