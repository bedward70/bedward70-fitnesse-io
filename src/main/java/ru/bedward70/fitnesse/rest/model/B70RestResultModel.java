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
package ru.bedward70.fitnesse.rest.model;

/**
 * Created by Eduard Balovnev on 11.06.17.
 */
public class B70RestResultModel {

    private final int statusCode;
    private final String reasonPhrase;
    private final byte[] body;

    public B70RestResultModel(int statusCode, String reasonPhrase, byte[] body) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("B70RestResultModel{");
        sb.append("statusCode=").append(statusCode);
        sb.append(", reasonPhrase='").append(reasonPhrase).append('\'');
        sb.append(", body=");
        if (body == null) sb.append("null");
        else {
            sb.append('[');
            for (int i = 0; i < body.length; ++i)
                sb.append(i == 0 ? "" : ", ").append(body[i]);
            sb.append(']');
        }
        sb.append('}');
        return sb.toString();
    }
}