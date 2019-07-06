#sql("paginate")
SELECT g.goods_code,g.goods_name,g.goods_introduce,g.orders,g.price,g.count,g.count_selling,g.created,g.status,g.banner_picture,t.type_name from
ml_goods g LEFT JOIN ml_goods_type t on g.goods_type_id=t.id
where 1=1
#if(goodsTypeId!=null&&goodsTypeId!='')
and  g.goods_type_id=#para(goodsTypeId)
#end
#if(goodsName!=null&&goodsName!='')
and  g.goods_name like concat('%', #para(goodsName), '%')
#end
#if(currentMallId!=null&&currentMallId!='')
and  g.current_mall_id =#para(currentMallId)
#end
#end



#sql("paginateApp")
SELECT g.id,g.goods_code,g.goods_name,g.goods_introduce,g.orders,g.price,g.count,g.count_selling,g.created,g.status,g.banner_picture from
ml_goods g
where 1=1
#if(goodsTypeId!=null&&goodsTypeId!='')
and  g.goods_type_id=#para(goodsTypeId)
#end
#if(goodsName!=null&&goodsName!='')
and  g.goods_name like concat('%', #para(goodsName), '%')
#end
#if(currentMallId!=null&&currentMallId!='')
and  g.current_mall_id =#para(currentMallId)
#end
#if(status!=null&&status!='')
and  g.status =#para(status)
#end
#end



#sql("detail")
	select #(columns) from ml_goods  where 1=1
	#if(mallId!=null&&mallId!='')
  and  	current_mall_id = #para(mallId)
  #end
  #if(id!=null&&id!='')
  and id = #para(id)
  #end
#end




