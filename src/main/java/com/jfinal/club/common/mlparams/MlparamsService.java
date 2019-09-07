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

package com.jfinal.club.common.mlparams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayUtil;
import com.jfinal.aop.Inject;
import com.jfinal.club.common.Enum.MlOrderStatusEnum;
import com.jfinal.club.common.Enum.MlPayLogStatusEnum;
import com.jfinal.club.common.address.MlAddressAppService;
import com.jfinal.club.common.exception.SystemException;
import com.jfinal.club.common.goods.MlGoodsAppService;
import com.jfinal.club.common.kit.DruidKit;
import com.jfinal.club.common.kit.HttpClientUtil;
import com.jfinal.club.common.kit.LockUtil;
import com.jfinal.club.common.model.*;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.Ret;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * project 管理业务
 */
public class MlparamsService {

	public static final MlparamsService me = new MlparamsService();
	private MlOrder dao = new MlOrder().dao();
	private MlPayLog mlPayLogDao = new MlPayLog().dao();
	private MlParams mlParamsDao=new MlParams().dao();
	private   static final  String COLUMNS="id,user_id,goods_id,amount,count,goods_attribute,coupon_id,address_id,order_code,order_time,pay_time,freight,trackingNumber,paytype,creator,created,modifier,modified,status,current_mall_id,remark";
	private   static final  String COLUMNS_BM="mlo.id,mlo.user_id,mlo.goods_id,mlo.amount,mlo.count,mlo.goods_attribute,mlo.coupon_id,mlo.address_id,mlo.order_code,mlo.order_time,mlo.pay_time,mlo.freight,mlo.trackingNumber,mlo.paytype,mlo.creator,mlo.created,mlo.modifier,mlo.modified,mlo.status,mlo.current_mall_id,mlo.remark,mlu.weixin_name,mlg.goods_name";
	private   static final  String PARAM_COLUMNS="id,type,paramkey,paramvalue,creator,created,modifier,modified,status,current_mall_id,remark";
	private   static final  String PAPLOG_COLUMNS="id,user_id,order_id,amount,pay_time,creator,created,modifier,modified,status,current_mall_id,remark";
	private   static final  String YUNFEIKEY="yunFree";
	private   static final  int  DELESTATUS=2;
	private static final String ML_GOODS = "ml_goods%s";

	private static final Log log = Log.getLog(MlparamsService.class);


	public MlParams getMlparams(Integer currentMallId,String key){
		Kv para1 = Kv.by("columns", PARAM_COLUMNS).set("mallId", currentMallId).set("keys",key);
		SqlPara sqlPara1 = mlParamsDao.getSqlPara("mall.getParams", para1);
		MlParams mlParams=mlParamsDao.findFirst(sqlPara1);
		return mlParams;

	}

}
