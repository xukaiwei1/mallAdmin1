
#sql("list")
	select #(columns) from ml_order  where 1=1
#if(userId!=null&&userId!='')
and  user_id =#para(userId)
#end
#if(status!=null&&status!='')
and  status =#para(status)
#end
#if(currentMallId!=null&&currentMallId!='')
and  current_mall_id =#para(currentMallId)
#end
#if(startTime!=null&&startTime!='')
and  order_time >=#para(startTime)
#end
#if(endTime!=null&&endTime!='')
and  order_time <=#para(endTime)
#end
#end



#sql("listOrder")
	select #(columns) from ml_order mlo LEFT JOIN ml_user mlu on mlo.user_id =mlu.id  LEFT JOIN ml_goods mlg  on mlo.goods_id=mlg.id  where 1=1
#if(userId!=null&&userId!='')
and  mlo.user_id =#para(userId)
#end
#if(status!=null&&status!='')
and  mlo.status =#para(status)
#end
#if(currentMallId!=null&&currentMallId!='')
and  mlo.current_mall_id =#para(currentMallId)
#end
#if(startTime!=null&&startTime!='')
and  mlo.order_time >=#para(startTime)
#end
#if(endTime!=null&&endTime!='')
and  mlo.order_time <=#para(endTime)
#end
order by order_time desc
#end

#sql("getOrder")
	select #(columns) from ml_order  where 1=1
#if(status!=null&&status!='')
and  status =#para(status)
#end
#if(id!=null&&id!='')
and  id = #para(id)
#end
#if(orderCode!=null&&orderCode!='')
and  order_code =#para(orderCode)
#end
#end

#sql("getPayLog")
	select #(columns) from ml_pay_log  where 1=1
#if(status!=null&&status!='')
and  status =#para(status)
#end
#if(id!=null&&id!='')
and  id = #para(id)
#end
#if(orderId!=null&&orderId!='')
and  order_id =#para(orderId)
#end
order by created desc
#end


#sql("closeOrder")
UPDATE   ml_order set status =#para(status)
where 1=1
#if(id!=null&&id!='')
and  id = #para(id)
#end
#end







