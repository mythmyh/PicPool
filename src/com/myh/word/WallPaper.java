package com.myh.word;

import java.sql.Timestamp;
//单个实体的所有壁纸
public class WallPaper {

	public Integer id;
	public String url;
	public String sourceJpg;
	private Timestamp timestamp;
	private MotherPicture motherPicture;

	public MotherPicture getMotherPicture() {
		return motherPicture;
	}

	public void setMotherPicture(MotherPicture motherPicture) {
		this.motherPicture = motherPicture;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getId() {
		return id;
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

	public String getSourceJpg() {
		return sourceJpg;
	}

	public void setSourceJpg(String sourceJpg) {
		this.sourceJpg = sourceJpg;
	}

	public WallPaper(String url, String sourceJpg, Timestamp tm, MotherPicture motherPicture) {
		super();
		this.url = url;
		this.sourceJpg = sourceJpg;
		this.timestamp = tm;
		this.motherPicture = motherPicture;
	}

	public WallPaper() {
		super();
		// TODO Auto-generated constructor stub
	}

}
