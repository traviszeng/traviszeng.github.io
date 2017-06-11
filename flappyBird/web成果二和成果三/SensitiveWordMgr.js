/**
 * 敏感词管理
 * author zeng.jian
 * date 2016-08-23
 */
$(document).ready(function() {
	
	using(['ztesidemenu','datagrid','dialog','messager'],function(){
		getSensitiveList(null);
		
		//默认点击第一个
		$(".titleList").find('.zte-title').eq(0).trigger('click');
	});
});

/**
 * 查找关键词
 * @author zeng.jian
 * @param keyword
 * 当参数为空时查找全部数据，参数不为空时进行模糊搜索
 */
function getSensitiveList(params) {
	$('#demoTable').datagrid({
		url : "SensitiveWordService.getSensitiveList",
		queryParams : params,
		pagination : true,
		collapsible : true,
		fitColumns : true,
		height : 'auto',
		nowrap : true,// 换行
		singleSelect : false,//为true时不能全选
		pageSize : 10,

		columns : [ [ 
		   {field:	'ck',	
		    title: "全选", 
		    checkbox:true
		 },
		 {
				field : 'id',
				title : 'ID',
				align : 'center',
				width:100,
			},
		   {
			field : 'words_content',
			title : '敏感词',
			align : 'center',
			width:100,
		}, {
			field : 'use_range',
			title : '适用范围',
			align : 'center',
			width:100,
		}, {
			field : 'update_time',
			title : '更新时间',
			align : 'center',
			width:100,
		}] ]

	});
	$("#demoTable").datagrid("reload");
}



/**
 * 根据敏感词模糊匹配
 * @author zeng.jian
 * @date 2016-08-24
 * 
 * */
function getSensitiveListLike(){
	var keyword=$("#sensitiveInfo").val();
	var params={};
	params.words_content=keyword;
	getSensitiveList(params);
}




/**
 * 添加敏感词
 * author zeng.jian
 * date 2016-08-23
 * 
 * */


function addSensitive(){
	$('#rootDiv').dialog({
	    title: '新增敏感词',
	    width: 800,
	    height: 350,
	    closed: false,
	    cache: false,
	    href: 'add_new_sensitive_pop.jsp',
	    modal: true,
	    buttons:[{
			text:'确定',
			cls:'zte-btn-sure',
			handler:function(){
				if(!$('#SensitiveInfo').form('validate')){
					return;
					
				}
				
				// 保存数据
				var params = $('#SensitiveInfo').form('getData');
				
				Ajax.getSy().remoteCall("SensitiveWordService", 'addSensitive', [ params ], function(data) {
					var result = data.getResult() || {};
					$.messager.alert(result.msg);
					if(result.logsign=="true"){
						$("#demoTable").datagrid("reload");
						$('#rootDiv').dialog('close');
					}
				}, function(msg, exc){
					$.messager.alert('系统异常','error');
				});
			}
		},{
			text:'取消',
			handler:function(){
				$('#rootDiv').dialog('close');
			}
		}]
	});
}

/**
 * 删除敏感词
 * @author zeng.jian
 * @date 2016-08-23
 * 
 * */
function deleteSensitive(){
	var rowData = $("#demoTable").datagrid('getSelected');
	if(!rowData||rowData.length==0||rowData.length>1){
		$.messager.alert("请选择一个分组！");
		return;
	}
	$('#rootDiv').dialog({
	    title: '提示信息',
	    width: 400,
	    height: 150,
	    closed: false,
	    cache: false,
	    href: 'pop_delete_sensitive.jsp',
	    modal: true,
	    
	    buttons:[{
			text:'确定',
			cls:'zte-btn-sure',
			handler:function(){
				// 保存数据
				var id = rowData.id;
				var words_content=rowData.words_content;
				var params = {};
				params.id = id;
				params.words_content=words_content;
	
				Ajax.getSy().remoteCall("SensitiveWordService", 'deleteSensitive', [ params ], function(data) {
					var result = data.getResult() || {};
					$.messager.alert(result.msg);
					if(result.logsign=="true"){
						$("#demoTable").datagrid("reload");
						$('#rootDiv').dialog('close');
					}
				}, function(msg, exc){
					$.messager.alert('系统异常','error');
				});
			}
		},{
			text:'取消',
			handler:function(){
				$('#rootDiv').dialog('close');
			}
		}]
	});
	
}


