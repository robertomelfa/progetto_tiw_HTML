package it.polimi.tiw.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch{
	
	private Categories root;
	
	public Branch() {
		this.root = new Categories();
	}
	
	public Categories getRoot() {
		return root;
	}
	
	public void setTree(List<Categories> categoriesList) {
		Map<Integer, Categories> categoriesMap = new HashMap<>();

		
		for(Categories category : categoriesList) {
			categoriesMap.put(category.getID(), category);
		}
		
		for(Categories category : categoriesList) {
			int idFather = category.getFather();
			Categories father = categoriesMap.get(idFather);
			
			if(father != null) {
				father.addSubCategory(category);
			}else {
				root.addSubCategory(category);
			}
		}
	}
}