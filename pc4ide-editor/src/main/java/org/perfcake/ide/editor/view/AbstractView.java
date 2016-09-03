/**
 *
 */
package org.perfcake.ide.editor.view;

import org.perfcake.ide.editor.layout.LayoutData;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AbstractView} implements some of the methods of {@link ComponentView} interface.
 * @author jknetl
 *
 */
public abstract class AbstractView implements ComponentView {

	private boolean isSelected = false;
	private List<ComponentView> children = new ArrayList<>();
	private ComponentView parent;

	protected LayoutData layoutData;

	public AbstractView(ComponentView parent) {
		super();
		this.parent = parent;
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
	public LayoutData getLayoutData() {
		return layoutData;
	}

	@Override
	public void setLayoutData(LayoutData layoutData) {
		this.layoutData = layoutData;
	}

	@Override
	public List<ComponentView> getChildren() {
		return children;
	}

	@Override
	public ComponentView getParent() {
		return parent;
	}

	@Override
	public void addChild(ComponentView view) {
		children.add(view);
	}

	@Override
	public boolean removeChild(ComponentView view) {
		return children.remove(view);
	}

}
