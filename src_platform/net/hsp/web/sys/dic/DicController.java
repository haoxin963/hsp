package net.hsp.web.sys.dic;

import java.util.HashMap;
import java.util.Map;

import net.hsp.entity.sys.dic.DicTypeEntity;
import net.hsp.entity.sys.dic.DictionaryEntity;
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
@RequestMapping("/sys/dic/dictionary")
public class DicController {

	@Autowired
	private DictionaryService service;

	private String listURL = "/sys/dic/dicList";
	private String formURL = "/sys/dic/dicForm";

	@RequestMapping("/doList")
	public ModelAndView doList(DicTypeEntity dicType, FilterMap filter, PageInfo pageInfo) {

		Map<String, Object> map = service.listDics(dicType.getDicTypeId(), filter, pageInfo);
		map.put("dicType", service.findDicType(dicType));

		MV mv = new MV(listURL);
		mv.addObject(WebConstant.COMMAND, map);
		return mv.fwd();
	}

	@RequestMapping("/toAdd")
	public ModelAndView toAdd(DictionaryEntity dic) {
		DicTypeEntity dicType = new DicTypeEntity();
		dicType.setDicTypeId(dic.getDicTypeId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dic", dic);
		map.put("dicType", service.findDicType(dicType));
		MV mv = new MV(formURL);
		mv.addObject(WebConstant.COMMAND, map);
		return mv.fwd();
	}

	@RequestMapping("/toEdit")
	public ModelAndView toEdit(DictionaryEntity dic) {

		Map<String, Object> map = new HashMap<String, Object>();
		dic = service.findDic(dic);
		DicTypeEntity dicType = new DicTypeEntity();
		dicType.setDicTypeId(dic.getDicTypeId());

		map.put("dic", dic);
		map.put("dicType", service.findDicType(dicType));
		MV mv = new MV(formURL);
		mv.addObject(WebConstant.COMMAND, map);
		return mv.fwd();
	}

	@RequestMapping("/save")
	public ModelAndView save(DictionaryEntity dic) {
		DictionaryEntity newDic = null;
		if (null != dic) {
			if (null == dic.getDictId() || dic.getDictId() < 1) {
				newDic = service.addDic(dic);
			} else {
				newDic = service.editDic(dic);
			}
		}
		MV mv = new MV(listURL);
		mv.addObject(WebConstant.COMMAND, newDic);
		DicTypeController.sync();
		return mv.fwd();
	}

	@RequestMapping("/doUp")
	public ModelAndView doUp(DictionaryEntity dic) {
		service.upDic(dic, true);
		MV mv = new MV(listURL);
		mv.addObject(WebConstant.COMMAND, dic);
		DicTypeController.sync();
		return mv.fwd();
	}

	@RequestMapping("/doDown")
	public ModelAndView doDown(DictionaryEntity dic) {
		service.upDic(dic, false);
		MV mv = new MV(listURL);
		mv.addObject(WebConstant.COMMAND, dic);
		DicTypeController.sync();
		return mv.fwd();
	}

	@RequestMapping("/disabled")
	public ModelAndView disabled(String dictIds) {
		service.disabledDic(dictIds, true);
		MV mv = new MV(listURL);
		mv.addObject(WebConstant.COMMAND, dictIds);
		DicTypeController.sync();
		return mv.fwd();
	}

	@RequestMapping("/enabled")
	public ModelAndView enabled(String dictIds) {
		service.disabledDic(dictIds, false);
		MV mv = new MV(listURL);
		mv.addObject(WebConstant.COMMAND, dictIds);
		DicTypeController.sync();
		return mv.fwd();
	}

	@RequestMapping("/doDel")
	public ModelAndView doDel(String dictIds) {
		service.deleteDic(dictIds);
		MV mv = new MV(listURL);
		mv.addObject(WebConstant.COMMAND, dictIds);
		DicTypeController.sync();
		return mv.fwd();
	}
}
