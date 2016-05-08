package cn.dayuanzi.common.huanxin;

/**
 * 
 * @author : leihc
 * @date : 2015年4月26日 上午11:21:50
 * @version : 1.0
 */
public class RequestEntity {

	private String grant_type = "client_credentials";
	
	private String client_id;
	
	private String client_secret;
	
	public RequestEntity(String client_id, String client_secret){
		this.client_id = client_id;
		this.client_secret = client_secret;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}
	
	
}
