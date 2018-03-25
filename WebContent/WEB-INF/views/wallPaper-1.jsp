<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<title>Insert title here</title>
<style type="text/css">
td {
	text-align: center;
	vertical-align: middle;
	width: 150px;
	height: 50px;
}

.tab {
	width: 450px;
	margin: 0 auto;
}

.topbar-wrap {
	height: 50px;
	width: 450px;
	margin: 0 auto;
	position: relative;
}

.nav {
	
}

.nav ul {
	text-align: center;
	height: 100%;
	width: 100%;
	list-style: none;
}

.nav li {
	display: inline-block;
}

.nav ul li a {
	text-decoration: none;
	font-size: 18px;
	color: black;
	text-align: center;
	line-height: 50px;
	margin: 0 5px;
}
</style>
</head>
<body>

	<div class="tab">
		<table width="" height="" border="1" cellspacing="0" align="center">
			<c:forEach var="singleUnit" items="${words}">
				<tr>
					<td><c:out value="${singleUnit.id}"></c:out></td>
					<td><c:out value="${singleUnit.url}"></c:out></td>
					<td><img alt=""
						src=".${fn:substring(singleUnit.url,51,fn:length(singleUnit.url))}" /></td>


				</tr>
			</c:forEach>
		</table>
	</div>
	<div class="topbar-wrap">
		<div class="nav">
			<ul>
				<li><c:if test="${ total>=pagesize+1 }">
						<a href="fotuQuery?id=1">首页</a>
					</c:if></li>
				<li><c:if test="${ total>=pagesize+1 }">
						<a href="fotuQuery?id=${total-pagesize }">前7页</a>
					</c:if></li>

				<c:choose>

					<c:when test="${ total==sortPage }">
						<c:set var="max" value="${maxPage}"></c:set>
						<c:forEach begin="${total }" end="${max}" var="i">
							<li><a href="fotuQuery?id=${i}"><c:out value="${i }">

									</c:out></a></li>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:forEach begin="${total }" end="${max}" var="i">
							<li><a href="fotuQuery?id=${i}"><c:out value="${i }">
									</c:out></a></li>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				<c:if test="${ total<sortPage }">
					<li><a href="fotuQuery?id=${total+pagesize}">后7页</a></li>
				</c:if>
				<c:if test="${ total<sortPage }">
					<li><a href="fotuQuery?id=${maxPage}">尾页</a></li>
				</c:if>
			</ul>
		</div>
	</div>

</body>
</html>
