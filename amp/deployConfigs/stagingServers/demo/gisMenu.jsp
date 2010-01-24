	function openGIS() {
		openNewWindowWithMenubar(1000, 600);
		popupPointer.location.href = "https://files.ampdev.net/client/public/rm/result_matrix_<%=((org.digijava.kernel.entity.Locale)request.getAttribute("org.digijava.kernel.navigation_language")).getCode()%>.pdf";
	}
