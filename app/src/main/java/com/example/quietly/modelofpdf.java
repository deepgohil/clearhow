package com.example.quietly;

public class modelofpdf {

    String filename,fileurl,totalpage;

    public modelofpdf()
    {

    }

    public modelofpdf(String filename, String fileurl, String totalpage) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.totalpage = totalpage;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(String totalpage) {
        this.totalpage = totalpage;
    }
}
