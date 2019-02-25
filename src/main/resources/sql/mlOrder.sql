
#sql("list")
	select #(columns) from ml_order  where user_id = #para(userId)
#if(status!=null&&status!='')
and  status =#para(status)
#end
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






