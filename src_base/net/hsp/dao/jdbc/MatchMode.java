package net.hsp.dao.jdbc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class MatchMode implements Serializable {
	private final String name;
	private static final Map INSTANCES = new HashMap();

	protected MatchMode(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public static final MatchMode EXACT = new MatchMode("EXACT") {
		public String toMatchString(String pattern) {
			return pattern;
		}
	};
 
	public static final MatchMode START = new MatchMode("START") {
		public String toMatchString(String pattern) {
			return pattern + '%';
		}
	};

	public static final MatchMode END = new MatchMode("END") {
		public String toMatchString(String pattern) {
			return '%' + pattern;
		}
	};

	public static final MatchMode ANYWHERE = new MatchMode("ANYWHERE") {
		public String toMatchString(String pattern) {
			return '%' + pattern + '%';
		}
	};

	static {
		INSTANCES.put(EXACT.name, EXACT);
		INSTANCES.put(END.name, END);
		INSTANCES.put(START.name, START);
		INSTANCES.put(ANYWHERE.name, ANYWHERE);
	}

	private Object readResolve() {
		return INSTANCES.get(name);
	}

	public abstract String toMatchString(String pattern);

}
