package cn.dayuanzi.dao;

/**
 * Copyright (c) 2005-2010 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: SimpleHibernateDao.java 1205 2010-09-09 15:12:17Z calvinxiu $
 */

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import cn.dayuanzi.util.ReflectionUtils;



/**
 * 封装Hibernate原生API的DAO泛型基类.
 * 
 * 可在Service层直接使用, 也可以扩展泛型DAO子类使用, 见两个构造函数的注释.
 * 参考Spring2.5自带的Petlinc例子, 取消了HibernateTemplate, 直接使用Hibernate原生API.
 * 
 * @param <T> DAO操作的对象类型
 * @param <PK> 主键类型
 * 
 */
@SuppressWarnings("unchecked")
@Repository
public class BaseDao<PK extends Serializable, T> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;

	/**
	 * 用于Dao层子类使用的构造函数.
	 * 通过子类的泛型定义取得对象类型Class.
	 */
	public BaseDao() {
		this.entityClass = (Class<T>) ReflectionUtils.getSuperClassGenricType(getClass());
	}

	/**
	 * 用于省略Dao层, 在Service层直接使用通用SimpleHibernateDao的构造函数.
	 * 在构造函数中定义对象类型Class.
	 */
	public BaseDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
		this.sessionFactory = sessionFactory;
		this.entityClass = entityClass;
	}

	/**
	 * 取得sessionFactory.
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 采用@Autowired按类型注入SessionFactory, 当有多个SesionFactory的时候在子类重载本函数.
	 */
	@Autowired
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 取得当前Session.
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 保存对象.
	 */
	public void save(final T entity) {
		Assert.notNull(entity, "entity不能为空");
//		getSession().saveOrUpdate(entity);
		getSession().save(entity);
		logger.debug("save entity: {}", entity);
	}
	
	public void saveOrUpdate(final T entity){
		Assert.notNull(entity, "entity不能为空");
		getSession().saveOrUpdate(entity);
		logger.debug("save entity: {}", entity);
	}
	
	/**
	 * 删除对象.
	 * 
	 * @param entity 对象必须是session中的对象或含id属性的transient对象.
	 */
	public void delete(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().delete(entity);
		logger.debug("delete entity: {}", entity);
	}

	/**
	 * 按id删除对象.
	 */
	public T delete(final PK id) {
		Assert.notNull(id, "id不能为空");
		T entity = get(id);
		if(entity==null)
		{
			return null;
		}
		delete(entity);
		logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
		return entity;
	}

	public T get(final PK id) {
		Assert.notNull(id, "id不能为空");
		return (T) getSession().get(entityClass, id);
	}
	
	public T load(final PK id){
		Assert.notNull(id, "id不能为空");
		return (T) getSession().load(entityClass, id);
	}

	/**
	 * 按id列表获取对象列表.
	 */
	public List<T> get(final Collection<PK> ids) {
		return find(Restrictions.in(getIdName(), ids));
	}

	/**
	 *	获取全部对象.
	 */
	public List<T> getAll() {
		return find();
	}

	/**
	 *	获取全部对象, 支持按属性行序.
	 */
	public List<T> getAll(String orderByProperty, boolean isAsc, Criterion... criterions) {
		Criteria c = createCriteria(criterions);
		if (isAsc) {
			c.addOrder(Order.asc(orderByProperty));
		} else {
			c.addOrder(Order.desc(orderByProperty));
		}
		return c.list();
	}

	/**
	 * 按属性查找对象列表, 匹配方式为相等.
	 */
	public List<T> findBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return find(criterion);
	}

	/**
	 * 按属性查找唯一对象, 匹配方式为相等.
	 */
	public T findUniqueBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return (T) createCriteria(criterion).uniqueResult();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public <X> List<X> find(final String hql, final Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public <X> List<X> find(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public <X> X findUnique(final String hql, final Object... values) {
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public <X> X findUnique(final String hql, final Map<String, ?> values) {
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Object... values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param hql 数据脚本
	 * @param values 命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public Query createQuery(final String queryString, final Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	/**
	 * 根据查询sql与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public Query createSqlQuery(final String queryString, final Map<String, ?> values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createSQLQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}
	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public Query createQuery(final String queryString, final Map<String, ?> values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	/**
	 * 按Criteria查询对象列表.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public List<T> find(final Criterion... criterions) {
		return createCriteria(criterions).list();
	}
	
	/**
	 * 按Criteria查询唯一对象.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public T findUnique(final Criterion... criterions) {
		return (T) createCriteria(criterions).uniqueResult();
	}

	/**
	 * 根据Criterion条件创建Criteria.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 初始化对象.
	 * 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化.
	 * 如果传入entity, 则只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性.
	 * 如需初始化关联属性,需执行:
	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合.
	 * Hibernate.initialize(user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initProxyObject(Object proxy) {
		Hibernate.initialize(proxy);
	}

	/**
	 * Flush当前Session.
	 */
	public void flush() {
		getSession().flush();
	}

	/**
	 * 为Query添加distinct transformer.
	 * 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Query distinct(Query query) {
		query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return query;
	}

	/**
	 * 为Criteria添加distinct transformer.
	 * 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Criteria distinct(Criteria criteria) {
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

	/**
	 * 取得对象的主键名.
	 */
	public String getIdName() {
		ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
		return meta.getIdentifierPropertyName();
	}

	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
	 */
	public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
		if (newValue == null || newValue.equals(oldValue)) {
			return true;
		}
		Object object = findUniqueBy(propertyName, newValue);
		return (object == null);
	}
	
	/**
	 * 分页查询
	 * 
	 * @param hql
	 * @param current_page
	 * @param max_num
	 * @param map
	 * @return
	 */
	public List<T> findForPage(String hql, int current_page, int max_num, Map<String, Object> map) {
		List<T> result = null;
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		try {
			if (!hql.contains("where")) {
	               hql = hql.concat(" where 1=1 ");
				}
			if (map != null) {
				Set<String> keys = map.keySet();
				for (String key : keys) {
					hql = hql.concat(" and " + key + " =:" + key);
				}
			}
			Query query = this.getSession().createQuery(hql);
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				query.setParameter(key, map.get(key));
			}
			query.setFirstResult(((current_page-1)) * max_num);
			query.setMaxResults(max_num);
			result = query.list();
		} catch (RuntimeException re) {
			throw re;
		}
		return result;
	}
	
	/**
	 * 带排序的分页查询
	 * 
	 * @param current_page >=1
	 * @param max_num >=1 <=1000
	 * @param map
	 * @param orderByProperty
	 * @param isAsc
	 * @return
	 */
	public List<T> findForPage(int current_page, int max_num, Map<String, Object> map, String orderByProperty, boolean isAsc) {
		List<T> result = null;
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		Criteria c = createCriteria();
		if(MapUtils.isNotEmpty(map)){
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				c.add(Restrictions.eq(key, map.get(key)));
			}
		}
		c.setFirstResult(((current_page-1)) * max_num);
		c.setMaxResults(max_num);
		if(StringUtils.isNotBlank(orderByProperty)){
			if (isAsc) {
				c.addOrder(Order.asc(orderByProperty));
			} else {
				c.addOrder(Order.desc(orderByProperty));
			}
		}
		result =  c.list();
		return result;
	}
	
	public List<T> findForPage(int current_page, int max_num, String orderByProperty, boolean isAsc, final Criterion... criterions){
		List<T> result = null;
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		Criteria c = createCriteria(criterions);
		c.setFirstResult(((current_page-1)) * max_num);
		c.setMaxResults(max_num);
		if(StringUtils.isNotBlank(orderByProperty)){
			if (isAsc) {
				c.addOrder(Order.asc(orderByProperty));
			} else {
				c.addOrder(Order.desc(orderByProperty));
			}
		}
		result =  c.list();
		return result;
	}
	
	/**
	 * 查询符合条件的数据条数
	 * 
	 * @param hql
	 * @param map
	 * @return
	 */
	public int count(String hql, Map<String, Object> map){
		int result = 0;
		try {
			if (!hql.contains("where")) {
	               hql = hql.concat(" where 1=1 ");
				}
			if (map != null) {
				Set<String> keys = map.keySet();
				for (String key : keys) {
					hql = hql.concat(" and " + key + " =:" + key);
				}
			}
			Query query = this.getSession().createQuery(hql);
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				query.setParameter(key, map.get(key));
			}
			result = query.list().size();
		} catch (RuntimeException re) {
			throw re;
		}
		return result;
	}
	
	/**
	 * 统计数量
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public long count(final String propertyName, final Object value){
		Assert.hasText(propertyName, "propertyName不能为空");
		return count(Restrictions.eq(propertyName, value));
	}
	
	/**
	 * 多条件统计数量
	 * @param criterions
	 * @return
	 */
	public long count(final Criterion... criterions){
		Criteria criteria = createCriteria(criterions);
		criteria.setProjection(Projections.rowCount());
		return (long)criteria.uniqueResult();
	}
}