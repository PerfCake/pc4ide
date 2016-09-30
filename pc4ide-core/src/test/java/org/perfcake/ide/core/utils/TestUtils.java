package org.perfcake.ide.core.utils;

import org.perfcake.ide.core.components.ComponentManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Created by jknetl on 9/30/16.
 */
public class TestUtils {

	private TestUtils() {
	}

	public static ComponentManager createComponentManager(){
		ComponentManager componentManager = null;
		try {
			componentManager = new ComponentManager(new FileInputStream("target/classes/perfcake-comment.properties"), Arrays.asList(new String[] {"org.perfcake"}));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return componentManager;
	}
}
