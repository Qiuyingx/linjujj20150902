package cn.dayuanzi.pojo;

/**
 * 外部平台
 * @author : leihc
 * @date : 2015年5月28日
 * @version : 1.0
 */
public enum ExternalType {

	邻聚{
		/**
		 * @see cn.dayuanzi.pojo.ExternalType#isOpen()
		 */
		@Override
		public boolean isOpen() {
			return true;
		}
	},QQ{
		/**
		 * @see cn.dayuanzi.pojo.ExternalType#isOpen()
		 */
		@Override
		public boolean isOpen() {
			return true;
		}
	},微信{
		/**
		 * @see cn.dayuanzi.pojo.ExternalType#isOpen()
		 */
		@Override
		public boolean isOpen() {
			return true;
		}
	},微博{
		/**
		 * @see cn.dayuanzi.pojo.ExternalType#isOpen()
		 */
		@Override
		public boolean isOpen() {
			return true;
		}
	};
	
	public abstract boolean isOpen();
}
