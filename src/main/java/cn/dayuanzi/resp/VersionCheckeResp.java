package cn.dayuanzi.resp;

import cn.dayuanzi.model.VersionChecke;


/**
 * 版本检查
 * 
 * @author qiuyingxiang
 *
 */
public class VersionCheckeResp extends Resp{
	
	
	/**
	 * 版本号
	 */
	private long versionid;
	/**
	 * 版本名称 
	 */
	private String version ;
	/**
	 * ios版本号
	 */
	private String iosverion;
	/**
	 * ios 地址
	 */
	private String iosaddress;
	
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 版本更新内容
	 */
	private String content;
	/**
	 * IOS更新内容
	 */
	private String iosContent;
	/**
	 * Ios 是否审核通过
	 */
	private boolean pass;
	
	public VersionCheckeResp(){
		
	}
	
	public VersionCheckeResp(VersionChecke ver ){
		this.address = ver.getAddress();
		this.content = ver.getContent();
		this.versionid = ver.getAppVersionid()+1;
		this.version = ver.getAppVersion();
		this.iosaddress = ver.getIosaddress();
		this.iosverion = ver.getIosverion();
		this.iosContent = ver.getIoscontent();
		this.pass = ver.isPass();
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

	public long getVersionid() {
		return versionid;
	}

	public void setVersionid(long  versionid) {
		this.versionid = versionid;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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

	public String getIosContent() {
		return iosContent;
	}

	public void setIosContent(String iosContent) {
		this.iosContent = iosContent;
	}
	
}
