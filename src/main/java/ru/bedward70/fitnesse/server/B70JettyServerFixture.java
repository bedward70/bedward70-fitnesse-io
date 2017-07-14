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
package ru.bedward70.fitnesse.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import ru.bedward70.fitnesse.io.B70DoFixture;
import ru.bedward70.fitnesse.io.parse.B70ParseBinder;

import static java.util.Objects.isNull;

/**
 * Created by Eduard Balovnev on 11.06.17.
 * https://www.leveluplunch.com/java/examples/construct-build-uri/
 * !!!Warning: it is a draft. Don't use.
 */
public class B70JettyServerFixture extends B70DoFixture {

    private final Server server = new Server();

    public void addConnectorHostPortIdleTimeout(
        final String host,
        final String port,
        final String idleTimeout
    ) {

        // HTTP connector
        ServerConnector http = new ServerConnector(server);
        http.setHost(B70ParseBinder.create(this, host).getValue().toString());
        http.setPort(getInt(B70ParseBinder.create(this, port).getValue()));
        http.setIdleTimeout(getInt(B70ParseBinder.create(this, port).getValue()));

        // Set the connector
        server.addConnector(http);
    }

    public void addContextHandlerPath(
        final String handler,
        final String path
    ) {

        ContextHandler context = new ContextHandler();
        context.setContextPath(B70ParseBinder.create(this, path).getValue().toString());
        context.setHandler((Handler) B70ParseBinder.create(this, handler).getValue());

        server.setHandler( context );
    }

    private Integer  getInt(Object value) {
        final Integer result;
        if (isNull(value)) {
            result = null;
        } else if (value instanceof Number) {
            result = ((Number) value).intValue();
        } else {
            result = Integer.valueOf(value.toString());
        }
        return result;
    }

    public Server start() throws Exception {

        server.start();
        while(!server.isRunning()) {
            Thread.sleep(100L);
        }
        return server;
    }

    public void stop(String serverName) throws Exception {

        final Server inputServer = (Server) B70ParseBinder.create(this, serverName).getValue();
        inputServer.stop();
    }
}