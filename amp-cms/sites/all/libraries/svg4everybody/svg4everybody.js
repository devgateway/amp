(function (document, uses, requestAnimationFrame, CACHE, IE9TO11) {
	if (IE9TO11) {
		onFrame();
	}

	function onFrame() {
		for (var i = 0; i < uses.length; i++) {
			processUseTag(uses[i]);
		}

		requestAnimationFrame(onFrame);
	}

	function processUseTag(useElement) {
		var
			once = useElement.getAttribute('data-svg4e'),
			url = useElement.getAttribute('xlink:href').split('#'),
			svgUrl = url[0],
			symbolId = url[1];

		// Only process the <use> tag once.
		if (once) {
			return;
		}
		useElement.setAttribute('data-svg4e', '1');

		if (svgUrl.length) {
			var xhr = CACHE[svgUrl] = CACHE[svgUrl] || new XMLHttpRequest();

			if (!xhr.s) {
				xhr.s = [];
				xhr.open('GET', svgUrl);
				xhr.onload = onSvgLoad;
				xhr.send();
			}

			// Set the source information.
			xhr.s.push({
				'id': symbolId, // new symbol element selector
				'el': useElement // original <use> element
			});

			if (xhr.readyState === 4) {
				xhr.onload();
			}
		}
	}

	function onSvgLoad() {
		var xhr = this, tempWrapper = document.createElement('div'), s = xhr.s;
		tempWrapper.innerHTML = xhr.responseText;

		s.splice(0).map(function (info) {
			var symbolSelector = info['id'].replace(/(\W)/g, '\\$1');
			var newElement = tempWrapper.querySelector('#' + symbolSelector);
			if (newElement) {
				embed(newElement, info['el']);
			}
		});
	};

	// Add the newly loaded symbol before the existing use element.
	function embed(loadedSymbolElement, useElement) {
		var
			parentElement = useElement.parentNode,
			newGroupElement = document.createElementNS('http://www.w3.org/2000/svg', 'g'),
			symbolClone = loadedSymbolElement.cloneNode(true);

		// Copy the viewBox from the original <symbol> element.
		if (symbolClone.attributes['viewBox']) {
			parentElement.setAttribute('viewBox', symbolClone.attributes['viewBox'].value);
		}

		// Copy the attributes from the loaded symbol element.
		while (symbolClone.childNodes.length) {
			newGroupElement.appendChild(symbolClone.childNodes[0]);
		}

		// Insert the new group in the right place.
		parentElement.insertBefore(newGroupElement, useElement);
		parentElement.removeChild(useElement);
	}
})(
	document,
	document.getElementsByTagName('use'),
	window.requestAnimationFrame || window.setTimeout,
	{},
	/Trident\/[567]\b/.test(navigator.userAgent) || (navigator.userAgent.match(/AppleWebKit\/(\d+)/) || [])[1] < 537
);
