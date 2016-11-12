package org.perfcake.pc4ide.netbeans;

import org.netbeans.spi.palette.PaletteActions;
import org.openide.util.Lookup;

import javax.swing.Action;

/**
 * Represents a no-operation palette actions.
 */
public class NoopPaletteActions extends PaletteActions {

	@Override
	public Action[] getImportActions() {
		return new Action[0];
	}

	@Override
	public Action[] getCustomPaletteActions() {
		return new Action[0];
	}

	@Override
	public Action[] getCustomCategoryActions(Lookup category) {
		return new Action[0];
	}

	@Override
	public Action[] getCustomItemActions(Lookup item) {
		return new Action[0];
	}

	@Override
	public Action getPreferredAction(Lookup item) {
		return null;
	}
}
