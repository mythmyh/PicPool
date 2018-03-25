package com.myh.word;

import java.sql.Timestamp;
//索引图片类
public class MotherPicture {
	private Integer motherid;//多对1---主键外键id列名不能重复
	private String url;
	private Timestamp timestamp;

	public Integer getMotherid() {
		return motherid;
	}
	public void setMotherid(Integer motherid) {
		this.motherid = motherid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public MotherPicture() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MotherPicture(String url, Timestamp timestamp) {
		super();
		this.url = url;
		this.timestamp = timestamp;
	}
	

}
