package org.brijframework.util.accessor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.casting.CastingUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.support.ReflectionAccess;

public class PropertyAccessorUtil{

	public static <T> T setProperty(Object bean, String field, Object value) {
		Assertion.notNull(field, AssertMessage.arg_null_message);
		return setProperty(bean, field, ReflectionAccess.PUBLIC, value);
	}
	
	public static <T> T setProperty(Object bean, String field, ReflectionAccess access, Object value) {
		Assertion.notNull(field, AssertMessage.arg_null_message);
		AccessibleObject colling = MetaAccessorUtil.setPropertyMeta(bean.getClass(), field, ReflectionAccess.PRIVATE, value);
		Assertion.notNull(colling, AssertMessage.unbounded_key_message + " " + field + " for " + bean.getClass());
		return setProperty(bean, colling,access,value);
	}

	public static <T> T setProperty(Object bean, AccessibleObject colling,ReflectionAccess access, Object value) {
		Assertion.notNull(colling, AssertMessage.arg_null_message);
		try {
			if (colling instanceof Method) {
				return setProperty(bean, (Method) colling,access, value);
			}
			if (colling instanceof Field) {
				return setProperty(bean, (Field) colling,access, value);
			}
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T setProperty(Object bean, Method colling,ReflectionAccess access,  Object value) {
		Assertion.notNull(colling, AssertMessage.arg_null_message);
		Assertion.isTrue(!access.isAccess(colling.getModifiers()), AssertMessage.ILLEgGAL_ACCESS_MSG + " " + colling.getName());

		try {
			colling.setAccessible(true);
			colling.invoke(bean, CastingUtil.castObject(value, colling.getParameterTypes()[0]));
			return (T) value;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T setProperty(Object bean, Field colling, ReflectionAccess access, Object value) {
		Assertion.notNull(colling, AssertMessage.arg_null_message);
		Assertion.isTrue(!access.isAccess(colling.getModifiers()), AssertMessage.ILLEgGAL_ACCESS_MSG + " " + colling.getName());

		try {
			colling.setAccessible(true);
			colling.set(bean, CastingUtil.castObject(value, colling.getType()));
			return (T) value;
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getProperty(Object bean, String field) {
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		return getProperty(bean, field, ReflectionAccess.PUBLIC);
	}

	public static <T> T getProperty(Object bean, String field, ReflectionAccess level) {
		Assertion.notNull(bean, AssertMessage.model_object_null_message);
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		AccessibleObject colling = MetaAccessorUtil.findGetterMeta(bean.getClass(), field, ReflectionAccess.PRIVATE);
		Assertion.notNull(colling, AssertMessage.unbounded_key_message + " " + field+" of bean : "+bean);
		return getProperty(bean, colling, level);
	}
	
	public static <T> T getProperty(Object bean,AccessibleObject colling,ReflectionAccess level) {
		Assertion.notNull(colling, AssertMessage.field_object_null_message );
		try {
			if (colling instanceof Method) {
				return getProperty(bean, (Method)colling,level);
			}
			if (colling instanceof Field) {
				return getProperty(bean, (Field)colling,level);
			}
		} catch (IllegalArgumentException e) {
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object bean,Method colling, ReflectionAccess access) {
		Assertion.notNull(colling, AssertMessage.field_object_null_message );
		Assertion.isTrue(!access.isAccess(colling.getModifiers()), AssertMessage.ILLEgGAL_ACCESS_MSG + " " + colling.getName());
		try {
			colling.setAccessible(true);
			return (T) colling.invoke(bean);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object bean,Field colling, ReflectionAccess access) {
		Assertion.notNull(colling, AssertMessage.field_object_null_message );
		Assertion.isTrue(!access.isAccess(colling.getModifiers()), AssertMessage.ILLEgGAL_ACCESS_MSG + " " + colling.getName());
		try {
			colling.setAccessible(true);
			return (T) colling.get(bean);
		}
		catch (IllegalAccessException | IllegalArgumentException e) {
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object bean,Field colling) {
		Assertion.notNull(colling, AssertMessage.field_object_null_message );
		try {
			colling.setAccessible(true);
			return (T) colling.get(bean);
		}
		catch (IllegalAccessException | IllegalArgumentException e) {
		}
		return null;
	}

	public static Boolean isProperty(Object bean, String field, ReflectionAccess level) {
		Assertion.notNull(field, AssertMessage.field_name_null_message);
		try {
			return CastingUtil.boolValue(getProperty(bean, field, level));
		} catch (IllegalArgumentException e) {
		}
		return false;
	}

	public static Field getType(Object bean, String field, ReflectionAccess level) {
		return FieldUtil.getField(bean.getClass(), field, level);
	}

	public static Map<String, Object> getProperties(Object bean) {
		return getProperties(bean, ReflectionAccess.PUBLIC);
	}

	public static Map<String, Object> getProperties(Object bean, ReflectionAccess level) {
		Map<String, Object> properties = new LinkedHashMap<>();
		for (String _key : MetaAccessorUtil.getNamePropertiesMeta(bean.getClass(), level)) {
			properties.put(_key, getProperty(bean, _key, level));
		}
		return properties;
	}

	public static Map<String, Object> setProperties(Object bean, Map<String, Object> _map) {
		return setProperties(bean, _map, ReflectionAccess.PUBLIC);
	}

	public static Map<String, Object> setProperties(Object bean, Map<String, Object> _map, ReflectionAccess level) {
		Map<String, Object> properties = new LinkedHashMap<>();
		for (String _key : _map.keySet()) {
			properties.put(_key,setProperty(bean, _key, level, _map.get(_key)));
		}
		return properties;
	}

	public static void setSafeProperty(Object bean, String field, Object value) {
		AccessibleObject colling = MetaAccessorUtil.setPropertyMeta(bean.getClass(), field, ReflectionAccess.PRIVATE, value);
		if(colling!=null) {
			setSafeProperty(bean, colling, value);
		}
	}

	public static void setSafeProperty(Object bean, AccessibleObject colling, Object value) {
		if(colling!=null) {
			setProperty(bean, colling, ReflectionAccess.PRIVATE, value);
		}
	}

}
