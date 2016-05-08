package cn.dayuanzi.service;

import java.util.List;

import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.HouseOwners;
import cn.dayuanzi.util.GeoRange;


/**
 * 
 * @author : leihc
 * @date : 2015年4月29日
 * @version : 1.0
 */
public interface ICourtYardService {

	/**
	 * 查询指定城市的社区
	 * 
	 * @param city_id
	 * @return
	 */
	public List<CourtYard> findCourtyardByCityId(int city_id);
	
	/**
	 * 查询指定城市指定范围的社区
	 * @param city_id
	 * @param gr
	 * @return
	 */
	public List<CourtYard> findCourtyardByCityId(int city_id,GeoRange gr);
	
	///**
	// * 通过院子ID获取该院子的楼栋信息
	// * 
	// * @param courtyard_id
	// * @return
	// */
	//public List<HouseOwners> findBuildingsByCourtyardId(long courtyard_id);
	/**
	 * 通过院子Id获取该院子期数信息
	 * @param courtyard_id
	 * @return
	 */
	public List<HouseOwners> findTermByCourtyardId(long courtyard_id);
	/**
	 * 通过院子Id,期数id,获得指定栋数信息
	 * @param courtyard_id
	 * @param termid
	 * @return
	 */
	public List<HouseOwners> findBuildingsByTermId(long courtyard_id,int term_id);
	/**
	 * 通过院子Id，期数id,栋数id 获取指定单元
	 * @param courtyard_id
	 * @param term_id
	 * @param unit_id
	 * @return
	 */
	public List<HouseOwners> findUnitByBuildings(long courtyard_id,int term_id,int building_id);
	/**
	 * 通过院子Id,期数id,栋数id 单元id 获取房号
	 * @param courtyard_id
	 * @param term_id
	 * @param building_id
	 * @param unid_id
	 * @return
	 */
	public List<HouseOwners> findHouseByUnit(long courtyard_id,int term_id,int building_id,int unit_id);
	/**
	 * 查询该院子是否存在
	 * @param courtyard_id
	 * @return
	 */
	public CourtYard findCourtyard (long courtyard_id);
}
