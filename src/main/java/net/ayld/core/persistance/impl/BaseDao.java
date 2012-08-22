package net.ayld.core.persistance.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import net.ayld.core.domain.BaseEntity;
import net.ayld.core.persistance.Dao;

import org.hibernate.SessionFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;

@Transactional
public abstract class BaseDao<E extends BaseEntity<I>, I extends Serializable> implements Dao<E, I>{

	private SessionFactory sessionFactory;

	@Override
	public E create(E entity) {
		@SuppressWarnings("unchecked")
		final I id = (I) sessionFactory.getCurrentSession().save(entity);
		
		return read(id);
	}

	@Override
	public E createOrUpdate(E entity) {
		if (find(entity.getId()) == null) {
			
			return create(entity);
		}
		return update(entity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public E read(I id) {
		return (E) sessionFactory.getCurrentSession().load(getEntityClass(), id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E find(I id) {
		return (E) sessionFactory.getCurrentSession().get(getEntityClass(), id);
	}

	@Override
	public Set<E> list() {
		@SuppressWarnings("unchecked")
		final List<E> list = sessionFactory.getCurrentSession().createQuery("from " + this.getEntityClass().getSimpleName()).list();
		
		return ImmutableSet.<E>builder().addAll(list).build();
	}

	@Override
	public E update(E entity) {
		sessionFactory.getCurrentSession().update(entity);
		
		return read(entity.getId());
	}

	@Override
	public E delete(I id) {
		E entity = read(id);

		sessionFactory.getCurrentSession().delete(entity);
		
		return entity;
	}

	private Class<?> getEntityClass() {
		final Class<?> result = GenericTypeResolver.resolveTypeArguments(getClass(), BaseDao.class)[0];
		if (result == null) {
			
			throw new IllegalArgumentException("can not resolve type argument for DAO: " + getClass().getSimpleName());
		}
		return result;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private static final long serialVersionUID = 1L;
}
