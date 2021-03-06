<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	pageContext.setAttribute("newLine", "\n");
%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
</head>
<body>
	<div id="container">
	<c:import url="/WEB-INF/views/includes/blog-header.jsp" />		
		<div id="wrapper">
			<div id="content">
				<div class="blog-content">
					<h4>${postOne.title }</h4>
					<p> ${fn:replace(postOne.contents, newLine, "<br>") } <br><p>
				</div>
				<ul class="blog-list">
				<li><a href="${pageContext.request.contextPath}/${blogVo.id}">전체목록</a></li>
				<li></li>
				<c:choose>
				<c:when test="${chk == 0 }">
					<c:forEach items="${postList }" var="vo" varStatus="status">
						<li><a href="${pageContext.request.contextPath}/${blogVo.id}/${vo.categoryNo}/${vo.no}">${vo.title }</a> <span>${vo.regDate }</span>	</li>
					</c:forEach>				
				</c:when>
				<c:otherwise>
					<c:forEach items="${postList }" var="vo" varStatus="status">
						<li><a href="${pageContext.request.contextPath}/${blogVo.id}/0/${vo.no}">${vo.title }</a> <span>${vo.regDate }</span>	</li>
					</c:forEach>
				</c:otherwise>
				</c:choose>				
				</ul>
			</div>
		</div>

		<div id="extra">
			<div class="blog-logo">
				<img src="${pageContext.request.contextPath}${blogVo.logo}">
			</div>
		</div>

		<div id="navigation">
			<h2>카테고리</h2>
			
			<ul>
				<c:forEach items="${categoryList }" var="vo" varStatus="status">
					<li><a href="${pageContext.request.contextPath}/${blogVo.id}/${vo.no}">${vo.name }</a></li>				
				</c:forEach>
			</ul>
		</div>
		
		<c:import url="/WEB-INF/views/includes/blog-footer.jsp" />	
		
	</div>
</body>
</html>