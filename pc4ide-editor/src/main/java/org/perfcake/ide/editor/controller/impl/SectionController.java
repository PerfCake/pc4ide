/**
 *
 */
package org.perfcake.ide.editor.controller.impl;

import org.perfcake.ide.core.model.AbstractModel;
import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.icons.ResizableIcon;
import org.perfcake.ide.editor.view.impl.SectorView;

/**
 * @author jknetl
 *
 */
public class SectionController extends AbstractController {

	private ComponentView view;

	public SectionController(String sectionName, ResizableIcon icon, AbstractModel model) {
		super(model);
		final ComponentView parentView = (getParent() == null) ? null : getParent().getView();
		view = new SectorView(parentView, sectionName, icon);
	}

	@Override
	public ComponentView getView() {
		return view;
	}

}
