package net.hsp.service.sys.manage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.common.filesystem.FileSystemUtil;
import net.hsp.dao.ProxoolExtDataSource;
import net.hsp.entity.sys.manage.Station;
import net.hsp.entity.sys.rbac.Function;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.sys.rbac.UserController;
import net.hsp.web.util.PageInfo;
import net.hsp.web.util.SpringCtx;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 站点业务类
 * 
 * @author aa
 */
@Service
@ServiceLogUtil(name = "站点业务类")
@Lazy(true)
public class StationServiceImpl extends BaseServiceImpl implements StationService {
	
	private static final String FUNCTION_HASCHILD = "1";
	
	private final Logger log = Logger.getLogger(StationServiceImpl.class);
	
	private static Map<String, Map<String, Object>> stations = new Hashtable();

	public static Map<String, Map<String, Object>> getStations() {
		
		return stations;
	}
	
	@Override
	public int update(Station obj) throws ServiceException {
		//刷新缓存
		String custId = obj.getDomainAddress();
		Map<String, Object> map = stations.get(custId);
		map.put("statName", obj.getStatName());
		return super.update(obj);
	}



	@Override
	public int[] batchDeleteTag(Class cls, String[] pks) throws ServiceException { 
		disable(pks);
		return super.batchDeleteTag(cls, pks);
	}


	@Override
	public int deleteTag(Object entity) throws ServiceException {  
		disable(new String[]{((Station) entity).getStatId()});
		return super.deleteTag(entity);
	}

	@Override
	@MethodLogUtil(type = "", value = "初始化新的站点")
	public int create(Station entity) throws ServiceException {
		Set<String> set = new HashSet<String>();
		set.add("pubmodule_actionlog_tbl");
		set.add("pubmodule_area_tbl");
		set.add("pubmodule_attachments_tbl");
		set.add("pubmodule_configitem_tbl");
		set.add("pubmodule_configvalue_tbl");
		set.add("pubmodule_department_tbl");
		set.add("pubmodule_dictionary_tbl");
		set.add("pubmodule_dictype_tbl");
		set.add("pubmodule_employeepost_tbl");
		set.add("pubmodule_employee_tbl");
		set.add("pubmodule_favorite_tbl");
		set.add("pubmodule_function_tbl");
		set.add("pubmodule_message_status_tbl");
		set.add("pubmodule_message_tbl");
		set.add("pubmodule_notice_tbl");
		set.add("pubmodule_noticecategory_tbl");
		set.add("pubmodule_noticecomment_tbl");
		set.add("pubmodule_noticeuser_tbl");
		set.add("pubmodule_position_tbl");
		set.add("pubmodule_post_tbl");
		set.add("pubmodule_rolefunction_tbl");
		set.add("pubmodule_role_tbl");
		set.add("pubmodule_userlog_tbl");
		set.add("pubmodule_userrole_tbl");
		set.add("pubmodule_user_tbl");
		int i =  config("create", entity, set);
		if(i==1){
			getDAO(PlatFormConstant.BASESTATIONID).save(entity);
		}
		return i;
	}

	@MethodLogUtil(type = "", value = "启用站点")
	public boolean enabled(String[] domains) {
		for (int i = 0; i < domains.length; i++) {
			Station obj = this.findById(Station.class, domains[i]); 
			config("enabled", obj, null);
			this.getDAO(PlatFormConstant.BASESTATIONID).update("update uisp_station_tbl set status = 1,delTag=0 where statId = ?", domains[i]);
		}
		return true;
	}

	@MethodLogUtil(type = "", value = "禁用站点")
	public boolean disable(String[] domains) {
		for (int i = 0; i < domains.length; i++) {
			Station obj = this.findById(Station.class, domains[i]); 
			String domainAddress = obj.getDomainAddress();
			// 文件系统配置文件修改
			FileSystemUtil.getInstance().removeProperty(domainAddress + ".filesystem.root");

			// 数据源配置文件修改
			String filePath = PlatFormConstant.CONFIGPATH + "/dsContext.xml";
			try {
				XMLFileUtil.removeContextXMLBean(filePath, "ds_", domainAddress);
			} catch (Exception e) {
				throw new ServiceException(e);
			}

			// 数据库重命名

			// 初始 springBean@datasource
			try {

				ApplicationContext context = SpringCtx.getSpringContext();
				DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
				beanFactory.removeBeanDefinition("ds_" + domainAddress);
			} catch (Exception e) {
				// TODO: handle exception
			}

			// 站点集合重置
			this.stations.remove(domainAddress);

			// 状态
			this.getDAO(PlatFormConstant.BASESTATIONID).update("update uisp_station_tbl set status = 2,delTag=1 where domainAddress = ?", domainAddress);
		}

		return true;
	}

