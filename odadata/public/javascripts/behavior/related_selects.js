grouped_options_store = new Array();

function initDynamicSelects() {
	parent_lists = $('select.dynamic_select.parent');
	child_lists = $('select.dynamic_select.child');
	
	for (n = 0; n < parent_lists.length; n++) {
		grouped_options_store[n] = new Array();
		grouped_options = grouped_options_store[n];
		
		parent_list = parent_lists[n];
		child_list = child_lists[n];
		if (!child_list) {
			alert("JavaScript Error: Can't find child list!");
			return false;
		}
				
		// Set rel attribute of parent list to child list index
		parent_list.setAttribute('rel', n);
		
		groups = child_list.getElementsByTagName('optgroup');
		// This happens if the element has been parsed already
		if (groups.length < 1)
			continue;
		
		for (i = 0; i < groups.length; i++) {
			grouped_options[i] = [groups[i].label, new Array()];
			options = groups[i].getElementsByTagName('option');
			for (k = 0; k < options.length; k++) {	    
				grouped_options[i][1][k] = options[k].cloneNode(true);
				
				// IE has a buggy implementation of cloneNode which causes option tags
				// to loose there selected attribute. This hack is for applying it again.
				if (options[k].getAttribute("selected")) {
				    grouped_options[i][1][k].setAttribute("selected", "selected");
				}
			}
		}
		
		parent_list.onchange = function(e) {
			e = e || window.event;
			target = e.target || e.srcElement;
			
			idx = parseInt(target.getAttribute('rel'));
			child_list = child_lists[idx];
			
			// Save value
			oldval = child_list.value;
			
			// Clear list by removing all child nodes
			while (child_list.childNodes.length >= 1) {
				child_list.removeChild(child_list.firstChild);
			}

			
			// Get related options
			var options = new Array;
			for (i = 0; i < grouped_options_store[idx].length; i++) {
				if (grouped_options_store[idx][i][0] == target.options[target.selectedIndex].text)
					options = grouped_options_store[idx][i][1];					
			}
			
			// Reinsert options
			if (options.length < 1) {
				child_list.style.visibility = 'hidden';
			} else {
				child_list.style.visibility = 'visible';
				for (i = 0; i < options.length; i++) {
					child_list.appendChild(options[i]);
				}
				// Restore value
				child_list.value = oldval;
			}
		}
		
		// Manually fire event to initialize list
		jQuery(parent_list).change();
	}
}

$(document).ready(function() {
	initDynamicSelects();
});