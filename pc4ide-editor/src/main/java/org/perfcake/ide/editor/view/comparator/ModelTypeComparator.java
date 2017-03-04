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

package org.perfcake.ide.editor.view.comparator;

import java.util.Comparator;
import java.util.HashMap;
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
 * ModelTypeComparator is {@link java.util.Comparator} which is able to compare Model objects. It contains Map of model type priorities,
 * Each model object is assigned some priority based on actual implementation class (type) of a model. So if objects have different type
 * then comparison is made based on type priorities. The lower priority, the less instance is.
 * If instance's type has no priority defined, then it has maximum priority.
 * If two instances have same priorities (e.g. they have same type), then they are considered equal.
 * the other.
 *
 * @author Jakub Knetl
 */
public class ModelTypeComparator implements Comparator<Model> {

    private HashMap<Class<? extends Model>, Integer> priorities;

    public ModelTypeComparator() {
        this.priorities = createPriorities();
    }

    private HashMap<Class<? extends Model>, Integer> createPriorities() {
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

    @Override
    public int compare(Model o1, Model o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }

        if (o1 == null) {
            return 1;
        }

        if (o2 == null) {
            return -1;
        }

        int priority1 = getPriority(o1.getClass());
        int priority2 = getPriority(o1.getClass());

        if (priority1 == priority2) {
            return 0;
        }

        if (priority1 < priority2) {
            return -1;
        } else {
            return 1;
        }
    }

    public HashMap<Class<? extends Model>, Integer> getPriorities() {
        return priorities;
    }

    /**
     * Gets priority of an type. If instance has no priority set, then Integer.MAX_VALUE is returned.
     *
     * @param clazz clazz which represents model type
     * @return a priority of type which represents model.
     */
    public int getPriority(Class<? extends Model> clazz) {
        return (priorities.get(clazz) == null ? Integer.MAX_VALUE : priorities.get(clazz));
    }
}
