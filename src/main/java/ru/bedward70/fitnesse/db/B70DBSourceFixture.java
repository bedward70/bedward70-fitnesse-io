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

import org.apache.commons.dbcp.BasicDataSource;
import ru.bedward70.fitnesse.io.B70DoFixture;
import ru.bedward70.fitnesse.io.parse.B70ParseBinder;

import java.sql.SQLException;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70DBSourceFixture extends B70DoFixture {

    private String driver;
    private String url;
    private String user;
    private String password;
    private Integer maxPoolSize;

    public B70DBSourceFixture setDriver(String driver) {
        this.driver = B70ParseBinder.create(this, driver).getValue().toString();
        return this;
    }

    public B70DBSourceFixture setUrl(String url) {
        this.url = B70ParseBinder.create(this, url).getValue().toString();
        return this;
    }

    public B70DBSourceFixture setUser(String user) {
        this.user = B70ParseBinder.create(this, user).getValue().toString();
        return this;
    }

    public B70DBSourceFixture setPassword(String password) {
        this.password = B70ParseBinder.create(this, password).getValue().toString();
        return this;
    }

    public B70DBSourceFixture setMaxPoolSize(String maxPoolSize) {
        this.maxPoolSize = Integer.valueOf(B70ParseBinder.create(this, maxPoolSize).getValue().toString());
        return this;
    }

    public BasicDataSource createDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setMaxActive(maxPoolSize);
        return ds;
    }

    public void shutdownDataSource(String dataSource) throws SQLException {
        final BasicDataSource bds = (BasicDataSource) B70ParseBinder.create(this, dataSource).getValue();
        bds.close();
    }
}