package cn.dayuanzi.service.impl;

import org.springframework.stereotype.Service;

import com.notnoop.apns.APNS;

import cn.dayuanzi.util.ApnsUtil;


/**
 * 
 * @author : leihc
 * @date : 2015年5月25日
 * @version : 1.0
 */
@Service
public class APNSServiceimpl {

	public void sendMsg(String deviceToken, String msg){
		long start = System.currentTimeMillis();
		String payload = APNS.newPayload().alertBody(msg).build();
		ApnsUtil.getInstance().getApnsService().push(deviceToken, payload);
		System.out.println("complete push message "+(System.currentTimeMillis()-start));
		
	}
}
