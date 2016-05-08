package cn.dayuanzi.pojo;

/**
 * 交易状态
 * 
 * @author : leihc
 * @date : 2015年5月4日
 * @version : 1.0
 */
public enum TradeStatus {

	WAIT_BUYER_PAY,//交易创建，等待买家付款
	TRADE_CLOSED,//在指定 时间段内未支付时关闭的交易； 在交易完 成全额退款成功时关闭的交易
	TRADE_SUCCESS,//交易成功，且可对该交易做操作，如：多级分润、退款等
	TRADE_FINISHED;//交易成功且结束，即不可再做任何操作
}
