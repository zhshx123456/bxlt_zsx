

<div ng-query="dict/getDictList|dictlist|dictReqeust" ng-init="dictReqeust={};dictReqeust.id='${id}';dictReqeust.bizType='${bizType}';dictReqeust.type=1;dictReqeust.isCopy=false">
	<input type="hidden"  ng-model="dictReqeust.isCopy" id="isCopy" value="${isCopy}"/>
	<input class="dosearch" type="hidden">
     <div class="tabbable" style="padding-left: 15px">
            <ul class="tab-box">
                <li class="active"><span data-toggle="tab" ng-click="dictReqeust.type=1;tabClick();">险种</span>
                </li>
                <li><span data-toggle="tab" ng-click="dictReqeust.type=2;tabClick();">影像资料</span></li>
            </ul>
    </div>
    <div class="clear"></div>
    	<div ng-show="${isCopy}">
    	<div ng-show="isShowCopyButton(dictReqeust)" ng-click="copyPlyDic(dictReqeust)" style="width: 150px;float: right;margin-right: 0;margin-left: 15px" class=" search-btn  list_refresh">复制承保${dicName}险种</div>
        </div>
        <div ng-show="${isAdmin}" ng-click="createDict(dictReqeust)" style="float: right;margin-right: 0" class=" search-btn  list_refresh">添&nbsp;加</div>

<div class="clear"></div>
<div class="list-table ">
	<%--<table class="table-striped table-hover l-table datatable"--%>
		<%--style="width: 100%; table-layout: fixed">--%>
		<%--<thead>--%>
			<%--<tr>--%>
				<%--<th class="substr" title="名称">名称</th>--%>
				<%--<th class="substr" title="代码">代码</th>--%>
				<%--<th ng-show="${isAdmin}" class="substr" style="width: 90px"--%>
					<%--title="操作">操作</th>--%>
			<%--</tr>--%>
		<%--</thead>--%>
		<%--<tbody>--%>
			<%--<tr ng-repeat="m in dictlist.content">--%>
				<%--<td class="substr" title="{{m.name}}">{{m.name}}</td>--%>
				<%--<td class="substr" title="{{m.code}}">{{m.code}}</td>--%>
				<%--<td ng-show="${isAdmin}"><a ng-show="${isAdmin} && m.code!='E0001'" href="###" ng-click="editDict(m.id)">编辑</a>--%>
					<%--<a ng-show="${isAdmin} && m.code!='E0001'"  href="###" ng-click="deleteDict(m.id)">删除</a></td>--%>
			<%--</tr>--%>
		<%--</tbody>--%>
	<%--</table>--%>
	<div class="table-box">
		<div class="sider-left" style="width: 100%">
			<div class="table-cont">
				<table style="border-collapse:collapse;">
					<tr>
						<th class="substr" title="名称">名称</th>
						<th class="substr" title="代码">代码</th>
						<th ng-show="dictReqeust.type==1 && ${bizType}==1" class="substr" title="单位保额">单位保额（元）</th>
						<th ng-show="dictReqeust.type==1 && ${bizType}==1" class="substr" title="费率">费率（%）</th>
						<th ng-show="dictReqeust.type==1 && ${bizType}==1" class="substr" title="农户自缴比例">农户自缴比例（%）</th>
						<th ng-show="${isAdmin}" class="substr" style="width: 90px"
							title="操作">操作</th>
					</tr>
					<tbody id="table-cont">
					<tr ng-repeat="m in dictlist.content">
						<td class="substr" title="{{m.name}}">{{m.name}}</td>
						<td class="substr" title="{{m.code}}">{{m.code}}</td>
						<td ng-show="dictReqeust.type==1 && ${bizType}==1" class="substr" title="{{m.amountOfInsuranceUnit}}">{{m.amountOfInsuranceUnit}}</td>
						<td ng-show="dictReqeust.type==1 && ${bizType}==1"  class="substr" title="{{m.rate}}">{{m.rate}}</td>
						<td ng-show="dictReqeust.type==1 && ${bizType}==1"  class="substr" title="{{m.farmerRate}}">{{m.farmerRate}}</td>
						<td ng-show="${isAdmin}"><a ng-show="${isAdmin} && m.code!='E0001'" href="###" ng-click="editDict(m.id,${bizType},dictReqeust.type)" style="color:#24bb66">编辑</a>
							<a ng-show="${isAdmin} && m.code!='E0001'"  href="###" ng-click="deleteDict(m.id)"style="color:#24bb66">删除</a></td>
					</tr>
					</tbody>
				</table>
			</div>
		</div>

	</div>
	<div class="bxltpage"></div>
</div>
 </div>