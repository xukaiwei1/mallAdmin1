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

package com.jfinal.club.common.goods;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import java.util.Date;

/**
 * 我的项目表单提交校验
 */
public class MlGoodsAppValidator extends Validator {

	@Inject
	MlGoodsAppService srv;
	protected void validate(Controller c) {
		// 验证一个失败之后立即返回 不去校验后面的
		setShortCircuit(true);

		// 空校验
		validateRequired("mlGoods.goodsTypeId", "msg", "商品类型不能为空");
		validateRequired("mlGoods.goodsName", "msg", "商品名称不能为空");
		validateRequired("mlGoods.price", "msg", "价格不能为空");
		validateRequired("mlGoods.count", "msg", "商品个数不能为空");
		//List<UploadFile> fileList= c.getFiles();
		validateRequired("mlGoods.sellingTime", "msg", "商品起售时间不能为空");
		validateRequired("mlGoods.stoppingTime", "msg", "商品停售时间不能为空");
		validateRequired("mlGoods.status", "msg", "商品状态不能为空");
		validateRequired("mlGoods.goodsAttribute", "msg", "商品属性不能为空");


		String goodsName = c.getPara("mlGoods.goodsName");
		if (srv.exists(-1,goodsName)) {
			addError("msg", "商品名称已经存在，请使用别的名称！");
		}
        Date sellingTime=c.getParaToDate("mlGoods.sellingTime");
		Date stoppingTime=c.getParaToDate("mlGoods.stoppingTime");
		if (stoppingTime.before(sellingTime)){
			addError("msg", "商品停售时间不能早于起售时间！");
		}

	}



	protected void handleError(Controller c) {
		c.renderJson();
	}
}
