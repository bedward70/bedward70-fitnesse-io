/*
 MIT License https://en.wikipedia.org/wiki/MIT_License

 Copyright (c) 2017, Eduard Balovnev (bedward70)
 All rights reserved.

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package ru.bedward70.fitnesse.io.parse;

import fit.Fixture;
import fit.TypeAdapter;
import fit.exception.FitFailureException;
import fit.exception.NoSuchFieldFitFailureException;
import fit.exception.NoSuchMethodFitFailureException;
import util.GracefulNamer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 * @see fit.Binding
 */
public abstract class B70ParseBinder {
    private static final Pattern regexMethodPattern = Pattern.compile("(.+)(?:\\?\\?|!!)");
    private static final Pattern methodPattern = Pattern.compile("(.+)(?:\\(\\)|\\?|!)");
    private static final Pattern fieldPattern = Pattern.compile("=?([^=]+)=?");


    public abstract Object getValue() throws RuntimeException;

    public static B70ParseBinder create(Fixture fixture, String name) throws RuntimeException {
        B70ParseBinder result;

        if (name.startsWith("=")) {
            result = new B70ParseBinder.RecallBinding(name.substring(1));
        } else if (
            regexMethodPattern.matcher(name).matches()
            || methodPattern.matcher(name).matches()
        ) {
            final TypeAdapter adapter = makeAdapter(fixture, name);
            result = new B70ParseBinder.QueryBinding(adapter);
        } else  {
            result = new B70ParseBinder.DefaultBuilding(name);
        }
        return result;
    }


    public static class RecallBinding extends B70ParseBinder {

        private final String symbolName;

        public RecallBinding(String symbolName) {
            super();
            this.symbolName = symbolName;
        }

        public Object getValue() throws RuntimeException {
            if(!Fixture.hasSymbol(symbolName)) {
                throw new FitFailureException("No such symbol: " + symbolName);
            } else {
                Object value = Fixture.getSymbol(symbolName);
                return value;
            }
        }
    }

    public static class QueryBinding extends B70ParseBinder {

        private TypeAdapter adapter;
        public QueryBinding(TypeAdapter adapter) {
            super();
            this.adapter = adapter;
        }

        @Override
        public Object getValue() throws RuntimeException {
            try {
                return adapter.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static  class DefaultBuilding extends B70ParseBinder {
        private final String name;

        public DefaultBuilding(String name) {
            super();
            this.name = name;
        }

        @Override
        public Object getValue() throws RuntimeException {
            return name;
        }
    }

    private static TypeAdapter makeAdapter(Fixture fixture, String name) throws RuntimeException {
        Matcher regexMatcher = regexMethodPattern.matcher(name);
        if(regexMatcher.find()) {
            return makeAdapterForRegexMethod(name, fixture, regexMatcher);
        } else {
            Matcher methodMatcher = methodPattern.matcher(name);
            return methodMatcher.find()?makeAdapterForMethod(name, fixture, methodMatcher):makeAdapterForField(name, fixture);
        }
    }


    private static TypeAdapter makeAdapterForMethod(String name, Fixture fixture, Matcher matcher) {
        return makeAdapterForMethod(name, fixture, matcher, false);
    }

    private static TypeAdapter makeAdapterForRegexMethod(String name, Fixture fixture, Matcher matcher) {
        return makeAdapterForMethod(name, fixture, matcher, true);
    }

    private static TypeAdapter makeAdapterForMethod(String name, Fixture fixture, Matcher matcher, boolean isRegex) {
        Method method = getMethod(name, fixture, matcher);
        if(method == null) {
            throw new NoSuchMethodFitFailureException(name);
        } else {
            return TypeAdapter.on(fixture, method, isRegex);
        }
    }

    private static TypeAdapter makeAdapterForField(String name, Fixture fixture) {
        Field field = null;
        if(GracefulNamer.isGracefulName(name)) {
            String simpleName = GracefulNamer.disgrace(name).toLowerCase();
            field = findField(fixture, simpleName);
        } else {
            Matcher matcher = fieldPattern.matcher(name);
            matcher.find();
            String fieldName = matcher.group(1);
            Class clazz = getTargetClass(fixture);

            try {
                field = clazz.getField(fieldName);
            } catch (NoSuchFieldException var9) {
                try {
                    field = getField(clazz, fieldName);
                } catch (NoSuchFieldException var8) {
                    ;
                }
            }
        }

        if(field == null) {
            throw new NoSuchFieldFitFailureException(name);
        } else {
            return TypeAdapter.on(fixture, field);
        }
    }

    private static Class<?> getTargetClass(Fixture fixture) {
        return fixture.getClass();
    }

    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException var4) {
            Class<?> superClass = clazz.getSuperclass();
            if(superClass == null) {
                throw var4;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

    private static Method getMethod(String name, Fixture fixture, Matcher matcher) {
        Method method = null;
        String methodName;
        if(GracefulNamer.isGracefulName(name)) {
            methodName = GracefulNamer.disgrace(name).toLowerCase();
            method = findMethod(fixture, methodName);
        } else {
            try {
                methodName = matcher.group(1);
                Class<?> targetClass = getTargetClass(fixture);
                method = targetClass.getMethod(methodName, new Class[0]);
            } catch (NoSuchMethodException var6) {
                ;
            }
        }

        return method;
    }

    private static Field findField(Fixture fixture, String simpleName) {
        List<Field> fields = getAllDeclaredFields(getTargetClass(fixture));
        Field field = null;
        Iterator var4 = fields.iterator();

        while(var4.hasNext()) {
            Field possibleField = (Field)var4.next();
            if(simpleName.equalsIgnoreCase(possibleField.getName())) {
                field = possibleField;
                break;
            }
        }

        return field;
    }

    private static List<Field> getAllDeclaredFields(Class<?> clazz) {
        if(clazz.getSuperclass() != null) {
            List<Field> fields = getAllDeclaredFields(clazz.getSuperclass());
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            return fields;
        } else {
            return new ArrayList(Arrays.asList(clazz.getDeclaredFields()));
        }
    }


    private static Method findMethod(Fixture fixture, String simpleName) {
        Method[] methods = getTargetClass(fixture).getMethods();
        Method method = null;
        Method[] var4 = methods;
        int var5 = methods.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Method possibleMethod = var4[var6];
            if(simpleName.equalsIgnoreCase(possibleMethod.getName())) {
                method = possibleMethod;
                break;
            }
        }

        return method;
    }
}