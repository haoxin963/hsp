<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>用户自定义皮肤</title>

<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<link rel="stylesheet" href="${path }/resource/jQuery-minicolors/jquery.minicolors.css"/>
<script src="${path }/resource/jQuery-minicolors/jquery.minicolors.js"></script>
<style>
.layui-form-select{
 width:300px;
}
.layui-input{
 width:300px;
}
.img{
   z-index:2000;
   width:500px;
   height:500px;
   border:1px  gray solid;
    position:absolute;
    left:145px;
    top:50px;
    background-color:white;
    overflow:auto;
    box-shadow: 5px 0px 5px darkgray;
}
a{
curosr: pointer;  
}
.imglist{
    width:150px;
    height:150px;
    float:left;
    margin-left:10px;
    margin-top:10px;
     border:1px  gray solid;
     box-shadow: inset 0 0 5px 5px darkgray;
     text-align:center;
      display: -webkit-box;
      display: -webkit-flex;
      
      display: -moz-box;
      display: -moz-flex;
      
      display: -ms-flexbox;
      
      display: flex;
      
      /* 水平居中*/
      -webkit-box-align: center;
      -moz-box-align: center;
      -ms-flex-pack:center;/* IE 10 */
      
      -webkit-justify-content: center;
      -moz-justify-content: center;
      justify-content: center;/* IE 11+,Firefox 22+,Chrome 29+,Opera 12.1*/
      
      /* 垂直居中 */
      -webkit-box-pack: center;
      -moz-box-pack: center;
      -ms-flex-align:center;/* IE 10 */
      
      -webkit-align-items: center;
      -moz-align-items: center;
      align-items: center;
}
.imgclick{
    margin-left:220px;
    margin-top:-38px;
    color:blue;
}
.aaa {
   box-shadow: 5px 0px 5px darkgray;
   background-color:yellow;
}
.img  a:hover div{
    box-shadow: 5px 0px 5px darkgray;
     background-color:yellow;
}
.imglist img{

max-height:150px; 
 max-width:150px; 
 width:expression(this.width >150?"150px":"auto"); 
 height:expression(this.height > 150?"150px":"auto");
}
</style>
</head>
<body>
     <div  class="img" >

         
           
     </div>
	<br>
	<f:form class="layui-form" action="${path}/sys/tool/toolcolor/doSave.json"
		id="toolForm" modelAttribute="command" method="POST"  >
		<f:hidden path="id" required='true' number='true' maxlength='255' />
		<input type="hidden" name="ss" id="ss">
		<input type="hidden" name="userid"  value="${userid }"  id="userid">
		<table class="table_one">
			    <c:forEach var="list" items="${list}">
			             <tr>
			                     <td>
			                             <div class="layui-form-item">
			                                        <label class="layui-form-label"><span class='required'>*</span>
						                                           ${list.stylename} ${separator}</label>			
						                  <c:choose>
						                        <c:when test="${list.styletype==0 }">
						                                        
			                             <div class="layui-input-block">
							                        <input type="text"  name="stylevalue"  id="${list.id }" value="${list.stylevalue }" maxlength='50' autocomplete="off"
								                    placeholder="请选择修改属性" class="layui-input  demo stylevalue"  lay-verify="required"   data-control="hue"   style="width:500px;height:38px;"/>
						                            </div>
						                    </div>    
						                    </c:when> 
						                       <c:when test="${list.styletype==1 }">
						                               <div class="layui-input-block">
							                        <input type="text"  name="stylevalue"  id="${list.id }" value="${list.stylevalue }" maxlength='50' autocomplete="off"
								                    placeholder="请选择图片" class="layui-input  stylevalue"  lay-verify="required"   style="width:200px;"/><a  href="javascript:;"  ><div  ref="${list.filepath }" title="${list.id }" class="imgclick">请选择图片</div></a>
						                            <div  class="imglist"  id="imgshow${list.id }" ref="${list.filepath }">
						                                 <img src="${path}/${list.filepath }/${list.stylevalue }">
						                            </div>
						                            </div>
						                    </div>  
						                       </c:when>
						                       <c:when test="${list.styletype==2 }">
						                        <div class="layui-input-block" >
						                                 <select name="stylevalue" lay-filter="value" class="stylevalue" >
                                                                   <option value="solid"  <c:if test="${list.stylevalue=='solid'}"> selected=""</c:if>>实线</option>
                                                                   <option value="dashed"  <c:if test="${list.stylevalue=='dashed'}"> selected=""</c:if>>虚线</option>
                                                       </select>      
                                                       
                                                   </div>    
						                     </c:when>
						                     
						                      <c:otherwise>
						                       <div class="layui-input-block">
						                              <select name="stylevalue" lay-filter="value"  class="stylevalue" >
                                                                   <option value="12px"   <c:if test="${list.stylevalue=='14px'}"> selected=""</c:if>>小</option>
                                                                   <option value="16px"  <c:if test="${list.stylevalue=='18px'}"> selected=""</c:if>>中</option>
                                                                   <option value="20px" <c:if test="${list.stylevalue=='24px'}"> selected=""</c:if>>大</option>
                                                       </select>
                                                  </div>     
						                      </c:otherwise>   
						                    </c:choose>  
			                     </td>
			             </tr>
			    </c:forEach>	
		</table>


	</f:form>
	<script>
		var serverURL = "${path}/sys/tool/toolcolor";
		var formId = "toolForm";

		function formVerifyExt() {
			//自定义验证规则
		
		}

		function doSave(obj) {
			var str="[";
			$(".stylevalue").each(function(){
				   str+="{'id':'"+$(this).attr("id")+"','value':'"+$(this).val()+"'},";
			});
			str=(str.slice(str.length-1)==',')?str.slice(0,-1):str;
			str+="]";
			$("#ss").val(str);
			if (valid()) {
				$("#" + formId).ajaxSubmit({
					success : function(result) {
						result = jQuery.parseJSON(result);
						if (result.status == "1") {
							obj.closeAndRef();
						} else {
							alertError(result.msg);
						}
					}
				});

			}
		}
	</script>
     	<script>
		$(document).ready( function() {
			$(".img").hide();
			
            $('.demo').each( function() {
				$(this).minicolors({
					control: $(this).attr('data-control') || 'hue',
					defaultValue: $(this).attr('data-defaultValue') || '',
					inline: $(this).attr('data-inline') === 'true',
					letterCase: $(this).attr('data-letterCase') || 'lowercase',
					opacity: $(this).attr('data-opacity'),
					position: $(this).attr('data-position') || 'bottom left',
					change: function(hex, opacity) {
						var log;
						try {
							log = hex ? hex : 'transparent';
							if( opacity ) log += ', ' + opacity;
							console.log(log);
						} catch(e) {}
					},
					theme: 'default'
				});
			
				
				
				
            });
			

		$(".imgclick").click(function(){
			    var id=$(this).attr("title");
			    var filepath=$(this).attr("ref");
				var data = {
			             filepath:filepath
		             }
		         $.ajax({
		             type: 'POST',
		             url: '${path}/sys/tool/toolcolor/imglist.json',
		             data: data ,
		             success:  function(res){
		            		result = jQuery.parseJSON(res);
		            		str="";
		            		for(i=0;i<result.imglist.length;i++){
		            			str+="  <a  href='javascript:;'   >";
		            			if(result.imglist[i]==$("#"+id).val()){
		            				str+=" <div  class='imglist aaa'  ref='"+result.imglist[i]+" id='imgname"+i+"' onclick='choose("+i+","+id+")'>";
		            			}else{
		            				str+=" <div  class='imglist' ref='"+result.imglist[i]+"' id='imgname"+i+"'  onclick='choose("+i+","+id+")'>";
		            			}
		            		     str+="<img src='${path}/"+filepath+"/"+result.imglist[i]+"' >";
            	                 str+="</div>";
            	                 str+="</a>";
		            		}
		        	     $(".img").show(); 
		        	     $(".img").html(str);
		        	     
			        }       	 	 
		        });       	
			});
		});
	
		function choose(i,id){
			var imgname=$("#imgname"+i).attr("ref");
			$("#"+id).val(imgname);
			var str='<img src="${path}/'+	$("#imgshow"+id).attr("ref")+'/'+imgname+'" >';
			$("#imgshow"+id).html(str);
			$(".img").hide();
		}
	</script>
</body>


</html>