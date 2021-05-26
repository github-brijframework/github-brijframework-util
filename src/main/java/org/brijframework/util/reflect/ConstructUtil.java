package org.brijframework.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.support.ReflectionAccess;

public abstract class ConstructUtil {

	public static List<Constructor<?>> getConstructors(Class<?> _class) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		List<Constructor<?>> constructors = new ArrayList<>();
		for (Constructor<?> constructor : _class.getDeclaredConstructors()) {
			constructors.add(constructor);
		}
		constructors.sort((constructor1,constructor2)->Integer.valueOf(constructor1.getParameterTypes().length).compareTo(Integer.valueOf(constructor2.getParameterTypes().length)));
		return constructors;
	}

	public static Constructor<?> getConstructor(Class<?> _class, ReflectionAccess access, Type... types) {
		Assertion.notNull(_class, AssertMessage.class_object_null_message);
		for (Constructor<?> constructor : _class.getDeclaredConstructors()) {
			if(!access.isAccess(constructor.getModifiers())) {
				continue;
			}
			if (ParamUtil.isEqualTypes(types,constructor.getParameterTypes())) {
				return constructor;
			}
		}
		return null;
	}
}
