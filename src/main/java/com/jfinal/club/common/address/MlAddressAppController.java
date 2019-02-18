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

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.club.common.controller.BaseController;
import com.jfinal.club.common.model.MlGoods;
import com.jfinal.club.index.IndexService;
import com.jfinal.club.project.ProjectService;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 商品管理控制器
 */
public class MlAddressAppController extends BaseController {

	@Inject
	MlAddressAppService srv;



	/**
	 * 得到默认收货地址
	 */
	public void getDefaultAddress() {
		// 查询默认收货地址
		Ret ret = srv.getDefaultAddress(getLoginMluser());
		renderJson(ret);
	}


	/**
	 * 新增默认收货地址
	 */
	public void addAddress() {
		// 新增默认收货地址
		Ret ret = srv.getDefaultAddress(getLoginMluser());
		renderJson(ret);
	}




}


