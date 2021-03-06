/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package com.crispico.flower.mp.codesync.code.adapter;

import java.util.Map;

import com.crispico.flower.mp.codesync.base.AbstractModelAdapter;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.action.ActionResult;

/**
 * 
 */
public abstract class AstModelElementAdapter extends AbstractModelAdapter {

	private ModelAdapterFactorySet modelAdapterFactorySet;
	
	@Override
	public ModelAdapterFactorySet getModelAdapterFactorySet() {
		return modelAdapterFactorySet;
	}
	
	@Override
	public IModelAdapter setModelAdapterFactorySet(ModelAdapterFactorySet modelAdapterFactorySet) {
		this.modelAdapterFactorySet = modelAdapterFactorySet;
		return this;
	}

	@Override
	public void addToMap(Object element, Map<Object, Object> map) {
		map.put(getMatchKey(element), element);
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		throw new UnsupportedOperationException("AstModelElementAdapter.removeFromMap() attempted.");
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		throw new IllegalArgumentException("Attempted to acces value feature " + feature + " for element " + element);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		throw new IllegalArgumentException("Attempted to acces containment feature " + feature + " for element " + element);
	}
	
	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
		// nothing to do
	}
	
	@Override
	public void featuresProcessed(Object element) {
		// nothing to do
	}

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result) {
		// nothing to do
	}

	abstract protected void updateUID(Object element, Object correspondingElement);
	
}