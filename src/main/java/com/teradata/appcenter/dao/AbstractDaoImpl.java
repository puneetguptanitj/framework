package com.teradata.appcenter.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public abstract class AbstractDaoImpl<E, I extends Serializable> implements AbstractDao<E, I> {
	@Autowired 
	protected SessionFactory sessionFactory;
	private Class<E> entityClass;

	protected AbstractDaoImpl() {
		//use reflection to identify entity class from generics
		if (getClass().getGenericSuperclass() instanceof ParameterizedType){
			ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
			Type[] types=genericSuperclass.getActualTypeArguments();
			this.entityClass = (Class<E>) types[0];
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public E getById(I id) {
		return (E) getCurrentSession().get(entityClass, id);
	}

	@Override
	@Transactional(readOnly=false,propagation = Propagation.REQUIRES_NEW )
	public E saveOrUpdate(E e) {
		return (E)getCurrentSession().merge(e);
	}

	@Override
	@Transactional(readOnly=false,propagation = Propagation.REQUIRES_NEW )
	public void refresh(E e) {
		getCurrentSession().refresh(e);
	}

	@Override
	@Transactional(readOnly=false,propagation = Propagation.REQUIRES_NEW )
	public void delete(E e) {
		getCurrentSession().delete(e);
	}

	@Override
	@Transactional(readOnly=false,propagation = Propagation.REQUIRES_NEW )
	public void delete(I id) {
		E entity=(E)getCurrentSession().get(entityClass, id);
		getCurrentSession().delete(entity);
	}

	@Override
	public List<E> findByCriteria(Criterion criterion) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(criterion);
		return criteria.list();
	}

	@Override
	public List<E> findByCriteria(List<Criterion> list) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		for(Criterion criterion: list ){
			criteria.add(criterion);
		}
		return criteria.list();
	}

	@Override
	public void flush() {
		getCurrentSession().flush();
	}

	@Override
	@Transactional(readOnly=false,propagation = Propagation.REQUIRES_NEW )
	public Object saveOrUpdateObject(Object o) {
		return getCurrentSession().merge(o);
	}

	public E findOneByHql(String hql, Object... params){
		Query q=getCurrentSession().createQuery(hql);
		if (params!=null){
			for (int i = 0; i < params.length; i++) {
				Object param = params[i];
				q.setParameter(i, param);
			}
		}
		return (E)q.uniqueResult();
	}

	@Override
	public List<E> getAll(){
		return getCurrentSession().createCriteria(entityClass).list();
	}
}