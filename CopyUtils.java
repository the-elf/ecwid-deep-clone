package com.sandbox.java.lightspeed;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CopyUtils {

    private CopyUtils() {
    }

    public static <T> T deepCopy(T object) {
        var copiedObjects = new IdentityHashMap<>();
        return deepCopy(object, copiedObjects);
    }

    private static <T> T deepCopy(T object, Map<Object, Object> copies) {
        if (object == null) {
            return null;
        }
        if (copies.containsKey(object)) {
            return (T) copies.get(object);
        }

        var clazz = object.getClass();
        try {
            if (clazz.isPrimitive() || clazz.isEnum() || isWrapperType(clazz) || String.class.isAssignableFrom(clazz)) {
                copies.put(object, object);
                return object;
            }
            if (clazz.isArray()) {
                return deepCopyArray(object, copies);
            }
            if (Collection.class.isAssignableFrom(clazz)) {
                return deepCopyCollection(object, copies);
            } else if (Map.class.isAssignableFrom(clazz)) {
                return deepCopyMap(object, copies);
            } else {
                return deepCopyObject(object, copies);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy object of class " + clazz, e);
        }
    }

    private static boolean isWrapperType(Class<?> clazz) {
        return clazz == Integer.class || clazz == Long.class
            || clazz == Float.class || clazz == Double.class
            || clazz == Short.class || clazz == Byte.class
            || clazz == Character.class || clazz == Boolean.class;
    }

    private static <T> T deepCopyArray(T object, Map<Object, Object> copies) {
        var length = Array.getLength(object);
        var copy = (T) Array.newInstance(object.getClass().getComponentType(), length);
        copies.put(object, copy);
        for (int i = 0; i < length; i++) {
            var element = Array.get(object, i);
            var elementCopy = deepCopy(element, copies);
            Array.set(copy, i, elementCopy);
        }
        return copy;
    }

    private static <T> T deepCopyCollection(
        T object,
        Map<Object, Object> copies
    ) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        var collection = (Collection<?>) object;
        var copy = (T) object.getClass().getConstructor().newInstance();
        copies.put(collection, copy);
        for (Object value : collection) {
            var valueCopy = deepCopy(value, copies);
            ((Collection<Object>) copy).add(valueCopy);
        }
        return copy;
    }

    private static <T> T deepCopyMap(
        T object,
        Map<Object, Object> copies
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var map = (Map<?, ?>) object;
        var copy = (T) object.getClass().getConstructor().newInstance();
        copies.put(map, copy);
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            var keyCopy = deepCopy(entry.getKey(), copies);
            var valueCopy = deepCopy(entry.getValue(), copies);
            ((Map<Object, Object>) copy).put(keyCopy, valueCopy);
        }
        return copy;
    }

    private static <T> T deepCopyObject(T object, Map<Object, Object> copies) throws IllegalAccessException {
        var clazz = object.getClass();
        var copy = (T) getNewInstance(clazz);
        copies.put(object, copy);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            var value = field.get(object);
            var valueCopy = deepCopy(value, copies);
            field.set(copy, valueCopy);
        }
        return copy;
    }

    private static Object getNewInstance(Class<?> clazz) {
        var constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        var parameterTypes = constructor.getParameterTypes();
        var arguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            var parameterType = parameterTypes[i];
            if (parameterType.isPrimitive()) {
                if (parameterType.isArray()) {
                    arguments[i] = Array.newInstance(parameterType.getComponentType(), 0);
                } else if (parameterType == boolean.class) {
                    arguments[i] = false;
                } else if (parameterType == byte.class ||
                    parameterType == short.class ||
                    parameterType == int.class ||
                    parameterType == long.class ||
                    parameterType == float.class ||
                    parameterType == double.class) {
                    arguments[i] = 0;
                } else if (parameterType == char.class) {
                    arguments[i] = '\0';
                }
            } else {
                arguments[i] = null;
            }
        }

        try {
            return constructor.newInstance(arguments);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get new instance of class " + clazz, e);
        }
    }
}
