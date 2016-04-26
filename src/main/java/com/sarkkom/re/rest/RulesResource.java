package com.sarkkom.re.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarkkom.re.service.RulesCatalog;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Rest Controller for get rules for the given ruleName at runtime.
 */
@RestController
@RequestMapping("/api")
public class RulesResource {
	@Autowired
	private RulesCatalog catalogService;

	static ScriptEngine engine = null;
	private String defaultRuleType = "WhatToDo";
	private String rule = null;

	@RequestMapping(value = "/rules/{name}",
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
			Map results = new HashMap<>();
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

