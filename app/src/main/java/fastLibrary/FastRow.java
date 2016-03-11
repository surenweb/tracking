package fastLibrary;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastRow {

	List<String> rowColumn = new ArrayList<String>();
	List<String> rowValue = new ArrayList<String>();
			
	public void add(String col, String val){
		rowColumn.add(col);
		rowValue.add(val);
	}
	
	public String get(int index ) {
		return rowValue.get(index) ;
	}
	
	//## BUG 
	public String get(String colName ) {
		int index = rowColumn.indexOf(colName);		
		if(index>-1)
			return rowValue.get(index) ;
		else  
			return ""; //NPE
	}
}
