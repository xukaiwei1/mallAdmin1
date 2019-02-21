
#sql("getMall")
	select #(columns) from ml_mall where mall_code = #para(mlCode) order by created desc
#end

#sql("getMallBanner")
	select #(columns) from ml_resource where current_mall_id = #para(mallId) and resource_type = #para(resourceType) order by orders asc
#end

#sql("getMallAll")
	select #(columns) from ml_mall order by created desc
#end

#sql("getMallById")
	select #(columns) from ml_mall where id = #para(id)
#end

#sql("updateMallStatusById")
	update ml_mall set status = #para(status),modified = new Date() ,where id=#para(id)
#end

#sql("getGoodsType")
	select #(columns) from ml_goods_type where current_mall_id = #para(mallId) order by ID asc
#end



#sql("getParams")
	select #(columns) from ml_params  where current_mall_id = #para(mallId) and paramkey=#para(keys)
#end






