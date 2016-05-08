/**
 * 
 */
package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.PersistentSupport;

/** 
 * @ClassName: TrainLaud 
 * @Description: 学堂红心表
 * @author qiuyingxiang
 * @date 2015年8月13日 下午5:56:28 
 *  
 */
@Entity
@Table(name = "t_train_laud")
public class TrainLaud extends PersistentSupport{
	/**
	 * 学堂ID
	 */
	private long train_id;
	/**
	 * 用户ID
	 */
	private long user_id;
	
	/**
	 * 院子ID
	 */
	private long courtyard_id;
	/**
	 * 点赞时间
	 */
	private long create_time;
	
	public TrainLaud(){
		
	}

	public TrainLaud(long trainId,long userId,long courtyardId){
		this.courtyard_id = courtyardId;
		this.create_time = System.currentTimeMillis();
		this.train_id = trainId;
		this.user_id = userId;
		
	}
	public long getTrain_id() {
		return train_id;
	}

	public void setTrain_id(long train_id) {
		this.train_id = train_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public long getCourtyard_id() {
		return courtyard_id;
	}

	public void setCourtyard_id(long courtyard_id) {
		this.courtyard_id = courtyard_id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	
	
}
