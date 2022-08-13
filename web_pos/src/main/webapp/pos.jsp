<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/test1.js"></script>
<script type="text/javascript" src="js/test2.js"></script>
<script>
	function accessOut() {
		/* submit()*/
		let form = document.getElementsByName("serverCall")[0];
		form.action = "AccessOut";
		form.submit();
	}
</script>
<link rel="stylesheet" href="css/NewFile.css"/>
<style>
	.total {
		position:fixed; margin:0 auto;
		width : 95%; height: 82%;
		border : 1px solid #489CFF;
		margin-top:4rem;left:0;right:0; padding : 1%;
		text-align : center;
	}
	
	.bigLeft {
		float : left;
		width : 63%; height : 99%;
		border : 1px solid #489CFF;
		margin-left : 1%;
		background-color : #D9E5FF;
	}
	
	.left-top {
		width : 100; height : 7%;
		color : #FFFFFF; font-size : 28pt; font-weight: bold;
		background-color : #489CFF;
		margin : auto;
	}
	
	.left-middle-left {
		float : left;
		width : 70%; height : 80%;
		font-size : 12pt;
		background-color : #FFFFFF;
	}
	
	.lml-head {
		display : table;
		width : 100%;  height : 1.5rem;
		border-bottom : 2px solid #489CFF;
		background-color : #B2CCFF;
	}
	
	.lml-body {
		display : table;
		width : 100%; height : 1.5rem;
	}
	
	.lml-contents {
		display : table-row;
	}
				
	.order {
		display : table-cell;
		width : 10%;
		border-right : 1px solid #489CFF;
	}
	
	.goCode {
		display : table-cell;
		width : 12%;
		border-right : 1px solid #489CFF;
	}
	
	.goName {
		display : table-cell;
		width : 29%;
		border-right : 1px solid #489CFF;
	}
	
	.goPrice {
		display : table-cell;
		width : 15%;
		border-right : 1px solid #489CFF;
	}
	
	.goQtt {
		display : table-cell;
		width : 10%;
		border-right : 1px solid #489CFF;
	}
	
	.goSum {
		display : table-cell;
		width : 15%;
	}
	.left-middle-right {
		display : inline-block;
		width : 29.3%; height : 80%;
	}
	
	#up {
		float : left;
		color : #FFFFFF; font-size : 32pt;
		border : 1px solid #489CFF;
		background-color : #489CFF;
		margin-top : 16%; margin-left : 20%;
	}
	
	#down {
		margin-top : 16%;
		color : #FFFFFF; font-size : 32pt;
		float : inline-block;
		border : 1px solid #489CFF;
		background-color : #489CFF;
	}
	
	#delete, #hold, #list, #payment {
		width : 80%; height : 4rem;
		color : #FFFFFF; font-size : 24pt;
		margin-top : 16%;
		border : 1px solid #489CFF;
		background-color : #489CFF;
	}
	
	.mIndex {
		display : table;
	}
	
	.mHead, .mBody {
		display : talbe-row;
	}
	
	.mOrder, .mCon {
		display : table-cell;
	}
	
	#barcode {
		float : left;
		width : 60%;
		font-size : 27pt;
		margin-top : 2.5%; margin-left : 6%;
	}
	
	#search {
		display : inline-block;
		width : 20%; height : 3.5rem;
		font-size : 24pt; color : #FFFFFF;
		border : 1px solid #489CFF;
		background-color : #489CFF;
		margin-top : 2.5%; margin-right : 6%;
	}
	
	.left-down {
		position : relative;
		width : 100; height : 13%;
		left:0;right:0;bottom:0;
		background-color : #B2CCFF;
	}
	
	.smallRight {
		float : right;
		width : 33%; height : 99%;
		border : 1px solid #489CFF;
		margin-right : 1%;
		background-color : #D9E5FF;
	}
	
	#pInfo {
		width : 100%; height : 7%;
		color : #FFFFFF; font-size : 28pt; font-weight: bold;
		background-color : #489CFF;
	}
	
	#gMoney, #pMoney, #cMoney {
		width : 80%; height : 14%;
		font-size : 28pt; font-weight: bold;
		border : 1px solid #489CFF;
		margin : 10%;
		background-color : #FFFFFF;
	}
	
	.title {
		height : 50%;
		background-color : #489CFF;
		color : #FFFFFF;
	}
	
	.money {
		height : 50%;
		border : 1px solid #489CFF;
		background-color : #FFFFFF;
		padding : 0;
	}
	
	.giveMoney {
		vertical-align : middle;
		width : 99%; height : 70%;
		font-size : 28pt;
		border : #FFFFFF;
		padding : 0;
	}
	
	#cPayment {
		width : 80%;
		margin : 10%;
	}
	
	#completPayment {
		width : 100%;
		font-size : 28pt; font-weight: bold; color : #FFFFFF;
		border : #489CFF;
		background-color : #489CFF;
	}
