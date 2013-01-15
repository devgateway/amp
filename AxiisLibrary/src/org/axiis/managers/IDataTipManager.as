package org.axiis.managers
{
	import mx.core.UIComponent;
	
	import org.axiis.core.AxiisSprite;
	
	/**
	 * IDataTipManagers are responsible for laying out data tips.
	 */
	public interface IDataTipManager
	{
		/**
		 * Adds data tips to the stage and sets their initial position. If you are creating your
		 * own implementation of this class, this is an ideal place to set up event listeners to
		 * support laying out the data tips as the user moves the mouse.
		 * 
		 * @param dataTips An arry of UIComponents to add to the stage and treat as data tips
		 * @param context The UIComponent that the data tips should be positioned relative to.
		 * This is often the DataCanvas the axiisSprite belongs to.
		 * @param axiisSprite The sprite that the user hit that triggered this data tip
		 */
		function createDataTip(dataTips:Array,context:UIComponent,axiisSprite:AxiisSprite):void;
		
		/**
		 * Removes all data tips from the stage and performs and clean up.
		 */
		function destroyAllDataTips():void;
		
		/**
		 * An array of data tips created by this manager.
		 */
		function get dataTips():Array;
	}
}