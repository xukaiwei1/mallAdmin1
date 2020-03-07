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

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayUtil;
import com.jfinal.aop.Inject;
import com.jfinal.club.common.Enum.MlGoodsStatusEnum;
import com.jfinal.club.common.Enum.MlOrderStatusEnum;
import com.jfinal.club.common.Enum.MlPayLogStatusEnum;
import com.jfinal.club.common.address.MlAddressAppService;
import com.jfinal.club.common.exception.SystemException;
import com.jfinal.club.common.goods.MlGoodsAppService;
import com.jfinal.club.common.kit.DruidKit;
import com.jfinal.club.common.kit.HttpClientUtil;
import com.jfinal.club.common.kit.LockUtil;
import com.jfinal.club.common.model.*;
import com.jfinal.club.common.pageview.PageViewService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

import java.io.BufferedOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * project 管理业务
 */
public class MlOrderAppService {

	public static final MlOrderAppService me = new MlOrderAppService();
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



	@Inject
	MlGoodsAppService srv;
	@Inject
	MlAddressAppService  addressSrv;
	private static final Log log = Log.getLog(MlOrderAppService.class);

	/**
	 * 计算金额和运费
	 */
	public Ret calculate(MlUser mlUser, String goodsJsonStr) {

	    // 目前只是计算运费
		// 获取运费值 目前是在数据库配置 写死的
		int amountTotle=0;
		for(int i=0;i<JSON.parseArray(goodsJsonStr).size();i++){

			JSONObject goodsJsonStrs= JSON.parseArray(goodsJsonStr).getJSONObject(i);
			int goodId=(int)goodsJsonStrs.get("goodsId");
			int number=(int)goodsJsonStrs.get("number");
			MlGoods mlGoods=srv.goodsDetailById(mlUser.getCurrentMallId(),goodId,null);
			int propertyId=goodsJsonStrs.getInteger("propertyId");
			int pricePer=getPricePer(propertyId,mlGoods);
			amountTotle+=pricePer*number;
		}
		Kv para1 = Kv.by("columns", PARAM_COLUMNS).set("mallId", mlUser.getCurrentMallId()).set("keys",YUNFEIKEY);
		SqlPara sqlPara1 = mlParamsDao.getSqlPara("mall.getParams", para1);
		MlParams mlParams=mlParamsDao.findFirst(sqlPara1);
		MlOrder mlOrder=new MlOrder();
		mlOrder.setFreight(Integer.parseInt(mlParams.getParamvalue()));
		mlOrder.put("freightY",DruidKit.changeF2Y(Integer.parseInt(mlParams.getParamvalue())));
		mlOrder.put("isNeedLogistics",0);
		mlOrder.setAmount(amountTotle);
		mlOrder.put("amountY",DruidKit.changeF2Y(amountTotle));
		mlOrder.put("allGoodsAndYunPrice",DruidKit.changeF2Y(amountTotle+Integer.parseInt(mlParams.getParamvalue())));
		return Ret.ok("mlOrder", mlOrder);
	}


	public  int getPricePer(int propertyId,MlGoods mlGoods){
		String goodsAttribute=mlGoods.getGoodsAttribute();
		String [] goodsAttributeArray=goodsAttribute.split("#");
		int pricePer=0;
		for (int i=0;i<goodsAttributeArray.length;i++){
			 String [] arrays=goodsAttributeArray[i].split("=");
			 if (propertyId==Integer.parseInt(arrays[0])){
			 	   String value=arrays[1];
				  pricePer=Integer.parseInt(value.split(",")[2]);
			 }
		}
		return pricePer;
	}


	public  String getPropertyName(int propertyId,MlGoods mlGoods){
		String goodsAttribute=mlGoods.getGoodsAttribute();
		String [] goodsAttributeArray=goodsAttribute.split("#");
		String  propertyName="";
		for (int i=0;i<goodsAttributeArray.length;i++){
			String [] arrays=goodsAttributeArray[i].split("=");
			if (propertyId==Integer.parseInt(arrays[0])){
				String value=arrays[1];
				propertyName=value.split(",")[0];
			}
		}
		return propertyName;
	}


	/**
	 * 判断库存
	 * @param number
	 * @param propertyId
	 * @param mlGoods
	 * @return
	 */
	public  boolean calculteStock(int number,int propertyId,MlGoods mlGoods){
		String goodsAttribute=mlGoods.getGoodsAttribute();
		String [] goodsAttributeArray=goodsAttribute.split("#");
		int stock=0;
		for (int i=0;i<goodsAttributeArray.length;i++){
			String [] arrays=goodsAttributeArray[i].split("=");
			if (propertyId==Integer.parseInt(arrays[0])){
				String value=arrays[1];
				stock=Integer.parseInt(value.split(",")[1]);
			}
		}
		if (stock<number){
			return false;
		}
		return true;
	}

