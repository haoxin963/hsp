package net.hsp.entity.sys.weixin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment("微信")
@Table(name="uisp_weixinapp_tbl")
public class WeixinApp implements Serializable{

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id", unique=true, nullable=false)
  private Integer id;

  @Comment("appId")
  @Column(name="appId", nullable=false, length=32)
  private String appId;

  @Comment("secret")
  @Column(name="secret", nullable=false, length=32)
  private String secret;

  @Comment("所属站点")
  @Column(name="custId", nullable=false, length=32)
  private String custId;


  @Comment("微信地址")
  @Column(name="serverUrl", length=50)
  private String serverUrl;
  
  public String getProxyUrl() {
	return proxyUrl;
}

public void setProxyUrl(String proxyUrl) {
	this.proxyUrl = proxyUrl;
}

@Comment("OBL地址")
  @Column(name="proxyUrl", length=50)
  private String proxyUrl;
  
  @Comment("微信用户名")
  @Column(name="userName", length=32)
  private String userName;

  @Comment("微信密码")
  @Column(name="pwd", length=32)
  private String pwd;

  @Comment("二维码")
  @Column(name="barcode", length=32)
  private String barcode;

  @Column(name="delTag", length=1)
  private String delTag;

  public WeixinApp()
  {
  }

  public WeixinApp(String appId, String secret, String custId)
  {
    this.appId = appId;
    this.secret = secret;
    this.custId = custId;
  }

  public WeixinApp(String appId, String secret, String custId, String userName, String pwd, String barcode, String delTag) {
    this.appId = appId;
    this.secret = secret;
    this.custId = custId;
    this.userName = userName;
    this.pwd = pwd;
    this.barcode = barcode;
    this.delTag = delTag;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAppId()
  {
    return this.appId;
  }

  public void setAppId(String appId)
  {
    this.appId = appId;
  }

  public String getSecret()
  {
    return this.secret;
  }

  public void setSecret(String secret)
  {
    this.secret = secret;
  }

  public String getCustId()
  {
    return this.custId;
  }

  public void setCustId(String custId)
  {
    this.custId = custId;
  }

  public String getUserName()
  {
    return this.userName;
  }

  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  public String getPwd()
  {
    return this.pwd;
  }

  public void setPwd(String pwd)
  {
    this.pwd = pwd;
  }

  public String getBarcode()
  {
    return this.barcode;
  }

  public void setBarcode(String barcode)
  {
    this.barcode = barcode;
  }

  public String getDelTag() {
    return this.delTag;
  }

  public void setDelTag(String delTag) {
    this.delTag = delTag;
  }

public String getServerUrl() {
	return serverUrl;
}

public void setServerUrl(String serverUrl) {
	this.serverUrl = serverUrl;
}
 
  
}
