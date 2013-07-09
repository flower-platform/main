package com.crispico.flower.mp.codesync.base.communication;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.EditorStatefulClientLocalState;
import org.flowerplatform.editor.remote.EditorStatefulService;
import org.flowerplatform.web.projects.remote.ProjectsService;

import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.action.ActionResult;
import com.crispico.flower.mp.codesync.base.action.ActionSynchronize;
import com.crispico.flower.mp.codesync.base.action.DiffAction;
import com.crispico.flower.mp.codesync.base.action.MatchActionAddLeftToRight;
import com.crispico.flower.mp.codesync.base.action.MatchActionAddRightToLeft;
import com.crispico.flower.mp.codesync.base.action.MatchActionRemoveLeft;
import com.crispico.flower.mp.codesync.base.action.MatchActionRemoveRight;

/**
 * @author Mariana
 */
public class CodeSyncEditorStatefulService extends EditorStatefulService {

	public static final String SERVICE_ID = "CodeSyncEditorStatefulService";
	
	public static final String CODE_SYNC_EDITOR = "codeSync";

	public CodeSyncEditorStatefulService() {
		setEditorName("codeSync");
		CommunicationPlugin.getInstance().getCommunicationChannelManager().addWebCommunicationLifecycleListener(this);
	}
	
//	@Override
//	public void subscribe(StatefulServiceInvocationContext context,	IStatefulClientLocalState statefulClientLocalState) {
//		EditorStatefulClientLocalState editorStatefulClientLocalState = (EditorStatefulClientLocalState) statefulClientLocalState;
//		Pair<IProject, IResource> pair = getProjectAndResource(editorStatefulClientLocalState.getEditableResourcePath());
//		IFile file = (IFile) pair.b; 
//		IProject project = pair.a;
//		editorStatefulClientLocalState.setEditableResourcePath(project.getFullPath().toString());
//		super.subscribe(context, statefulClientLocalState);
//		CodeSyncPlugin.getInstance().getCodeSyncAlgorithmRunner().runCodeSyncAlgorithm(project, file, "java", context.getCommunicationChannel());
//	}
//	
//	@Override
//	public void unsubscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
//		EditorStatefulClientLocalState editorStatefulClientLocalState = (EditorStatefulClientLocalState) statefulClientLocalState;
//		editorStatefulClientLocalState.setEditableResourcePath(getProjectPath(editorStatefulClientLocalState.getEditableResourcePath()));
//		super.unsubscribe(context, statefulClientLocalState);
//	}

	private IResource getResource(String editableResourcePath) {
		String absolutePath = CommonPlugin.getInstance().getWorkspaceRoot().getAbsolutePath().toString() + editableResourcePath;
		return ProjectsService.getInstance().getProjectWrapperResourceFromFile(new File(absolutePath));
	}
	
	private String getProjectPath(String editableResourcePath) {
//		Pair<IProject, IResource> pair = getProjectAndResource(editableResourcePath);
//		return pair.a.getFullPath().toString();
		return editableResourcePath;
	}

	@Override
	protected boolean areLocalUpdatesAppliedImmediately() {
		return true;
	}

	@Override
	protected EditableResource createEditableResourceInstance() {
		return new CodeSyncEditableResource();
	}

	@Override
	protected void loadEditableResource(StatefulServiceInvocationContext context, EditableResource editableResource) throws FileNotFoundException {
	}

	@Override
	protected void disposeEditableResource(EditableResource editableResource) {
	}

	@Override
	protected void doSave(EditableResource editableResource) {
	}

	@Override
	protected void sendFullContentToClient(EditableResource editableResource, EditableResourceClient client) {
	}

	@Override
	protected void updateEditableResourceContentAndDispatchUpdates(CommunicationChannel originatingCommunicationChannel, String originatingStatefulClientId, EditableResource editableResource, Object updatesToApply) {
		for (EditableResourceClient client : editableResource.getClients()) {
			sendContentUpdateToClient(editableResource, client, null, true);
		}
	}

//	@Override
//	protected File getEditableResourceFile(EditableResource editableResource) {
//		return ResourcesPlugin.getWorkspace().getRoot().getProject(editableResource.getEditableResourcePath()).getLocation().toFile();
//	}

	public Match getMatchForPath(String path) {
		CodeSyncEditableResource er = (CodeSyncEditableResource) getEditableResource(getProjectPath(path));
		if (er != null) {
			return er.getMatch();
		}
		return null;
	}
	
	public ModelAdapterFactorySet getModelAdapterFactorySetForPath(String path) {
		CodeSyncEditableResource er = (CodeSyncEditableResource) getEditableResource(getProjectPath(path));
		if (er != null) {
			return er.getModelAdapterFactorySet();
		}
		return null;
	}
	
