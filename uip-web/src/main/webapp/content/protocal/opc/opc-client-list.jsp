<%--
  Created with IntelliJ IDEA.
  User: shuiqing
  Date: 2014/12/26
  Time: 23:50
  Email: shuiqing301@gmail.com
  GitHub: https://github.com/ShuiQing301
  Blog: http://shuiqing301.github.io/
   _
  |_)._ _
  | o| (_
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/content/include/taglib.jsp" %>
<html>

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="">
  <meta name="author" content="">

  <%@include file="/content/include/jslib.jsp" %>
  <%@include file="/content/include/csslib.jsp" %>


</head>

<body>

    <div id="wrapper">

      <%@include file="/content/layout/head.jsp" %>
      <div id="page-wrapper">
        <div class="row">
          <div class="col-lg-12">
            <h1 class="page-header">Opc Connection List!</h1>
          </div>
        </div>
        <!-- /.row -->
        <div class="row">
          <div class="col-lg-12">
            <div class="panel panel-default">
              <!-- /.panel-heading -->
              <div class="panel-body">
                <div class="dataTables_wrapper">
                  <table class="table table-striped table-bordered table-hover" id="opcconnect-infoGrid">
                    <thead>
                    <tr class="success">
                      <th>序号</th>
                      <th>连接名称</th>
                      <th>目标IP</th>
                      <th>用户名</th>
                      <th>登录密码</th>
                      <th>CLSID</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${requestScope.opcServerInfomationList}" var="item"  varStatus="status">
                      <tr>
                        <td>${status.count}</td>
                        <td>${item.domain}</td>
                        <td>${item.host}</td>
                        <td>${item.user}</td>
                        <td>${item.password}</td>
                        <td>${item.clsid}</td>
                      </tr>
                    </c:forEach>
                    </tbody>
                  </table>
                </div>
                <!-- /.table-responsive -->
              </div>
              <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
          </div>
          <!-- /.col-lg-12 -->
        </div>
      </div>
    </div>
</body>

</html>
