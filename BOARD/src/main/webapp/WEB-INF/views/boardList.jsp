<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<c:set var="loginId"     value="${sessionScope.id == null ? '': sessionScope.id}"/>>
<c:set var="loginLink"   value="${loginId=='' ? '/login/login' : '/login/logout.do'}"/>
<c:set var="loginLabel"  value="${loginId=='' ? 'Login' : 'Logout'}"/>
<c:set var="regLabel"    value="${loginId=='' ? 'Sign in' : 'Account'}"/>
<c:set var="regLink"     value="${loginId=='' ? '/user/add' : '/user/modify'}"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 목록</title>
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

        a {
            text-decoration: none;
            color: black;
        }
        button,
        input {
            border: none;
            outline: none;
        }

        .board-container {
            width: 60%;
            height: 1200px;
            margin: 0 auto;
            /* border: 1px solid black; */
        }
        .search-container {
            background-color: rgb(253, 253, 250);
            width: 100%;
            height: 110px;
            border: 1px solid #ddd;
            margin-top : 10px;
            margin-bottom: 30px;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .search-form {
            height: 37px;
            display: flex;
        }
        .search-option {
            width: 100px;
            height: 100%;
            outline: none;
            margin-right: 5px;
            border: 1px solid #ccc;
            color: gray;
        }

        .search-option > option {
            text-align: center;
        }

        .search-input {
            color: gray;
            background-color: white;
            border: 1px solid #ccc;
            height: 100%;
            width: 300px;
            font-size: 15px;
            padding: 5px 7px;
        }
        .search-input::placeholder {
            color: gray;
        }

        .search-button {
            /* 메뉴바의 검색 버튼 아이콘  */
            width: 20%;
            height: 100%;
            background-color: rgb(22, 22, 22);
            color: rgb(209, 209, 209);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 15px;
        }
        .search-button:hover {
            color: rgb(165, 165, 165);
        }

        table {
            border-collapse: collapse;
            width: 100%;
            border-top: 2px solid rgb(39, 39, 39);
        }

        tr:nth-child(even) {
            background-color: #f0f0f070;
        }

        th,
        td {
            width:300px;
            text-align: center;
            padding: 10px 12px;
            border-bottom: 1px solid #ddd;
        }

        td {
            color: rgb(53, 53, 53);
        }

        .no      { width:150px;}
        .title   { width:50%;  }

        td.title   { text-align: left;  }
        td.writer  { text-align: left;  }
        td.viewcnt { text-align: right; }

        td.title:hover {
            text-decoration: underline;
        }

        .paging {
            color: black;
            width: 100%;
            align-items: center;
        }

        .page {
            color: black;
            padding: 6px;
            margin-right: 10px;
        }
        .paging-active {
            background-color: rgb(216, 216, 216);
            border-radius: 5px;
            color: rgb(24, 24, 24);
        }

        .paging-container {
            width:100%;
            height: 70px;
            display: flex;
            margin-top: 50px;
            margin : auto;
        }
        .btn-write {
            background-color: rgb(236, 236, 236); /* Blue background */
            border: none; /* Remove borders */
            color: black; /* White text */
            padding: 6px 12px; /* Some padding */
            font-size: 16px; /* Set a font size */
            cursor: pointer; /* Mouse pointer on hover */
            border-radius: 5px;
            margin-left: 30px;
        }

        .btn-write:hover {
            text-decoration: underline;
        }
    </style>
</head>
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
<script>
    let msg = "${param.msg}";
    if(msg !== "") alert(msg);

</script>
<div style="text-align:center">
    <div class="board-container">
        <div class="search-container">
            <form action="<c:url value="/board/list"/>" class="search-form" method="get">
                <select class="search-option" name="option">
                    <option value="A" ${boardSelector.option=='A' || boardSelector.option=='' ? "selected" : ""}>제목+내용</option>
                    <option value="T" ${boardSelector.option=='T' ? "selected" : ""}>제목만</option>
                    <option value="W" ${boardSelector.option=='W' ? "selected" : ""}>작성자</option>
                </select>

                <input type="text" name="keyword" class="search-input" type="text" value="${boardSelector.keyword}" placeholder="검색어를 입력해주세요">
                <input type="submit" class="search-button" value="검색">
            </form>
            <button id="writeBtn" class="btn-write" onclick="location.href='<c:url value="/board/add"/>'"><i class="fa fa-pencil"></i> 글쓰기</button>
        </div>
        
        <table>
            <tr>
                <th class="no">번호</th>
                <th class="title">제목</th>
                <th class="writer">이름</th>
                <th class="regdate">등록일</th>
                <th class="viewcnt">조회수</th>
            </tr>
            <c:forEach var="board" items="${boardList}">
                <tr>
                    <td class="no">${board.bno}</td>
                    <td class="title"><pre><a href="<c:url value="/board/board?${boardSelector.queryString()}&bno=${board.bno}"/>">${board.title}</a></pre></td>
                    <td class="writer">${board.writer}</td>
                    <c:choose>
                        <c:when test="${board.reg_date.time >= today.time}">
                            <td class="regdate"><fmt:formatDate value="${board.reg_date}" pattern="HH:mm" type="time"/></td>
                        </c:when>
                        <c:otherwise>
                            <td class="regdate"><fmt:formatDate value="${board.reg_date}" pattern="yyyy-MM-dd" type="date"/></td>
                        </c:otherwise>
                    </c:choose>
                    <td class="viewcnt">${board.view_cnt}</td>
                </tr>
            </c:forEach>
        </table>
        <br>
        <div class="paging-container">
            <div class="paging">
                <c:if test="${boardSelector==null || boardSelector.totalCnt == 0}">
                    <div> 게시물이 없습니다. </div>
                </c:if>
                <c:if test="${boardSelector!=null && boardSelector.totalCnt != 0}">
                    <c:if test="${boardSelector.hasPrev}">
                        <a class="page" href="<c:url value="/board/list?${boardSelector.getQueryString(boardSelector.beginPage-1)}"/>">&lt;</a>
                    </c:if>
                    <c:forEach var="i" begin="${boardSelector.beginPage}" end="${boardSelector.endPage}">
                        <a class="page ${i==boardSelector.page? "paging-active" : ""}" href="<c:url value="/board/list?${boardSelector.getQueryString(i)}"/>">${i}</a>
                    </c:forEach>
                    <c:if test="${boardSelector.hasNext}">
                        <a class="page" href="<c:url value="/board/list?${boardSelector.getQueryString(boardSelector.endPage+1)}"/>">&gt;</a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>