</style>
</head>
<body>
<div id="header">
	<span class="top-span">${epName}(${levelName}) Last Access : ${accessTime}
		<input type="button" value="로그아웃" class="small-btn" onMouseOver="changeBtnCss(this,'small-btn-over')" 
		onMouseOut="changeBtnCss(this, 'small-btn')" onClick="accessOut('${hiddenData }')" />
	</span>	
</div>
<div class = "total">
	<div class = "bigLeft">
		<div class = "left-top">상 품 판 매 정 보</div>
		<div class = "left-middle-left">
			<div class = lml-head>
				<div class = "lml-contents">
					<div class = "order">순번</div>
					<div class = "goCode">코드</div>
					<div class = "goName">상품이름</div>
					<div class = "goPrice">개당가격</div>
					<div class = "goQtt">수량</div>
					<div class = "goSum">종목합계</div>
				</div>
			</div>
			<div id = "lmlBody" class = lml-body>
				
			</div>
		</div>
		<div class = "left-middle-right">
			<div class = "lmr-body">
				<input type = "button" id = "up" value = "▲" onClick = "upDown(1)"/>
				<input type = "button" id = "down" value = "▼" onClick = "upDown(-1)" />
				<input type = "button" id = "delete" value = "Delete" onClick = "delRecord()"/>
				<input type = "button" id = "hold" value = "주문 보류" onClick = "modifyOrder()" />
				<input type = "button" id = "list" value = "보류 목록" onClick = "modifyOrderList('보류목록')" />
				<input type = "button" id = "payment" value = "결제" onClick = "orderComplete()"/>
			</div>
		</div>
		<div class = "left-down">
			<input type = "text" id = "barcode" name = "goodsCode" onkeyup = "searchGoods(event)" placeholder = "바코드 입력" />
			<input type = "button" id = "search" value = "검색" />
		</div>
	</div>
	<div class = "smallRight">
		<div id = "pInfo">결 제 정 보</div>
		<div id = "gMoney">
			<div class = "title">주 문 금 액</div>
			<div id = "showMoney" class = "money"></div>
		</div>
		<div id = "pMoney">
			<div class = "title">받 은 금 액</div>
			<div id = "payMoney" class = "money"><input type = "text" id = "inputMoney" class = "giveMoney" onkeyup = "setChange(value, event)"/></div>
		</div>
		<div id = "cMoney">
			<div class = "title">거 스 름 돈</div>
			<div id = "changeMoney" class = "money"></div>
		</div>
		<div id = "cPayment"><input type = "button" id = "completPayment" value = "결 제 완 료" onClick = "memberCheck('회원 체크')"/></div>
	</div>
</div>
<div class="modalBox">
	<div class="modalBody">
		<div class="modalTitle">
		</div>
		<div class="modalCommand"></div>	
	</div>
</div>
<div id="footer"></div>
	<form  method = "post" name= "serverCall" ></form>
</body>
</html>