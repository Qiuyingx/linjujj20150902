package cn.dayuanzi.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.dayuanzi.pojo.TradeStatus;
import cn.framework.persist.db.VersionPersistentSupport;

/**
 * 用户交易表，保存用户支付物业管理费的订单
 * 
 * @author : leihc
 * @date : 2015年5月4日
 * @version : 1.0
 */
@Entity
@Table(name = "t_user_trade")
public class UserTrade extends VersionPersistentSupport {

	/**
	 * 用户ID
	 */
	private long user_id;
	/**
	 * 院子ID
	 */
	private long courtyard_id;
	/**
	 * 订单号
	 */
	private String trade_id;
	/**
	 * 支付宝平台生成的订单号
	 */
	private String alipay_trade_id;
	/**
	 * 交易状态
	 */
	private TradeStatus trade_status;
	/**
	 * 物业公司的支付宝用户号
	 */
	private String seller_id;
	/**
	 * 物业公司的支付宝账号
	 */
	private String seller_email;
	/**
	 * 用户的支付宝用户号
	 */
	private String buyer_id;
	/**
	 * 用户的支付宝账号
	 */
	private String buyer_email;
	/**
	 * 交易金额
	 */
	private int total_fee;
	
	/**
	 * 订单生成时间
	 */
	private long create_time;

	public UserTrade(){
		
	}
	
	public UserTrade(long user_id, long courtyard_id, String seller_email){
		this.user_id = user_id;
		this.courtyard_id = courtyard_id;
		this.seller_email = seller_email;
		this.trade_id = UUID.randomUUID().toString();
		this.create_time = System.currentTimeMillis();
	}
	
	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}

	public String getAlipay_trade_id() {
		return alipay_trade_id;
	}

	public void setAlipay_trade_id(String alipay_trade_id) {
		this.alipay_trade_id = alipay_trade_id;
	}

	public TradeStatus getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(TradeStatus trade_status) {
		this.trade_status = trade_status;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getBuyer_email() {
		return buyer_email;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public long getCourtyard_id() {
		return courtyard_id;
	}

	public void setCourtyard_id(long courtyard_id) {
		this.courtyard_id = courtyard_id;
	}
	
	
}
