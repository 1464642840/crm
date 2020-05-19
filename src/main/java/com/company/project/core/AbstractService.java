package com.company.project.core;


import com.company.project.model.Person;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 */

public abstract class AbstractService<T> implements Service<T> {

    @Autowired
    protected Mapper<T> mapper;

    private Class<T> modelClass;    // 当前泛型真实类型的Class

    public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }
    @Override
    public int save(T model) {
        int i = mapper.insertSelective(model);
        return i;
    }
    @Override
    public T saveOneKey(T model) {
        mapper.insertUseGeneratedKeys(model);
        return model;
    }
    @Override
    public void save(List<T> models) {
        mapper.insertList(models);
    }
    @Override
    public void deleteById(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }
    @Override
    public void update(T model) {
        mapper.updateByPrimaryKeySelective(model);
    }
    @Override
    public T findById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public T findBy(String fieldName, Object value) throws TooManyResultsException {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
    @Override
    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }
    @Override
    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }
    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }
    @Override
    public void updateSelective(T model){
        mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public int saveSelective(T model) {
      return  mapper.insertSelective(model);

    }

    @Override
    public T getByT(T t) {
        return mapper.selectOne(t);
    }

    @Override
    public T saveSelectOneKey(T t) {
        Class<?> clazz = t.getClass();
        clazz.getDeclaredFields();
        

        return null;
    }
}
