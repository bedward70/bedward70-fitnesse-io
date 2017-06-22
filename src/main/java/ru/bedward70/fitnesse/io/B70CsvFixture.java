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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.bedward70.fitnesse.io.parse.B70ParseBinder;
import ru.bedward70.fitnesse.row.data.B70MapData;
import ru.bedward70.fitnesse.row.data.B70MapDataImpl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70CsvFixture extends B70DoFixture {

    private String data;
    private Character delimiter;

    public void setData(String name) {
        this.data = B70ParseBinder.create(this, name).getValue().toString();
    }

    public void setDelimiter(String name) throws Exception {
        final String delimiterString = B70ParseBinder.create(this, name).getValue().toString();
        if (delimiterString.length() != 1) {
            throw new Exception("Expected an one char, but delimiter length = " + delimiterString.length());
        }
        this.delimiter = delimiterString.charAt(0);
    }

    public Object[] query() throws Exception {

        final List<B70MapData> result = new ArrayList<>();
        final CSVFormat csvFileFormat = CSVFormat.DEFAULT
                .withDelimiter(delimiter)
                .withIgnoreSurroundingSpaces();

        boolean header = true;
        final Map<Integer,String> headerMap = new HashMap<>();
        final CSVParser csvFileParser = new CSVParser(new StringReader(data), csvFileFormat);
        for (CSVRecord record : csvFileParser) {
            final B70MapDataImpl rowData =  new B70MapDataImpl();
            if (!header) {
                result.add(rowData);
            }

            for (int i = 0; i < record.size(); i++) {
                String value = record.get(i);
                if (isNotBlank(value)) {
                    if (header) {
                        headerMap.put(i, value);
                    } else {
                        if (headerMap.containsKey(i)) {
                            rowData.put(headerMap.get(i), value);
                        }
                    }
                }
            }
            header = false;
        }
        return result.toArray();
    }
}