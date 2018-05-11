package com.sakx.developer.rulesengine.rest;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakx.developer.rulesengine.service.RulesCatalog;

/**
 * Rest Controller for get rules for the given ruleName at runtime.
 */
@RestController
@RequestMapping("/")
public class RulesResource {
	@Autowired
	private RulesCatalog catalogService;

	static ScriptEngine engine = null;
//	private String defaultRuleType = "WhatToDo";
//	private String rule = null;

	@RequestMapping("/")
	public String home() {
		return "\n\n	*** Rules Engine *** ";
	}

	@RequestMapping(value = "/api/rules/{name}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> evaluateRule(
			@PathVariable String name,
			@RequestParam(value = "family_visiting", defaultValue = "") String familyVisiting,
			@RequestParam(value = "weather", defaultValue = "") String weather,
			@RequestParam(value = "money", defaultValue = "") String money) throws JSONException {
		String json = null;
		try {
			engine = new ScriptEngineManager().getEngineByName("Nashorn");
			String rule = catalogService.getNashornRule(name);
			engine.put("family_visiting", (familyVisiting != null && familyVisiting.equalsIgnoreCase("yes") ? true : false));
			engine.put("weather", weather);
			engine.put("money", money);
			engine.eval(rule);

			// JSONObject results = new JSONObject();
			Map<String, String> results = new HashMap<String, String>();
			results.put("todo", (String) engine.get("todo"));
			json = (new ObjectMapper()).writeValueAsString(results);
		} catch (Exception e) {
			StringBuilder msg = new StringBuilder();
			msg.append("Evaluate rule - ").append(name).append(" failed - ");
			msg.append(e.getMessage());
			throw new JSONException(msg.toString());
		}
		ResponseEntity<String> responseEntity = new ResponseEntity<String>(json, HttpStatus.OK);
		return responseEntity;
	}
}

