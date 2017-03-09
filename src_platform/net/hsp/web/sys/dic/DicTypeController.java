package net.hsp.web.sys.dic;

import java.util.Map;

import net.hsp.entity.sys.dic.DicTypeEntity;
import net.hsp.service.sys.dic.DictionaryService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/dic/dicType")
public class DicTypeController {

	@Autowired
	private DictionaryService service;

	private String listURL = "/sys/dic/dicTypeList";
	private String formURL = "/sys/dic/dicTypeForm";

	/**
	 * 刷新ApplicationScope cache(临时方案，多jvm时仍不同步)
	 */
	protected static void sync(){ 
		Map map = (Map)ActionUtil.getCtx().getServletContext().getAttribute("dics");
		map.remove(HttpSessionFactory.getCustId());
		
		Map dicsMap = (Map)ActionUtil.getCtx().getServletContext().getAttribute("dicsMap");
		dicsMap.remove(HttpSessionFactory.getCustId()); 
	}
	
	@RequestMapping("/doList")
	public ModelAndView doList(FilterMap filter, PageInfo pageInfo) {
		Map<?, ?> map = service.listDicTypes(filter, pageInfo);
		MV mv = new MV(listURL);
		mv.addObject(WebConstant.COMMAND, map);
		return mv.fwd();
	}

	@RequestMapping("/toAdd")
	public ModelAndView toAdd(DicTypeEntity dicType) {
		MV mv = new MV(formURL);
		mv.addObject(WebConstant.COMMAND, dicType);
		return mv.fwd();
	}

	@RequestMapping("/toEdit")
	public ModelAndView toEdit(DicTypeEntity dicType) {
		dicType = service.findDicType(dicType);
		MV mv = new MV(formURL);
		mv.addObject(WebConstant.COMMAND, dicType);
		return mv.fwd();
	}

	@RequestMapping("/save")
	public ModelAndView save(DicTypeEntity dicType) {
		MV mv = new MV(listURL);
		Integer dicTypeId = dicType.getDicTypeId();
		DicTypeEntity newDicType = null;
		mv.addObject("status", 1);
		if (null != dicTypeId && dicTypeId > 0) {
			DicTypeEntity oldDicTypeEntity = service.findDicType(dicType);
			if (null == oldDicTypeEntity) {
				mv.addObject("status", 0);
				mv.addObject("msg", "字典类型不存在");
			} else {
				newDicType = service.editDicType(dicType);
			}
		} else {
			newDicType = service.addDicType(dicType);
		}
		mv.addObject(WebConstant.COMMAND, newDicType);
		DicTypeController.sync();
		return mv.fwd();
	}
}
