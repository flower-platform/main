/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flexutil.mobile.view_content_host {
	import flash.events.Event;
	
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.mobile.spinner.MobileSpinner;
	import org.flowerplatform.flexutil.action.ActionUtil;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IComposedAction;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.flexutil.selection.ISelectionForServerProvider;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	
	import spark.components.CalloutButton;
	import spark.components.Group;
	import spark.components.Label;
	import spark.components.SkinnableContainer;
	import spark.components.View;
	import spark.components.ViewMenuItem;
	import spark.components.supportClasses.ButtonBase;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.events.ViewNavigatorEvent;
	import spark.primitives.BitmapImage;
	
	/**
	 * Abstract class. A mobile view that implements <code>IViewHost</code>.
	 * Its logic populates the navigator content and menu.
	 * 
	 * <p>
	 * Caches the selection and actions for the current VH.
	 * 
	 * @author Cristian Spiescu
	 */
	public class MobileViewHostBase extends View implements IViewHost {
		
		private var _activeViewContent:IViewContent;
		
		protected var openMenuAction:OpenMenuAction;
		
		protected var iconComponent:BitmapImage;
		
		protected var labelComponent:Label;
		
		protected var allActionsForActiveViewContent:Vector.<IAction>;
		
		protected var selectionForActiveViewContent:IList;
		
		protected var spinner:MobileSpinner;
		
		public function get activeViewContent():IViewContent {	
			return _activeViewContent;
		}
		
		public function setActiveViewContent(value:IViewContent, viaFocusIn:Boolean = false):void {	
			_activeViewContent = value;
			FlexUtilGlobals.getInstance().selectionManager.viewContentActivated(this, value, viaFocusIn);
		}
		
		public function MobileViewHostBase() {
			super();
			openMenuAction = new OpenMenuAction(this);
			addEventListener(FlexEvent.MENU_KEY_PRESSED, menuKeyPressedEvent);
			addEventListener(ViewNavigatorEvent.VIEW_DEACTIVATE, viewDeactivateHandler);
		}
		
		protected function menuKeyPressedEvent(event:FlexEvent):void {
			// we do this so that the main app logic (that just opens the menu) won't execute
			event.preventDefault();
			if (openMenuAction.enabled) {
				openMenuAction.run();
			}
		}
		
		protected function viewDeactivateHandler(event:ViewNavigatorEvent):void {
			FlexUtilGlobals.getInstance().selectionManager.viewContentRemoved(this, activeViewContent); 
		}
		
		override protected function createChildren():void {
			super.createChildren();

			iconComponent = new BitmapImage();
			labelComponent = new Label();
			titleContent = [iconComponent, labelComponent]; 
		}
		
		public function setIcon(value:Object):void {
			iconComponent.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(value);
		}
		
		public function setLabel(value:String):void {
			labelComponent.text = value;	
		}
		
		/**
		 * @author Mariana
		 */
		public function displayCloseButton(value:Boolean):void {
		}
		
		/**
		 * @author Mariana
		 */
		public function addToControlBar(value:Object):void {
			var elt:IVisualElement = IVisualElement(value);
			elt.includeInLayout = true;
			Group(activeViewContent).addElement(elt);
		}

		/**
		 * Called by <code>refreshActions()</code>. This may be overridden, if the WrapperView
		 * wants to add some actions.
		 */
		protected function getActionsFromViewContent(viewContent:IViewContent, selection:IList):Vector.<IAction> {
			// for SplitViewWrapper, viewContent may be null if the current active view (left or right) is
			// not a IViewContent. However, we want the action logic to execute, so that SplitViewWrapper can
			// add its switch* actions
			return viewContent != null ? viewContent.getActions(selection) : new Vector.<IAction>();
		}
		
		/**
		 * Populates the View Navigator and the OpenMenuAction, with the first level of actions.
		 */
		public function selectionChanged():IList	{
			var viewContent:IViewContent = activeViewContent;
			
			if (viewContent is ISelectionProvider) {
				selectionForActiveViewContent = ISelectionProvider(viewContent).getSelection();	
			} else {
				// for SplitViewWrapper, viewContent may be null if the current active view (left or right) is
				// not a IViewContent. However, we want the action logic to execute, so that SplitViewWrapper can
				// add its switch* actions
				selectionForActiveViewContent = null;
			}
			
			allActionsForActiveViewContent = getActionsFromViewContent(viewContent, selectionForActiveViewContent);
			
			var newActionContent:Array = new Array();
			var newViewMenuItems:Vector.<ViewMenuItem> = new Vector.<ViewMenuItem>();
			ActionUtil.processAndIterateActions(null, allActionsForActiveViewContent, selectionForActiveViewContent, this, function (action:IAction):void {
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
			
			// give the viewMenuItems to the actions so that it can calculate it's enablement
			openMenuAction.viewMenuItems = newViewMenuItems;
			viewMenuItems = newViewMenuItems; 

			appendToActionContent(newActionContent);			
			actionContent = newActionContent;
			
			return selectionForActiveViewContent;
		}
		
		protected function appendToActionContent(actionContent:Array):void {
			var menuButton:ActionButton = new ActionButton();
			populateButtonWithAction(menuButton, openMenuAction);
			actionContent.push(menuButton);
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
			ActionUtil.processAndIterateActions(composedAction.id, allActionsForActiveViewContent, selectionForActiveViewContent, this, function (action:IAction):void {
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
					action.selection = selectionForActiveViewContent;
					action.run();				
				} finally {
					action.selection = null;
				}
			}
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function showSpinner(text:String):void {
			if (spinner != null) {
				throw new Error("Spinner is already displayed!");
			}
			spinner = new MobileSpinner();
			spinner.percentWidth = 100;
			spinner.percentHeight = 100;			
			spinner.text = text;			
			addElement(spinner);
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function hideSpinner():void {		
			removeElement(spinner);
			spinner = null;
		}
		
	}
}