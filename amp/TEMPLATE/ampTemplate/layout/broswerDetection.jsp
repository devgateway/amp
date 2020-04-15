    <%@ taglib uri="/taglib/digijava" prefix="digi" %>
    <%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>

	<feature:display name="Detect browser" module="Login - User Management">
	<div class="modal fade" id="browserIncompatibleModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">Unsupported browser version</h4>
				</div>
				<div class="modal-body">
					<br/>
					<p><digi:trn>You are trying to access AMP with a non supported browser version</digi:trn>.</p>
					<p><digi:trn>AMP currently supports Internet Explorer 11, Microsoft Edge, Google Chrome and Firefox</digi:trn></p>
					<br/>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function() {
		detectBrowser();
	});
	function detectBrowser(){
		$.ajax({
		async: false,
		url: "/repository/aim/view/scripts/browserDetection/browser.js",
		dataType: "script"
		});
		var browserInfo = get_browser();
		var debug = '';
		debug += 'browser.name = ' + browserInfo.name + '\n\r';
		debug += 'browser.version = ' + browserInfo.version + '\n\r';
		alert(debug);
		if(browserInfo.name !== BROWSER_IE && browserInfo.name !== BROWSER_CHROME && browserInfo.name!=BROWSER_FIREFOX){
			$('#browserIncompatibleModal').modal();
		}else if(browserInfo.name === BROWSER_IE && browserInfo.version < 11){
			$('#browserIncompatibleModal').modal();
		}
	}
	</script>
		</feature:display>