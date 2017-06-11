<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>消息分类列表</title>

<!--字符编码-->
<meta charset="utf-8" />
<!--使用最高版本的IE内核进行渲染-->
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="library" content="kernel,zteUI,dialog">
<!--自动适应手机屏幕宽度-->
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<!-- 引入zteselect -->
<meta http-equiv="zteui" content="zteselect"/>
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
	src="${pageContext.request.contextPath}/page/msg/js/msgEdit.js"></script>
	<!-- ueditor的配置和引入js -->
<script 
	type="text/javascript" src="ueditor/ueditor.config.js"></script>
<script 
	type="text/javascript" src="ueditor/ueditor.all.js"></script>
</head>	
<body>

	
	<!-- main-end -->
	<div>
	<!-- 右侧主页面的开始 -->
	<div>
	<center>
	<button type="button" class="zte-btn zte-btn-common" style="background:#efefef">消息编辑</button>
	<button type="button" class="zte-btn zte-btn-common">消息预览</button>
	</center>
	</div>
	<div >  
	<center>                  	
	 	<table class="zte-ipt-con" style="width: 600px;">
              <tr >
                 <td class="w30"  style="text-align: left;" ><span >目标用户：</span></td>
             	 <td style="text-align: left;">                             
             	 <div class="zte-select-radio" >
                     <div name="" id="target_user" class="zte-select-result" onsubmit="">
                           <input type="text" placeholder="-请选择-" class="zte-inp-width-a"/>
                            <button type="button" class="zte-btn-select zte-iconfont icon-arrow-down-b"  ></button>
                            <div class="zte-clear"></div>
                            
                      </div>
                 <div class="zte-select-box" >  
                  <ul>
					<li>选择一</li>
					<li>选择二</li>
					<li>选择三</li>
					<li>选择四</li>
					<li>选择五</li>
				 </ul>
                  </div>
                </div> 
                 </td>             
              </tr>
                 <tr >
                  <td class="w30" "><span >消息模板：</span></td>
                  <td>                             
             	 <div class="zte-select-radio">
                     <div name="" id="msg_template" class="zte-select-result" onsubmit="">
                           <input type="text" placeholder="-请选择-" class="zte-inp-width-a"/>
                            <button type="button" class="zte-btn-select zte-iconfont icon-arrow-down-b" ></button>
                            <div class="zte-clear"></div>
                      </div>
                 <div class="zte-select-box" >   
                  </div>
                </div> 
                 </td>    
                  </tr>
                  <tr>
                  <td class="w30"  ><span>推送信息：</span></td>
                  </tr>   
            </table>              
	</center> 
	</div>
	
	<div>
	<!-- ueditor引入 -->
	<textarea id="container" name="container"
	style="width: 600px; height: 300px; margin: 0 auto;">
    </textarea>
    <script type="text/javascript">
	var ue = UE.getEditor("container");
	</script>
	</div>
	</div>
	<center>
	<button type="button" class="zte-btn zte-btn-common">关闭</button>
	<button type="button" class="zte-btn zte-btn-common">下一步</button>
	</center>
</body>
</html>
