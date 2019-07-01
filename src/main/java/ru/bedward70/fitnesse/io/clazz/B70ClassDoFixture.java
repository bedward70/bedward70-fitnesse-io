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
package ru.bedward70.fitnesse.io.clazz;

import ru.bedward70.fitnesse.io.B70DoFixture;
import ru.bedward70.fitnesse.io.parse.B70ParseBinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70ClassDoFixture extends B70DoFixture {


    private Object instance;

    private Class classDefinition;

    private List<Object> params = new ArrayList<Object>();

    public B70ClassDoFixture() {
    }

    public Class setClass(final String className) throws ClassNotFoundException, NoSuchMethodException {

        instance = null;
        classDefinition = Class.forName(className);
        return classDefinition;
    }

    public Object set(final String name) throws ClassNotFoundException, NoSuchMethodException {

        instance = B70ParseBinder.create(this, name).getValue();
        classDefinition = instance.getClass();
        return instance;
    }

    public boolean addParam(final String object) {

        return params.add(B70ParseBinder.create(this, object).getValue());
    }

    public void cleanParams() {

        params.clear();
    }

    public Object createInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        final List<Class> classes = new ArrayList<Class>();
        for (Object param : params) {
            classes.add(param.getClass());
        }
        final Constructor constructor = classDefinition.getConstructor(classes.toArray(new Class[] {}));
        return constructor.newInstance(params.toArray());
    }

    public Object method(String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        final List<Class> classes = new ArrayList<Class>();
        for (Object param : params) {
            classes.add(param.getClass());
        }
        final Method m = classDefinition.getDeclaredMethod(methodName, classes.toArray(new Class[] {}));
        return m.invoke(instance, params.toArray());
    }
}
