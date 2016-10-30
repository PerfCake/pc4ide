/**
 *
 */
package org.perfcake.pc4ide.eclipse;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author jknetl
 *
 */
public class Activator extends AbstractUIPlugin {

	private static EclipseLogger logger;

	// The plug-in ID
	public static final String PLUGIN_ID = "org.perfcake.pc4ide.eclipse"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	public Activator() {
		super();

		logger = new EclipseLogger(getLog(), PLUGIN_ID);

		// Set swing L&F
		try {
			for (final javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {
					// PROBLEM eclipse freezes with GTK laF
					//					logger.info("Setting GTK LaF.");
					//					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					//					break;
				}
			}
			final String lafClass = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
			logger.info("Setting LaF: " + lafClass);
			UIManager.setLookAndFeel(lafClass);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			logger.warn("Cannot set sytem L&F. " + e.getClass().getCanonicalName() + ": " + e.getMessage());
		}

		plugin = this;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		logger.info("BUNDLE is starting");
		super.start(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		logger.info("BUNDLE is stopping");
		super.stop(context);
		plugin = null;
	}

	public static Activator getInstance() {
		return plugin;
	}

	public EclipseLogger getLogger() {
		return logger;
	}
}
