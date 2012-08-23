package net.ayld.core.persistance.impl;

import org.springframework.transaction.annotation.Transactional;

import net.ayld.core.domain.TestEntity;

@Transactional
public class TestDaoImpl extends BaseDao<TestEntity, Integer> implements TestDao{

	private static final long serialVersionUID = 1L;

}
