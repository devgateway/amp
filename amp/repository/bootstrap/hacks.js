// including-page-side hacks area
// http://stackoverflow.com/questions/9975810/make-iframe-automatically-adjust-height-according-to-the-contents-without-using
function resizeIframe(obj)
{
	//console.log("changing size of iframe from " + obj.style.height + " to " + obj.contentWindow.document.body.scrollHeight);
	//var iWantHeight = $(obj.contentWindow.document).find('#iframe-hack-end-marker').offset();
	//console.log("let's try " + iWantHeight.top);
	obj.style.height = obj.contentWindow.document.body.scrollHeight + 50 + 'px'; // +50 added for Firefox; TODO in the future: better hack or browser detection
};

function resizeIframeCallback(iframewnd, ignored, newHeight)
{
	$('iframe').each(function()
	{
		resizeIframe(this); // somewhat ugly - we resize all iframes when one changes size
	});
}

// iframe-side hacks area
var bootstrap_iframe_last_height = -1;
function bootstrap_parent_resizer()
{
	if (window.document.body.scrollHeight != bootstrap_iframe_last_height)
	{
		bootstrap_iframe_last_height = window.document.body.scrollHeight;
		window.parent.resizeIframeCallback(window, null /*$('#bootstrap_hack_iframe_id').val()*/, bootstrap_iframe_last_height);
	}
}

function bootstrap_iframe()
{
	setInterval(bootstrap_parent_resizer, 50);
}

$(document).ready(function()
{
		$(document).find('iframe').ready(function()
		{
			console.log('iframe ready!');
		});
});
