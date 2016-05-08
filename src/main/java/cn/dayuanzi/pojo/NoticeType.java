package cn.dayuanzi.pojo;

/**
 * 
 * @author : leihc
 * @date : 2015年5月25日
 * @version : 1.0
 */
public enum NoticeType {

	系统通知{
		@Override
		public String getTitle() {
			return this.name();
		}
	},社区公告{
		@Override
		public String getTitle() {
			return this.name();
		}
	},采纳通知 {
		@Override
		public String getTitle() {
			return "你的答案被采纳";
		}
	},求助通知{
		@Override
		public String getTitle() {
			return "紧急求助";
		}
	},活动通知{
		@Override
		public String getTitle() {
			return "活动通知";
		}
	},官方活动报名成功通知{
		@Override
		public String getTitle() {
			return "官方活动报名成功";
		}
	},邀约通知{
		@Override
		public String getTitle() {
			return "你成功报名了一个邀约";
		}
	},验证通知{
		/**
		 * @see cn.dayuanzi.pojo.NoticeType#getTitle()
		 */
		@Override
		public String getTitle() {
			return null;
		}
	},注册通知{
		/**
		 * @see cn.dayuanzi.pojo.NoticeType#getTitle()
		 */
		@Override
		public String getTitle() {
			//不推送，但通知标题和系统通知一样
			return 系统通知.name();
		}
	};
	/**
	 * 获取通知标题
	 * @return
	 */
	public abstract String getTitle();
}
