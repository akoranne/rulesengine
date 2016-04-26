package com.sarkkom.re;

import com.sarkkom.re.service.RulesCatalog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static junit.framework.TestCase.fail;

/**
 * Created by ajaykoranne on 4/21/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RulesEvaluateApplication.class)
public class WhatToDoRuleTest {
	private static final Logger log = LoggerFactory.getLogger(WhatToDoRuleTest.class);

	@Autowired
	private RulesCatalog catalogService;

	static ScriptEngine engine = null;
	private String ruleType = "WhatToDo";
	private String rule = null;

	@Before
	public void setUp() throws Exception {
		try {
			catalogService.loadCatalog();
			Assert.assertNotNull(" The rules file map is null.. ", catalogService.getFileCatalog());
			Assert.assertNotNull(" rules file not found in rule catalog ", catalogService.getFileCatalog().get(ruleType));
		} catch (Exception e) {
			log.error(" Received error while loading rule files json", e);
			fail();
		}

		// check nashorn engine instance
		engine = new ScriptEngineManager().getEngineByName("Nashorn");
		Assert.assertNotNull(" Nashorn engine failed priming .. ", engine);

		rule = catalogService.getNashornRule(ruleType);
		Assert.assertNotNull(" Rule is null ", rule);
	}

	@Test
	public void test1() throws Exception {
		engine.put("family_visiting", true);
		engine.eval(rule);
		Assert.assertEquals("Cinema", engine.get("todo"));
	}

	@Test
	public void test2() throws Exception {
		engine.put("family_visiting", false);
		engine.put("weather", "cold");
		engine.eval(rule);
		Assert.assertEquals(null, engine.get("todo"));
	}

	@Test
	public void test3() throws Exception {
		engine.put("family_visiting", false);
		engine.put("weather", "sunny");
		engine.eval(rule);
		Assert.assertEquals("Play Tennis", engine.get("todo"));
	}

	@Test
	public void test4() throws Exception {
		engine.put("family_visiting", false);
		engine.put("weather", "rainy");
		engine.eval(rule);
		Assert.assertEquals("Stay In", engine.get("todo"));
	}

	@Test
	public void test5() throws Exception {
		engine.put("family_visiting", false);
		engine.put("weather", "windy");
		engine.eval(rule);
		Assert.assertEquals(null, engine.get("todo"));
	}

	@Test
	public void test6() throws Exception {
		engine.put("family_visiting", false);
		engine.put("weather", "windy");
		engine.put("money", "rich");
		engine.eval(rule);
		Assert.assertEquals("Shopping", engine.get("todo"));
	}

	@Test
	public void test7() throws Exception {
		engine.put("family_visiting", false);
		engine.put("weather", "windy");
		engine.put("money", "poor");
		engine.eval(rule);
		Assert.assertEquals("Cinema", engine.get("todo"));
	}

	@Test
	public void test8() throws Exception {
		engine.put("family_visiting", false);
		engine.put("weather", "cloudy");
		engine.put("money", "poor");
		engine.eval(rule);
		Assert.assertEquals("Cinema", engine.get("todo"));
	}
}
