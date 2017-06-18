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
package ru.bedward70.fitnesse.io.argument;

import fit.Fixture;
import fit.Parse;
import ru.bedward70.fitnesse.io.parse.B70ParseBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70BindArguments {

    private final List<Object> bindArgs = new ArrayList<>();

    public final Fixture fixture;

    public B70BindArguments(Fixture fixture) {
        this.fixture = fixture;
    }

    public void addArgs(Parse table) throws RuntimeException {

        for(Parse parameters = table.parts.parts.more; parameters != null; parameters = parameters.more) {
            String name = Parse.unescape(parameters.body);
            Object value = addArg(name);
            if (!name.equals(value)) {
                parameters.addToBody(Fixture.gray(" = " + value));
            }
        }
    }

    public Object addArg(String name) throws RuntimeException {

        final Object result = B70ParseBinder.create(fixture, name).getValue();
        bindArgs.add(result);
        return result;
    }

    public List<Object> getBindArgs() {
        return new ArrayList<>(bindArgs);
    }
}