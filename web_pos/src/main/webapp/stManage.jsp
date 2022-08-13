<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/test1.js"></script>
<script>
	function changePassword(data) {
		const modal = document.querySelector(".modalBox");
		let modalTitle = document.querySelector(".modalTitle");
		let modalCommand = document.querySelector(".modalCommand");
		
		modalTitle.innerHTML = "직원 비밀번호 변경" + "<span class=\"modalX\"> <input type = \"button\" value = \"X\" class = \"modalCL\" onClick=\"modalClose()\" /> </span>";
		
		let info = data.split(":");
		
		let table = "<table><tr><th>직원코드</th><th>직원이름</th><th>직원등급</th></tr>";
		table += "<tr><td>" + info[0] + "</td><td>" + info[1] + "</td><td>" +info[2] + "</td><tr></table>";
		
		modalCommand.innerHTML = table;
		
		let obj = [];
		obj[0] = createInput("password", "password", "box", null, "변경 할 패스워드", null);
		modalCommand.appendChild(obj[0]);
		obj[1] = createInput("hidden", "epCode", "box", info[0], null, null);
		modalCommand.appendChild(obj[1]);

		modalCommand.appendChild(document.createElement("br"));
		
		let objBtn = createInput("button", null, "sub-btn", "취소", null, null);
		modalCommand.appendChild(objBtn);
		objBtn = createInput("button", null, "sub-btn", "업데이트", null, null);
		objBtn.addEventListener("click", function() {
			serverTransfer(obj, "UpdPass");
		});
		modalCommand.appendChild(objBtn);
		
		modal.style.display = "block";
	}
	
	function serverTransfer(obj, action) {
		let form = document.getElementsByName("serverCall")[0];
		form.action = action;

		for (idx = 0; idx < obj.length; idx++) {
			if (obj[idx].value.length <= 0) {
				alert("입력값 없음");
				return;
			}
			form.appendChild(obj[idx]);
		}
		form.submit();
	}

	function changeLevel(data) {
		const modal = document.querySelector(".modalBox");
		let modalTitle = document.querySelector(".modalTitle");
		let modalCommand = document.querySelector(".modalCommand");

		modalTitle.innerHTML = "직원 정보 변경"
				+ "<span class=\"modalX\"> <input type = \"button\" value = \"X\" class = \"modalCL\" onClick=\"modalClose()\" /> </span>";
		let info = data.split(":");

		let table = "<table><tr><th>직원코드</th><th>직원이름</th><th>직원등급</th></tr>";
		table += "<tr><td>" + info[0] + "</td><td>" + info[1] + "</td><td>"
				+ info[2] + "</td><tr></table>";

		modalCommand.innerHTML = table;

		let obj =[];
		obj[0] = createInput("text", "epLevel", "box", null, "직원 등급 조정", null);
		modalCommand.appendChild(obj[0]);
		obj[1] = createInput("hidden", "epCode", "box", info[0], null, null);
		modalCommand.appendChild(obj[1]);

		modalCommand.appendChild(document.createElement("br"));

		let objBtn = createInput("button", null, "sub-btn", "취소", null, null);
		modalCommand.appendChild(objBtn);
		objBtn = createInput("button", null, "sub-btn", "업데이트", null, null);
		objBtn.addEventListener("click", function() {
			serverTransfer(obj, "UpdLevel");
		});
		modalCommand.appendChild(objBtn);

		modal.style.display = "block";
	}


	function changeInfo(data) {
		let item = ["goodsCode", "goodsName", "goodsCost", "goodsPrice", "goodsStock", "categoryName"];
		const modal = document.querySelector(".modalBox");
		let modalTitle = document.querySelector(".modalTitle");
		let modalCommand = document.querySelector(".modalCommand");

		modalTitle.innerHTML = "상품 정보 변경"
				+ "<span class=\"modalX\"> <input type = \"button\" value = \"X\" class = \"modalCL\" onClick=\"modalClose()\" /> </span>";
		let info = data.split(":");
		
		let table = "<table class='goTable'><tr><th>상품코드</th><th>상품이름</th><th>구매비용</th><th>판매가격</th><th>재고</th><th>상품분류</th></tr>";
		table += "<tr class='goTr'>";
		for(idx=0; idx<info.length; idx++){
			table += "<td>";
			table += ("<input type='text' class='goInput' name='" + item[idx] + "' value='" + info[idx] + "' " + ((idx > 0 && idx < info.length-1)? "/>":"readOnly />"));
			table += "</td>";
		}
		table += "</tr></table>";
		
		modalCommand.innerHTML = table;
		
		/* HTML Object 추가 :  JS영역에 개체 생성 후 --> HTML 영역에 추가 : appendChild() */
		let objBtn = createInput("button", null, "sub-btn", "업데이트", null, null);
		// 동적으로 생성된 개체에 이벤트 추가  addEventListener(event, function)
		objBtn.addEventListener("click", function(){
			transfer(item);
		});
		modalCommand.appendChild(objBtn);
		
		objBtn = createInput("button", null, "sub-btn", "초기화", null, null);
		modalCommand.appendChild(objBtn);
		
		modal.style.display = "block";
	}
	
	function transfer(item, action) {
		let form = document.getElementsByName("serverCall")[0];
		form.setAttribute("action", action);
		for (idx = 0; idx<item.length; idx++) {
			form.appendChild(document.getElementsByName(item[idx])[0]);
		}
		form.submit();
	}
	
	function insertGo() {
		let item = ["goodsCode", "goodsName", "goodsCost", "goodsPrice", "goodsStock", "categoryCode"];
		const modal = document.querySelector(".modalBox");
		let modalTitle = document.querySelector(".modalTitle");
		let modalCommand = document.querySelector(".modalCommand");

		modalTitle.innerHTML = "상품 추가"
				+ "<span class=\"modalX\"> <input type = \"button\" value = \"X\" class = \"modalCL\" onClick=\"modalClose()\" /> </span>";
				
		let table = "<table class='goTable'><tr><th>상품 코드</th><th>상품 이름</th><th>구매 비용</th><th>판매 가격</th><th>재고</th><th>분류 코드</th></tr>";
		table += "<tr class='goTr'>";
		for(idx=0; idx<item.length; idx++){
			table += "<td>";
			table += ("<input type='text' class='goInput' name='" + item[idx] + "' value=''/>");
			table += "</td>";
		}
		table += "</tr></table><br/><div>상품코드는 기본 코드에 대표 아이디(00006)+분류 코드를 앞에 추가해서 입력</div><br/><div>상품 분류 코드는 아래와 같음</div><br/>";
		
		table += "<table class = 'CGICODE'><tr><th>음료</th><th>농축산물</th><th>과자류</th><th>식자재</th><th>인스턴트</th></tr>";
		table += "<tr><td>GB</td><td>GF</td><td>GS</td><td>GG</td><td>GI</td><tr></table>"
		
		modalCommand.innerHTML = table;
		
		/* HTML Object 추가 :  JS영역에 개체 생성 후 --> HTML 영역에 추가 : appendChild() */
		let objBtn = createInput("button", null, "sub-btn", "추가", null, null);
		// 동적으로 생성된 개체에 이벤트 추가  addEventListener(event, function)
		objBtn.addEventListener("click", function(){
			transfer(item, "RegGo");
		});
		modalCommand.appendChild(objBtn);
		
		objBtn = createInput("button", null, "sub-btn", "초기화", null, null);
		modalCommand.appendChild(objBtn);
		
		modal.style.display = "block";
	}

	function insertEp(title){
		const modal = document.querySelector(".modalBox");
		let modalTitle = document.querySelector(".modalTitle");
		let modalCommand = document.querySelector(".modalCommand");
		
		modalTitle.innerHTML = title + "<div class=\'closeBox\' onClick=\'modalClose()\'> X</div>";
	
		/* Table 추가 */
		let table = "<table><tr><th>직원코드</th><th>직원이름</th><th>직원등급</th><th>비밀번호</th></tr>";

		modalCommand.innerHTML = table;
		
		/* HTML Object 추가 : JS영역에 개체 생성 수 -> HTML영역에 추가 : appendChild()*/
		let obj = [];
		obj[0]= createInput("text", "epCode", "box", null, "직원코드", null);
		modalCommand.appendChild(obj[0]);
		obj[1]= createInput("text", "epName", "box", null, "직원이름", null);
		modalCommand.appendChild(obj[1]);
		obj[2]= createInput("text", "epLevel", "box", null, "직원등급", null);
		modalCommand.appendChild(obj[2]);
		obj[3] = createInput("password", "epPassword", "box", null, "비밀번호", null);
		modalCommand.appendChild(obj[3]);
	
		/* 줄바꿈 */
		modalCommand.appendChild(document.createElement("br"));
		
		/* HTML Object 추가 :  JS영역에 개체 생성 후 --> HTML 영역에 추가 : appendChild() */
		let objBtn = createInput("button", null, "small-btn", "업데이트", null, null);
		objBtn.addEventListener("click", function(){
			serverTransfer(obj, "RegEp");
		});
		modalCommand.appendChild(objBtn);
		modal.style.display = "block";
	}
