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

import com.alibaba.druid.util.StringUtils;
import com.jfinal.club.common.Enum.MlGoodsStatusEnum;
import com.jfinal.club.common.Enum.MlGoodsTypeKindEnum;
import com.jfinal.club.common.Enum.MlGoodsTypeStatusEnum;
import com.jfinal.club.common.Enum.MlOrderStatusEnum;
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
public class MlGoodsAppService {
	public static final MlGoodsAppService me = new MlGoodsAppService();
	private MlGoods dao = new MlGoods().dao();
	private  static final int LENGTH=8;
	private   static final  String COLUMNS="id,goods_code,goods_type_id,goods_name,goods_introduce,orders,price,count,goods_detail,detail_picture,banner_picture,selling_time,stopping_time,goods_attribute,logistics,creator,created,modifier,modified,status,current_mall_id,remark,count_selling";
	private   static final  String COLUMNS_BM="gd.id,gd.type_name,gd.type_code,gd.parent_type,gd.creator,gd.created,gd.modifier,gd.modified,gd.status,gd.current_mall_id,gd.remark,gd.type_kind";
    private static final int  LOGISTICS=0;  // 0是不需要快递信息
    private static final int  LOGISTICSIS=1;  // 0是需要快递信息
	/**
	 * 项目分页
	 */
	public Page<MlGoods> paginate(int pageNum,Integer goodsTypeId,String goodsName,Integer pageSize,Integer mallId) {

		Kv para = Kv.by("goodsTypeId", goodsTypeId).set("goodsName", goodsName).set("currentMallId",mallId).set("status",MlGoodsStatusEnum.Selling);
		SqlPara sqlPara = dao.getSqlPara("mlgoods.paginateApp",para);
		Page<MlGoods> mlGoodsPage = dao.paginate(pageNum, pageSize, sqlPara);
		// 过滤状态
		filterStatusList(mlGoodsPage.getList());
		return mlGoodsPage;
	}



	public Ret goodsDetail(Integer mallId,Integer id,Integer propertyId) {
		Kv para = Kv.by("columns", COLUMNS).set("mallId", mallId).set("id",id);
		SqlPara sqlPara = dao.getSqlPara("mlgoods.detail", para);
		MlGoods mlGoods = dao.findFirst(sqlPara);
		handleGoods(mlGoods,propertyId);
		return Ret.ok("mlGoods", mlGoods);
	}

	public MlGoods goodsDetailById(Integer mallId,Integer id,Integer propertyId) {
		Kv para = Kv.by("columns", COLUMNS).set("mallId", mallId).set("id",id);
		SqlPara sqlPara = dao.getSqlPara("mlgoods.detail", para);
		MlGoods mlGoods = dao.findFirst(sqlPara);
		handleGoods(mlGoods,propertyId);
		return mlGoods;
	}

	public MlGoods goodsDetailByIdAllMall(Integer id) {
		Kv para = Kv.by("columns", COLUMNS).set("id",id);
		SqlPara sqlPara = dao.getSqlPara("mlgoods.detail", para);
		MlGoods mlGoods = dao.findFirst(sqlPara);
		return mlGoods;
	}

