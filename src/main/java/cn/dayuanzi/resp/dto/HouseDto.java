package cn.dayuanzi.resp.dto;

public class HouseDto {

	/**
	 * 房号
	 */
	private int house_id;
	/**
	 * 房名称（显示）
	 */
	private String house;
	/**
	 * 对应房号业主手机号前3位
	 */
	private String telf;
	/**
	 * 对应房号业主手机号后四位
	 */
	private String tell;
	
	public HouseDto(int houseId,String house,String telf,String tell){
		this.house_id = houseId ;
		this.house = house;
		this.telf = telf;
		this.tell =tell;
		
	}

	public int getHouse_id() {
		return house_id;
	}

	public void setHouse_id(int house_id) {
		this.house_id = house_id;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getTelf() {
		return telf;
	}

	public void setTelf(String telf) {
		this.telf = telf;
	}

	public String getTell() {
		return tell;
	}

	public void setTell(String tell) {
		this.tell = tell;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HouseDto  hdto = (HouseDto)obj;
		if(this.house_id == hdto.house_id && this.house.equals(hdto.house)){
			return true;
		}
		return false;
	}
}
