package cn.dayuanzi.service.impl;


import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.dayuanzi.config.ExpInfo;
import cn.dayuanzi.config.LindouInfo;
import cn.dayuanzi.config.Settings;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.ExpDetailDao;
import cn.dayuanzi.dao.HouseOwnersDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.dao.UserInterestDao;
import cn.dayuanzi.dao.UserPostDao;
import cn.dayuanzi.dao.ValidateUserDao;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.ExpDetail;
import cn.dayuanzi.model.HouseOwners;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.ValidateUser;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.pojo.ThingsAdder;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.service.IHouseOwnersService;
import cn.dayuanzi.service.INoticeService;
import cn.dayuanzi.util.DateTimeUtil;
import cn.dayuanzi.util.ImageUtil;

/**
 * 
 * @author : leihc
 * @date : 2015年4月19日 上午11:06:58
 * @version : 1.0
 */
@Service
public class HouseOwnersServiceImpl implements IHouseOwnersService {

	@Autowired
	private HouseOwnersDao houseOwnersDao;
	
	@Autowired
	private ValidateUserDao validateUserDao;
	
	@Autowired
	private CourtYardDao couryardDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ExpDetailDao expDetailDao;
	@Autowired
	private INoticeService noticeService;
	@Autowired
	private UserInterestDao userInterestDao;
	@Autowired
	private UserPostDao userPostDao;
	
	
	/**
	 * @see cn.dayuanzi.service.IHouseOwnersService#validateHouseOwner(long, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false)
	public boolean validateHouseOwner(long userId, long courtyardId, Long inviteCode,Integer validateType,CommonsMultipartFile image) {
		CourtYard courtyard = this.couryardDao.get(courtyardId);
		if(courtyard==null){
			throw new GeneralLogicException("没有这个社区");
		}
		User user = this.userDao.get(userId);
		HouseOwners houseOwners = null;
//		houseOwners = this.houseOwnersDao.findHouseOwnerByHouseId(courtyardId,term, building, unit, houseId);
		// 未合作
		if(courtyard.getAlly()==0){
			if(validateType!=1 && validateType!=3){
				throw new GeneralLogicException("参数错误");
			}
			// 图片认证
			if(validateType==1){
				if(image == null){
					throw new GeneralLogicException("请选择图片");
				}
			}else{
				//邀请码认证
				if(inviteCode==null){
					throw new GeneralLogicException("请输入邀请码");
				}else{
					User inviter = this.userDao.get(inviteCode);
					if(inviter==null)
						throw new GeneralLogicException("对不起，邀请码无效");
					if(!validateUserDao.isValidate(inviter.getId(), inviter.getCourtyard_id())){
						throw new GeneralLogicException("对不起，邀请码无效");
					}
					if(courtyardId!=inviter.getCourtyard_id()&&(image==null)){
						throw new GeneralLogicException(Resp.CODE_ERR_INVITATECODE,"你和邀请人不在一个社区，请上传图片认证");
					}
				}
			}
			if(houseOwners == null){
//				houseOwners = new HouseOwners(courtyardId, term, building, unit, houseId);
//				houseOwners.setTerm(term+"期");
//				houseOwners.setBuilding(building+"栋");
//				houseOwners.setUnit(unit+"单元");
//				houseOwners.setHouse(houseId+"号");
//				this.houseOwnersDao.save(houseOwners);
			}
		}else{
			if(houseOwners == null){
				return false;
			}
//			if(StringUtils.isBlank(tel)){
//				throw new GeneralLogicException("请输入业主手机号码");
//			}
		}
		ValidateUser validateUser = this.validateUserDao.findValidateUser(userId, courtyardId);
		if(validateUser!=null){
			if(validateUser.getValidate_status()==1){
				throw new GeneralLogicException("您已经验证过了哦");
			}else{
				validateUser.setValidate_status(0);
			}
		}else{
			ValidateUser lastVu = this.validateUserDao.getLastValidate(userId);
			if(lastVu!=null&&DateTimeUtil.countDays(lastVu.getCreate_time(), System.currentTimeMillis())<Settings.CHANGE_COURTYARD_CYCLE){
				throw new GeneralLogicException("三个月才能更换一次社区哦");
			}
			validateUser = new ValidateUser(userId, courtyardId);
		}
		if(courtyard.getAlly()==0){
			validateUser.setValidate_type(validateType);
			if(validateType==1){
				// 保存用户的头像目录下
				String originalName = image.getOriginalFilename();
				String fileName = String.valueOf("validate_"+System.currentTimeMillis())+originalName.substring(originalName.lastIndexOf('.'));
				validateUser.setAppend(ImageUtil.saveImageInHeadDire(userId, fileName, image));
			}else{
				User inviter = this.userDao.get(inviteCode);
				user.setInviteCode(inviteCode);
				// 和邀请人同一个社区就直接通过验证
				if(inviter.getCourtyard_id()==courtyardId){
					validateUser.setValidate_status(1);
					UserSession.get().setValidate(true);
					// 用户通过社区认证发放邻豆和经验
					LindouInfo info = ThingsAdder.社区认证.getLindouInfo();
					ServiceRegistry.getUserService().addLindou(user.getId(), info.getLindou(), info.getRemark());
					ExpInfo expInfo = ThingsAdder.社区认证.getExpInfo();
					user.addExp(expInfo.getExp());
					ExpDetail exp = new ExpDetail(user.getId(),expInfo.getExp(),expInfo.getRemark());
					this.expDetailDao.save(exp);
					
					String content = "恭喜你！验证成功，获得{0}个经验值，{1}个邻豆";
					content = MessageFormat.format(content, expInfo.getExp(),info.getLindou());
					noticeService.sendNoticeToUser(NoticeType.验证通知, user.getId(), NoticeType.验证通知.getTitle(),content,0);
					
					// 给邀请人发放邻豆经验
					info = ThingsAdder.邀请邻居.getLindouInfo();
					ServiceRegistry.getUserService().addLindou(inviter.getId(), info.getLindou(), info.getRemark());
					expInfo = ThingsAdder.邀请邻居.getExpInfo();
					inviter.addExp(expInfo.getExp());
					ExpDetail exps = new ExpDetail(inviter.getId(),expInfo.getExp(),expInfo.getRemark());
					this.expDetailDao.save(exps);
					
					// 通过验证自动发送一个话题
					// {0}的邻居们，大家好！我叫{1}，很高兴认识大家。喜欢篮球/摄影的可以找我
//					content = "{0}的邻居们，大家好！我叫{1}，很高兴认识大家。";
//					List<Integer> interests = userInterestDao.findInterests(userId);
//					if(!interests.isEmpty()){
//						content += "喜欢";
//						for(Integer interestId : interests){
//							if(InterestConf.getInterests().get(interestId)!=null){
//								content = content +InterestConf.getInterests().get(interestId).getInterest()+"/";
//							}
//						}
//						content = content.substring(0, content.lastIndexOf("/"));
//						content = content +"的可以找我";
//					}
//					content = MessageFormat.format(content, courtyard.getCourtyard_name(),user.getNickname());
//					UserPost userPost = new UserPost(courtyardId,userId, null, content);
//					userPost.setContent_type(ContentType.分享.getValue());
//					userPost.setShow_around(false);
//					this.userPostDao.save(userPost);
				}else{
					// 设置为图片认证
					validateUser.setValidate_type(1);
					String originalName = image.getOriginalFilename();
					String fileName = String.valueOf("validate_"+System.currentTimeMillis())+originalName.substring(originalName.indexOf('.'));
					validateUser.setAppend(ImageUtil.saveImageInHeadDire(userId, fileName, image));
				}
			}
		}else{
			//验证手机
//			if(!tel.equals(houseOwners.getTel())){
//				throw new GeneralLogicException("手机号码验证不正确");
//			}
//			validateUser.setValidate_status(1);
//			UserSession.get().setValidate(true);
//			noticeService.sendNoticeToUser(NoticeType.系统通知, userId, "恭喜你，住址验证成功",0);
		}
		// 切换用户社区
		UserSession.get().setCourtyardId(courtyardId);
		user.setCourtyard_id(courtyardId);
//		user.setHouseowner_id(houseOwners.getId());
		this.userDao.saveOrUpdate(user);
		validateUserDao.save(validateUser);
		return true;
	}
 
	/**
	 * @see cn.dayuanzi.service.IHouseOwnersService#validateHouseOwner(long, long, double, double)
	 */
	@Override
	@Transactional
	public boolean validateHouseOwner(long userId, long courtyardId, double lgt, double lat) {
		CourtYard courtyard = this.couryardDao.get(courtyardId);
		if(courtyard==null){
			throw new GeneralLogicException("没有这个社区");
		}
		User user = this.userDao.get(userId);
		if(courtyard.atRange(Settings.MAP_VALIDATE_DISTANCE*1000, lgt, lat)){
			ValidateUser validateUser = this.validateUserDao.findValidateUser(userId, courtyardId);
			if(validateUser!=null){
				if(validateUser.getValidate_status()==1){
					throw new GeneralLogicException("您已经验证过了哦");
				}else{
					validateUser.setValidate_status(0);
				}
			}else{
				ValidateUser lastVu = this.validateUserDao.getLastValidate(userId);
				if(lastVu!=null&&DateTimeUtil.countDays(lastVu.getCreate_time(), System.currentTimeMillis())<Settings.CHANGE_COURTYARD_CYCLE){
					throw new GeneralLogicException("三个月才能更换一次社区哦");
				}
				validateUser = new ValidateUser(userId, courtyardId);
			}
			// 设置地图验证方式
			validateUser.setValidate_type(4);
			// 设置通过验证
			validateUser.setValidate_status(1);
			UserSession.get().setValidate(true);
			// 用户通过社区认证发放邻豆和经验
			LindouInfo info = ThingsAdder.社区认证.getLindouInfo();
			ServiceRegistry.getUserService().addLindou(user.getId(), info.getLindou(), info.getRemark());
			ExpInfo expInfo = ThingsAdder.社区认证.getExpInfo();
			user.addExp(expInfo.getExp());
			ExpDetail exp = new ExpDetail(user.getId(),expInfo.getExp(),expInfo.getRemark());
			this.expDetailDao.save(exp);
			UserSession.get().setCourtyardId(courtyardId);
			user.setCourtyard_id(courtyardId);
			this.userDao.saveOrUpdate(user);
			validateUserDao.save(validateUser);
			String content = "恭喜你！验证成功，获得{0}个经验值，{1}个邻豆";
			content = MessageFormat.format(content, expInfo.getExp(),info.getLindou());
			noticeService.sendNoticeToUser(NoticeType.系统通知, user.getId(), NoticeType.系统通知.getTitle(),content,0);
			
			// 通过验证自动发送一个话题
			// {0}的邻居们，大家好！我叫{1}，很高兴认识大家。喜欢篮球/摄影的可以找我
//			content = "{0}的邻居们，大家好！我叫{1}，很高兴认识大家。";
//			List<Integer> interests = userInterestDao.findInterests(userId);
//			if(!interests.isEmpty()){
//				content += "喜欢";
//				for(Integer interestId : interests){
//					if(InterestConf.getInterests().get(interestId)!=null){
//						content = content +InterestConf.getInterests().get(interestId).getInterest()+"/";
//					}
//				}
//				content = content.substring(0, content.lastIndexOf("/"));
//				content = content +"的可以找我";
//			}
//			content = MessageFormat.format(content, courtyard.getCourtyard_name(),user.getNickname());
//			UserPost userPost = new UserPost(courtyardId,userId, null, content);
//			userPost.setContent_type(ContentType.分享.getValue());
//			userPost.setShow_around(false);
//			this.userPostDao.save(userPost);
		}else{
			throw new GeneralLogicException(31,"您当前位置没有在所选社区附近，无法通过验证");
		}
		return true;
	}
	
//	@Transactional(readOnly = false)
//	public boolean codevalidate( long userId,String code){
//		User user = this.userDao.get(userId);
//		ValidateUser validateUser = this.validateUserDao.findValidateUser(userId,user.getHouseowner_id());
//		if(validateUser==null){
//			throw new GeneralLogicException("您还没有提交验证请求");
//		}
//		if(validateUser.getValidate_type()!=2){
//			throw new GeneralLogicException("您的验证方式不是code");
//		}
//		if(!code.equals(validateUser.getAppend())){
//			throw new GeneralLogicException("code码不正确");
//		}
//		validateUser.setValidate_status(1);
//		validateUserDao.save(validateUser);
//		noticeService.sendNoticeToUser(NoticeType.系统通知, user.getId(), "恭喜你，住址验证成功",0);
//		return true;
//	}
}
