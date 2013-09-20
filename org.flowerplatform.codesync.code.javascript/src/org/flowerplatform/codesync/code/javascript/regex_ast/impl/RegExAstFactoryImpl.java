/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.codesync.code.javascript.regex_ast.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.flowerplatform.codesync.code.javascript.regex_ast.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RegExAstFactoryImpl extends EFactoryImpl implements RegExAstFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RegExAstFactory init() {
		try {
			RegExAstFactory theRegExAstFactory = (RegExAstFactory)EPackage.Registry.INSTANCE.getEFactory("regex_ast"); 
			if (theRegExAstFactory != null) {
				return theRegExAstFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new RegExAstFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegExAstFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case RegExAstPackage.NODE: return createNode();
			case RegExAstPackage.PARAMETER: return createParameter();
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT: return createRegExAstCacheElement();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node createNode() {
		NodeImpl node = new NodeImpl();
		return node;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameter createParameter() {
		ParameterImpl parameter = new ParameterImpl();
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegExAstCacheElement createRegExAstCacheElement() {
		RegExAstCacheElementImpl regExAstCacheElement = new RegExAstCacheElementImpl();
		return regExAstCacheElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegExAstPackage getRegExAstPackage() {
		return (RegExAstPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static RegExAstPackage getPackage() {
		return RegExAstPackage.eINSTANCE;
	}

} //RegExAstFactoryImpl
