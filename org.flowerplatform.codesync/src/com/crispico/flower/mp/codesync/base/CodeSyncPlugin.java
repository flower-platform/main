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
package com.crispico.flower.mp.codesync.base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.blazeds.custom_serialization.CustomSerializationDescriptor;
import org.flowerplatform.codesync.DependentFeature;
import org.flowerplatform.codesync.changes_processor.CodeSyncTypeCriterionDispatcherProcessor;
import org.flowerplatform.codesync.config.extension.AddNewExtension;
import org.flowerplatform.codesync.config.extension.AddNewExtension_Note;
import org.flowerplatform.codesync.config.extension.AddNewExtension_TopLevelElement;
import org.flowerplatform.codesync.config.extension.AddNewRelationExtension;
import org.flowerplatform.codesync.config.extension.FeatureAccessExtension;
import org.flowerplatform.codesync.config.extension.InplaceEditorExtension;
import org.flowerplatform.codesync.config.extension.InplaceEditorExtension_Default;
import org.flowerplatform.codesync.config.extension.InplaceEditorExtension_Note;
import org.flowerplatform.codesync.config.extension.NamedElementFeatureAccessExtension;
import org.flowerplatform.codesync.processor.ChildrenUpdaterDiagramProcessor;
import org.flowerplatform.codesync.processor.RelationDiagramProcessor;
import org.flowerplatform.codesync.processor.RelationsChangesDiagramProcessor;
import org.flowerplatform.codesync.processor.TopLevelElementChildProcessor;
import org.flowerplatform.codesync.projects.IProjectAccessController;
import org.flowerplatform.codesync.regex.RegexService;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.codesync.wizard.WizardChildrenPropagatorProcessor;
import org.flowerplatform.codesync.wizard.WizardElementKeyFeatureChangedProcessor;
import org.flowerplatform.codesync.wizard.remote.WizardDependency;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.emf_model.notation.NotationPackage;
import org.flowerplatform.emf_model.regex.MacroRegex;
import org.flowerplatform.emf_model.regex.ParserRegex;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana Gheorghe
 * @author Cristian Spiescu
 */
public class CodeSyncPlugin extends AbstractFlowerJavaPlugin {
	
	protected static CodeSyncPlugin INSTANCE;
	
	public static final String CONTEXT_INITIALIZATION_TYPE = "initializationType";
	
	public static final String VIEW = "view";
	public static final String PARENT_CODE_SYNC_ELEMENT = "parentCodeSyncElement";
	public static final String PARENT_VIEW = "parentView";

	public static final String SOURCE = "source";
	public static final String TARGET = "target";
	
	public static final String WIZARD_ELEMENT = "wizardElement";
	public static final String WIZARD_ATTRIBUTE = "wizardAttribute";
	
	/**
	 * The location of the CSE mapping file, relative to the project. May be
	 * configurable in the future.
	 * 
	 * @author Mariana
	 */
	public String CSE_MAPPING_FILE_LOCATION = "/CSE.notation";
	
	/**
	 * The location of the ACE file, relative to the project. May be configurable
	 * in the future.
	 * 
	 * @author Mariana
	 */
	public String ACE_FILE_LOCATION = "/ACE.notation";
	
	protected List<String> srcDirs = null;
	
	public static final String FOLDER = "Folder";
	
	public static final String FILE = "File";
	
	public static final String TOP_LEVEL = "topLevel";
	
	private final static Logger logger = LoggerFactory.getLogger(CodeSyncPlugin.class);

	protected ComposedFullyQualifiedNameProvider fullyQualifiedNameProvider;
	
	protected ComposedCodeSyncAlgorithmRunner codeSyncAlgorithmRunner;
	
	protected List<DependentFeature> dependentFeatures;
	
	protected List<CodeSyncElementDescriptor> codeSyncElementDescriptors;
	
	protected List<RelationDescriptor> relationDescriptors;

	protected List<FeatureAccessExtension> featureAccessExtensions;
	
	protected List<AddNewExtension> addNewExtensions;
	
	protected List<AddNewRelationExtension> addNewRelationExtensions;
	
	/**
	 * @author Cristina Constantinescu
	 */
	protected List<InplaceEditorExtension> inplaceEditorExtensions;
	
