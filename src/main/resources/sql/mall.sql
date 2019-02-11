
#sql("getMall")
	select #(columns) from ml_mall where mall_code = #para(mlCode) order by created desc
#end

#sql("getMallBanner")
	select #(columns) from ml_resource where mall_code = #para(mlCode) order by created desc
#end







