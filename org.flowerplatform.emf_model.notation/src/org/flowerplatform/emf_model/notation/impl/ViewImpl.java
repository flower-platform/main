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
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.notation.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.flowerplatform.emf_model.notation.Edge;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationPackage;
import org.flowerplatform.emf_model.notation.View;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>View</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.ViewImpl#getViewType <em>View Type</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.ViewImpl#getPersistentChildren <em>Persistent Children</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.ViewImpl#getViewDetails <em>View Details</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.ViewImpl#getDiagrammableElement <em>Diagrammable Element</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.ViewImpl#getSourceEdges <em>Source Edges</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.ViewImpl#getTargetEdges <em>Target Edges</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ViewImpl extends NotationElementImpl implements View {
	/**
	 * The default value of the '{@link #getViewType() <em>View Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViewType()
	 * @generated
	 * @ordered
	 */
	protected static final String VIEW_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getViewType() <em>View Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViewType()
	 * @generated
	 * @ordered
	 */
	protected String viewType = VIEW_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPersistentChildren() <em>Persistent Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPersistentChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<Node> persistentChildren;

	/**
	 * The default value of the '{@link #getViewDetails() <em>View Details</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViewDetails()
	 * @generated
	 * @ordered
	 */
	protected static final Object VIEW_DETAILS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getViewDetails() <em>View Details</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViewDetails()
	 * @generated
	 * @ordered
	 */
	protected Object viewDetails = VIEW_DETAILS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDiagrammableElement() <em>Diagrammable Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiagrammableElement()
	 * @generated
	 * @ordered
	 */
	protected EObject diagrammableElement;

	/**
	 * The cached value of the '{@link #getSourceEdges() <em>Source Edges</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceEdges()
	 * @generated
	 * @ordered
	 */
	protected EList<Edge> sourceEdges;

	/**
	 * The cached value of the '{@link #getTargetEdges() <em>Target Edges</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetEdges()
	 * @generated
	 * @ordered
	 */
	protected EList<Edge> targetEdges;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ViewImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NotationPackage.Literals.VIEW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getViewType() {
		return viewType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setViewType(String newViewType) {
		String oldViewType = viewType;
		viewType = newViewType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.VIEW__VIEW_TYPE, oldViewType, viewType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getPersistentChildren() {
		if (persistentChildren == null) {
			persistentChildren = new EObjectContainmentEList<Node>(Node.class, this, NotationPackage.VIEW__PERSISTENT_CHILDREN);
		}
		return persistentChildren;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getViewDetails() {
		return viewDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public View getParentView() {
		return (View) eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setViewDetails(Object newViewDetails) {
		Object oldViewDetails = viewDetails;
		viewDetails = newViewDetails;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.VIEW__VIEW_DETAILS, oldViewDetails, viewDetails));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getDiagrammableElement() {
		if (diagrammableElement != null && diagrammableElement.eIsProxy()) {
			InternalEObject oldDiagrammableElement = (InternalEObject)diagrammableElement;
			diagrammableElement = eResolveProxy(oldDiagrammableElement);
			if (diagrammableElement != oldDiagrammableElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, NotationPackage.VIEW__DIAGRAMMABLE_ELEMENT, oldDiagrammableElement, diagrammableElement));
			}
		}
		return diagrammableElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetDiagrammableElement() {
		return diagrammableElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiagrammableElement(EObject newDiagrammableElement) {
		EObject oldDiagrammableElement = diagrammableElement;
		diagrammableElement = newDiagrammableElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.VIEW__DIAGRAMMABLE_ELEMENT, oldDiagrammableElement, diagrammableElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Edge> getSourceEdges() {
		if (sourceEdges == null) {
			sourceEdges = new EObjectWithInverseResolvingEList<Edge>(Edge.class, this, NotationPackage.VIEW__SOURCE_EDGES, NotationPackage.EDGE__SOURCE);
		}
		return sourceEdges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Edge> getTargetEdges() {
		if (targetEdges == null) {
			targetEdges = new EObjectWithInverseResolvingEList<Edge>(Edge.class, this, NotationPackage.VIEW__TARGET_EDGES, NotationPackage.EDGE__TARGET);
		}
		return targetEdges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getAllChildren() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NotationPackage.VIEW__SOURCE_EDGES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSourceEdges()).basicAdd(otherEnd, msgs);
			case NotationPackage.VIEW__TARGET_EDGES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getTargetEdges()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NotationPackage.VIEW__PERSISTENT_CHILDREN:
				return ((InternalEList<?>)getPersistentChildren()).basicRemove(otherEnd, msgs);
			case NotationPackage.VIEW__SOURCE_EDGES:
				return ((InternalEList<?>)getSourceEdges()).basicRemove(otherEnd, msgs);
			case NotationPackage.VIEW__TARGET_EDGES:
				return ((InternalEList<?>)getTargetEdges()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NotationPackage.VIEW__VIEW_TYPE:
				return getViewType();
			case NotationPackage.VIEW__PERSISTENT_CHILDREN:
				return getPersistentChildren();
			case NotationPackage.VIEW__VIEW_DETAILS:
				return getViewDetails();
			case NotationPackage.VIEW__DIAGRAMMABLE_ELEMENT:
				if (resolve) return getDiagrammableElement();
				return basicGetDiagrammableElement();
			case NotationPackage.VIEW__SOURCE_EDGES:
				return getSourceEdges();
			case NotationPackage.VIEW__TARGET_EDGES:
				return getTargetEdges();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case NotationPackage.VIEW__VIEW_TYPE:
				setViewType((String)newValue);
				return;
			case NotationPackage.VIEW__PERSISTENT_CHILDREN:
				getPersistentChildren().clear();
				getPersistentChildren().addAll((Collection<? extends Node>)newValue);
				return;
			case NotationPackage.VIEW__VIEW_DETAILS:
				setViewDetails(newValue);
				return;
			case NotationPackage.VIEW__DIAGRAMMABLE_ELEMENT:
				setDiagrammableElement((EObject)newValue);
				return;
			case NotationPackage.VIEW__SOURCE_EDGES:
				getSourceEdges().clear();
				getSourceEdges().addAll((Collection<? extends Edge>)newValue);
				return;
			case NotationPackage.VIEW__TARGET_EDGES:
				getTargetEdges().clear();
				getTargetEdges().addAll((Collection<? extends Edge>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case NotationPackage.VIEW__VIEW_TYPE:
				setViewType(VIEW_TYPE_EDEFAULT);
				return;
			case NotationPackage.VIEW__PERSISTENT_CHILDREN:
				getPersistentChildren().clear();
				return;
			case NotationPackage.VIEW__VIEW_DETAILS:
				setViewDetails(VIEW_DETAILS_EDEFAULT);
				return;
			case NotationPackage.VIEW__DIAGRAMMABLE_ELEMENT:
				setDiagrammableElement((EObject)null);
				return;
			case NotationPackage.VIEW__SOURCE_EDGES:
				getSourceEdges().clear();
				return;
			case NotationPackage.VIEW__TARGET_EDGES:
				getTargetEdges().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case NotationPackage.VIEW__VIEW_TYPE:
				return VIEW_TYPE_EDEFAULT == null ? viewType != null : !VIEW_TYPE_EDEFAULT.equals(viewType);
			case NotationPackage.VIEW__PERSISTENT_CHILDREN:
				return persistentChildren != null && !persistentChildren.isEmpty();
			case NotationPackage.VIEW__VIEW_DETAILS:
				return VIEW_DETAILS_EDEFAULT == null ? viewDetails != null : !VIEW_DETAILS_EDEFAULT.equals(viewDetails);
			case NotationPackage.VIEW__DIAGRAMMABLE_ELEMENT:
				return diagrammableElement != null;
			case NotationPackage.VIEW__SOURCE_EDGES:
				return sourceEdges != null && !sourceEdges.isEmpty();
			case NotationPackage.VIEW__TARGET_EDGES:
				return targetEdges != null && !targetEdges.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (viewType: ");
		result.append(viewType);
		result.append(", viewDetails: ");
		result.append(viewDetails);
		result.append(')');
		return result.toString();
	}

} //ViewImpl