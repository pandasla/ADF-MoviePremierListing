package model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.io.StringReader;

import java.io.StringWriter;
import java.io.Writer;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;

import oracle.xml.parser.v2.DOMParser;

import oracle.xml.parser.v2.XMLDocument;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;


public class MovieMetaData {
    public static final String ERROR = "ERROR";
    
    /**
     * This is main method to test the Java class as standalone.
     * @param args
     */
    public static void main(String[] args) {
        MovieMetaData test = new MovieMetaData();
        String response = test.sendServletRequest("06");
        Element xmlElement = test.parseMovieData(response);
    }
    
    /**
     * This method gets live movie data from www.myapifilms.com
     * @param month
     * @return String
     */
    public String sendServletRequest(String month) {
        String strResults = "";
        BufferedReader bufferedreader = null;
    
        try {
            // Connect to movie site and get movie data in XML format
            String movieSite = "http://www.myapifilms.com/imdb/comingSoon?format=XML&lang=en-us&date=2015-" + month
                               + "&token=236cf26e-e04f-4e92-b8ee-d3040bde3ffd";
            URL url = new URL(movieSite);
            
            HttpURLConnection urlconnection = (HttpURLConnection)url.openConnection();
            urlconnection.setRequestMethod("GET");
            urlconnection.setDoOutput(true);
            bufferedreader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "utf-8"));
            for (String strLine = "";(strLine = bufferedreader.readLine()) != null; ) {
                strResults = (new StringBuilder()).append(strResults).append(" ").append(strLine).toString();
            }

            return strResults.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        } finally {
            try {
              bufferedreader.close();
            } catch(Exception e) {
                e.printStackTrace();
                return ERROR;
            }
        }
    }
    
    /**
     * This method parses raw movie data and create simple XML element that can be populated to View Object
     * @param strData
     * @return Element
     */
    public Element parseMovieData(String strData) {
        Element rootElement = null;
        
        try {
            // Parse rawMovieData to generate xml Element object
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            
            Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(strData.getBytes("utf-8"))));
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("movies");
            
            // For new xml document to populate View Object to display on UI
            // root elements
            Document docForVO = dBuilder.newDocument();
            rootElement = docForVO.createElement("movies");
            docForVO.appendChild(rootElement);
             
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());
            
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    
                    //System.out.println("Movie Date : " + eElement.getElementsByTagName("date").item(0).getTextContent());
                    NodeList nMovieList = eElement.getElementsByTagName("movie");
                    
                    for (int j = 0; j < nMovieList.getLength(); j++) {
                        Node nMovie = nMovieList.item(j);
                        if (nMovie.getNodeType() == Node.ELEMENT_NODE) {
                            Element eMovieElement = (Element) nMovie;
                            
                            // movie elements for new xml document to populate View Object to display on UI
                            Element movie = docForVO.createElement("movieRow");
                            rootElement.appendChild(movie);
                            
                            // set attribute to the movie element
                            Element title = docForVO.createElement("Title");
                            title.setTextContent(eMovieElement.getElementsByTagName("title").item(0).getTextContent());
                            movie.appendChild(title);
                            
                            // set date attribute to the movie element
                            Element date = docForVO.createElement("StartDate");
                            date.setTextContent(eElement.getElementsByTagName("date").item(0).getTextContent());
                            movie.appendChild(date);
                            
                            // set rated attribute to the movie element
                            Element rated = docForVO.createElement("Rated");
                            String strRated = eMovieElement.getElementsByTagName("rated").item(0).getTextContent();
                            if (strRated==null || strRated.equals("")) {
                                strRated = "N/A";
                            }
                            rated.setTextContent(strRated);
                            movie.appendChild(rated);
                            
                            // set posterURL attribute to the movie element
                            Element posterURL = docForVO.createElement("PosterURL");
                            posterURL.setTextContent(eMovieElement.getElementsByTagName("urlPoster").item(0).getTextContent());
                            movie.appendChild(posterURL);
                            
                            // set linkURL attribute to the movie element
                            Element linkURL = docForVO.createElement("LinkURL");
                            linkURL.setTextContent(eMovieElement.getElementsByTagName("urlIMDB").item(0).getTextContent());
                            movie.appendChild(linkURL);
                            //System.out.println("linkURL is " +linkURL);
                            
                            // set simplePlot attribute to the movie element
                            Element simplePlot = docForVO.createElement("Summary");
                            simplePlot.setTextContent(eMovieElement.getElementsByTagName("simplePlot").item(0).getTextContent());
                            movie.appendChild(simplePlot);
                        }
                    }
                }
            }
            
            // Check the generated xml document
            //prettyPrint(docForVO);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return rootElement;
    }
    
    /**
     * This method prints XML document
     */
    private void prettyPrint(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        System.out.println(out.toString());
    }
}