	/**
	 * 扣减库存
	 * @param number
	 * @param propertyId
	 * @param mlGoods
	 * @return
	 */
	public  void DeductionStock(int number,int propertyId,MlGoods mlGoods,int caoZuo){
		String goodsAttribute=mlGoods.getGoodsAttribute();
		String [] goodsAttributeArray=goodsAttribute.split("#");
		StringBuilder sb=new StringBuilder();
		int stock=0;
		for (int i=0;i<goodsAttributeArray.length;i++){

			String [] arrays=goodsAttributeArray[i].split("=");
			sb.append(arrays[0]).append("=");
			if (propertyId==Integer.parseInt(arrays[0])){
				String value=arrays[1];
				String[] valueArray=value.split(",");
				stock=Integer.parseInt(valueArray[1]);
				if (caoZuo==0){
					stock=stock-number;
				}
				else if (caoZuo==1){
					stock=stock+number;
				}

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
		mlGoods.setGoodsAttribute(sb.toString());
		mlGoods.update();
	}


	/**
	 * 更新库存
	 * @return
	 */
	public  void updateStock(){

		String key = LockUtil.getJimKey(ML_GOODS,1);
		boolean isLock = LockUtil.lock(key, 120);
		if (isLock) {
			try {
				Kv para1 = Kv.by("columns", COLUMNS).set("status",MlOrderStatusEnum.ToPay.toCode());
				SqlPara sqlPara1 = dao.getSqlPara("mlOrder.getOrder", para1);
				List<MlOrder> orderList=dao.find(sqlPara1);
				for (MlOrder mlOrder:orderList){
					if (isExpire(new Date(),mlOrder.getOrderTime(),10)){
						String goodsAttribute =mlOrder.getGoodsAttribute();
						String [] goodsAttributeArray=goodsAttribute.split("#");
						for (String goodsAttributess:goodsAttributeArray){
							// 订单商品个数
							int number=Integer.parseInt(goodsAttributess.split(",")[2]);
							int goodsId=Integer.parseInt(goodsAttributess.split(",")[0]);
							int propertiesId=Integer.parseInt(goodsAttributess.split(",")[1]);
							MlGoods mlGoods=MlGoodsAppService.me.goodsDetailByIdAllMall(goodsId);
							DeductionStock(number,propertiesId,mlGoods,1);

						}
						mlOrder.setStatus(MlOrderStatusEnum.Expire.toCode());
						mlOrder.update();
					}

				}
			}
			finally {
				if(isLock){
					LockUtil.unLock(key);
				}
			}

		}

	}

	/**
	 * 更新库存
	 * @return
	 */
	public  boolean isExpire(Date now,Date lastDate,int minitue){
		Calendar dateOne=Calendar.getInstance(), dateTwo=Calendar.getInstance();
		dateOne.setTime(now);	//设置为当前系统时间
		dateTwo.setTime(lastDate);	//设置为2015年1月15日
		long timeOne=dateOne.getTimeInMillis();
		long timeTwo=dateTwo.getTimeInMillis();
		long minute=(timeOne-timeTwo)/(1000*60);//转化minute
		if (minute>=minitue){
			return  true;
		}
		 return  false;
	}



	/**
	 * 下单
	 */
	public Ret 	create(MlUser mlUser, String goodsJsonStr,int addressId,String remark) throws Exception{

		// 目前只是计算运费
		// 获取运费值 目前是在数据库配置 写死的
		int amountTotle=0;
		int goodId=0;
		int numberAll=0;
		StringBuilder attrs= new StringBuilder("");
		for (int i=0;i<JSON.parseArray(goodsJsonStr).size();i++){
			JSONObject goodsJsonStrs= JSON.parseArray(goodsJsonStr).getJSONObject(i);
			goodId=(int)goodsJsonStrs.get("goodsId");
			int number=(int)goodsJsonStrs.get("number");
			numberAll+=number;
			MlGoods mlGoods=srv.goodsDetailById(mlUser.getCurrentMallId(),goodId,null);
			int perPrice=getPricePer(goodsJsonStrs.getInteger("propertyId"),mlGoods);
			amountTotle+=perPrice*number;
			String properties=getPropertyName(goodsJsonStrs.getInteger("propertyId"),mlGoods);
			// 格式是商品名字，商品属性，个数  可爱袜子,白色,1
			/*attrs.append(mlGoods.getGoodsName()).append(",").append(properties).append(",")
					.append(goodsJsonStrs.get("number")).append("#");*/

			attrs.append(String.valueOf(goodId)).append(",").append(goodsJsonStrs.getString("propertyId")).append(",")
					.append(goodsJsonStrs.get("number")).append(",").append(String.valueOf(perPrice)).append("#");

			// 增加分布式锁
			String key = LockUtil.getJimKey(ML_GOODS,goodId);
			boolean isLock = LockUtil.lock(key, 120);
			if (!isLock) {

				throw new SystemException("当前购买人数过多，请稍后重试");
			}
			// 判断库存
			try {
				if (!calculteStock(number,goodsJsonStrs.getInteger("propertyId"),mlGoods)){
					throw new SystemException("商品:"+mlGoods.getGoodsName()+"库存不足,请稍后购买");

				}
				// 扣减库存
				DeductionStock(number,goodsJsonStrs.getInteger("propertyId"),mlGoods,0);
			}
			finally {
				if(isLock){
					LockUtil.unLock(key);
				}
			}


		}
		Kv para1 = Kv.by("columns", PARAM_COLUMNS).set("mallId", mlUser.getCurrentMallId()).set("keys",YUNFEIKEY);
		SqlPara sqlPara1 = mlParamsDao.getSqlPara("mall.getParams", para1);
		MlParams mlParams=mlParamsDao.findFirst(sqlPara1);

		MlOrder mlOrder=new MlOrder();
		mlOrder.setUserId(mlUser.getId());
		// goodId不准  sttr里面的才是真正的商品id
		mlOrder.setGoodsId(goodId);
		mlOrder.setAmount(amountTotle+Integer.parseInt(mlParams.getParamvalue()));
		mlOrder.setAddressId(addressId);
		mlOrder.setCount(numberAll);
		mlOrder.setOrderCode(DruidKit.genRandomNum(8));
		mlOrder.setCreated(new Date());
		mlOrder.setCreator(mlUser.getId());
		mlOrder.setCurrentMallId(mlUser.getCurrentMallId());
		mlOrder.setModified(new Date());
		mlOrder.setModifier(mlUser.getId());
		mlOrder.setOrderTime(new Date());
		mlOrder.setRemark(remark);
		mlOrder.setStatus(MlOrderStatusEnum.ToPay.toCode());
		mlOrder.setFreight(Integer.parseInt(mlParams.getParamvalue()));
		mlOrder.put("isNeedLogistics",1);
		mlOrder.setGoodsAttribute(attrs.substring(0,attrs.length()-1).toString());
		mlOrder.save();


		return Ret.ok("mlOrder", null);
	}

	public List list(MlUser mlUser,int status){
		Kv para1 = Kv.by("columns", COLUMNS).set("userId", mlUser.getId()).set("status",status);
		SqlPara sqlPara1 = dao.getSqlPara("mlOrder.list", para1);
		List mlOderList=dao.find(sqlPara1);
		filterOderList(mlOderList);
		return mlOderList;
	}


	public Page listOrder(Account account,Integer status,Date startTime,Date endTime,Integer page,Integer rows){
		Kv para1 = Kv.by("columns", COLUMNS_BM).set("currentMallId", account.getCurrentMallId()).set("status",status).set("startTime",startTime)
				.set("endTime",endTime);
		SqlPara sqlPara1 = dao.getSqlPara("mlOrder.listOrder", para1);

		Page<MlOrder> page11=dao.paginate(page,rows,sqlPara1);
		filterOderList(page11.getList());
		return page11;
	}





	public Ret orderDetail(MlUser mluser,Integer id) {
		MlOrder mlOrder=getOrderById(id);
		String goods_attribute=mlOrder.getGoodsAttribute();
		String [] attrArrays=goods_attribute.split("#");
		List<MlGoods> list=new ArrayList();
		for (int i=0;i<attrArrays.length;i++){
			String [] sttss=attrArrays[i].split(",");
			int goodsId =Integer.parseInt(sttss[0]);
			MlGoods mlGoods=srv.goodsDetailById(mluser.getCurrentMallId(),goodsId,Integer.parseInt(sttss[1]));
			mlGoods.put("propertyCount",Integer.parseInt(sttss[2]));
            list.add(mlGoods);
		}
		mlOrder.put("goodsAmount",DruidKit.changeF2Y(mlOrder.getAmount()-mlOrder.getFreight()));
		mlOrder.put("amount",DruidKit.changeF2Y(mlOrder.getAmount()));
		mlOrder.put("freight",DruidKit.changeF2Y(mlOrder.getFreight()));
		mlOrder.put("goods",list);
		//目前所有商品不都需要快递信息
		mlOrder.put("logistics",false);
		MlAddress mlAddress=addressSrv.getAddressById(mlOrder.getAddressId());
		mlOrder.put("logistics",mlAddress);
		return Ret.ok("mlOder", mlOrder);
	}



	public void  closeOrder(int id){
		MlOrder mlOrder=dao.findById(id);
		mlOrder.setStatus(DELESTATUS);
		mlOrder.update();
		/*Kv para1 = Kv.by("status", DELESTATUS).set("id",id);
		SqlPara sqlPara1 = dao.getSqlPara("mlOrder.closeOrder", para1);
		MlOrder mlOrder=dao.update(sqlPara1);
		return mlOrder;*/
	}

	public MlOrder getOrderById(int id){
		Kv para1 = Kv.by("columns", COLUMNS).set("id",id);
		SqlPara sqlPara1 = dao.getSqlPara("mlOrder.getOrder", para1);
		MlOrder mlOrder=dao.findFirst(sqlPara1);
		return mlOrder;
	}

	public MlOrder getOrderByOrderCode(String orderCode){
		Kv para1 = Kv.by("columns", COLUMNS).set("orderCode",orderCode);
		SqlPara sqlPara1 = dao.getSqlPara("mlOrder.getOrder", para1);
		MlOrder mlOrder=dao.findFirst(sqlPara1);
		return mlOrder;
	}


	public MlPayLog getPayLog(String key,int value){
		Kv para1 = Kv.by("columns", PAPLOG_COLUMNS).set(key,value);
		SqlPara sqlPara1 = mlPayLogDao.getSqlPara("mlOrder.getPayLog", para1);
		MlPayLog mlPayLog=mlPayLogDao.findFirst(sqlPara1);
		return mlPayLog;
	}



	public void   filterOderList(List<? extends Model> mlOderList){
		for (Model m : mlOderList) {
			int status = m.getInt("status");
			m.put("statusStr", MlOrderStatusEnum.enumValueOf(status).toName());
			m.put("allAmount",DruidKit.changeF2Y(m.getInt("amount")));
		}
	}


	/**
	 * 支付
	 */
	public Ret pay(MlUser mlUser, String remark,String payName,String nextAction) throws Exception {
		Map<String, Integer> nextActionMap = JSON.parseObject(nextAction,Map.class);
		int orderId=nextActionMap.get("id");
		MlOrder mlOrder=getOrderById(orderId);
		String createOrderUrl= PropKit.get("wxpay.createOrderUrl");
		Map<String,Object> result=new HashMap<>();
		result.put("status","1");
		result.put("payType","weixin");
		result.put("orderId",mlOrder.getOrderCode());
		String formData=commitData(mlUser.getOpenid(),mlOrder,payName);
		Map dataMap=new HashMap();
		dataMap.put("xml",formData);
		String httpResult = HttpClientUtil.httpPostRequest(createOrderUrl,dataMap);
		try {
			Map<String, String> resultMap = WXPayUtil.xmlToMap(httpResult);
			result.put("package", "prepay_id=" + resultMap.get("prepay_id"));
			result.put("nonceStr",resultMap.get("nonce_str"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String times= System.currentTimeMillis()+"";
		result.put("timeStamp",times);
		Map<String, String> packageParams = new HashMap<String ,String>();
		packageParams.put("appId", PropKit.get("appid"));
		packageParams.put("signType", PropKit.get("signType"));
		packageParams.put("nonceStr",result.get("nonceStr")+"");
		packageParams.put("timeStamp",times);
		packageParams.put("package", result.get("package")+"");//商户订单号
		String sign="";
		try {
			sign= WXPayUtil.generateSignature(packageParams, PropKit.get("merchant.key"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("paySign",sign);
		createPaylog(mlOrder,remark);
		return Ret.ok("mlPay", result);
	}

	public String commitData(String openId,MlOrder mlOrder,String payName){
		String nonceStr= WXPayUtil.generateNonceStr();
		String body=payName;
		Map<String, String> packageParams = new HashMap<String ,String>();
		packageParams.put("appid", PropKit.get("appid"));
		packageParams.put("body",body);
		packageParams.put("mch_id", PropKit.get("merchant.mch_id"));
		packageParams.put("nonce_str",nonceStr);
		packageParams.put("notify_url", PropKit.get("notify_url") );//支付成功后的回调地址
		packageParams.put("openid",openId+"");//支付方式
		packageParams.put("out_trade_no", mlOrder.getOrderCode());//商户订单号
		packageParams.put("sign_type", PropKit.get("signType"));
		packageParams.put("spbill_create_ip",PropKit.get("spbill_create_ip"));
		packageParams.put("total_fee", String.valueOf(mlOrder.getAmount()));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
		packageParams.put("trade_type", PropKit.get("trade_type"));//支付方式
		String sign="";
		try {
			sign= WXPayUtil.generateSignature(packageParams,PropKit.get("merchant.key"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String formData = "<xml>";
		formData += "<appid>"+ PropKit.get("appid")+"</appid>"; //appid
		formData += "<body>" + body+ "</body>";
		formData += "<mch_id>"+ PropKit.get("merchant.mch_id")+"</mch_id>"; //商户号
		formData += "<nonce_str>"+nonceStr+"</nonce_str>";
		formData += "<notify_url>"+PropKit.get("notify_url")+"</notify_url>";
		formData += "<openid>"+openId+"</openid>";
		formData += "<out_trade_no>" + mlOrder.getOrderCode() + "</out_trade_no>";
		formData += "<sign_type>"+ PropKit.get("signType")+"</sign_type>";
		formData += "<spbill_create_ip>"+PropKit.get("spbill_create_ip")+"</spbill_create_ip>";
		formData += "<total_fee>" + String.valueOf(mlOrder.getAmount()) + "</total_fee>";
		formData += "<trade_type>"+ PropKit.get("trade_type")+"</trade_type>";
		formData += "<sign>"+sign+"</sign>";
		formData += "</xml>";
		return formData;
	}

	public  void createPaylog(MlOrder mlOrder,String remark){
		MlPayLog mlPayLog=new MlPayLog();
		mlPayLog.setUserId(mlOrder.getUserId());
		mlPayLog.setAmount(mlOrder.getAmount());
		mlPayLog.setOrderId(mlOrder.getId());
		mlPayLog.setPayTime(new Date());
		mlPayLog.setCurrentMallId(mlOrder.getCurrentMallId());
		mlPayLog.setCreated(new Date());
		mlPayLog.setCreator(mlOrder.getCreator());
		mlPayLog.setModified(new Date());
		mlPayLog.setModifier(mlOrder.getModifier());
		mlPayLog.setRemark(remark);
		mlPayLog.setStatus(MlPayLogStatusEnum.ToPay.toCode());
		mlPayLog.save();
	}


	public  Boolean notifyPay(String s) throws Exception {
		Format format=new SimpleDateFormat("yyyyMMddHHmmss");
		String key= PropKit.get("merchant.key");
		Map packageParams=WXPayUtil.xmlToMap(s);
		if(WXPayUtil.isSignatureValid(s,key)) {
			String resXml = "";
			String transaction_id = (String)packageParams.get("transaction_id");
			String time_end=(String)packageParams.get("time_end");
			if("SUCCESS".equals((String)packageParams.get("result_code"))){
				//得到返回的参数
				String openid = (String)packageParams.get("openid");

				String out_trade_no = (String)packageParams.get("out_trade_no");
				String total_fee = (String)packageParams.get("total_fee");
				Float fee= Float.parseFloat(total_fee)/100;
				//这里可以写你需要的业务
				MlOrder mlOrder=getOrderByOrderCode(transaction_id);
				// 如果不是待支付 返回
				if (mlOrder.getStatus()!=MlOrderStatusEnum.ToPay.toCode()){
					return true;
				}
				mlOrder.setStatus(MlOrderStatusEnum.TOTransfer.toCode());
				MlPayLog mlPayLog=getPayLog("orderId",mlOrder.getId());
				mlPayLog.setStatus(MlPayLogStatusEnum.PaySeccess.toCode());
				mlPayLog.setPayTime((Date)format.parseObject(time_end));
				Db.tx(() -> {
					mlOrder.save();
					mlPayLog.save();
					return true;
				});
                 return  true;
			} else {
				//这里可以写你需要的业务
				MlOrder mlOrder=getOrderByOrderCode(transaction_id);
				MlPayLog mlPayLog=getPayLog("orderId",mlOrder.getId());
				mlPayLog.setStatus(MlPayLogStatusEnum.PayFail.toCode());
				mlPayLog.setPayTime((Date)format.parseObject(time_end));
				mlPayLog.save();
				log.error("回调失败");
				return true;
			}
		}
		return false;
	}

}
