package net.hsp.web.sys.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.hsp.common.CfgUtil;
import net.hsp.entity.sys.config.ConfigItem;
import net.hsp.entity.sys.config.ConfigItemGroup;
import net.hsp.entity.sys.config.ConfigVaule;
import net.hsp.service.sys.config.ConfigService;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/config")
public class ConfigController {
	@Autowired
	ConfigService configService;

	private String getCustId() {
		String custId = ActionUtil.getCtx().getRequest().getParameter("custId");
		if (StringUtils.isBlank(custId)) {
			custId = HttpSessionFactory.getCustId(ActionUtil.getCtx().getRequest());
		}
		return custId;
	}

	@RequestMapping("/listConfig")
	public ModelAndView listConfig() {
		LinkedHashMap<String, ConfigItemGroup> itemGroups = configService.getItemGroups();
		MV mv = new MV("/sys/config/configList");
		mv.addObject("itemGroups", itemGroups);
		mv.addObject("devTag", ActionUtil.getCtx().getRequest().getParameter("devTag"));
		return mv.fwd();
	}

	@RequestMapping("/toEditConfig")
	public ModelAndView toEditConfig(String itemGroup, String custId) {
		if (null == custId || custId.isEmpty()) {
			custId = getCustId();
		}
		LinkedHashMap<String, ConfigVaule> configMap = CfgUtil.getConfigs(custId);
		List<ConfigVaule> configs = new ArrayList<ConfigVaule>();
		if (null != itemGroup) {
			for (String key : configMap.keySet()) {
				ConfigVaule configValue = configMap.get(key);
				if (itemGroup.equals(configValue.getConfigItem().getItemGroup())) {
					configs.add(configValue);
				}
			}
		}
		MV mv = new MV("/sys/config/configForm");
		mv.addObject("configs", configs);
		mv.addObject("itemGroup", itemGroup);
		mv.addObject("devTag", ActionUtil.getCtx().getRequest().getParameter("devTag"));

		return mv.fwd();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/editConfig")
	public ModelAndView editConfig(HttpServletRequest req) {
		String itemGroup = req.getParameter("itemGroup");
		if (null != itemGroup) {
			String custId = getCustId();
			List<ConfigItem> configItems = configService.listConfigItem(custId);
			if (null != configItems) {
				List<ConfigVaule> configValues = new ArrayList<ConfigVaule>();
				for (int i = 0; i < configItems.size(); i++) {
					ConfigItem configItem = configItems.get(i);
					String itemKey = configItem.getItemKey();
					if (itemGroup.equals(configItem.getItemGroup())) {
						ConfigVaule configVaule = null;
						String itemType = configItem.getItemType();
						if ("multiple".equals(itemType)) {
							configVaule = new ConfigVaule(itemKey, req.getParameterValues(itemKey));
						} else {
							configVaule = new ConfigVaule(itemKey, req.getParameter(itemKey));
						}
						configValues.add(configVaule);
					}
				}
				if (!configValues.isEmpty()) {
					configService.batchUpdateConifg(custId, configValues);
					CfgUtil.clear(custId);
				}
			}
		}
		MV mv = new MV("/sys/config/configForm");

		return mv.fwd();
	}

	@RequestMapping("/listWholeConfig")
	public ModelAndView listWholeConfig(String custId) {
		if (null == custId || custId.isEmpty()) {
			custId = getCustId();
		}
		MV mv = new MV("/sys/config/wholeConfigList");
		mv.addObject("devTag", "1");
		return mv.fwd();
	}

	@RequestMapping("/toAddConfigItem")
	public ModelAndView toAddConfigItem(String itemGroup) {
		MV mv = new MV("/sys/config/configItemForm");
		ConfigItemGroup configItemGroup = configService.getItemGroup(itemGroup);
		mv.addObject("itemGroup", configItemGroup);
		return mv.fwd();
	}

	@RequestMapping("/addConfigItem")
	public ModelAndView addConfigItem(ConfigItem configItem) {
		MV mv = new MV("/sys/config/configForm");
		String key = configItem.getItemKey();
		String custId = ActionUtil.getCtx().getRequest().getParameter("custId");
		if (StringUtils.isBlank(custId)) {
			custId = getCustId();
		}
		LinkedHashMap<String, ConfigVaule> map = CfgUtil.getConfigs(custId);
		if (map.containsKey(key)) {
			mv.addObject("status", "0");
			mv.addObject("msg", "参数标志已存在");
		} else {
			configItem.setItemValue(configItem.getItemValues());
			configService.saveConfigItem(custId, configItem);
		}
		return mv.fwd();
	}

	@RequestMapping("/reloadConfigs")
	public ModelAndView reloadConfigs(String custId) {
		if (null == custId) {
			custId = getCustId();
		}
		if (null != custId) {
			CfgUtil.clear(custId);
		}
		MV mv = new MV();
		return mv.fwd();
	}
}
