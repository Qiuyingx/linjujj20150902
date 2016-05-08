package cn.dayuanzi.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.config.TrainTagConfig;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.LindouDetailDao;
import cn.dayuanzi.dao.OpenTrainDao;
import cn.dayuanzi.dao.ShopDao;
import cn.dayuanzi.dao.ShopOfCourtyardDao;
import cn.dayuanzi.dao.TrainInfoDao;
import cn.dayuanzi.dao.TrainLaudDao;
import cn.dayuanzi.dao.TrainViewDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.dao.UserInterestDao;
import cn.dayuanzi.dao.UserOrderDao;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.LindouDetail;
import cn.dayuanzi.model.OpenTrain;
import cn.dayuanzi.model.Shop;
import cn.dayuanzi.model.TrainInfo;
import cn.dayuanzi.model.TrainLaud;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserLinDou;
import cn.dayuanzi.model.UserOrder;
import cn.dayuanzi.model.view.TrainView;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.OpenTrainResp;
import cn.dayuanzi.resp.ShopListResp;
import cn.dayuanzi.resp.ShopResp;
import cn.dayuanzi.resp.TrainDetailResp;
import cn.dayuanzi.resp.TrainListResp;
import cn.dayuanzi.resp.UserOrderListResp;
import cn.dayuanzi.resp.UserOrderResp;
import cn.dayuanzi.resp.dto.LaudListDto;
import cn.dayuanzi.resp.dto.ShopDto;
import cn.dayuanzi.resp.dto.TrainDto;
import cn.dayuanzi.resp.dto.UserOrderDto;
import cn.dayuanzi.service.IShopService;
import cn.dayuanzi.util.YardUtils;

/**
 * 
 * @author : leihc
 * @date : 2015年6月9日
 * @version : 1.0
 */
@Service
public class ShopServiceImpl implements IShopService{

