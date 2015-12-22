package com.codegeneration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

public class main {

	public static void main(String args[]) {
		
	    Scanner in = new Scanner(System.in);

	    System.out.println("Enter a path");
	    String path = in.nextLine();
	    
	    System.out.println("Enter a packageName");
	    String packageName = in.nextLine();
	    
	    System.out.println("Enter a functionName");
	    String functionName = in.nextLine();
	    
	    
		System.out.println("Do u need basic CRUD? y or n");
		String isNeedCRUD = in.nextLine();
	    
		try {
			main.generateSource(path, packageName, functionName, isNeedCRUD);
        } catch (JClassAlreadyExistsException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	public static void generateSource(String path, String packageName, String functionName, String isNeedCRUD) throws JClassAlreadyExistsException, IOException {
		// Instantiate an instance of the JCodeModel class
		JCodeModel codeModel = new JCodeModel();
		
		//將第一個字母變為大寫
		String newFunctionName = Character.toUpperCase(functionName.charAt(0)) + functionName.substring(1);
		
		// JDefinedClass will let you create a class in a specified package.
		JDefinedClass interfaceFacade = codeModel._class(JMod.PUBLIC, "com.lttc.cbt."+packageName+".facade.I"+newFunctionName+"Facade", ClassType.INTERFACE);
		JDefinedClass interfaceService = codeModel._class(JMod.PUBLIC, "com.lttc.cbt."+packageName+".service.I"+newFunctionName+"Service", ClassType.INTERFACE);
		JDefinedClass classFacade = codeModel._class(JMod.PUBLIC, "com.lttc.cbt."+packageName+".facade.impl."+newFunctionName+"Facade", ClassType.CLASS);
		JDefinedClass classService = codeModel._class(JMod.PUBLIC, "com.lttc.cbt."+packageName+".service.impl."+newFunctionName+"Service", ClassType.CLASS);

		//implements interface
		classFacade._implements(interfaceFacade);
		classService._implements(interfaceService);
		
		//add annotation
		classFacade.annotate(codeModel.ref("org.springframework.stereotype.Component")).param("value", "value = "+functionName+"Facade");
		classService.annotate(codeModel.ref("org.springframework.stereotype.Component")).param("value", "value = "+functionName+"Service");
		
		// Creating private fields in the class
		JFieldVar facadefield = classFacade.field(0, interfaceService, functionName+"Service");
		facadefield.annotate(codeModel.ref("org.springframework.beans.factory.annotation.Autowired"));
		facadefield.annotate(codeModel.ref("org.springframework.beans.factory.annotation.Qualifier")).param("value", "value = "+functionName+"Service");
		
		// The codeModel instance will have a list of Java primitives which can
		// be
		// used to create a primitive field in the new class
//		JFieldVar field2 = classToBeCreated.field(JMod.PRIVATE, codeModel.DOUBLE, "bar");

		// Create getter and setter methods for the fields
		if("y".equals(isNeedCRUD)){
			JDefinedClass filter = codeModel._class(JMod.PUBLIC, "com.lttc.cbt."+packageName+".filter."+newFunctionName+"Filter", ClassType.CLASS);

			//facade interface mehthod
			JMethod interfaceFacadeList = interfaceFacade.method(JMod.PUBLIC, void.class, "getList");
			interfaceFacadeList.param(filter, functionName+"Filter");
			JMethod interfaceFacadeSingle = interfaceFacade.method(JMod.PUBLIC, void.class, "getSingle");
			interfaceFacadeSingle.param(String.class, "id");
			JMethod interfaceFacadeInsert = interfaceFacade.method(JMod.PUBLIC, void.class, "insert");
			interfaceFacadeInsert.param(Object.class, "data");
			JMethod interfaceFacadeUpdate = interfaceFacade.method(JMod.PUBLIC, void.class, "update");
			interfaceFacadeUpdate.param(Object.class, "data");
			JMethod interfaceFacadeDelete = interfaceFacade.method(JMod.PUBLIC, void.class, "delete");
			interfaceFacadeDelete.param(String.class, "id");

			//facade impl method
			JMethod classFacadeList = classFacade.method(JMod.PUBLIC, void.class, "getList");
			classFacadeList.param(filter, functionName+"Filter");
			classFacadeList.annotate(codeModel.ref("org.springframework.transaction.annotation.Transactional"));
			classFacadeList.annotate(codeModel.ref("java.lang.Override"));
			
			JMethod classFacadeSingle = classFacade.method(JMod.PUBLIC, void.class, "getSingle");
			classFacadeSingle.param(String.class, "id");
			classFacadeSingle.annotate(codeModel.ref("org.springframework.transaction.annotation.Transactional"));
			classFacadeSingle.annotate(codeModel.ref("java.lang.Override"));
			
			JMethod classFacadeInsert = classFacade.method(JMod.PUBLIC, void.class, "insert");
			classFacadeInsert.param(Object.class, "data");
			classFacadeInsert.annotate(codeModel.ref("org.springframework.transaction.annotation.Transactional"));
			classFacadeInsert.annotate(codeModel.ref("java.lang.Override"));
			
			JMethod classFacadeUpdate = classFacade.method(JMod.PUBLIC, void.class, "update");
			classFacadeUpdate.param(Object.class, "data");
			classFacadeUpdate.annotate(codeModel.ref("org.springframework.transaction.annotation.Transactional"));
			classFacadeUpdate.annotate(codeModel.ref("java.lang.Override"));
			
			JMethod classFacadeDelete = classFacade.method(JMod.PUBLIC, void.class, "delete");
			classFacadeDelete.param(String.class, "id");
			classFacadeDelete.annotate(codeModel.ref("org.springframework.transaction.annotation.Transactional"));
			classFacadeDelete.annotate(codeModel.ref("java.lang.Override"));
			
			//service method
			JMethod interfaceServiceList = interfaceService.method(JMod.PUBLIC, void.class, "getList");
			interfaceServiceList.param(filter, functionName+"Filter");
			JMethod interfaceServiceSingle = interfaceService.method(JMod.PUBLIC, void.class, "getSingle");
			interfaceServiceSingle.param(String.class, "id");
			JMethod interfaceServiceInsert = interfaceService.method(JMod.PUBLIC, void.class, "insert");
			interfaceServiceInsert.param(Object.class, "data");
			JMethod interfaceServiceUpdate = interfaceService.method(JMod.PUBLIC, void.class, "update");
			interfaceServiceUpdate.param(Object.class, "data");
			JMethod interfaceServiceDelete = interfaceService.method(JMod.PUBLIC, void.class, "delete");
			interfaceServiceDelete.param(String.class, "id");

			
			//service impl method
			JMethod classServiceList = classService.method(JMod.PUBLIC, void.class, "getList");
			classServiceList.param(filter, functionName+"Filter");
			classServiceList.annotate(codeModel.ref("java.lang.Override"));
			
			JMethod classServiceSingle = classService.method(JMod.PUBLIC, void.class, "getSingle");
			classServiceSingle.param(String.class, "id");
			classServiceSingle.annotate(codeModel.ref("java.lang.Override"));
			
			JMethod classServiceInsert = classService.method(JMod.PUBLIC, void.class, "insert");
			classServiceInsert.param(Object.class, "data");
			classServiceInsert.annotate(codeModel.ref("java.lang.Override"));
			
			JMethod classServiceUpdate = classService.method(JMod.PUBLIC, void.class, "update");
			classServiceUpdate.param(Object.class, "data");
			classServiceUpdate.annotate(codeModel.ref("java.lang.Override"));
			
			JMethod classServiceDelete = classService.method(JMod.PUBLIC, void.class, "delete");
			classServiceDelete.param(String.class, "id");
			classServiceDelete.annotate(codeModel.ref("java.lang.Override"));
		}		
		
		//classServiceExample.annotate(codeModel.ref("java.lang.Override"));

		// code to create a return statement with the field1
//		field1GetterMethod.body()._return(field1);
//		JMethod field1SetterMethod = classToBeCreated.method(JMod.PUBLIC, codeModel.VOID, "setFoo");
		// code to create an input parameter to the setter method which will
		// take a variable of type field1
//		field1SetterMethod.param(field1.type(), "inputFoo");
		// code to create an assignment statement to assign the input argument
		// to the field1
//		field1SetterMethod.body().assign(JExpr._this().ref("foo"), JExpr.ref("inputFoo"));

//		JMethod field2GetterMethod = classToBeCreated.method(JMod.PUBLIC, field2.type(), "getBar");
//		field2GetterMethod.body()._return(field2);
//		JMethod field2SetterMethod = classToBeCreated.method(JMod.PUBLIC, codeModel.VOID, "setBar");
//		field2SetterMethod.param(field2.type(), "inputBar");
//		field2SetterMethod.body().assign(JExpr._this().ref("bar"), JExpr.ref("inputBar"));

		// creating an enum class within our main class
//		JDefinedClass enumClass = classToBeCreated._enum(JMod.PUBLIC, "REPORT_COLUMNS");
		// This code creates field within the enum class
//		JFieldVar columnField = enumClass.field(JMod.PRIVATE | JMod.FINAL, String.class, "column");
//		JFieldVar filterableField = enumClass.field(JMod.PRIVATE | JMod.FINAL, codeModel.BOOLEAN, "filterable");

		// Define the enum constructor
//		JMethod enumConstructor = enumClass.constructor(JMod.PRIVATE);
//		enumConstructor.param(String.class, "column");
//		enumConstructor.param(codeModel.BOOLEAN, "filterable");
//		enumConstructor.body().assign(JExpr._this().ref("column"), JExpr.ref("column"));
//		enumConstructor.body().assign(JExpr._this().ref("filterable"), JExpr.ref("filterable"));

//		JMethod getterColumnMethod = enumClass.method(JMod.PUBLIC, String.class, "getColumn");
//		getterColumnMethod.body()._return(columnField);
//		JMethod getterFilterMethod = enumClass.method(JMod.PUBLIC, codeModel.BOOLEAN, "isFilterable");
//		getterFilterMethod.body()._return(filterableField);

//		JEnumConstant enumConst = enumClass.enumConstant("FOO_BAR");
//		enumConst.arg(JExpr.lit("fooBar"));
//		enumConst.arg(JExpr.lit(true));

		// This will generate the code in the specified file path.
		codeModel.build(new File(path+"src/main/java"));
	}
}
