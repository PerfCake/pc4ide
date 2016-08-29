/**
 *
 */
package org.perfcake.ide.editor.controller.impl;

import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.icons.GeneratorIcon;
import org.perfcake.ide.editor.view.impl.SectorView;

import java.awt.Graphics;

/**
 * @author jknetl
 *
 */
public class SectionController extends AbstractController {

	private ComponentView view;

	//TODO(jknetl): encapsulate multiple argument with object (e.g. LayoutData)
	public SectionController(LayoutData data) {
		super(data);
		final ComponentView parentView = (getParent() == null) ? null : getParent().getView();
		view = new SectorView(parentView, "Section", data, new GeneratorIcon());
	}

	@Override
	public void drawView(Graphics g) {
		view.draw(g);
	}

	@Override
	public ComponentView getView() {
		return view;
	}

}
