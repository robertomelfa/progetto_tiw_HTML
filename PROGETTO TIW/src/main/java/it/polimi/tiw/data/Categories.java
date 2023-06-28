package it.polimi.tiw.data;

public class Categories {
	private int id;
	private int category;
	private String name;
	private int father;
	
	public Categories() {
		super();
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void setCategory(int category) {
		this.category = category;
	}
	
	public int getCategory() {
		return this.category;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setFather(int father) {
		this.father = father;
	}
	
	public int getFather() {
		return this.father;
	}
}