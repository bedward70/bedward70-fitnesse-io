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

import java.io.UnsupportedEncodingException;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70EncodingFixture extends B70DoFixture {

    /**
     * Default encoding
     */
    static final String DEFAULT_ENCODING = "UTF-8";

    /** Encoding */
    private String encoding = DEFAULT_ENCODING;

    /**
     * @return encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Reads from file as byte array
     * @param name fileName
     * @return byte array
     * @throws java.io.IOException IOException
     */

    /**
     * Convert String to bytes
     * @param name value name
     * @return array of bytes
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public byte[] stringToBytes(String name) throws UnsupportedEncodingException {

        return stringToBytes(name, encoding);
    }

    /**
     * Convert String to bytes
     * @param name value name
     * @param customEncoding custom encoding
     * @return array of bytes
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public byte[] stringToBytes(String name, final String customEncoding) throws UnsupportedEncodingException {

        final String value = B70ParseBinder.create(this, name).getValue().toString();
        return value.getBytes(customEncoding);
    }
    /**
     * Convert bytes to String
     * @param name value name
     * @param customEncoding custom encoding
     * @return string
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public String bytesToString(String name, final String customEncoding) throws UnsupportedEncodingException {

        final byte[] value = (byte[]) B70ParseBinder.create(this, name).getValue();
        return new String(value, customEncoding);
    }


    /**
     * Convert bytes to String
     * @param name value name
     * @return string
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public String bytesToString(String name) throws UnsupportedEncodingException {

        return bytesToString(name, encoding);
    }
}