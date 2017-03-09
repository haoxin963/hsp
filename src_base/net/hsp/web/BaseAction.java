package net.hsp.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.hsp.service.BaseService;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

public abstract class BaseAction<T extends java.io.Serializable> {

	protected abstract void init();

	protected String listURL;
	protected String formURL;
	protected String command = "command";

	protected Class<T> clazz;
  
	@Autowired
	@Qualifier("baseServiceImpl")
	protected BaseService baseService;

	public BaseAction() {
		init();
		//this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@RequestMapping("/doSave") 
	public ModelAndView doSave(T o, HttpServletRequest req, HttpServletResponse res) {
		try {
			baseService.save(o);
			return new MV(listURL, command, o).forward();
		} catch (Exception e) {
			return new MV().processException(e,null);
		} 
	}

	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(T o, HttpServletRequest req, HttpServletResponse res) {
		try {
			String pkStr = req.getParameter("pks");
			String[] pks = null;
			if (StringUtils.isNotBlank(pkStr)) {
				pks = req.getParameter("pks").split(",");
			}
			if (pks!=null) { 
				baseService.batchDelete(o.getClass(),pks);
			}else{
				baseService.delete(o);
			}
			return new MV(listURL, command, o).forward();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	@RequestMapping("/form")
	public ModelAndView form(T o, HttpServletRequest req, HttpServletResponse res) {
		try {
			return new MV(formURL, command, o,this).forward();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	@RequestMapping("/formEdit")
	public ModelAndView formEdit(T o, HttpServletRequest req, HttpServletResponse res) {
		try {
			return new MV(formURL, command, baseService.findById(o)).forward();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	@RequestMapping("/doUpdate") 
	public ModelAndView doUpdate(T o,HttpServletRequest req, HttpServletResponse res) {
		try {
			baseService.update(o); 
			return new MV(listURL, command, o).forward();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	@RequestMapping("/doUpdates")
	public ModelAndView doUpdates(List<T> list,HttpServletRequest req, HttpServletResponse res) {
		try {
			baseService.update(list); 
			return new MV(listURL, command, list).forward();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	@RequestMapping("/doSelect")
	public ModelAndView doSelect(T o,HttpServletRequest req, HttpServletResponse res,PageInfo pageInfo) {
		try {
			Map<String, Object> map = baseService.findPageInfo2Entity(o.getClass(), pageInfo); 
			return new MV(listURL, command, map).forward();
		}catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}
	
	
	
	
	
//	@RequestMapping("/doList")
//	public ModelAndView doList(HttpServletRequest req, HttpServletResponse res,PageInfo pageInfo) {
//		try { 
//			Map<String, Object> map = baseService.execute(req.getParameterMap(),pageInfo);  
//			return new MV(listURL, command, map).forward(req, res);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		} 
//	}

	
	
}