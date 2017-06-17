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

import org.apache.commons.io.FileUtils;
import ru.bedward70.fitnesse.io.parse.B70ParseBinder;
import ru.bedward70.fitnesse.io.saver.B70ByteFileSaver;
import ru.bedward70.fitnesse.io.saver.B70Saver;
import ru.bedward70.fitnesse.io.saver.B70TextFileSaver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70FileFixture extends B70DoFixture {

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
     * @throws IOException IOException
     */
    public byte[] readByteFromFile(String name) throws IOException {
        final String fileName = B70ParseBinder.create(this, name).getValue().toString();
        byte[] result = null;
        final File file = new File(fileName);
        if (file.exists()) {
            result = FileUtils.readFileToByteArray(file);
        } else {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        return result;
    }

    /**
     * Reads from file as string
     * @param name name
     * @return string
     * @throws IOException IOException
     */
    public String readFromFile(String name) throws IOException {
        return readFromFileEncoding(name, encoding);
    }

    /**
     * Reads from file as string
     * @param name name
     * @param customEncoding custom encoding
     * @return string
     * @throws IOException IOException
     */
    public String readFromFileEncoding(String name, final String customEncoding) throws IOException {
        final String fileName = B70ParseBinder.create(this, name).getValue().toString();
        String result = null;
        final File file = new File(fileName);
        if (file.exists()) {
            result = FileUtils.readFileToString(file, customEncoding);
        } else {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        return result;
    }


    /**
     * Deletes file
     * @param fileName fileName
     * @return true if it was deleted.
     * @throws IOException IOException
     */
    public boolean deleteFile(String fileName) throws IOException {
        final File file = new File(fileName);
        return file.exists() && file.delete();
    }

    /**
     * Creates B70Saver instance  to file
     * @param fileName fileName
     * @throws Exception Exception
     */
    public B70Saver createByteFileSaver(final String fileName) throws Exception {
        return new B70ByteFileSaver(fileName);
    }

    /**
     * Creates B70Saver instance  to file
     * @param fileName fileName
     * @throws Exception Exception
     */
    public B70Saver createTextFileSaver(final String fileName) throws Exception {
        return createTextFileSaverEncoding(fileName, encoding);
    }

    /**
     * Creates B70Saver instance  to file
     * @param fileName fileName
     * @param customEncoding custom encoding
     * @throws Exception Exception
     */
    public B70Saver createTextFileSaverEncoding(final String fileName, final String customEncoding) throws Exception {
        return new B70TextFileSaver(fileName, customEncoding);
    }
}