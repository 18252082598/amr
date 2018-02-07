<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="../jsp/common/taglib.jsp"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<title>公众号信息</title>
<link rel="stylesheet" type="text/css" href="${path }wechat_web/Assets/css/reset.css"/>
<link rel="stylesheet" type="text/css" href="${path }wechat_web/Assets/css/common.css"/>
<link rel="stylesheet" type="text/css" href="${path }wechat_web/Assets/css/thems.css">
<script type="text/javascript" src="${path }wechat_web/Assets/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
$(function(){
	//自适应屏幕宽度
	window.onresize=function(){ location=location }; 
	
	var main_h = $(window).height();
	$('.hy_list').css('height',main_h-45+'px');
	
	var search_w = $(window).width()-40;
	$('.search').css('width',search_w+'px');
	//$('.list_hy').css('width',search_w+'px');
});
</script>
<!--框架高度设置-->
</head>
<body>
<div id="right_ctn">
	<div class="right_m">
		<!--会议列表-->
        <div class="hy_list">
        	<div class="box_t">
            	<span class="name">公众号列表</span>
                <!--当前位置-->
                <div class="position">
                	<a href=""><img src="${path }wechat_web/Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="${path }wechat_web/Assets/images/icon3.png" alt=""/></span>
                    <a href="">会议管理</a>
                    <span><img src="${path }wechat_web/Assets/images/icon3.png" alt=""/></span>
                    <a href="">会议列表</a>
                </div>
                <!--当前位置-->
            </div>
            <!--查询-->
            <div class="search">
            	<span>查询：</span>
                <div class="s_text"><input name="" type="text"></div>
                <a href="" class="btn">查询</a>
            </div>
            <!--查询-->
            <div class="space_hx">&nbsp;</div>
            <!--列表-->
            <form action="" method="post">
            <table cellpadding="0" cellspacing="0" class="list_hy">
              <tr>
                <th class="xz" scope="col">选择</th>
                <th scope="col"><div>公众号名称<a href="" class="up">&nbsp;</a><a href="" class="down">&nbsp;</a></div></th>
                <th class="zt" scope="col"><div>状态<a href="" class="up">&nbsp;</a><a href="" class="down">&nbsp;</a></div></th>
                <th scope="col">创建时间</th>
                <th scope="col">cjoyId</th>
                <th scope="col">appId</th>
                <th scope="col">cjoy</th>
                <th scope="col">会议容量</th>
              </tr>
              
         <c:forEach items="${wechats }" varStatus="i" var="wechat" >
          <tr id="list">
                <td class="xz"><input name="" type="checkbox" value=""></td>
                <td id="wechatname">${wechat.wechatname }</td>
                <td class="zt">${wechat.status }</td>
                <td><fmt:formatDate value="${wechat.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>${wechat.cjoyid}</td>
                <td>${wechat.appid}</td>
                <td>主会议</td>
                <td>-</td>
              </tr>
         </c:forEach>          
              <tr>
                <td class="xz"><input name="" type="checkbox" value=""></td>
                <td>ip64/1</td>
                <td class="zt">-</td>
                <td>2012-10-11 13:28</td>
                <td>永久会议</td>
                <td>-</td>
                <td>主会议</td>
                <td>-</td>
              </tr>
            </table>
            
            
            
            
            <!--列表-->
            <!--右边底部-->
            <div class="r_foot">
            	<div class="r_foot_m">
            	<span>
                	<input name="" type="checkbox" value="">
                    <em>全部选中</em>
                </span>
                <a href="" class="btn">删除</a>
                <a href="" class="btn">刷新</a>
                
                <!--分页-->
                <div class="page">
                	<a href="" class="prev"><img src="${path }wechat_web/Assets/images/icon7.png" alt=""/></a>
                    <a class="now">1</a>
                    <a href="">2</a>
                    <a href="">3</a>
                    <a href="">4</a>
                    <a href="">5</a>
                    <a href="">6</a>
                    <a href="" class="next"><img src="${path }wechat_web/Assets/images/icon8.png" alt=""/></a>
                </div>
                <!--分页-->
                </div>
            </div>
            </form>
            <!--右边底部-->
        </div>
        <!--会议列表-->
    </div>
</div>
</body>
</html>