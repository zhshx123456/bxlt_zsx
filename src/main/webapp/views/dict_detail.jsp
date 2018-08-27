<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="aimsApp">
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link type="image/x-icon"
          href="<%=request.getContextPath()%>/static/images/favicon.ico"
          rel="shortcut icon">
    <%@ include file="../layouts/css2.jsp" %>
    <%@ include file="../layouts/js2.jsp" %>
</head>
<body>
<div ng-controller="IndexController">

    <div style="position: relative; top: 20px;" ng-find="../dict/getDictInfo|dictInfo|id=${id }">
         <table class="table_block">
            <tr>
                <td>名称:</td>
                <td>
                    <input type="text" ng-model="dictInfo.name"/>
                </td>
            </tr>
            <tr>
                <td>代码:</td>
                <td>
                    <input type="text" ng-model="dictInfo.code" disabled="true"/>
                </td>
            </tr>
            <tr ng-show="${bizType}==1&&${type}==1">
                <td>单位保额（元）:</td>
                <td>
                    <input type="text" ng-model="dictInfo.amountOfInsuranceUnit" />
                </td>
            </tr>
            <tr ng-show="${bizType}==1&&${type}==1">
                <td>费率（%）:</td>
                <td>
                    <input type="text" ng-model="dictInfo.rate" />
                </td>
            </tr>
            <tr ng-show="${bizType}==1&&${type}==1">
                <td>农户自缴比例（%）:</td>
                <td>
                    <input type="text" ng-model="dictInfo.farmerRate" />
                </td>
            </tr>
             <tr>
                  <td></td>
                 <td> <button class="baocun right" type="button" ng-click="saveEditDict(dictInfo);">保存</button></td>
            </tr>
        </table>
    </div>
    <div class="clear"></div>
    <div class="kon"></div>
</div>
</body>
</html>






















