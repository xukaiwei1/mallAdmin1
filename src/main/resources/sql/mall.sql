
#sql("getMall")
	select #(columns) from ml_mall where mall_code = #para(mlCode) order by created desc
#end

#sql("getMallBanner")
	select #(columns) from ml_resource where current_mall_id = #para(mallId) and resource_type = #para(resourceType) order by orders asc
#end







