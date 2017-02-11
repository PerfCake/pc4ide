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
/*
 * PerfClispe
 *
 *
 * Copyright (c) 2014 Jakub Knetl
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
 */

package org.perfcake.ide.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.perfcake.model.Scenario.Messages;
import org.perfcake.model.Scenario.Messages.Message;


/**
 * Model of a messages.
 */
public class MessagesModel extends AbstractModel {

    public static final String PROPERTY_MESSAGE = "messages-message";

    private Messages messages;

    MessagesModel(Messages messages) {
        super();
        if (messages == null) {
            throw new IllegalArgumentException("Messages must not be null");
        }
        this.messages = messages;

        if (messages.getMessage() != null) {
            for (final Message m : messages.getMessage()) {
                getMapper().bind(m, new MessageModel(m));
            }
        }
    }

    /**
     * Creates new messages model.
     */
    public MessagesModel() {
        super();
        messages = new Messages();
    }

    /**
     * This method should not be used for modifying messages (in a way getMessages().add(message))
     * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
     *
     * @return PerfCake model of Messages
     */
    Messages getMessages() {
        return messages;
    }

    /**
     * Adds a message.
     * @param m message to be added
     */
    public void addMessage(MessageModel m) {
        addMessage(messages.getMessage().size(), m);
    }

    /**
     * Adds a message to specified index.
     *
     * @param index index of a message
     * @param m message to be added
     */
    public void addMessage(int index, MessageModel m) {
        messages.getMessage().add(index, m.getMessage());
        getMapper().bind(m.getMessage(), m);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_MESSAGE, null, m);
    }

    /**
     * Removes message.
     * @param m message to be removed
     */
    public void removeMessage(MessageModel m) {
        if (messages.getMessage().remove(m.getMessage())) {
            getMapper().unbind(m.getMessage(), m);
            getPropertyChangeSupport().firePropertyChange(PROPERTY_MESSAGE, m, null);
        }
    }

    /**
     * @return Unmodifiable list of messages.
     */
    public List<MessageModel> getMessage() {
        final List<MessageModel> result = MapperUtils.getPc4ideList(messages.getMessage(), getMapper());
        return Collections.unmodifiableList(result);
    }

    @Override
    public List<AbstractModel> getModelChildren() {
        final List<AbstractModel> children = new ArrayList<>();
        children.addAll(getMessage());
        return children;
    }
}
