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
package ru.bedward70.fitnesse.io;

import ru.bedward70.fitnesse.io.parse.B70ParseBinder;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70PropertyFixture extends B70DoFixture {

    /**
     * Read property
     * @param source  source
     * @param propertyName property name
     * @return an object
     */
    public String readProperty(String source, String propertyName) throws IOException {
        final Properties prop = new Properties();
        final String str = B70ParseBinder.create(this, source).getValue().toString();
        try (StringReader sr = new StringReader(str)) {
            prop.load(sr);
        }
        return prop.getProperty(B70ParseBinder.create(this, propertyName).getValue().toString());
    }
}