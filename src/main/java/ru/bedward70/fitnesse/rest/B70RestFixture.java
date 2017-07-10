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
package ru.bedward70.fitnesse.rest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import ru.bedward70.fitnesse.io.B70DoFixture;
import ru.bedward70.fitnesse.io.parse.B70ParseBinder;
import ru.bedward70.fitnesse.rest.model.B70RestResultModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Created by Eduard Balovnev on 11.06.17.
 * https://www.leveluplunch.com/java/examples/construct-build-uri/
 * !!!Warning: it is a draft. Don't use.
 */
public class B70RestFixture extends B70DoFixture {

    private final Map<String, String> headers = new HashMap<>();
    private final List<Object> pathParameters = new ArrayList<>();
    private final List<NameValuePair> queryParameters = new ArrayList<>();

    private String body;


    public void setBody(String value) {

        body = B70ParseBinder.create(this, value).getValue().toString();
    }

    public void addHeaderTo(String name, String value) {

        headers.put(
            B70ParseBinder.create(this, name).getValue().toString(),
            B70ParseBinder.create(this, value).getValue().toString()
        );
    }

    public void addPathParameter(String name) {

        pathParameters.add(
            B70ParseBinder.create(this, name).getValue().toString()
        );
    }

    public void addQueryParameterTo(String name, String value) {

        queryParameters.add(
            new BasicNameValuePair(
                B70ParseBinder.create(this, name).getValue().toString(),
                B70ParseBinder.create(this, value).getValue().toString()
            )
        );
    }

    public B70RestResultModel put() throws IOException, URISyntaxException {
        HttpEntityEnclosingRequestBase put = new HttpPut(uri());
        return execute(put);
    }

    public B70RestResultModel post() throws IOException, URISyntaxException {
        HttpEntityEnclosingRequestBase post = new HttpPost(uri());
        return execute(post);
    }

    private B70RestResultModel execute(HttpEntityEnclosingRequestBase enclosingRequestBase) throws IOException {
        if (!isNull(body)) {
            StringEntity params = new StringEntity(body, "UTF-8");
            enclosingRequestBase.setEntity(params);
        }
        return executeHttpRequestBase(enclosingRequestBase);
    }



    public B70RestResultModel get() throws IOException, URISyntaxException {

        HttpGet get = new HttpGet(uri());
        return executeHttpRequestBase(get);
    }

    private B70RestResultModel executeHttpRequestBase(HttpRequestBase requestBase) throws IOException {

        final HttpClient client = new DefaultHttpClient();
        headers.entrySet().stream().forEach(
            entry -> requestBase.setHeader(entry.getKey(), entry.getValue())
        );

        final HttpResponse response = client.execute(requestBase);
        byte[] resultBody = null;
        if (!isNull(response.getEntity().getContent())) {
            try (InputStream is = response.getEntity().getContent()) {
                resultBody = IOUtils.toByteArray(is);
            }
        }
        return new B70RestResultModel(
            response.getStatusLine().getStatusCode(),
            response.getStatusLine().getReasonPhrase(),
            resultBody
        );
    }

    private URI uri() throws URISyntaxException {

        final String path = bindArguments.getBindArgs()
            .stream()
            .map(Object::toString)
            .collect(Collectors.joining());

        final String pathPathParameters = applyPathParameters(path);

        return new URIBuilder(pathPathParameters)
            .addParameters(queryParameters)
            .build();
    }

    private String applyPathParameters(String path) {
        final Pattern pattert = Pattern.compile("(?<prefix>.*)(?<param>\\{.+\\})(?<suffix>.*)");
        String template = path;
        Matcher m = null;
        int i = 0;
        while ((m = pattert.matcher(template)).find() ) {
            template = m.group("prefix") + pathParameters.get(0) + m.group("suffix");
            i++;
        }
        return template;
    }
}