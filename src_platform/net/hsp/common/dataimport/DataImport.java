package net.hsp.common.dataimport;

@java.lang.annotation.Target(value = { java.lang.annotation.ElementType.TYPE })
@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public abstract @interface DataImport {
	// 名称
	String name();

	Class<?> entity();

	String template() default "";
}
