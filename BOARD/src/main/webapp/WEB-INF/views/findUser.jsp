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
        
        #radio-area{
            width: 300px;
            height: 40px;
            border : 1px solid rgb(89,117,196);
            border-radius:5px;
            padding: 0 10px;
            margin-bottom: 10px;
        }
    </style>
    <title>회원찾기</title>
</head>
<body>
	<form>
		<h3 id="title">회원찾기</h3>
		<br>
		
		<label>항목</label>
		<div id="radio-area">
			<input type="radio" name="radio_type" value="id" checked>아이디
			<input type="radio" name="radio_type" value="pw" >비밀번호
		</div>
		<label for="email">이메일</label>
		<input class="input-field" type="text" name="email" id="email" placeholder="본인확인 이메일을 입력해주세요."> 		
		<label for="name">이름</label>
		<input class="input-field" type="text" name="name" id="name" placeholder="이름을 입력해주세요.">		
		
		<div id="email-Auth" style="display:none;">
			<input type="text" name="emailAuthNum" id="emailAuthNum">
			<input type="button" id="btnEmailAuth" name="btnEmailAuth" value="확인">
		</div>
		
		<input type="hidden" id="id" value="">
 	
		<button type="button" id="btnFind">찾 기</button>
		<button type="button" id="btnBack" onclick="history.go(-1)">돌아가기</button>
	</form> 
   
   <script>
   		$(document).ready(function(){
//			document.getElementById("emailCheck").style.display = "none";
//   			document.getElementById("emailCheck").disabled = true;   						
   		});

   		
        $("#btnFind").on("click", function(){
        	//검증
   			let email = document.getElementById("email").value;
   			let name = document.getElementById("name").value;
   			
   			if (email.trim().length == 0 || name.trim().length == 0){
   				alert('이메일 또는 이름이 누락되었습니다.');
   				return;
   			} 
   			
   			//이메일 유효한지 확인 OK
   			//보낼 메일 세팅
   			//메일 발송
   			//사용자에 결과 전달
   			emailAndNameCheck(email, name);
   				
        });
         
        //이메일 유효한지 확인
        function emailAndNameCheck(email, name){
   			let request = new XMLHttpRequest(); 
	      	let url =  "/BOARD/user/checkEmail?";	      	
	      	url += "email=" + encodeURI(email); 
	      	url += "&name=" + encodeURI(name);
	      	
			request.open("GET", url, true); //요청
			request.onreadystatechange = function(){
				if (request.readyState == 4 && request.status == 200){	
	       			let object = JSON.parse(request.responseText);
	   				let result = object.result;
	   				let size = document.getElementsByName("radio_type").length;
	   				
	   				let kind = -1;
	   				if (result === "OK"){
	   					let id = object.id;
	   				 	for(let i = 0; i < size; i++)
	   				        if (document.getElementsByName("radio_type")[i].checked)
	   			        		if (document.getElementsByName("radio_type")[i].value === "id")
	   			        			 kind = 3;	
	   			        		else
	   			        			 kind = 2;	

		   				if (kind !== -1){
		   					sendMail(email, name, kind, id);
		   				}
		   					 					
		   				else
			   				alert("아이디 또는 비밀번호를 선택하세요");
	   				} else if (result === "NO")
		   					alert("이메일을 찾을 수 없습니다.");	
	   				else
	   					alert("서버에서 에러가 발생했습니다.");
	   				
				}
				
			};
			request.send(null);	     	
        }
//xhr.setRequestHeader('Content-Type', 'application/json');

		 function sendMail(email, name, kind, id){
   			let request = new XMLHttpRequest(); 
	      	let url =  "/BOARD/mail/mail?";
	      	url += "email=" + encodeURI(email); 
	      	url += "&name=" + encodeURI(name);
	      	url += "&kind=" + kind;
	      	url += "&id=" + id;
	      	
			request.open("POST", url, true); //요청
			request.onreadystatechange = function(){
				if (request.readyState == 4 && request.status == 200){ 
					let obj = JSON.parse(request.responseText); 	
					
		        	if (obj.result === "OK"){
						if (kind === 3){
							alert("아이디가 메일로 발송되었습니다.");	
							location.href = "<c:url value='/'/>";
							//페이지 홈으로 이동
						}else{
							document.getElementById("id").value = id;
							document.getElementById("email-Auth").style.display = "block";
							document.getElementById("btnFind").disabled = true; 
							alert("메일에 전달된 인증번호 4자리를 입력해주세요..");	
						}		
		        	}else
		        		alert("메일 발송에 실패했습니다.");		        	
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
						alert("인증이 완료되었습니다 비밀번호를 재설정 해주세요.");
						let id = document.getElementById("id").value; 
						let url = "/BOARD/user/newpw?id="+ id;
						location.href = url;
					}else if (obj.result === "INVALID"){
						alert("인증번호가 유효하지 않습니다.");					
					}else 
						alert("죄송합니다. 서버에 에러가 발생했습니다.");								
				};
			};
			request.send(null);     	
        }
   </script>
</body>
</html>