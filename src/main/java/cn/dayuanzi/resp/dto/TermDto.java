package cn.dayuanzi.resp.dto;



 /**
  * 
  * @author qiuyingxiang
  *
  */
public class TermDto {
	
	/**
	 * 期数id
	 */
	private int term_id;
	/**
	 * 期数名称
	 */
	private String term;
	
	public TermDto(int Term_id,String Term){
		this.term_id = Term_id;
		this.term = Term;
	}
	
	public int getTerm_id() {
		return term_id;
	}

	public void setTerm_id(int term_id) {
		this.term_id = term_id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TermDto tdto = (TermDto)obj;
		if(this.term_id==tdto.term_id && this.term.equals(tdto.term)){
			return true;
		}
		
		return false;
	}
	
}
