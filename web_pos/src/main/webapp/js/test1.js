/**
 * 
 */
 
function changeBtnCss (obj, cName) {
		obj.className = cName;
}

function modalClose(obj) {
	const modal = document.querySelector(".modalBox");
	const modalCommand = document.querySelector(".modalCommand");
	modalCommand.innerText = "";
	modal.style.display = "none";
}
	
function accessOut() {
	/* submit()*/
	let form = document.getElementsByName("serverCall")[0];
	form.action = "Logout";
	form.submit();
}

function createInput(type, name, className, value, placeholder, isRead) {
	let obj = document.createElement("input");
	obj.setAttribute("type", type);
	if(name != null) obj.setAttribute("name", name)
	if(className != null) obj.setAttribute("class", className)
	if(value != null) obj.setAttribute("value", value)
	if(placeholder != null) obj.setAttribute("placeholder", placeholder)
	if(isRead != null) obj.setAttribute("readOnly", isRead)
	
	return obj;
}