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

package com.jfinal.club._admin.goods;

import com.jfinal.club.common.Enum.MlGoodsStatusEnum;
import com.jfinal.club.common.kit.DruidKit;
import com.jfinal.club.common.model.Account;
import com.jfinal.club.common.model.MlGoods;
import com.jfinal.club.common.model.Project;
import com.jfinal.club.common.safe.JsoupFilter;
import com.jfinal.club.my.project.MyProjectService;
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
public class GoodsAdminService {

	private MlGoods dao = new MlGoods().dao();
	private  static final int LENGTH=8;

	/**
	 * 项目分页
	 */
	public Page<MlGoods> paginate(int pageNum,Integer goodsTypeId,String goodsName) {

		Kv para = Kv.by("goodsTypeId", goodsTypeId).set("goodsName", goodsName);
		SqlPara sqlPara = dao.getSqlPara("admin.mlgoods.paginate",para);
		Page<MlGoods> mlGoodsPage = dao.paginate(pageNum, 10, sqlPara);
		// 过滤状态
		filterStatusList(mlGoodsPage.getList());
		return mlGoodsPage;
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
		mlGoods.setOrder(getNextOrder(mlGoods.getGoodsTypeId()));
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

		String sql = "select order  from ml_goods where goods_type_id = ? order by order desc  limit 1";
		Integer id = Db.queryInt(sql,typeId);
		if (id==null){
			return 1;
		}
		return id;
	}

	public void   filterStatusList(List<? extends Model> modelList){
		for (Model m : modelList) {
			int status = m.getInt("status");
			m.set("statusStr", MlGoodsStatusEnum.enumValueOf(status).toName());
		}
	}

	public void deleteGoods(Account account,int goodsId){
		Db.update("update ml_goods set status = ?,modifier = ? ,modified = ? where id=?", MlGoodsStatusEnum.Delete,account.getId(),new Date(),goodsId);
	}

}
