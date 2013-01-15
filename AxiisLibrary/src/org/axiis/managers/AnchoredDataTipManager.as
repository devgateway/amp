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
	 * AnchoredDataTipManager will lay out a single data tip at a fixed point.
	 * The data tip will be anchored at the dataTipAnchorPosition of the
	 * AxiisSprite the user is interacting with. AxiisSprites have their
	 * dataTipAnchorPosition set during their parentLayout's render cycle based
	 * on the parentLayout's dataTipAnchorPosition.
	 */
	public class AnchoredDataTipManager implements IDataTipManager
	{
		public function AnchoredDataTipManager()
		{
			super();
			systemManager = ApplicationGlobals.application.systemManager as ISystemManager;
		}
		
		/**
		 * @inheritDoc IDataTipManager#dataTips
		 */
		public function get dataTips():Array {
			return _dataTips;
		}
		
		private var systemManager:ISystemManager;
		
		private var contexts:Array = [];
		
		private var _dataTips:Array = [];
		
		private var axiisSprites:Array = [];

		/**
		 * @inheritDoc IDataTipManager#createDataTip
		 */
		public function createDataTip(_dataTips:Array,context:UIComponent,axiisSprite:AxiisSprite):void
		{
			var dataTip:UIComponent=_dataTips[0];
			var anchorPoint:Point = calculateDataTipPosition(axiisSprite,context);
			dataTip.x = anchorPoint.x;
			dataTip.y = anchorPoint.y;
			
			systemManager.topLevelSystemManager.addChildToSandboxRoot("toolTipChildren", dataTip);
			
			contexts.push(context);
			this._dataTips.push(dataTip);
			axiisSprites.push(axiisSprite);
			
			axiisSprite.addEventListener(MouseEvent.MOUSE_OUT,handleMouseOut);
			//axiisSprite.addEventListener(MouseEvent.MOUSE_MOVE,handleMouseMove);
		}
		
		/**
		 * @private
		 */
		protected function calculateDataTipPosition(trigger:AxiisSprite,context:DisplayObject):Point
		{
			var point:Point=trigger.localToGlobal(trigger.dataTipAnchorPoint);
			point = systemManager.stage.globalToLocal(point);
			return point;
		}
		
		/**
		 * @private
		 */
		protected function handleMouseOut(event:MouseEvent):void
		{
			destroyAllDataTips();
		}
		
		/**
		 * @private
		 */
		protected function handleMouseMove(event:MouseEvent):void
		{
			//trace("mousing move");
			
		}
		
		/**
		 * @inheritDoc IDataTipManager#destroyAllDataTips
		 */
		public function destroyAllDataTips():void
		{
			while (_dataTips.length > 0)
			{
				var context:Sprite = contexts.pop();
				var dataTip:UIComponent = _dataTips.pop();
				var axiisSprite:AxiisSprite = axiisSprites.pop();
				context.graphics.clear();
				systemManager.topLevelSystemManager.removeChildFromSandboxRoot("toolTipChildren", dataTip);

				axiisSprite.removeEventListener(MouseEvent.MOUSE_OUT,handleMouseOut);
				
				context=null;
				dataTip=null;
				axiisSprite=null;

			}
			contexts = [];
			_dataTips = [];
			axiisSprites = [];
		}
	}
}