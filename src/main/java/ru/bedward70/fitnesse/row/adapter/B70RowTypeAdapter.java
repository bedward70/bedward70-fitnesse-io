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
package ru.bedward70.fitnesse.row.adapter;

import fit.Fixture;
import fit.TypeAdapter;
import ru.bedward70.fitnesse.io.parse.B70ParseBinder;
import ru.bedward70.fitnesse.row.cell.B70ExpectedCellConvertor;
import ru.bedward70.fitnesse.row.data.B70MapData;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70RowTypeAdapter extends TypeAdapter implements Cloneable {

    private final Fixture fixture;
    private final String columnName;
    private final B70ExpectedCellConvertor cellConvertor;

    public B70RowTypeAdapter(Fixture fixture, String columnName, B70ExpectedCellConvertor cellConvertor) {
        this.fixture = fixture;
        this.columnName = columnName;
        this.cellConvertor = cellConvertor;
    }

    @Override
    public Object parse(String string) throws Exception {
        return B70ParseBinder.create(fixture, string).getValue();
    }

    @Override
    public Object get() throws IllegalAccessException, InvocationTargetException {
        return ((B70MapData) this.target).get(columnName);
    }
}