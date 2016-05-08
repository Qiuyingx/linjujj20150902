/**
 * 
 */
package cn.dayuanzi.resp.dto;



import cn.dayuanzi.model.UserOrder;
import cn.dayuanzi.util.DateTimeUtil;

/** 
 * @ClassName: UserOrderDto 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月11日 下午12:08:29 
 *  
 */

public class UserOrderDto {
	
	/**
	 * 兑换id
	 */
	private long id;
	/**
	 * 商品ID
	 */
	private long goods_id;
	/**
	 *商品名字
	 */
	private String goodsName;
	/**
	 * 商品图片
	 */
	private String  goodsImage;
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
	
	public UserOrderDto(){
		
	}
	public  UserOrderDto (UserOrder order){
		this.id = order.getId();
		this.amount = order.getAmount();
		this.create_time = DateTimeUtil.formatDateTime(order.getCreate_time());
		this.goods_id =  order.getGoods_id();
	
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	
	
	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	
}
