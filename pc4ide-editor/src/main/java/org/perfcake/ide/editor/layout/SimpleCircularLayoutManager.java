/**
 *
 */
package org.perfcake.ide.editor.layout;

import org.perfcake.ide.editor.controller.Controller;

import java.util.Iterator;

/**
 *
 * Simple circular manager lays out the children into circular secotrs. It computes angular
 * extent of each child equally so that each child has same extent.
 *
 * @author jknetl
 *
 */
public class SimpleCircularLayoutManager implements LayoutManager {

	// layout data that are provided to this layout manager so it can split them up to children components
	private LayoutData dataForChildren;
	//controller for which this layout manager manages children
	private Controller controller;

	/**
	 * @param dataForChildren LayoutData which can this layout manager use to layout the children
	 * @param controller Controller whose children are managed by this manger
	 */
	public SimpleCircularLayoutManager(Controller controller) {
		super();
		this.controller = controller;
	}

	@Override
	public void layoutChildren() {

		final int numOfChildren = computeChildren();

		final double angularExtendForChild = dataForChildren.getAngularData().getAngleExtent() / numOfChildren;
		double startAngle = dataForChildren.getAngularData().getStartAngle();

		final Iterator<Controller> it = controller.getChildrenIterator();
		while (it.hasNext()) {
			final Controller child = it.next();
			final LayoutData data = new LayoutData(dataForChildren);
			data.getAngularData().setAngleExtent(angularExtendForChild);
			data.getAngularData().setStartAngle(startAngle);
			//sets layout data to child layoutManager
			//TODO(jknetl): there should be called setLayoutData only once!!!
			child.setLayoutData(data);
			//sets layout data to child view
			child.getView().setLayoutData(data);
			startAngle += angularExtendForChild;
		}

	}

	private int computeChildren() {
		int i = 0;
		final Iterator<Controller> it = controller.getChildrenIterator();
		while (it.hasNext()) {
			i++;
			it.next();
		}

		return i;
	}

	@Override
	public void setLayoutData(LayoutData data) {
		dataForChildren = data;

	}

	@Override
	public LayoutData getLayoutData() {
		return dataForChildren;
	}

}
