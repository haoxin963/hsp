package net.hsp.common.schedule;

@java.lang.annotation.Target(value = { java.lang.annotation.ElementType.TYPE })
@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public abstract @interface Task {
	// 任务名
	String name();

	// 所属站点
	String station() default "";
}