	@Autowired
	private UserOrderDao userOrderDao;
	@Autowired
	private LindouDetailDao lindouDetailDao;
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private ShopOfCourtyardDao shopOfCourtyardDao;
	@Autowired
	private OpenTrainDao openTrainDao;
	@Autowired
	private TrainInfoDao trainInfoDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private CourtYardDao courtYardDao;
	@Autowired
	private TrainLaudDao trainLaudDao;
	@Autowired
	private UserInterestDao userInterestDao;
	@Autowired
	private TrainViewDao trainViewDao;
	/**
	 * @see cn.dayuanzi.service.IShopService#placeOrder(long, long, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	@Transactional
	public UserOrderResp placeOrder(long userId, long courtyardId, String name,
			String tel, String address, long goodsId, int amount) {
		Shop shop =  this.shopDao.get(goodsId) ;
		if(shop ==null ){
			throw new GeneralLogicException("该商品不存在");
		}
		if(shop.getStatus()==1){
			throw new GeneralLogicException("该商品已经下架");
		}
		if(shop.getStockLimit()==0){
			throw new GeneralLogicException("该商品已空");
		}
		List<Long> courtyardIds = this.shopOfCourtyardDao.findCourtyardIdsOfGoods(goodsId);
		if(!courtyardIds.contains(0l)&&!courtyardIds.contains(courtyardId)){
			throw new GeneralLogicException("兑换失败");
		}
		int cost = amount * shop.getPrice();
	
		UserLinDou userLindou = ServiceRegistry.getUserService().getUserLinDou(userId);
		if(cost > userLindou.getAmount()){
			throw new GeneralLogicException("您的邻豆不足啦");
		}
		userLindou.setAmount(userLindou.getAmount()-cost);
		// 添加邻豆变化记录
		this.lindouDetailDao.save(new LindouDetail(userId, -cost, "兑换商品"));
		// 生成订单
		UserOrder order = new UserOrder(userId,courtyardId,name,tel,address,goodsId,amount);
		this.userOrderDao.save(order);
		shop.setStockLimit(shop.getStockLimit()-amount);
		UserOrderResp resp = new UserOrderResp(order);
		resp.setImage(shop.getImage());
		resp.setShopprice(shop.getPrice());
		resp.setGoodsName(shop.getGoodsName());
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IShopService#findMyOrders(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserOrderListResp findMyOrders(long userId) {
		List<UserOrder> ordes = this.findOrderList(userId);
		UserOrderListResp resp = new UserOrderListResp();
		for(UserOrder orde : ordes){
			UserOrderDto dto = new UserOrderDto(orde);
			dto.setGoodsImage(this.shopDao.get(orde.getGoods_id()).getImage());
			dto.setShopprice(this.shopDao.get(orde.getGoods_id()).getPrice());
			dto.setGoodsName(this.shopDao.get(orde.getGoods_id()).getGoodsName());
			resp.getDatas().add(dto);
		}
		return resp;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserOrder> findOrderList(long userId){
		return this.userOrderDao.findOrderList("create_time",false,Restrictions.eq("user_id", userId));
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public UserOrderResp findMyOrdersDetail(long ordersId){
		UserSession userSession = UserSession.get();
		UserOrder userOrder = this.userOrderDao.get(ordersId);
		if(userOrder==null){
			throw new GeneralLogicException("该订单不存在");
		}
		if(userOrder.getUser_id()!=userSession.getUserId()){
			throw new GeneralLogicException("您无权查看其他用户的订单");
		}
		UserOrderResp resp = new UserOrderResp(userOrder);
		resp.setShopprice(this.shopDao.get(userOrder.getGoods_id()).getPrice());
		resp.setImage(this.shopDao.get(userOrder.getGoods_id()).getImage());
		resp.setGoodsName(this.shopDao.get(userOrder.getGoods_id()).getGoodsName());
		return resp ;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public ShopResp  getShopDetail(long goodsId){
		Shop shop =  this.shopDao.get(goodsId) ;
		if(shop ==null ){
			throw new GeneralLogicException("该商品不存在");
		}
		if(shop.getStatus()==1){
			throw new GeneralLogicException("该商品已经下架");
		}
		ShopResp resp = new ShopResp(shop);
		resp.setCount(this.userOrderDao.count(Restrictions.eq("goods_id",shop.getId())));
		resp.setImage(this.shopDao.get(goodsId).getListImage());
		return resp;
	}
	
	
	
	@Override
	@Transactional(readOnly = true)
	public ShopListResp getShopList(int  current_page, int  max_num){
		ShopListResp resp = new ShopListResp();
		List<Shop> shops = this.findShop(current_page, max_num);
		if(CollectionUtils.isNotEmpty(shops)){
			for(Shop shop : shops){
				ShopDto  dto  = new ShopDto(shop);
				if(dto.getStockLimit()==0){
					continue;
				}
				dto.setCount(this.userOrderDao.count(Restrictions.eq("goods_id",shop.getId())));
				dto.setImage(this.shopDao.get(shop.getId()).getImage());
				resp.getDatas().add(dto);
			}
		}
		return resp;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<Shop> findShop(int current_page, int max_num){
		UserSession userSession = UserSession.get();
		List<Long> goodsIds = shopOfCourtyardDao.findGoodsForCourtyardId(userSession.getCourtyardId());
		if(goodsIds.isEmpty() ){
		   return null;
		}
		return shopDao.findForPage(current_page, max_num, Restrictions.in("id", goodsIds),Restrictions.eq("status", 0),Restrictions.ne("stockLimit", 0));
	}  //返回数量大于0，且正常上架的商品。
	
	@Override
	@Transactional(readOnly = false)
	public void openShop(Integer category, String introduction,String  tel,long courtyardId,long userId,String mail){
		OpenTrain ot = this.openTrainDao.findUnique(Restrictions.eq("userId", userId));
		if(ot!=null){
			//是否申请
//			if(this.openTrainDao.findUnique(Restrictions.eq("userId", userId),Restrictions.or(Restrictions.eq("passed",0),Restrictions.eq("passed",1)))!=null){
//				throw new GeneralLogicException("您的申请还在审核中");
//			}
			//是否通过
			if(ot.getPassed()==1){
				throw new GeneralLogicException("您的申请已经通过请按照说明使用该功能");
			}
		}else{
		//申请
			ot = new OpenTrain(category,introduction,tel,courtyardId,userId,mail);
		}
		ot.setPassed(0);
		this.openTrainDao.save(ot);
	}
	
	@Override
	@Transactional(readOnly = true)
	public TrainListResp trainList(int current_page, int max_num,long courtyardId){
		CourtYard courtyard = this.courtYardDao.get(courtyardId);
		if(courtyard==null){
			throw new GeneralLogicException("您还没有院子？快去选个社区");
		}
		
		//找到该城市所有学堂
		TrainListResp resp = new TrainListResp();
		List<TrainView> tvs = this.trainViewDao.getAllByCity(courtyard.getCity_id());
		if(tvs.isEmpty()){
			return resp;
		}
		List<Long> trainIds = new ArrayList<Long>();
		for(TrainView tv : tvs){
			double range = YardUtils.getDistance(courtyard.getLongitude(), courtyard.getLatitude(), tv.getLongitude(), tv.getLatitude());
			BigDecimal df = new BigDecimal(range/1000);
	        double rate = df.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			tv.setDisance(rate);
			trainIds.add(tv.getId());
		}
		Collections.sort(tvs);
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		int firstIndex = ((current_page-1)) * max_num;
		int endIndex = firstIndex + max_num;
		if (firstIndex >= tvs.size()) {
			firstIndex = tvs.size() - 1;
		}
		if (endIndex >= tvs.size()) {
			endIndex = tvs.size();
		}
		tvs = tvs.subList(firstIndex, endIndex);
		trainIds = trainIds.subList(firstIndex, endIndex);
		if(trainIds.isEmpty()){
			return resp;
		}
		List<TrainInfo> trains = this.trainInfoDao.get(trainIds);
		if(trains.isEmpty()){
			return resp;
		}
		Map<Long, TrainInfo> temp = new HashMap<Long, TrainInfo>();
		
		for(TrainInfo train:trains){
			temp.put(train.getId(), train);
		}
		for(TrainView tv : tvs){
			TrainInfo train = temp.get(tv.getId());
			if(train==null){
				continue;
			}
			TrainDto dto = new TrainDto(train); 		 
			User us = this.userDao.get(train.getUserId());
			dto.setHead_icon(us.getHead_icon());
			dto.setRange(tv.getDisance());
			resp.getDatas().add(dto);
		}
		temp = null;
		return resp ;
	}
	
	
	@Override
	@Transactional(readOnly = false)
	public TrainDetailResp traindetail(long trainId,long userId){
		TrainInfo train =  this.trainInfoDao.get(trainId);
		if(train==null){
			throw new GeneralLogicException("该学堂不存在或还未开通");
		}
		//详细的信息
		User user = this.userDao.get(train.getUserId());
		CourtYard cou =this.courtYardDao.get(user.getCourtyard_id());
		TrainDetailResp resp = new TrainDetailResp(train);
		resp.setHead_icon(user.getHead_icon());
		resp.setName(user.getNickname());
		resp.setLaudAmount(this.trainLaudDao.count(Restrictions.eq("train_id", trainId)));
		resp.setLauded(this.trainLaudDao.findUnique(Restrictions.eq("train_id", trainId),Restrictions.eq("user_id", userId))!=null);
		//返回点赞的人
		List<Long> userIds = this.trainLaudDao.laudTrainUser(trainId);
		List<LaudListDto> laudLists = new ArrayList<LaudListDto>();
		if(CollectionUtils.isNotEmpty(userIds)){
			List<User> users = userDao.get(userIds);
		    for(User use : users){
				LaudListDto dto = new LaudListDto(use);
				laudLists.add(dto);
		    }
		    resp.setLaudList(laudLists);
		}
		
		//推荐的学堂
		TrainInfo tr = this.trainInfoDao.findtrain(train.getCategoryName(),userId);
		List<TrainDto> trainlist = new ArrayList<TrainDto>();
		if(tr!=null&&tr.getId()!=trainId){
			TrainDto dto = new TrainDto(tr);
			double range = YardUtils.getDistance(cou.getLongitude(), cou.getLatitude(), tr.getLongitude(), tr.getLatitude());
			BigDecimal df = new BigDecimal(range/1000);
	        double rate = df.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			dto.setHead_icon(this.userDao.get(tr.getUserId()).getHead_icon());
	        dto.setRange(rate);
			trainlist.add(dto);
		}
		List<Integer> interests = this.userInterestDao.findInterests(userId);
		if(!interests.isEmpty()){
			TrainInfo trs = this.trainInfoDao.findtrains(interests);
			if(trs!=null&&trs.getId()!=tr.getId()&&trs.getId()!=trainId){
				TrainDto dtos = new TrainDto(trs);
				double range = YardUtils.getDistance(cou.getLongitude(), cou.getLatitude(), trs.getLongitude(), trs.getLatitude());
				BigDecimal df = new BigDecimal(range/1000);
		        double rate = df.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		        dtos.setHead_icon(this.userDao.get(trs.getUserId()).getHead_icon());
		        dtos.setRange(rate);
		        trainlist.add(dtos);
			}
		}
		resp.setTrainlist(trainlist);
		//更新阅读次数
		train.setViews(train.getViews()+1);
		this.trainInfoDao.saveOrUpdate(train);
		return resp;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void trainLaud(long trainId,long userId){
		TrainInfo Train = this.trainInfoDao.get(trainId);
		if(Train==null){
			throw new GeneralLogicException("该学堂不存在");
		}
		if(this.trainLaudDao.findUnique(Restrictions.eq("train_id", trainId),Restrictions.eq("user_id", userId))!=null){
			throw new GeneralLogicException("你已经感谢过了");
		}
		//红心
		User user = this.userDao.get(userId);
		TrainLaud tr = new TrainLaud(trainId,userId,user.getCourtyard_id());
		this.trainLaudDao.save(tr);
	}
	
	@Override
	@Transactional(readOnly = true)
	public OpenTrainResp getTraininfo (long userId){
		if(this.userDao.get(userId)==null){
			throw new GeneralLogicException("用户不存在，无法查看");
		}
		OpenTrainResp resp = new OpenTrainResp();
		OpenTrain openTrain = this.openTrainDao.findUnique(Restrictions.eq("userId", userId));
		if(openTrain==null){
			resp.setPassed(-1);
			return resp;
		}
		if(openTrain.getPassed()==1){
			resp.setPassed(openTrain.getPassed());
			resp.setEmail(openTrain.getEmail());
			TrainInfo info = this.trainInfoDao.findUnique(Restrictions.eq("userId", userId));
			if(info!=null&&info.getStatus()==1){
				resp.setMyShopId(info.getId());
			}
		}else{
			resp = new OpenTrainResp(openTrain);
			resp.setCategory(TrainTagConfig.getTrainTag().get(openTrain.getCategory()).getName());
			resp.setCategoryId(openTrain.getCategory());
			
		}
		
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IShopService#searchTrain(long, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true,propagation=Propagation.NOT_SUPPORTED)
	public TrainListResp searchTrain(long courtyardId, String keys) {
		TrainListResp resp = new TrainListResp();
		CourtYard courtyard = this.courtYardDao.get(courtyardId);
		List<TrainInfo> infos = this.trainInfoDao.find(Restrictions.eq("cityId", courtyard.getCity_id()),Restrictions.or(Restrictions.like("title", "%"+keys+"%"),Restrictions.like("categoryName", "%"+keys+"%")));
		if(infos.isEmpty()){
			return resp;
		}
		for(TrainInfo train : infos){
			TrainDto dto = new TrainDto(train); 		 
			User us = this.userDao.get(train.getUserId());
			dto.setHead_icon(us.getHead_icon());
			if(courtyard!=null){
				double range = YardUtils.getDistance(courtyard.getLongitude(), courtyard.getLatitude(), train.getLongitude(), train.getLatitude());
				BigDecimal df = new BigDecimal(range/1000);
		        double rate = df.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				dto.setRange(rate);
			}
			resp.getDatas().add(dto);
		}
		if(courtyard!=null){
			Collections.sort(resp.getDatas());
		}
		return resp;
	}
}