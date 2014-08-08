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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.junit.Before;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.puppetlabs.geppetto.common.util.BundleAccess;
import com.puppetlabs.geppetto.injectable.CommonModuleProvider;
import com.puppetlabs.graph.DefaultGraphModule;

/**
 *
 */
public class AbstractGraphTests {

	public static void delete(File fileOrDir) throws IOException {
		File[] children = fileOrDir.listFiles();
		if(children != null)
			for(File child : children)
				delete(child);
		if(!fileOrDir.delete() && fileOrDir.exists())
			throw new IOException("Unable to delete " + fileOrDir);
	}

	private static File getBasedir() {
		if(basedir == null) {
			String basedirProp = System.getProperty("basedir");
			if(basedirProp == null) {
				try {
					File testData = getBundleAccess().getFileFromClassBundle(AbstractGraphTests.class, "output");
					if(testData == null || !testData.isDirectory())
						fail("Unable to determine basedir");
					basedir = testData.getParentFile();
				}
				catch(IOException e) {
					fail(e.getMessage());
				}
			}
			else
				basedir = new File(basedirProp);
		}
		return basedir;
	}

	public static BundleAccess getBundleAccess() {
		return commonInjector.getInstance(BundleAccess.class);
	}

	public static File getTestOutputFolder(String name, boolean purge) throws IOException {
		File testFolder = new File(new File(new File(getBasedir(), "target"), "testOutput"), name);
		testFolder.mkdirs();
		if(purge) {
			// Ensure that the folder is empty
			for(File file : testFolder.listFiles())
				delete(file);
		}
		return testFolder;
	}

	public static File toFile(URL url) throws IOException {
		return new File(new Path(FileLocator.toFileURL(url).getPath()).toOSString());
	}

	private static File basedir;

	private static Injector commonInjector = Guice.createInjector(CommonModuleProvider.getCommonModule());

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
