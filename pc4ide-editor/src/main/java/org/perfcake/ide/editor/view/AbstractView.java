/**
 *
 */
package org.perfcake.ide.editor.view;

import org.perfcake.ide.editor.controller.Controller;
import org.perfcake.ide.editor.layout.LayoutData;
import org.perfcake.ide.editor.layout.LayoutManager;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link AbstractView} implements some of the methods of {@link ComponentView} interface.
 *
 * @author jknetl
 */
public abstract class AbstractView implements ComponentView {

	private boolean isSelected = false;
	private List<ComponentView> children = new ArrayList<>();
	private ComponentView parent;
	protected boolean isValid;

	protected LayoutData layoutData;
	protected LayoutManager layoutManager;

	public AbstractView(ComponentView parent) {
		super();
		this.parent = parent;
		isValid = false;
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void setSelected(boolean selected) {
		if (this.isSelected != selected) {
			this.isSelected = selected;
		}
	}

	@Override
	public boolean isValid() {
		return isValid;
	}

	@Override
	public void invalidate() {
		isValid = false;
		if (parent != null) {
			// indicate to parent that this controller needs validation
			parent.invalidate();
		}
	}

	@Override
	public void validate(Graphics2D g2d) {
		if (layoutManager != null) {
			layoutManager.layout(g2d);
		}
		for (final ComponentView view : children) {
			view.validate(g2d);
		}
		isValid = true;
	}

	@Override
	public LayoutData getLayoutData() {
		return layoutData;
	}

	@Override
	public void setLayoutData(LayoutData layoutData) {
		this.layoutData = layoutData;
	}

	@Override
	public List<ComponentView> getChildren() {
		return Collections.unmodifiableList(children);
	}

	@Override
	public ComponentView getParent() {
		return parent;
	}

	@Override
	public void setParent(ComponentView parent) {
		this.parent = parent;
	}

	@Override
	public void addChild(ComponentView view) {
		children.add(view);
		if (layoutManager != null){
			layoutManager.add(view);
		}
	}

	@Override
	public boolean removeChild(ComponentView view) {
		if (layoutManager != null){
			layoutManager.remove(view);
		}
		return children.remove(view);
	}
}
