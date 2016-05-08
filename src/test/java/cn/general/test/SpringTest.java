package cn.general.test;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.notnoop.apns.APNS;

import cn.dayuanzi.dao.LindouDetailDao;
import cn.dayuanzi.dao.ThankReplyDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.model.LindouDetail;
import cn.dayuanzi.model.User;
import cn.dayuanzi.service.impl.APNSServiceimpl;
import cn.dayuanzi.service.impl.ServiceRegistry;
import cn.dayuanzi.util.ApnsUtil;


/**
 * 
 * @author : lhc
 * @date : 2015年4月10日 下午4:59:21
 * @version : 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback=false )
@ContextConfiguration(locations = { "classpath:application.xml" })
public class SpringTest {
	
//	@Test
//	@Transactional
	public void test(){
		ThankReplyDao userDao = ServiceRegistry.getApplicationContext().getBean(ThankReplyDao.class);
		List<Long> ids = new ArrayList<Long>();
		ids.add(123l);
		ids.add(234l);
		userDao.removeAll(ids);
//		List<User> users = userDao.getAll();
//		User target = ServiceRegistry.getUserService().findUserByTel("15882230323");
//		for(User user:users){
//			ApnsUtil.getInstance().send(user, "哈哈哈"+(++i));
//		}
//		ServiceRegistry.getSmsSenderService().sendMsg("18502882730");
		for(int i=0;i<3;i++){
//			String payload = APNS.newPayload().badge(10).build();
//			ApnsUtil.getInstance().getApnsService().push("1f78092d 2935881d d77f1aef 0b2dfa01 48f9a292 81b1a06a ddfb85dd df2a3a0f", payload);
//			String payload = APNS.newPayload().alertBody("推送消息").build();
//			ApnsUtil.getInstance().getApnsService().push("1f78092d2935881dd77f1aef0b2dfa0148f9a29281b1a06addfb85dddf2a3a0f", payload);
			try {
//				Thread.sleep(1000);
			} catch (Exception e) {
				
			}
		}
		
//		ApnsUtil.getInstance().send(target, "哈哈哈");
		System.out.println("success");
//		try {
//			Thread.sleep(30*1000);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
	}
	
	@Test
	@Transactional
	public void testAddLindou() throws Exception {
		LindouDetailDao lindouDetailDao= ServiceRegistry.getApplicationContext().getBean(LindouDetailDao.class);
		lindouDetailDao.save(new LindouDetail(1002, -5, "帮帮悬赏"));
	}
}
