package com.sarkkom.re.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ajaykoranne on 4/26/16.
 */
@Service
public class RulesCatalog {
	private final Logger log = LoggerFactory.getLogger(RulesCatalog.class);

	// The fileCatalog file defining rules
	public final String defaultCatalogFile = "/rules/rules_catalog.json";

	private Map<String, String> fileCatalog = new HashMap<String, String>();
	private Map<String, String> jsCatalogMap = new HashMap<String, String>();

	/**
	 * Load the fileCatalog
	 *
	 * @throws Exception
	 */
	public void loadCatalog() throws IOException, UnsupportedEncodingException {
		loadCatalog(defaultCatalogFile, false);
	}

	/**
	 * Load the catalog file, read rules and cache into rules catalog.
	 *
	 * @param catalogFile
	 * @param isRefresh
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public void loadCatalog(String catalogFile, boolean isRefresh) throws IOException, UnsupportedEncodingException {
		// the products fileCatalog json file must be under resources folder
		// in the classpath

		if (fileCatalog == null || fileCatalog.isEmpty() || isRefresh == true) {
			log.debug("	... loading rules fileCatalog from - " + catalogFile);

			InputStream in = this.getClass().getResourceAsStream(catalogFile);
			String json = CharStreams.toString(new InputStreamReader(in, "UTF-8"));

			ObjectMapper mapper = new ObjectMapper();
			fileCatalog = mapper.readValue(json, new TypeReference<Map<String, String>>() {
			});

			log.info("	... the rules fileCatalog - " + fileCatalog);

			log.debug("	... finished loading rules fileCatalog. ");
		}
	}


	public Map<String, String> getFileCatalog() {
		return fileCatalog;
	}

	//
	public String getNashornRule(String typeName) throws IOException, UnsupportedEncodingException {
		String rule = null;

		// if the catalog of the rule files is empty,
		if (fileCatalog.isEmpty()) {
			loadCatalog();
		}

		// is the given type in the decision dag catalogmap?
		if (jsCatalogMap.containsKey(typeName)) {
			// found it
			rule = jsCatalogMap.get(typeName);
		} else {
			// not found in the weak hash, load the decision dag catalog
			// get the dag rule file for the given type
			rule = getJSRule(typeName);
		}
		return rule;
	}

	private String getJSRule(String typeName) throws IOException, FileNotFoundException {
		String rulesText = null;
		// get the name of the rule file for the given type
		String ruleFileName = fileCatalog.get(typeName);
		if (StringUtils.hasText(ruleFileName)) {
			log.debug("reteriving javascript rules - " + ruleFileName);

			InputStream inStr = this.getClass().getResourceAsStream(ruleFileName);
			try (BufferedReader in = new BufferedReader(new InputStreamReader(inStr, "UTF-8"))) {
				rulesText = in.lines().collect(Collectors.joining("\n"));
			}

//			ClassLoader classLoader = RulesCatalog.class.getClassLoader();
//			try (BufferedReader in = new BufferedReader(new FileReader(classLoader.getResource(ruleFileName).getFile()))) {
//				rulesText = in.lines().collect(Collectors.joining("\n"));
//			}


			if (StringUtils.hasText(rulesText)) {
				// found and loaded rules, add to the map
				jsCatalogMap.put(typeName, rulesText);
			}
		}
		return rulesText;
	}
}

