package net.hsp.web.util;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.hsp.web.util.xls.ExcelConveter;
import net.hsp.web.util.xls.ExcelUtils;
import net.hsp.web.util.xls.GridReportBase;
import net.hsp.web.util.xls.TableData;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ViewExcel extends AbstractExcelView
{
  public void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String[] hearders = (String[])null;
    String[] exportField = (String[])null;
    String fileName = null;
    if (StringUtils.isNotBlank(request.getParameter("exportHead")))
      hearders = request.getParameter("exportHead").split(",");
    else {
      hearders = (String[])model.get("exportHead");
    }
    if (StringUtils.isNotBlank(request.getParameter("exportField")))
      exportField = request.getParameter("exportField").split(",");
    else {
      exportField = (String[])model.get("exportField");
    }
    if (hearders == null) {
      return;
    }

    if (StringUtils.isNotBlank(request.getParameter("exportTitle")))
      fileName = request.getParameter("exportTitle");
    else {
      fileName = (String)model.get("exportTitle");
    }
    if (fileName == null) {
      return;
    }
    String sheetName = fileName;
    String agent = request.getHeader("User-Agent");
    boolean isMSIE = (agent != null) && (agent.indexOf("MSIE") != -1);
    if (isMSIE)
      fileName = URLEncoder.encode(fileName, "UTF-8");
    else {
      fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
    }

    String createName = "admin";
    Map m = (Map)model.get("command");
    List dataList = null;
    if (m != null) {
      dataList = (List)m.get("rows");
    }

    //　兼容遗留代码　
    if (dataList== null) {
        dataList = (List)model.get("rows");
     }
    ExcelConveter c = (ExcelConveter)model.get("excelConveter");

    String[] parents = (String[])model.get("exportParents");
    String[][] children = (String[][])model.get("exportChildren");
    boolean showTitle = model.get("showTitle") == null ? false : ((Boolean)model.get("showTitle")).booleanValue();
    Map map = (Map)model.get("exportHeader");
    TableData td = null;
    if ((parents != null) && (children != null))
    {
      td = ExcelUtils.createTableData(dataList, ExcelUtils.createTableHeader(parents, children), exportField, c);
    }
    else {
      td = ExcelUtils.createTableData(dataList, ExcelUtils.createTableHeader(hearders), exportField, c);
    }
    GridReportBase report = new GridReportBase(request, response);
    report.exportToExcel(fileName, sheetName, createName, td, showTitle, map);
  }
}