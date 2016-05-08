package cn.dayuanzi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.dao.NoticeDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.model.Notice;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserSetting;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.service.INoticeService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.util.ApnsUtil;

/**
 * 
 * @author : leihc
 * @date : 2015年6月24日
 * @version : 1.0
 */
@Service
public class NoticeService implements INoticeService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private IUserService userService;
	
	/**
	 * @see cn.dayuanzi.service.INoticeService#sendNoticeToAllCourtyard(cn.dayuanzi.pojo.NoticeType, java.lang.String)
	 */
	@Override
	@Transactional
	public void sendNoticeToAllCourtyard(NoticeType noticeType, String title,String content, long append) {
		sendNoticeToCourtyard(noticeType, 0, title, content, append);
	}

	/**
	 * @see cn.dayuanzi.service.INoticeService#sendNoticeToCourtyard(cn.dayuanzi.pojo.NoticeType, long, java.lang.String)
	 */
	@Override
	@Transactional
	public void sendNoticeToCourtyard(NoticeType noticeType, long courtyardId, String title, String content, long append) {
		Notice notice = new Notice(noticeType.ordinal(), courtyardId, content);
		notice.setTitle(title);
		notice.setAppend(append);
		this.noticeDao.save(notice);
	}

	/**
	 * @see cn.dayuanzi.service.INoticeService#sendNoticeToUser(cn.dayuanzi.pojo.NoticeType, long, java.lang.String)
	 */
	@Override
	@Transactional
	public void sendNoticeToUser(NoticeType noticeType, long userId, String title, String content, long append) {
		User user = this.userDao.get(userId); 
		Notice notice = new Notice(noticeType.ordinal(),user.getCourtyard_id(),user.getId(), content);
		notice.setAppend(append);
		notice.setTitle(title);
		this.noticeDao.save(notice);
		int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadNotice(userId, 1);
		UserSetting setting = this.userService.getUserSetting(userId);
		if(setting.isSystem()){
			if(noticeType==NoticeType.采纳通知){
				ApnsUtil.getInstance().send(user, NoticeType.采纳通知, "恭喜你，你的答案被采纳，赶快去看看吧！", count);
			}else{
				ApnsUtil.getInstance().send(user, count);
			}
		}else{
			ApnsUtil.getInstance().send(user, count);
		}
	}

}
