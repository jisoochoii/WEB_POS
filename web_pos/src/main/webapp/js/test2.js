/**
 * server 요청
 -Get - data :  ?itemName=itemValue&itemName=itemValue
 -Post - data :  ?itemName=itemValue&itemName=itemValue
 		 data :  (itemName, itemValue) --> FormData
 		 
 Ajax
 :XmlHttpRequest
 ajax process
 1. XML 활성화
 	-new XMLHttpRequest();
 2. 콜백함수 설정 및 연결
 	콜 --> 전송방식(post | get), URL 설정, 비동기|동기 여부 설정
 	XHR.onreadystatechange = 콜백 함수 연결
 	  - readyState : 0 : .UNSET - 실행 대기
 	  			   : 1 : .OPENED - 서버에게 요청
 	  			   : 2 : .HEADERS_RECEIVED - 서버에서 처리중
 	  			   : 3 : .LOADING - 서버에서 오는중
 	  			   : 4 : .DONE - 통신종료
	  - STATUS	   : 200 : OK
	  			   : 400 : BAD REQUEST
	  			   : 500 : INTERNAL SERVER ERROR
	  
	  
Javascript Array
	: 가변사이즈 = 동적 할당
	: 자바의 ArrayList와 비슷
	
	let array_name = []; --> 1차원 배열 = 레코드
	let array_name2 = []; --> 1차원 배열 = 레코드
	
	**push : 입력-->
	array_name2.push(array_name); --> 2차원 배열 = 리스트
	
	**array address --> 수정
	**삭제 --> shift : 배열의 요소 중에 맨 앞에 있는 요소를 삭제 : index == 0
			  pop : 배열의 요소 중 맨 뒤에 있는 요소를 삭제 : index = length-1
			  splice : xmrwjd dnlcldml dythfmf tkrwp --> array.splice(index, 삭제하고자 하는 방의 개수)
	let 
 */

let list = [];
let items = ["order", "goCode", "goName", "goPrice", "goQtt", "goSum"]
let selectedIdx = -1;

 function searchGoods(event) {
	if (event.key != "Enter") return;

	let goodsCode = document.getElementsByName("goodsCode")[0];
	/* 바코드 유효성 검사: 입력 문자의 길이가 10자리 --> isCode(){} */
	if (isCode(goodsCode.value, 10)) {
		//ajax이용
		const jobCode = "SearchGo";
		const formData = goodsCode.name + "=" + goodsCode.value;
		//formData.append(goodsCode.name, goodsCode.value);
		sendAjax(jobCode, formData, "setOrderList");
	} else {
		alert("상품코드를 확인해주세요");
	}
	
	goodsCode.value = "";
}

function isCode (data, num) {
	return (data.length == num)? true:false;
}

function sendAjax (jobCode, formData, funcName) {

	const ajax = new XMLHttpRequest();
	ajax.open('POST', jobCode); //default 비동기
	//ajax시작 ajax.send(formData); application/json
	ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
	ajax.send(formData);

	ajax.onreadystatechange = function() {
		if (ajax.readyState == ajax.DONE && ajax.status == "200") {
			window[funcName](ajax.responseText);
			//setOrderList(ajax.responseText); //responseText = 서버로부터 넘어온 데이터
		}
	}
}

function setOrderList (data) {
	const search = document.getElementsByName("goodsCode")[0];
	let isCheck = false;
	
	if(data != "none"){
		if(data.length>0){
			/* 이미 등록된 상품이라면 수량만 증가 */
			if(list.length>0){
				const goodsCode = data.substring(0, data.indexOf(":"));
				isCheck = increaseQty(goodsCode);
			}
	
			if(!isCheck){
				let record = (data += (":1:0")).split(":");
				list.push(record);
				selectedIdx = list.length-1;
			}
			makeOrder();
		}
	}else{
		alert("상품코드를 확인해주세요");
		search.value = "";
		search.focus;
	}
	/*
	if (data.length>0) {
		let record = (data += (":1:0")).split(":");
		let type = [];
		
		if (isExist(record[0], type)[0]) {
			alert(record[4] + " : " + list[list.length-1][4]);
			if (record[4] == list[list.length-1][4]) {
				list[type[1]][3] = String(Number(list[type[1]][3])+1);
				alert("sef list : "+list[list.length-1][4]);
			}
		}
		list.push(record);
	
		selectedIdx = list.length-1;
		makeOrder();
	}*/
}

