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

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import ru.bedward70.fitnesse.row.data.B70MapData;
import ru.bedward70.fitnesse.row.data.B70MapDataImpl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70SectionPropertyOrderedRowFixture extends B70OrderedRowFixture {

    @Override
    public Object[] query() throws Exception {

        final List<B70MapData> result = new ArrayList<>();
        final String data = bindArguments.getBindArgs().get(0).toString();
        final INIConfiguration iniConfiguration = getProperties(data);
        final String section = bindArguments.getBindArgs().get(1).toString();
        final SubnodeConfiguration subnodeConfiguration = iniConfiguration.getSection(section);

        // One line
        final B70MapDataImpl rowData =  new B70MapDataImpl();
        result.add(rowData);

        Iterator<String> iterator = subnodeConfiguration.getKeys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = subnodeConfiguration.getString(key);
            rowData.put(key, value);
        }
        return result.toArray();
    }

    private INIConfiguration getProperties(String data) throws ConfigurationException, IOException {
        INIConfiguration iniConfiguration = new INIConfiguration();
        try (StringReader sr = new StringReader(data)) {
            iniConfiguration.read(sr);
        }
        return iniConfiguration;
    }
}