</script>
<link rel="stylesheet" href="css/NewFile.css"/>
<style>
	.total {
		position:fixed; margin:0 auto;
		width:99%; height:92%;
		top:4.3rem;left:0;right:0; padding-left:0.5%;
		text-align:center;
	}
	
	.epInfo, .mmInfo {
		float: left;
		width: 30%;
		height: 90%;
		border: 3px solid #489CFF;
		margin: 0.5%;
		margin-top: 10px;
	}
	
	.goInfo {
		float: left;
		width: 35%;
		height: 90%;
		border: 3px solid #489CFF;
		margin: 0.5%;
		margin-top: 10px;
		overflow:auto;
	}
	
	.title {
		background-color : #489CFF; color : #FFFFFF;
		font-size: 18pt;
		font-weight: bold;
	}
	
	.left, .middle, .right {
		display: table;
		width: 100%;
		overflow:auto;
	}
	
	.epMenu, .mmMenu, .goMenu {
		display: table-header-group;
		width: 100%;
		border: 1px solid;
		table-layout: fixed;
		font-weight: bold;
	}
	
	.epContent, .mmContent, .goContent {
		display:table-row-group;
		height:1.5rem;
	}
	
	.epCode, .epName, .epLevel, .password, .name, .memberCode, .memberName, .memberPhone, .changePhone, .goodsCode, .goodsName, .caName, .goodsCost, .goodsPrice, .goodsStock, .changeInfo {
		display: table-cell;
		border: 1px solid #489CFF;
	}
	
	.btn {
		display: table-cell;
		border: 1px solid #489CFF;
		background-color : #FFFFFF; color : #489CFF;
		border : 3px solid #FFFFFF;
		font-weight : 900;
	}
	
	.btn-over {
		display: table-cell;
		border: 1px solid #489CFF;
		background-color : #489CFF; color : #FFFFFF;
		border : 3px solid #489CFF;
		font-weight : 900;
	}
	
	table {
		width : 100%;
	    margin : 0 auto;
	    text-align : center;
	}
	
	.sub-btn {
		border: 1px solid #489CFF;
		background-color : #FFFFFF; color : #489CFF;
		border : 3px solid #FFFFFF;
		font-weight : 900;
	    text-align : center;
	}
	
	.goInput {
		width : 90%;
		text-align : center;
	}

	.insContent{
		transform : translate(900%);
		width : 5%;
		font-size: 18pt;
		cursor : pointer;
	}
	
</style>
</head>
<body>
	<div id="header">
		<span class="top-span">${epName}(${levelName}) Last Access : ${loginTime}
			<input type="button" value="로그아웃" class="small-btn" onMouseOver="changeBtnCss(this,'small-btn-over')" 
			onMouseOut="changeBtnCss(this, 'small-btn')" onClick="accessOut()" />
		</span>	
	</div>	
	<div class = "total">
		<div class="epInfo">
			<div class="title">직원정보</div>
			${left}
		</div>
		<div class="mmInfo">
			<div class="title">회원정보</div>
			${middle}
		</div>
		<div class="goInfo">
			<div class="title">상품정보</div>
			${right}
		</div>
	</div>
	<div id="footer"></div>
	<div class="modalBox">
		<div class="modalBody">
			<div class="modalTitle">
			</div>
			<div class="modalCommand"></div>	
		</div>
	</div>
	<form  method = "post" name= "serverCall" ></form>
</body>
</html>