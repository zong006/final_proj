package vttp.final_backend.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface Utils {
    String newsUrl = "https://content.guardianapis.com/"; 
    String newsSearchQuery = "search?q="; // default is sort by relevance

    String negativeSearch = "crisis%20OR%20doom%20OR%20negative";
    // String negativeSearch = "neutral";
    String setSection = "&section=";
    
    String newsApiEntry = "&api-key=";

    // ======== pagination ============== //
    String newsPageSizeEntry = "&page-size=";
    // String newsPageSize = "15";
    String newsPageEntry = "&page=";
    // ======== pagination ============== //

    String showThumbnail = "&show-fields=thumbnail";

    // ======== to get list of sections ========== //
    String newsSectionQuery = "sections?";
    // ======== to get list of sections ========== //

    int articlesPerLoad = 18;

    String defaultThumbnailUrl = "https://st2.depositphotos.com/2026267/5233/i/450/depositphotos_52334423-stock-photo-news-article-on-digital-tablet.jpg";

    String sortbyNewest = "&order-by=newest";

    Set<String> excludedTopics = new HashSet<>(Arrays.asList(
            "better-business", "business-to-business", "cardiff", "community",
            "crosswords", "culture-network", "culture-professionals-network", 
            "commentisfree", "enterprise-network", "extra", "football", 
            "global-development-professionals-network", "government-computing-network",
            "guardian-foundation", "guardian-professional",
            "help", "info", "jobsadvice", "leeds", "katine", "local-government-network", "local", 
            "membership", "search", "stage", "small-business-network", "society-professionals",
             "thefilter", "theguardian", "theobserver", "theobserver", "travel/offers", "us-wellness", 
             "voluntary-sector-network", "wellness"
            )
        );

    
        String delimiter = "DELIMITER";
}
