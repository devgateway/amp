$('select.dynamic_select.parent').livequery(function() {

parentObject = $(this);
  
  child_element = parentObject.nextAll('select.dynamic_select.child')[0];
  if (!child_element) {
	  alert("JavaScript Error: Can't find child list!");
	  return false;
	} else {
	  parentObject.data('child_element', child_element)
	}
	
	
	grouped_options = [];
	groups = child_element.getElementsByTagName('optgroup');
	// This happens if the element has been parsed already
	if (groups.length < 1)
	  return true;
	
	for (i = 0; i < groups.length; i++) {
	  dupped_options = [];
	  options = groups[i].getElementsByTagName('option');
	  
	  for (k = 0; k < options.length; k++) {      
	    dupped_options[k] = options[k].cloneNode(true);
	    
	    // IE has a buggy implementation of cloneNode which causes option tags
	    // to lose their selected attribute. This hack is for applying it again.
	    if (options[k].getAttribute("selected")) {
	        dupped_options[k].setAttribute("selected", "selected");
	    }
	  }
	  
	  grouped_options[groups[i].label] = dupped_options;
	}
	parentObject.data('grouped_options', grouped_options);
	
	parentObject.change(function(e) {
	  selected = e.target.options[this.selectedIndex].text;
	  child_element = $(e.target).data('child_element');
	  child_options = $(e.target).data('grouped_options')[selected];
    oldval = child_element.value; // Save value
    
    // Clear list by removing all child nodes
    while (child_element.childNodes.length >= 1) {
      child_element.removeChild(child_element.firstChild);
    }
    
    // Reinsert options
    if (typeof(child_options) == 'undefined') {
      child_element.style.visibility = 'hidden';
    } else {
      child_element.style.visibility = 'visible';
    
      for (i = 0; i < child_options.length; i++) {
        child_element.appendChild(child_options[i]);
      }
    
      // Restore value
      child_element.value = oldval;
    }
	});
	
	
	// Manually fire event to initialize list
  parentObject.change();
});