package com.degrafa.geometry
{
	import com.degrafa.geometry.command.CommandStackItem;
	import com.degrafa.geometry.RoundedRectangleComplex;
	
	[Bindable]
	/**
	 * CalloutBalloon is the Geometry used to render the default Axiis data tip.
	 */
	public class CalloutBalloon extends RoundedRectangleComplex
	{
		private var _calloutY:Number;
		/**
		* The Y position of the callout relative to its 0,0 location
		**/
		public function get calloutY():Number {
			if(!_calloutY){return 0;}
			return _calloutY;
		}
		public function set calloutY(value:Number):void{
			if(_calloutY != value){
				_calloutY = value;
				invalidated = true;
			}
			
		}	
		
		private var _calloutX:Number;
		/**
		* The X position of the callout relative to its 0,0 location
		**/
		public function get calloutX():Number{
			if(!_calloutX){return 0;}
			return _calloutX;
		}
		public function set calloutX(value:Number):void{
			if(_calloutX != value){
				_calloutX = value;
				invalidated = true;
			}
			
		}	
		
		private var _calloutWidthRatio:Number=.3;
		/**
		* How wide the base of the callout triangle is where it intersects with the side of the callout
		**/
		public function get calloutWidthRatio():Number{
			if(!_calloutWidthRatio){return 0;}
			return _calloutWidthRatio;
		}
		public function set calloutWidthRatio(value:Number):void{
			if(_calloutWidthRatio != value){
				_calloutWidthRatio = value;
				invalidated = true;
			}
			
		}	
		
		/**
		 * Constructor.
		 */
		public function CalloutBalloon()
		{
			super();
		}
		
		override public function preDraw():void {
			
			commandStack.source.length=0;
			
			super.preDraw();
			
			createCallout();
		}
		
		private function getAngle(x:Number, y:Number):Number {
			 
			var xpos:Number = x - (this.x + width/2);
			var ypos:Number = y - (this.y + height/2);
			var radius:Number = Math.sqrt( xpos*xpos + ypos*ypos );
			var radianSin:Number = 0;
			var radianCos:Number = 0;
			if( radius > 0 ) {
				radianCos = Math.acos( ypos/radius );
				radianSin = Math.asin( xpos/radius );
			}
			
			var angle:Number = radianCos*180/Math.PI;
			if( radianSin > 0 ) angle = 360 - angle;
			
			return angle;
		}
		
		private function createCallout():void {
			
			//Find the lines in the commandStack
			//Assume lines are found in this order (clockwise)  TOP, RIGHT, BOTTOM, LEFT
			
			/** Determine which side of the rectangle to draw the callout on
			 *  1. Determine the angles of each corner
			 *  2. Determine the angle of the callout x,y
			 *  3. Map the callout x,y within the bounds of arc created by two corners of the rectangle
			 */
			 
			if ( ((calloutX > this.x) && (calloutX < this.x+width)) &&  ( (calloutY > this.y) && (calloutY < this.y+height))) return;
			 
			var calloutAngle:Number=getAngle(calloutX,calloutY);
			var topLeftAngle:Number=getAngle(this.x,this.y);
			var topRightAngle:Number=getAngle(this.x+width,this.y+0);
			var bottomRightAngle:Number=getAngle(this.x + width,this.y+height);
			var bottomLeftAngle:Number=getAngle(this.x,this.y+height);
			var angleRatio:Number;
			
			 
			var line:CommandStackItem;
			var w:Number;
			
			if (calloutAngle<=bottomRightAngle && calloutAngle>=topRightAngle) {
			
				//trace("RIGHT SIDE");
				line=this.rightLine;
				w=height-topRightRadius-bottomRightRadius;
				angleRatio=(bottomRightAngle-calloutAngle)/(bottomRightAngle-topRightAngle);
				
			}
			else if (calloutAngle<=topRightAngle && calloutAngle>=topLeftAngle) {
				//trace("TOP SIDE");
				line=this.topLine;
				w=width-topRightRadius-topLeftRadius;
				angleRatio=(topRightAngle-calloutAngle)/(topRightAngle-topLeftAngle);
			}
			else if (calloutAngle<=topLeftAngle && calloutAngle >=bottomLeftAngle) {
				//trace("LEFT SIDE");
				line=this.leftLine;
				w=height-this.topLeftRadius-this.bottomRightRadius;
				angleRatio=(topLeftAngle-calloutAngle)/(topLeftAngle-bottomLeftAngle);
			}
			else  {
				//trace("BOTTOM SIDE");
				line=this.bottomLine;
				w=width-this.bottomLeftRadius-this.bottomRightRadius;
				var ca:Number= (calloutAngle>=bottomRightAngle) ? calloutAngle-bottomRightAngle: (360-bottomRightAngle) + calloutAngle;
				angleRatio=   1-(ca/((360-bottomRightAngle)+bottomLeftAngle))

			}
			
			//trace("topLeftAngle=" + topLeftAngle + " topRightAngle=" + topRightAngle + " bottomRightAngle=" + bottomRightAngle + " bottomLeftAngle=" + bottomLeftAngle + " calloutAngle=" + calloutAngle + " angleRatio=" + angleRatio);
			
			var i:int=0;
			var previousItem:CommandStackItem;
			var o:Number=w*(1-calloutWidthRatio)*(1-angleRatio)*.9;
			w=w*calloutWidthRatio;
			
			if (o+w > this.width) o=this.width-w;
			
			for each (var item:CommandStackItem in this.commandStack.source) {
				
				if (item == line ) {
					
					var oldX:int=item.x;
				
					var px:Number=(isNaN(previousItem.x)) ? previousItem.x1 : previousItem.x;
					var py:Number=(isNaN(previousItem.y)) ? previousItem.y1 : previousItem.y;
					
				
					
					//Create first edge of triangle
					var line0:CommandStackItem;
					var line1:CommandStackItem;
					var line2:CommandStackItem;
					var line3:CommandStackItem;
					
					
					if (line==this.topLine) {
						line0 = new CommandStackItem(CommandStackItem.LINE_TO,px+o,item.y);
						line1 = new CommandStackItem(CommandStackItem.LINE_TO,calloutX,calloutY);
						line2 = new CommandStackItem(CommandStackItem.LINE_TO,px+o+w,item.y);
						line3 = new CommandStackItem(CommandStackItem.LINE_TO,item.x,item.y);
					}
					else if (line==this.rightLine) {
						line0 = new CommandStackItem(CommandStackItem.LINE_TO,item.x,py+o);
						line1 = new CommandStackItem(CommandStackItem.LINE_TO,calloutX,calloutY);
						line2 = new CommandStackItem(CommandStackItem.LINE_TO,item.x,py+o+w);
						line3 = new CommandStackItem(CommandStackItem.LINE_TO,item.x,item.y);
					}
					if (line==this.bottomLine) {
						line0 = new CommandStackItem(CommandStackItem.LINE_TO,px-o,item.y);
						line1 = new CommandStackItem(CommandStackItem.LINE_TO,calloutX,calloutY);
						line2 = new CommandStackItem(CommandStackItem.LINE_TO,px-o-w,item.y);
						line3 = new CommandStackItem(CommandStackItem.LINE_TO,item.x,item.y);
					}
					else if (line==this.leftLine) {
						line0 = new CommandStackItem(CommandStackItem.LINE_TO,item.x,py-o);
						line1 = new CommandStackItem(CommandStackItem.LINE_TO,calloutX,calloutY);
						line2 = new CommandStackItem(CommandStackItem.LINE_TO,item.x,py-o-w);
						line3 = new CommandStackItem(CommandStackItem.LINE_TO,item.x,item.y);
					}
					
					commandStack.addItemAt(line0,i+1);
					commandStack.addItemAt(line1,i+2);
					commandStack.addItemAt(line2,i+3);
					commandStack.addItemAt(line3,i+4);
					
					//remove original line
					commandStack.source.splice(i,1);
					
					//commandStack.source[i]=item;
					
					break;
				}
				previousItem=item;
				i++;
			}
			
		}
		
		

	}
}