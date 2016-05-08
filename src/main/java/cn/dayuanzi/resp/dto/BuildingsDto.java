package cn.dayuanzi.resp.dto;



/**
 * 
 * @author : leihc
 * @date : 2015年4月29日
 * @version : 1.0
 */
public class BuildingsDto {

	
	/**
	 * 楼栋号
	 */
	private int building_id;


	/**
	 * 楼栋名称（显示）
	 */
	private String building;
	
	//public BuildingsDto(HouseOwners houseOwners){
	//	this.building_id = houseOwners.getBuilding_id();
	//	this.term_id = houseOwners.getTerm_id();
	//	this.house_id = houseOwners.getHouse_id();
	//	this.building = houseOwners.getBuilding();
	//	this.term = houseOwners.getTerm();
	//	this.house = houseOwners.getHouse();
	//	this.unit_id = houseOwners.getUnit_id();
	//	this.unit = houseOwners.getUnit();
	//}
	public BuildingsDto(int buildingid,String building){
		this.building_id = buildingid;
		this.building =building;
	}
	public int getBuilding_id() {
		return building_id;
	}
	public void setBuilding_id(int building_id) {
		this.building_id = building_id;
	}
	
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildingsDto  bdto = (BuildingsDto)obj;
		if(this.building_id == bdto.building_id && this.building.equals(bdto.building)){
			return true;
		}
		return false;
	}
	
	
	
}
