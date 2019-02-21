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

import com.alibaba.druid.util.StringUtils;
import com.jfinal.club.common.Enum.MlGoodsStatusEnum;
import com.jfinal.club.common.kit.DruidKit;
import com.jfinal.club.common.model.*;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

import java.util.*;

/**
 * project 管理业务
 */
public class MlAddressAppService {

	private MlAddress dao = new MlAddress().dao();
	private   static final  String COLUMNS="id,user_id,addressxy,is_default,receive_address,telphone,name,zipcode,creator,created,modifier,modified,status,current_mall_id,remark";


	public Ret getDefaultAddress(MlUser mlUser) {
		Kv para = Kv.by("columns", COLUMNS).set("mallId", mlUser.getCurrentMallId()).set("userId",mlUser.getId());
		SqlPara sqlPara = dao.getSqlPara("mlAddress.defaultAddress", para);
		MlAddress mlAddress = dao.findFirst(sqlPara);
		return Ret.ok("mlAddress", mlAddress);
	}



	/**
	 * 创建默认地址
	 */
	public Ret addDefaultAddress(MlUser mlUser, MlAddress mlAddress) {

		mlAddress.setUserId(mlUser.getId());
		mlAddress.setIsDefault(0);
		mlAddress.setCreator(mlUser.getId());
		mlAddress.setCreated(new Date());
		mlAddress.setModifier(mlUser.getId());
		mlAddress.setModified(new Date());
		mlAddress.setStatus(0);
		mlAddress.setCurrentMallId(mlUser.getCurrentMallId());
		mlAddress.save();
		return Ret.ok("msg", "创建成功");
	}

}
