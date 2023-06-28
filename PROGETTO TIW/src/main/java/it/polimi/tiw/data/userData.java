package it.polimi.tiw.data;

public class userData {
	private Integer id;
	private String name;
	private String surname;
	private String region;
	private String password;
	
	public userData() {
		super();
	}
	
	public userData(Integer id, String name, String surname, String region, String password) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.surname = surname;
		this.region = region;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getSurname() {
		return this.surname;
	}
	
	public void setRegion(String region) {
		this.region = region;
	}
	
	public String getRegion() {
		return this.region;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
}