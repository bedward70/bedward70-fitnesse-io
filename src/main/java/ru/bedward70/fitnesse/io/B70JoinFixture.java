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

import fit.Fixture;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Created by ebalovnev on 07.06.17.
 * http://fitnesse.org/FitNesse.FullReferenceGuide.UserGuide.FixtureGallery.ImportantConcepts.FixtureSymbols
 */
public class B70JoinFixture extends B70DoFixture {

    public static final int REQUERED_PARAMETER_COUNT = 0;
    public static final String SYMBOL_PREFIX = "=";

    private final List<String> parts = new ArrayList<>();


    /**
     * Add part
     * @param part part
     * @return
     */
    public void add(String part) {
        parts.add(part);
    }

    /**
     * Join
     * @return
     */
    public String join() {
        StringBuffer result = new StringBuffer();

        // From headers
        for (int i = 0; i < args.length - REQUERED_PARAMETER_COUNT; i++) {
            result.append(parse(args[i + REQUERED_PARAMETER_COUNT]));
        }

        // Added
        for (String part : parts) {
            result.append(parse(part));
        }
        return result.toString();
    }

    /**
     * Checks symbol prefix if then prefix and read symbol
     * @param part part
     * @return
     */
    private Object parse(String part) {
        if (!isNull(part) && part.startsWith(SYMBOL_PREFIX) && Fixture.hasSymbol(part.substring(1).trim())) {
            return getSymbol(part.substring(1).trim());
        } else {
            return part;
        }
    }

}