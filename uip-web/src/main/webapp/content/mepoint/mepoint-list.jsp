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

    <title>mtime</title>

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

<div id="wrapper" >
    <%@include file="/content/layout/head.jsp" %>
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">测点管理</h1>
                </div>
            </div>
            <!-- /.row -->

            <div class="row" >
                <div class="col-lg-12">
                    <div class="panel panel-default" >
                        <div class="panel-body">
                         <form id="pageform" action="${ctx}/testPoint/upFileAndAnalysis.do" enctype="multipart/form-data" method="post">
                             <div class="table-responsive">
                             <table width="100%" >
                                <tr>
                                    <td>
                                <table width="60%">
                                  <tr>
                                    <td align="center">
                                        <input type="button"  class="btn btn-primary"  id="addBut" onclick="addPoint()" value="新增"/>
                                    </td>
                                    <td align="right"><font style="font-weight: bold" size=4.5>条件:</font></td>
                                    <td align="center"><input class="form-control" style="width:250px"type="text" value="${requestScope.sourceName}"name="sourceName" class="input-small search-query"
                                                                    				placeholder="请输入目标编码或者测点名："><input type="hidden" id="source" name="source" value="${requestScope.sourceName}">
                                    </td>
                                    <td align="left"><input type="button"  class="btn btn-warning"  id="sell01" value="查询"/>
                                    </td>
                                  </tr>
                                </table>

                                <td style="width:4%" align="right">
                                   <div class="dropdown">
                                       <button type="button" class="btn dropdown-toggle" id="dropdownMenu1"
                                           data-toggle="dropdown">
                                           excel
                                          <span class="caret"></span>
                                         </button>
                                         <ul class="dropdown-menu pull-right" role="menu" aria-labelledby="dropdownMenu1">
                                            <li role="presentation">
                                               <a role="menuitem" tabindex="-1" href="javascript:;" onclick="upfilemethod()">导入</a>
                                             </li>
                                            <li role="presentation">
                                               <a role="menuitem" tabindex="-1" href="${ctx}/testPoint/exportToExcel.do">导出</a>
                                            </li>
                                            <li role="presentation" class="divider"></li>
                                             <li role="presentation">
                                                <a role="menuitem" tabindex="-1" href="#">
                                                <div style="background-repeat:no-repeat;width: 120px;height:25;padding-bottom:5px;">
                                                <label>
                                                      选择文件
                                                      <input type="file" name="upfile"style="opacity:0; filter:alpha(opacity=0);width: 120px;height:25;"/>
                                                </label>
                                                </div>
                                                </a>
                                             </li>
                                         </ul>
                                      </div>
                                    </td>
                                </tr>
                            </table>

                            <div class="form-group" align="left">
                                 <div align="left"  class="input-group text col-sm-2">
                                 </div>
                            </div>
                            <table class="table table-condensed table-bordered table-hover" id="mepoint-list" >
                                    <thead>
                                        <tr class="success">
                                            <th style="width:50px">编号</th>
                                            <th style="width:60px">操作</th>
                                            <th style="width:240px">接口编码</th>
                                            <th style="width:240px">目标编码</th>
                                            <th style="width:220px">测点</th>
                                            <th style="width:60px">值</th>
                                            <th style="width:60px">编号</th>
                                        </tr>
                                    </thead>
                                    <tbody id="dataajax">
                                        <c:forEach items="${requestScope.mpList}" var="item"  varStatus="status">
                                            <tr ondblclick="modifyy(${item.id},'${item.sourceCode}','${item.targetCode}','${item.pointName}',${item.sysId})"
                                                						onmouseover="this.style.cursor='pointer';this.style.background='#0072E3'"
                                                						onmouseout="this.style.cursor='default';this.style.backgroundColor='#ffffff'">
                                                <td style="width:50px">${status.count}</td>
                                                <td style="width:60px">
                                                    <input type="button" onclick="deleteThisData(${item.id})" class="btn btn-danger btn-xs" value="删除"/>
                                                </td>
                                                <td style="width:240px">${item.sourceCode}</td>
                                                <td style="width:240px">${item.targetCode}</td>
                                                <td style="width:220px">${item.pointName}</td>
                                                <c:forEach items="${requestScope.reList}" var="r">
                                                <c:if test="${item.id eq r.id}">
                                                    <td style="width:60px">${r.values}</td>
                                                </c:if>
                                                </c:forEach>
                                                <td style="width:60px">${item.sysId}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                    <div id="eeee" align="center" ><img src="${ctx}/static/bootstrap/js/e.gif" /></div>
                              </div>

                    <div class="form-group" align="center">
                        <input type="hidden" id="flatPage" name="page" value="${requestScope.flatPage}"/>
                        <ul class="pagination pagination-lg" id="reBuildPageNo">
                             <li><a href="#"id="firstye" >首页</a></li>
                        <c:if test="${requestScope.flatPage gt 1}">
                             <li id="ctrlfirstpage"><input type="hidden" id="beforePage01" value="${requestScope.flatPage}">
                                <a href="#" id="beforeye">上一页</a></li>
                        </c:if>
                        <c:if test="${requestScope.flatPage eq 1}">
                             <li class="previous disabled"><a>上一页</a></li>
                        </c:if>
                         <%-- <li class="previous disabled"><a>选择页数:</a></li>

                             <li><a><select id="selectIndex" onchange="toAnyPage(this.options[this.selectedIndex].value,'${requestScope.sourceName}')">
                        <c:forEach begin="1" end="${requestScope.lastPage}" step="1" var="p">
                            <option value="${p}" <c:if test="${p == requestScope.flatPage}">selected</c:if>>
                                ${p}
                            </option>
                        </c:forEach>
                        </select></a></li>--%>
                            <li class="previous disabled"><a id="count">目前共:${requestScope.countList}条数据-计${requestScope.lastPage}页</a></li>
                        <c:if test="${requestScope.flatPage lt requestScope.lastPage}">
                            <li id="ctrllastpage"><input type="hidden" id="nextPage01" value="${requestScope.flatPage}"><a href="#" id="nextye">下一页</a></li>
                        </c:if>
                        <c:if test="${requestScope.flatPage eq requestScope.lastPage}">
                            <li class="previous disabled"><a>下一页</a></li>
                        </c:if>
                            <li><a href="#"id="lastye"><input type="hidden" id="lastyeValue" value="${requestScope.lastPage}">尾页</a></li>
                         </ul>
                    </div>
                        </form>
                    </div>
                        <!-- /.panel-body -->
                </div>
                    <!-- /.panel -->
            </div>
                <!-- /.col-lg-12 -->
        </div>
