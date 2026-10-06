		/**
		* The MyDragAndDropObject class
		*/
		function MyDragAndDropObject ( containerId, destContainerId ) {
			//alert('parentConstructor: ' + containerId);
			this.containerId		= containerId;
			this.destContainerId	= destContainerId;
		}
		
			MyDragAndDropObject.prototype.onDragOver	= function (e, tId) {
				var destObj			= document.getElementById(tId);
				var srcObj			= document.getElementById(this.id);
				
				if ( ! this.checkDestination(destObj) )
					return;
				if (destObj.nodeName.toLowerCase()=="li") {
					if ( this.goingUp ) {
						destObj.parentNode.insertBefore(srcObj, destObj);
					}
					else
						destObj.parentNode.insertBefore(srcObj, destObj.nextSibling);
// 					alert(destObj.parentNode.id);
				}
				if (destObj.nodeName.toLowerCase()=="ul" || destObj.nodeName.toLowerCase()=="ol") {
					if ( srcObj.parentNode != destObj ) {
						destObj.appendChild( srcObj );
					}
				}
			};
			MyDragAndDropObject.prototype.startDrag		= function(x, y) {
				( new YAHOO.util.Element(this.getDragEl()) ).addClass("list1");
				var realObj	= this.getEl();
				var dragObj	= this.getDragEl();
				dragObj.innerHTML	= realObj.innerHTML;
			};
			MyDragAndDropObject.prototype.endDrag		= function(e) {
				var realObj	= this.getEl();
				var dragObj	= this.getDragEl();
				MyDragAndDropObject.prototype.endAnimation(realObj, dragObj);
				if (typeof repManager != 'undefined') {
					repManager.checkSteps();
				}
				
			};
			MyDragAndDropObject.prototype.onDrag		= function(e) {
				//alert('onDrag');
		        // Keep track of the direction of the drag for use during onDragOver
				var y = YAHOO.util.Event.getPageY(e);
// 				alert(this.lastY + " -- " + y);
				if (y < this.lastY) {
					this.goingUp = true;
				} else if (y > this.lastY) {
					this.goingUp = false;
				}
				this.lastY = y;
			};
			
			MyDragAndDropObject.prototype.createDragAndDropItems	= function() {
				var src_container	= document.getElementById( this.containerId );
				var i=0;
				for (i=0; i<src_container.childNodes.length; i++) {
					if (src_container.childNodes[i].nodeName.toLowerCase()=="li") {
						var elId		= src_container.childNodes[i].id;
						if (elId != null) {
							var draggableItem	= new YAHOO.util.DDProxy(elId);
							draggableItem		= this.addActions(draggableItem, elId);
						}
					}
				}
			};
			
		MyDragAndDropObject.prototype.endAnimation		= function(realObj, dragObj) {
				
				if ( YAHOO.util.Dom.getX( realObj ) == null )
						return;
		
				YAHOO.util.Dom.setStyle(dragObj, "visibility", ""); 
				var a = new YAHOO.util.Motion(  
					dragObj, {  
						points: {  
							to: YAHOO.util.Dom.getXY(realObj) 
						} 
					},  
					0.5,
					YAHOO.util.Easing.easeOut  
				);
				
				a.onComplete.subscribe(function() { 
					YAHOO.util.Dom.setStyle(dragObj.id, "visibility", "hidden"); 
					YAHOO.util.Dom.setStyle(realObj.id, "visibility", ""); 
				}); 
				a.animate();
		};
		
		MyDragAndDropObject.prototype.addActions	= function (obj) {
		
			obj.lastY				= 0;
			obj.goingUp				= false;
			obj.onDragOver			= this.onDragOver;
			obj.startDrag			= this.startDrag;
			obj.endDrag				= this.endDrag;
			obj.onDrag				= this.onDrag;
			obj.checkDestination	= this.checkDestination;
			obj.containerId			= this.containerId;
			obj.destContainerId		= this.destContainerId;
		};
		
		MyDragAndDropObject.prototype.checkDestination			= function (destObj) {
			var tempObj			= destObj;

			while ( tempObj != null && (tempObj.nodeName.toLowerCase()!="ul" && tempObj.nodeName.toLowerCase()!="ol" && tempObj.nodeName.toLowerCase()!="div" ) ) {
				if ( tempObj.nodeName.toLowerCase()!="div" && tempObj.id == this.containerId)
					break;
				tempObj		= tempObj.parentNode;
			}
			if (tempObj == null || (tempObj.id != this.destContainerId && tempObj.id != this.containerId ) )
				return false;
			
			return true;
		};
		
		MyDragAndDropObject.selectObj			= function (ddObj, srcObj, destObj) {
			destObj.appendChild( srcObj );
		};
		MyDragAndDropObject.deselectObj			= MyDragAndDropObject.selectObj;
		MyDragAndDropObject.selectObjs			= function (srcId, destId) {
			var srcEl		= document.getElementById(srcId);
			var destEl		= document.getElementById(destId);
			
			var inputEls	= srcEl.getElementsByTagName("input");
			for (var i=0; i<inputEls.length; i++) {
				if ( inputEls[i].checked ) {
					inputEls[i].checked	= false;
					MyDragAndDropObject.selectObj(null, inputEls[i].parentNode, destEl);
					i=i-1;
				}
			}
			if (typeof repManager != 'undefined') {
				repManager.checkSteps();
			}
			return false;
		};
		MyDragAndDropObject.deselectObjs		= MyDragAndDropObject.selectObjs;
		
		MyDragAndDropObject.selectObjsByDbId	= function (srcId, destId, arrayOfIds) {
			var srcEl		= document.getElementById(srcId);
			var destEl		= document.getElementById(destId);
			
			for (var j=0; j<arrayOfIds.length; j++) {
				var inputEls	= srcEl.getElementsByTagName("input");
				for (var i=0; i<inputEls.length; i++) {
					if ( inputEls[i].value == arrayOfIds[j] ) {
						MyDragAndDropObject.selectObj(null, inputEls[i].parentNode, destEl);
					}
				}
			}
			if (typeof repManager != 'undefined') {
				repManager.checkSteps();
			}
			return false;
		};
		
		/**
		* The ColumnsDragAndDropObject class extends MyDragAndDropObject
		*/
		
		ColumnsDragAndDropObject.prototype				= new MyDragAndDropObject();
		ColumnsDragAndDropObject.prototype.parent		= MyDragAndDropObject;
		ColumnsDragAndDropObject.prototype.constructor	= ColumnsDragAndDropObject;
		function ColumnsDragAndDropObject (containerId, destContainerId) {
			//alert('childConstructor:' + containerId);
			this.parent.call(this, containerId, destContainerId);
			this.realWidth		= "29%";
		}
			ColumnsDragAndDropObject.prototype.createDragAndDropItems	= function () {
				new YAHOO.util.DDTarget("dest_col_ul");
				new YAHOO.util.DDTarget("source_col_div");
				
				var divObj		= document.getElementById( this.containerId );
				var liObjs		= divObj.getElementsByTagName('li');
				var aObjs		= divObj.getElementsByTagName('a');
				var imgObjs		= divObj.getElementsByTagName('img');
				
				for (var i=0; i<liObjs.length; i++) {
					if ( liObjs[i].getElementsByTagName("ul").length==0 ) {
						var tId					= liObjs[i].id;
						var draggableItem		= new YAHOO.util.DDProxy(tId);
						draggableItem.realWidth	= this.realWidth;
						this.addActions(draggableItem);
					}
				}
				
				for (var i=0; i<imgObjs.length; i++) {
					if ( imgObjs[i].src.indexOf("folder") >= 0 ) {
						imgObjs[i].style.display	= "none";
					}
				}
			};
			ColumnsDragAndDropObject.prototype.startDrag	= function(x, y) {
				var dragYEl					= new YAHOO.util.Element( this.getDragEl() );
				dragYEl.addClass("list1");
				dragYEl.setStyle("width", this.realWidth );
				var realObj					= document.getElementById(this.id);
				var inputEl					= realObj.getElementsByTagName('input')[0];
				var spanEl					= realObj.getElementsByTagName('span')[0];
				var startHtml				= "<input type='checkbox' name='selectedColumns' id='proxy_"+this.id+"' value='"+inputEl.value+"' />  ";
				this.getDragEl().innerHTML	= startHtml + spanEl.innerHTML;
				//YAHOO.util.Dom.setStyle(this.getDragEl(), "background", "#D7EAFD");
				//this.newObj					= document.createElement("li");
				
				//this.newObj.innerHTML		= startHtml + spanEl.innerHTML;
				//this.newObj.setAttribute('class','list2');
				//this.newObj.setAttribute('id', 'clone_'+this.id);
				
				this.newObj					= ColumnsDragAndDropObject.generateLi( 
									"clone_"+this.id, "list1 text-align", "dup_"+this.id, inputEl.value, "selectedColumns", spanEl.innerHTML);
				
				this.colObj					= realObj;
				
				this.dndObj					= new SelColsDragAndDropObject();

			};
			ColumnsDragAndDropObject.prototype.onDragOver	= function (e, tId) {
				var destObj			= document.getElementById(tId);
				var srcObj			= this.newObj;
				var onSourceArea	= false;
				var pn				= destObj;
				
				if ( ! this.checkDestination(destObj))
					return false;
				
				if ( destObj.id=="source_col_div" )
						onSourceArea=true;
				if (destObj.nodeName.toLowerCase()=="ul") {
						if ( srcObj.parentNode!=destObj ){
								ColumnsDragAndDropObject.selectObj(this.dndObj, srcObj, destObj, this.colObj);
								//this.colObj.style.display	= "none";
								//destObj.appendChild( srcObj );
								//dragObj		= new YAHOO.util.DDProxy(srcObj.id);
								//this.dndObj.addActions( dragObj );
						}
						return;
				}
				
				if ( !onSourceArea ){					
					if (destObj.nodeName.toLowerCase()=="li") {
						while (pn!=null) {
							if ( pn.nodeName.toLowerCase()=="div" && pn.id=="source_col_div") {
								onSourceArea	= true;
								break;
							}
							pn	= pn.parentNode;
						}
						if ( !onSourceArea ) {
							this.colObj.style.display	= "none";
							if ( this.goingUp ) {
								destObj.parentNode.insertBefore(srcObj, destObj);
							}
							else
								destObj.parentNode.insertBefore(srcObj, destObj.nextSibling);
							if ( srcObj.parentNode==null ){
								ColumnsDragAndDropObject.selectObj(srcObj, destObj, this.colObj);
								dragObj		= new YAHOO.util.DDProxy(srcObj.id);
								this.dndObj.addActions( dragObj );
							}
						}
					}
				}
				if ( onSourceArea )	 {
					destId		= srcObj.firstChild.id.substring(4);
					document.getElementById(destId).style.display	= "";
					if (srcObj.parentNode!=null) {
						srcObj.parentNode.removeChild(srcObj);
					}
				}
			};
			ColumnsDragAndDropObject.prototype.endDrag 		= function(e) {
				var realObj	= this.colObj;
				if ( this.newObj!=null && this.newObj.parentNode!=null )
					realObj	= this.newObj;
				var dragObj	= this.getDragEl();
				MyDragAndDropObject.prototype.endAnimation(realObj, dragObj);
				generateHierarchies();
				repManager.checkSteps();
			};
			ColumnsDragAndDropObject.generateLi			=  function (liId, liClass, inputId, inputValue, inputName, html) {
				var startHtml				= "<input type='checkbox' name='"+inputName+"' id='"+inputId+"' value='"+inputValue+"' style='line-height:15px; margin-top:6px;'/>  ";
				var newObj					= document.createElement("li");
				
				//newObj.innerHTML			= startHtml + "<label style='vertical-align: baseline;'>" + html + "</label>";
				newObj.innerHTML			= startHtml + html;
				//newObj.setAttribute('class',liClass);
				newObj.setAttribute('id', liId);
				
				var newObjY					= new YAHOO.util.Element( newObj );
				newObjY.addClass( liClass );
				
				return newObj;
			};
			ColumnsDragAndDropObject.selectObj			= function (ddObj, srcObj, destObj, realSrcObj) {
				realSrcObj.style.display	= "none";
				destObj.appendChild( srcObj );
				var dragObj					= new YAHOO.util.DDProxy(srcObj.id);
				ddObj.addActions( dragObj );
			};
			ColumnsDragAndDropObject.deselectObj		= function (srcObj) {
				destId		= srcObj.firstChild.id.substring(4);
				document.getElementById(destId).style.display	= "";
				if (srcObj.parentNode!=null) {
					srcObj.parentNode.removeChild(srcObj);
				}
			};
			ColumnsDragAndDropObject.selectObjs			= function (srcId, destId) {
				var srcEl		= document.getElementById(srcId);
				var destEl		= document.getElementById(destId);
				var myDDObj		= new SelColsDragAndDropObject();
				
				var inputEls	= srcEl.getElementsByTagName("input");
				for (var i=0; i<inputEls.length; i++) {
					if ( inputEls[i].checked ) {
						inputEls[i].checked	= false;
						if ( inputEls[i].id.indexOf("field") >= 0 ) {
							var spanEl			= inputEls[i].parentNode.getElementsByTagName('span')[0];
							var liId			= inputEls[i].parentNode.id;
							var newObj			= ColumnsDragAndDropObject.generateLi( "clone_"+liId, "list1 text-align", "dup_"+liId, inputEls[i].value, "selectedColumns",spanEl.innerHTML);
							ColumnsDragAndDropObject.selectObj(myDDObj, newObj, destEl, inputEls[i].parentNode);
							i=i-1;
						}
					}
				}
				generateHierarchies();
				if (typeof repManager != 'undefined') {
					repManager.checkSteps();
				}
				return false;
			};
			ColumnsDragAndDropObject.selectObjsByDbId	= function (srcId, destId, arrayOfIds) {
				var srcEl		= document.getElementById(srcId);
				var destEl		= document.getElementById(destId);
				var myDDObj		= new SelColsDragAndDropObject();
				
				
				for ( var j=0; j<arrayOfIds.length; j++) {
					var inputEls	= srcEl.getElementsByTagName("input");
					for (var i=0; i<inputEls.length; i++) {
						if ( inputEls[i].value == arrayOfIds[j] ) {
							if ( inputEls[i].id.indexOf("field") >= 0 ) {
								var spanEl			= inputEls[i].parentNode.getElementsByTagName('span')[0];
								var liId			= inputEls[i].parentNode.id;
								var newObj			= ColumnsDragAndDropObject.generateLi( "clone_"+liId, "list1 text-align", "dup_"+liId, inputEls[i].value, "selectedColumns",spanEl.innerHTML);
								ColumnsDragAndDropObject.selectObj(myDDObj, newObj, destEl, inputEls[i].parentNode);
							}
						}
					}
				}
				if (typeof repManager != 'undefined') {
					repManager.checkSteps();
				}
				return false;
			};
			ColumnsDragAndDropObject.forEachObjByDbId	= function (srcId, arrayOfIds, fn) {
				var srcEl		= document.getElementById(srcId);
				for (var j = 0; j < arrayOfIds.length; j++) {
					var inputEls = srcEl.getElementsByTagName("input");
					for (var i = 0; i < inputEls.length; i++) {
						if (inputEls[i].value == arrayOfIds[j]) {
							fn.apply(this, [ inputEls[i].parentNode ]);
						}
					}
				}
			};
        	ColumnsDragAndDropObject.hideObjsByDbId	= function (srcId, arrayOfIds) {
                ColumnsDragAndDropObject.forEachObjByDbId(srcId, arrayOfIds, function (el) {
                	el.style.display = "none";
				});
            };
			ColumnsDragAndDropObject.showObjsByDbId	= function (srcId, arrayOfIds) {
                ColumnsDragAndDropObject.forEachObjByDbId(srcId, arrayOfIds, function (el) {
                    el.style.display = "";
                });
			};
			ColumnsDragAndDropObject.deselectObjs		= function (srcId) {
				var srcEl		= document.getElementById(srcId);
				inputEls		= srcEl.getElementsByTagName("input");
				for (var i=0; i<inputEls.length; i++) {
					if ( inputEls[i].checked ) {
						inputEls[i].checked	= false;
						ColumnsDragAndDropObject.deselectObj( inputEls[i].parentNode );
						i=i-1;
					}
				}
				generateHierarchies();
				if (typeof repManager != 'undefined') {
					repManager.checkSteps();
				}
				return false;
			};
			
		SelColsDragAndDropObject.prototype				= new ColumnsDragAndDropObject('source_col_div','dest_col_ul');
		SelColsDragAndDropObject.prototype.parent		= ColumnsDragAndDropObject;
		SelColsDragAndDropObject.prototype.constructor	= SelColsDragAndDropObject;
		function SelColsDragAndDropObject() {
			this.realWidth	= "30%";
		};
		SelColsDragAndDropObject.prototype.startDrag	= function(x, y) {
			MyDragAndDropObject.prototype.startDrag.call(this,x,y);
			this.newObj			= document.getElementById(this.id);
			this.dndObj			= new SelColsDragAndDropObject();
			colId				= this.newObj.firstChild.id.substring(4);
			this.colObj			= document.getElementById(colId);
			YAHOO.util.Dom.setStyle(this.getDragEl(), "background", "#D7EAFD"); 	
		};
		
		
		
