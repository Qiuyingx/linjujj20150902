package cn.dayuanzi.pojo;

/**
 * 类型
 *  
 * @author : leihc
 * @date : 2015年5月5日
 * @version : 1.0
 */
public enum ContentType {

	邀约(1),邻居帮帮(2),分享(3),活动(4),专题(5),帖子评论(6),活动评论(7),专题评论(8);
	
	private int value;
	
	ContentType(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static ContentType getInstance(int value){
		for(int i=0;i<ContentType.values().length;i++){
			if(ContentType.values()[i].getValue()==value){
				return ContentType.values()[i];
			}
		}
		return null;
	}
	
}
