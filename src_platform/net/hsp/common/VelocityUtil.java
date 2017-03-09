package net.hsp.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class VelocityUtil {

	/**
	 * 
	 * @param templateDir 
	 * @param templateFileName
	 * @param outputFileName 
	 * @param context
	 */
	public void genFile(String templateDir,String templateFileName,
			String outputFileName, VelocityContext context) {
		try {
			Properties prop = new Properties();
			prop.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
			prop.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
			prop.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templateDir);
			VelocityEngine velocityEngine = new VelocityEngine();
			velocityEngine.init(prop);

			Template template = null;
			try {
				template = velocityEngine.getTemplate(templateFileName, "UTF-8");
			} catch (ResourceNotFoundException e1) {
				System.out.println("Velocity error,ResourceNotFoundException:"
						+ templateFileName);
			} catch (ParseErrorException e2) {
				System.out.println("Velocity error,ParseErrorException:"
						+ templateFileName + ":" + e2);
			}

			File _file = new File(outputFileName);
			if (!_file.getParentFile().exists()) {
				_file.getParentFile().mkdirs();
			}
			// 如果不覆盖，则使用以下命名策略

			OutputStream out = new FileOutputStream(_file.getCanonicalPath());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					out, "UTF-8"));// 文件生成时的格式为UTF-8
			if (template != null) {
				template.merge(context, writer);
			}
			writer.flush();
			writer.close();
			out.close();
			System.out.println("111");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