	private int config(String action,Station obj, Set<String> tables) {
		String domain = obj.getDomainAddress();
		if (this.stations.get(domain) == null) {
			// 文件系统配置文件修改
			String fileSystemPath = "/app/hsp";

			Map<String, ?> station = this.getDAO(PlatFormConstant.BASESTATIONID).queryForMap("select * from uisp_systemplate_tbl limit 0,1");
			try {
				fileSystemPath = this.getDAO(PlatFormConstant.BASESTATIONID).queryForObject("SELECT fileSystemPath FROM uisp_station_tbl WHERE domainAddress = ? and fileSystemPath is not null limit 0,1", String.class, domain);
			} catch (Exception e) {
				
			}
			if(fileSystemPath==null){
				fileSystemPath = String.valueOf(station.get("fileSystemPath"));
			}
			
			String ip = String.valueOf(station.get("dbAddress"));
			String dbPrefix = String.valueOf(station.get("dbPrefix"));
			String initSize = String.valueOf(station.get("initPoolSize"));
			String maxSize = String.valueOf(station.get("maxPoolSize"));
			String minSize = String.valueOf(station.get("minPoolSize"));
			String dbUsername = String.valueOf(station.get("dbUsername"));
			String dbPassword = String.valueOf(station.get("dbPassword"));
			FileSystemUtil.getInstance().addProperty(domain + ".filesystem.root", fileSystemPath);
			log.info("reg filesystem");
			// 数据源配置文件修改
			String filePath = PlatFormConstant.CONFIGPATH + "/dsContext.xml";
			try {
				XMLFileUtil.createContextXMLBean(filePath, "ds", domain, true, "1", ip, dbPrefix, initSize, maxSize, minSize);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
			// 创建数据库
			String dbName = dbPrefix+ "_" + domain;
			this.getDAO(PlatFormConstant.BASESTATIONID).execute("CREATE DATABASE  IF NOT EXISTS  " +dbName );

			// 初始 springBean@datasource
			ApplicationContext context = SpringCtx.getSpringContext();
			DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
			try {
				
				AbstractBeanDefinition defin = new RootBeanDefinition(ProxoolExtDataSource.class);
				beanFactory.registerBeanDefinition("ds_" + domain, defin);
				ProxoolExtDataSource ds = (ProxoolExtDataSource) context.getBean("ds_" + domain);
				ds.setAlias("ds_" + domain);
				ds.setDriverUrl(ip + "/" + dbPrefix + "_" + domain);
				ds.setDriver("com.mysql.jdbc.Driver");
				ds.setUser(dbUsername);
				ds.setPassword(dbPassword);
				ds.setMaximumConnectionCount(Integer.parseInt(maxSize));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 初始化数据 仅第一次初始化数据
			if ("create".equals(action) && tables != null) {
				for (String tab : tables) {
					//同步表结构 含主键、默认值、非空约束等
//					this.getDAO(domain).execute("create table if not exists "+ tab + " like app_uisp3_datatemplate." + tab);
					try {
						//同步数据
						this.getDAO(domain).execute("create table if not exists "+ tab + " select * from app_uisp3_datatemplate." + tab);
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
				log.info("reg:"+tables);
			}

			// 站点集合重置
			Map m = new HashMap();
			m.put("domainAddress", domain);
			m.put("statName", obj.getStatName());
			this.stations.put(domain, m);
			return 1;
		} else {
			return 2;
		}
	}

	@MethodLogUtil(type = "", value = "站点查询")
	public Map<String, Object> query(Map param, PageInfo p) throws ServiceException {
		StringBuilder sb = new StringBuilder("select domainAddress,statName,language,statId,status,createdate  from uisp_station_tbl where 1=1");
		if (param.get("status") == null) {
			param.put("status", "1");
		}
		Map sqlBuild = SQLBuild.buildLike(param);
		sb.append(sqlBuild.get("sql") == null ? "" : sqlBuild.get("sql"));
		if (p == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = null;
			Object args = sqlBuild.get("args");
			if (args != null) {
				list = this.find(sb.toString(), (Object[]) args);
			} else {
				list = this.getDAO().queryForList(sb.toString());
			} 
			map.put("rows", list);
			map.put("total", list.size());
			return map;
		} else {
			sb.append(" order by createdate desc");
			return this.findPageInfo(sb.toString(), (Object[]) sqlBuild.get("args"), p);
		}
	}

	@MethodLogUtil(type = "", value = "站点查询")
	public List query() throws ServiceException {
		String sql = "select domainAddress,statName,msgServerAddr,configFile  from uisp_station_tbl where delTag = 0 and status = 1";
		return getDAO(PlatFormConstant.BASESTATIONID).queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> buildTree(Map map, PageInfo p) {
		String sql = "select * from uisp_station_tbl where delTag = ?  and status = 1 ";
		return this.getDAO(PlatFormConstant.BASESTATIONID).queryForList(sql, new Object[] { "0" });
	}
	
/**
 * -----------------------------------站点菜单功能---------------------------------------------------------------------------------------------------------------------------
 */	
	@MethodLogUtil(type = "", value = "多条件查询站点功能")
	public Map queryFunction(Map param, PageInfo p) {
		String custDomain = param.get("custDomain").toString();
		param.remove("custDomain");
		
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuilder sb = new StringBuilder("select functionId,buttonId,functionName,child,flag,linkAddress,parent_id,pictureAddr,sortNo,tag,type from pubmodule_function_tbl where 1=1");
		Map sqlBuild = SQLBuild.buildEquals(param);
		sb.append(sqlBuild.get("sql") == null ? "" : sqlBuild.get("sql"));
		sb.append(" order by parent_id,sortNo ");
		List<Map<String, Object>> list = null;
		Object args = sqlBuild.get("args");
		if (args != null) {
			 list = this.getDAO(custDomain).queryForList(sb.toString(), (Object[]) args);
		} else {
			 list = this.getDAO(custDomain).queryForList(sb.toString());
		}
		map.put("rows", list);
		return map;
	}
	/**
	 * 递归删除功能及其子功能
	 * @param funIds
	 * @param custDomain
	 */
	public void deleteFunctionAndChildren(String funIds, String custDomain){
		this.batchDeleteFunction(funIds.split(","), custDomain);
		
		funIds = "'" + funIds.replaceAll(",", "','") +"'";
		StringBuffer sb = new StringBuffer();
		sb.append(" select f.* from pubmodule_function_tbl f where f.parent_id in (")
			.append(funIds+")");
		List<Map<String, Object>> funcList = this.getDAO(custDomain).queryForList(sb.toString());
		String ids = "";
		for(Map<String,Object> map : funcList){
			String functionId = map.get("functionId").toString();
			ids += functionId + ",";
		}
		if(!"".equals(ids)){
			ids = ids.substring(0, ids.length()-1);
			deleteFunctionAndChildren(ids,custDomain);
		}
	}
	
	public List<Map<String, Object>> getAllChildFunctionById(String funId, String custDomain) {
		String sql = "select f.* from pubmodule_function_tbl f where f.parent_id = ? ";
		List<Map<String, Object>> funcList = this.getDAO(custDomain).queryForList(sql, new Object[] { funId });
		return funcList;
	}
	private void updateHasChild(String hasChild, String funId, String custDomain) {
		String sql = "update pubmodule_function_tbl set child = '" + hasChild + "' where functionId = '" + funId + "'";
		this.getDAO(custDomain).execute(sql);
	}
	
	@Override
	public Function addFunction(Function function,String custDomain) {
		List<Map<String, Object>> funcList = getAllChildFunctionById(function.getParentId() + "",custDomain);
		int sortNo = funcList.size() > 0 ? (funcList.size() + 1) : 1;
		UUID uuid = UUID.randomUUID();
		String funcId = uuid.toString();
		funcId = funcId.replace("-", "").toLowerCase();
		function.setFunctionId(funcId);
		function.setSortNo(sortNo);		
		String addSql = "insert into pubmodule_function_tbl (functionId, buttonId, child, flag, functionName, linkAddress, parent_id, pictureAddr, sortNo, tag,type) " + "values ('" + function.getFunctionId() + "', '" + function.getButtonId() + "', '0', '', '" + function.getFunctionName() + "', '" + function.getLinkAddress() + "', '" + function.getParentId() + "', '" + function.getPictureAddr() + "', "
				+ function.getSortNo() + ", '" + function.getTag() + "','"+function.getType()+"') ";
		this.getDAO(custDomain).execute(addSql);
		if (funcList.size() == 0) {
			updateHasChild(FUNCTION_HASCHILD, function.getParentId(),custDomain);
		}
		return function;
	}
	
	public Function insertFunction(Function function, String custDomain){
		UUID uuid = UUID.randomUUID();
		String funcId = uuid.toString();
		funcId = funcId.replace("-", "").toLowerCase();
		function.setFunctionId(funcId);
		String addSql = "insert into pubmodule_function_tbl (functionId, buttonId, child, flag, functionName, linkAddress, parent_id, pictureAddr, sortNo, tag,type) " + "values ('" + function.getFunctionId() + "', '" + function.getButtonId() + "', '0', '', '" + function.getFunctionName() + "', '" + function.getLinkAddress() + "', '" + function.getParentId() + "', '" + function.getPictureAddr() + "', "
			+ function.getSortNo() + ", '" + function.getTag() + "','"+function.getType()+"') ";
		this.getDAO(custDomain).execute(addSql);
		//插入功能后，将大于该功能序号后的功能的序号增1
		StringBuffer sb = new StringBuffer();
		sb.append(" update pubmodule_function_tbl set sortNo =  sortNo+1 where sortNo>= "+function.getSortNo()+" and parent_id = '"+function.getParentId()+"'");
		this.getDAO(custDomain).execute(sb.toString());
		return function;
	}
	
	@Override
	public int modifyFunction(Function function, String custDomain) {
		return this.getDAO(custDomain).update2(function);
	}
	
	public int deleteFunction(Function obj, String custDomain){
		return this.getDAO(custDomain).delete(obj);
	}
	
	public int[] batchDeleteFunction(String[] funIds, String custDomain){
		return this.getDAO(custDomain).batchDelete(Function.class, funIds);
	}
	
	public Function findFunById(Function obj, String custDomain){
		return this.getDAO(custDomain).findById(obj);
	}
	
	@Override
	public int checkFuncHasChild(String[] ids, String custDomain) {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<ids.length;i++){
			if(i==ids.length-1){
				sb.append("'"+ids[i]+"'");
			}else{
				sb.append("'"+ids[i]+"',");
			}
		}
		String sql = "select f.* from pubmodule_function_tbl f where f.parent_id in ("+sb.toString()+") ";
		List list = this.getDAO(custDomain).queryForList(sql);
		if(list.size()>0){
			return 1;
		}else{
			return 0;
		}
	}
	
	@Override
	public List<Map<String, Object>> buildMenuTree(String custDomain) {
		String sql = "select * from pubmodule_function_tbl order by parent_id,sortNo ";
		return this.getDAO(custDomain).queryForList(sql);
	}
	

	@Override
	public int changeFunctionLevel(String id, String type, String parentId, String sortNo, String[] sortList,String custDomain) {
		StringBuffer sb = new StringBuffer();
		if ("1".equals(type)) {
			// 更新节点层级
			sb.append("update pubmodule_function_tbl set parent_id = '" + parentId + "'");
			if (!"".equals(sortNo)) {
				sb.append(",sortNo = " + sortNo);
			}
			sb.append(" where functionId = '" + id + "'");
			this.getDAO(custDomain).execute(sb.toString());
			sb.delete(0, sb.length());
			// 更新原来父节点及兄弟节点的结构
		}
		// 调整关联部门顺序
		for (int i = 0; i < sortList.length; i++) {
			if (sortList[i].indexOf(",") < 0) {
				continue;
			}
			String[] sortArr = sortList[i].split(",");
			String sortid = sortArr[0];
			String sortno = sortArr[1];
			sb.append("update pubmodule_function_tbl set sortNo = " + sortno + " where functionId = '" + sortid + "'");
			this.getDAO(custDomain).execute(sb.toString());
			sb.delete(0, sb.length());
		}
		return 1;
	}
	 
}
