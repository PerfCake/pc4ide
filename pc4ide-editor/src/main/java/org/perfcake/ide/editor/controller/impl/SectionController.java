/**
 *
 */
package org.perfcake.ide.editor.controller.impl;

import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.layout.SimpleCircularLayoutManager;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.impl.SectorView;

import javax.swing.Icon;

import java.awt.Graphics;

/**
 * @author jknetl
 *
 */
public class SectionController extends AbstractController {

	private ComponentView view;
	private AbstractModel model;

	public SectionController(String sectionName, Icon icon, AbstractModel model) {
		super();
		this.model = model;
		this.layoutManager = new SimpleCircularLayoutManager(this);
		final ComponentView parentView = (getParent() == null) ? null : getParent().getView();
		view = new SectorView(parentView, sectionName, icon);
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
