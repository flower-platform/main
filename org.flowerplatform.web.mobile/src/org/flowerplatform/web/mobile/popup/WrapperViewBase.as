package org.flowerplatform.web.mobile.popup {
	import flash.events.Event;
	
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionUtil;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IComposedAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.components.Label;
	import spark.components.View;
	import spark.components.ViewMenuItem;
	import spark.components.supportClasses.ButtonBase;
	import spark.primitives.BitmapImage;
	
	public class WrapperViewBase extends View implements IPopupHost {
		
		protected var openMenuAction:OpenMenuAction;
		
		protected var iconComponent:BitmapImage;
		
		protected var labelComponent:Label;
		
		protected var allActionsForActivePopupContent:Vector.<IAction>;
		
		protected var selectionForActivePopupContent:IList;
		
		public function WrapperViewBase() {
			super();
			openMenuAction = new OpenMenuAction(this);
			addEventListener(FlexEvent.MENU_KEY_PRESSED, menuKeyPressedEvent);
		}
		
		protected function menuKeyPressedEvent(event:FlexEvent):void {
			// we do this so that the main app logic (that just opens the menu) won't execute
			event.preventDefault();
			if (openMenuAction.enabled) {
				openMenuAction.run();
			}
		}
		
		override protected function createChildren():void {
			super.createChildren();

			iconComponent = new BitmapImage();
			labelComponent = new Label();
			titleContent = [iconComponent, labelComponent]; 
		}
		
		public function get activePopupContent():IPopupContent {
			throw new Error("Should be implemented");
		}
		
		public function setIcon(value:Object):void {
			iconComponent.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(value);
		}
		
		public function setLabel(value:String):void {
			labelComponent.text = value;			
		}
		
		/**
		 * Populates the View Navigator and the OpenMenuAction, with the first level of actions.
		 */
		public function refreshActions(popupContent:IPopupContent):void	{
			if (activePopupContent != popupContent) {
				return;
			}
			selectionForActivePopupContent = popupContent.getSelection();
			allActionsForActivePopupContent = popupContent.getActions(selectionForActivePopupContent);

			var newActionContent:Array = new Array();
			var newViewMenuItems:Vector.<ViewMenuItem> = new Vector.<ViewMenuItem>();
			ActionUtil.processAndIterateActions(null, allActionsForActivePopupContent, selectionForActivePopupContent, this, function (action:IAction):void {
				if (action.preferShowOnActionBar) {
					var button:ActionButton = new ActionButton();
					populateButtonWithAction(button, action);				
					newActionContent.push(button);
				} else {
					var actionViewMenuItem:ActionViewMenuItem = new ActionViewMenuItem();
					populateButtonWithAction(actionViewMenuItem, action);				
					newViewMenuItems.push(actionViewMenuItem);
				}
			});
			
			openMenuAction.viewMenuItems = newViewMenuItems;
			
			var menuButton:ActionButton = new ActionButton();
			populateButtonWithAction(menuButton, openMenuAction);
			newActionContent.push(menuButton);
			
			actionContent = newActionContent;
		}
		
		protected function populateButtonWithAction(button:ButtonBase, action:IAction):void {
			button.label = action.label;
			button.setStyle("icon", FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(action.icon));
			button.enabled = action.enabled;
			if (button is ActionButton) {
				var actionButton:ActionButton = ActionButton(button);
				actionButton.action = action;
				actionButton.view = this;
			} else if (button is ActionViewMenuItem) {
				var actionViewMenuItem:ActionViewMenuItem = ActionViewMenuItem(button);
				actionViewMenuItem.action = action;
				actionViewMenuItem.view = this;
			}
		}
		
		/**
		 * For a ComposedAction, fills the current view menu.
		 */
		protected function populateViewMenuWithActions(composedAction:IComposedAction):void {
			var newViewMenuItems:Vector.<ViewMenuItem> = new Vector.<ViewMenuItem>();
			ActionUtil.processAndIterateActions(composedAction.id, allActionsForActivePopupContent, selectionForActivePopupContent, this, function (action:IAction):void {
				var actionViewMenuItem:ActionViewMenuItem = new ActionViewMenuItem();
				populateButtonWithAction(actionViewMenuItem, action);				
				newViewMenuItems.push(actionViewMenuItem);
			});
			
			viewMenuItems = newViewMenuItems;
		}
		
		/**
		 * The click handler for ActionButton and ActionViewMenuItem.
		 */
		public function actionClickHandler(action:IAction):void {
			if (action is IComposedAction) {
				
				var runnable:Function = function (event:Event):void {
					removeEventListener("viewMenuClose", runnable);
					callLater(function ():void {
						populateViewMenuWithActions(IComposedAction(action));
						FlexGlobals.topLevelApplication.viewMenuOpen = true;
					});
				};			
				if (FlexGlobals.topLevelApplication.viewMenuOpen) {
					// 1) we need to do a hack; the menu doesn't get repopulated until ViewNavigatorApplication.viewMenuClose_handler() is invoked,
					// (i.e. after the close transition of the menu is finished)
					// 2) we need the callLater() too, because if we do the logic directly, the menu instance won't be set to null in the main app
					// and the repopulation won't be done
					//
					// The issue is that this event, cf. the comment, seems a private/internal event "for testing"; so maybe in the future it won't
					// be available?
					//
					// 3) another solution would be to use a timer of about 200ms, which is greater than the transitions defined in the ViewMenuSkin
					// but relying on timers to run after other timers, etc. doesn't seem very robust
					addEventListener("viewMenuClose", runnable);
					
				} else {
					runnable.call(null, null);
				}
			} else {
				try {
					action.selection = selectionForActivePopupContent;
					action.run();				
				} finally {
					action.selection = null;
				}
			}
		}
		
	}
}