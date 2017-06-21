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
package ru.bedward70.fitnesse.db;

import ru.bedward70.fitnesse.row.B70RowFixture;
import ru.bedward70.fitnesse.row.data.B70MapData;
import ru.bedward70.fitnesse.row.data.B70MapDataImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70DBRowFixture extends B70RowFixture {

    public static final int REQUERED_PARAMETER_COUNT = 2;

    @Override
    public Object[] query() throws Exception {

        final List<B70MapData> result = new ArrayList<>();
        final DataSource ds = (DataSource) bindArguments.getBindArgs().get(0);
        final String sql = bindArguments.getBindArgs().get(1).toString();
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                for (int i = 0; i < args.length - REQUERED_PARAMETER_COUNT; i++) {
                    st.setObject(i + 1, bindArguments.getBindArgs().get(i + REQUERED_PARAMETER_COUNT));
                }
                try (ResultSet rs = st.executeQuery()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    while (rs.next()) {
                        final B70MapDataImpl rowData =  new B70MapDataImpl();
                        for (int i = 0; i < meta.getColumnCount(); i++) {
                            rowData.put(meta.getColumnName(i + 1).toLowerCase(), rs.getString(i + 1));
                        }
                        result.add(rowData);
                    }
                }
            }
        }
        return result.toArray();
    }
}