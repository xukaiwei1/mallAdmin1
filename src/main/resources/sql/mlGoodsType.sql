#sql("goodsList")
	select #(columns) from ml_goods_type gd LEFT JOIN   account ac  on gd.creator=ac.id  where 1=1
#if(typeName!=null&&typeName!='')
and  gd.type_name =#para(typeName)
#end
#if(currentMallId!=null&&currentMallId!='')
and  gd.current_mall_id =#para(currentMallId)
#end
#if(startTime!=null&&startTime!='')
and  gd.created >=#para(startTime)
#end
#if(endTime!=null&&endTime!='')
and  gd.created <=#para(endTime)
#end
#end