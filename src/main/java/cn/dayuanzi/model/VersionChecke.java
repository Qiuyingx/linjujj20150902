package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.VersionPersistentSupport;

@Entity
@Table(name = "t_version")
public class VersionChecke extends VersionPersistentSupport{
	
	/**
	 * 版本Id
	 */
	private int  appVersionid;
	/**
	 * 版本号 
	 */
	private String appVersion ;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 版本更新内容
	 */
	private String content;
	
	/**
	 * ios版本号
	 */
	private String iosverion;
	/**
	 * ios 地址
	 */
	private String iosaddress;
	/**
	 * 更新内容
	 */
	private String ioscontent;
	/**
	 * Ios 是否审核通过
	 */
	private boolean pass;
	
	
	public VersionChecke(){
		
	}
	
	
	public boolean isPass() {
	    return pass;
	}


	public void setPass(boolean pass) {
	    this.pass = pass;
	}


	public String getIosverion() {
	    return iosverion;
	}


	public void setIosverion(String iosverion) {
	    this.iosverion = iosverion;
	}


	public String getIosaddress() {
	    return iosaddress;
	}


	public void setIosaddress(String iosaddress) {
	    this.iosaddress = iosaddress;
	}


	public int getAppVersionid() {
		return appVersionid;
	}

	public void setAppVersionid(int appVersionid) {
		this.appVersionid = appVersionid;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getIoscontent() {
		return ioscontent;
	}

	public void setIoscontent(String ioscontent) {
		this.ioscontent = ioscontent;
	}
	
}
