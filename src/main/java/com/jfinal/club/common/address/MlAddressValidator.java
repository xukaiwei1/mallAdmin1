/**
 * 请勿将俱乐部专享资源复制给其他人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目文档与设计资源、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多
 * 
 * JFinal 俱乐部是五年以来首次寻求外部资源的尝试，以便于有资源创建更加
 * 高品质的产品与服务，为大家带来更大的价值，所以请大家多多支持，不要将
 * 首次的尝试扼杀在了摇篮之中
 */

package com.jfinal.club.common.address;

import com.jfinal.aop.Inject;
import com.jfinal.club._admin.goods.GoodsAdminService;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import java.util.Date;

/**
 * 我的项目表单提交校验
 */
public class MlAddressValidator extends Validator {

	@Inject
	GoodsAdminService srv;
	protected void validate(Controller c) {
		// 验证一个失败之后立即返回 不去校验后面的
		setShortCircuit(true);
		validateRegex("mobile", "((13[0-9])|(15[^4])|(18[0,1,2,3,4,5-9])|(17[0-8])|(147))\\d{8}","mobile", "手机号格式不正确");

	}



	protected void handleError(Controller c) {
		c.renderJson();
	}
}
