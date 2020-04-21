<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/jquery/jquery-3.4.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/ejs/ejs.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
var mode = false;

var listTemplate = new EJS({
	url: "${pageContext.request.contextPath }/assets/js/ejs/list-template.ejs"
});

var listItemTemplate = new EJS({
	url: "${pageContext.request.contextPath }/assets/js/ejs/list-item-template.ejs"
});

var fetchList = function(){
	$.ajax({
		url: '${pageContext.request.contextPath }/${authUser.id}/api/blog/list',
		async: true,
		type: 'get',
		dataType: 'json',
		data: '',
		success: function(response){
			
			if(response.result != "success") {
				console.error(response.message);
				return;
			}
			
			// rendering
			response.contextPath = "${pageContext.request.contextPath }";
			var html = listTemplate.render(response);
			$(".admin-cat").append(html);
			
		},
		error: function(xhr, status, e){
			console.error(status + ":" + e);
		}
	});
}


$(function(){		
	// 카테고리 삽입
	$('#add-form').submit(function(event){
		event.preventDefault();
		var vo = {};
		vo.name = $("#name").val();
		vo.description = $("#description").val();
		vo.id = "${authUser.id}";
		
		$.ajax({
			url: '${pageContext.request.contextPath }/${authUser.id}/api/blog/add',
			async: true,
			type: 'post',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(vo),
			success: function(response){
				if(response.result != "success") {
					console.error(response.message);
					return;
				}
				
				// rendering			
				response.data.contextPath = "${pageContext.request.contextPath }";
				response.data.listCnt = $('.admin-cat tr').length;
				
				var html = listItemTemplate.render(response.data);
				$(".admin-cat tbody").after(html);
				
				// form reset
				$("#add-form")[0].reset();	// 유사배열(= get(0))
			},
			error: function(xhr, status, e){
				console.error(status + ":" + e);
			}
			
		});
			
	});
	
	// 카테고리 삭제
	$(document).on('click', '.admin-cat tr td a', function(event){
		event.preventDefault();
		var no = $(this).data('no');
		$(this).parents('tr').remove();
		
		$.ajax({
			url: '${pageContext.request.contextPath }/${authUser.id}/api/blog/delete/' + no,
			async: true,
			type: 'delete',
			dataType: 'json',

			success: function(response){
				var j = $('.admin-cat tr').length-1;
				console.log("j 1 : " + j);
				
				for(var i = 0; i < j; i++){
					
					console.log(i);
					//$('.admin-cat tr:nth-child(1) td').text =1000;
					console.log("j2 :" + j);
					console.log($('.admin-cat tr td:nth-child(1)')[i]);
						$('.admin-cat tr td:nth-child(1)')[i].innerText = j-i;
						//j--;
						

				};
				
				if(response.result != "success") {
					console.error(response.message);
					return;
				}
				
				$(".admin-cat tr[data-no=" + no + "]").remove();
			},
			error: function(xhr, status, e){
				console.error(status + ":" + e);
			}
		});	
		
		
	});	
		
	// 처음 리스트 가져오기
	fetchList();

});

</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/blog-header.jsp" />
		<div id="wrapper">
			<div id="content" class="full-screen">
				<ul class="admin-menu">
					<li><a href="${pageContext.request.contextPath}/${authUser.id}/admin/basic">기본설정</a></li>
					<li><a href="${pageContext.request.contextPath }/${authUser.id}/admin/category">카테고리</a></li>
					<li class="selected">카테고리(Ajax)</li>
					<li><a href="${pageContext.request.contextPath}/${authUser.id}/admin/write">글작성</a></li>
				</ul>
		      	<table class="admin-cat">
		      		<tr id='menu-title'>
		      			<th>번호</th>
		      			<th>카테고리명</th>
		      			<th>포스트 수</th>
		      			<th>설명</th>
		      			<th>삭제</th>      			
		      		</tr>												  					  
				</table>
      	
      			<h4 class="n-c">새로운 카테고리 추가</h4>
      			<form action="" id="add-form"  method="post">
	      			<table id="admin-cat-add">
			      		<tr>
			      			<td class="t">카테고리명</td>
			      			<td><input type="text" name="name" id="name"></td>
			      		</tr>
			      		<tr>
			      			<td class="t">설명</td>
			      			<td><input type="text" name="description" id="description"></td>
			      		</tr>
			      		<tr>
			      			<td class="s">&nbsp;</td>
			      			<td><input type="submit" value="카테고리 추가"></td>
			      		</tr>      		      		
			      	</table> 
      			</form>

			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/blog-footer.jsp" />	
	</div>
</body>
</html>