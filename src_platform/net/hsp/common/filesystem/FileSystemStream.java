package net.hsp.common.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.hsp.common.constants.PlatFormConstant;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FileSystemStream implements IFileSystemStream {
	// 日志
	private Logger logger = LogManager.getLogger(FileSystemStream.class);

	// 文件或文件目录
	private File file;
	// 指定文件的文件流
	private InputStream istream = null;
	// 文件系统站点主路径
	private String rootPath = null;
	// 文件系统指定目录或文件全路径
	private String fullPath = null;
	// 文件名称
	private String fileName = null;
	// 站点名称
	private String instanceName;

	// 站点文件系统所用空间
	private long length = 0;
	// 站点文件系统总空间
	private long totalLength = 0;

	/**
	 * 本地文件系统构造器
	 * 
	 * @param root
	 *            站点文件系统配置路径
	 * @param instanceName
	 *            站点名称
	 * @param moduleName
	 *            指定操作路径
	 * @param fileName
	 *            文件名
	 */
	public FileSystemStream(String root, String instanceName, String moduleName, String fileName) {
		this.instanceName = instanceName.trim();
		this.rootPath = revisePath(root.trim()) + this.instanceName;
		this.fullPath = revisePath(rootPath) + moduleName.trim();
		this.file = new File(this.fullPath);
		if (fileName != null && !fileName.equals("")) {
			this.file = new File(revisePath(this.fullPath) + fileName);
		}
		this.fileName = fileName;
		cdDirectory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#close()
	 */
	@Override
	public void close() {
		if (this.istream == null)
			return;
		try {
			this.istream.close();
		} catch (Exception ex) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#delete()
	 */
	@Override
	public boolean delete() {
		if (!exists())
			return false;
		// 不允许删除站点根文件系统 2012-10-10
		if (this.file.getAbsolutePath().equals(new File(this.rootPath).getAbsolutePath())) {
			System.out.println("不允许删除站点根文件系统 2012-10-10 ");
			StackTraceElement[] els = Thread.currentThread().getStackTrace();
			for (int i = 0; i < els.length; i++) {
				System.out.println(els[i]);
			}
			return false;
		}
		System.out.println("文件系统日志:" + file.getAbsolutePath() + "被删除!");
		if (this.file.isFile()) {
			return this.file.delete();
		} else
			deleteDir(this.file);
		return true;
	}

	/*
	 * 递归删除文件
	 */
	private void deleteDir(File f) {
		if (f.isDirectory()) {
			File[] fList = f.listFiles();
			for (int j = 0; j < fList.length; j++) {
				deleteDir(new File(fList[j].getPath()));
			}
		}
		f.delete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#list()
	 */
	@Override
	public List<FileSystem> list() {
		return list(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#list(boolean)
	 */
	@Override
	public List<FileSystem> list(boolean listDir) {
		List<FileSystem> results = new ArrayList<FileSystem>();

		if (!exists())
			return results;

		File[] tmpfile = this.file.listFiles();
		if (tmpfile != null && tmpfile.length > 0) {
			for (int i = 0; i < tmpfile.length; i++) {
				if (!listDir) {
					if (tmpfile[i].isDirectory())
						continue;
				}

				FileSystem result = new FileSystem();
				result.setModifyDate(new java.util.Date(tmpfile[i].lastModified()));
				result.setName(tmpfile[i].getName());
				result.setSize(tmpfile[i].length());
				result.setFile(true);
				if (tmpfile[i].isDirectory()) {
					result.setFile(false);
				}
				results.add(result);
			}
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#modifyDate()
	 */
	@Override
	public Date modifyDate() {
		if (!exists())
			return null;

		return new Date(this.file.lastModified());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#read()
	 */
	@Override
	public InputStream read() {
		if (!exists())
			return null;

		try {
			this.istream = new FileInputStream(this.file);
			return istream;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#ren(java.lang.String)
	 */
	@Override
	public boolean ren(String dest) {
		if (!exists())
			return false;

		String filePath = null;

		if (this.file.isFile()) {
			filePath = revisePath(this.fullPath) + dest;
		} else {
			filePath = revisePath(this.rootPath) + dest;

			if (dest.indexOf("/") == -1 && dest.indexOf(File.separator) == -1) {
				filePath = revisePath(this.file.getParent()) + dest;
			}
		}

		File dt = new File(filePath);
		boolean cb = this.file.renameTo(dt);
		if (cb) {
			this.file = dt;
			if (this.file.isFile()) {
				this.fileName = dest;
			}
		}
		return cb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#exists()
	 */
	@Override
	public boolean exists() {
		return this.file.exists();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#isValidate()
	 */
	@Override
	public boolean isValidate() {
		return exists();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#size()
	 */
	@Override
	public long size() {
		if (!exists())
			return 0;
		return this.file.length();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#write(java.io.InputStream)
	 */
	@Override
	public boolean write(InputStream os) {
		System.out.println("文件系统日志:" + file.getAbsolutePath() + "被写入!");
		try {
			return write(os, false);
		} catch (Exception e) {
			logger.error("站点[" + this.instanceName + "]空间读取操作失败:" + e.getMessage());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#write(java.io.InputStream,
	 *      boolean)
	 */
	@Override
	public boolean write(InputStream fis, boolean isAppend) throws Exception {
		if (!exists()) {
			cdDirectory();
		}

		if (!isSpaceFree(fis)) {
			return false;
		}
		System.out.println(this.file.getCanonicalPath());
		FileOutputStream fos = new FileOutputStream(this.file, isAppend);
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			fos.close();
			fis.close();
		}
		return true;

	}

	/*
	 * 站点是否还有剩余空间
	 */
	private boolean isSpaceFree(InputStream fis) {
		try {
			String space = FileSystemUtil.getInstance().getProperty(this.instanceName + ".filesystem.maxspace");
			if (space == null || space.trim().equals(""))
				return true;

			this.totalLength = Long.parseLong(space) * 1024 * 1024;
			this.length = 0;
			long usedSpace = getLength();

			int available = fis.available();
			if (available != 0) {
				if (available > (totalLength - usedSpace)) {
					logger.warn("站点空间不足，总空间:" + totalLength + ",已用空间:" + usedSpace);
					return false;
				}
			}

			if (totalLength < usedSpace) {
				logger.warn("站点空间不足，总空间:" + totalLength + ",已用空间:" + usedSpace);
				return false;
			}
		} catch (Exception e) {
			logger.error("站点[" + this.instanceName + "]空间读取操作失败:" + e.getMessage());
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hsp.filesystem.IFileSystemStream#getLength()
	 */
	@Override
	public long getLength() {
		if (length == 0) {
			sdl(this.rootPath);
		}

		return length;
	}

	/*
	 * 递归读取目录大小，并保存目录大小的变量
	 */
	private void sdl(String dirname) {
		File dir = new File(dirname);
		String f[] = dir.list();
		File f1;
		for (int i = 0; i < f.length; i++) {
			f1 = new File(dirname + "/" + f[i]);
			if (!f1.isDirectory())
				length += f1.length();
			else
				sdl(dirname + "/" + f[i]);
		}
	}

	/**
	 * 判断文件系统目录是否存在
	 * 
	 * @param root
	 *            站点文件系统配置路径
	 * @param instanceName
	 *            站点名称
	 * @param moduleName
	 *            指定操作路径
	 * @return
	 */
	public static boolean isDirectoryExists(String root, String instanceName, String moduleName) {
		String path = revisePath(root) + revisePath(instanceName) + moduleName;
		File tmp = new File(path);
		return tmp.exists();
	}

	/*
	 * 修正路径
	 */
	private static String revisePath(String path) {
		String newPath = path;
		if (!path.endsWith(File.separator) && !path.endsWith("/")) {
			newPath = path + "/";
		}
		return newPath;
	}

	/*
	 * 创建模块目录
	 */
	private void cdDirectory() {
		File filePath = new File(this.fullPath);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
	}

	public String getDir() {
		return this.file.getPath();
	}

	@Override
	public boolean copy(IFileSystemStream to) {
		if (this.file.isDirectory()) {
			copyFolder(this.getDir(),to.getDir());
		}
		return false;
	}

	private  void copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) { 
			logger.error("站点[" + this.instanceName + "]复制整个文件夹内容操作出错:" + e.getMessage());
		}

	}

}
