<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
	<title>统一推送平台</title>
	<!--字符编码-->
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="library" content="kernel,zteUI,dialog">
	<!--使用最高版本的IE内核进行渲染-->
	<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
	<!--自动适应手机屏幕宽度-->
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/common/Common.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/jquery/jquery.jcryption-1.1.js"></script>
	<!--登录组件类-->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/login.css" />
	<script src="<%=request.getContextPath()%>/page/login/js/login.js"></script>
</head>

<body class="themes">
	<div class="login">
		<div class="header">
			<img src="<%=request.getContextPath()%>/images/logintit.png"> <span
				class="fs22 color-fff">统一推送平台</span>
		</div>
		<div class="login-form">
			<div class="login-box" id="login-box">
				<div class="title">用户登录</div>
				<div class='login-input'>
					<div id="msg-error" class="msg-error" style="display: block;"></div>
					<div class="item">
						<i class="iconfont">&#xe60c;</i> 
						<input type="text" id="staff_code" name="staff_code" value="" placeholder="用户名" class="form-control" />
					</div>
					<div class="item">
						<i class="iconfont">&#xe60d;</i> 
						<input type="password" id="password" name="password" value="" placeholder="密码" class="form-control" />
					</div>
					<div class="code">
						<input type="text" id="verCode" name="verCode" value="" placeholder="验证码" class="form-control" /> 
						<img src="<%=request.getContextPath()%>/servlet/RandomImageServlet" id="safecode" onclick="reloadVerifyCode();" alt="点击刷新" />
					</div>
					<div class="submit">
						<button id="login_btn" type="button" class="btn btn-submit">登录</button>
					</div>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<div class="footer"></div>

</body>

</html>