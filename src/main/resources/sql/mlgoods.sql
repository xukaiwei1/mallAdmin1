#sql("paginate")
SELECT g.goods_code,g.goods_name,g.goods_introduce,g.order,g.price,g.count,g.count_selling,g.created,g.status,t.type_name from
ml_goods g LEFT JOIN ml_goods_type t on g.goods_type_id=t.id
where 1=1
#if(goodsTypeId!=null)
and  g.goods_type_id=#para(goodsTypeId)
#end
#if(goodsName!=null)
and  g.goods_name like concat('%', #para(goodsName), '%')
#end
#end





