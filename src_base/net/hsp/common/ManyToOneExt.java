package net.hsp.common;

@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(value = { java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD,java.lang.annotation.ElementType.TYPE,java.lang.annotation.ElementType.METHOD })
public abstract @interface ManyToOneExt {
	public abstract Class value() ;
}