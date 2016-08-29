/**
 *
 */
package org.perfcake.ide.editor.view.impl;

import org.perfcake.ide.editor.layout.AngularData;
import org.perfcake.ide.editor.layout.RadiusData;
import org.perfcake.ide.editor.view.AbstractView;
import org.perfcake.ide.editor.view.ComponentView;

import java.awt.Graphics;
import java.awt.Shape;

/**
 * @author jknetl
 *
 */
public class EditorView extends AbstractView {




	public EditorView(ComponentView parent) {
		super(parent);
	}

	/* (non-Javadoc)
	 * @see org.perfcake.ide.editor.view.ComponentView#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
	}

	/* (non-Javadoc)
	 * @see org.perfcake.ide.editor.view.ComponentView#getViewBounds()
	 */
	@Override
	public Shape getViewBounds() {
		return null;
	}

	@Override
	public AngularData getPrefferedAngularData(RadiusData radius) {
		return new AngularData();
	}
}
