package org.axiis.managers
{
	import flash.display.DisplayObject;
	import flash.display.Sprite;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.core.ApplicationGlobals;
	import mx.core.UIComponent;
	import mx.managers.ISystemManager;
	
	import org.axiis.core.AxiisSprite;

	/**
	 * FreeDataTipManager will lay out a single data tip that follows the cursor
	 * as the user moves the mouse.
	 */
	public class FreeDataTipManager implements IDataTipManager
	{
		public function FreeDataTipManager()
		{
			super();
			systemManager = ApplicationGlobals.application.systemManager as ISystemManager;
		}
		
		/**
		 * @inheritDoc IDataTipManager#dataTips
		 */
		public function get dataTips():Array {
			return [dataTip];
		}
		
		private var systemManager:ISystemManager;
		
		private var context:Sprite;
		
		private var dataTip:UIComponent;
		
		private var axiisSprite:AxiisSprite;
		
		/**
		 * @inheritDoc IDataTipManager#createDataTip
		 */
		public function createDataTip(dataTips:Array,context:UIComponent,axiisSprite:AxiisSprite):void
		{
			var dataTip:UIComponent=dataTips[0];
			destroyAllDataTips();
			
			this.dataTip = dataTip;
			this.context = context;
			this.axiisSprite = axiisSprite;
			
			var point:Point = calculateDataTipPosition(axiisSprite,context)
			dataTip.x = point.x+3;
			dataTip.y = point.y;
			
			if(context)
				context.systemManager.addChildToSandboxRoot("toolTipChildren", dataTip);
			else
				systemManager.topLevelSystemManager.addChildToSandboxRoot("toolTipChildren", dataTip);
			
			axiisSprite.addEventListener(MouseEvent.MOUSE_MOVE,handleMouseMove);
			axiisSprite.addEventListener(MouseEvent.MOUSE_OUT,handleMouseOut);
		}
		
		/**
		 * @private
		 */
		protected function calculateDataTipPosition(trigger:DisplayObject,context:DisplayObject):Point
		{
			var point:Point = new Point(trigger.mouseX,trigger.mouseY); 
			point = trigger.localToGlobal(point);
			point = systemManager.stage.globalToLocal(point);
			return point;
		}
		
		/**
		 * @private
		 */
		protected function handleMouseMove(event:MouseEvent):void
		{
			if(context != null)
			{
				var point:Point = calculateDataTipPosition(DisplayObject(event.target),context);
				dataTip.x = point.x;
				dataTip.y = point.y;
				dataTip.invalidateDisplayList();
			}
		}
		
		/**
		 * @private
		 */
		protected function handleMouseOut(event:MouseEvent):void
		{
			destroyAllDataTips();
		}
		
		/**
		 * @inheritDoc IDataTipManager#destroyAllDataTips
		 */
		public function destroyAllDataTips():void
		{
			if(context != null && dataTip != null && axiisSprite != null)
			{
				UIComponent(context).systemManager.removeChildFromSandboxRoot("toolTipChildren", dataTip);
				axiisSprite.removeEventListener(MouseEvent.MOUSE_MOVE,handleMouseMove);
				axiisSprite.removeEventListener(MouseEvent.MOUSE_OUT,handleMouseOut);
				context = null;
				dataTip = null;
				axiisSprite = null;
			}
		}
	}
}