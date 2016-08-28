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

import java.util.ArrayList;
import java.util.List;

public class HeaderModel extends AbstractModel {

	public static final String PROPERTY_NAME = "header-name";
	public static final String PROPERTY_VALUE = "header-value";

	private Header header;

	HeaderModel(Header header) {
		super();
		if (header == null) {
			throw new IllegalArgumentException("Header must not be null");
		}
		this.header = header;
	}

	public HeaderModel() {
		super();
		this.header = new Header();
	}

	/**
	 * This method should not be used for modifying header (in a way getHeader().setName()))
	 * since these changes would not fire PropertyChange getListeners(). Use set methods of this class instead.
	 *
	 * @return PerfCake model of Header
	 */
	Header getHeader() {
		return header;
	}

	public String getName() {
		return header.getName();
	}

	public void setName(String name) {
		final String oldName = header.getName();
		header.setName(name);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_NAME, oldName, name);
	}

	public String getValue() {
		return header.getValue();
	}

	public void setValue(String value) {
		final String oldValue = header.getValue();
		header.setValue(value);
		getPropertyChangeSupport().firePropertyChange(PROPERTY_VALUE, oldValue, value);
	}

	@Override
	public List<AbstractModel> getModelChildren() {
		final List<AbstractModel> children = new ArrayList<>();

		return children;
	}
}
