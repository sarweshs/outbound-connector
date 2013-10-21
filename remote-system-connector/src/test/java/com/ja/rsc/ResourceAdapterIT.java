/*
Copyright 2013 Thomas Scheuchzer, java-adventures.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.ja.rsc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.ResourceAdapterArchive;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ja.rsc.dummyra.DummyAdapter;
import com.ja.rsc.dummyra.DummyConnection;
import com.ja.rsc.dummyra.DummyConnectionFactory;
import com.ja.rsc.dummyra.TimeDummyConnection;

public class ResourceAdapterIT {
	private Logger log = LoggerFactory.getLogger(ResourceAdapterIT.class);
	@Rule
	public GlassfishController gf = new GlassfishController();

	@Test
	public void testResourceAdapterDeployment() throws Exception {
		gf.deploy(createRarArchive("dummy.rar"));

		Properties properties = new Properties();
		properties.setProperty("Format", "yyyy");
		gf.createConnectorConnectionPool("dummy", DummyConnectionFactory.class,
				"dummyPool", properties);
		gf.createConnectorResource("dummyPool", "jca/dummy");

		DummyConnectionFactory factory = gf.lookup("jca/dummy");
		assertThat(factory, is(notNullValue()));

		try (DummyConnection connection = factory.getConnection()) {
			assertThat(connection, is(notNullValue()));
			assertEquals(TimeDummyConnection.class, connection.getClass());
			String time = connection.getTime();
			log.info("Time={}", time);
			assertThat(time,
					is(new SimpleDateFormat("yyyy").format(new Date())));
		}

	}

	@Test
	public void testResourceAdapterDeploymentWithNoFormatProperty()
			throws Exception {
		gf.deploy(createRarArchive("someDummy.rar"));

		Properties properties = new Properties();
		properties.setProperty("Format", "yyyy");
		gf.createConnectorConnectionPool("someDummy",
				DummyConnectionFactory.class, "someDummyPool", properties);
		gf.createConnectorResource("someDummyPool", "jca/someDummy");

		DummyConnectionFactory factory = gf.lookup("jca/someDummy");
		assertThat(factory, is(notNullValue()));

		try (DummyConnection connection = factory.getConnection()) {
			assertThat(connection, is(notNullValue()));
			assertEquals(TimeDummyConnection.class, connection.getClass());
			String time = connection.getTime();
			log.info("Time={}", time);
			assertThat(time,
					is(new SimpleDateFormat("yyyy").format(new Date())));
		}

	}

	private ResourceAdapterArchive createRarArchive(String rarName) {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class,
				new Random().nextInt(100000) + ".jar");
		jar.addPackage(AbstractAdapter.class.getPackage());
		jar.addPackage(DummyAdapter.class.getPackage());
		log.info("Jar: {}", jar.toString(true));

		ResourceAdapterArchive rar = ShrinkWrap.create(
				ResourceAdapterArchive.class, rarName);
		rar.addAsLibrary(jar);
		log.info("Rar: {}", rar.toString(true));
		return rar;
	}
}
