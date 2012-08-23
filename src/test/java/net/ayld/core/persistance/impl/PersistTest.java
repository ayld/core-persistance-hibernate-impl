package net.ayld.core.persistance.impl;

import java.util.Set;

import junit.framework.Assert;
import net.ayld.core.domain.TestEntity;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:**/test-context.xml" })
public class PersistTest {
	
	private TestDao testDao;
	private SessionFactory sessionFactory;
	
	private Integer testEntity1id;
	private Integer testEntity2id;
	private final String testEntity1name = "e1";
	private final String testEntity2name = "e2";
	
	@Before
	public void setup() {
		final Session hSession = sessionFactory.openSession();
		hSession.beginTransaction();
		
		final TestEntity testEntity1 = new TestEntity();
		testEntity1.setName(testEntity1name);
		testEntity1id = (Integer) hSession.save(testEntity1);
		
		final TestEntity testEntity2 = new TestEntity();
		testEntity2.setName(testEntity2name);
		testEntity2id = (Integer) hSession.save(testEntity2);
		
		hSession.getTransaction().commit();
		hSession.flush();
		hSession.close();
	}
	
	@Test
	@Transactional
	public void createTest() {
		final String expectedName = "banica brat";
		
		final TestEntity testEntity = new TestEntity();
		testEntity.setName(expectedName);
		
		final TestEntity created = testDao.create(testEntity);
		
		Assert.assertEquals(expectedName, created.getName());
	}
	
	@Test
	@Transactional
	public void readTest() {
		final TestEntity read = testDao.read(testEntity1id);
		
		validate(read);
		
		try {
			final TestEntity notRead = testDao.read(Integer.MAX_VALUE);
			System.out.println(notRead.getName()); // XXX print it so the compiler doesn't optimize it out
			
		} catch (ObjectNotFoundException e) {
			
			// ok
			return;
		}
		Assert.fail();
	}
	
	@Test
	@Transactional
	public void findTest() {
		final TestEntity found = testDao.find(testEntity1id);
		if (found == null) {
			
			Assert.fail();
		}
		validate(found);
		
		final TestEntity notFound = testDao.find(Integer.MAX_VALUE);
		if (notFound != null) {
			
			Assert.fail();
		}
	}

	@Test
	@Transactional
	public void listTest() {
		final Set<TestEntity> listed = testDao.list();
		if (listed.size() < 2) {
			
			Assert.fail();
		}
		for (TestEntity te : listed) {
			
			final Integer id = te.getId();
			Assert.assertNotNull(id);
			
			final String name = te.getName();
			Assert.assertNotNull(name);
			
			Assert.assertTrue(id > 0 && (name.length() > 0));
		}
	}
	
	@Test
	@Transactional
	public void updateTest() {
		final String updateName = "updated";
		
		final TestEntity read = testDao.read(testEntity1id);
		read.setName("updated");
		
		final TestEntity updated = testDao.update(read);
		
		Assert.assertEquals(testEntity1id, updated.getId());
		Assert.assertEquals(updateName, updated.getName());
	}
	
	private void validate(final TestEntity entity) {
		Assert.assertEquals(testEntity1id, entity.getId());
		Assert.assertEquals(testEntity1name, entity.getName());
	}
	
	@Autowired
	public void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
