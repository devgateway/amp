package com.exportUtil
{
	public class FlexExporter
	{
		import flash.display.DisplayObject;
		import flash.display.Sprite;
		import flash.events.IOErrorEvent;
		import flash.events.SecurityErrorEvent;
		import flash.events.TextEvent;
		import flash.net.*;
		import flash.text.StyleSheet;
		import flash.text.TextField;
		import flash.text.TextFieldAutoSize;
		import flash.text.TextFieldType;
		import flash.text.TextFormat;
		import flash.utils.ByteArray;
		
		import mx.controls.Alert;
		import mx.graphics.ImageSnapshot;
		import mx.graphics.codec.*;
		
		import org.axiis.data.DataSet;
		import org.axiis.events.LayoutItemEvent;
		
		
		public function FlexExporter()
		{
		}
		
		public static function ExportToImage(displayObject:DisplayObject, type:String, url:String, graph:String):void {
			var jpgEnc:JPEGEncoder = new JPEGEncoder();
			var pngEnc:PNGEncoder = new PNGEncoder();
			var ohSnap:ImageSnapshot;
			var imgEnc:IImageEncoder;
			if (type == "jpg"){
				imgEnc = jpgEnc;
			} else {
				imgEnc = pngEnc;
			}
			ohSnap = ImageSnapshot.captureImage(displayObject, 0, imgEnc);
			var buffer:String = ImageSnapshot.encodeImageAsBase64(ohSnap);
			saveImg(buffer, type, url, "inline","testImg","", graph); 
		}
		
		private static function saveImg ( buffer:String, type:String='jpg', url:String='', downloadMethod:String='inline', fileName:String='generated', frame:String="_blank", graph:String='1' ):void {
			var header:URLRequestHeader = new URLRequestHeader ("Content-type","application/octet-stream");
			var myRequest:URLRequest = new URLRequest (url+'&name='+fileName+'&type='+type+'&method='+downloadMethod +'&graph='+graph);
			myRequest.requestHeaders.push (header);
			myRequest.method = URLRequestMethod.POST;
			myRequest.data = buffer;
			//Alert.show("Sending image for graph " + graph + " with length " + buffer.length);
			var loader:URLLoader = new URLLoader();
			
			loader.load(myRequest);

			//navigateToURL ( myRequest, frame );
		}
	}
}