</div>


<!--  修改和添加Modal -->
    <div class="modal fade" style="margin-top: 200px;" id="pointResult" tabindex="-1" role="dialog" aria-labelledby="proModifyLabel" aria-hidden="true">
        <div class="modal-dialog">

	<div class="panel panel-primary">
	    <div class="panel-heading">
	        <h3 class="panel-title">测点修改或添加</h3>
	    </div>
	<div class="panel-body">

	<form id="f1" class="form-horizontal"  method="post" action="${ctx}/testPoint/modifyPointWell.do">
	  	<input type="hidden" id="id" name="id" value="${onePoint.id }"/>
	  		<div class="form-group">
                <label for="companyId" class="col-lg-2 control-label">接口编码</label>
                <div class="col-lg-10">
                    <input type="text" class="form-control" id="sourceCode"  name="sourceCode" value="${onePoint.sourceCode }"/>
                </div>
            </div>

        <div class="form-group">
            <label for="companyName" class="col-lg-2 control-label">目标编码</label>
            <div class="col-lg-10">
               <input type="text" class="form-control" id="targetCode" name="targetCode" value="${onePoint.targetCode}">
            </div>
        </div>

        <div class="form-group">
            <label for="companyName" class="col-lg-2 control-label">测点名称</label>
            <div class="col-lg-10">
                <input type="text" class="form-control" id="pointName" name="pointName" value="${onePoint.pointName}">
            </div>
        </div>

        <div class="form-group">
            <label for="companyName" class="col-lg-2 control-label">系统编号</label>
            <div class="col-lg-10">
                <input type="text" class="form-control" id="sysId" name="sysId" value="${onePoint.sysId}">
                <span id="res" style="color:red"></span>
            </div>
        </div>


        <div class="form-group">
            <div class="col-lg-offset-2 col-lg-10">
                <input type="button" id="but01" class="btn btn-lg btn-info" style="padding: 5px 38px;border-radius: 0px;"value="保存"/>
                <input type="button" data-dismiss="modal" id="but02" onclick="removeAllInf()" class="btn btn-lg btn-info" style="padding: 5px 38px;border-radius: 0px;"value="返回"/>
            </div>
        </div>
    </form>
      </div>
    </div>
 <!-- 修改和添加Modal -->

