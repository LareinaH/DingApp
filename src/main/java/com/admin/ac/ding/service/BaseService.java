package com.admin.ac.ding.service;

import com.admin.ac.ding.model.BaseModel;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *
 * @author lareina_h
 * @version 1.0
 * @date 2018/5/10
 */

public interface BaseService<ModelType extends BaseModel> {
	boolean insert(ModelType model);

	boolean update(ModelType model);

	boolean delete(ModelType model);

	boolean deleteById(Long id);

	ModelType getById(Long id);

	ModelType selectOne(ModelType model);

	long count(ModelType model);

	long count(Object condition);

	List<ModelType> queryList();

	List<ModelType> queryList(ModelType model);

	List<ModelType> queryList(Object condition);

	/**
	 * 分页查询
	 * 
	 * @param pageNum
	 *            页码，从0开始
	 * @param pageSize
	 *            页大小，从0开始。取0则不分页
	 * @return
	 */
	PageInfo<ModelType> query(int pageNum, int pageSize);

	/**
	 * 分页查询
	 * 
	 * @param pageNum
	 *            页码，从0开始
	 * @param pageSize
	 *            页大小，从0开始。取0则不分页
	 * @param model
	 *            查询条件
	 * @return
	 */
	PageInfo<ModelType> query(int pageNum, int pageSize, ModelType model);

	/**
	 * 分页查询
	 * 
	 * @param pageNum
	 *            页码，从0开始
	 * @param pageSize
	 *            页大小，从0开始。取0则不分页
	 * @param condition
	 *            查询条件 （Example）
	 * @return
	 */
	PageInfo<ModelType> query(int pageNum, int pageSize, Object condition);
}
