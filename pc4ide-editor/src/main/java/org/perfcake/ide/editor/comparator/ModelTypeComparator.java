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
import org.perfcake.ide.core.model.Model;
import org.perfcake.ide.core.model.components.CorrelatorModel;
import org.perfcake.ide.core.model.components.DestinationModel;
import org.perfcake.ide.core.model.components.GeneratorModel;
import org.perfcake.ide.core.model.components.MessageModel;
import org.perfcake.ide.core.model.components.ReceiverModel;
import org.perfcake.ide.core.model.components.ReporterModel;
import org.perfcake.ide.core.model.components.ScenarioModel;
import org.perfcake.ide.core.model.components.SenderModel;
import org.perfcake.ide.core.model.components.SequenceModel;
import org.perfcake.ide.core.model.components.ValidatorModel;

/**
 * Implementation of {@link ImplementationPriorityComparator}.
 * @author Jakub Knetl
 */
public class ModelTypeComparator extends ImplementationPriorityComparator<Model> {

    private HashMap<Class<? extends Model>, Integer> priorities;

    public ModelTypeComparator() {
        super();
    }

    @Override
    protected Map<Class<? extends Model>, Integer> initializePriorities() {
        HashMap<Class<? extends Model>, Integer> map = new HashMap<>();

        map.put(ScenarioModel.class, 0);
        map.put(GeneratorModel.class, 100);
        map.put(SenderModel.class, 200);
        map.put(ReceiverModel.class, 300);
        map.put(CorrelatorModel.class, 310);
        map.put(SequenceModel.class, 400);
        map.put(ReporterModel.class, 500);
        map.put(DestinationModel.class, 510);
        map.put(MessageModel.class, 600);
        map.put(ValidatorModel.class, 700);

        return priorities;
    }

}
