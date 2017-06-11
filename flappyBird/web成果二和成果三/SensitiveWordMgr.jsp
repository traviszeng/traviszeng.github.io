<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>敏感词列表</title>

<!--字符编码-->
<meta charset="utf-8" />
<!--使用最高版本的IE内核进行渲染-->
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="library" content="kernel,zteUI,dialog">
<!--自动适应手机屏幕宽度-->
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<!--jquery1.9.1-->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/public/jquery/jquery-1.9.1.min.js"></script>
<!--首页-->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/index.css" afterparser />
<!--列表-->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/list.css" afterparser />
<!--扩展-->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/patch.css" afterparser />
<!--框架-->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/public/common/Common.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/public/jquery/easyui/easyloader.js"></script>
<!-- 菜单组件类 -->
<script 
	src="${pageContext.request.contextPath}/page/msg/js/SensitiveWordMgr.js"></script>
<!--交互JS-->
<script 
	src="${pageContext.request.contextPath}/zteUI/base/js/interactive.js"></script>
</head>	
<body>

	<div class="project-content">
		<div class="class-menu">
			<h1>敏感词分类</h1>
			<div id="sidemenu" style="border:0;"></div>
		</div>
		<div class="push-info" style="margin-left: 275px;">
			<div class="zte-search-condition">
				<div class="zte-title-list zte-clear">
					<ul class="titleList zte-clear">
						<li class="zte-title cur">敏感词列表</li>
					</ul>
					<div class="zte-search-wrap">
						<div class="zte-search-senior searchSenior">
							<span>高级筛选</span> <i></i>
						</div>
						<i class="zte-iconfont icon-search-b fs22" onclick="getSensitiveListLike()" ></i> <input type="text"
							 id="sensitiveInfo" class="zte-form-control" placeholder="在结果中查询">
					</div>
				</div>
				<div class="zte-con-wrap conWrap" style="display: none;">
					<table class="zte-table zte-table-noLR zte-table-edit">
						</table>
				</div>
			</div>
			
			<div class="add-tools-manipulate">
				<div class="manipulate">
					<a class="zte-iconfont icon-add-a" onclick="addSensitive()">新增</a> 
					<span class="line"></span> 
					
					<a class="zte-iconfont icon-del-a" onclick="deleteSensitive()">删除</a>
				</div>
				<div class="large-table">
					<div id="demoTable"></div>
				</div>
			  </div>
		  </div>
		</div>
		<!-- project-content-end -->
	
	<!-- main-end -->
	<div id ='rootDiv'></div>

</body>
</html>