	private void synchronize(Match match, ModelAdapterFactorySet set) {
		DiffAction action = null;
		if (Match.MatchType._1MATCH_LEFT.equals(match.getMatchType())) {
			action = new MatchActionAddLeftToRight(false);
		}
		if (Match.MatchType._1MATCH_RIGHT.equals(match.getMatchType())) {
			action = new MatchActionAddRightToLeft(false);
		}
		if (Match.MatchType._2MATCH_ANCESTOR_LEFT.equals(match.getMatchType())) {
			action = new MatchActionRemoveLeft();
		}
		if (Match.MatchType._2MATCH_ANCESTOR_RIGHT.equals(match.getMatchType())) {
			action = new MatchActionRemoveRight();
		}

		if (action != null) {
			ActionResult result = action.execute(match, -1);
			actionPerformed(match, set.getLeftFactory(), true, true, match.getFeature(), result);
			actionPerformed(match, set.getRightFactory(), false, true, match.getFeature(), result);
		}
		
		for (Match subMatch : match.getSubMatches()) {
			synchronize(subMatch, set);
		}
		
		ActionSynchronize syncAction = new ActionSynchronize();
		ActionResult[] results = syncAction.execute(match);
		for (int i = 0; i < results.length; i++) {
			Object feature = match.getDiffs().get(i).getFeature();
			// notify that an action was performed
			actionPerformed(match, set.getLeftFactory(), true, false, feature, results[i]);
			actionPerformed(match, set.getRightFactory(), false, false, feature, results[i]);
		}
	}
	
	private void actionPerformed(Match match, ModelAdapterFactory factory, boolean isLeft, boolean fromParent, Object feature, ActionResult result) {
		Match m = fromParent ? match.getParentMatch() : match;
		Object lateral = isLeft ? m.getLeft() : m.getRight();
		if (lateral != null) {
			factory.getModelAdapter(lateral).actionPerformed(lateral, feature, result);
		}
	}
	
	private void allActionsPerformed(Match match, ModelAdapterFactorySet set) {
		for (Match subMatch : match.getSubMatches()) {
			allActionsPerformed(subMatch, set);
		}
		
		// after all the available actions were performed on this element, notify the adapters
		allActionsPerformed(match, set.getLeftFactory(), true);
		allActionsPerformed(match, set.getRightFactory(), false);
	}
	
	private void allActionsPerformed(Match match, ModelAdapterFactory factory, boolean isLeft) {
		Object lateral = isLeft ? match.getLeft() : match.getRight();
		Object opposite = isLeft ? match.getRight() : match.getLeft();
		if (lateral != null) {
			factory.getModelAdapter(lateral).allActionsPerformed(lateral, opposite);
		}
	}
	
	private void saveModifications(CodeSyncEditableResource er) {
		saveModifications(er.getMatch(), er.getModelAdapterFactorySet().getLeftFactory(), true);
		saveModifications(er.getMatch(), er.getModelAdapterFactorySet().getRightFactory(), false);
	}
	
	private void saveModifications(Match match, ModelAdapterFactory factory, boolean isLeft) {
		Object lateral = isLeft ? match.getLeft() : match.getRight();
		if (lateral != null) {
			if (factory.getModelAdapter(lateral).save(lateral)) {
				for (Match subMatch : match.getSubMatches()) {
					saveModifications(subMatch, factory, isLeft);
				}
			}
		}
	}
	
	private void discardModifications(CodeSyncEditableResource er) {
		discardModifications(er.getMatch(), er.getModelAdapterFactorySet().getLeftFactory(), true);
		discardModifications(er.getMatch(), er.getModelAdapterFactorySet().getRightFactory(), false);
	}
	
	private void discardModifications(Match match, ModelAdapterFactory factory, boolean isLeft) {
		Object lateral = isLeft ? match.getLeft() : match.getRight();
		if (lateral != null) {
			if (factory.getModelAdapter(lateral).discard(lateral)) {
				for (Match subMatch : match.getSubMatches()) {
					discardModifications(subMatch, factory, isLeft);
				}
			}
		}
	}
	
	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////
	
	@RemoteInvocation
	public void synchronize(StatefulServiceInvocationContext context, String editableResourcePath) {
		CodeSyncEditableResource er = (CodeSyncEditableResource) getEditableResource(getProjectPath(editableResourcePath));
		if (er != null) {
			synchronize(er.getMatch(), er.getModelAdapterFactorySet());
			attemptUpdateEditableResourceContent(context, editableResourcePath, null);
		}
	}
	
	@RemoteInvocation
	public void applySelectedActions(StatefulServiceInvocationContext context, String editableResourcePath) {
		CodeSyncEditableResource er = (CodeSyncEditableResource) getEditableResource(getProjectPath(editableResourcePath));
		if (er != null) {
			allActionsPerformed(er.getMatch(), er.getModelAdapterFactorySet());
			saveModifications(er);
			attemptUpdateEditableResourceContent(context, editableResourcePath, null);
		}
	}
	
	@RemoteInvocation
	public void cancelSelectedActions(String editableResourcePath) {
		CodeSyncEditableResource er = (CodeSyncEditableResource) getEditableResource(getProjectPath(editableResourcePath));
		if (er != null) {
			discardModifications(er);
			unsubscribeAllClientsForcefully(getProjectPath(editableResourcePath), true);
		}
	}
	
}
