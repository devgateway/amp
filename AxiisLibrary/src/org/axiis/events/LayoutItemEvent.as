package org.axiis.events
{
	import flash.events.Event;
	
	import org.axiis.core.AxiisSprite;
	
	/**
	 * LayoutItemEvent is the primary event disptached by BaseLayout when a user interacts with an AxiisSprite contained within the layout
	 * events such as MouseOver, MouseOut, MouseClick, selected, unselected etc
	 */
	public class LayoutItemEvent extends Event
	{
		/**
		 * The item which triggered the original Event this Object wraps
		 */
		public function get item():AxiisSprite {
			return _item;
		}
		
		private var _item:AxiisSprite;
		
		/**
		 * The original Event this Object is redispatching
		 */
		public function get sourceEvent():Event {
			return _sourceEvent;
		}
		
		private var _sourceEvent:Event;
		
		public function LayoutItemEvent(type:String, item:AxiisSprite, e:Event):void
		{
			
			super(type,false,true);
			_item=item;
			_sourceEvent=e;
		}

	}
}