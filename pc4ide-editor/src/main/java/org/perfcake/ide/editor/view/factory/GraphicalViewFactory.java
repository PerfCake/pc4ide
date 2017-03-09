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

package org.perfcake.ide.editor.view.factory;

import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.editor.view.View;
import org.perfcake.ide.editor.view.impl.CorrelatorView;
import org.perfcake.ide.editor.view.impl.DestinationView;
import org.perfcake.ide.editor.view.impl.GeneratorView;
import org.perfcake.ide.editor.view.impl.MessageView;
import org.perfcake.ide.editor.view.impl.ReceiverView;
import org.perfcake.ide.editor.view.impl.ReporterView;
import org.perfcake.ide.editor.view.impl.ScenarioView;
import org.perfcake.ide.editor.view.impl.SenderView;
import org.perfcake.ide.editor.view.impl.SequenceView;
import org.perfcake.ide.editor.view.impl.ValidatorView;

/**
 * Implementation of view factory for graphical editor.
 *
 * @author Jakub Knetl
 */
public class GraphicalViewFactory implements ViewFactory {

    @Override
    public View createView(Model model) {

        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }

        View view = null;
        switch (model.getComponent()) {
            case GENERATOR:
                view = new GeneratorView();
                break;
            case CORRELATOR:
                view = new CorrelatorView();
                break;
            case DESTINATION:
                view = new DestinationView();
                break;
            case MESSAGE:
                view = new MessageView();
                break;
            case RECEIVER:
                view = new ReceiverView();
                break;
            case REPORTER:
                view = new ReporterView();
                break;
            case SCENARIO:
                view = new ScenarioView();
                break;
            case SENDER:
                view = new SenderView();
                break;
            case SEQUENCE:
                view = new SequenceView();
                break;
            case VALIDATOR:
                view = new ValidatorView();
                break;
            default:
                return null;
        }

        return view;
    }
}
