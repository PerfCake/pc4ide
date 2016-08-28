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

import org.perfcake.model.Header;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario.Messages.Message;
import org.perfcake.model.Scenario.Messages.Message.ValidatorRef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageModel extends AbstractModel implements PropertyContainer {

	public static final String PROPERTY_HEADERS = "message-header";
	public static final String PROPERTY_PROPERTIES = "message-property";
	public static final String PROPERTY_VALIDATOR_REFS = "message-validator-ref";
	public static final String PROPERTY_URI = "message-uri";
	public static final String PROPERTY_MULTIPLICITY = "message-multiplicity";
	private static final String PROPERTY_CONTENT = "message-content";

	private Message message;

	MessageModel(Message message) {
		super();
		if (message == null) {
			throw new IllegalArgumentException("Message must not be null.");
		}
		this.message = message;

		if (message.getHeader() != null) {
			for (final Header h : message.getHeader()) {
				addHeader(new HeaderModel(h));
			}
		}

		if (message.getProperty() != null) {
			for (final Property p : message.getProperty()) {
				addProperty(new PropertyModel(p));
			}
		}

		if (message.getValidatorRef() != null) {
			for (final ValidatorRef v : message.getValidatorRef()) {
				addValidatorRef(new ValidatorRefModel(v));
			}
		}
	}

	public MessageModel() {
		super();
		this.message = new Message();
	}

	/**
	 * This method should not be used for modifying message (in a way getMessage().setUri(uri))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Message
	 */
	Message getMessage() {
		return message;
	}

	@Override
	public void addProperty(PropertyModel Property) {
		addProperty(message.getProperty().size(), Property);
	}

	@Override
	public void addProperty(int index, PropertyModel property) {
		message.getProperty().add(index, property.getProperty());
		getMapper().bind(property.getProperty(), property);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, null, property);
	}

	@Override
	public void removeProperty(PropertyModel property) {
		if (message.getProperty().remove(property)) {
			getMapper().unbind(property.getProperty(), property);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_PROPERTIES, property, null);
		}
	}

	@Override
	public List<PropertyModel> getProperty() {
		final List<PropertyModel> result = MapperUtils.getPc4ideList(message.getProperty(), getMapper());
		return Collections.unmodifiableList(result);
	}

	public void addHeader(HeaderModel header) {
		addHeader(message.getHeader().size(), header);
	}

	public void addHeader(int index, HeaderModel header) {
		message.getHeader().add(index, header.getHeader());
		getMapper().bind(header.getHeader(), header);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_HEADERS, null, header);
	}

	public void removeHeader(HeaderModel header) {
		if (message.getHeader().remove(header.getHeader())) {
			getMapper().unbind(header.getHeader(), header);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_HEADERS, header, null);
		}
	}

	public List<HeaderModel> getHeader() {
		final List<HeaderModel> result = MapperUtils.getPc4ideList(message.getHeader(), getMapper());
		return Collections.unmodifiableList(result);
	}

	public void addValidatorRef(ValidatorRefModel ref) {
		addValidatorRef(message.getValidatorRef().size(), ref);
	}

	public void addValidatorRef(int index, ValidatorRefModel ref) {
		message.getValidatorRef().add(index, ref.getValidatorRef());
		getMapper().bind(ref.getValidatorRef(), ref);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_VALIDATOR_REFS, null, ref);
	}

	public void removeValidatorRef(ValidatorRefModel ref) {
		if (message.getValidatorRef().remove(ref.getValidatorRef())) {
			getMapper().unbind(ref.getValidatorRef(), ref);
			getPropertyChangeSupport().firePropertyChange(PROPERTY_VALIDATOR_REFS, ref, null);
		}
	}

	public List<ValidatorRefModel> getValidatorRef() {
		final List<ValidatorRefModel> result = MapperUtils.getPc4ideList(message.getValidatorRef(), getMapper());
		return Collections.unmodifiableList(result);
	}

	public String getUri() {
		return message.getUri();
	}

	public void setUri(String uri) {
		final String oldUri = message.getUri();
		message.setUri(uri);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_URI, oldUri, uri);
	}

	public String getMultiplicity() {
		return message.getMultiplicity();
	}

	public void setMultiplicity(String multiplicity) {
		final String oldMultiplicity = message.getMultiplicity();
		message.setMultiplicity(multiplicity);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_MULTIPLICITY, oldMultiplicity, multiplicity);
	}

	public String getContent() {
		return message.getContent();
	}

	public void setContent(String content) {
		final String oldContent = message.getContent();
		message.setContent(content);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_CONTENT, oldContent, content);
	}

	@Override
	public List<AbstractModel> getModelChildren() {
		final List<AbstractModel> children = new ArrayList<>();
		children.addAll(getProperty());
		children.addAll(getHeader());
		children.addAll(getValidatorRef());
		return children;
	}
}
