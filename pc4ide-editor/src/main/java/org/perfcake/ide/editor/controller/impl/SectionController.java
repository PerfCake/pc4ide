/**
 *
 */
package org.perfcake.ide.editor.controller.impl;

import org.perfcake.ide.editor.controller.AbstractController;
import org.perfcake.ide.editor.view.ComponentView;
import org.perfcake.ide.editor.view.icons.GeneratorIcon;
import org.perfcake.ide.editor.view.impl.SectorView;

import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * @author jknetl
 *
 */
public class SectionController extends AbstractController {

	private ComponentView view;



	//TODO(jknetl): encapsulate multiple argument with object (e.g. LayoutData)
	public SectionController(Point2D center, int outerRadius, int innerRadius, double startAngle, double angleExtent) {
		super();
		view = new SectorView("Section", center, outerRadius, innerRadius, startAngle, angleExtent, new GeneratorIcon());
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
