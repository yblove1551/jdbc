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
    </style>
    <title>비밀번호설정</title>
</head>
<body>
	<form>
		<label for="pw">비밀번호</label>
		<input class="input-field" type="password" name="pw" id="pw" placeholder="비밀번호를 재설정 해주세요."> 		
		<label for="pwCheck">이름</label>
		<input class="input-field" type="password" name="pwCheck" id="pwCheck" placeholder="입력한 비밀번호를 확인해주세요.">	
		<input type="hidden" name="id" id="id" value="${requestScope.id}">	 	
		<button type="button" id="btnSave">저 장</button>
		<button type="button" id="btnBack" onclick="<c:url value='/'/>">돌아가기</button>
	</form> 
   
   <script>
        $("#btnSave").on("click", function(){
        	//검증
   			let pw = document.getElementById("pw").value;
   			let pwCheck = document.getElementById("pwCheck").value;
   			
   			if (pw.trim().length == 0 || pwCheck.trim().length == 0){
   				alert('이메일 또는 이름이 누락되었습니다.');
   				return;
   			} 
   			
   			if (pw.trim() !== pwCheck.trim()){
   				alert('비밀번호와 비밀번호 확인의 내용이 다릅니다.');
   				return; 				
   			}
   			let id = document.getElementById("id").value;
			//비밀번호 재설정
   			setUserPw(id, pw);
   				
        });
         
        //비번변경
        function setUserPw(id, pw){
   			let request = new XMLHttpRequest(); 
	      	let url =  "/BOARD/user/newpw?";	      	
	      	url += "id=" + encodeURI(id); 
	      	url += "&pw=" + encodeURI(pw);
	      	
			request.open("POST", url, true); //요청
			request.onreadystatechange = function(){
				if (request.readyState == 4 && request.status == 200){		
					let obj = JSON.parse((request.responseText);
					if (obj.result === "OK"){
						alert("비밀번호 변경에 성공했습니다. 다시 로그인 해주세요");	
						location.href = "<c:url value='/login/login'/>";
					}else if (obj.result === "NO"){
						alert("비밀번호 수정에 실패했습니다. 다시 시도해주세요");
					}else if (obj.result === "ERROR"){
						alert("서버에서 에러가 발생했습니다.");
					}
				}			
			};
			request.send(null);	     	
        }
   </script>
</body>
</html>