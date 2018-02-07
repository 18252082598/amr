<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../jsp/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>login</title>
<link rel="stylesheet" type="text/css" href="../Assets/css/reset.css" />
<link rel="stylesheet" type="text/css" href="../Assets/css/common.css" />
<link rel="stylesheet" type="text/css" href="../Assets/css/thems.css" />
<script type="text/javascript" src="../Assets/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="../Assets/js/login.js"></script>
<script type="text/javascript">
	$(function() {
		//自适应屏幕宽度
		window.onresize = function() {
			location = location
		};

		var w_height = $(window).height();
		$('.bg_img').css('height', w_height + 'px');

		var bg_wz = 1920 - $(window).width();
		$('.bg_img img').css('margin-left', '-' + bg_wz / 2 + 'px')

		$('.language .lang').click(function() {
			$(this).siblings('.lang_ctn').toggle();
		});
	})
</script>
</head>
<body>
	<div class="login">
		<div class="bg_img">
			<img src="../Assets/images/login_bg.jpg">
		</div>
		<div class="logo">
			<a href=""><img src="../Assets/images/logo.png" /></a>
		</div>
		<div class="login_m">
			<form action="${path }admin/get" id="login" method="post">
				<ul>
					<li class="wz">用户名:</li>
					<li><input name="username" type="text" id="username"></li>
					<li class="wz">密码:</li>
					<li><input name="password" type="password" id="password"></li>
					<li class="l_btn" type=""><input type="submit" value="登陆"></li>
				</ul>
			</form>
		</div>
	</div>
	
</body>
</html>