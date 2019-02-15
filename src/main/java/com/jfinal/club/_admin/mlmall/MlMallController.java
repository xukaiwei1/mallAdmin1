package com.jfinal.club._admin.mlmall;

import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.club.common.controller.BaseController;
import com.jfinal.club.common.model.MlMall;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * @Auther: wangli05
 * @Date: 2019/1/31
 * @Description:
 */
public class MlMallController extends BaseController {
    @Inject
    MlMallService mlMallService;

    /**
     * 分页获取商城列表
     */
    public void mallList() {
        Page<MlMall> projectPage = mlMallService.paginate( getParaToInt(0), getParaToInt(1));
        renderJson(projectPage);
    }

    /**
     * 新增
     */
    @Before({MlMallValidator.class})
    public void save() {
        MlMall mlMall = getBean(MlMall.class);
        Ret ret = mlMallService.save(mlMall);
        renderJson(ret);
    }

    /**
     * 根据id获取商城信息
     */
    public void getMallById() {
        MlMall mlMall = mlMallService.getMallById( getParaToInt(0));
        renderJson(mlMall);
    }

    /**
     * 更新
     */
    @Before({MlMallValidator.class})
    public void update() {
        Ret ret = mlMallService.update(getModel(MlMall.class));
        renderJson(ret);
    }

    /**
     * 删除
     */
    public void delete() {
        Ret ret = mlMallService.deleteById(getParaToInt("id"));
        renderJson(ret);
    }
}
