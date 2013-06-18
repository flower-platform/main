package org.flowerplatform.web.git.layout {
	
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.spinner.ModalSpinner;
	import com.crispico.flower.util.spinner.ModalSpinnerSupport;
	
	import mx.containers.VBox;
	import mx.core.UIComponent;
	
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.common.explorer.ExplorerViewProvider;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.dto.ViewInfoDto;

	/**
	 *	@author Cristina Constantinescu
	 */
	public class GitAbstractView extends VBox implements ModalSpinnerSupport {
		
		public function getInfo():ViewInfoDto {
			throw "Must implement get info()";
		}
		
		protected function getSelectedObjectFromExplorer():Object {	
			var workbench:Workbench = WebPlugin.getInstance().workbench;
			
			var explorer:UIComponent = UIComponent(WebPlugin.getInstance().workbench.getComponent(ExplorerViewProvider.ID));
		
			return null;				
		}
		
		protected function getMessage(messageId:String, params:Array=null):String {
			return GitPlugin.getInstance().getMessage(messageId, params);
		}
				
		protected function getResourceUrl(resource:String):String {
			return GitPlugin.getInstance().getResourceUrl(resource);				
		}
		///////////////////////////////////////////////////////////////
		// Modal Spinner Support
		///////////////////////////////////////////////////////////////
		
		private var _modalSpinner:ModalSpinner;
		
		public function get modalSpinner():ModalSpinner	{
			return _modalSpinner;
		}
		
		public function set modalSpinner(value:ModalSpinner):void {
			_modalSpinner = value;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if (modalSpinner != null) {
				modalSpinner.setActualSize(unscaledWidth, unscaledHeight);
			}
		}
	}
}