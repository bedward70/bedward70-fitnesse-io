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
package ru.bedward70.fitnesse.io.saver;

import fit.Fixture;
import fitlibrary.table.Cell;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static java.util.Objects.isNull;

/**
 * Created by bedward70 on 11.06.17.
 *
 */
public class B70TextFileSaver implements B70Saver {

    private final String filename;
    private final String encoding;

    public B70TextFileSaver(String filename, String encoding) {
        this.filename = filename;
        this.encoding = encoding;
    }

    @Override
    public void save(Cell expectedCell, Object object) throws IOException {
        if (isNull(object)) {
            throw new NullPointerException("Can't save null object to \"" + filename + "\" file");
        }
        FileUtils.write(new File(filename), object.toString(), encoding);
        expectedCell.parse.addToBody(Fixture.gray(" = " + new File(filename).getAbsolutePath()));
    }
}