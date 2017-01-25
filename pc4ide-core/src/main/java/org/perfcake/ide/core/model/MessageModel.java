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

import org.perfcake.model.HeaderType;
import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario.Messages.Message;
import org.perfcake.model.Scenario.Messages.Message.ValidatorRef;

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
            for (final HeaderType h : message.getHeader()) {
                getMapper().bind(h, new HeaderModel(h));
            }
        }

        if (message.getProperty() != null) {
            for (final PropertyType p : message.getProperty()) {
                getMapper().bind(p, new PropertyModel(p));
            }
        }

        if (message.getValidatorRef() != null) {
            for (final ValidatorRef v : message.getValidatorRef()) {
                getMapper().bind(v, new ValidatorRefModel(v));
            }
        }
    }

    /**
     * Creates new message model.
     */
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

    /**
     * Adds a header to message.
     *
     * @param header header to be added.
     */
    public void addHeader(HeaderModel header) {
        addHeader(message.getHeader().size(), header);
    }

    /**
     * Adds a header to message.
     *
     * @param index index of the header.
     * @param header header to be added.
     */
    public void addHeader(int index, HeaderModel header) {
        message.getHeader().add(index, header.getHeader());
        getMapper().bind(header.getHeader(), header);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_HEADERS, null, header);
    }

    /**
     * Removes a header from the message.
     *
     * @param header header to be removed
     */
    public void removeHeader(HeaderModel header) {
        if (message.getHeader().remove(header.getHeader())) {
            getMapper().unbind(header.getHeader(), header);
            getPropertyChangeSupport().firePropertyChange(PROPERTY_HEADERS, header, null);
        }
    }

    /**
     * Gets unmodifiable list of headers of the message.
     * @return Unmodifiable list of headers
     */
    public List<HeaderModel> getHeader() {
        final List<HeaderModel> result = MapperUtils.getPc4ideList(message.getHeader(), getMapper());
        return Collections.unmodifiableList(result);
    }

    /**
     * Adds a validator reference to the message.
     *
     * @param ref Validator reference to be added.
     */
    public void addValidatorRef(ValidatorRefModel ref) {
        addValidatorRef(message.getValidatorRef().size(), ref);
    }

    /**
     * Adds a validator reference to the message.
     *
     * @param index index of the validator reference
     * @param ref Validator reference to be added.
     */
    public void addValidatorRef(int index, ValidatorRefModel ref) {
        message.getValidatorRef().add(index, ref.getValidatorRef());
        getMapper().bind(ref.getValidatorRef(), ref);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_VALIDATOR_REFS, null, ref);
    }

    /**
     * Removes a validator reference from the message.
     *
     * @param ref validator reference to be removed.
     */
    public void removeValidatorRef(ValidatorRefModel ref) {
        if (message.getValidatorRef().remove(ref.getValidatorRef())) {
            getMapper().unbind(ref.getValidatorRef(), ref);
            getPropertyChangeSupport().firePropertyChange(PROPERTY_VALIDATOR_REFS, ref, null);
        }
    }

    /**
     * Gets a unmodifiable list of validator references.
     *
     * @return Unmodifiable list of validator references.
     */
    public List<ValidatorRefModel> getValidatorRef() {
        final List<ValidatorRefModel> result = MapperUtils.getPc4ideList(message.getValidatorRef(), getMapper());
        return Collections.unmodifiableList(result);
    }

    public String getUri() {
        return message.getUri();
    }

    /**
     * Sets uri of the message.
     *
     * @param uri URI of the message
     */
    public void setUri(String uri) {
        final String oldUri = message.getUri();
        message.setUri(uri);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_URI, oldUri, uri);
    }

    public String getMultiplicity() {
        return message.getMultiplicity();
    }

    /**
     * Sets multiplicity of the message.
     *
     * @param multiplicity multiplicity to be set
     */
    public void setMultiplicity(String multiplicity) {
        final String oldMultiplicity = message.getMultiplicity();
        message.setMultiplicity(multiplicity);
        getPropertyChangeSupport().firePropertyChange(PROPERTY_MULTIPLICITY, oldMultiplicity, multiplicity);
    }

    public String getContent() {
        return message.getContent();
    }

    /**
     * Sets content of the message.
     * @param content content of the message
     */
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
