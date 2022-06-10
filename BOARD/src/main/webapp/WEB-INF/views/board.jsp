<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="loginId"     value="${sessionScope.id == null ? '':sessionScope.id}"/>
<c:set var="loginLink"   value="${loginId=='' ? '/login/login' : '/login/logout.do'}"/>
<c:set var="loginLabel"  value="${loginId=='' ? 'Login' : 'Logout'}"/>
<c:set var="regLabel"    value="${loginId=='' ? 'Sign in' : 'Account'}"/>
<c:set var="regLink"     value="${loginId=='' ? '/user/add' : '/user/modify'}"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>게시글</title>
	<link rel="stylesheet" href="<c:url value='/resources//css/menu.css'/>">   
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<script src="https://code.jquery.com/jquery-1.11.3.js"></script>
	<style>
    	* {
     	 box-sizing: border-box;
     	 margin: 0;
      	padding: 0;
      	font-family: "Noto Sans KR", sans-serif;
   		 }

	    .container {
	      width : 50%;
	      margin : auto;
	    }

	    .writing-header {
	      position: relative;
	      margin: 20px 0 0 0;
	      padding-bottom: 10px;
	      border-bottom: 1px solid #323232;
	    }

	    input {
	      width: 100%;
	      height: 35px;
	      margin: 5px 0px 10px 0px;
	      border: 1px solid #e9e8e8;
	      padding: 8px;
	      background: #f8f8f8;
	      outline-color: #e6e6e6;
	    }

	    textarea {
	      width: 100%;
	      background: #f8f8f8;
	      margin: 5px 0px 10px 0px;
	      border: 1px solid #e9e8e8;
	      resize: none;
	      padding: 8px;
	      outline-color: #e6e6e6;
	    }

	    .frm {
	      width:100%;
	    }
	    .btn {
	      background-color: rgb(236, 236, 236); /* Blue background */
	      border: none; /* Remove borders */
	      color: black; /* White text */
	      padding: 6px 12px; /* Some padding */
	      font-size: 16px; /* Set a font size */
	      cursor: pointer; /* Mouse pointer on hover */
	      border-radius: 5px;
	    }

	    .btn:hover {
	      text-decoration: underline;
	    }
    
	    table {
            border-collapse: collapse;
            width: 100%;
            margin: 20px 0px 20px 0px;
            border-top: 1px solid #ddd;
        }

        tr:nth-child(even) {
            background-color: #f0f0f070;
        }

        th,td {
            width:300px;
            text-align: center;
            padding: 10px 12px;
            border-bottom: 1px solid #ddd;
        }

        td {
            color: rgb(53, 53, 53);
            text-decoration-line: none;         
        }
        
        .reply-list-replyer  { text-align: center; font-weight: bold; width:15%; }
        .reply-list-reply   { text-align: left; width:70%;  }
        .reply-list-regdate { text-align: center; font-size: 14px; width:15%;}
		.reply-list-rno  {width:0%; display:none; }

		#reply-writebox {
		    background-color: white;
		    border : 1px solid #e5e5e5;
		    border-radius: 5px;
		    margin : 10px;
		}

		#reply-writebox div {
		    padding : 3px 10px 10px 10px;
		}

		#reply-textarea{
		    display: block;
		    width: 100%;
		    min-height: 17px;
		    padding: 0 20px;
		    border: 0;
		    outline: 0;
		    font-size: 13px;
		    resize: none;
		    box-sizing: border-box;
		    background: transparent;
		    overflow-wrap: break-word;
		    overflow-x: hidden;
		    overflow-y: auto;
		}
	</style>
</head>

<script>
  let msg = "${msg}";
  if(msg=="WRT_ERR") alert("게시물 등록에 실패하였습니다. 다시 시도해 주세요.");
  if(msg=="MOD_ERR") alert("게시물 수정에 실패하였습니다. 다시 시도해 주세요.");
</script>

