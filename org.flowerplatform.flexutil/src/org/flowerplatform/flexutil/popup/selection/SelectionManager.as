package org.flowerplatform.flexutil.popup.selection {
	import flash.events.EventDispatcher;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	/**
	 * Holds the active <code>ISelectionProvider</code>, and receives notifications of VH activated and selection
	 * changed, delegating to the corresponding <code>IViewHost</code> and then dispatching events.
	 * 
	 * @author Cristian Spiescu
	 */
	public class SelectionManager extends EventDispatcher {

		public var activeSelectionProvider:ISelectionProvider;
		
		public var timestamp:Number;
		
		public var timerAfterFocusIn:Timer = new Timer(250, 1);
		
		protected var viewHostPreparedForSelectionChangedInvocation:IPopupContent;
		
		public function SelectionManager() {
			timerAfterFocusIn.addEventListener(TimerEvent.TIMER_COMPLETE, timerAfterFocusInHandler);
		}
		
		/**
		 * Invoked by <code>IViewHosts</code> when a view becomes active: on init, on view change (if there are 
		 * multiple VCs) or via focus in style handler. 
		 * 
		 * <p>
		 * When <code>viaFocusIn</code> is <code>true</code> the <code>timerAfterFocusIn</code> is started.
		 */
		public function viewContentActivated(viewHost:IPopupHost, viewContent:IPopupContent, viaFocusIn:Boolean):void {
			if (viewContent != null) {
				viewContent.popupHost = viewHost;
			}
			if (viewContent is ISelectionProvider) {
				activeSelectionProvider = ISelectionProvider(viewContent);
			}
			
			if (viaFocusIn) {
				if (timerAfterFocusIn.running) {
					timerAfterFocusIn.reset();
				}
				timerAfterFocusIn.start();
			}
			selectionChanged(viewContent);
		}
		
		protected function timerAfterFocusInHandler(event:TimerEvent):void {
			selectionChanged(viewHostPreparedForSelectionChangedInvocation);
			viewHostPreparedForSelectionChangedInvocation = null;
		}
		
		/**
		 * Delegates to the VH (so that the actions and selection can be retrieved/processed) and 
		 * dispatches <code>SelectionChangedEvent</code>.
		 * 
		 * <p>
		 * If <code>timerAfterFocusIn</code> is running, i.e. an activation via focus in has just been made,
		 * then the call will be rescheduled. If during the timer period, there are other invocations of this method,
		 * then the last VC will be remebered (and used when the timer expires.
		 * 
		 * <p>
		 * This timer mechanism exists, because when clicking on a VH, usually we get 2 calls: the first via the focus
		 * in handler, and the second via the selection changed handler. In this case, we want to optimize, and have only
		 * one processing for the selection/actions. However, there are cases, when clicking on a VH doesn't trigger the 
		 * selection changed listener; so we need the focus in mechanism as well.
		 */
		public function selectionChanged(selectionProvider:IPopupContent):void {
			if (selectionProvider.popupHost == null) {
				// may happen (in theory), if the sel_changed listener from the VC fires
				// really quickly, before it has a VH
				return;
			}
			
			if (timerAfterFocusIn.running) {
				// an invocation caused by "focusIn" has recently been fired; don't do anything
				// now; reschedule
				viewHostPreparedForSelectionChangedInvocation = selectionProvider;
				return;
			}
			
			var selection:IList = selectionProvider.popupHost.selectionChanged();
			
			if (activeSelectionProvider == selectionProvider) {
				// this "if" is intended for a case like the following: there are 2 ViewContent/SelectionProviders;
				// the user clicks on VC1 and selects/deselects. So VC1 is the activeSelectionProvider.
				// Meanwhile, the selection is changed programmatically in VC2. In this case, we don't need to dispatch,
				// because the "global" selection still comes from VC1
				var event:SelectionChangedEvent = new SelectionChangedEvent();
				event.selectionProvider = activeSelectionProvider;
				event.selection = selection;
				if (selectionProvider is ISelectionForServerProvider) {
					event.selectionForServer = ISelectionForServerProvider(selectionProvider).convertSelectionToSelectionForServer(selection);
				}
				dispatchEvent(event);
			}
		}
		
	}
}