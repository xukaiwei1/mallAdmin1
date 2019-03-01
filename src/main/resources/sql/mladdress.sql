
#sql("defaultAddress")
	select #(columns) from ml_address  where current_mall_id = #para(mallId) and user_id = #para(userId) and is_default=0
#end
#sql("list")
	select #(columns) from ml_address  where current_mall_id = #para(mallId) and user_id = #para(userId)
#end

#sql("getAddressById")
	select #(columns) from ml_address  where id = #para(id)
#end


