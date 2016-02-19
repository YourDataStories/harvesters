/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insightcentre.egov.yds.pilot3.harvester;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.insightcentre.egov.yds.pilot3.rdfizer.DataRdfizer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Islam A. Hassan
 */
public class PilotThreeHarvester extends BreadthCrawler {

    static JSONArray result = new JSONArray();

    /**
     * @param crawlPath crawlPath is the path of the directory which maintains
     * information of this crawler
     * @param autoParse if autoParse is true,BreadthCrawler will auto extract
     * links which match regex rules from pag
     */
    public PilotThreeHarvester(String crawlPath, boolean autoParse) {

        super(crawlPath, autoParse);

        /*start page*/
        this.addSeed("https://irl.eu-supply.com/ctm/supplier/publictenders");

        /*fetch url like http://news.yahoo.com/xxxxx*/
        this.addRegex("https://irl.eu-supply.com/ctm/supplier/");
        /*do not fetch url like http://irl.eu-supply.com/xxxx/xxx)*/
        //this.addRegex("-http://irl.eu-supply.com/.+/.*");
        /*do not fetch jpg|png|gif*/
        this.addRegex("-.*\\.(jpg|png|gif).*");
        /*do not fetch url contains #*/
        //this.addRegex("-.*#.*");

    }

///html/body/div/table/tbody/tr/td[@class="bold l pu"]
///html/body/div/table/tbody/tr/td[@class='l w pd']
//td[class$="bold l pu"]
//td[class$="l w pd"]
//span[class$="s"] b   
//td[class$="bold l pu"] a[href]  
    @Override
    public void visit(Page page, Links nextLinks) {
        String url = page.getUrl();
        String csvContent = "";
        csvContent = csvContent + "url,";
        csvContent = csvContent + "title,";
        csvContent = csvContent + "instanceURL,";
        csvContent = csvContent + "category,";
        csvContent = csvContent + "tendar,";
        csvContent = csvContent + "tendarURL,";
        csvContent = csvContent + "content,";
        csvContent = csvContent + "time\n";

        /*if page is news page*/
        if (Pattern.matches("https://irl.eu-supply.com/ctm/supplier/\\/.*", url)) {
            /*we use jsoup to parse page*/
            Document doc = page.getDoc();
            System.out.println("URL:" + url);
            System.out.println("========================= Page =============================");
            /*extract title and content of news by css selector*/
            //String title = doc.select("h1[class=headline]").first().text();
            //String content = doc.select("div[class=body yom-art-content clearfix]").first().text();
            Elements head = doc.select("td[class$=\"bold l pu\"]");
            Elements body = doc.select("td[class$=\"l w pd\"]");
            JSONObject x = new JSONObject();
            for (int i = 0; i < body.size(); i++) {
                Element headEelement = head.get(i);
                Elements headAEelements = headEelement.select("a");
                String title = headAEelements.get(4).text();
                String instanceURL = headAEelements.get(4).attr("href");
                String tendarCategory = headAEelements.get(3).text();
                String tendar = headAEelements.get(5).text();
                String tendarURL = headAEelements.get(5).attr("href");
                String tendarLocation = headAEelements.get(5).attr("title").replace("Location: ", "");
                Elements headDateEelements = headEelement.select("span[class$=\"s\"] b");
                String time = headDateEelements.get(0).text();

                String instanceBody = body.get(i).toString();
                x.put("url", url);
                x.put("title", title);
                x.put("instanceURL", instanceURL);
                x.put("category", tendarCategory);
                x.put("tendar", tendar);
                x.put("tendarLocation", tendarLocation);
                x.put("tendarURL", tendarURL);
                x.put("content", instanceBody);
                x.put("time", time);

                csvContent = csvContent + url + ",";
                csvContent = csvContent + title + ",";
                csvContent = csvContent + instanceURL + ",";
                csvContent = csvContent + tendarCategory + ",";
                csvContent = csvContent + tendar + ",";
                csvContent = csvContent + tendarURL + ",";
                csvContent = csvContent + tendarLocation + ",";
                csvContent = csvContent + time + ",";

                System.out.println("url" + url);
                System.out.println("title" + title);
                System.out.println("instanceURL" + instanceURL);
                System.out.println("category" + tendarCategory);
                System.out.println("tendar" + tendar);
                System.out.println("tendarURL" + tendarURL);
                System.out.println("content" + tendarLocation);
                System.out.println("time" + time);
                if (headDateEelements.size() == 2) {
                    String day = headDateEelements.get(1).text();
                    System.out.println("Day:\n" + day);
                    x.put("day", day);
                }
                result.put(x);
                //System.out.println("Content:\n" + instanceBody);
                //System.out.println("=========================");
            }
            DataRdfizer rdfizer = new DataRdfizer();
            try {
                rdfizer.run(csvContent);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PilotThreeHarvester.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(PilotThreeHarvester.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                rdfizer.rdfWriter();
            } catch (IOException ex) {
                Logger.getLogger(PilotThreeHarvester.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*If you want to add urls to crawl,add them to nextLinks*/
            /*WebCollector automatically filters links that have been fetched before*/
            /*If autoParse is true and the link you add to nextLinks does not match the regex rules,the link will also been filtered.*/
            // nextLinks.add("http://xxxxxx.com");
            nextLinks.addAllFromDocument(page.getDoc(), "p a");
        }
    }

    

    public static void exportFile() throws IOException {

        FileWriter writer = new FileWriter("resultoutput01.json");
        writer.append(result.toString());
        writer.flush();
        writer.close();
    }
}
