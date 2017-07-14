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

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;
import org.apache.commons.io.IOUtils;
import ru.bedward70.fitnesse.io.parse.B70ParseBinder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Eduard Balovnev on 11.06.17.
 *
 */
public class B70SftpFixture extends B70DoFixture {

    private String user;
    private String password;
    private String host;
    private int port = 22;
    private String path;


    public B70SftpFixture setUser(String user) {
        this.user = user;
        return this;
    }

    public B70SftpFixture setPassword(String password) {
        this.password = password;
        return this;
    }

    public B70SftpFixture setHost(String host) {
        this.host = host;
        return this;
    }

    public B70SftpFixture setPort(int port) {
        this.port = port;
        return this;
    }

    public B70SftpFixture setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * Reads from file as byte array
     * @param name fileName
     * @return byte array
     * @throws java.io.IOException IOException
     */
    public byte[] readByteFromFile(String name) throws IOException, JSchException, SftpException {
        final String fileName = B70ParseBinder.create(this, name).getValue().toString();
        byte[] result = null;
        JSch jsch=new JSch();

        Session session =jsch.getSession(user, host, port);
        // username and password will be given via UserInfo interface.
        UserInfo ui = new UserInfo() {

            @Override
            public String getPassphrase() {
                return null;
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public boolean promptPassword(String message) {
                return true;
            }

            @Override
            public boolean promptPassphrase(String message) {
                return false;
            }

            @Override
            public boolean promptYesNo(String message) {
                return true;
            }

            @Override
            public void showMessage(String message) {
                System.out.println(message);
            }
        };
        session.setUserInfo(ui);
        session.connect();

        Channel channel=session.openChannel("sftp");
        channel.connect();
        ChannelSftp c=(ChannelSftp)channel;
        c.cd(path);
//            java.util.Vector vv=c.ls(".");
//        for(int ii=0; ii<vv.size(); ii++){
////		out.println(vv.elementAt(ii).toString());
//
//            Object obj=vv.elementAt(ii);
//            if(obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry){
//                System.out.println(((com.jcraft.jsch.ChannelSftp.LsEntry)obj).getLongname());
//            }
//
//        }

        try (InputStream is = c.get(fileName)) {
            result = IOUtils.toByteArray(is);
        }

        c.exit();
        session.disconnect();
        return result;
    }
}