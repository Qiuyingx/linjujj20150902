package cn.dayuanzi.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;



/**
 * 
 * @author : leihc
 * @date : 2015年4月19日 上午11:00:00
 * @version : 1.0
 */
public interface IHouseOwnersService {

	/**
	 * 
	 * @param userId 用户ID
	 * @param courtyardId 院子ID
	 * @param term 期数id
	 * @param building 楼栋id
	 * @param unit 单元id
	 * @param houseId  房号id
	 * @param tel   与社区有合作时需要该参数，无合作时可不传
	 * @param validateType   与社区无合作时需要该参数，取值1 上传图片验证 2 code验证，有合作时可不传该参数
	 * @param image    与社区无合作并用图片上传时需要该参数，其他情况可不传
	 * @return
	 */
	public boolean validateHouseOwner(long userId, long courtyardId, Long inviteCode, Integer validateType,CommonsMultipartFile image);
	/**
	 * 通过地图距离验证社区
	 * 
	 * @param userId
	 * @param courtyardId
	 * @param lgt
	 * @param lat
	 * @return
	 */
	public boolean validateHouseOwner(long userId, long courtyardId, double lgt, double lat);
	/**
	 * code 验证
	 * @param userId
	 * @param code
	 * @return
	 */
//	public boolean codevalidate(long userId,String code);
}
