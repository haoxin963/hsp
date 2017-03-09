package net.hsp.entity.sys.upload;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

 
@Entity
@Table(name="pubmodule_attachments_tbl")
public class Attachment  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id 
    @Column(name="id", unique=true, nullable=false)
 	private long id ;

    
    @Column(name="custId", length=50)
 	private String custId ;

    
    @Column(name="type")
 	private String type ;

    
    @Column(name="fileName", length=100)
 	private String fileName ;

    
    @Column(name="filePath", length=200)
 	private String filePath ;

    
    @Column(name="keyUUID", nullable=false, length=32)
 	private String keyUuid ;

    
    @Column(name="status", nullable=false)
 	private int status ;

    public Attachment() {
    }

	
    public Attachment(long id, String keyUuid, int status) {
        this.id = id;
        this.keyUuid = keyUuid;
        this.status = status;
    }
    public Attachment(long id, String custId, String type, String fileName, String filePath, String keyUuid, int status) {
       this.id = id;
       this.custId = custId;
       this.type = type;
       this.fileName = fileName;
       this.filePath = filePath;
       this.keyUuid = keyUuid;
       this.status = status;
    }
   


    public long getId() {
        return this.id;
    }
    

    public void setId(long id) {
        this.id = id;
    }


    public String getCustId() {
        return this.custId;
    }
    

    public void setCustId(String custId) {
        this.custId = custId;
    }


    public String getType() {
        return this.type;
    }
    

    public void setType(String type) {
        this.type = type;
    }


    public String getFileName() {
        return this.fileName;
    }
    

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFilePath() {
        return this.filePath;
    }
    

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getKeyUuid() {
        return this.keyUuid;
    }
    

    public void setKeyUuid(String keyUuid) {
        this.keyUuid = keyUuid;
    }


    public int getStatus() {
        return this.status;
    }
    

    public void setStatus(int status) {
        this.status = status;
    }




}


