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

import ru.bedward70.fitnesse.io.B70DoFixture;
import ru.bedward70.fitnesse.io.parse.B70ParseBinder;
import ru.bedward70.fitnesse.row.data.B70MapData;
import ru.bedward70.fitnesse.row.data.B70MapDataImpl;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70DBFixture extends B70DoFixture {

    private DataSource ds;
    private String sql;
    private final List<Object> params = new ArrayList<>();

    public void setDataSource(String name) {
        this.ds = (DataSource) B70ParseBinder.create(this, name).getValue();
    }

    public void setSql(String name) {
        this.sql = B70ParseBinder.create(this, name).getValue().toString();
    }

    public void addParam(String name) {
        params.add(B70ParseBinder.create(this, name).getValue());
    }

    public void clearParams() {
        params.clear();
    }

    public int execute() throws IOException, SQLException {
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                for (int i = 0; i < params.size(); i++) {
                    st.setObject(i + 1, params.get(i));
                }
                return st.executeUpdate();
            }
        }
    }

    public Object[] query() throws Exception {

        final List<B70MapData> result = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                for (int i = 0; i < params.size(); i++) {
                    st.setObject(i + 1, params.get(i));
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