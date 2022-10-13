package com.example.common.enums.wx;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WxNotifyType {

	/**
	 * 支付通知
	 */
	NATIVE_NOTIFY("/sys/native/pay/notify"),

	/**
	 * 支付通知V2
	 */
	NATIVE_NOTIFY_V2(""),


	/**
	 * 退款结果通知
	 */
	REFUND_NOTIFY("/sys/native/pay/refund/notify");

	/**
	 * 类型
	 */
	private final String type;
}
