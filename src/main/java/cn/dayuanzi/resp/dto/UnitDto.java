package cn.dayuanzi.resp.dto;





/**
 * 
 * @author qiuyingxiang
 *
 */
public class UnitDto {

	/**
	 * 单元id
	 */
	private int unit_id;
	/**
	 * 单元
	 */
	private String unit;
	
	public  UnitDto (int unit_id,String unit){
		this.unit_id = unit_id;
		this.unit = unit;
		
	}

	public int getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnitDto  udto = (UnitDto)obj;
		if(this.unit_id == udto.unit_id && this.unit.equals(udto.unit)){
			return true;
		}
		return false;
	}
	
}
