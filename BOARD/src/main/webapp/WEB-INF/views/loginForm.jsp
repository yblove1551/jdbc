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
  <title>로그인</title>
    <link rel="stylesheet" href="<c:url value='/resources//css/menu.css'/>">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"/>
  <style>
    * { box-sizing:border-box; }
    a { text-decoration: none; }
    form {
      width:400px;
      height:500px;
      display : flex;
      flex-direction: column;
      align-items:center;
      position : absolute;
      top:50%;
      left:50%;
      transform: translate(-50%, -50%) ;
      border: 1px solid rgb(89,117,196);
      border-radius: 10px;
    }
    input[type='text'], input[type='password'] {
      width: 300px;
      height: 40px;
      border : 1px solid rgb(89,117,196);
      border-radius:5px;
      padding: 0 10px;
      margin-bottom: 10px;
    }
    button {
      background-color: rgb(89,117,196);
      color : white;
      width:300px;
      height:50px;
      font-size: 17px;
      border : none;
      border-radius: 5px;
      margin : 20px 0 30px 0;
    }
    #title {
      font-size : 50px;
      margin: 40px 0 30px 0;
    }
    #msg {
      height: 30px;
      text-align:center;
      font-size:16px;
      color:red;
      margin-bottom: 20px;
    }
  </style>
</head>
<body>
	<div id="menu">
	    <ul>
	        <li id="logo">게시판</li>
	        <li><a href="<c:url value=''/>">Home</a></li>
	        <li><a href="<c:url value='/board/list'/>">Board</a></li>
	        <li><a href="<c:url value='${loginLink}'/>">${loginLabel}</a></li>
	        <li><a href="<c:url value='${regLink}'/>">${regLabel}</a></li>
	    </ul>
	</div>
	<form action="<c:url value="/login/login"/>" method="post">
		<h3 id="title">Login</h3>
		<div id="msg">
			<c:if test="${not empty param.msg}">
				<i class="fa fa-exclamation-circle">${param.msg}</i>
			</c:if>
		</div>
		<input type="text" name="id" value="${cookie.id.value}" placeholder="이메일 입력"  required ${empty cookie.id.value ? "autofocus" : ""}>
		<input type="password" name="pwd" placeholder="비밀번호" required ${not empty cookie.id.value ? "autofocus" : ""}>
		<input type="hidden" name="toURL" value="${param.toURL}">
		<button>로그인</button>
		<div>
	    	<label><input type="checkbox" name="rememberId" value="true" ${empty cookie.id.value ? "":"checked"}> 아이디 기억</label> |
	    	<a href="<c:url value='/user/find'/>">비밀번호 찾기</a> |
	    	<a href="<c:url value='/user/add'/>">회원가입</a>
	  	</div>
	</form>
</body>
</html>