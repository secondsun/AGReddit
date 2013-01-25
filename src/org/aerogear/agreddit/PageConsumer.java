package org.aerogear.agreddit;

import java.net.URI;

import org.jboss.aerogear.android.ReadFilter;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.pipeline.paging.PageConfig;
import org.jboss.aerogear.android.pipeline.paging.PageResultExtractor;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class PageConsumer implements PageResultExtractor<PageConfig>{

    public ReadFilter getNextFilter(HeaderAndBody result, PageConfig config) {
        ReadFilter filter = new ReadFilter();
        JsonParser parser =new JsonParser();
        JsonElement element = parser.parse(new String(result.getBody()));
        String next = getFromJSON(element, config.getNextIdentifier());
        if (next != null) {
        	filter.setLinkUri(URI.create("?count=25&after=" + next));
        } 
        return filter;
    }
    
    public ReadFilter getPreviousFilter(HeaderAndBody result, PageConfig config) {
        ReadFilter filter = new ReadFilter();
        JsonParser parser =new JsonParser();
        JsonElement element = parser.parse(new String(result.getBody()));
        String previous = getFromJSON(element, config.getPreviousIdentifier());
        if (previous != null) {
        	filter.setLinkUri(URI.create("?count=26&before=" + previous));
        } 
        return filter;
    }

    private String getFromJSON(JsonElement element, String nextIdentifier) {
        String[] identifiers = nextIdentifier.split("\\.");
        for( String identifier:identifiers) {
            element = element.getAsJsonObject().get(identifier);
        }
        if (element.isJsonNull()) {
        	return null;
        }
        
        return element.getAsString();
    }

}
