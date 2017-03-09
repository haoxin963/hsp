package net.hsp.web.util.xls;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelUtils
{
  public static Map<String, Object> beanToMap(Object obj)
  {
    Map params = new HashMap(0);

    return params;
  }

  public static TableHeaderMetaData createTableHeader(List<String> list)
  {
    TableHeaderMetaData headMeta = new TableHeaderMetaData();
    for (String title : list) {
      TableColumn tc = new TableColumn();
      tc.setDisplay(title);
      headMeta.addColumn(tc);
    }
    return headMeta;
  }

  public static TableHeaderMetaData createTableHeader(String[] titls)
  {
    TableHeaderMetaData headMeta = new TableHeaderMetaData();
    String[] arrayOfString = titls; int j = titls.length; for (int i = 0; i < j; i++) { String title = arrayOfString[i];
      TableColumn tc = new TableColumn();
      tc.setDisplay(title);
      headMeta.addColumn(tc);
    }
    return headMeta;
  }

  public static TableHeaderMetaData createTableHeader(String[] parents, String[][] children)
  {
    TableHeaderMetaData headMeta = new TableHeaderMetaData();
    TableColumn parentColumn = null;
    TableColumn sonColumn = null;
    for (int i = 0; i < parents.length; i++) {
      parentColumn = new TableColumn();
      parentColumn.setDisplay(parents[i]);
      if ((children != null) && (children[i] != null)) {
        for (int j = 0; j < children[i].length; j++) {
          sonColumn = new TableColumn();
          sonColumn.setDisplay(children[i][j]);
          parentColumn.addChild(sonColumn);
        }
      }
      headMeta.addColumn(parentColumn);
    }
    return headMeta;
  }
 
  
  public static TableData createTableData(List list, TableHeaderMetaData headMeta, String[] exportField, ExcelConveter c)
  {
    TableData td = new TableData(headMeta);
    TableDataRow row = null;
    if ((list != null) && (!list.isEmpty()))
    {
      Iterator localIterator;
      if (list.get(0).getClass().isArray())
        for (localIterator = list.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
          row = new TableDataRow(td);
          for (Object o : (Object[])obj) {
            row.addCell(o);
          }
          td.addRow(row);
        }
      else {
        for (localIterator = list.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
          row = new TableDataRow(td);
          Map map = (obj instanceof Map) ? (Map)obj : beanToMap(obj);
          if(exportField==null || exportField.length==0){
    	    Set<String> keys = map.keySet();
        	for (String key : keys) { 
        		if (c!=null) {
        			 row.addCell(c.conveter(key, map.get(key)));
				}else{
						 row.addCell(map.get(key));
				}
        		
			} 
          }else{
	          for (int i = 0; i < exportField.length; i++) {
	            if (c != null)
	              row.addCell(c.conveter(exportField[i], map.get(exportField[i])));
	            else {
	              row.addCell(map.get(exportField[i]));
	            }
	
	          }
          }
          td.addRow(row);
        }
      }
    }
    return td;
  }

  
   

  public static String dumpCellStyle(HSSFCellStyle style) {
    StringBuffer sb = new StringBuffer();
    sb.append(style.getHidden()).append(",");
    sb.append(style.getLocked()).append(",");
    sb.append(style.getWrapText()).append(",");
    sb.append(style.getAlignment()).append(",");
    sb.append(style.getBorderBottom()).append(",");
    sb.append(style.getBorderLeft()).append(",");
    sb.append(style.getBorderRight()).append(",");
    sb.append(style.getBorderTop()).append(",");
    sb.append(style.getBottomBorderColor()).append(",");
    sb.append(style.getDataFormat()).append(",");
    sb.append(style.getFillBackgroundColor()).append(",");
    sb.append(style.getFillForegroundColor()).append(",");
    sb.append(style.getFillPattern()).append(",");
    sb.append(style.getIndention()).append(",");
    sb.append(style.getLeftBorderColor()).append(",");
    sb.append(style.getRightBorderColor()).append(",");
    sb.append(style.getRotation()).append(",");
    sb.append(style.getTopBorderColor()).append(",");
    sb.append(style.getVerticalAlignment());

    return sb.toString();
  }

  public static String dumpFont(HSSFFont font) {
    StringBuffer sb = new StringBuffer();
    sb.append(font.getItalic()).append(",").append(font.getStrikeout())
      .append(",").append(font.getBoldweight()).append(",").append(
      font.getCharSet()).append(",").append(font.getColor())
      .append(",").append(font.getFontHeight()).append(",").append(
      font.getFontName()).append(",").append(
      font.getTypeOffset()).append(",").append(
      font.getUnderline());
    return sb.toString();
  }

  public static void copyCellStyle(HSSFWorkbook destwb, HSSFCell dest, HSSFWorkbook srcwb, HSSFCell src)
  {
    if ((src == null) || (dest == null)) {
      return;
    }
    HSSFCellStyle nstyle = findStyle(src.getCellStyle(), srcwb, destwb);
    if (nstyle == null) {
      nstyle = destwb.createCellStyle();
      copyCellStyle(destwb, nstyle, srcwb, src.getCellStyle());
    }
    dest.setCellStyle(nstyle);
  }

  private static boolean isSameColor(short a, short b, HSSFPalette apalette, HSSFPalette bpalette)
  {
    if (a == b)
      return true;
    HSSFColor acolor = apalette.getColor(a);
    HSSFColor bcolor = bpalette.getColor(b);
    if (acolor == null)
      return true;
    if (bcolor == null)
      return false;
    return acolor.getHexString().equals(bcolor.getHexString());
  }

  private static short findColor(short index, HSSFWorkbook srcwb, HSSFWorkbook destwb)
  {
    Integer id = new Integer(index);
    if (HSSFColor.getIndexHash().containsKey(id))
      return index;
    if (index == 64)
      return index;
    HSSFColor color = srcwb.getCustomPalette().getColor(index);
    if (color == null) {
      return index;
    }

    HSSFColor ncolor = destwb.getCustomPalette().findColor(
      (byte)color.getTriplet()[0], (byte)color.getTriplet()[1], 
      (byte)color.getTriplet()[2]);
    if (ncolor != null)
      return ncolor.getIndex();
    destwb.getCustomPalette().setColorAtIndex(index, 
      (byte)color.getTriplet()[0], (byte)color.getTriplet()[1], 
      (byte)color.getTriplet()[2]);
    return index;
  }

  public static HSSFCellStyle findStyle(HSSFCellStyle style, HSSFWorkbook srcwb, HSSFWorkbook destwb)
  {
    HSSFPalette srcpalette = srcwb.getCustomPalette();
    HSSFPalette destpalette = destwb.getCustomPalette();

    for (short i = 0; i < destwb.getNumCellStyles(); i = (short)(i + 1)) {
      HSSFCellStyle old = destwb.getCellStyleAt(i);
      if (old != null)
      {
        if ((style.getAlignment() == old.getAlignment()) && 
          (style.getBorderBottom() == old.getBorderBottom()) && 
          (style.getBorderLeft() == old.getBorderLeft()) && 
          (style.getBorderRight() == old.getBorderRight()) && 
          (style.getBorderTop() == old.getBorderTop()) && 
          (isSameColor(style.getBottomBorderColor(), 
          old.getBottomBorderColor(), srcpalette, destpalette)))
          if ((style.getDataFormat() == old.getDataFormat()) && 
            (isSameColor(style.getFillBackgroundColor(), 
            old.getFillBackgroundColor(), srcpalette, destpalette)))
            if (isSameColor(style.getFillForegroundColor(), 
              old.getFillForegroundColor(), srcpalette, destpalette))
              if ((style.getFillPattern() == old.getFillPattern()) && 
                (style.getHidden() == old.getHidden()) && 
                (style.getIndention() == old.getIndention()) && 
                (isSameColor(style.getLeftBorderColor(), 
                old.getLeftBorderColor(), srcpalette, destpalette)))
                if ((style.getLocked() == old.getLocked()) && 
                  (isSameColor(style.getRightBorderColor(), 
                  old.getRightBorderColor(), srcpalette, destpalette)))
                  if ((style.getRotation() == old.getRotation()) && 
                    (isSameColor(style.getTopBorderColor(), 
                    old.getTopBorderColor(), srcpalette, destpalette)))
                    if (style.getVerticalAlignment() == 
                      old.getVerticalAlignment())
                      if (style.getWrapText() == old.getWrapText())
                      {
                        HSSFFont oldfont = destwb.getFontAt(old.getFontIndex());
                        HSSFFont font = srcwb.getFontAt(style.getFontIndex());
                        if ((oldfont.getBoldweight() == font.getBoldweight()) && 
                          (oldfont.getItalic() == font.getItalic()) && 
                          (oldfont.getStrikeout() == font.getStrikeout()) && 
                          (oldfont.getCharSet() == font.getCharSet()) && 
                          (isSameColor(oldfont.getColor(), font.getColor(), 
                          srcpalette, destpalette)))
                          if ((oldfont.getFontHeight() == font.getFontHeight()) && 
                            (oldfont.getFontName().equals(font.getFontName())) && 
                            (oldfont.getTypeOffset() == font.getTypeOffset()) && 
                            (oldfont.getUnderline() == font.getUnderline()))
                            return old;
                      }
      }
    }
    return null;
  }

  public static void copyCellStyle(HSSFWorkbook destwb, HSSFCellStyle dest, HSSFWorkbook srcwb, HSSFCellStyle src)
  {
    if ((src == null) || (dest == null))
      return;
    dest.setAlignment(src.getAlignment());
    dest.setBorderBottom(src.getBorderBottom());
    dest.setBorderLeft(src.getBorderLeft());
    dest.setBorderRight(src.getBorderRight());
    dest.setBorderTop(src.getBorderTop());
    dest.setBottomBorderColor(
      findColor(src.getBottomBorderColor(), srcwb, 
      destwb));
    dest.setDataFormat(destwb.createDataFormat()
      .getFormat(srcwb.createDataFormat().getFormat(src.getDataFormat())));
    dest.setFillPattern(src.getFillPattern());
    dest.setFillForegroundColor(
      findColor(src.getFillForegroundColor(), 
      srcwb, destwb));
    dest.setFillBackgroundColor(
      findColor(src.getFillBackgroundColor(), 
      srcwb, destwb));
    dest.setHidden(src.getHidden());
    dest.setIndention(src.getIndention());
    dest.setLeftBorderColor(
      findColor(src.getLeftBorderColor(), srcwb, 
      destwb));
    dest.setLocked(src.getLocked());
    dest.setRightBorderColor(
      findColor(src.getRightBorderColor(), srcwb, 
      destwb));
    dest.setRotation(src.getRotation());
    dest
      .setTopBorderColor(findColor(src.getTopBorderColor(), srcwb, 
      destwb));
    dest.setVerticalAlignment(src.getVerticalAlignment());
    dest.setWrapText(src.getWrapText());

    HSSFFont f = srcwb.getFontAt(src.getFontIndex());
    HSSFFont nf = findFont(f, srcwb, destwb);
    if (nf == null) {
      nf = destwb.createFont();
      nf.setBoldweight(f.getBoldweight());
      nf.setCharSet(f.getCharSet());
      nf.setColor(findColor(f.getColor(), srcwb, destwb));
      nf.setFontHeight(f.getFontHeight());
      nf.setFontHeightInPoints(f.getFontHeightInPoints());
      nf.setFontName(f.getFontName());
      nf.setItalic(f.getItalic());
      nf.setStrikeout(f.getStrikeout());
      nf.setTypeOffset(f.getTypeOffset());
      nf.setUnderline(f.getUnderline());
    }
    dest.setFont(nf);
  }

  private static HSSFFont findFont(HSSFFont font, HSSFWorkbook src, HSSFWorkbook dest)
  {
    for (short i = 0; i < dest.getNumberOfFonts(); i = (short)(i + 1)) {
      HSSFFont oldfont = dest.getFontAt(i);
      if ((font.getBoldweight() == oldfont.getBoldweight()) && 
        (font.getItalic() == oldfont.getItalic()) && 
        (font.getStrikeout() == oldfont.getStrikeout()) && 
        (font.getCharSet() == oldfont.getCharSet()) && 
        (font.getColor() == oldfont.getColor()) && 
        (font.getFontHeight() == oldfont.getFontHeight()) && 
        (font.getFontName().equals(oldfont.getFontName())) && 
        (font.getTypeOffset() == oldfont.getTypeOffset()) && 
        (font.getUnderline() == oldfont.getUnderline())) {
        return oldfont;
      }
    }
    return null;
  }


  public static void copyRow(HSSFWorkbook destwb, HSSFRow dest, HSSFWorkbook srcwb, HSSFRow src)
  {
    if ((src == null) || (dest == null))
      return;
    for (short i = 0; i <= src.getLastCellNum(); i = (short)(i + 1))
      if (src.getCell(i) != null) {
        HSSFCell cell = dest.createCell(i);
        copyCell(destwb, cell, srcwb, src.getCell(i));
      }
  }

  public static void copyCell(HSSFWorkbook destwb, HSSFCell dest, HSSFWorkbook srcwb, HSSFCell src)
  {
    if (src == null) {
      dest.setCellType(3);
      return;
    }

    if (src.getCellComment() != null)
      dest.setCellComment(src.getCellComment());
    if (src.getCellStyle() != null) {
      HSSFCellStyle nstyle = findStyle(src.getCellStyle(), srcwb, destwb);
      if (nstyle == null) {
        nstyle = destwb.createCellStyle();
        copyCellStyle(destwb, nstyle, srcwb, src.getCellStyle());
      }
      dest.setCellStyle(nstyle);
    }
    dest.setCellType(src.getCellType());

    switch (src.getCellType())
    {
    case 3:
      break;
    case 4:
      dest.setCellValue(src.getBooleanCellValue());
      break;
    case 2:
      dest.setCellFormula(src.getCellFormula());
      break;
    case 5:
      dest.setCellErrorValue(src.getErrorCellValue());
      break;
    case 0:
      dest.setCellValue(src.getNumericCellValue());
      break;
    case 1:
    default:
      dest.setCellValue(new HSSFRichTextString(
        src.getRichStringCellValue().getString()));
    }
  }
}