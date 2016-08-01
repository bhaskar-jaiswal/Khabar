package com.nytarticlesearch.bhaskarjaiswal.khabar.constants;

/**
 * Created by bjaiswal on 7/27/2016.
 */
public class Config {

    // USED BY SearchActivity CLASS
    public static final String API_KEY="7e4cd095df3342a39f236889e6c09bd9";
    public static final String NYTIMES_SEARCH_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    public static final int PAGE_ZERO = 0;

    // USED BY Article CLASS
    public static final String NYTIMES_URL = "http://www.nytimes.com/";

    //USED BY SearchAdapter CLASS
    public static final int HEADLINE_SNIPPET = 0, HEADLINE_THUMBNAIL = 1, HEADLINE_NULL = -1;

    //Used by SearchCriteriaDialogFragment CLASS
    public static final String PREFERENCES = "SEARCH_CRITERIA";
    public static final String DATE_FORMAT="MM-dd-yyyy";

}
