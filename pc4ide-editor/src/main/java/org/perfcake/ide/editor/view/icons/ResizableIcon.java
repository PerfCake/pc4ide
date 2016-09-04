package org.perfcake.ide.editor.view.icons;

import javax.swing.Icon;

/**
 * Swing icon which can change its size.
 * @author jknetl
 *
 */
public interface ResizableIcon extends Icon {

	void setIconWidth(int width);

	void setIconHeight(int height);

}
