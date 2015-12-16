package com.codegeneration;

import java.io.File;
import java.io.IOException;
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

	    System.out.println("Enter a packageName");
	    String packageName = in.nextLine();
	    
	    System.out.println("Enter a functionName");
	    String functionName = in.nextLine();
	    
		try {
			main.generateSource(packageName, functionName);
        } catch (JClassAlreadyExistsException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	public static void generateSource(String packageName, String functionName) throws JClassAlreadyExistsException, IOException {
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
		JMethod interfaceFacadeExample = interfaceFacade.method(JMod.PUBLIC, void.class, "example");
		JMethod classFacadeExample = classFacade.method(JMod.PUBLIC, void.class, "example");
		//classFacadeExample.annotate(codeModel.ref("java.lang.Override"));
		classFacadeExample.annotate(codeModel.ref("org.springframework.transaction.annotation.Transactional"));
		
		JMethod interfaceServiceExample = interfaceService.method(JMod.PUBLIC, void.class, "example");
		JMethod classServiceExample = classService.method(JMod.PUBLIC, void.class, "example");
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
		codeModel.build(new File("src/main/java"));
	}
}
