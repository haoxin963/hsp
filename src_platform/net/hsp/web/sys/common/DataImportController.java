package net.hsp.web.sys.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import net.hsp.common.Holder;
import net.hsp.common.filesystem.FileSystemUtil;
import net.hsp.service.sys.dataimport.DataImportService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;
import net.hsp.web.util.SpringCtx;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/dataimport")
public class DataImportController {

	private String resultUrl = "redirect:/sys/dataimport/importResult.do";

	private DataImportService getService(String type, String entity) {
		DataImportService dataImportService = null;
		try {
			if (null == type || type.isEmpty()) {
				dataImportService = (DataImportService) SpringCtx.getSpringContext().getBean("BaseDataImportServiceImpl");

				Class<?> entityClass = null;
				try {
					entityClass = Class.forName(entity);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				dataImportService.setEntityClass(entityClass);

			} else {
				dataImportService = (DataImportService) SpringCtx.getSpringContext().getBean(type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataImportService;
	}

	@RequestMapping("/toImport")
	public ModelAndView toImport(String type, String entity) {
		Map<String, String> command = new HashMap<String, String>();
		command.put("type", type);
		command.put("entity", entity);

		return (new MV("/sys/dataimport/import", WebConstant.COMMAND, command)).fwd();
	}

	@RequestMapping("/exportTemplate")
	public ModelAndView exportTemplate(String type, String entity) {
		MV mv = new MV();
		DataImportService dataImportService = getService(type, entity);

		if (null != dataImportService) {
			String template = dataImportService.getTemplate();
			// 直接输出模版
			if (null != template && !template.isEmpty()) {
				return new MV("redirect:" + template);
			}
			// 生成模版
			else {
				Map<String, String> columnMap = dataImportService.getColumnMap();
				String[] headers = new String[columnMap.size()];
				int i = 0;
				for (String key : columnMap.keySet()) {
					headers[i] = key;
					i++;
				}
				mv.addObject("exportHead", headers);
				mv.addObject("exportTitle", "导入模版");
			}
		}

		return mv.fwd();
	}

	@RequestMapping("/doImport")
	public ModelAndView doImport(String type, String entity, @RequestParam(value = "dataFile", required = false)
	MultipartFile file) {
		int importRows = 0;
		String msg = "";
		Holder<String> error = new Holder<String>();
		try {
			DataImportService dataImportService = getService(type, entity);

			importRows = importExcel(dataImportService, file.getInputStream(), error);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (importRows > 0) {
			msg = "导入成功！共导入" + importRows + "条数据。";
		} else {
			msg = error.value;
		}
		try {
			msg = URLEncoder.encode(msg, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MV mv = new MV(this.resultUrl + "?type=" + type + "&entity=" + entity + "&msg=" + msg);
		return mv.fwd();
	}

	@RequestMapping("/doHughImport")
	public ModelAndView doHughImport(String type, String entity, String filePath) {
		int importRows = 0;
		String msg = "";
		Holder<String> error = new Holder<String>();
		String custId = HttpSessionFactory.getCustId(ActionUtil.getCtx().getRequest());
		String rootPath = FileSystemUtil.getRoot(custId);
		File file = new File(rootPath + filePath);

		if (file.isFile()) {
			DataImportService dataImportService = getService(type, entity);
			try {
				FileInputStream in = new FileInputStream(file);
				importRows = importExcel(dataImportService, in, error);
				in.close();
			} catch (FileNotFoundException e) {
				error.value = "上传文件已不存在。";
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				file.delete();
			}
		}
		if (importRows > 0) {
			msg = "导入成功！共导入" + importRows + "条数据。";
		} else {
			msg = error.value;
		}
		try {
			msg = URLEncoder.encode(msg, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MV mv = new MV(this.resultUrl + "?type=" + type + "&entity=" + entity + "&msg=" + msg);
		return mv.fwd();
	}

	private int importExcel(DataImportService dataImportService, InputStream inputStream, Holder<String> error) {
		int importRows = 0;
		if (null != dataImportService) {
			try {
				Workbook wb = Workbook.getWorkbook(inputStream);
				Sheet st = wb.getSheet(0);

				int rows = st.getRows();
				int columns = st.getColumns();
				if (rows > 1 && columns > 1) {
					Cell[] headerCells = st.getRow(0);
					Map<String, String> columnMap = dataImportService.getColumnMap();
					Map<String, Integer> indexMap = new HashMap<String, Integer>();
					for (int i = 0; i < headerCells.length; i++) {
						String label = headerCells[i].getContents();
						if (columnMap.containsKey(label)) {
							indexMap.put(columnMap.get(label), i);
						}
					}

					List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
					for (int i = 1; i < rows; i++) {
						Cell[] rowCells = st.getRow(i);
						Map<String, String> data = new HashMap<String, String>();
						for (Entry<String, Integer> entry : indexMap.entrySet()) {
							String key = entry.getKey();
							int index = entry.getValue();
							String content = "";
							try {
								content = rowCells[index].getContents().trim();
							} catch(java.lang.ArrayIndexOutOfBoundsException e) {
							}
							data.put(key, content);
						}
						dataList.add(data);
					}

					dataList = dataImportService.getDateList(dataList);
					if (dataImportService.batchImport(dataList, error)) {
						importRows = dataList.size();
					}
				}

			} catch (BiffException e) {
				error.value = "上传文件格式不正确，请上传xls文件。";
				e.printStackTrace();
			} catch (IOException e) {
				error.value = "无法读取上传文件。";
				e.printStackTrace();
			}
		} else {
			error.value = "导入业务未实现。";
		}
		return importRows;
	}

	@RequestMapping("/importResult")
	public ModelAndView importResult(String type, String entity, String msg) {
		MV mv = new MV("/sys/dataimport/importResult");
		Map<String, Object> command = new HashMap<String, Object>();
		command.put("type", type);
		command.put("entity", entity);
		mv.addObject(WebConstant.COMMAND, command);
		System.out.println(msg);
		mv.addObject("msg", msg);
		return mv.fwd();
	}
}
