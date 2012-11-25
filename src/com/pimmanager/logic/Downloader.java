/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pimmanager.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;

// TODO: Auto-generated Javadoc
/**
 * The Class Downloader.
 *
 * @author mladen
 */
@ManagedBean(name="downloadBean")
@RequestScoped
public class Downloader implements Serializable {

    /**
     * Gets the zipped my backup jar.
     *
     * @return the zipped my backup jar
     */
    public static DefaultStreamedContent getZippedMyBackupJar() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WEB-INF/files/mybackup.zip");
        String mimeType = "application/zip";
        String fileName = "MyBackup_v1.zip";
        FileDownloadController downloader = new FileDownloadController(path, mimeType, fileName);

        return downloader.getFile();
    }
}
class FileDownloadController {

    private DefaultStreamedContent file;

    public FileDownloadController(String path, String mimeType, String targetFileName) {

        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        file = new DefaultStreamedContent(stream, mimeType, targetFileName);

    }

    public DefaultStreamedContent getFile() {
        return file;
    }

    public void setFile(DefaultStreamedContent file) {
        this.file = file;
    }
}
