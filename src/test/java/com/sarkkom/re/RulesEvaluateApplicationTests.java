package com.sarkkom.re;

import com.sarkkom.re.service.RulesCatalog;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static junit.framework.TestCase.fail;

//import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringJUnit4ClassRunner.class)
// @SpringBootTest(classes = RulesEvaluateApplication.class)
@SpringApplicationConfiguration(classes = RulesEvaluateApplication.class)
public class RulesEvaluateApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(RulesEvaluateApplicationTests.class);

	@Autowired
	private RulesCatalog catalogService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testLoadCatalog() {
		try {
			catalogService.loadCatalog();
		} catch (Exception e) {
			log.error(" Received error while loading rule files json", e);
			fail();
		}

		Assert.assertNotNull(" The rules file map is null.. ", catalogService.getFileCatalog());
		catalogService.getFileCatalog().forEach((k, v) -> log.debug(k + " :- " + v));

		// load rules....
		Map<String, String> fileCatalog = catalogService.getFileCatalog();
		for (Map.Entry<String, String> entry : fileCatalog.entrySet()) {
			String ruleType = entry.getKey();
			try {
				String rule = catalogService.getNashornRule(ruleType);
				Assert.assertNotNull(" rule is null for - " + ruleType, rule);
			} catch (Exception e) {
				log.error(" Received error while loading rules for type - " + ruleType, e);
				fail();
			}
		}
		catalogService.getFileCatalog().forEach((k, v) -> log.debug(k + " :- " + v.toString()));

	}
}
