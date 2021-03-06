package org.flowerplatform.editor.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public abstract class PlainFileAccessController implements IFileAccessController {

	@Override
	public long getLastModifiedTimestamp(Object file) {
		return ((File) file).lastModified();
	}

	@Override
	public String getName(Object file) {
		return ((File) file).getName();		
	}
	
	/**
	 * @see IOUtils
	 * @see FileUtils	
	 */
	@Override
	public InputStream getContent(Object file) {		
		try {			
			return FileUtils.openInputStream((File) file);
		} catch (Throwable e) {
			throw new RuntimeException("Error while loading file content " + file, e);
		}		
	}

	@Override
	public void setContent(Object file, String content) {
		try {			
			FileUtils.writeStringToFile((File) file, content);
		} catch (IOException e) {
			throw new RuntimeException("Error while saving the file " + file, e);
		}
	}
	
	@Override
	public String getFileExtension(Object object) {
		File file = (File) object;
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index >= 0) {
			return name.substring(index + 1);
		}
		return "";
	}
	
	@Override
	public boolean isFile(Object file) {
		return (file instanceof File);
	}
	
	@Override
	public Class getFileClass() {
		return File.class;
	}
	
	@Override
	public File[] listFiles(Object folder) {
		return ((File)folder).listFiles();
	}
	
	@Override
	public boolean delete(Object child) {
		return ((File)child).delete();
	}
	
	@Override
	public Object getParentFile(Object file){
		return ((File)file).getParentFile();
	}
	
	@Override
	public String getParent(Object file){
		return ((File)file).getParent();
	}
	@Override
	public void rename(Object file, Object dest) {
		((File)file).renameTo((File)dest);
	}
	
	@Override
	public String readFileToString(Object file){
		try {
			return FileUtils.readFileToString((File)file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void writeStringToFile(Object file, String str) {
		try {
			FileUtils.writeStringToFile((File)file, str);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean isDirectory(Object file) {
		return ((File)file).isDirectory();
	}

	@Override
	public boolean createNewFile(Object file) {
		try {
			return ((File) file).createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object createNewFile(Object file, String name) {
		return new File((File) file, name);
	}

	@Override
	public boolean exists(Object file) {
		return ((File) file).exists();
	}

	@Override
	public String getPathRelativeToFile(Object file, Object relativeTo) {
		String relative = ((File)relativeTo).toURI().relativize(((File)file).toURI()).getPath();
		if (relative.length() > 0 && relative.endsWith("/")) {
			relative = relative.substring(0, relative.length() - 1);
		}
		return relative;
	}

	@Override
	public String getAbsolutePath(Object file) {
		return ((File)file).getAbsolutePath();
	}
	
}
