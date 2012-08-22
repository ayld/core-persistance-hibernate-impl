package net.ayld.core.persistance.impl;

import net.ayld.core.domain.TestEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:**/test-context.xml" })
public class PersistTest {
	
	private TestDao testDao;
	
	@Test
	public void createTest() {
		
		testDao.create(new TestEntity());
	}

	@Autowired
	public void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}
}
