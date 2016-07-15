<%--
  Created with IntelliJ IDEA.
  User: ywj
  Date: 2016/02/01
  Time: 10:23
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/content/include/taglib.jsp" %>
<%pageContext.setAttribute("currentHeader", "opcServerInfomation");%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>opcServer</title>

    <%@include file="/content/include/jslib.jsp" %>
    <%@include file="/content/include/csslib.jsp" %>

</head>
   <body>
     <div id="wrapper">

         <%@include file="/content/layout/head.jsp" %>
             <div id="page-wrapper">
                 <div class="row">
                     <div class="col-lg-12">
                         <h1 class="page-header">opc服务信息</h1>
                     </div>
                     <!-- /.col-lg-12 -->
                 </div>
                 <!-- /.row -->

                 <div class="row">
                     <div class="col-lg-12">
                         <div class="panel panel-default" >
                             <div class="panel-body">
                             <form id="pageform" action="${ctx}/testPoint/getSysIdAndReveiveOpc.do" method="post">
                                 <div class="table-responsive">
                                 <table width="1000px">
                                     <tr>
                                         <td align="right"><font style="font-weight: bold" size=4.5>条件:</font></td>
                                         <td align="center"><input class="form-control" style="width:300px"type="text" name="submitopc" class="input-small search-query"
                                                                         				placeholder="请依次输入host,user,password：逗号隔开"value="${requestScope.opcId}" >
                                         <td align="left"><input type="submit"  class="btn btn-warning" id="submitopc" value="查询"/>
                                         </td>
                                         <td width="500px"align="right"><a href="${ctx}/testPoint/exportOpcPoint.do"  class="btn btn-danger">导出叶子节点</a></td>
                                     </tr>
                                 </table>
                                 <div class="form-group" align="left">
                                      <div align="left"  class="input-group text col-sm-2">
                                      </div>
                                 </div>
                                 <table class="table table-condensed table-bordered table-hover" id="mepoint-list" >
                                         <thead>
                                             <tr class="success">
                                                 <th style="width:220px">clsId</th>
                                                 <th style="width:220px">progId</th>
                                                 <th style="width:240px">description</th>
                                             </tr>
                                         </thead>
                                         <tbody>
                                         <c:if test="${requestScope.opcException eq 0}">
                                         <tr>
                                         <td colspan="3" align="center">
                                             <span id="selectresult" style="color:red">没有查询到相关信息!</span>
                                         </td>
                                         </tr>
                                         </c:if>
                                          <c:forEach items="${requestScope.opc}" var="item">
                                                 <tr>
                                                     <td style="width:220px">${item.clsId}</td>
                                                     <td style="width:220px">${item.progId}</td>
                                                     <td style="width:240px">${item.description}</td>
                                                 </tr>
                                             </c:forEach>
                                         </tbody>
                                     </table>
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
   </body>

</html>