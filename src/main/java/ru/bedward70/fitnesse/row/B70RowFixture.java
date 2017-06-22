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
package ru.bedward70.fitnesse.row;

import fit.Binding;
import fit.Fixture;
import fit.Parse;
import fit.RowFixture;
import fit.TypeAdapter;
import ru.bedward70.fitnesse.io.argument.B70BindArguments;
import ru.bedward70.fitnesse.row.adapter.B70RowTypeAdapter;
import ru.bedward70.fitnesse.row.cell.B70ExpectedCellConvertor;
import ru.bedward70.fitnesse.row.cell.B70ExpectedCellConvertorBinder;
import ru.bedward70.fitnesse.row.cell.B70ExpectedCellConvertorBlack;
import ru.bedward70.fitnesse.row.cell.B70ExpectedCellConvertorNull;
import ru.bedward70.fitnesse.row.data.B70MapData;

import static ru.bedward70.fitnesse.io.parse.B70ParseBinder.ASSIGNMENT_STRING;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70RowFixture extends RowFixture {

    public final B70BindArguments bindArguments = new B70BindArguments(this);

    @Override
    public void getArgsForTable(Parse table) {
        super.getArgsForTable(table);
        bindArguments.addArgs(table);
    }


    @Override
    public Object[] query() throws Exception {
        return (Object[]) bindArguments.getBindArgs().get(0);
    }

    /**
     * @return data as map(key, value)
     */
    @Override
    public Class<B70MapData> getTargetClass() {
        return B70MapData.class;
    }

    /**
     * Creates Binding instance with specific adapters
     * @param column
     * @param heads
     * @return Binding instance
     * @throws Throwable
     */
    @Override
    protected Binding createBinding(int column, Parse heads) throws Throwable {

        final Binding result = new Binding() {
            @Override
            public void doCell(Fixture fixture, Parse cell) throws Throwable {
            }
        };
        final String columnName = heads.text();
        result.adapter = new B70RowTypeAdapter(this, columnName, getCellConvertor(columnName));
        return result;
    }

    /**
     * Override to equal by blank cell
     * @param cell cell
     * @param a adapter
     */
    @Override
    public void handleBlankCell(Parse cell, TypeAdapter a) {

        try {
            Object result = a.get();
            Object expected =  a.isRegex ? cell.text(): a.parse(cell.text());
            if(a.equals(expected, result)) {
                right(cell);
            } else {
                wrong(cell, a.toString(result));
            }
        } catch (Exception var4) {
            cell.addToBody(gray("error"));
        }
    }

    /**
     * Override
     *  1. to save symbol if the suffix equals "="
     *  2. to add grey value if prefix equals "="
     * @param cell
     * @param a
     */
    @Override
    public void check(Parse cell, TypeAdapter a) {

        /**
         * Prepares value @see fit.ColumnFixture.{@link #check(fit.Parse, fit.TypeAdapter)}
         */
        try {
            this.executeIfNeeded();
        } catch (Exception var4) {
            this.exception(cell, var4);
        }

        String text = cell.text();
        if (text.endsWith(ASSIGNMENT_STRING)) {

            /**
             * Save symbol
             */
            saveSymbolByData(cell, a, text.substring(0, text.length() - ASSIGNMENT_STRING.length()));
        } else {

            /**
             * Check value @see fit.ColumnFixture.{@link #check(fit.Parse, fit.TypeAdapter)}
             */
            super.check(cell, a);

            /**
             * Add grey value if prefix equals "="
             */
            if (text.startsWith(ASSIGNMENT_STRING) ) {
                try {
                    cell.body = Fixture.gray("" + a.parse(text)) + cell.body;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void saveSymbolByData(Parse cell, TypeAdapter a, String symbolName) {
        try {
            Object valueObj = a.get();
            Fixture.setSymbol(symbolName, valueObj);
            cell.addToBody(Fixture.gray("" + valueObj));
        } catch (Exception var4) {
            this.exception(cell, var4);
        }
    }

    /**
     * Sets rules to convert a cell string value to object
     * @return ad object or null
     * @param columnName column name
     */
    public B70ExpectedCellConvertor getCellConvertor(String columnName) {
        return new B70ExpectedCellConvertorNull(
            new B70ExpectedCellConvertorBlack(
                new B70ExpectedCellConvertorBinder()
            )
        );
    }
}