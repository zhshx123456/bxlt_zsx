
<script type="text/javascript">
$(function(){
	loadDictTree();

})
</script>

<div>
    <div class="ContentLayoutLeft" style="top:10px !important" ng-init="currentView=''">
        <table class="table table-bordered table-striped" >
            <tr>
                <td ><ul id="dicttree" class="easyui-tree" style="padding-left: 20px"></ul></td>
            </tr>
           
        </table>
    </div>
    
    <div class="ContentLayoutRight">
    <div  ng-include="currentView"></div>
    </div>
    
</div>
