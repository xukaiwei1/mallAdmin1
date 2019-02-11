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

package com.jfinal.club._app.mall;

import com.jfinal.club.common.Enum.MlGoodsStatusEnum;
import com.jfinal.club.common.kit.DruidKit;
import com.jfinal.club.common.model.Account;
import com.jfinal.club.common.model.MlGoods;
import com.jfinal.club.common.model.MlMall;
import com.jfinal.club.common.model.Project;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

import java.util.Date;
import java.util.List;

/**
 * project 管理业务
 */
public class MallAppService {

	private MlMall dao = new MlMall().dao();
	private   static final  String COLUMNS="id,mall_name,mall_desc,mall_code,mall_type,status";



	public Ret getMallconfig(int mlCode) {
		Kv para = Kv.by("columns", COLUMNS).set("mlCode", mlCode);
		SqlPara sqlPara = dao.getSqlPara("mall.getMall", para);
		MlMall mlMall = dao.findFirst(sqlPara);
		return Ret.ok("mlMall", mlMall);
	}

	public Ret getMallBanner(int mlCode) {
		Kv para = Kv.by("columns", COLUMNS).set("mlCode", mlCode);
		SqlPara sqlPara = dao.getSqlPara("mall.getMallBanner", para);
		MlMall mlMall = dao.findFirst(sqlPara);
		return Ret.ok("mlMall", mlMall);
	}


}
