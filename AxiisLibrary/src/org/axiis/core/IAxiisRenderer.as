package org.axiis.core
{
	import com.degrafa.core.IGraphicsFill;
	
	/**
	 * An interface for objects that are used to render an item from the Axiis render cycle.
	 * 
	 * During the render cycle, the layouts update their <code>currentLabel</code>,
	 * <code>currentDatum</code>, <code>currentValue</code>, and <code>currentIndex</code> properties.
	 * Data tip content renderers are expected to implement this interface.
	 */
	public interface IAxiisRenderer
	{
		/**
		 * The label for the object rendered. This is the same label the object took on during the layout render cycle.
		 */
		function get label():String;
		function set label(value:String):void;
		
		/**
		 * The raw data for the object being rendered.
		 */
		function get data():Object;
		function set data(value:Object):void;
		
		/**
		 * The value of the object being rendered. This is the same value the object took on during the layout render cycle.
		 */
		function get value():Object;
		function set value(value:Object):void;
		
		/**
		 * The index into the layout's dataProvider where <code>data</code> is found.
		 */
		function get index():int;
		function set index(value:int):void;

	}
}