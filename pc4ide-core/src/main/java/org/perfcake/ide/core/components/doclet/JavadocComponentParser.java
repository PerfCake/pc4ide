package org.perfcake.ide.core.components.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class JavadocComponentParser {

	/**
	 * Properties file.
	 */
	public static final String JAVADOC_PROPERTIES_FILE = "perfcake-comment.properties";

	private static final String JAVADOC_PROPERTIES_COMMENT = "Javadoc for the perfcake components fields.";

	public static boolean start(RootDoc root) {

		boolean processedSuccessfully = false;
		final ClassDoc[] classes = root.classes();

		final Properties properties = new Properties();

		for (final ClassDoc classDoc : classes) {
			if (isPerfcakeComponent(classDoc)) {

				if (classDoc.commentText() != null && !classDoc.commentText().isEmpty()) {
					properties.setProperty(classDoc.qualifiedName(), classDoc.commentText());
				}
				for (final FieldDoc field : classDoc.fields()) {
					if (field.commentText() != null && !field.commentText().isEmpty()) {
						properties.setProperty(field.qualifiedName(), field.commentText());
					}
				}
			}
		}

		processedSuccessfully = true;

		try {
			System.out.println("Storing properties to file: " + JAVADOC_PROPERTIES_FILE);
			properties.store(new FileOutputStream(JAVADOC_PROPERTIES_FILE), JAVADOC_PROPERTIES_COMMENT);
		} catch (final FileNotFoundException e) {
			logException(e);

		} catch (final IOException e) {
			logException(e);
		}
		return processedSuccessfully;

	}

	private static boolean isPerfcakeComponent(ClassDoc classDoc) {

		boolean isComponent = false;
		/* it could be useful to fine grane this filtering so that only required javadoc are parsed,
		 * but it would require to create a list of types that should be parsed. The list will need to
		 * be updated with perfcake changes so until this is a performance or javadoc size issue such coarse
		 * grined filtering is good enough.
		 */
		if (classDoc != null && classDoc.qualifiedName().startsWith("org.perfcake")) {
			isComponent = true;
		}

		return isComponent;
	}

	private static void logException(Throwable e) {
		System.out.println("Exception: " + e.getClass().getName());
		System.out.println("Message: " + e.getMessage());
		e.printStackTrace();
	}
}
