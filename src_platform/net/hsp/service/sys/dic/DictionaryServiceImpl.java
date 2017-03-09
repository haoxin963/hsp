package net.hsp.service.sys.dic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.dic.DicExtFieldEntity;
import net.hsp.entity.sys.dic.DicTypeEntity;
import net.hsp.entity.sys.dic.DictionaryEntity;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

public class DictionaryServiceImpl extends BaseServiceImpl implements DictionaryService {
	public List<Map<String, Object>> findAllDics() throws Exception {
		String sql = "SELECT DICTNAME,DICTYPENAME,A.TYPECODE as TYPECODE,DICTID,EXT0,EXT1,EXT2,EXT3 FROM pubmodule_dictionary_tbl A,pubmodule_dictype_tbl B WHERE A.TYPECODE = B.TYPECODE AND B.DELTAG=0 and A.DELTAG=0 and A.state='1' and B.state='1' ";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = getDAO().queryForList(sql);
		} catch (Exception e) {
			sql = "SELECT DICTNAME,DICTYPENAME,A.TYPECODE as TYPECODE,DICTID FROM pubmodule_dictionary_tbl A,pubmodule_dictype_tbl B WHERE A.TYPECODE = B.TYPECODE AND B.DELTAG=0 and A.DELTAG=0 and A.state='1' and B.state='1' ";
			list = getDAO().queryForList(sql);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> listDicTypes(FilterMap filterMap, PageInfo p) throws ServiceException {
		String sql = " SELECT * FROM pubmodule_dictype_tbl WHERE 1=1";
		Object[] args = null;
		return this.findPageInfo(sql, args, p);
	}

	@Override
	public DicTypeEntity findDicType(DicTypeEntity dicType) throws ServiceException {
		dicType = (DicTypeEntity) this.findById(dicType);
		dicType.setExtFields(listExtFieldsByDicType(dicType.getDicTypeId()));
		return dicType;
	}

	@Override
	public DicTypeEntity addDicType(DicTypeEntity dicType) throws ServiceException {
		DicTypeEntity newDicType = (DicTypeEntity) this.save(dicType);
		int dicTypeId = newDicType.getDicTypeId();
		int extFieldNums = 0;
		for (Iterator<DicExtFieldEntity> iterator = dicType.getExtFields().iterator(); iterator.hasNext();) {
			DicExtFieldEntity extField = iterator.next();
			if (extFieldNums > 20) {
				break;
			}
			if (null != extField.getFieldName()) {
				extField.setDicTypeId(dicTypeId);
				extField.setExtField("ext" + extFieldNums);
				this.getDAO().save2(extField);
				extFieldNums++;
			}
		}
		return this.findDicType(newDicType);
	}

	@Override
	public DicTypeEntity editDicType(DicTypeEntity dicType) throws ServiceException {
		this.update(dicType);

		// 新扩展字段
		List<DicExtFieldEntity> newDicExtFields = new ArrayList<DicExtFieldEntity>();
		// 遍历，先处理更新已有的扩展字段
		for (Iterator<DicExtFieldEntity> iterator = dicType.getExtFields().iterator(); iterator.hasNext();) {
			DicExtFieldEntity extField = iterator.next();
			if (null != extField.getFieldName()) {
				if (null != extField.getDicTypeId() && null != extField.getExtField()) {
					this.update(extField);
				} else {
					newDicExtFields.add(extField);
				}
			}

		}
		int dicTypeId = dicType.getDicTypeId();
		//
		int extFieldNums = countExtFieldsByDicType(dicTypeId);
		//
		for (Iterator<DicExtFieldEntity> iterator = newDicExtFields.iterator(); iterator.hasNext();) {
			if (extFieldNums > 20) {
				break;
			}
			DicExtFieldEntity extField = iterator.next();
			extField.setDicTypeId(dicTypeId);
			extField.setExtField("ext" + extFieldNums);
			this.getDAO().save2(extField);
			extFieldNums++;
		}

		return this.findDicType(dicType);
	}

	@SuppressWarnings("unchecked")
	private List<DicExtFieldEntity> listExtFieldsByDicType(int dicTypeId) {
		List<DicExtFieldEntity> extFields = new ArrayList<DicExtFieldEntity>();
		String sql = "SELECT * FROM pubmodule_dicextfield_tbl WHERE dicTypeId = ?";
		extFields = this.find(sql, new Object[] { dicTypeId }, DicExtFieldEntity.class);
		return extFields;
	}

	@SuppressWarnings("unused")
	private int countExtFieldsByDicType(int dicTypeId) {
		String sql = "SELECT COUNT(*) FROM pubmodule_dicextfield_tbl WHERE dicTypeId = ?";
		return this.getDAO().queryForObject(sql, new Object[] { dicTypeId }, Integer.class);
	}

	/* 以上为字典类型相关函数--------------------------------------------------------- */

	/* 以下为字典相关函数------------------------------------------------------------ */
	@Override
	public DictionaryEntity findDic(DictionaryEntity dic) throws ServiceException {
		return (DictionaryEntity) this.findById(dic);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> listDics(Integer dicTypeId, FilterMap filterMap, PageInfo p) throws ServiceException {
		String sql = " SELECT * FROM pubmodule_dictionary_tbl WHERE dicTypeId=? ORDER BY sortNo";
		Object[] args = new Object[] { dicTypeId };
		return this.findPageInfo(sql, args, p);
	}

	@Override
	public DictionaryEntity addDic(DictionaryEntity dic) throws ServiceException {
		Integer maxSort = getMaxDicSort(dic.getDicTypeId());
		int sortNo = maxSort == null ? 1 : maxSort + 1;
		dic.setSortNo(sortNo);
		return (DictionaryEntity) this.save(dic);
	}

	@Override
	public DictionaryEntity editDic(DictionaryEntity dic) throws ServiceException {
		DictionaryEntity oldDic = findDic(dic);
		dic.setSortNo(oldDic.getSortNo());
		dic.setDelTag(oldDic.getDelTag());
		dic.setState(oldDic.getState());
		this.update(dic);
		return findDic(dic);
	}

	private Integer getMaxDicSort(int dicTypeId) {
		String sql = " SELECT MAX(sortNo) FROM pubmodule_dictionary_tbl WHERE dicTypeId=?";
		return this.getDAO().queryForObject(sql, new Object[] { dicTypeId }, Integer.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void upDic(DictionaryEntity dic, boolean up) throws ServiceException {
		dic = findDic(dic);
		int dicTypeId = dic.getDicTypeId();
		int sortNo = dic.getSortNo();

		String sql = " SELECT * FROM pubmodule_dictionary_tbl WHERE dicTypeId=? AND sortNo" + (up ? "<" : ">") + sortNo + " ORDER BY sortNo " + (up ? "DESC" : "") + " LIMIT 0,1";
		RowMapper<DictionaryEntity> rowMapper = new BeanPropertyRowMapper(DictionaryEntity.class);
		DictionaryEntity anotherDic = this.getDAO().queryForObject(sql, new Object[] { dicTypeId }, rowMapper);
		// System.out.println(anotherDic.getSortNo());
		if (null != anotherDic.getDictId()) {
			int anotherSortNo = anotherDic.getSortNo();
			dic.setSortNo(anotherSortNo);
			update(dic);
			anotherDic.setSortNo(sortNo);
			update(anotherDic);
		}
	}

	@Override
	public void deleteDic(String dictIds) throws ServiceException {
		// System.out.println(555555555);
		String sql = "UPDATE pubmodule_dictionary_tbl SET delTag=1 WHERE dictId =?";
		batchUpdateDic(sql, dictIds);
	}

	@Override
	public void disabledDic(String dictIds, boolean disabled) throws ServiceException {
		String state = disabled ? "0" : "1";
		String sql = "UPDATE pubmodule_dictionary_tbl SET state=" + state + " WHERE dictId =?";
		batchUpdateDic(sql, dictIds);
	}

	private void batchUpdateDic(String sql, String dictIds) {
		if (dictIds != null && !dictIds.isEmpty()) {

			List<Object[]> param = new ArrayList<Object[]>();

			for (String dictId : dictIds.split(",")) {
				String[] o = new String[] { dictId };
				param.add(o);
			}

			this.getDAO().batchUpdate(sql, param);
		}
	}
}
