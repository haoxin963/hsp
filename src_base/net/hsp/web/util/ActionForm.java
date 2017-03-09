package net.hsp.web.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 前端map数据封装
 * 
 * @author Administrator
 * 
 */
public class ActionForm {
	/**
	 * 
	 * 
	 * <form action="/hello/getMap" method="post">
	 *  <input name="mapVo['a'].name"> 
	 *  <input name="mapVo['a'].password" type="password"> 
	 *  <input name="mapVo['b'].name">
	 *  <input name="mapVo['b'].password" type="password">
	 *  <input type="submit"value="submit">
	 *  </form>
	 */
	private Map m = new LinkedHashMap();

	public Map getM() {
		return m;
	}

	public void setM(Map m) {
		this.m = m;
	}
}
