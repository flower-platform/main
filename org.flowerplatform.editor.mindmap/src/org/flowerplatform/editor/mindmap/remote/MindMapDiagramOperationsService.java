package org.flowerplatform.editor.mindmap.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.codesync.remote.CodeSyncElementFeatureChangesProcessor;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.MindMapNode;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Cristina Constantinescu
 */
public class MindMapDiagramOperationsService {

	private static final String SERVICE_ID = "mindMapDiagramOperationsService";
	
	public static MindMapDiagramOperationsService getInstance() {
		return (MindMapDiagramOperationsService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
		
	public void setExpanded(ServiceInvocationContext context, String viewId, boolean expanded) {
		MindMapNode node = getMindMapNodeById(context, viewId);
		node.setExpanded(expanded);
		
		if (!expanded) {
			node.getPersistentChildren().clear();
		}
		notifyProcessors(context, node);
	}
	
	public void setText(ServiceInvocationContext context, String viewId, String text) {
		MindMapNode node = getMindMapNodeById(context, viewId);		
		CodeSyncElement cse = (CodeSyncElement) node.getDiagrammableElement();
		CodeSyncPlugin.getInstance().setFeatureValue(cse, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name(), text);
		
		notifyProcessors(context, node);
	}
	
	public Object changeParent(ServiceInvocationContext context, String viewId, String parentViewId, int index, int side) {
		MindMapNode node = getMindMapNodeById(context, viewId);	
		MindMapNode newParentNode = getMindMapNodeById(context, parentViewId);
		
		CodeSyncElement cse = (CodeSyncElement) node.getDiagrammableElement();
		((MindMapNode) node.eContainer()).getPersistentChildren().remove(node);
		((CodeSyncElement) cse.eContainer()).getChildren().remove(cse);
		
		((CodeSyncElement) newParentNode.getDiagrammableElement()).getChildren().add(index, cse);
//		
//		List<EObject> children = (List<EObject>) CodeSyncPlugin.getInstance().getFeatureValue((CodeSyncElement) cse.eContainer(), CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children());
//		children.remove(cse);
//		CodeSyncPlugin.getInstance().setFeatureValue((CodeSyncElement) cse.eContainer(), CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children(), children);
		
//		children = (List<EObject>) CodeSyncPlugin.getInstance().getFeatureValue((CodeSyncElement) newParentNode.getDiagrammableElement(), CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children());
//		children.add(cse);
//		CodeSyncPlugin.getInstance().setFeatureValue((CodeSyncElement) newParentNode.getDiagrammableElement(), CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children(), children);
		
		node.setSide(side);
		
//		View rootNode = node;
//		while (!(rootNode.eContainer() instanceof Diagram)) {
//			rootNode = (View) rootNode.eContainer();
//		}
		notifyProcessors(context, newParentNode);
		
		return node;
	}
	
	protected DiagramEditableResource getEditableResource(ServiceInvocationContext context) {
		return (DiagramEditableResource) context.getAdditionalData().get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	protected MindMapNode getMindMapNodeById(ServiceInvocationContext context, String viewId) {
		return (MindMapNode) getEditableResource(context).getEObjectById(viewId);
	}
	
	protected void notifyProcessors(ServiceInvocationContext context, View view) {
		Map<String, Object> processingContext = new HashMap<String, Object>();
		processingContext.put(DiagramEditorStatefulService.PROCESSING_CONTEXT_EDITABLE_RESOURCE, getEditableResource(context));
		
		List<IDiagrammableElementFeatureChangesProcessor> processors = EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().getDiagrammableElementFeatureChangesProcessors(view.getViewType());
		if (processors != null) {
			for (IDiagrammableElementFeatureChangesProcessor processor : processors) {
//				if (processor instanceof CodeSyncElementFeatureChangesProcessor) {
					processor.processFeatureChanges(view.getDiagrammableElement(), null, view, processingContext);
//				}
			}
		}
	}
}