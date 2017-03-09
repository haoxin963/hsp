package net.hsp.web.util;

import java.awt.Color;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.hsp.web.constants.WebConstant;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 生成PDF视图，可用PDF浏览器打开或者保存 由ViewController的return new ModelAndView(viewPDF,
 * model)生成
 */
public class ViewPDF extends AbstractPdfView {

	@Override
	public void buildPdfDocument(Map model, Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
 
		String[] hearders = null;
		String[] exportField = null;
		String fileName = null;
		if(request.getParameter("exportHead")!=null){
			hearders =request.getParameter("exportHead").split(",");
		}else{
			hearders = (String[]) model.get("exportHead");
		}
		if(request.getParameter("exportField")!=null){
			exportField =request.getParameter("exportField").split(",");
		}else{
			exportField = (String[]) model.get("exportField");
		} 
		if (hearders==null) { 
			return;
		}
		
		if(request.getParameter("exportTitle")!=null){
			fileName = request.getParameter("exportTitle");
		}else{
			fileName =  (String) model.get("exportTitle");
		}
		if (fileName==null) { 
			return;
		}
		String agent = request.getHeader("User-Agent");
		boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
		if (isMSIE) {
		    //fileName = URLEncoder.encode(fileName);
		} else {
		   // fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		 
		String createName = "admin";
		Map m = (Map)model.get("command");
		List dataList = null;
		if(m!=null){
			dataList = (List) m.get("rows");
		}
		
		// 设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
	 
		
		Map m2 = (Map)model.get(WebConstant.COMMAND);
		Object obj = null;
		if(m2!=null){
			obj = (List) m2.get(PageInfo.ROWS);
		}
		if (obj instanceof List) {
			Table table = new Table(exportField.length);
			table.setBorderWidth(0);
			table.setBorderColor(new Color(220, 255, 100));
			table.setPadding(1);
			table.setSpacing(0);
			BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);//设置中文字体
			Font headFont = new Font(bfChinese, 8, Font.BOLD);//设置字体大小 

			for (int i = 0; i < exportField.length; i++) { 
				PdfPCell  cell  =  new PdfPCell(new Paragraph(hearders[i],headFont));//建立一个单元格
				Cell  cell2 = new Cell(hearders[i]);
				table.addCell(cell2);
			}
			table.endHeaders();
			List list = (List) obj; 
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i); 
				for(int k =0;k<exportField.length;k++){
					table.addCell(map.get(exportField[k])+"");
		    	}
			}
			document.add(table);
		} else if (obj instanceof Map) {
			Map map = (Map) obj;
			Set<String> keys = map.keySet();
			for (String key : keys) {
				document.add(new Paragraph(map.get(key).toString()));
			}
		} 
		document.close();
	}
}
