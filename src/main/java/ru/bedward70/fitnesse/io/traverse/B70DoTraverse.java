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
package ru.bedward70.fitnesse.io.traverse;

import fit.Fixture;
import fitlibrary.closure.CalledMethodTarget;
import fitlibrary.exception.table.MissingCellsException;
import fitlibrary.table.Cell;
import fitlibrary.table.Row;
import fitlibrary.traverse.workflow.DoTraverse;
import fitlibrary.utility.TestResults;

/**
 * Created by bedward70 on 11.06.17.
 * The extender of DoTraverse to saveSymbol as symbol
 */
public class B70DoTraverse extends DoTraverse {

    /**
     * Constructor
     * @param sut sut
     */
    public B70DoTraverse(Object sut) {
        super(sut);
    }

    /**
     * Save as symbol
     * @param row row
     * @param testResults testResults
     * @throws Exception Exception
     */
    public void saveSymbol(Row row, TestResults testResults) throws Exception {
        save(
            row,
            testResults,
            new B70Saver() {
                @Override
                public void save(Cell expectedCell, Object object) {
                    String name = expectedCell.text();
                    Fixture.setSymbol(name, object);
                    expectedCell.parse.addToBody(Fixture.gray(" = " + Fixture.getSymbol(name)));
                }
            }
        );
    }

    /**
     * Saves by B70Saver
     * @param row row
     * @param testResults testResults
     * @param saver saver
     * @throws Exception Exception
     */
    public void save(Row row, TestResults testResults, B70Saver saver) throws Exception {
        int less = 3;
        if(row.size() < less) {
            throw new MissingCellsException("B70DoTraverseSave");
        } else {
            CalledMethodTarget target = this.findMethodFromRow(row, less);
            Object result = target.invoke(row.rowFrom(2), testResults, true);
            Cell expectedCell = row.last();
            saver.save(expectedCell, result);
        }
    }
}