	/**
	 * 处理商品
	 * @param mlGoods
	 */
	public  void handleGoods(MlGoods mlGoods,Integer propertyId){
		String bannerUrl=mlGoods.getBannerPicture();
		String [] bannerUrlAry=new String[]{};
		List dataList=new ArrayList();
		if (!StrKit.isBlank(bannerUrl)){
			bannerUrlAry=bannerUrl.split("#");
		}
		for (int i=0;i<bannerUrlAry.length;i++){
			// 去banner第一个值作为购买时候的显示图片。
			if (i==0){
				mlGoods.put("pic",bannerUrlAry[i]);
			}
			Map data=new HashMap();
			data.put("pic",bannerUrlAry[i]);
			data.put("id",i);
			dataList.add(data);
		}
		mlGoods.put("pics",dataList);

		String detailUrl=mlGoods.getDetailPicture();
		String [] detailUrlAry=new String[]{};
		List detailUrlList=new ArrayList();
		if (!StrKit.isBlank(detailUrl)){
			detailUrlAry=detailUrl.split("#");
		}
		for (int i=0;i<detailUrlAry.length;i++){
			Map data=new HashMap();
			data.put("pic",detailUrlAry[i]);
			data.put("id",i);
			detailUrlList.add(data);
		}
		mlGoods.put("detailPics",detailUrlList);
		if((int)mlGoods.get("logistics")==LOGISTICS){
			mlGoods.put("logistics",false);
		}
		else if((int)mlGoods.get("logistics")==LOGISTICSIS){
			mlGoods.put("logistics",true);
		}

		String goodsAttribute = mlGoods.getGoodsAttribute();
		String [] properties = new String[]{};
		List propertiesList=new ArrayList();
		if (!StrKit.isBlank(goodsAttribute)){
			properties = goodsAttribute.split("#");
		}
		//int minPrice=mlGoods.getPrice();
		for (int i=0;i<properties.length;i++){
			Map data=new HashMap();
			String[] propertiesOne = properties[i].split("=");
			if(propertiesOne.length >0){
				data.put("id",propertiesOne[0]);
				// 黑色=10,100#灰色=10,200#白色=10,300

				String [] values=propertiesOne[1].split(",");
				data.put("name",values[0]);
				data.put("amount",values[1]);
				/*if (minPrice>Integer.parseInt(values[2])){
					minPrice=Integer.parseInt(values[2]);
				}*/
				data.put("perPrice",DruidKit.changeF2Y(Integer.parseInt(values[2])));
				propertiesList.add(data);
				if(propertyId!=null){
					if (Integer.parseInt(propertiesOne[0])==propertyId){
						mlGoods.put("propertyPrice",DruidKit.changeF2Y(Integer.parseInt(values[2])));
						mlGoods.put("propertyCount",Integer.parseInt(values[1]));
						mlGoods.put("propertyName",values[0]);
					}

				}
			}else {
				data.put("name",propertiesOne[0]);
				data.put("amount",0);
				data.put("id",i);
				propertiesList.add(data);
			}
		}
		//mlGoods.setPrice(minPrice);
		mlGoods.put("price",DruidKit.changeF2Y(mlGoods.getPrice()));
		mlGoods.put("properties",propertiesList);
	}
	/**
	 * 判断商品名称是否存在
	 * @param projectId 当前 mlgoods 对象的 id 号，如果 mlgoods 对象还未创建，提供一个小于 0 的值即可
	 * @param name 项目名
	 */
	public boolean exists(int projectId, String name) {
		name = name.toLowerCase().trim();
		String sql = "select id from ml_goods where lower(goods_name) = ? and id != ? limit 1";
		Integer id = Db.queryInt(sql, name, projectId);
		return id != null;
	}

	/**
	 * 创建项目
	 */
	public Ret save(Account account, MlGoods mlGoods) {

		//project.setAccountId(accountId);
		mlGoods.setGoodsName((mlGoods.getGoodsName().trim()));
		mlGoods.setGoodsCode(DruidKit.genRandomNum(LENGTH));
		mlGoods.setOrders(getNextOrder(mlGoods.getGoodsTypeId()));
		mlGoods.setCreated(new Date());
		mlGoods.setCreator(account.getId());
		mlGoods.setModified(new Date());
		mlGoods.setModifier(account.getId());
		mlGoods.setCurrentMallId(account.getCurrentMallId());
		mlGoods.save();
		return Ret.ok("msg", "创建成功");
	}

	public MlGoods edit(int id) {
		return dao.findById(id);
	}

	public Ret update(Account account,MlGoods mlGoods) {
		mlGoods.setGoodsName((mlGoods.getGoodsName().trim()));
		mlGoods.setModified(new Date());
		mlGoods.setModifier(account.getId());
		mlGoods.update();
		return Ret.ok("msg", "修改成功");
	}

	/**
	 * 锁定
	 */
	public Ret lock(int id) {
		Db.update("update project set report = report + ? where id=?", Project.REPORT_BLOCK_NUM, id);
		return Ret.ok("msg", "锁定成功");
	}

	/**
	 * 解除锁定
	 */
	public Ret unlock(int id) {
		Db.update("update project set report = 0 where id=?", id);
		return Ret.ok("msg", "解除锁定成功");
	}

	/**
	 * 删除 project
	 */
	public Ret delete(Account account,int goodsId) {
		Integer accountId = Db.queryInt("select id from ml_goods  where id=? limit 1", goodsId);
		if (accountId != null) {
			deleteGoods(account, goodsId);
			return Ret.ok("msg", "project 删除成功");
		} else {
			return Ret.fail("msg", "project 删除失败");
		}
	}

	public  int getNextOrder(int typeId){

		String sql = "select orders  from ml_goods where goods_type_id = ? order by orders desc  limit 1";
		Integer id = Db.queryInt(sql,typeId);
		if (id==null){
			return 1;
		}
		return id;
	}

	public void   filterStatusList(List<? extends Model> modelList){
		for (Model m : modelList) {
			String bannerUrl=m.getStr("banner_picture");
			String menuPicture="";
			if (!StringUtils.isEmpty(bannerUrl)){
				menuPicture=bannerUrl.split("#")[0];
			}
			m.put("price",DruidKit.changeF2Y(m.getInt("price")));
			m.put("menuPicture",menuPicture);
		}
	}



	public void deleteGoods(Account account,int goodsId){
		Db.update("update ml_goods set status = ?,modifier = ? ,modified = ? where id=?", MlGoodsStatusEnum.Delete,account.getId(),new Date(),goodsId);
	}



}
