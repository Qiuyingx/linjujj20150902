package cn.dayuanzi.resp.dto;



import cn.dayuanzi.model.Shop;

/**
 * 
* @ClassName: OrderDto 
* @Description: 商品dto 用于积分商城返回
* @author qiuyingxiang
* @date 2015年6月11日 下午1:38:43 
*
 */
public class ShopDto {


	/**
	 * 商品id
	 */
	private long  goodsId;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品价格
	 */
	private int price;
	/**
	 * 库存限制，-1表示无限制
	 */
	private int stockLimit;
	/**
	 * 图片
	 */
	private String  image;
	/**
	 * 商品描述
	 */
	private String remark;
	
	/**
	 * 商品状态，0正常 1下架
	 */
	private int status;
	/**
	 * 多少人购买
	 */
	private long count;
	/**
	 * 商品类型，0兑换类 1 抽奖类
	 */
	private int goodsType;
	
	public ShopDto(){
		
	}
	
	public ShopDto(Shop shop){
		this.goodsId = shop.getId();
		this.goodsName = shop.getGoodsName();
		this.image = shop.getImage();
		this.price = shop.getPrice();
		this.remark = shop.getRemark();
		this.status = shop.getStatus();
		this.stockLimit = shop.getStockLimit();
//		this.goodsType = shop.getGoodsType();
	}
	
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStockLimit() {
		return stockLimit;
	}

	public void setStockLimit(int stockLimit) {
		this.stockLimit = stockLimit;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	
}
