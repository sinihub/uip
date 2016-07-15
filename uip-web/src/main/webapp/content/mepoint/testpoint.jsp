<%--
  Created with IntelliJ IDEA.
  User: ywj
  Date: 2016/01/21
  Time: 15:33
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/content/include/taglib.jsp" %>
<%pageContext.setAttribute("currentHeader", "testpoint");%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>pointResult</title>

    <%@include file="/content/include/jslib.jsp" %>
    <%@include file="/content/include/csslib.jsp" %>

</head>
   <body>
    <!-- ___ __
             _{___{__}\
            {_}      `\)
           {_}        `            _.-''''--.._
           {_}                    //'.--.  \___`.
            { }__,_.--~~~-~~~-~~-::.---. `-.\  `.)
             `-.{_{_{_{_{_{_{_{_//  -- 8;=- `
                `-:,_.:,_:,_:,.`\\._ ..'=- ,
                    // // // //`-.`\`   .-'/
                   << << << <<    \ `--'  /----)
                    ^  ^  ^  ^     `-.....--'''
     -->
   <div id="wrapper">

     <%@include file="/content/layout/head.jsp" %>
     <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
            <h1 class="page-header">测点测试</h1>
        </div>
     </div>
       <!-- /.row -->
       <div class="row">
         <div class="col-lg-12">
           <div class="panel panel-default">
             <div class="panel-body">
               <div class="row">
                 <form id="f1" class="form-horizontal" role="form">
                   <div class="col-lg-6">
                     <div class="form-group">
                       <label for="connName" class="col-sm-3 control-label">测点编码</label>
                       <div class="input-group text col-sm-5">
                         <input id="pointInfo" placeholder="请输入测点编码..." name="pointInfo" class="form-control" type="text">
                       </div>
                     </div>

                    <div class="form-group">
                       <label for="connName" class="col-sm-3 control-label">系统编号</label>
                         <div class="input-group text col-sm-5">
                          <input id="sysId" placeholder="请输入系统编号..." name="id" class="form-control" type="text">
                         </div>
                     </div>


                     <div class="form-group">
                       <div class="col-sm-offset-3">
                         <input type="button" class="btn btn-success col-sm-3" id="but01" onclick="result()" value="提交"/>
                       </div>
                     </div>
                   </div>

                 <div class="col-lg-6">
                   <div class="form-group">
                     <label for="result01" class="col-sm-3 control-label">连接结果</label>
                        <div class="input-group text col-sm-5">
                           <input id="result01" name="result01" class="form-control" type="text" readonly="readonly">
                        </div>
                   </div>

                    <div class="form-group">
                       <label for="result02" class="col-sm-3 control-label">测点结果</label>
                       <div class="input-group text col-sm-5">
                         <input id="result02" name="result02" class="form-control" type="text" readonly="readonly">
                       </div>
                    </div>
                 </div>
                 </form>

                 <!-- /.col-lg-6 (nested) -->
               </div>
               <!-- /.row (nested) -->
             </div>
             <!-- /.panel-body -->
           </div>
           <!-- /.panel -->
         </div>
         <!-- /.col-lg-12 -->
       </div>

     </div>
   </div>

    <!-- 检测结果Modal -->
    			<div class="modal fade" style="margin-top: 200px;" id="pointResult" tabindex="-1" role="dialog" aria-labelledby="proModifyLabel" aria-hidden="true">
    			  <div class="modal-dialog">
    			    <div class="modal-content">
    			        <form class="form-horizontal">
    					      <div class="modal-header">
    					        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
    					        <h4 class="modal-title" id="myModalLabel">测试结果</h4>
    					      </div>
    					      <div class="modal-body">
    					      			<div class="form-group">
    						              <label for="pResult01" class="col-lg-2 control-label">信息</label>
    						              <div class="col-lg-10">
    						                <input type="text" class="form-control" name="pResult02" id="pResult02" readonly="readonly"/>
    						              </div>
    						            </div>
    						            <div class="form-group">
    						              <label for="pResult01" class="col-lg-2 control-label">结果</label>
    						              <div class="col-lg-10">
    						                <input type="text" class="form-control" name="pResult03" id="pResult03" readonly="readonly"/>
    						              </div>
    						            </div>
    					      </div>
    					      <div class="modal-footer">
    					        <input type="button"  class="btn btn-primary" data-dismiss="modal" size="20" value="取消">
    					      </div>
               			</form>
    			    </div>
    			  </div>
    			</div>
    		<!-- 检测结果Modal -->



   </body>
   <script type="text/javascript">
   $().ready(
   			 function(){
   				 $("#but01").click(result);
   			 }
   		    );
   		function result()
   		{
   		    var param=$("#f1").serialize();//序列化表单,将表单中的参数组装成参数格式
            	$.ajax(
            			{ //以post方式发出ajax请求
            				url:"${ctx}/testPoint/testPointResult.do",
            				type:"post",
            				data:param, //请求传递的参数
            				dataType:"json",//设置返回的数据类型
            				success:function(data){ //响应成功时的回调函数,data中包含Ajax请求返回的数据
            					$("#result01").val(data.info);
            					$("#result02").val(data.resul);
            				}
            			}
            		  );
   		}
   </script>
</html>