package cn.dayuanzi.resp;

/**
 * 用户在交物管费发起订单时，客户端需要先获取相关支付参数
 * 
 * @author : leihc
 * @date : 2015年5月4日
 * @version : 1.0
 */
public class OrderResp extends Resp {

	/**
	 * 合作者身份ID，签约的支付宝账号对应的支付宝唯一用户号
	 */
	private String partner;
	/**
	 * 服务端生成的唯一订单号
	 */
	private String order_id;
	/**
	 * 支付宝服务端需要异步通知的页面路径
	 */
	private String notify_url;
	/**
	 * 商品名称，可以设定为默认值，如“物业费”
	 */
	private String subject;
	/**
	 * 商品详情
	 */
	private String body;
	/**
	 * 物业公司的支付宝账号
	 */
	private String seller_id;
	
	
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	
	
}
