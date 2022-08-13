<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<script>
function loginCtl(){
	/*storeCode.length == 10 인지 아닌지의 검사를 script에서 해야함*/
	const loginInfo=[]; /*배열 선언,html에서 배열은 무조건 가변배열/const는 밸류값을 수정할수없음-->*/
	loginInfo.push(document.getElementsByName("epCode"));
	loginInfo.push(document.getElementsByName("password")); 
	/*순차적접근은 push가 편함*/
	
	/*employeeCode.length == 3 */
	if(loginInfo[0][0].value.length != 5){
		alert("직원코드는 5자리 입니다.");
		return;
	}
	/* Server 요청 : request << form */                                                                          
	const form = document.getElementsByName("loginForm")[0];
	form.submit();
	/*submit 하게되면 form의 action에서 "login"와 method에서 "post"를 가지고 request에 담겨 FrontController로 가게됨*/
	
	/*storeCode[] = {name,value}
    name으로 연결할땐 무조건 배열에 들어가기때문에 [] 생각하기*/
}

function getMessage(message) {
	if(message != "") {
		alert(message);
	}
}

</script>
</head>
<body onLoad = "getMessage('${param.message}')">
	<h1>welcome to team6 homepage mk.2</h1>
	<form name="loginForm" action="Login" method="post">
		<input type="text" name = "epCode" placeholder="epCode" />
		<input type="password" name = "password" placeholder="password" />
		<input type="button" value="Login" onclick = "loginCtl()"/>
	</form>
</body>
</html>