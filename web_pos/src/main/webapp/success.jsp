<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/test1.js"></script>
<script>
	function loginOut() {
		/* submit()*/
		let form = document.getElementsByName("serverCall")[0];
		form.action = "Logout";
		form.submit();
	}

	function GoPage(level, action) {
		let form = document.getElementsByName("serverCall")[0];
		alert(action);
		switch(action){
		case "GoMgr" :
			form.action = "GoMgr";
			form.submit();
			break;
		case "GoPos" :
			form.action = "GoPos";
			form.submit();
			break;
		case "goStatistics" :
			form.action = "goStatistics";
			form.submit();
			break;
		}
	}
	function getMessage(message) {
		if(message != "") {
			alert(message);
		}
	}
</script>
<link rel="stylesheet" href="css/NewFile.css"/>
<style>
#outline {
	position : absolute; top : 50%; left : 50%; transform : translate(-45%, -45%);
	width : 50%;
	height : 300px;
	padding : 10px;
}
.goBtn {
	float : left;
	width : 27%;
	height : 60%;
	font-size : 25pt;
	text-align : center;
	line-height : 180px;
	color : #489CFF;
	margin : 10px;
	border : 3px solid #489CFF;
	cursor : pointer;
}
.goBtn-over {
	float : left;
	width : 27%;
	height : 60%;
	font-size : 25pt;
	text-align : center;
	line-height : 180px;
	color : #FFFFFF;
	background-color : #489CFF;
	margin : 10px;
	border : 3px solid #FFFFFF;
	cursor : pointer;
}
</style>
</head>
<body onLoad = "getMessage('${param.message}')">
	<div id="header">
		<span class="top-span">${epName}(${levelName}) Last login : ${loginTime}
			<input type="button" value="로그아웃" class="small-btn" onMouseOver="changeBtnCss(this,'small-btn-over')" 
			onMouseOut="changeBtnCss(this, 'small-btn')" onClick="loginOut()" />
		</span>
	</div>
	${btn}
	<form  method = "post" name= "serverCall" ></form>
</body>
</html>