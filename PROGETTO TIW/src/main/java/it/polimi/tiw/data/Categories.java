package it.polimi.tiw.data;

import java.util.ArrayList;
import java.util.List;

public class Categories {
	private int id;
	private String category;
	private String name;
	private int father;
	private boolean copy = true;
	private List<Categories> subCategories = new ArrayList<Categories>();
	
	public Categories() {
		super();
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
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
	
	public void addSubCategory(Categories category) {
		subCategories.add(category);
	}
	
	public List<Categories> getSubCategories(){
		return subCategories;
	}
	
	public void setCopy() {
		this.copy = false;
	}
	
	public boolean getCopy() {
		return this.copy;
	}
	
	public boolean contains(String string) {
		for(int i = 0; i < string.length(); i++) {
			if(category.charAt(i) != string.charAt(i)) {
				return false;
			}
		}
		return true;
	}
}