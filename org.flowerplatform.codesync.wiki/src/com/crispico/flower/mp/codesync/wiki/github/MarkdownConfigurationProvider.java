package com.crispico.flower.mp.codesync.wiki.github;

import static com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration.*;

import org.flowerplatform.common.regex.RegexWithAction;

import astcache.wiki.Page;

import com.crispico.flower.mp.codesync.wiki.IConfigurationProvider;
import com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration;
import com.crispico.flower.mp.codesync.wiki.WikiTextBuilder;
import com.crispico.flower.mp.codesync.wiki.WikiTreeBuilder;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana
 */
public class MarkdownConfigurationProvider implements IConfigurationProvider {

	private final String HEADLINE_LEVEL_1_UNDERLINE = "(\\S.*?)[\r\n]=+[\r\n]";
	private final String HEADLINE_LEVEL_2_UNDERLINE = "(\\S.*?)[\r\n]-+[\r\n]";
	
	@Override
	public void buildConfiguration(WikiRegexConfiguration config, CodeSyncElement cse) {
		config
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_6_CATEGORY, getHeadline(6), HEADLINE_LEVEL_6_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_5_CATEGORY, getHeadline(5), HEADLINE_LEVEL_5_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_4_CATEGORY, getHeadline(4), HEADLINE_LEVEL_4_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_3_CATEGORY, getHeadline(3), HEADLINE_LEVEL_3_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_2_CATEGORY, getHeadline(2), HEADLINE_LEVEL_2_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_1_CATEGORY, getHeadline(1), HEADLINE_LEVEL_1_CATEGORY))
//		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_1_CATEGORY, HEADLINE_LEVEL_1_UNDERLINE, HEADLINE_LEVEL_1_CATEGORY))
//		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(HEADLINE_LEVEL_2_CATEGORY, HEADLINE_LEVEL_2_UNDERLINE, HEADLINE_LEVEL_2_CATEGORY))
		.add(new RegexWithAction.IfFindThisAnnounceMatchCandidate(PARAGRAPH_CATEGORY, PARAGRAPH_REGEX, PARAGRAPH_CATEGORY))
		.setUseUntilFoundThisIgnoreAll(false);
	}

	@Override
	public Class<? extends WikiTreeBuilder> getWikiTreeBuilderClass() {
		return WikiTreeBuilder.class;
	}

	@Override
	public WikiTextBuilder getWikiTextBuilder(CodeSyncElement cse) {
		return new MarkdownTextBuilder();
	}

	private String getHeadline(int level) {
		String delim = String.format("#{%s}", level);
		return delim + "(.*?)" + delim + LINE_TERMINATOR;
	}

	@Override
	public CodeSyncRoot getWikiTree(Object wiki) {
		throw new UnsupportedOperationException("A markup configuration provider cannot be used to build a wiki structure!");
	}

	@Override
	public void savePage(Page page) {
		throw new UnsupportedOperationException("A markup configuration provider cannot be used to save a wiki structure!");
	}
	
}