<body>
	<div id="menu">
		<ul>
			<li id="logo">게시판</li>
			<li><a href="<c:url value='/'/>">Home</a></li>
			<li><a href="<c:url value='/board/list'/>">Board</a></li>
			<li><a href="<c:url value='${loginLink}'/>">${loginLabel}</a></li>
			<li><a href="<c:url value='${regLink}'/>">${regLabel}</a></li>
		</ul>
	</div>

	<div class="container">
		<h2 class="writing-header">게시판 ${empty board ? "글쓰기" : "읽기"}</h2>
		<form id="form" class="frm" action="" method="post">
			<input type="hidden" name="bno" id="bno" value="${board.bno}">			
			<input name="title" type="text" value="${board.title}" placeholder="  제목을 입력해 주세요." ${empty board ? "" : "readonly='readonly'"}><br>
			<textarea name="content" rows="20" placeholder=" 내용을 입력해 주세요." ${empty board ? "" : "readonly='readonly'"}>${board.content}</textarea><br>
			
			<c:if test="${empty board}">
				<button type="button" id="writeBtn" class="btn btn-write"><i class="fa fa-pencil"></i> 등록</button>
			</c:if>
			
			<c:if test="${!empty board}">
				<button type="button" id="writeNewBtn" class="btn btn-write"><i class="fa fa-pencil"></i> 글쓰기</button>
			</c:if>
			
			<c:if test="${board.writer eq loginId}">
				<button type="button" id="modifyBtn" class="btn btn-modify"><i class="fa fa-edit"></i> 수정</button>
				<button type="button" id="removeBtn" class="btn btn-remove"><i class="fa fa-trash"></i> 삭제</button>
			</c:if>
			
			<button type="button" id="listBtn" class="btn btn-list"><i class="fa fa-bars"></i> 목록</button>
		</form>
	</div>
<br>
<br>

<div id="reply-Area" class=container>
	<div  id="reply-list">
		<h2>답글목록</h2>
		<table>
			<thead>
				<tr>
					<th class="reply-list-rno"></th>
					<th class="reply-list-replyer">글쓴이</th>
					<th class="reply-list-reply">내용</th>
					<th class="reply-list-regdate">등록일</th>	
				</tr>
			</thead>
			<tbody>       	
			</tbody>
		</table>	
		<br>
	</div>
	<div id="reply-writebox">
		<div id="reply-writebox-top">
			${id}  
			<label for="reply-parent-rno" id="reply-to"></label>
			<input type="hidden" id="reply-parent-rno" name="prno">
			<input type="hidden" id="reply-rno" name="rno">
		</div>
		<div id="reply-writebox-content">
	        <textarea name="reply" id="reply-textarea" cols="30" rows="3" placeholder="댓글을 남겨보세요"></textarea>
	    </div>
	    <div id="reply-writebox-bottom"> 
	    	<a href="#" class="btn" id="btn-write-reply">등록</a> 
	    	<a href="#" class="btn" id="btn-modify-reply">수정</a>  
			<a href="#" class="btn" id="btn-cancel-reply">취소</a>        
	    </div>
	</div>
</div>
	


