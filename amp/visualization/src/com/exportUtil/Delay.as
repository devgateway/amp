package com.exportUtil
{
	
	import flash.events.*;  
	import flash.utils.Timer;  
	
	public class Delay
	{
		private var launcher:Function;        
		private var time:Number;  
		private var times:Number;        
		private var pause:Boolean;  
		private var delayed:Timer;  
		
		function Delay(Func:Function,Time:Number,Times:Number) {  
			launcher = Func;  
			time = Time;  
			times = Times;  
			pause = false;  
			init();  
		}  
		
		private function init():void {  
			delayed = new Timer(time,times);  
			delayed.addEventListener(TimerEvent.TIMER, launch);  
			delayed.start();     
		}  
		
		private function launch(e:Event):void {  
			if(!pause){  
				launcher();  
			}  
		}  
		
		public function stopTime():void {  
			pause = true;  
		}  
		
		public function restartTime():void {  
			pause = false;  
		}  
		
		public function cancel():void {  
			delayed.removeEventListener(TimerEvent.TIMER, launch);  
		}              
	}  
}