	protected CodeSyncTypeCriterionDispatcherProcessor codeSyncTypeCriterionDispatcherProcessor;
	
	/**
	 * Runnables that create and add descriptors.
	 * 
	 * @author Mircea Negreanu
	 */
	protected List<Runnable> runnablesThatLoadDescriptors;
	
	/**
	 * @see #getProjectAccessController()
	 */
	private IProjectAccessController projectAccessController;
	
	protected boolean useUIDs = true;
	
	public static CodeSyncPlugin getInstance() {
		return INSTANCE;
	}
	
	public CodeSyncTypeCriterionDispatcherProcessor getCodeSyncTypeCriterionDispatcherProcessor() {
		return codeSyncTypeCriterionDispatcherProcessor;
	}

	public ComposedFullyQualifiedNameProvider getFullyQualifiedNameProvider() {
		return fullyQualifiedNameProvider;
	}
	
	public ComposedCodeSyncAlgorithmRunner getCodeSyncAlgorithmRunner() {
		return codeSyncAlgorithmRunner;
	}
	
	/**
	 * Platform-dependent.
	 * 
	 * @author Mariana Gheorghe
	 */
	public IProjectAccessController getProjectAccessController() {
		return projectAccessController;
	}

	public void setProjectsProvider(IProjectAccessController projectAccessController) {
		this.projectAccessController = projectAccessController;
	}
	
	/**
	 * A list of feature to be deleted in case an object is deleted 
	 * (e.g. an edge that starts or ends in a deleted view).
	 */
	public List<DependentFeature> getDependentFeatures() {
		return dependentFeatures;
	}
	
	public List<CodeSyncElementDescriptor> getCodeSyncElementDescriptors() {
		return codeSyncElementDescriptors;
	}
	
	public List<RelationDescriptor> getRelationDescriptors() {
		return relationDescriptors;
	}

	public CodeSyncElementDescriptor getCodeSyncElementDescriptor(String codeSyncType) {
		// TODO CS/JS we should have a mapping; maybe send it to flex as a map; for quick access; idem for relations
		for (CodeSyncElementDescriptor descriptor : getCodeSyncElementDescriptors()) {
			if (descriptor.getCodeSyncType().equals(codeSyncType)) {
				return descriptor;
			}
		}
		return null;
	}
	
	// TODO CS/JS we should unify the descriptors. And have them in Model?
	public RelationDescriptor getRelationDescriptor(String type) {
		for (RelationDescriptor descriptor : getRelationDescriptors()) {
			if (descriptor.getType().equals(type)) {
				return descriptor;
			}
		}
		return null;
	}
	
	public List<FeatureAccessExtension> getFeatureAccessExtensions() {
		return featureAccessExtensions;
	}
	
	public List<AddNewExtension> getAddNewExtensions() {
		return addNewExtensions;
	}
		
	public List<AddNewRelationExtension> getAddNewRelationExtensions() {
		return addNewRelationExtensions;
	}

	/**
	 * @author Cristina Constantinescu
	 */
	public List<InplaceEditorExtension> getInplaceEditorExtensions() {
		return inplaceEditorExtensions;
	}

	public boolean useUIDs() {
		return useUIDs;
	}
	