</body>
<script>
    function upfilemethod(){
        document.getElementById("pageform").submit();
    }
    function removeAllInf(){
        $("#id").val(null);
        $("#sourceCode").val(null);
        $("#targetCode").val(null);
        $("#pointName").val(null);
        $("#sysId").val(null);
        $("#res").html(null);
    }
    function modifyy(id,sourceCode,targetCode,pointName,sysId) {
        $("#id").val(id);
        $("#sourceCode").val(sourceCode);
        $("#targetCode").val(targetCode);
        $("#pointName").val(pointName);
        $("#sysId").val(sysId);
        $('#pointResult').modal();
    }
     function addPoint() {
        removeAllInf();
        $('#pointResult').modal();
     }
     function deleteThisData(id){
        var se=confirm("您确定删除该数据吗？");
        if (se==true){
           location.replace("${ctx}/testPoint/deletePoint.do?second="+id);
        }
     }
    $().ready(
       		function(){
       		    $("#but01").click(result);
       			$("#sell01").click(selectData);
                $("#eeee").hide();
                vartyupfile();
       		}
       		);
       		function selectData(){
       		    toFirstPage();
       		}
            function vartyupfile(){
                if($("#vartyupfile").value == 1){
                alert("success")
            }
            if($("#vartyupfile").value == 0){
                alert("fail")
            }
            }
       		function result()
       		{
       		    var param=$("#f1").serialize();//序列化表单,将表单中的参数组装成参数格式
                $.ajax(
                	{ //以post方式发出ajax请求
                		url:"${ctx}/testPoint/checkModifyAndAddIsNotWell.do",
                		type:"post",
                		data:param, //请求传递的参数
                		dataType:"json",//设置返回的数据类型
                		success:function(data){ //响应成功时的回调函数,data中包含Ajax请求返回的数据
                			if(data.resul!= "fail"){
                                $("#f1").submit();
                			}else{
                				$("#res").html("*  测点通讯不成功，无法修改 *");
                			}
                		}
                	}
                	  );
       		}
