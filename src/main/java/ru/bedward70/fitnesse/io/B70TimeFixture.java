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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bedward70 on 06.06.17.
 */
public class B70TimeFixture extends B70DoFixture {

    public void waitSeconds(String seconds) throws Exception {

        Thread.sleep(1000L * getValue(seconds));
    }

    public void waitMilliseconds(String milliseconds) throws Exception {

        Thread.sleep(getValue(milliseconds));
    }

    private Long getValue(String seconds) {
        final Object object = B70ParseBinder.create(this, seconds).getValue();
        return object instanceof Number ? ((Number) object).longValue() : Long.valueOf(object.toString());
    }

    public Date currentDate() {
        return new Date();
    }

    public String toFormat(String dateStr, String formatStr) {
        final Date date = (Date) B70ParseBinder.create(this, dateStr).getValue();
        final String format = B70ParseBinder.create(this, formatStr).getValue().toString();
        return new SimpleDateFormat(format).format(date);
    }

}