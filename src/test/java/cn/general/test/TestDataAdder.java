package cn.general.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.config.CareerConfig;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.HouseOwnersDao;
import cn.dayuanzi.dao.MessageStatusDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.HouseOwners;
import cn.dayuanzi.model.MessageStatus;
import cn.dayuanzi.model.User;
import cn.dayuanzi.service.impl.ServiceRegistry;

/**
 * 
 * @author : leihc
 * @date : 2015年4月24日 上午10:26:33
 * @version : 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback=false )
@ContextConfiguration(locations = { "classpath:application.xml" })
public class TestDataAdder {

	@Test
	@Transactional
	public void test(){
		ApplicationContext context = ServiceRegistry.getApplicationContext();
		CourtYardDao courtyardDao = context.getBean(CourtYardDao.class);
		// 添加社区
		CourtYard courtyard = new CourtYard();
		courtyard.setCompany_id(1);
		courtyard.setCourtyard_name("万科悦城");
		courtyard.setCity_id(10);
		courtyard.setAddress("成都市天府大道一段1号");
		courtyardDao.save(courtyard);
		
		HouseOwnersDao ownerDao = context.getBean(HouseOwnersDao.class);
		List<Long> ownersId = new ArrayList<Long>();
		for(int i=0;i<10;i++){
			HouseOwners owner = new HouseOwners();
			owner.setCourtyard_id(courtyard.getId());
			owner.setBuilding_id(1);
			owner.setBuilding("一栋");
			owner.setUnit_id(1);
			owner.setUnit("一单元");
			owner.setTerm_id(1);
			owner.setTerm("一期");
			owner.setHouse_id(i+1);
			owner.setHouse(owner.getHouse_id()+"号");
			owner.setOwner_name("lhc-"+i);
			owner.setTel("13000000000");
			ownerDao.save(owner);
			ownersId.add(owner.getId());
		}
		long tel = 13800000000l;
		List<Integer> careers = new ArrayList<Integer>();
		careers.addAll(CareerConfig.getCareers().keySet());
		UserDao userDao = context.getBean(UserDao.class);
		MessageStatusDao msDao = context.getBean(MessageStatusDao.class);
//		long id = 1000;
		// 创建20个用户
		for(int i=0;i<20;i++){
			tel += i;
			User user = new User(String.valueOf(tel),DigestUtils.md5Hex("1234"));
			user.setCourtyard_id(courtyard.getId());
			user.setNickname("老王-"+i);
			user.setGender(RandomUtils.nextBoolean()?1:0);
			user.setCareerId(careers.get(RandomUtils.nextInt(careers.size())));
			user.setBirthday(new Timestamp(System.currentTimeMillis()));
			long randoId = ownersId.get(RandomUtils.nextInt(ownersId.size()));
			user.setHouseowner_id(randoId);
			user.setPlatform(1);
			userDao.save(user);
			MessageStatus messageStatus = new MessageStatus();
			messageStatus.setUser_id(user.getId());
			messageStatus.setCourtyard_id(user.getCourtyard_id());
			msDao.save(messageStatus);
		}
		
	}
}
