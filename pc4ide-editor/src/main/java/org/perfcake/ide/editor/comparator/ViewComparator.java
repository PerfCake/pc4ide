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

package org.perfcake.ide.editor.comparator;

import java.util.HashMap;
import java.util.Map;
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
 * Implementation of {@link ImplementationPriorityComparator}.
 *
 * @author Jakub Knetl
 */
public class ViewComparator extends ImplementationPriorityComparator<View> {

    public ViewComparator() {
        super();
    }

    @Override
    protected Map<Class<? extends View>, Integer> initializePriorities() {
        Map<Class<? extends View>, Integer> map = new HashMap<>();

        map.put(ScenarioView.class, 0);
        map.put(GeneratorView.class, 100);
        map.put(SenderView.class, 200);
        map.put(ReceiverView.class, 300);
        map.put(CorrelatorView.class, 310);
        map.put(SequenceView.class, 400);
        map.put(ReporterView.class, 500);
        map.put(DestinationView.class, 510);
        map.put(MessageView.class, 600);
        map.put(ValidatorView.class, 700);

        return map;
    }
}
