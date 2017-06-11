/**
 * 消息管理
 * @author zeng.jian	
 * @date 2016-08-18
 */
	$(document).ready(function() {
	
	using(['ztesidemenu','datagrid','dialog','messager'],function(){
		getMsgList(null);
		//getMsgClasses();
		//默认点击第一个
		$(".titleList").find('.zte-title').eq(0).trigger('click');
	});
});
	/**
	 * 查找消息
	 * @author zeng.jian
	 * @param keyword
	 * 当参数为空时查找全部数据，参数不为空时进行模糊搜索
	 */
	function getMsgList(params) {
		$('#demoTable').datagrid({
			url : "MsgService.getMsgList",
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
				title : '任务ID',
				align : 'center',
				width:100,
			}, {
				field : 'cerate_staff_name',
				title : '提交人',
				align : 'center',
				width:100,
			}, {
				field : 'publish_time',
				title : '提交时间',
				align : 'center',
				width:100,
			}, {
				field : 'publish_number',
				title : '发送数量',
				align : 'center',
				width:100,
				
			}, {
				field : 'msg_summary',
				title : '详情',
				align : 'center',
				width:100,
			} ] ]

		});
		$("#demoTable").datagrid("reload");
	}
	
	
	/**
	 * 根据输入的关键词搜索消息summary
	 * @author zeng.jian
	 * @date 2016-08-24
	 */
	function getMsgListLike() {
		var keyword = $("#msgInfo").val();
		var params={};
		params.msg_summary=keyword;
		getMsgList(params);

	}
	/**
	 * 查找消息类型
	 * @author zeng.jian
	 * @date 2016-08-19
	 * */
	
	function getMsgClasses() {

		var msgClasses = null;
		Ajax.getSy().remoteCall("MsgService", 'getMsgClasses', [],
				function(data) {
					console.log("data:" + data);
					$('#sidemenu').ztesidemenu({
						multiple : false,
						onClick : function(data, sidemenu) {

						},
						onBeforeAdd : function(params) {

						},
						onAdd : function(parent, text) {

						},
						onBeforeEdit : function(target) {

						},
						onEdit : function(target, text) {

						},
						onBeforeDelete : function(target) {

						},
						onDelete : function(target) {

						},
						data :data.data//要加.data,不然无法解析
					});
				}, function(msg, exc) {
					$.messager.alert('系统异常', 'error');
				});

		$('#sidemenu').ztesidemenu("reload");
	}