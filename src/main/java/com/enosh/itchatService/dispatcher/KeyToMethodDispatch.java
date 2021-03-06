package com.enosh.itchatService.dispatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.enosh.itchatService.common.MailMessage;
import com.enosh.itchatService.common.MessageFilter;
import com.enosh.itchatService.common.MessageFilterChain;
import com.enosh.itchatService.service.NoteService;
import com.enosh.itchatService.service.ShareNoteService;
import com.enosh.itchatService.utils.Strings;

@Configuration("dispacher")
@PropertySource("classpath:key-method.properties")
@Order(value=3)
public class KeyToMethodDispatch implements MessageFilter{
	
	@Autowired private ApplicationContextProvider applicationContextProvider;
	@Autowired private Environment env;
	@Autowired ShareNoteService shareNoteService;
	@Autowired NoteService noteService;
	
	private Map<String, MethodWrapper> keyToMethodMap = new HashMap<String, MethodWrapper>();
	
	@PostConstruct
	public void scan() {
		ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateComponentProvider(false) {
			@Override
		    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		        return true;
		    }
		};
		scanningProvider.addIncludeFilter(new AnnotationTypeFilter(KeyMethodMapping.class));

		Set<BeanDefinition> beanDefinitions = scanningProvider.findCandidateComponents("com.enosh.itchatService.service");
		
		for (BeanDefinition beanDefinition : beanDefinitions) {
			String beanName = beanDefinition.getBeanClassName();
			try {
				Class clz = Class.forName(beanName);
				Method[] methods = clz.getDeclaredMethods();
				for (Method method : methods) {
					Annotation[] annotations = method.getAnnotations();
					Class [] parameterTypes = method.getParameterTypes();
					Object bean = null;
					String key = "";
					for (Annotation annotation : annotations) {
						if(annotation instanceof KeyMethodMapping) {
							bean = applicationContextProvider.getContext().getBean(clz);
							String value = ((KeyMethodMapping)annotation).value();
							key = env.getProperty(value);
							break;
						}
					}
					
					if(bean != null && !Strings.isEmpty(key)) {
						MethodWrapper methodWrapper = new MethodWrapper(bean, method, parameterTypes);
						keyToMethodMap.put(key, methodWrapper);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void dispatchRequest(String key, Object[] args) {
		if(!Strings.isEmpty(key)) {
			//if it is a common note
			if(args == null) {
				boolean succeed = noteService.createNote(key);
				if(succeed) return;
			} 
			//if it is a command
			MethodWrapper methodWrapper = keyToMethodMap.get(key);
			if(methodWrapper != null) {
				methodWrapper.invoke(args);
				return;
			}
			key = "";
		}
		shareNoteService.createShareNote();
	}

	@Override
	public void doFilter(MailMessage msg, MessageFilterChain filterChain) {
		dispatchRequest(msg.getMethodKey(), msg.getArgs());
	}
}
