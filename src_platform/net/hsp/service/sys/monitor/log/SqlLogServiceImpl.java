package net.hsp.service.sys.monitor.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.dao.jdbc.BaseDAO;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.PageInfo;

@Service
@ServiceLogUtil(name = "sql日志业务类")
@Lazy(true)
public class SqlLogServiceImpl implements SqlLogService {

	private static Logger logger = Logger.getLogger(SqlLogServiceImpl.class);
	
	@MethodLogUtil(type="",value="查询SQL警告日志")
	@Override
	public Map<String, Object> querySqlWarn(PageInfo pageInfo) {
		Map<String, Object> map = operLogFile("warn.log", pageInfo);
		return map;
	}
	
	@MethodLogUtil(type="",value="查询SQL信息日志")
	@Override
	public Map<String, Object> querySqlInfo(PageInfo pageInfo) {
		Map<String, Object> map = operLogFile("info.log", pageInfo);
		return map;
	}

	private Map<String, Object> operLogFile(String fileName, PageInfo pageInfo) {
		String defaultPath = ActionUtil.getCtx().getServletContext().getRealPath("/page");
		String filePath = new File(defaultPath, "logs").getAbsolutePath();
		File[] files = new File(filePath).listFiles();
		if (files == null) {
			logger.warn(filePath + "URL不存在");
			return null;
		}

		Map<String, Object> map = null;

		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			if (name.equals(fileName)) {
				try {
					int fromIndex = Math.max(0, pageInfo.getPage() - 1) * pageInfo.getRows();
					int toIndex = fromIndex + pageInfo.getRows();
					map = readFileByLines(files[i], fromIndex, toIndex);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	private Map<String, Object> readFileByLines(File file, int fromIndex, int toIndex) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		RandomAccessFile raf = null;
		int line = 0;
		try {
			raf = new RandomAccessFile(file, "r");
			long fileSize = raf.length();
			long pos = fileSize;
			while (pos > 0) {
				pos--;
				raf.seek(pos);
				if (pos==0 || raf.readByte() == '\n') {
					String lastLine = raf.readLine();
					if(lastLine == null) { continue; }
					if(line >= toIndex) { break; }
					if (line >= fromIndex && line < toIndex) {
						lastLine = new String(lastLine.getBytes("iso-8859-1"), "gbk");
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("itemid", (line+1));
						item.put("sql", lastLine);
						items.add(item);
					}
					line++;
				}
			}
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (raf != null) {
				try {
					raf.close();
					raf = null;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		map.put("total", 500);//line
		map.put("rows", items);
		return map;
	}

}
