package org.flowerplatform.web.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.util.FS;

/**
 * @author Cristina Constantienscu
 */
public class GitUtils {
	
	public static final String MAIN_REPOSITORY = "main";
		
	public Repository getRepository(File repoFile) {
		File gitDir = getGitDir(repoFile);
		if (gitDir != null) {
			try {
				return RepositoryCache.open(FileKey.exact(gitDir, FS.DETECTED));
			}  catch (IOException e) {
				// TODO CC: log
			}
		}
		return null;		
	}
	
	public Repository getMainRepository(File repoFile) {	
		return getRepository(new File(repoFile, MAIN_REPOSITORY));
	}
	
	public File getGitDir(File file) {
		if (file.exists()) {
			while (file != null) {
				if (RepositoryCache.FileKey.isGitRepository(file, FS.DETECTED)) {
					return file;
				} else if (RepositoryCache.FileKey.isGitRepository(new File(file, Constants.DOT_GIT), FS.DETECTED)) {
					return new File(file, Constants.DOT_GIT);
				}
				file = file.getParentFile();
			}
		}
		return null;
	}
	
	public boolean isRepository(File file) {
		return getGitDir(file) != null;
	}
	
}
