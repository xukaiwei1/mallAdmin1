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

package com.jfinal.club._app.order;

import com.github.wxpay.sdk.WXPayUtil;
import com.jfinal.aop.Inject;
import com.jfinal.club.common.Enum.MlOrderStatusEnum;
import com.jfinal.club.common.controller.BaseController;
import com.jfinal.club.common.model.EnumDomain;
import com.jfinal.club.common.model.MlOrder;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.plugin.redis.RedisPlugin;

import java.io.*;
import java.util.*;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/**
 * 商品管理控制器
 */
public class MlOrderAppController extends BaseController {

	@Inject
	MlOrderAppService srv;
	private static final Log log = Log.getLog(MlOrderAppController.class);





	/**
	 * 提交订单
	 */
	//@Before(MlOrderAppValidator.class)
	public void create() {
		Boolean calculate=getParaToBoolean("calculate");
		int addressId=getParaToInt("addressId");
		String remark=getPara("remark");
		Ret ret=null;
		if (calculate){
			 ret = srv.calculate(getLoginMluser(), getPara("goodsJsonStr"));
		}
		else {
			try {
				ret = srv.create(getLoginMluser(), getPara("goodsJsonStr"),addressId,remark);
			}
			catch (Exception e){
				ret=Ret.fail("msg",e.getMessage());

			}

		}

		renderJson(ret);
	}

	/**
	 * 查询订单
	 */
	//@Before(MlOrderAppValidator.class)
	public void list() {
        int status=getParaToInt("status");
		List<MlOrder> orderList = srv.list(getLoginMluser(),status);
		renderJson(Ret.ok("msg", "查询成功").set("orderList",orderList));
	}





	/**
	 * 取消订单
	 */
	//@Before(MlOrderAppValidator.class)
	public void closeOrder() {
		// 根据商品类型 ，商品名字 查询
		Integer id=getParaToInt("id");
		srv.closeOrder(id);
		renderJson(Ret.ok("msg", "删除成功"));
	}


	/**
	 * 订单详情
	 */
	public void orderDetail() {
		// 根据商品类型 ，商品名字 查询
		Integer id=getParaToInt("id");
		Ret ret = srv.orderDetail(getLoginMluser(),id);
		renderJson(ret);
	}


	/**
	 * 支付订单
	 */
	//@Before(MlOrderAppValidator.class)
	public void pay() throws Exception {
		String remark=getPara("remark");
		String  payName=getPara("payName");
		String nextAction=getPara("nextAction");
		Ret ret=srv.pay(getLoginMluser(),remark,payName,nextAction);
		renderJson(ret);

	}

	/**
	 * 支付订单
	 */
	public void notifyPay(Controller c) throws Exception {
		InputStream inputStream ;
		StringBuffer sb = new StringBuffer();
		inputStream = c.getRequest().getInputStream();
		String s ;
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		while ((s = in.readLine()) != null){
			sb.append(s);
		}
		in.close();
		inputStream.close();
		String resXml="";
		if(srv.notifyPay(sb.toString())) {
			    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			} else {
			  resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[签名失败]]></return_msg>" + "</xml> ";
				log.error("回调失败");
			}
		BufferedOutputStream out = new BufferedOutputStream(c.getResponse().getOutputStream());
		out.write(resXml.getBytes());
		out.flush();
		out.close();
		}

	public static void main(String[] args) {

		/*RedisPlugin rp = new RedisPlugin("myRedis", "39.105.229.235");
		// 与web下唯一区别是需要这里调用一次start()方法
		rp.start();

		Redis.use().set("key", "value");
		Redis.use().get("key");*/

		String goodsAttribute="1=黑色,9,10056,#2=灰色,10,20045#3=白色,10,30067#";
		String [] goodsAttributeArray=goodsAttribute.split("#");
		StringBuilder sb=new StringBuilder();
		int stock=0;
		for (int i=0;i<goodsAttributeArray.length;i++){

			String [] arrays=goodsAttributeArray[i].split("=");
			sb.append(arrays[0]).append("=");
			if (1==Integer.parseInt(arrays[0])){
				String value=arrays[1];
				String[] valueArray=value.split(",");
				stock=Integer.parseInt(valueArray[1]);
				stock=stock-1;
				valueArray[1]=String.valueOf(stock);
				for (int j=0;j<valueArray.length;j++){
					sb.append(valueArray[j]).append(",");
				}
			}
			else {
				sb.append(arrays[1]);
			}
			sb.append("#");
		}
		System.out.println(sb.toString());
	}




	/**
	 * 查询订单
	 */
	//@Before(MlOrderAppValidator.class)
	public void init() {
		List<EnumDomain> orderStatus=MlOrderStatusEnum.toCodeNameList();
		renderJson(Ret.ok("msg", "查询成功").set("orderStatus",orderStatus));
	}


	/** 管理员查询
	 *   订单
	 */
	//@Before(MlOrderAppValidator.class)
	public void listOrder() {
		Integer status=getParaToInt("status");
		Date startTime=getParaToDate("startTime");
		Date endTime=getParaToDate("endTime");
		Integer page=getParaToInt("page",1);
		Integer rows=getParaToInt("rows");

		Page orderList = srv.listOrder(getLoginAccount(),status,startTime,endTime,page,rows);
		renderJson(Ret.ok("msg", "查询成功").set("orderList",orderList));
	}



}


