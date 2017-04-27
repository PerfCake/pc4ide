/*
 *-----------------------------------------------------------------------------
 * pc4ide
 *
 * Copyright 2017 Jakub Knetl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *-----------------------------------------------------------------------------
 */

package org.perfcake.ide.editor.controller.visitor;

import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import org.perfcake.ide.editor.controller.Controller;

/**
 * ToolTipVisitor displays tooltip of a component.
 *
 * @author Jakub Knetl
 */
public class ToolTipVisitor extends ViewTargetedVisitor {

    private JPanel jPanel;
    private MouseEvent mouseEvent;

    public ToolTipVisitor(JPanel jPanel, MouseEvent mouseEvent) {
        super(mouseEvent.getPoint());
        this.jPanel = jPanel;
        this.mouseEvent = mouseEvent;
    }

    @Override
    protected void performOperation(Controller controller) {
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance().sharedInstance();

        jPanel.setToolTipText(controller.getView().getToolTip(mouseEvent.getPoint()));
        //toolTipManager.setInitialDelay(200);
        //toolTipManager.setDismissDelay(1000);
        toolTipManager.mouseMoved(mouseEvent);

    }
}
