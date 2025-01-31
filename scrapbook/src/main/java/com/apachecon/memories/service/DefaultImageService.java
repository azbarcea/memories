/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apachecon.memories.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.markup.html.form.upload.FileUpload;

public class DefaultImageService implements ImageService {

    private static final FilenameFilter FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            name = name.toLowerCase();
            return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif");
        }
    };

    private File approveDirectory;
    private File declineDirectory;
    private File uploadDirectory;

    @Override
    public void newFile(FileUpload upload) throws Exception {
        String ext = upload.getClientFileName().substring(upload.getClientFileName().lastIndexOf('.'));
        File file = new File(uploadDirectory, UUID.randomUUID() + ext);

        InputStream is = upload.getInputStream();
        FileOutputStream os = new FileOutputStream(file);

        byte[] buffer = new byte[512];
        int length = 0;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        os.close();
        is.close();
    }

    @Override
    public List<UserFile> getUploaded() {
        return list(uploadDirectory.listFiles(FILTER), null);
    }

    @Override
    public List<UserFile> getApproved() {
        return list(approveDirectory.listFiles(FILTER), true);
    }

    @Override
    public List<UserFile> getDeclined() {
        return list(declineDirectory.listFiles(FILTER), false);
    }

    private List<UserFile> list(File[] listFiles, Boolean approved) {
        if (listFiles == null) {
            return Collections.emptyList();
        }

        List<UserFile> files = new ArrayList<UserFile>();
        for (File file : listFiles) {
            files.add(new UserFile(file, approved));
        }
        return files;
    }

    @Override
    public List<UserFile> getAll() {
        List<UserFile> files = new ArrayList<UserFile>();
        files.addAll(getUploaded());
        files.addAll(getApproved());
        files.addAll(getDeclined());
        //Collections.shuffle(files);
        return files;
    }

    public void setApproveDirectory(File directory) {
        approveDirectory = directory;
        approveDirectory.mkdirs();
    }

    public void setDeclineDirectory(File directory) {
        declineDirectory = directory;
        declineDirectory.mkdirs();
    }

    public void setUploadDirectory(File directory) {
        uploadDirectory = directory;
        uploadDirectory.mkdirs();
    }

    public void approve(String name) {
        File uf = new File(uploadDirectory, name);
        File af = new File(approveDirectory, name);
        uf.renameTo(af);
        
        uf = new File(uploadDirectory, name + "_thumb");
        af = new File(approveDirectory, name + "_thumb");
        if (uf.exists()) {
            uf.renameTo(af);
        }
    }

    public void decline(String name) {
        File uf = new File(uploadDirectory, name);
        File af = new File(declineDirectory, name);
        uf.renameTo(af);
        
        uf = new File(uploadDirectory, name + "_thumb");
        af = new File(declineDirectory, name + "_thumb");
        if (uf.exists()) {
            uf.renameTo(af);
        }
    }

}
