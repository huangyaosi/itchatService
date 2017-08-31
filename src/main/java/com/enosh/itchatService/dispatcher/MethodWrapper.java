package com.enosh.itchatService.dispatcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodWrapper {
	
	private Object bean;
	private Method method;
	private Class [] parameterTypes;
	
	public MethodWrapper(Object bean, Method method, Class [] parameterTypes){
		this.bean = bean;
		this.method = method;
		this.parameterTypes = parameterTypes;
	}
	
	public void invoke(Object... args) {
		if(isValidateParameters(args)) {
			try {
				method.invoke(bean, args);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean isValidateParameters(Object[] args) {
		if(args == null && (parameterTypes == null || parameterTypes.length == 0)) return true;
		
		if(args.length != parameterTypes.length) return false;
		
		boolean valid = true;
		
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			if(!parameterTypes[i].isAssignableFrom(arg.getClass())){
				valid = false;
				break;
			}
		}
		
		return valid;
	}
}
