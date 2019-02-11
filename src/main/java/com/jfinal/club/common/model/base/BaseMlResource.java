package com.jfinal.club.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseMlResource<M extends BaseMlResource<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setResourceName(java.lang.String resourceName) {
		set("resource_name", resourceName);
	}
	
	public java.lang.String getResourceName() {
		return getStr("resource_name");
	}

	public void setResourceType(java.lang.Integer resourceType) {
		set("resource_type", resourceType);
	}
	
	public java.lang.Integer getResourceType() {
		return getInt("resource_type");
	}

	public void setResourceValue(java.lang.String resourceValue) {
		set("resource_value", resourceValue);
	}
	
	public java.lang.String getResourceValue() {
		return getStr("resource_value");
	}

	public void setParentResource(java.lang.Integer parentResource) {
		set("parent_resource", parentResource);
	}
	
	public java.lang.Integer getParentResource() {
		return getInt("parent_resource");
	}

	public void setCreator(java.lang.Integer creator) {
		set("creator", creator);
	}
	
	public java.lang.Integer getCreator() {
		return getInt("creator");
	}

	public void setCreated(java.util.Date created) {
		set("created", created);
	}
	
	public java.util.Date getCreated() {
		return get("created");
	}

	public void setModifier(java.lang.Integer modifier) {
		set("modifier", modifier);
	}
	
	public java.lang.Integer getModifier() {
		return getInt("modifier");
	}

	public void setModified(java.util.Date modified) {
		set("modified", modified);
	}
	
	public java.util.Date getModified() {
		return get("modified");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}
	
	public java.lang.Integer getStatus() {
		return getInt("status");
	}

	public void setCurrentMallId(java.lang.Integer currentMallId) {
		set("current_mall_id", currentMallId);
	}
	
	public java.lang.Integer getCurrentMallId() {
		return getInt("current_mall_id");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}
	
	public java.lang.String getRemark() {
		return getStr("remark");
	}

}
