package utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.Properties;
//import java.util.logging.Logger;
//import java.util.logging.Level;

/**
 * 
 * @author Diavgeia Developers
 * @author G. Razis
 * @author G. Vafeiadis
 */
public class Configuration {
	
    private static final String PROPERTIES_PREFIX = "opendata";
    //private static final Logger LOG = Logger.getLogger(Configuration.class.getName());
    
    private static final String PROP_BASE_URL = "https://diavgeia.gov.gr/luminapi/opendata"; //set to "" for test
    private static final String PROP_AUTH = "true"; //set to false for test
    private static final String PROP_USERNAME = "****";
    private static final String PROP_PASSWORD = "*****";
    
    public static final String PROP_BASE_URL_DEFAULT = "https://test3.diavgeia.gov.gr/luminapi/opendata";
    public static final String PROP_AUTH_DEFAULT = "false";
    
    public static final boolean searchDay = false;
    public static final boolean searchPeriod = true;
    public static final boolean searchMontlhy = true;
	
    public static final int RESULTS_PER_PAGE = 500; //set to 500
    public static final String rdfName = "Overall_subprojects_15-03-2016.rdf"; //Diavgeia2_New_daily
    public static final String FILEPATH = "/Users/giovaf/Documents/diavgeia_harvester/";
    
    private String baseUrl;
    private boolean auth;
    private String username;
    private String password;
    
    public Configuration() {
        this(System.getProperties());
    }
    
    public Configuration(final Properties props) {
        initializeConfiguration(props);
    }
    
    public Configuration(final String filePath) {
        this(new File(filePath));
    }
    
    public Configuration(final File file) {
        
        Properties p = new Properties();
        if (file != null) {
            try {
                FileReader f = new FileReader(file);
                p.load(f);
            } catch (IOException e) {
                throw new IllegalStateException("Error while loading configuration from " + file.getName());
            }
        }
        initializeConfiguration(p);
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public boolean isAuthenticationEnabled() {
        return auth;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    // PRIVATE ////////////////////
    private String getFullPropertyName(String toolProperty) {
        return PROPERTIES_PREFIX + "." + toolProperty;
    }
    
    private String getPropertyValue(final Properties props, final String toolProperty, final String defaultValue) {
        String propValue = props.getProperty(getFullPropertyName(toolProperty));
        if ( (propValue == null) || (propValue == "") ) {
            propValue = defaultValue;
        }
        return propValue;
    }
    
    private void initializeConfiguration(final Properties props) {
    	props.put(PROPERTIES_PREFIX + "." + "PROP_BASE_URL", PROP_BASE_URL);
        props.put(PROPERTIES_PREFIX + "." + "PROP_AUTH", PROP_AUTH);
        props.put(PROPERTIES_PREFIX + "." + "PROP_USERNAME", PROP_USERNAME);
        props.put(PROPERTIES_PREFIX + "." + "PROP_PASSWORD", PROP_PASSWORD);
        /*System.out.println("All Props:");
        System.out.println(props);
        System.out.print("\n");*/
        //logProperties(props, new String[] { "PROP_BASE_URL", "PROP_AUTH", "PROP_USERNAME", "PROP_PASSWORD" });
        String propValue = getPropertyValue(props, "PROP_BASE_URL", PROP_BASE_URL_DEFAULT);
        this.baseUrl = propValue;
        
        propValue = getPropertyValue(props, "PROP_AUTH", PROP_AUTH_DEFAULT);
        this.auth = ("true".equals(propValue));
        
        if (this.auth) {
            this.username = getPropertyValue(props, "PROP_USERNAME", null);
            this.password = getPropertyValue(props, "PROP_PASSWORD", null);
        }
    }
    
    /*private void logProperties(Properties props, String[] propNames) {
        for (String propName : propNames) {
            String key = PROPERTIES_PREFIX + "." + propName;
            String val = props.getProperty(key);
            if (val != null) {
                LOG.log(Level.INFO, "{0}: {1}", new Object[] { key, val });
            }
        }
    }*/
    
}
