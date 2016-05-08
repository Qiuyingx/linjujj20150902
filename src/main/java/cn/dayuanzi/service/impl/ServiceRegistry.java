package cn.dayuanzi.service.impl;


import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import cn.dayuanzi.service.IHouseOwnersService;
import cn.dayuanzi.service.IHuanXinService;
import cn.dayuanzi.service.IInvitationService;
import cn.dayuanzi.service.IMessageCheckService;
import cn.dayuanzi.service.IRedisService;
import cn.dayuanzi.service.ITelValidateService;
import cn.dayuanzi.service.IUserInterestService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.service.IValidateUserService;



@Service
public class ServiceRegistry implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		ServiceRegistry.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static SessionFactory getSessionFactory() {
		return applicationContext.getBean(SessionFactory.class);
	}
	
	public static IUserService getUserService(){
		return applicationContext.getBean(UserServiceImpl.class);
	}
	
	public static SmsSenderService getSmsSenderService(){
		return applicationContext.getBean(SmsSenderService.class);
	}
	
	public static ITelValidateService getTelValidateService(){
		return applicationContext.getBean(TelValidateServiceImpl.class);
	}
	
	public static IUserInterestService getUserInterestService(){
		return applicationContext.getBean(UserInterestServiceImpl.class);
	}
	
	public static IHouseOwnersService getHouseOwnersService(){
		return applicationContext.getBean(HouseOwnersServiceImpl.class);
	}
	
	public static IInvitationService getInvitationService(){
		return applicationContext.getBean(InvitationServiceImpl.class);
	}
	
	public static IValidateUserService getValidateUserService(){
		return applicationContext.getBean(ValidateUserServiceImpl.class);
	}
	
	public static IHuanXinService getHuanXinService(){
		return applicationContext.getBean(HuanXinServiceImpl.class);
	}
	
	public static IRedisService getRedisService(){
		return applicationContext.getBean(RedisServiceImpl.class);
	}
	
	public static IMessageCheckService getMessageCheckService(){
		return applicationContext.getBean(MessageCheckServiceImpl.class);
	}
}