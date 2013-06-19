package org.flowerplatform.web.git.history.internal;

import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.jgit.revplot.PlotCommitList;

/**
 *	@author Cristina Constantinescu
 */
public class WebCommitList extends PlotCommitList<WebLane> {

	private static final String[] COMMIT_HEX = new String[] { 
	"#85A6D6", "#DDCD5D", "#C78639", "#839662", "#C57B7F", "#8B888C", "#308790", "#BE5D42", 
	"#8FA336", "6565D9", "#489977", "#1765A0", "#84A476", "#FFE63B", "#88B046", "#FF8A01",
	"#7BBB5F", "#E95862", "#5D9EFE", "#AFD700", "#8C868E", "#E8A815", "#00ACBF", "#FB3A04", 
	"#3F40FF", "#1BC282", "#0068B7"};
	
	private ArrayList<String> allColors;

	private LinkedList<String> availableColors;
	
	public WebCommitList() {
		allColors = new ArrayList<String>(COMMIT_HEX.length);
		for (String hex : COMMIT_HEX) {
			allColors.add(hex);
		}
		availableColors = new LinkedList<String>();
		repackColors();		
	}
	
	@Override
	protected void recycleLane(WebLane lane) {
		availableColors.add(lane.color);
	}
	
	private void repackColors() {
		availableColors.addAll(allColors);
	}
	
	@Override
	protected WebLane createLane() {	
		if (availableColors.isEmpty()) {
			repackColors();
		}
		return new WebLane(availableColors.removeFirst());
	}
}
