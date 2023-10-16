package edu.byu.mse;

import edu.byu.mse.downloader.SitemapDownloader;
import edu.byu.mse.exception.EntityImportException;
import edu.byu.mse.graph.XMLGraphMaker;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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

        File file = new File("sitemap.xml");

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newDefaultInstance();
        Document doc = null;

        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            doc = docBuilder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("url");

        XMLGraphMaker maker = new XMLGraphMaker(nList);

        try {
            maker.importObjects();
        } catch (EntityImportException e) {
            throw new RuntimeException(e);
        }
    }


}