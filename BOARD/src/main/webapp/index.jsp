<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="loginId"     value="${sessionScope.id == null ? '': sessionScope.id}"/>
<c:set var="loginLink"   value="${loginId=='' ? '/login/login' : '/login/logout.do'}"/>
<c:set var="loginLabel"  value="${loginId=='' ? 'Login' : 'Logout'}"/>
<c:set var="regLabel"    value="${loginId=='' ? 'Sign in' : 'Account'}"/>
<c:set var="regLink"     value="${loginId=='' ? '/user/add' : '/user/modify'}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>게시판 홈</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/menu.css'/>" type="text/css">
</head>
<body>
<script>
    let msg = "${param.msg}";
 	if (msg != "") alert(msg);
</script>

<div id="menu">
    <ul>
        <li id="logo">게시판</li>
        <li><a href="<c:url value='/'/>">Home</a></li>
        <li><a href="<c:url value='/board/list'/>">Board</a></li>
        <li><a href="<c:url value='${loginLink}'/>">${loginLabel}</a></li>
        <li><a href="<c:url value='${regLink}'/>">${regLabel}</a></li>
    </ul>
</div>
<div style="text-align:center">
    <h1>게시판 홈입니다.</h1>
</div>
</body>
</html>