$(function() {
	LoginUtil.init();
});
var LoginUtil = {
	params : {},
	init : function() {
		this.checkFilter();
		this.getPubKeys();
		this.clickLogin();
	},
	// 判断有无参数sign，有则显示拦截
	checkFilter: function(){
		var sign = getUrlParam("sign")==null?"":getUrlParam("sign");
		if(sign == "0"){
			using(['messager'], function(){
				$.messager.alert('登录超时，请重新登录');
			});
		}
	},
	// 获取公钥
	getPubKeys: function() { 
		var me = this;
	    $.jCryption.getKeys(js_path_prefix+'servlet/PubKeyServlet.do',function(receivedKeys) {  
	        me.params.keys = receivedKeys;  
	    });      
	},
	// 点击登录
	clickLogin : function() {
		var me = this;
		$("#login_btn").unbind("click").click(function() {
			$("#msg-error").html("");
			// 获取表单值
			var requestParams = {};
			$("#login-box").find("input").each(function(index, ele) {
				var name = $(this).attr("name");
				var value = $(this).val();
				requestParams[name] = value;
			});
			if(requestParams.staff_code == ""){
				$("#msg-error").html("用户名输入不能为空");
				return;
			}
			if(requestParams.password == ""){
				$("#msg-error").html("密码输入不能为空");
				return;
			}
			if(requestParams.verCode == ""){
				$("#msg-error").html("验证码输入不能为空");
				return;
			}
			//密码加密，然后登录
			$.jCryption.encrypt(requestParams.password, me.params.keys, function(encryptedPasswd) {   
				var newPassword = encryptedPasswd;
				requestParams.password = newPassword;
				Ajax.getSy().remoteCall("LoginService", 'loginPost', [ requestParams ], function(data) {
					var result = data.getResult() || {};
					if(result.logsign == "true"){
						$("#msg-error").html("");
						window.location.href = js_path_prefix + "page/main/index.jsp"
					}else{
						$("#msg-error").html(result.msg);
						reloadVerifyCode();
					}
				}, function(msg, exc){
					$("#msg-error").html("登录失败：系统异常");
				});
			});
		});
	}
}
// 加载验证码
function reloadVerifyCode(){
	var d=Math.random()*(9999-1000)+1000;
	var verifyCorrectCode=parseInt(d);
	document.getElementById("safecode").src= js_path_prefix+ "servlet/RandomImageServlet?verifyCorrectCode="+d;
}
//获取url中的参数
function getUrlParam(name){
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}