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
package ru.bedward70.fitnesse.row.cell;

import fit.Fixture;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Objects.isNull;

/**
 * Created by Eduard Balovnev on 11.06.17.
 * Convert to Blank value
 */
public class B70ExpectedCellConvertorBlack implements B70ExpectedCellConvertor {

    private static final Collection<String> EXPECTED_VALUES = new ArrayList<String>() {
        {
            add("blank");
        }
    };

    private final B70ExpectedCellConvertor next;

    public B70ExpectedCellConvertorBlack(B70ExpectedCellConvertor next) {
        this.next = next;
    }

    @Override
    public Object convert(Fixture fixture, String inputCellValue) {

        Object result;
        if (
            isNull(inputCellValue)
            || EXPECTED_VALUES.contains(inputCellValue)
        ) {
            result = "";
        } else if (!isNull(next)) {
            result = next.convert(fixture, inputCellValue);
        } else {
            result = inputCellValue;
        }
        return result;
    }
}