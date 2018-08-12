package com.admin.ac.ding.base;

import com.admin.ac.ding.model.BaseModel;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 *
 * @author lareina_h
 * @version 1.0
 * @date 2018/5/10
 */

public interface BaseMapper<ModelType extends BaseModel> extends Mapper<ModelType>, MySqlMapper<ModelType> {
}
