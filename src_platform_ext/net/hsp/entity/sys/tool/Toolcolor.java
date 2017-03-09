package net.hsp.entity.sys.tool;
import net.hsp.common.Comment;

import org.springframework.format.annotation.DateTimeFormat;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

 
@Entity
	@Comment(value = "pubmodule_toolcolor_tbl")
@Table(name="pubmodule_toolcolor_tbl"
)
public class Toolcolor  implements java.io.Serializable {

     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
	 
	 
 	private Integer id ;

	@Comment(value = "用户表外键")

    
    @Column(name="userid")
	 
	 
 	private Integer userid ;

	@Comment(value = "组件对应id")

    
    @Column(name="toolid")
	 
	 
 	private Integer toolid ;

	@Comment(value = "组件属性值")

    
    @Column(name="stylevalue", length=100)
	 
	 
 	private String stylevalue ;

	@Comment(value = "对应组件属性id")

    
    @Column(name="styleid")
	 
	 
 	private Integer styleid ;

    public Toolcolor() {
    }

    public Toolcolor(Integer userid, Integer toolid, String stylevalue, Integer styleid) {
       this.userid = userid;
       this.toolid = toolid;
       this.stylevalue = stylevalue;
       this.styleid = styleid;
    }
   


    public Integer getId() {
        return this.id;
    }
    

    public void setId(Integer id) {
        this.id = id;
    }

/** get 用户表外键*/
    public Integer getUserid() {
        return this.userid;
    }
    
/** set 用户表外键*/
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

/** get 组件对应id*/
    public Integer getToolid() {
        return this.toolid;
    }
    
/** set 组件对应id*/
    public void setToolid(Integer toolid) {
        this.toolid = toolid;
    }

/** get 组件属性值*/
    public String getStylevalue() {
        return this.stylevalue;
    }
    
/** set 组件属性值*/
    public void setStylevalue(String stylevalue) {
        this.stylevalue = stylevalue;
    }

/** get 对应组件属性id*/
    public Integer getStyleid() {
        return this.styleid;
    }
    
/** set 对应组件属性id*/
    public void setStyleid(Integer styleid) {
        this.styleid = styleid;
    }




}


