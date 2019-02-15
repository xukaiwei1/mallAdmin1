package com.jfinal.club._admin.mlmall;

import com.jfinal.club.common.model.MlMall;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

import java.util.Date;

/**
 * @Auther: wangli05
 * @Date: 2019/1/31 17
 * @Description:
 */
public class MlMallService {
    private MlMall dao = new MlMall().dao();
    private   static final  String COLUMNS="id,mall_name,mall_desc,mall_code,mall_type,mall_address,mall_phone,creator,created,modifier,modified,status,remark";
    /**
     * 根据ID获取商城信息
     * @param mlMallId
     * @return
     */
    public MlMall getMallById(int mlMallId) {
        Kv para = Kv.by("columns", COLUMNS).set("id", mlMallId);
        SqlPara sqlPara = dao.getSqlPara("mall.getMallById",para);
        return dao.findFirst(sqlPara);
    }

    /**
     * 分页获取商城列表
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public Page<MlMall> paginate(int pageNumber, int pageSize) {
        Kv para = Kv.by("columns", COLUMNS);
        SqlPara sqlPara = dao.getSqlPara("mall.getMallAll",para);
        return dao.paginate(pageNumber, pageSize, sqlPara);
    }

    /**
     * 保存
     * @param mlMall
     * @return
     */
    public Ret save(MlMall mlMall) {
        mlMall.save();
        return Ret.ok("msg", "保存成功");
    }

    /**
     * 修改
     * @param mlMall
     * @return
     */
    public Ret update(MlMall mlMall) {
        mlMall.update();
        return Ret.ok("msg", "修改成功");
    }

    /**
     * 删除
     * @param mlMallId
     * @return
     */
    public Ret deleteById(int mlMallId) {
        MlMall mlMall  = getMallById(mlMallId);
        if (mlMall != null) {
            int flag = Db.update("update ml_mall set status = ?,modified = ? where id=? ",2,new Date(),mlMallId);
            if(flag == 1){
                return Ret.ok("msg", "删除成功");
            }
        }
        return Ret.fail("msg", "删除失败");
    }
}
