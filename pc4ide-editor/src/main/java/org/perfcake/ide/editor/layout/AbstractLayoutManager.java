package org.perfcake.ide.editor.layout;

import org.perfcake.ide.editor.view.ComponentView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jknetl on 10/15/16.
 */
public abstract class AbstractLayoutManager implements LayoutManager {
	protected LayoutData constraints;
	protected List<ComponentView> children;

	public AbstractLayoutManager() {
		children = new ArrayList<>();
	}

	@Override
	public void setConstraint(LayoutData constraint) {
		this.constraints = constraint;
	}

	@Override
	public void add(ComponentView component) {
		children.add(component);
	}

	@Override
	public boolean remove(ComponentView component) {
		return children.remove(component);
	}

	@Override
	public List<ComponentView> getChildren() {
		return Collections.unmodifiableList(children);
	}
}
