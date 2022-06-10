<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page import="java.net.URLDecoder"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://code.jquery.com/jquery-1.11.3.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css" />
    <style>
        * { box-sizing:border-box; }

        form {
            width:400px;
            height:700px;
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

        .input-field {
            width: 300px;
            height: 40px;
            border : 1px solid rgb(89,117,196);
            border-radius:5px;
            padding: 0 10px;
            margin-bottom: 10px;
        }
        .input-field:read-only{
    	    color: gray;
        }
        label {
            width:300px;
            height:30px;
            margin-top :4px;
        }
        
        .labelAndBtn{
     	  width:100px; 
        }

        button {
            background-color: rgb(89,117,196);
            color : white;
            width:300px;
            height:50px;
            font-size: 17px;
            border : none;
            border-radius: 5px;
            margin : 10px 0 15px 0;
        }
        
         .checkBtn {
            background-color: rgb(89,117,196);
            color : white;
            width:50px;
            height:30px;
            font-size: 17px;
            border : none;
            border-radius: 5px;
            margin : 10px 0 15px 0;
        }   
        
        .checkBtn:hover{
  			cursor:pointer;
        }  

        .title {
            font-size : 50px;
            margin: 20px 0;
        }

        .msg {
            height: 30px;
            text-align:center;
            font-size:16px;
            color:red;
            margin-bottom: 20px;
        }
    </style>
    <title>Register</title>
</head>
<body>
	<form action="<c:url value="${empty user.id? '/user/add.do' : '/user/modify.do'}"/>" method="POST" onsubmit="return formCheck(this)">
<!-- form action="<c:url value="user/modify.do"/>" method="POST" onsubmit="return formCheck(this)"-->
		<div class="title">Register</div>
		<div id="msg" class="msg"> ${URLDecoder.decode(param.msg, "utf-8")}</div>  
		
		<div style="width:300px; height:40px;">
			<label for="id">아이디</label>
			<input type="button" name="idCheck" id="idCheck" class="checkBtn" value="확인">
		</div>
		
		<input class="input-field" type="text" name="id" id="id" placeholder="5~30자리의 영대소문자와 숫자 조합" required value="${user.id}" ${empty user ? "":"readonly"}>
		<label for="pwd">비밀번호</label>
		<input class="input-field" type="text" name="pwd" id="pwd" placeholder="5~30자리의 영대소문자와 숫자 조합" required value="${user.pwd}">
		<label for="name">이름</label>
		<input class="input-field" type="text" name="name" id="name" placeholder="홍길동" required value="${user.name}">
		<label for="phone">휴대폰</label>
		<input class="input-field" type="text" name="phone" id="phone" placeholder="-없이" value="${user.phone}"> 
			
		<div style="width:300px; height:40px;">
			<label for="email">이메일</label>
			<input type="button" name="emailCheck" id="emailCheck"  class="checkBtn" value="확인">
			<input type="button" name="emailModify" id="emailModify" class="checkBtn" value="수정" style="display:none;">
		</div>
		
		<input class="input-field" type="text" name="email" id="email" placeholder="example@fastcampus.co.kr" value="${user.email}"> 
		<div id="email-Auth" style="display:none;">
			<input type="text" name="emailAuthNum" id="emailAuthNum">
			<input type="button" id="btnEmailAuth" name="btnEmailAuth" value="인증">
		</div>
		
		<label for="birth">생일</label>
		<input class="input-field" type="text" name="birth" id="birth" placeholder="2020-12-31" value=<fmt:formatDate value="${user.birth}" pattern="yyyy-MM-dd" type="date"/>>
		 	
		<button type="button" id="btnSave">저 장</button>
		<button type="button" id="btnBack" onclick="history.go(-1)">돌아가기</button>
		<button type="button" id="btnDelete" style="display:none">회원탈퇴</button>
	</form> 
   
   <script>
   		$(document).ready(function(){
   			let id = "${user.id}";
   			if (id != ""){
   				document.querySelector(".title").innerText = "Modify";
   			    document.querySelector("title").innerText = "Modify";
   			    document.querySelector("#btnDelete").style.display = "block";
   			    document.getElementById("idCheck").style.display = "none";
   				document.getElementById("idCheck").disabled = true;
   			    document.getElementById("emailCheck").style.display = "none";
   				document.getElementById("emailCheck").disabled = true;
   				document.getElementById("emailModify").style.display = "inline";
   				
   				document.getElementById("email").readOnly = true;
   			}
   			
   		});
   		
        $("#btnSave").on("click", function(){
   			let id = "${user.id}";
   			if (id != "" && document.querySelector("#id").value !== id) return;
   			
   			let form = document.querySelector("form");
   			
   			if (!formCheck(form)) return;
   			
   			form.setAttribute("method", "post");
			form.setAttribute("action", "<c:url value="${empty user.id? '/user/add' : '/user/modify'}"/>");
			form.submit();	
        });
        
        $("#btnDelete").on("click", function(){
   	   		let id = "${user.id}";
   	   		if (id != "" && document.querySelector("#id").value === id){
   		   		if (!confirm("정말 회원을 탈퇴하시겠습니까?")) return;	
   		   		let form = document.querySelector("form");
   			 	form.setAttribute("method", "post");
   			 	form.setAttribute("action", "<c:url value='/user/remove'/>");
   			 	form.submit();
   	   		}	
        });
        
        $("#idCheck").on("click", function(){
            let id = document.querySelector("#id").value;
            
            if (id.length < 3){
                setMessage('id의 길이는 3이상이어야 합니다.', id);
                return;
            }

            idCheck(id);
        });
        
        //id 중복확인 ajax
        function idCheck(id){
        	
   			let request = new XMLHttpRequest(); 
	      	let url =  "/BOARD/user/id?id=" + id;
	
			request.open("GET", url, true); //요청
			request.onreadystatechange = function(){
				if (request.readyState == 4){
					let obj = JSON.parse(request.responseText);
					if (request.status == 200){
						if (obj.result === "OK"){
							document.getElementById("id").readOnly = true;
							document.getElementById("idCheck").disabled = true;			
							alert("사용가능한 ID입니다");					
						}else if (obj.result === "NO")
							alert("중복된 아이디입니다.");						
					}
					else if (request.status == 500){
						if (obj.result === "ERROR")
							alert("죄송합니다. 서버에러가 발생했습니다");	
					}
				}				
			};
			request.send(null);	      	
        }

        $("#emailCheck").on("click", function(){
            let email = document.querySelector("#email").value;
            emailCheck(email);
	
        });
        
        function emailCheck(email){
   			let request = new XMLHttpRequest(); 
	      	let url =  "/BOARD/user/email?email=" + email;
	
			request.open("GET", url, true); //요청
			request.onreadystatechange = function(){
				if (request.readyState == 4 && request.status == 200){
					let obj = JSON.parse(request.responseText);
					if (obj.result === "OK"){
			            let email = document.getElementById("email").value;
			            let name = document.getElementById("name").value;
			            let kind = 1;
			            sendMail(email, name, kind);				
					}else if (obj.result === "NO")
						alert("중복된 이메일입니다.");
					else
						alert("죄송합니다. 서버 에러입니다. 잠시 후 다시 시도해주세요.");
				}
			};
			request.send(null);	     	
        }
        
        
        function sendMail(email, name, kind){
   			let request = new XMLHttpRequest(); 
	      	let url =  "/BOARD/mail/mail?";
	      	url += "email=" + encodeURI(email); 
	      	url += "&name=" + encodeURI(name);
	      	url += "&kind=" + kind;
	
			request.open("POST", url, true); //요청
			request.onreadystatechange = function(){
				if (request.readyState == 4 && request.status == 200){ 
					alert(request.responseText);
					let obj = JSON.parse(request.responseText); 
					if (obj.result === "OK"){
						document.getElementById("email").readOnly = true;
						document.getElementById("emailCheck").disabled = true;
						document.getElementById("email-Auth").style.display = "block";						
						alert("메일발송에 성공했습니다");
					}else if (obj.result === "NO" || obj.result === "ERROR" )
						alert("메일발송에 실패했습니다");
				};
			};
			request.send(null);     	
        }
       
        $("#btnEmailAuth").on("click", function(){
            let email = document.getElementById("email").value;
            let authNum = document.getElementById("emailAuthNum").value;
            if (authNum.length != 4){
                setMessage('인증번호는 4자리입니다.', authNum);
                return;
            }

            authMail(email, authNum);
        });
        
        function authMail(email, authNumber){
   			let request = new XMLHttpRequest(); 
	      	let url =  "/BOARD/mail/auth?";
	      	url += "authNumber=" + encodeURI(authNumber); 
	      	url += "&email=" + encodeURI(email);
	
			request.open("POST", url, true); //요청
			request.onreadystatechange = function(){
				if (request.readyState == 4 && request.status == 200){ 
					let obj = JSON.parse(request.responseText);
					if (obj.result === "OK"){
						alert("인증이 완료되었습니다.");
						document.getElementById("email-Auth").style.display = "none";
					}else if (obj.result === "INVALID"){
						alert("인증번호가 유효하지 않습니다.");					
					}else 
						alert("죄송합니다. 서버에 에러가 발생했습니다.");				
				};
			};
			request.send(null);     	
        }
        
        $("#emailModify").on("click", function(){
        	if (!confirm("이메일을 수정하려면 인증이 필요합니다. 정말 수정하십니까?"))	 return;
        	
			document.getElementById("emailCheck").style.display = "inline";
   			document.getElementById("emailCheck").disabled = false;
			document.getElementById("emailModify").style.display = "none";
   			document.getElementById("emailModify").disabled = true;
   			
   			document.getElementById("email").readOnly = false;
        }); 

          		   		   
       function formCheck(frm) {
            var msg ='';

            if(frm.id.value.length<3) {
                setMessage('id의 길이는 3이상이어야 합니다.', frm.id);
                return false;
            }
            
            if (!document.getElementById("idCheck").disabled){
                setMessage('아이디 확인이 필요합니다.', document.getElementById("idCheck"));
                return false;            	
            }
            
            if (!document.getElementById("emailCheck").disabled){
                setMessage('이메일 인증이 필요합니다.', document.getElementById("emailCheck"));
                return false;            	
            }
          
            let result = confirm("저장하시겠습니까?");
            
           return result;
       }

       function setMessage(msg, element){
            document.getElementById("msg").innerHTML = `<i class="fa fa-exclamation-circle"> ${'${msg}'}</i>`;

            if(element) {
                element.select();
            }
       }
   </script>
</body>
</html>