///////////////////////////////////////////////////////////////////////////////
//	Copyright (c) 2009 Team Axiis
//
//	Permission is hereby granted, free of charge, to any person
//	obtaining a copy of this software and associated documentation
//	files (the "Software"), to deal in the Software without
//	restriction, including without limitation the rights to use,
//	copy, modify, merge, publish, distribute, sublicense, and/or sell
//	copies of the Software, and to permit persons to whom the
//	Software is furnished to do so, subject to the following
//	conditions:
//
//	The above copyright notice and this permission notice shall be
//	included in all copies or substantial portions of the Software.
//
//	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//	OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//	HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//	WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//	OTHER DEALINGS IN THE SOFTWARE.
///////////////////////////////////////////////////////////////////////////////

package org.axiis.layouts.utils
{
	import com.degrafa.geometry.Geometry;
	
	import flash.events.EventDispatcher;
	import flash.utils.clearTimeout;
	import flash.utils.getTimer;
	import flash.utils.setTimeout;
	
	/**
	 * GeometryRepeater modifies geometries through the use of
	 * PropertyModifiers.
	 * 
	 * When modifications take longer than a single frame, they are distributed
	 * over multiple frames to prevent the application from appearing to have
	 * frozen. Objects can register callback functions so they can be notified
	 * when the GeometryRepeater begins or ends each iteration or ends its
	 * final iteration.
	 */
	public class GeometryRepeater extends EventDispatcher
	{
		/**
		 * Constructor.
		 */
		public function GeometryRepeater()
		{
			super();
		}
		
		private var timerID:int;
		
		/**
		 * The geometry that should be repeated.
		 */
		public var geometry:Geometry;
		
		[Inspectable(category="General", arrayType="org.axiis.layouts.utils.PropertyModifier")]
		[ArrayElementType("org.axiis.layouts.utils.PropertyModifier")]
		/**
		 * An array of PropertyModifiers that should be applied with each
		 * iteration of the GeometryRepeater.
		 */
		public var modifiers:Array;
		
		/**
		 * The number of iterations that the GeometryRepeater has processed.
		 * When the GeometryRepeater is not running, this value is -1.
		 */
		public function get currentIteration():int
		{
			return _currentIteration;
		}
		private function set _currentIteration(value:int):void
		{
			__currentIteration = value;
		}
		private function get _currentIteration():int
		{
			return __currentIteration;
		}
		private var __currentIteration:int = -1;
		
		/**
		 * A flag indicating that the GeometryRepeater has finished repeating
		 * but the geometry's properties have not yet been restored to their
		 * original values.
		 */
		public function get iterationLoopComplete():Boolean
		{
			return _iterationLoopComplete;
		}
		private var _iterationLoopComplete:Boolean = true;
		
		/**
		 * Determines how many milliseconds repeater will iterate before waiting for the next frame;
		 */
		 public var millisecondsPerFrame:Number=50;
		
		/**
		 * Begins the modifications process.
		 * 
		 * <p>
		 * Repeatedly applies the PropertyModifiers the specified number of
		 * times. Optionally, you can set up callbacks that this method will
		 * call before the PropertyModifiers are applied, after they are
		 * applied, and when the final iteration completes.
		 * </p> 
		 * 
		 * @param numIternations The number of iterations that should be
		 * executed by before the GeometryRepeater ends. 
		 * @param preIterationCallback A function that will be called at the
		 * beginning of every repeat iteration, before the PropertyModifiers are
		 * applied.
		 * @param postIterationCallback A function that will be called on each
		 * iteration after the PropertyModifiers have been applied and the
		 * currentIteration has been incremented.
		 * @param completeCallback A function that will be called when
		 * <code>numIterations</code> of this GeometryRepeater have been
		 * executed.
		 * @param canIterateCallback A function that will be called before 
		 * every iteration. It has to return a <code>Boolean</code> value.
		 * If the result is <code>true</code> the iteration proceeds normally,
		 * if it is <code>false</code> then it will try aggain later until 
		 * the function returns <code>true</code> and proceed the iteration
		 */
		public function repeat(numIterations:int, preIterationCallback:Function = null, postIterationCallback:Function=null, completeCallback:Function = null, canIterateCallBack:Function = null):void
		{
			if(numIterations <= 0)
				return;
			
			_iterationLoopComplete = false;
			clearTimeout(timerID);
			_currentIteration = 0;
			repeatHelper(numIterations,preIterationCallback,postIterationCallback,completeCallback,canIterateCallBack);
		}
		
		/**
		 * @private
		 */
		protected function repeatHelper(numIterations:int,preIterationCallback:Function = null, postIterationCallback:Function=null, completeCallback:Function = null, canIterateCallBack:Function = null):void
		{

			var startTime:int = getTimer();
			var totalTime:int = 0;
			while(totalTime < millisecondsPerFrame && currentIteration < numIterations)
			{
				if(canIterateCallBack != null)
				{
					//it seems that iteration at this moment is not possible, so we break
					//it will then give the player 10ms to render and, and try again
					if(canIterateCallBack.call(this) == false)
						break;
				}
				
				if(preIterationCallback != null)
					preIterationCallback.call(this);
				
				if(geometry)
				{
					for each (var modifier:PropertyModifier in modifiers)
					{
						//trace(modifier.property,modifier) 
						if(currentIteration == 0)
							modifier.beginModify(geometry);
						modifier.apply();
					}
				}
				_currentIteration++;
				
				if (postIterationCallback != null)
					postIterationCallback.call(this);
			
				totalTime = getTimer() - startTime;
			}
			
			// We've finished looping before time ran out. Tear down and call completeCallback
			if(currentIteration == numIterations)
			{					
				_iterationLoopComplete = true;
				for each (modifier in modifiers)
				{
					modifier.end();
				}
				_currentIteration = -1;
				completeCallback.call(this); //Call back now, before we set all our properties back to the original values
			}
			// The loop took too long and we had to break out. Give the player 10ms to render and, and try again
			else
			{
				timerID = setTimeout(repeatHelper,1,numIterations,preIterationCallback,postIterationCallback,completeCallback,canIterateCallBack);
			}
		}
	}
}