</script>
<script>
    $().ready(
   	     function(){
   	        setInterval("tes()",5000);
   	        $("#firstye").click(toFirstPage);
   	        $("#beforeye").click(toBeforePage);
   		    $("#nextye").click(toNextPage);
   		    $("#lastye").click(toLastPage);
   	     }
   		 );
        function tes(){
            toPage();
        }
   		 function changeQuery(page) {
         	$("#flatPage").val(page);
         	toPage();
         }
   		 function toFirstPage(){
            document.getElementById("flatPage").value = 1;
            toPage();
         }
         function toAnyPage(page,source){
            document.getElementById("flatPage").value = page;
            toPage();
         }
         function toBeforePage(){
            document.getElementById("flatPage").value = (Number($("#beforePage01").val())-1);
            toPage();
         }
   		 function toNextPage(){
   		    document.getElementById("flatPage").value = (Number($("#nextPage01").val())+1);
            toPage();
   		 }
   		 function toLastPage(){
            document.getElementById("flatPage").value = $("#lastyeValue").val();
            toPage();
         }
 		function toPage()
   		{
   		    $("#dataajax").empty();
   		    $('#eeee').show();//按键后先显示图片
   		    var param=$("#pageform").serialize();//序列化表单,将表单中的参数组装成参数格式
            	$.ajax(
            		{   //以post方式发出ajax请求
            			url:"${ctx}/testPoint/AnyMepoint-list.do",
            			type:"post",
            			data:param, //请求传递的参数
            			dataType:"json",//设置返回的json数据类型
            			success:showdata
            		}
            		  );
   		}
    	function showdata(datas){
   	        $("#eeee").hide();//隐藏等待图片
            $("#dataajax").empty();
            var list=datas.items;
            //构造表格显示内容的界面
            for(var i=0;i<list.length;i++){
                var tr1=$("<tr>").attr("ondblclick","modifyy("+list[i].id+",'"+list[i].sourceCode+"','"+list[i].targetCode+"','"+list[i].pointName+"',"+list[i].sysId+")").attr("onmouseover","this.style.cursor='pointer';this.style.background='#0072E3'").attr("onmouseout","this.style.cursor='default';this.style.backgroundColor='#ffffff'");
            	$("#dataajax").append(tr1);
                var td0=$("<td>").attr("style","width:50px").text(i+1);
                var tdd0=$("</td>");
                td0.append(tdd0);
                var td1=$("<td>").attr("style","width:60px");
                var input1=$("<input>").attr("type","button").attr("onclick","deleteThisData("+list[i].id+")").attr("class","btn btn-danger btn-xs").attr("value","删除");
                var tdd1=$("</td>");
                td1.append(input1).append(tdd1);
                var td2=$("<td>").attr("style","width:240px").text(list[i].sourceCode);
                var tdd2=$("</td>");
                td2.append(tdd2);
                var td3=$("<td>").attr("style","width:240px").text(list[i].targetCode);
                var tdd3=$("</td>");
                td3.append(tdd3);
                var td4=$("<td>").attr("style","width:260px").text(list[i].pointName);
                var tdd4=$("</td>");
                td4.append(tdd4);
                var td6=$("<td>").attr("style","width:60px").text(list[i].values);
                var tdd6=$("</td>");
                td6.append(tdd6);
                var td5=$("<td>").attr("style","width:50px").text(list[i].sysId);
                var tdd5=$("</td>");
                td5.append(tdd5);
                var trr1=$("</tr>");
                tr1.append(td0).append(td1).append(td2).append(td3).append(td4).append(td6).append(td5).append(trr1);
            }
            //构造分页插件
            $("#reBuildPageNo").empty().attr("class","pagination pagination-lg");
            var li1=$("<li>");
            var a1=$("<a>").attr("href","javascript:changeQuery("+1+")").attr("id","firstye").text("首页");
            var aa1=$("</a>");
            var lli1=$("</li>");
            li1.append(a1).append(aa1).append(lli1);
            //上一页
            var li2=$("<li>");
            var input11=$("<input>").attr("type","hidden").attr("id","beforePage01").attr("value",datas.flatPage);
            var a2=$("<a>").text("上一页");
            var aa2=$("</a>");
            var lli2=$("</li>");
            if(datas.flatPage==1){
                 li2.attr("class","previous disabled");
            }else{
                 a2.attr("href","javascript:changeQuery("+datas.beforePage+")").attr("id","beforeye");
             }
            li2.append(input11).append(a2).append(aa2).append(lli2);
            //跳转页数
            var li3=$("<li>").attr("class","previous disabled");
            var a3=$("<a>").text("选择页数:");
            var aa3=$("</a>");
            var lli3=$("</li>");
            li3.append(a3).append(aa3).append(lli3);
            var li8=$("<li>");
            var a8=$("<a>");
            var s8=$("<select>").attr("id","selectIndex").attr("onchange","changeQuery(this.options[this.selectedIndex].value)");
            for(var j=1;j<=datas.lastPage;j++){
                var opt=$("<option>").attr("value",j).text(j);
                var optt=$("</option>")
                if(j==(datas.flatPage)){
        	        opt.attr("selected",true);
                }
                s8.append(opt).append(optt);
            }
            var ss8=$("</select>");
            var aa8=$("</a>");
            var lli8=$("</li>");
            a8.append(s8).append(ss8).append(aa8);
            li8.append(a8).append(lli8);
            //统计
            var li9=$("<li>").attr("class","previous disabled");
            var a9=$("<a>").text("目前共"+datas.countList+"条数据-计"+datas.lastPage+"页");
            var aa9=$("</a>");
            var lli9=$("</li>");
            li9.append(a9).append(aa9).append(lli9);
            //下一页
            var li4=$("<li>");
            var input14=$("<input>").attr("type","hidden").attr("id","nextPage01").attr("value",datas.flatPage);
            var a4=$("<a>").text("下一页");
            var aa4=$("</a>");
            var lli4=$("</li>");
            if(datas.flatPage==datas.lastPage){
                 li4.attr("class","previous disabled");
            }else{
                 a4.attr("href","javascript:changeQuery("+datas.nextPage+")").attr("id","nextye");
            }
            li4.append(input14).append(a4).append(aa4).append(lli4);
            //尾页
            var li5=$("<li>");
            var a5=$("<a>").attr("href","javascript:changeQuery("+datas.lastPage+")").attr("id","lastye").text("尾页");
            var input15=$("<input>").attr("type","hidden").attr("id","lastyeValue").attr("value",datas.lastPage);
            var aa5=$("</a>");
            var lli5=$("</li>");
            li5.append(a5).append(input15).append(aa5).append(lli5);
            $("#reBuildPageNo").append(li1).append(li2).append(li9).append(li4).append(li5);
   	    }
</script>
</html>