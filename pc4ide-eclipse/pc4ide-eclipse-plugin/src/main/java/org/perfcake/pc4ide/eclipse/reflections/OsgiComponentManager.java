package org.perfcake.pc4ide.eclipse.reflections;

import org.osgi.framework.Bundle;
import org.perfcake.ide.core.components.ComponentManager;
import org.reflections.Reflections;
import org.reflections.vfs.Vfs;

import java.io.InputStream;
import java.util.List;

public class OsgiComponentManager extends ComponentManager {

	public OsgiComponentManager(InputStream javadocStream, List<String> packagesToScan) {
		super(javadocStream, packagesToScan);
	}

	@Override
	protected Reflections createReflections() {
		final Bundle bundle = org.perfcake.pc4ide.eclipse.Activator.getInstance().getBundle();
		Vfs.addDefaultURLTypes(new BundleUrlType(bundle));
		return super.createReflections();
	}


}
