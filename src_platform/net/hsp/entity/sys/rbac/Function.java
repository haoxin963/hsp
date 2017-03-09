package net.hsp.entity.sys.rbac;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "权限表")
@Table(name = "pubmodule_function_tbl")
public class Function implements java.io.Serializable {

	@Comment(value = "主键")
	@Id
	@Column(name = "functionId", unique = true, nullable = false)
	private String functionId;
	
	@Comment(value = "按钮ID")
	@Column(name = "buttonId", length = 30)
	private String buttonId;

	@Comment(value = "是否有子节点")
	@Column(name = "child", length = 1)
	private String child;

	@Comment(value = "特殊功能标识")
	@Column(name = "flag", length = 1)
	private String flag;

	@Comment(value = "功能名称")
	@Column(name = "functionName", nullable = false, length = 65535)
	private String functionName;
 

	@Comment(value = "功能地址")
	@Column(name = "linkAddress")
	private String linkAddress;

	@Comment(value = "父节点ID")
	@Column(name = "parent_id")
	private String parentId;

	@Comment(value = "图片地址")
	@Column(name = "pictureAddr")
	private String pictureAddr;

	@Comment(value = "所在层级")
	@Column(name = "lev")
	private Integer lev;
	
	@Comment(value = "排序号")
	@Column(name = "sortNo")
	private Integer sortNo;

	@Comment(value = "按钮/链接")
	@Column(name = "tag", length = 1)
	private String tag;
	
	@Comment(value = "是否显示")
	@Column(name = "type", length = 1)
	private String type;	
	
	@Comment(value = "内联地址")
	@Column(name = "innerUrl", length = 500)
	private String innerUrl;
	
	
	public Function() {
	}
 
	/** get 主键 */
	public String getFunctionId() {
		return this.functionId;
	}

	/** set 主键 */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	/** get 按钮ID */
	public String getButtonId() {
		return this.buttonId;
	}

	/** set 按钮ID */
	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	/** get 是否有子节点 */
	public String getChild() {
		return this.child;
	}

	/** set 是否有子节点 */
	public void setChild(String child) {
		this.child = child;
	}

	/** get 特殊功能标识 */
	public String getFlag() {
		return this.flag;
	}

	/** set 特殊功能标识 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/** get 功能名称 */
	public String getFunctionName() {
		return this.functionName;
	}

	/** set 功能名称 */
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}


	/** get 功能连接地址 */
	public String getLinkAddress() {
		return this.linkAddress;
	}

	/** set 功能连接地址 */
	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	/** get 父节点ID */
	public String getParentId() {
		return this.parentId;
	}

	/** set 父节点ID */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/** get 图片地址 */
	public String getPictureAddr() {
		return this.pictureAddr;
	}

	/** set 图片地址 */
	public void setPictureAddr(String pictureAddr) {
		this.pictureAddr = pictureAddr;
	}

	public Integer getLev() {
		return lev;
	}

	public void setLev(Integer lev) {
		this.lev = lev;
	}

	/** get 排序号 */
	public Integer getSortNo() {
		return this.sortNo;
	}

	/** set 排序号 */
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	/** get 标签 */
	public String getTag() {
		return this.tag;
	}

	/** set 标签 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/** get 类型 */
	public String getType() {
		return type;
	}

	/** set 类型 */
	public void setType(String type) {
		this.type = type;
	}

	/** get内联地址**/
	public String getInnerUrl() {
		return innerUrl;
	}

	/** set内联地址**/
	public void setInnerUrl(String innerUrl) {
		this.innerUrl = innerUrl;
	}
	
}
