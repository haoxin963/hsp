package net.hsp.web.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

public class BeanNameGenerator extends AnnotationBeanNameGenerator {

	@Override
	protected String buildDefaultBeanName(BeanDefinition definition) {
		// 如有BeanClassName注解，则返回package+BeanName
		try {
			if (Class.forName(definition.getBeanClassName()).isAnnotationPresent((BeanClassName.class))) {
				System.out.println("getBeanClassName=>"+definition.getBeanClassName());
				return definition.getBeanClassName();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.buildDefaultBeanName(definition);
	}
}
