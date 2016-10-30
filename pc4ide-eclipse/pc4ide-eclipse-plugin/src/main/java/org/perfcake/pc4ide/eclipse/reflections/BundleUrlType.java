package org.perfcake.pc4ide.eclipse.reflections;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;
import org.reflections.vfs.Vfs;

import com.google.common.collect.AbstractIterator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Enables Reflections to work in OSGI (particularly equinox)
 */
public class BundleUrlType implements Vfs.UrlType {

	public static final String BUNDLE_PROTOCOL = "bundleresource";

	private final Bundle bundle;

	public BundleUrlType(Bundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public Vfs.Dir createDir(URL url) {
		return new BundleDir(bundle, url);
	}

	@Override
	public boolean matches(URL url) {
		return BUNDLE_PROTOCOL.equals(url.getProtocol());
	}


	public static class BundleDir implements Vfs.Dir {

		private String path;
		private final Bundle bundle;

		private static String urlPath(Bundle bundle, URL url) {
			try {
				final URL resolvedURL = FileLocator.resolve(url);
				final String resolvedURLAsfile = resolvedURL.getFile();

				final URL bundleRootURL = bundle.getEntry("/");
				final URL resolvedBundleRootURL = FileLocator.resolve(bundleRootURL);
				final String resolvedBundleRootURLAsfile = resolvedBundleRootURL.getFile();
				return("/"+resolvedURLAsfile.substring(resolvedBundleRootURLAsfile.length()));
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		public BundleDir(Bundle bundle, URL url) {
			//this(bundle, url.getPath());
			this(bundle, urlPath(bundle,url));
		}

		public BundleDir(Bundle bundle, String p) {
			this.bundle = bundle;
			this.path = p;
			if (path.startsWith(BUNDLE_PROTOCOL + ":")) {
				path = path.substring((BUNDLE_PROTOCOL + ":").length());
			}
		}

		@Override
		public String getPath() {
			return path;
		}

		@Override
		public Iterable<Vfs.File> getFiles() {
			return new Iterable<Vfs.File>() {
				@Override
				public Iterator<Vfs.File> iterator() {
					return new AbstractIterator<Vfs.File>() {
						final Enumeration<URL> entries = bundle.findEntries(path, "*.class", true);

						@Override
						protected Vfs.File computeNext() {
							return entries.hasMoreElements() ? new BundleFile(BundleDir.this, entries.nextElement()) : endOfData();
						}
					};
				}
			};
		}

		@Override
		public void close() { }
	}


	public static class BundleFile implements Vfs.File {

		private final BundleDir dir;
		private final String name;
		private final URL url;

		public BundleFile(BundleDir dir, URL url) {
			this.dir = dir;
			this.url = url;
			final String path = url.getFile();
			this.name = path.substring(path.lastIndexOf("/") + 1);
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getRelativePath() {
			return getFullPath().substring(dir.getPath().length());
		}

		public String getFullPath() {
			return url.getFile();
		}

		@Override
		public InputStream openInputStream() throws IOException {
			return url.openStream();
		}
	}
}
