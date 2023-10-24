package edu.byu.mse;

import edu.byu.mse.downloader.SitemapDownloader;
import edu.byu.mse.exception.EntityImportException;
import edu.byu.mse.graph.URLGraph;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) {

        SitemapDownloader downloader = new SitemapDownloader();

        try {
           downloader.downloadSitemap();
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        URLGraph graph = new URLGraph();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader("site-data.csv"));

            String line = "";
            boolean read_headers = false;
            while((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if(read_headers)
                    graph.importObject(values);
                else {
                    read_headers = true;
                    graph.setHeaders(values);
                }
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (EntityImportException e) {
            throw new RuntimeException(e);
        }
    }


}