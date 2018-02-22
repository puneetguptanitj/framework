package com.teradata.appcenter.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;

public interface AbstractDao<E, I extends Serializable> {
   E getById(I id);
   
   E saveOrUpdate(E e);

   void delete(E e);

   void delete(I id);

   List<E> findByCriteria(Criterion criterion);
   
   List<E> findByCriteria(List<Criterion> list);

   void flush();

   Object saveOrUpdateObject(Object o);

   void refresh(E e);
   
   List<E> getAll();
   
}
