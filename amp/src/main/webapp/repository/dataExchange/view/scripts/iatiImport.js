function changeSelLang(radio) {
	$("input[type=checkbox][name='selLanguages']:disabled").removeAttr('disabled');
	var checkBox = $("input[type=checkbox][name='selLanguages'][value='" + radio.value + "']");
	if (checkBox.length==1) {
		checkBox = checkBox.get(0);
		checkBox.checked = "checked";
		checkBox.disabled = true;
	}
}
function enableAllLang() {
	$("input[type=checkbox][name='selLanguages']:disabled").removeAttr('disabled');
}
