package net.hsp.service.sys.dic;

import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.dic.DicTypeEntity;
import net.hsp.entity.sys.dic.DictionaryEntity;
import net.hsp.service.ServiceException;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

public interface DictionaryService {

	public Map<String, Object> listDicTypes(FilterMap filterMap, PageInfo p) throws ServiceException;

	public DicTypeEntity findDicType(DicTypeEntity dicType) throws ServiceException;

	public DicTypeEntity addDicType(DicTypeEntity dicType) throws ServiceException;

	public DicTypeEntity editDicType(DicTypeEntity dicType) throws ServiceException;

	public Map<String, Object> listDics(Integer dicTypeId, FilterMap filterMap, PageInfo p) throws ServiceException;

	public DictionaryEntity findDic(DictionaryEntity dic) throws ServiceException;

	public DictionaryEntity addDic(DictionaryEntity dic) throws ServiceException;

	public DictionaryEntity editDic(DictionaryEntity dic) throws ServiceException;

	public void deleteDic(String dictIds) throws ServiceException;

	public void upDic(DictionaryEntity dic, boolean up) throws ServiceException;

	public void disabledDic(String dictIds, boolean disabled) throws ServiceException;

	public abstract List<Map<String, Object>> findAllDics() throws Exception;

}