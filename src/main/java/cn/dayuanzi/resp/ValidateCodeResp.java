package cn.dayuanzi.resp;

/**
 * 返回给客户端的验证码
 * 
 * @author : leihc
 * @date : 2015年4月14日 下午2:52:51
 * @version : 1.0
 */
public class ValidateCodeResp extends Resp {

	/**
	 * 返回的验证码
	 */
	private String validateCode;
	
	public ValidateCodeResp(String validateCode){
		this.validateCode = validateCode;
	}

	/**
	 * @return the validateCode
	 */
	public String getValidateCode() {
		return validateCode;
	}

	/**
	 * @param validateCode the validateCode to set
	 */
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	
	
}