<script>		
	function replyWriteInit(){
		document.getElementById("reply-to").innerHTML = "";
		document.getElementById("reply-textarea").value = "";
		document.getElementById("reply-parent-rno").value = "";
		document.getElementById("reply-rno").value = "";		
		document.getElementById("btn-modify-reply").style.display = "none";
		document.getElementById("btn-write-reply").style.display = "inline";
	}

	function bindWriteBox(aTarget){
		replyWriteInit();
		
		let mother = aTarget.parentNode.parentNode;
		let childs = mother.childNodes;
		
		let rno = 0;
		let replyer = "";
		let reply  = "";
		let temp = "";
		for (let i=0; i<childs.length; i++){
			let child = childs[i];
			if (child.className == "reply-list-rno")
				rno = child.innerHTML; 	
			else if (child.className == "reply-list-replyer")
				replyer = child.innerHTML;
			else if (child.className == "reply-list-reply"){
				reply = child.firstChild.lastChild.innerHTML;
			}	
		}
			
		if (aTarget.className === "addChildReply" && rno !== 0 && replyer !== ""){			
			document.getElementById("reply-to").innerHTML = '->' + replyer;
			document.getElementById("reply-parent-rno").value = rno;
		}else if (aTarget.className === "modifyReply" && rno !== 0 && reply !== ""){
			document.getElementById("reply-rno").value = rno;
			document.getElementById("reply-textarea").value = reply;
			//분리예정
			document.getElementById("btn-modify-reply").style.display = "inline";
			document.getElementById("btn-write-reply").style.display = "none";
		}
	}
	function formCheck() {
		let form = document.getElementById("form");
		if(form.title.value=="") {
			alert("제목을 입력해 주세요.");
			form.title.focus();
			return false;
   		}

		if(form.content.value=="") {
			alert("내용을 입력해 주세요.");
			form.content.focus();
			return false;
		}
		return true;
	}

	
    function writeNewBtnClick(){
      location.href="<c:url value='/board/add'/>";
    };


    function writeBtnClick(){
      let form = document.querySelector("#form");
      form.setAttribute("action", "<c:url value='/board/add'/>");
      form.setAttribute("method", "post");

      if(formCheck())
        form.submit();
    };

    function modifyBtnClick(){
    	let form = document.querySelector("#form");
      	let isReadonly = document.querySelectorAll('input[name=title]').item(0).readOnly;
      if(isReadonly == true) {
    	 document.querySelector(".writing-header").innerText = "게시판 수정";
    	 document.querySelectorAll("input[name=title]").item(0).readOnly = false;
    	 document.querySelector("textarea").readOnly = false;
    	 document.querySelector("#modifyBtn").innerHTML = "<i class='fa fa-pencil'></i> 등록";
        return;
      }

      // 2. 수정 상태이면, 수정된 내용을 서버로 전송
      form.setAttribute("action", "<c:url value='/board/modify?${boardSelector.getQueryString(boardSelector.page)}'/>");
      form.setAttribute("method", "post");
      if(formCheck())
        form.submit();
    };

    function removeBtnClick(){
      if(!confirm("정말로 삭제하시겠습니까?")) return;

      let form = document.querySelector("#form");
      form.setAttribute("action", "<c:url value='/board/remove?${boardSelector.getQueryString(boardSelector.page)}'/>");
      form.setAttribute("method", "post");
      form.submit();
    };

    function listBtnClick(){
      location.href="<c:url value='/board/list?${boardSelector.getQueryString(boardSelector.page)}'/>";
    };
    
   
    function replyWriteBtnClick(){
		let request = new XMLHttpRequest(); 
      	let bno =  document.getElementById("bno").value;
      	let reply = document.querySelector('#reply-textarea').value;
      	let prno = document.querySelector('#reply-parent-rno').value;
      	let url =  "/BOARD/reply/add?bno=" + bno + 
      			   "&reply=" + encodeURIComponent(reply) + 
      			   "&prno=" + prno;
      	
      	request.open("POST", url, true); //요청
      	request.onreadystatechange = function(){
    		let area = document.querySelector("tbody");
    		area.innerHTML = "";
    		if (request.readyState == 4 && request.status == 200){ 
       			alert("댓글 작성에 성공하셨습니다.");
       			replyWriteInit();
       			getReply();
       			
    		};
    	};   
    	request.send(null);	  	
    }
    
    function replyModifyBtnClick(){
    	let request = new XMLHttpRequest(); 
      	let rno =  document.getElementById("reply-rno").value; //rno가져와야함
      	let reply = document.getElementById("reply-textarea").value
      	let url =  "/BOARD/reply/modify?rno=" + rno + 
      			   "&reply=" + encodeURIComponent(reply);
      	
      	request.open("POST", url, true); //요청
      	request.onreadystatechange = function(){
    		let area = document.querySelector("tbody");
    		area.innerHTML = "";
    		if (request.readyState == 4){
    			let obj = JSON.parse(request.responseText);
    			if (request.status == 200){
           			alert("댓글 수정에 성공하셨습니다.");
           			replyWriteInit();
           			getReply();  				
    			}else if (request.status == 400 || request.status == 500)
    				alert(obj.errMsg);
    		}
    	};   
    	request.send(null);	  	
    }
    
    function replyRemoveClick(aTarget){
    	if (!confirm("정말로 댓글을 삭제합니까?")) return;
    	
		let mother = aTarget.parentNode.parentNode;
		let childs = mother.childNodes;
		
		let rno = 0;
	
		for (let i=0; i<childs.length; i++){
			let child = childs[i];
			if (child.className == "reply-list-rno")
				rno = child.innerHTML; 	
		}
		
		if (rno !== 0){
			let request = new XMLHttpRequest(); 
	      	let url =  "/BOARD/reply/remove?rno=" + rno;
	
			request.open("POST", url, true); //요청
			request.onreadystatechange = function(){
				let area = document.querySelector("tbody");
				area.innerHTML = "";
	    		if (request.readyState == 4){
	    			let obj = JSON.parse(request.responseText);
	    			if (request.status == 200){
						alert("댓글 삭제에 성공하셨습니다.");
						replyWriteInit();
						getReply();					
	    			}else if (request.status == 400 || request.status == 500)
	    				alert(obj.errMsg);
	    		}
			};
			request.send(null);	
		};
		  				
    }
        
	function getReply(){
		let requestReply = new XMLHttpRequest(); 
		let bno = document.getElementById("bno").value;
		requestReply.open("Get", "/BOARD/reply/reply?bno=" + bno , true); //요청
		requestReply.onreadystatechange = function(){
    		let area = document.querySelector("tbody");
    		area.innerHTML = "";
    		
    		if (requestReply.readyState == 4){
    			let object = JSON.parse(requestReply.responseText);
    			if (requestReply.status == 200){
    	   			for (let i = 0; i < object.length; i++){
        				let layerLabel = "";
        				for (let j = 1; j < object[i].level; j++)
        					layerLabel += "    ";				
        				if (layerLabel !== "") layerLabel += "ㄴ [답변] ";
        				
        				//loginId, object[i].replyer
        				area.innerHTML +=
        					"<tr>" +  
        					"	<td class='reply-list-rno'>" + object[i].rno + "</td> " + 
    						"	<td class='reply-list-replyer'>" + object[i].replyer + "</td> " +
    						"	<td class='reply-list-reply'><pre><span>" + layerLabel + "</span><span>" + object[i].reply + "</span></pre></td> " + 
    						"	<td class='reply-list-regdate'>" + object[i].reg_date + " <br><br> " + 
    						"		<a href='#reply-textarea' class='addChildReply' onClick= 'bindWriteBox(this);'>답글 </a>	" +
    					('${loginId}'=== object[i].replyer ? 
    						"		<a href='#reply-textarea' class='modifyReply'   onClick= 'bindWriteBox(this);'>| 수정 </a>	" +  
    						"		<a href='#' class='deleteReply'  onClick= 'replyRemoveClick(this);'>| 삭제</a>	" : "") +
    						"	</td>" + 
    						"<tr>";
        			}
        		
        			if (object.length == 0){
        				area.innerHTML +=
    					"<tr>" +  
    					"	<td class='writer' colspan=4'> 게시글이 없습니다!</td> " +
    					"<tr>";   			
        			}
        		}else if (requestReply.status == 400 || requestReply.status == 500)
    				alert(object.errMsg);
    		}
    	};   
    	requestReply.send(null);	
	}
    
	window.onload = function(){
		let targetDom = document.getElementById("writeNewBtn"); 
		if (targetDom != null) targetDom.onclick = writeNewBtnClick;
		
		targetDom = document.getElementById("writeBtn");  
		if (targetDom != null) targetDom.onclick = writeBtnClick;
		
		targetDom = document.getElementById("modifyBtn");  
		if (targetDom != null) targetDom.onclick = modifyBtnClick;
		
		targetDom = document.getElementById("removeBtn");  
		if (targetDom != null) targetDom.onclick = removeBtnClick;

		targetDom = document.getElementById("listBtn");  
		if (targetDom != null) targetDom.onclick = listBtnClick;

		targetDom = document.getElementById("btn-write-reply");  
		if (targetDom != null) targetDom.onclick = replyWriteBtnClick;
	
		targetDom = document.getElementById("addChileReply");  
		if (targetDom != null) targetDom.onclick = replyWriteBtnClick;

		targetDom = document.getElementById("btn-cancel-reply");  
		if (targetDom != null) targetDom.onclick = replyWriteInit;
		
		targetDom = document.getElementById("btn-modify-reply");  
		if (targetDom != null) targetDom.onclick = replyModifyBtnClick;

		if (${not empty board}){
			document.getElementById("reply-Area").style.display = 'block';
			replyWriteInit();
			getReply();
		}else{
			document.getElementById("reply-Area").style.display = 'none';		
		}
	}
</script>
</body>
</html>