package twitter;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SocialNetworkTest {

    /**
     * Testing strategy
     * 
     * 1) @guessFollowGraph
     * 
     * 		Partitioned the inputs as follows:
     * 			@param tweets
     * 			- Empty set
     * 			- Non-empty set
     * 
     * 
     * 2) @influencers
     * 
     * 		Partitioned the inputs as follows:
     * 			@param followsGraph
     * 			- Empty graph
     * 			- Singleton graph
     * 			- Graph with multiple elements (incl. repetition)
     */
	    
    private static final Tweet tweet1 = new Tweet(1, "LoVeSbEnEdIcT", "@slaurentherin,@SLAURENTHERIN THE MOST ADORABLE PERSON EVER", Instant.EPOCH);
    private static final Tweet tweet2 = new Tweet(2, "slaurentherin", "@lovesbenedict THANK YOU BBY", Instant.EPOCH);
    private static final Tweet tweet3 = new Tweet(3, "bbItdIddlE", "@lOvEsbEnEdIct,@slaurentherin @bbitdiddle that's great!", Instant.EPOCH);
    private static final Tweet tweet4 = new Tweet(4, "lovesbEnEdIcT", "@lovesbenedict", Instant.EPOCH);
   
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // enable assertions with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList());
    
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphNonEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4));
        Set<String> keySet = followsGraph.keySet();
        
        assertEquals("expected graph with three elements", 3, followsGraph.size());
        if (keySet.contains("bbitdiddle")) {
        	assertEquals("expected graph to guess follow", followsGraph.get("bbitdiddle"), new HashSet<String>(Arrays.asList("lovesbenedict", "slaurentherin")));
        } else {
        	assertEquals("expected graph to guess follow", followsGraph.get("BBITDIDDLE"), new HashSet<String>(Arrays.asList("lovesbenedict", "slaurentherin")));
        }
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersSingleton() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("LoVeSBeNeDiCT", new HashSet<>(Arrays.asList("slaurentherin", "mAdOnnA")));
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected singleton list", 3, influencers.size());
        assertTrue("expected influencers to contain user", influencers.contains("madonna"));
    }
    
    @Test
    public void testInfluencersMultiple() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("lovesbenedict", new HashSet<>(Arrays.asList("slaurentherin")));
        followsGraph.put("slaurentherin", new HashSet<>(Arrays.asList("lovesbenedict")));
        followsGraph.put("bbitdiddle", new HashSet<>(Arrays.asList("lovesbenedict")));
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertEquals("expected list with three elements", 3, influencers.size());
        assertEquals("expected sorted order", "bbitdiddle", influencers.get(2));
    }
    
    // Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
}