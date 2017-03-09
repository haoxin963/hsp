package net.hsp.web.util;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

/**
 * 上传进度监听，将实时进度写入session，便于ajax异步从服务器端获取
 * @author JSmart
 *
 */
public class FileUploadProcessListener implements ProgressListener {
    private String id;
    private HttpSession session;
    private long[] progressData = new long[2];
    private long megaBytes = -1;
    
    /**
     * 
     * @param id 上传ID，标识一次上传动作的唯一KEY
     * @param session
     */
    public FileUploadProcessListener(String id, HttpSession session) {
        this.id = id;
        this.session = session; 
    }
    public void update(long pBytesRead, long pContentLength, int pItems) {
        progressData[0] = pBytesRead;
        progressData[1] = pContentLength;
        session.setAttribute(id, progressData);
        long mBytes = pBytesRead / 1000;
        if (megaBytes == mBytes) {
            return;
        }
        megaBytes = mBytes;
    }
}