function increaseQty(goodsCode){
	let isCheck = false;
	for(recordIdx=0; recordIdx<list.length;recordIdx++){
		if(list[recordIdx][4] == 0){
			if(list[recordIdx][0] == goodsCode){
				list[recordIdx][3] = Number(list[recordIdx][3])+1;
				isCheck = true;
				break;
			}
		}
	}
	
	return isCheck;
}

function isExist (record, type) {
	for (idx = 0; idx < list.length; idx++) {
		if (list[idx][0] == record) {
			type[0] = true;
			type[1] = idx; 
			break;
		}
	}
	
	return type;
}

function makeOrder() {
	const lmlBody = document.getElementById("lmlBody");
	let showMoney = document.getElementById("showMoney");
	let amount = 0;
	
	lmlBody.style.borderBottom = "2px solid #489CFF";
	
	lmlBody.innerText = "";
	let recordNum = 0;
	for (recordIdx = 0; recordIdx < list.length; recordIdx++) {
		if(list[recordIdx][4] == "0") {
			recordNum++;
			const lmlContent = document.createElement("div");
			lmlContent.setAttribute("class", "lml-contents");
			lmlContent.setAttribute("id", "contents" + recordIdx);
			lmlContent.setAttribute("onClick", "selectIdx("+recordIdx+")");
			
			let record = [];
				
			for (idx = 0; idx < 6; idx++) {
				record.push(document.createElement("div"));
				record[idx].setAttribute("class", items[idx]);
				lmlContent.appendChild(record[idx]);
			}
			record[0].innerText = recordNum;
			record[1].innerText = list[recordIdx][0];
			record[2].innerText = list[recordIdx][1];
			record[3].innerText = Number(list[recordIdx][2]).toLocaleString('ko-KR');
			record[4].innerText = Number(list[recordIdx][3]).toLocaleString('ko-KR');
			record[5].innerText = (list[recordIdx][2] * list[recordIdx][3]).toLocaleString('ko-KR');
			
			amount += Number(list[recordIdx][2]) * Number(list[recordIdx][3]);
			
			lmlBody.appendChild(lmlContent);
			
			showMoney.innerText = amount.toLocaleString('ko-KR');
		}
	}
}

function selectIdx(idx) {
	selectedIdx = idx;
	const contents = document.getElementById("contents"+selectedIdx);
	contents.style.backgroundColor = "#B2CCFF";
}

function upDown (num) {
	list[selectedIdx][3] = Number(list[selectedIdx][3]) + num;
	
	if (list[selectedIdx][3] == 0) {
		list[selectedIdx][3] = Number(list[selectedIdx][3]) + 1;
		alert("1이하로 줄일 수 없습니다.")
	}
	
	makeOrder();
	const contents = document.getElementById("contents"+selectedIdx);
	contents.style.backgroundColor = "#B2CCFF";
}

function delRecord() {
	selectedIdx = (selectedIdx != -1)? selectedIdx : list.length-1;
	list.splice(selectedIdx, 1);
	makeOrder();
}

function modifyOrder () {
	let maxNumber = findMaxNumber();
	for (idx = 0; idx < list.length; idx++){
		if (list[idx][4] == 0) {
			list[idx][4] = maxNumber;
		}
	}
	
	makeOrder();
}

function findMaxNumber () {
	let maxNumber = 0;
	let showMoney = document.getElementById("showMoney");
	for (idx = 0; idx < list.length; idx++) {
		if (Number(list[idx][4]) > maxNumber) {
			maxNumber = Number(list[idx][4]);
		}
	}
	
	showMoney.innerText = "";
	return maxNumber + 1;
}

