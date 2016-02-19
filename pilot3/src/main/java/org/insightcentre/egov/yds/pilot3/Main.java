/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.insightcentre.egov.yds.pilot3;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.insightcentre.egov.yds.pilot3.harvester.PilotThreeHarvester;

/**
 *
 * @author Islam A. Hassan
 */
public class Main {
    public static void main(String[] args) throws Exception {
        final PilotThreeHarvester harvester = new PilotThreeHarvester("crawl", true);

        harvester.setThreads(100);
        harvester.setTopN(1000);
        //crawler.setResumable(true);
        /*start crawl with depth of 4*/
        harvester.start(500000);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    harvester.exportFile();
                } catch (IOException ex) {
                    Logger.getLogger(PilotThreeHarvester.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));

    }
}
