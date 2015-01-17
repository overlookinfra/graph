/**
 * Copyright (c) 2011 Cloudsmith Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Cloudsmith
 *
 */
package com.puppetlabs.graph.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.puppetlabs.graph.DefaultGraphModule;

/**
 *
 */
public class AbstractGraphTests {
	public static File getTestOutputFolder(String subdirName) throws IOException {
		if(tmpdir == null)
			tmpdir = Files.createTempDirectory("graphtest_").toFile();
		tmpdir.deleteOnExit();
		File subdir = new File(tmpdir, subdirName);
		subdir.mkdirs();
		return subdir;
	}

	private static File tmpdir;

	private Injector injector;

	protected <T> T get(Class<T> clazz) {
		return getInjector().getInstance(clazz);
	}

	protected Injector getInjector() {
		return injector;
	}

	/**
	 * This implementation returns a DefaultGraphModule.
	 *
	 * @return
	 */
	protected Module getModule() {
		return new DefaultGraphModule();
	}

	@Before
	public void setUp() throws Exception {
		injector = Guice.createInjector(getModule());
	}
}
