package net.hsp.web.util;

import java.util.HashMap;
import java.util.Set;

public class DicValueMap extends HashMap{
	private String n;
	private String i;
	
	public String getN() {
		return super.get("n")+"";
	}

	public void setN(String n) {
		this.n = n;
	}

	public String getI() {
		return super.get("i")+"";
	}

	public void setI(String i) {
		this.i = i;
	}

	@Override
	public Object get(Object key) {  
		return super.get(key);
	}

	@Override
	public String toString() { 
		return super.toString();
	}
	
	
	
	
}
