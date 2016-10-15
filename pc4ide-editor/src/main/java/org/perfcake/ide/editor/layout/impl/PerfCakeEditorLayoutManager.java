package org.perfcake.ide.editor.layout.impl;

import org.perfcake.ide.editor.layout.AbstractLayoutManager;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.LayoutManager;
import org.perfcake.ide.editor.view.ComponentView;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jknetl on 10/15/16.
 */
public class PerfCakeEditorLayoutManager extends AbstractLayoutManager {
	@Override
	public void layout(Graphics2D g2d) {

		Map<ComponentView,Double> extentMap = new HashMap<>();

		double requestedExtentByChildren = 0.0;

		for (ComponentView child : children){
			double extent = child.getMinimumSize(constraints, g2d).getAngularData().getAngleExtent();
			extentMap.put(child, extent);
			requestedExtentByChildren += extent;
		}

		double startAngle = constraints.getAngularData().getStartAngle();

		if (requestedExtentByChildren < constraints.getAngularData().getAngleExtent()) {
			for (ComponentView v : children) {
				final LayoutData data = new LayoutData(constraints);
				data.getAngularData().setAngleExtent(extentMap.get(v));
				data.getAngularData().setStartAngle(startAngle);
				v.setLayoutData(data);
				startAngle += extentMap.get(v);
			}
		} else {
			// TODO: component requested larger angular extent than is available
			// we need to somehow implement shirnking of some views!
		}
	}

}