function modifyOrderList(title) {
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	
	if (list.length > 0) {
		modalTitle.innerHTML = title + "<span class=\"modalX\"> <input type = \"button\" value = \"X\" class = \"modalCL\" onClick=\"modalClose()\" /> </span>";
		if (list[list.length-1][4] != 0) {
			if (findMaxNumber() > 1) {
				let maxNumber = findMaxNumber();
				let html = "<table><tr><th>번호</th><th>내용</th></tr>";
				
				for (number = 1; number < maxNumber; number++) {
					let count = -1;
					let info;
					for (recordIdx = 0; recordIdx < list.length; recordIdx++) {
						if (Number(list[recordIdx][4]) == number) {
							count++;
							if(count==0){
								info = list[recordIdx][1];
							}
						}
					}
					info += (" 외" + count +"개의 품목");
					html += ("<tr onClick='moveOrderList(" + number +", "+ count + ")'><td>"+ number +"</td><td>"+ info +"</td></tr>"); 
				}
				html += "</table>"
				
				modalCommand.innerHTML = html;
			}
		} else {
			modalCommand.innerText = "현재 주문을 완료하셔야 목록을 불러옵니다.";
		}
		modal.style.display = "block";
	} else {
		alert("보류 목록이 없습니다.");
	}
}

function moveOrderList(number, count) {
	for (recordIdx = 0; recordIdx < list. length; recordIdx++){
		list[recordIdx][4] = (list[recordIdx][4] == number)? 0 :
			(list[recordIdx][4]>number)? list[recordIdx][4] -1 : list[recordIdx][4];
	}
	
	let recordIdx2 = -1;
	for (idx = list.length-1; idx >= 0; idx--) {
		if (list[idx][4] == 0) {
			recordIdx2 = idx;
			let record = list [idx];
			list.push(record);
		}
	}
	list.splice(recordIdx2, count+1);
	
	makeOrder();
	modalClose();
}

function orderComplete(){
	if(Number(document.getElementById("showMoney").innerText) != ""){
		let obj = document.getElementsByClassName("giveMoney")[0];
		obj.placeholder = "금액입력";
		obj.focus();	
	}else{
		alert("주문내역이 없습니다.");
	}
	
}

function setChange(value, event){
	if(event.key != "Enter") return;
	
	let amount = document.getElementById("showMoney").innerText.replace(",", "");
	
	
	let change = document.getElementById("changeMoney");
	change.innerText = (Number(value) - Number(amount)).toLocaleString();
}

function payment(){
	const modal = document.querySelector(".modalBox");
	modal.style.display = "none";
	let change = document.getElementById("changeMoney").innerText;
	let memCode = document.getElementsByName("memCode")[0].value;
	alert(change);
	if(change != "" && Number(change.replace(",","")) >= 0){
		const jobCode = "Orders";
		let formData = "";
		for(idx=0;idx<list.length;idx++) {
			if(list[idx][4]==0){
				formData += "goCode="+list[idx][0]+"&"+"qtt="+list[idx][3];
				if(idx!=list.length-1){
					formData+="&";
				}
			}
		}
		if(formData == "") return;
		if(memCode != "") {
			formData += "&mmCode="+memCode;
		}

		sendAjax(jobCode, formData, "completPayment");
	}else{
		alert("받은 금액 처리를 확인해 주세요");
		let obj = document.getElementById("money");
		obj.placeholder = "금액입력";
		obj.focus();
	}
	
}

function completPayment(data) {
	alert(data)
	if(data == "주문완료") {
		for(recordIdx = list.length-1; recordIdx >= 0; recordIdx--) {
			if (list[recordIdx][4] == 0) {
				list.splice(list[recordIdx], 1);
			}
		}
		modalClose();
		makeOrder();
		orderInit();
	}
}

function orderInit() {
	document.getElementById("showMoney").innerText = "";
	document.getElementById("inputMoney").value = "";
	document.getElementById("changeMoney").innerText = "";
}

function memberCheck (title) {
const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	
	modalTitle.innerHTML = title + "<div class='modalX' onclick='modalClose()'>X</div>";
	
	let html = "<div>회원코드를 입력하세요</div>";
	html += "<input type='text' name='memCode' placeholder='회원코드 입력'/>";
	html += "<input type='button' value='확인' onclick='payment()'/>"
	
	modalCommand.innerHTML = html;
	modal.style.display = "block";
}

function codeInput () {
	let modalCommand = document.querySelector(".modalCommand");
	
	modalCommand.innerHTML = "<div class = \"inputCode\"><input tpye = \"text\" class = \"inputMember\" onKeyup = \"codeCheck(value, event)\" placeholder = \"회원번호 입력 후 엔터\"/></div>";
}

function codeCheck (value, event) {
	if(event.key != "Enter") return;
	if(value.length != 5) {
		alert("회원코드를 확인해주세요. 회원 코드는 5자리 입니다.");
		codeInput();
	} else {
		payment(value);
	}
}