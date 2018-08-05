package com.admin.ac.ding.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author lareina_h
 * @version 1.0
 * @date 2018/5/10
 */

public class BaseModel implements Serializable {
	private static final long serialVersionUID = -6332620263145558954L;
	public static final String ID = "id";
	public static final String GMT_CREATE = "gmt_create";

	public BaseModel() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	/**
	 * 删除状态
	 */
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "gmt_create")
	private Date gmtCreate;

	@Column(name = "gmt_modify")
	private Date gmtModify;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * 获取删除状态
	 *
	 * @return is_deleted - 删除状态
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * 设置删除状态
	 *
	 * @param isDeleted 删除状态
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return gmt_create
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}

	/**
	 * @param gmtCreate
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	/**
	 * @return gmt_modify
	 */
	public Date getGmtModify() {
		return gmtModify;
	}

	/**
	 * @param gmtModify
	 */
	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

}
