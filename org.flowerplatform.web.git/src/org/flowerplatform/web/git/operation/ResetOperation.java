package org.flowerplatform.web.git.operation;

import org.eclipse.core.resources.IProject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.lib.Repository;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.web.git.GitPlugin;

/**
 * @author Cristina Constantinescu
 */
public class ResetOperation {

	private Repository repository;
	private String refName;
	private ResetType type;
	private CommunicationChannel channel;
	
	public ResetOperation(Repository repository, String refName, ResetType type, CommunicationChannel channel) {
		this.repository = repository;
		this.refName = refName;
		this.type = type;
		this.channel = channel;
	}
	
	public void execute() throws Exception {
		ProgressMonitor monitor = ProgressMonitor.create(GitPlugin.getInstance().getMessage("git.resetPage.monitor.title"), channel);
			
		monitor.beginTask(GitPlugin.getInstance().getMessage("git.resetPage.monitor.message", new Object[] {type.name(), refName}), 2);
		
		try {
			IProject[] validProjects = null;
			if (type.equals(ResetType.HARD)) {
//				validProjects = GitPlugin.getInstance().getGitUtils().getValidProjects(repository);	
//				GitPlugin.getInstance().getGitUtils().backupProjectConfigFiles(null, validProjects);
			}
			Git.wrap(repository).reset().setMode(type).setRef(refName).call();
		
			monitor.worked(1);
			if (type.equals(ResetType.HARD)) {
//				GitPlugin.getInstance().getGitUtils().refreshValidProjects(validProjects, new SubProgressMonitor(monitor, 1));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			monitor.done();
//			GitPlugin.getInstance().getGitUtils().restoreProjectConfigFiles(repository, null);
		}	
	}

}
