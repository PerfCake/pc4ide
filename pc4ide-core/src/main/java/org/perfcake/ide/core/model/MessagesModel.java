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

import org.perfcake.model.Scenario.Messages;

public class MessagesModel extends AbstractModel {

	public static final String PROPERTY_MESSAGE = "messages-message";

	private Messages messages;

	MessagesModel(Messages messages) {
		super();
		if (messages == null) {
			throw new IllegalArgumentException("Messages must not be null");
		}
		this.messages = messages;
	}

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

	public void addMessage(MessageModel m) {
		addMessage(messages.getMessage().size(), m);
	}

	public void addMessage(int index, MessageModel m) {
		messages.getMessage().add(index, m.getMessage());
		getMapper().bind(m.getMessage(), m);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_MESSAGE, null, m);
	}

	public void removeMessage(MessageModel m) {
		if (messages.getMessage().remove(m.getMessage())) {
			getMapper().unbind(m.getMessage(), m);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_MESSAGE, m, null);
		}
	}
}
