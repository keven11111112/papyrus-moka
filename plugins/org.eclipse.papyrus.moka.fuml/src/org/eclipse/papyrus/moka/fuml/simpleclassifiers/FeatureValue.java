/*****************************************************************************
 * Copyright (c) 2012 CEA LIST.
 *
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  CEA LIST - Initial API and implementation
 *
 *****************************************************************************/
package org.eclipse.papyrus.moka.fuml.simpleclassifiers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.StructuralFeature;

public class FeatureValue implements IFeatureValue {

	/*
	 * The structural feature being given value(s).
	 */
	public StructuralFeature feature;

	/*
	 * The values of for the feature. Zero or more values are possible, as
	 * constrained by the multiplicity of the feature.
	 */
	public List<IValue> values = new ArrayList<IValue>();

	/*
	 * The position of this feature value in a set of ordered values for a
	 * feature of an association. [This is only relevant if the feature value is
	 * for a link and the feature is ordered.]
	 */
	public Integer position;

	public Boolean hasEqualValues(IFeatureValue other) {
		// Determine if this feature value has an equal set of values as another
		// feature value.
		// If the feature is ordered, then the values also have to be in the
		// same order.
		boolean equal = true;
		if (this.values.size() != other.getValues().size()) {
			equal = false;
		} else {
			// Debug.println("[hasEqualValues] feature = " + this.feature.name +
			// ", " + this.values.size() + " value(s).");
			if (this.feature.isOrdered()) {
				int i = 1;
				while (equal & i <= this.values.size()) {
					equal = this.values.get(i - 1).equals(other.getValues().get(i - 1));
					i = i + 1;
				}
			} else {
				// Note: otherFeatureValues is used here solely as a holder for
				// a copy of the list of other values,
				// since the Java to UML mapping conventions do not allow
				// "remove" on a local list variable.
				FeatureValue otherFeatureValues = new FeatureValue();
				List<IValue> values = other.getValues();
				for (int i = 0; i < values.size(); i++) {
					IValue value = values.get(i);
					otherFeatureValues.values.add(value);
				}
				int i = 1;
				while (equal & i <= this.values.size()) {
					// Debug.println("[hasEqualValues] This value [" + (i-1) +
					// "] = " + this.values.get(i-1));
					boolean matched = false;
					int j = 1;
					while (!matched & j <= otherFeatureValues.values.size()) {
						if (this.values.get(i - 1).equals(otherFeatureValues.values.get(j - 1))) {
							// Debug.println("[hasEqualValues] Other value [" +
							// (j-1) + "] = " +
							// otherFeatureValues.values.get(j-1));
							matched = true;
							otherFeatureValues.values.remove(j - 1);
						}
						j = j + 1;
					}
					equal = matched;
					i = i + 1;
				}
			}
		}
		return equal;
	}

	public IFeatureValue copy() {
		// Create a copy of this feature value.
		IFeatureValue newValue = new FeatureValue();
		newValue.setFeature(this.feature);
		newValue.setPosition(this.position);
		List<IValue> values = this.values;
		for (int i = 0; i < values.size(); i++) {
			IValue value = values.get(i);
			newValue.getValues().add(value.copy());
		}
		return newValue;
	}

	public List<IValue> getValues() {
		return this.values;
	}

	public void setValues(List<IValue> values) {
		if (values != null) {
			this.values = values;
		}
	}

	public StructuralFeature getFeature() {
		return this.feature;
	}

	public void setFeature(StructuralFeature feature) {
		this.feature = feature;
	}

	public Integer getPosition() {
		return this.position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
}
