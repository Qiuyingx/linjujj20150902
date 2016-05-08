package cn.dayuanzi.service;



import java.util.List;

import cn.dayuanzi.model.Shop;
import cn.dayuanzi.model.UserOrder;
import cn.dayuanzi.resp.OpenTrainResp;
import cn.dayuanzi.resp.ShopListResp;
import cn.dayuanzi.resp.ShopResp;
import cn.dayuanzi.resp.TrainDetailResp;
import cn.dayuanzi.resp.TrainListResp;
import cn.dayuanzi.resp.UserOrderListResp;
import cn.dayuanzi.resp.UserOrderResp;

/**
 * 
 * @author : leihc
 * @date : 2015年6月9日
 * @version : 1.0
 */
public interface IShopService {

	/**
	 * 用户下订单
	 * 
	 * @param userId
	 * @param courtyardId
	 * @param goodsId
	 * @param amount
	 * @param name
	 * @param tel
	 * @param address
	 */
	public UserOrderResp placeOrder(long userId,long courtyardId,String name, String tel, String address,long goodsId,int amount);
	/**
	 * 查询我的兑换列表
	 * @param userId
	 * @return
	 */
	public UserOrderListResp findMyOrders(long userId);
	/**
	 * 我的兑换详情
	 */
	public UserOrderResp findMyOrdersDetail(long ordersId);
	/**
	 * 商品详情
	 * 
	 */
	public ShopResp  getShopDetail(long goodsId);
	/**
	 * 商品列表
	 */
	public ShopListResp getShopList(int  current_page, int  max_num);
	/**
	 *查询上架的商品
	 */
	public List<Shop> findShop(int current_page, int max_num );
	/**
	 * 查询我的订单列表
	 */
	public List<UserOrder> findOrderList(long userId);
	/**
	 * 申请开店
	 */
	public void openShop(Integer category, String introduction,String  tel,long courtyardId,long userId,String mail);
	/**
	 * 学堂列表
	 */
	public TrainListResp trainList(int current_page, int max_num,long courtyardId);
	/**
	 * 学堂详情
	 */
	public TrainDetailResp traindetail(long trainId,long userId);
	/**
	 * 学堂感谢
	 */
	public void trainLaud(long trainId,long userId);
	/**
	 * 我的学堂内的内容
	 */
	public OpenTrainResp getTraininfo (long userId);
	/**
	 * 搜索学堂
	 * @param courtyardId
	 * @param keys 搜索关键字
	 * @return
	 */
	public TrainListResp searchTrain(long courtyardId,String keys);
}
