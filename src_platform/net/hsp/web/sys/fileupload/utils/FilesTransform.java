package net.hsp.web.sys.fileupload.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class FilesTransform {

	private static String PDF2SWF_PATH = "C:/Program Files (x86)/SWFTools/pdf2swf.exe";
	private static String languagedir = "C:/xpdf/xpdf-chinese-simplified";
	private static String linuxlanguagedir = "/usr/share/xpdf/xpdf-chinese-simplified";

	
	private static final String openofficeip = "127.0.0.1";
	private static final int openofficeport = 8100;
	/**
	 * text转PDF
	 * 
	 * @param textFilePath
	 * @param pdfFilePath
	 */
	public static void textToPdf(String textFilePath, String pdfFilePath) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
			document.open();
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			Font fontChinese = new Font(bfChinese, 12, Font.NORMAL);
			String txt = getTextFromTxt(textFilePath);
			document.add(new Paragraph(txt, fontChinese));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		} catch (Exception ioe) {
			System.err.println(ioe.getMessage());
		} finally {
			document.close();
		}
	}

	/**
	 * office转PDF
	 * @param sourceFile
	 * @param pdfFile
	 */
	public static synchronized void officeToPdf(File sourceFile, File pdfFile) throws Exception {
		System.out.println("start officeToPdf.......");
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(openofficeip, openofficeport);
		try {
			System.out.println("get openoffice connect.......");
			connection.connect();
			System.out.println("start converter.......");
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			converter.convert(sourceFile, pdfFile);
			pdfFile.createNewFile();
			System.out.println("end create pdfFile.......");
			connection.disconnect();
			System.out.println("disconnect officeToPdf.......");
		} catch (java.net.ConnectException e) {
			e.printStackTrace();
		} catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * pdf转swf
	 * @param pdfFile
	 * @param swfFile
	 */
	public static void pdfToSwfTest(File pdfFile, File swfFile) {
		Runtime r;
		r = Runtime.getRuntime();
		if (pdfFile.exists()) {
			try {
				Process process = r.exec(PDF2SWF_PATH + " " + pdfFile.getPath() + " -o " + getFilePath(swfFile.getPath()) + "%.swf" + " -s flashversion=9 -f -s languagedir=" + languagedir);
				StreamGobbler sg1 = new StreamGobbler(process.getInputStream(), "Console");
				StreamGobbler sg2 = new StreamGobbler(process.getErrorStream(), "Error");
				sg1.start();
				sg2.start();
				try {
					process.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				swfFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					throw e;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else {
		}
	}
	
	public static String getFilePath(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index != -1) {
			String path = fileName.substring(0,index);
			return path;
		} else {
			return fileName;
		}
	}
	
	/**
	 * pdf转swf
	 * 
	 * @param pdfFile
	 * @param swfFile
	 */
	public static void pdfToSwf(File pdfFile, File swfFile) {
		System.out.println("start create swf.....");
		if (pdfFile.exists()) {
			try {
				Process process = null;
				if(isWindows()){
					System.out.println("isWindows.....");
					Runtime r = Runtime.getRuntime();
					process = r.exec(PDF2SWF_PATH + " " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -s flashversion=9 -f -s languagedir=" + languagedir);
				} else {
					System.out.println("isLinux.....");
					String[] command = { "/bin/sh", "-c", "pdf2swf "+" " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -s flashversion=9 -f -s languagedir=" + linuxlanguagedir};
					process = Runtime.getRuntime().exec(command );
					System.out.println("exec command.....");
				}
				StreamGobbler sg1 = new StreamGobbler(process.getInputStream(), "Console");
				StreamGobbler sg2 = new StreamGobbler(process.getErrorStream(), "Error");
				sg1.start();
				sg2.start();
				try {
					process.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				swfFile.createNewFile();
				System.out.println("end create swf.....");
			} catch (Exception e) {
				e.printStackTrace();
				try {
					throw e;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else {
			System.out.println("文件不存在！");
		}
	}

	/**
	 * pdf转swf
	 * 
	 * @param pdfFile
	 * @param swfFile
	 */
	public static void pdfToSwf2(File pdfFile, File swfFile) {
		
		Runtime r;
		r = Runtime.getRuntime();
		if (pdfFile.exists()) {
			try {
				Process process = r.exec(PDF2SWF_PATH + " " + pdfFile.getPath() + " -o " + swfFile.getPath() + " -s flashversion=9 -s languagedir=" + languagedir + " -G -s bitmap");
				StreamGobbler sg1 = new StreamGobbler(process.getInputStream(), "Console");
				StreamGobbler sg2 = new StreamGobbler(process.getErrorStream(), "Error");
				sg1.start();
				sg2.start();
				try {
					process.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				swfFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					throw e;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else {
			System.out.println("文件不存在！");
		}
	}
	
	private static boolean isWindows(){
		String osName = System.getProperty("os.name");  
        if (osName == null) {  
        	return false; 
        }  
        osName = osName.toLowerCase();  
        if (osName.indexOf("windows") != -1) {  
        	return true;
        }
        return false;
	}

	/**
	 * 
	 * 获取text内容
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String getTextFromTxt(String filePath) throws Exception {
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		StringBuffer buff = new StringBuffer();
		String temp = null;
		while ((temp = br.readLine()) != null) {
			buff.append(temp + "\r\n");
		}
		br.close();
		return buff.toString();
	}
	
	private static String getIp()
	{
		String ipAddress = null;
		try
		{ 
			ipAddress = InetAddress.getLocalHost().toString();

			if(ipAddress.indexOf("/")>0){
				ipAddress = ipAddress.substring(ipAddress.lastIndexOf("/")+1);
			}
		} catch (UnknownHostException e)
		{ 
			e.printStackTrace();
		}
		return ipAddress;
	}

}
