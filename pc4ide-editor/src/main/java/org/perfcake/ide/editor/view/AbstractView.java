/**
 *
 */
package org.perfcake.ide.editor.view;

/**
 * {@link AbstractView} implements some of the methods of {@link ComponentView} interface.
 * @author jknetl
 *
 */
public abstract class AbstractView implements ComponentView {

	private boolean isSelected = false;

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void setSelected(boolean selected) {
		if (this.isSelected != selected){
			this.isSelected = selected;
		}
	}
}