	/**
	 * @author Mariana Gheorge
	 * @author Mircea Negreanu
	 * @author Cristina Constantinescu
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;

		// initialize the list of code that will regenerate the descriptors
		runnablesThatLoadDescriptors = new ArrayList<>();

		// initialize codesyncplugin internals
		codeSyncTypeCriterionDispatcherProcessor = new CodeSyncTypeCriterionDispatcherProcessor();
		
		codeSyncElementDescriptors = new ArrayList<CodeSyncElementDescriptor>();
		relationDescriptors = new ArrayList<RelationDescriptor>();
		
		addNewExtensions = new ArrayList<AddNewExtension>();
		addNewRelationExtensions = new ArrayList<AddNewRelationExtension>();
		inplaceEditorExtensions = new ArrayList<InplaceEditorExtension>();
		
		featureAccessExtensions = new ArrayList<FeatureAccessExtension>();
		
		fullyQualifiedNameProvider = new ComposedFullyQualifiedNameProvider();
		
		dependentFeatures = new ArrayList<DependentFeature>();
		dependentFeatures.add(new DependentFeature(NotationPackage.eINSTANCE.getView(), NotationPackage.eINSTANCE.getEdge_Source(), true));
		dependentFeatures.add(new DependentFeature(NotationPackage.eINSTANCE.getView(), NotationPackage.eINSTANCE.getEdge_Target(), true));
		dependentFeatures.add(new DependentFeature(NotationPackage.eINSTANCE.getView(), NotationPackage.eINSTANCE.getView_PersistentChildren(), false));
		
		// main list of code sync descriptors
		addRunnablesForLoadDescriptors(new Runnable() {
			@Override
			public void run() {
				// descriptors
				List<CodeSyncElementDescriptor> descriptors = new ArrayList<>();
				descriptors.add(
						new CodeSyncElementDescriptor()
						.setCodeSyncType(FOLDER)
						.setLabel(FOLDER)
						.addChildrenCodeSyncTypeCategory(FILE)
						.addFeature(NamedElementFeatureAccessExtension.NAME)
						.setKeyFeature(NamedElementFeatureAccessExtension.NAME));
				descriptors.add(
						new CodeSyncElementDescriptor()
						.setCodeSyncType(FILE)
						.setLabel(FILE)
						.addCodeSyncTypeCategory(FILE)
						.addFeature(NamedElementFeatureAccessExtension.NAME)
						.setKeyFeature(NamedElementFeatureAccessExtension.NAME));
				
				descriptors.add(
						new CodeSyncElementDescriptor()
						.setCodeSyncType(WIZARD_ELEMENT)
						.setLabel("Wizard Element")
						.setIconUrl("images/wizard/wand-hat.png")
						.setDefaultName("NewWizardElement")
						.addCodeSyncTypeCategory("topLevel")
						.addChildrenCodeSyncTypeCategory(WIZARD_ATTRIBUTE)
						.addFeature(NamedElementFeatureAccessExtension.NAME)
						.setKeyFeature(NamedElementFeatureAccessExtension.NAME)						
						.setStandardDiagramControllerProviderFactory("topLevelBox")
						.setOrderIndex(500));
				descriptors.add(
						new CodeSyncElementDescriptor()
						.setCodeSyncType(WIZARD_ATTRIBUTE)
						.addCodeSyncTypeCategory(WIZARD_ATTRIBUTE)
						.setLabel("Wizard Attribute")
						.setIconUrl("images/wizard/wand-hat.png")
						.setDefaultName("NewWizardAttribute")						
						.setCategory("children")
						.addFeature(NamedElementFeatureAccessExtension.NAME)
						.setKeyFeature(NamedElementFeatureAccessExtension.NAME)						
						.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
						.setOrderIndex(501));
						
				
				getCodeSyncElementDescriptors().addAll(descriptors);
								
				EditorModelPlugin.getInstance().getMainChangesDispatcher().addProcessor(codeSyncTypeCriterionDispatcherProcessor);

				// extensions
				getFeatureAccessExtensions().add(new NamedElementFeatureAccessExtension(descriptors));
								
				getAddNewExtensions().add(new AddNewExtension_Note());	
				getAddNewExtensions().add(new AddNewExtension_TopLevelElement());					
								
				getInplaceEditorExtensions().add(new InplaceEditorExtension_Default());
				getInplaceEditorExtensions().add(new InplaceEditorExtension_Note());
				
				// processors
				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("edge", new RelationDiagramProcessor());
				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.wizardElement", new ChildrenUpdaterDiagramProcessor());				
				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.wizardElement.wizardAttribute", new TopLevelElementChildProcessor());
				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.wizardElement.wizardAttribute", new RelationsChangesDiagramProcessor());
				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.wizardElement", new RelationsChangesDiagramProcessor());
							
				getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(WIZARD_ATTRIBUTE, new WizardChildrenPropagatorProcessor());
				getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(WIZARD_ATTRIBUTE, new WizardElementKeyFeatureChangedProcessor());
				getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(WIZARD_ELEMENT, new WizardElementKeyFeatureChangedProcessor());
				
				CustomSerializationDescriptor macroRegexSD = new CustomSerializationDescriptor(MacroRegex.class)
				.addDeclaredProperty("name")
				.addDeclaredProperty("regex")				
				.register();
				
				new CustomSerializationDescriptor(ParserRegex.class)
				.addDeclaredProperties(macroRegexSD.getDeclaredProperties())
				.addDeclaredProperty("action")
				.addDeclaredProperty("fullRegex")				
				.register();
			}
		});

		// give the codeSyncExtension the signal to register their own runnable descriptors
		initializeExtensionPoint_codeSyncAlgorithmRunner();
		
		// needs custom descriptor because it uses the builder template (i.e. setters return the instance)
		new CustomSerializationDescriptor(CodeSyncElementDescriptor.class)
			.addDeclaredProperty("codeSyncType")
			.addDeclaredProperty("initializationTypes")
			.addDeclaredProperty("initializationTypesLabels")
			.addDeclaredProperty("initializationTypesOrderIndexes")
			.addDeclaredProperty("label")
			.addDeclaredProperty("iconUrl")
			.addDeclaredProperty("defaultName")
			.addDeclaredProperty("extension")
			.addDeclaredProperty("codeSyncTypeCategories")
			.addDeclaredProperty("childrenCodeSyncTypeCategories")
			.addDeclaredProperty("category")
			.addDeclaredProperty("features")
			.addDeclaredProperty("keyFeature")
			.addDeclaredProperty("standardDiagramControllerProviderFactory")
			.addDeclaredProperty("orderIndex")
			.register();
		
		new CustomSerializationDescriptor(RelationDescriptor.class)
			.addDeclaredProperty("type")
			.addDeclaredProperty("label")
			.addDeclaredProperty("iconUrl")
			.addDeclaredProperty("sourceCodeSyncTypes")
			.addDeclaredProperty("targetCodeSyncTypes")
			.addDeclaredProperty("sourceCodeSyncTypeCategories")
			.addDeclaredProperty("targetCodeSyncTypeCategories")
			.addDeclaredProperty("acceptTargetNullIfNoCodeSyncTypeDetected")
			.register();
		
		new CustomSerializationDescriptor(WizardDependency.class)
			.addDeclaredProperty("type")
			.addDeclaredProperty("label")
			.addDeclaredProperty("targetLabel")
			.addDeclaredProperty("targetIconUrl")
			.register();
	}
	
	private void initializeExtensionPoint_codeSyncAlgorithmRunner() throws CoreException {
		codeSyncAlgorithmRunner = new ComposedCodeSyncAlgorithmRunner();
		IConfigurationElement[] configurationElements = 
				Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.codesync.codeSyncAlgorithmRunner");
		for (IConfigurationElement configurationElement : configurationElements) {
			String id = configurationElement.getAttribute("id");
			String technology = configurationElement.getAttribute("technology");
			Object instance = configurationElement.createExecutableExtension("codeSyncAlgorithmRunnerClass");
			codeSyncAlgorithmRunner.addRunner(technology, (ICodeSyncAlgorithmRunner) instance);
			logger.debug("Added CodeSync algorithm runner with id = {} with class = {}", id, instance.getClass());
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	public String getFileExtension(File file) {
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index >= 0) {
			return name.substring(index + 1);
		}
		return "";
	}
	
	/**
	 * Important: the code sync mapping and cache resources <b>must</b> be loaded through the same {@link ResourceSet}.
	 */
	public ResourceSet getOrCreateResourceSet(Object file, String diagramEditorStatefulServiceId) {
		Object project = getProjectAccessController().getContainingProjectForFile(file);
		DiagramEditorStatefulService service = (DiagramEditorStatefulService) CommunicationPlugin.getInstance()
				.getServiceRegistry().getService(diagramEditorStatefulServiceId);

		DiagramEditableResource diagramEditableResource = null;		
		if (project != null) {
			String path = EditorPlugin.getInstance().getFileAccessController().getAbsolutePath(project);
			for (EditableResource er : service.getEditableResources().values()) {
				DiagramEditableResource der = (DiagramEditableResource) er;				
				if (EditorPlugin.getInstance().getFileAccessController().getAbsolutePath(der.getFile()).startsWith(path)) {
					diagramEditableResource = der;
					break;
				}
			}
		}
		if (diagramEditableResource != null) {
			return diagramEditableResource.getResourceSet();
		}
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ResourceFactoryImpl() {
			
			@Override
			public Resource createResource(URI uri) {
				return new XMIResourceImpl(uri) {
			    	protected boolean useUUIDs() {
			    		return true;
			    	}
				};
			}
		});
		return resourceSet;
	}

	/**
	 * @author Mariana
	 * @author Sebastian Solomon
	 */
	public Resource getResource(ResourceSet resourceSet, Object file) {
		URI uri = EditorModelPlugin.getInstance().getModelAccessController().getURIFromFile(file);
		boolean fileExists = EditorPlugin.getInstance().getFileAccessController().exists(file);
		return getResource(resourceSet, uri, fileExists);
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getResource(ResourceSet resourceSet, URI uri, boolean fileExists) {
		if (fileExists) {
			return resourceSet.getResource(uri, true);
		} else {
			Resource resource =	resourceSet.getResource(uri, false);
			if (resource == null) {
				resource = resourceSet.createResource(uri);
			}
			resource.unload();
			return resource;
		}
	}
	
	/**
	 * Saves all the resources from the {@link ResourceSet} where <code>resourceToSave</code>
	 * is contained.
	 * 
	 * @author Mariana
	 */
	public void saveResource(Resource resourceToSave) {
		if (resourceToSave != null) {
			List<Resource> resources = Collections.singletonList(resourceToSave);
			if (resourceToSave.getResourceSet() != null) {
				resources = resourceToSave.getResourceSet().getResources();
			}
			for (Resource resource : resources) {
				try { 
					Map<Object, Object> options = EditorModelPlugin.getInstance().getLoadSaveOptions();
					resource.save(options);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	/**
	 * Discards the modifications from all the resources from the {@link ResourceSet} 
	 * where <code>resourceToDiscard</code> is contained.
	 * 
	 * @author Mariana
	 */
	public void discardResource(Resource resourceToDiscard) {
		if (resourceToDiscard != null) {
			List<Resource> resources = Collections.singletonList(resourceToDiscard);
			if (resourceToDiscard.getResourceSet() != null) {
				resources = resourceToDiscard.getResourceSet().getResources();
			}
			for (Resource resource : resources) {
				resource.unload();
			}
		}
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getCodeSyncMapping(Object project, ResourceSet resourceSet) {
		Object codeSyncElementMappingFile = CodeSyncPlugin.getInstance().getProjectAccessController().getFile(project, CSE_MAPPING_FILE_LOCATION); 
		Resource cseResource = CodeSyncPlugin.getInstance().getResource(resourceSet, codeSyncElementMappingFile);
		if (!EditorPlugin.getInstance().getFileAccessController().exists(codeSyncElementMappingFile)) {
			// first clear the resource in case the mapping file was deleted 
			// after it has been loaded at a previous moment
			cseResource.getContents().clear();
			
			for (String srcDir : getSrcDirs()) {
				CodeSyncRoot cseRoot = (CodeSyncRoot) getRoot(cseResource, srcDir);
				if (cseRoot == null) {
					// create the CSE for the SrcDir
					cseRoot = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncRoot();
					cseRoot.setName(srcDir);
					cseRoot.setType(FOLDER);
				}
				cseResource.getContents().add(cseRoot);
			}
			
			CodeSyncPlugin.getInstance().saveResource(cseResource);
		}
		return cseResource;
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getAstCache(Object project, ResourceSet resourceSet) {
		Object astCacheElementFile = CodeSyncPlugin.getInstance().getProjectAccessController().getFile(project, ACE_FILE_LOCATION); 
		Resource resource = CodeSyncPlugin.getInstance().getResource(resourceSet, astCacheElementFile);
		if (!EditorPlugin.getInstance().getFileAccessController().exists(astCacheElementFile)) {
			resource.getContents().clear();
			CodeSyncPlugin.getInstance().saveResource(resource);
		}
		return resource;
	}
	
	/**
	 * @author Mariana
	 */
	protected CodeSyncRoot getRoot(Resource resource, String srcDir) {
		for (EObject eObj : resource.getContents()) {
			if (eObj instanceof CodeSyncRoot) {
				CodeSyncRoot root = (CodeSyncRoot) eObj;
				if (CodeSyncOperationsService.getInstance()
						.getKeyFeatureValue(root).equals(srcDir))
					return root;
			}
		}
		return null;
	}
	
	/**
	 * @author Mariana
	 */
	public CodeSyncElement getSrcDir(Resource resource, String name) {
		CodeSyncElement srcDir = null;
		for (EObject member : resource.getContents()) {
			if ((CodeSyncOperationsService.getInstance().getKeyFeatureValue((CodeSyncElement) member)).equals(name)) {
				srcDir = (CodeSyncElement) member;
				break;
			}
		}
		return srcDir;
	}
	
	/**
	 * @author Mariana
	 */
	public List<String> getSrcDirs() {
		if (srcDirs == null) {
			// TODO Mariana : get user input
			return Collections.singletonList("src");
		} 
		return srcDirs;
	}
	
	public void addSrcDir(String srcDir) {
		if (srcDirs == null) {
			srcDirs = new ArrayList<String>();
		}
		if (!srcDirs.contains(srcDir)) {
			srcDirs.add(srcDir);
		}
	}
	
	/**
	 * Executes the runnable and keeps it around to be executed
	 * when the descriptors need to be refreshed.
	 * 
	 * @param runnable
	 * 
	 * @author Mircea Negreanu
	 */
	public void addRunnablesForLoadDescriptors(Runnable runnable) {
		runnable.run();
		
		runnablesThatLoadDescriptors.add(runnable);
	}
	
	/**
	 * Reruns all the registered runnable to regenerate descriptors,
	 * processors, ..
	 * 
	 * @return String containing errors thrown during run (if any)
	 */
	public String regenerateDescriptors() {
		// clear the descriptors
		getCodeSyncElementDescriptors().clear();
		getRelationDescriptors().clear();
		getFeatureAccessExtensions().clear();
		getAddNewExtensions().clear();
		getInplaceEditorExtensions().clear();
		getCodeSyncTypeCriterionDispatcherProcessor().clear();
		RegexService.getInstance().clearRegexActionsAndCompiledRegexConfigurations();
		
		StringBuilder errorsCollected = new StringBuilder();
		for (Runnable run: runnablesThatLoadDescriptors) {
			try {
				run.run();
			} catch (Exception ex) {
				errorsCollected.append(ex.toString());
				errorsCollected.append("\n");
			}
		}
		
		return errorsCollected.toString();
	}
	
	public List<RelationDescriptor> getRelationDescriptorsHavingThisTypeAsSourceCodeSyncType(String type) {
		List<RelationDescriptor> descriptors = new ArrayList<RelationDescriptor>();
		for (RelationDescriptor descriptor : getRelationDescriptors()) {
			if (descriptor.getSourceCodeSyncTypes().contains(type)) {
				descriptors.add(descriptor);
			}
		}
		return descriptors;
	}
	
	public List<RelationDescriptor> getRelationDescriptorsHavingThisEndTypes(String sourceType, String targetType) {
		CodeSyncElementDescriptor sourceDescriptor = getCodeSyncElementDescriptor(sourceType);
		CodeSyncElementDescriptor targetDescriptor = getCodeSyncElementDescriptor(targetType);
		List<RelationDescriptor> descriptors = new ArrayList<RelationDescriptor>();
		for (RelationDescriptor descriptor : getRelationDescriptors()) {
			boolean sourceTypeOk = false;
			boolean targetTypeOk = false;
			if (descriptor.getSourceCodeSyncTypes() != null && descriptor.getSourceCodeSyncTypes().contains(sourceType)) {
				sourceTypeOk = true;				
			} else {
				for (String category : sourceDescriptor.getCodeSyncTypeCategories()) {
					if (descriptor.getSourceCodeSyncTypeCategories() != null && descriptor.getSourceCodeSyncTypeCategories().contains(category)) {
						sourceTypeOk = true;
					}
				}
			}
			if (descriptor.getTargetCodeSyncTypes() != null && descriptor.getTargetCodeSyncTypes().contains(targetType)) {
				targetTypeOk = true;				
			} else {
				for (String category : targetDescriptor.getCodeSyncTypeCategories()) {
					if (descriptor.getTargetCodeSyncTypeCategories() != null && descriptor.getTargetCodeSyncTypeCategories().contains(category)) {
						targetTypeOk = true;
					}
				}
			}
			if (sourceTypeOk && targetTypeOk) {
				descriptors.add(descriptor);
			}
		}
		return descriptors;
	}
	
}
