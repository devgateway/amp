
package  com.degrafa.decorators.axiis {

	import com.degrafa.core.IGraphicsFill;
	import com.degrafa.decorators.RenderDecoratorBase;
	import com.degrafa.geometry.command.CommandStack;
	
	import flash.display.Graphics;
	import flash.geom.Rectangle;
	
	public class BaseLineAreaLineDecorator extends RenderDecoratorBase{

		public function BaseLineAreaLineDecorator(){
			super();
		}
		
		public var fillBounds:Rectangle;
		
		private var _commandStack:CommandStack;
		private var _currentStrokeArgs:Array;
		private var _currentFill:IGraphicsFill;
		private var _reStrokeActive:Boolean;
		
		private var _reStroke:Function;
		
		override public function initialize(stack:CommandStack):void {
			_commandStack=stack;
			_currentFill=CommandStack.currentFill;
			if (fillBounds) {
				_currentFill.begin(CommandStack.currentContext,fillBounds);
				//trace("Decorator fill.y=" + fillBounds.y + " fill.height=" + fillBounds.height);	
			}
			if (CommandStack.currentStroke) {
					_currentStrokeArgs = CommandStack.currentStroke.lastArgs;
					var restroke:Function = CommandStack.currentStroke.reApplyFunction;
						_reStroke = function(graphics:Graphics):void {
							restroke(graphics,_currentStrokeArgs);
						_reStrokeActive = true;
				}
			}
		}
		
		override public function lineTo(x:Number, y:Number, graphics:Graphics):void {
			if ( _commandStack.cursor.currentIndex == _commandStack.length - 3 )
			{
				graphics.lineStyle();
			}
			graphics.lineTo(x,y);
			if (_reStrokeActive) _reStroke(graphics);
		}

		override public function moveTo(x:Number,y:Number,graphics:Graphics):void {
			graphics.moveTo(x,y);
		}


		override public function curveTo(cx:Number, cy:Number, x:Number, y:Number,graphics:Graphics):void {
			graphics.curveTo(cx,cy,x,y);
		}
		
	}
}