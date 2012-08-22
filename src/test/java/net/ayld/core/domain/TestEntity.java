package net.ayld.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "test_stuff")
public class TestEntity extends BaseEntity<Integer>{

	private String name;
	private Integer id;
	
	
	public void setName(String name) {
		this.name = name;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	@Id
	@Override
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}


	private static final long serialVersionUID = 1L;
}
