package cn.dayuanzi.service.impl;

import java.text.Collator;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.HouseOwnersDao;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.HouseOwners;
import cn.dayuanzi.service.ICourtYardService;
import cn.dayuanzi.util.GeoRange;

import com.google.common.collect.Ordering;


/**
 * 
 * @author : leihc
 * @date : 2015年4月29日
 * @version : 1.0
 */
@Service
public class CourtYardServiceImpl implements ICourtYardService {

	@Autowired
	private CourtYardDao courtYardDao;
	@Autowired
	private HouseOwnersDao houseOwnersDao;
	
	/**
	 * @see cn.dayuanzi.service.ICourtYardService#findCourtyardByCityId(int)
	 */
	@Override
	@Transactional(readOnly = true,propagation=Propagation.NOT_SUPPORTED)
	public List<CourtYard> findCourtyardByCityId(int city_id) {
		List<CourtYard> s = this.courtYardDao.find(Restrictions.eq("city_id", city_id));
		if(s.isEmpty()){
			return s;
		}
		final Collator cmp = Collator.getInstance(java.util.Locale.CHINA); 
		Ordering<CourtYard> orders = new Ordering<CourtYard>(){
			@Override
			public int compare(CourtYard o1, CourtYard o2) {
				return cmp.compare(o1.getCourtyard_name(), o2.getCourtyard_name());
			}
		};
		s = orders.sortedCopy(s);
		return s;
	}

	/**
	 * @see cn.dayuanzi.service.ICourtYardService#findCourtyardByCityId(int, cn.dayuanzi.util.GeoRange)
	 */
	@Override
	@Transactional(readOnly = true,propagation=Propagation.NOT_SUPPORTED)
	public List<CourtYard> findCourtyardByCityId(int city_id, GeoRange gr) {
		return courtYardDao.findCourtyards(city_id, gr);
	}
	/**
	 * @see cn.dayuanzi.service.ICourtYardService#findBuildingsByCourtyardId(long)
	 */
	//@Override
	//@Transactional(readOnly = true)
	//public List<HouseOwners> findBuildingsByCourtyardId(long courtyard_id) {
	//	return houseOwnersDao.find(Restrictions.eq("courtyard_id", courtyard_id));
	//}
	@Override
	@Transactional(readOnly = true)
	public List<HouseOwners> findTermByCourtyardId(long courtyard_id){
		return houseOwnersDao.find(Restrictions.eq("courtyard_id", courtyard_id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<HouseOwners> findBuildingsByTermId(long courtyard_id,int term_id){
		return houseOwnersDao.find(Restrictions.eq("courtyard_id", courtyard_id),Restrictions.eq("term_id",term_id));
	}
	@Override
	@Transactional(readOnly = true)
	public List<HouseOwners> findUnitByBuildings(long courtyard_id,int term_id,int building_id){
		return houseOwnersDao.find(Restrictions.eq("courtyard_id", courtyard_id),Restrictions.eq("term_id",term_id),Restrictions.eq("building_id",building_id));
	}
	@Override
	@Transactional(readOnly = true)
	public List<HouseOwners> findHouseByUnit(long courtyard_id,int term_id,int building_id,int unit_id){
		return houseOwnersDao.find(Restrictions.eq("courtyard_id", courtyard_id),Restrictions.eq("term_id",term_id),Restrictions.eq("building_id",building_id),Restrictions.eq("unit_id",unit_id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public CourtYard findCourtyard (long courtyard_id){
		return this.courtYardDao.get(courtyard_id);
	}
}
