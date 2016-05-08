/**
 * 
 */
package cn.dayuanzi.resp;


import cn.dayuanzi.model.UserOrder;
import cn.dayuanzi.util.DateTimeUtil;

/** 
 * @ClassName: UserOrderResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月11日 下午1:22:35 
 *  
 */

public class UserOrderResp extends Resp{
	/**
	 * 收货人
	 */
	private String userName;
	/**
	 * 联系电话
	 */
	private String tel;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 商品ID
	 */
	private long goods_id;
	/**
	 * 商品名字
	 */
	private String goodsName;
	/**
	 * 商品图片
	 */
	private String image;
	/**
	 * 邻豆商品价格
	 */
	private int   Shopprice;
	/**
	 * 购买数量
	 */
	private int amount;
	/**
	 * 购买时间
	 */
	private String create_time;
	
	public UserOrderResp(){
		
	}
	public  UserOrderResp (UserOrder order){
		this.address = order.getAddress();
		this.amount = order.getAmount();
		this.create_time = DateTimeUtil.formatDateTime(order.getCreate_time());
		this.goods_id =  order.getGoods_id();
		this.tel = order.getTel();
		this.userName = order.getUserName();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(long goods_id) {
		this.goods_id = goods_id;
	}


	public int getShopprice() {
		return Shopprice;
	}

	public void setShopprice(int shopprice) {
		Shopprice = shopprice;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	
}
