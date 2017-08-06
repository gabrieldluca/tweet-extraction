package twitter;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class FilterTest {

    /**
     * Testing strategy
     * 
     * 1) @writtenBy
     * 
     * 		Partitioned the inputs as follows:
     * 			@param username
     * 			- Username not in tweets
     * 			- Single username in tweets
     * 			- Multiple usernames in tweets
     * 
     * 			@param tweets
     * 			- Empty list
     * 			- Non-empty list
     * 
     * 
     * 2) @inTimespan
     * 
     * 		Partitioned the inputs as follows:
     * 			@param timespan
     * 			- No tweet in timespan
     * 			- Single tweet in timespan
     * 			- Multiple tweets in timespan
     * 
     * 
     * 3) @containing
     * 		
     * 		Partitioned the inputs as follows:
     * 			@param words
     * 			- word not contained in tweets
     * 			- word contained in tweets (incl. noncase-sensitive)
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "bBitDIDDlE", "does capitalizing letters really make any difference? curious", d2);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // enable assertions with VM argument: -ea
    }

    @Test
    public void testWrittenByEmptyTweets() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(), "obama");
        
        assertTrue("expected empty list", writtenBy.isEmpty());
    }
    
    @Test
    public void testWrittenByEmptyResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "beyonce");
        
        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenBySingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenByMultipleResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet2, tweet1, tweet3), "bbitdiddle");
        
        assertEquals("expected list with two elements", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet2));
    }
    
    @Test
    public void testInTimespanEmptyResult() {
        Instant testStart = Instant.parse("2016-02-17T10:00:01Z");
        Instant testEnd = Instant.parse("2016-02-17T10:30:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertTrue("expected empty list", inTimespan.isEmpty());
    }
    
    @Test
    public void testInTimespanSingleResult() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T10:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertEquals("expected singleton list", 1, inTimespan.size());
        assertTrue("expected list to contain tweet", inTimespan.contains(tweet1));
    }
    
    @Test
    public void testInTimespanMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertEquals("expected list with two elements", 2, inTimespan.size());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    @Test
    public void testContainingEmptyResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("rive"));
        
        assertTrue("expected non-empty list", containing.isEmpty());
    }
    
    @Test
    public void testContainingSingleResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("cUrIOUs"));
        
        assertEquals("expected singleton list", 1, containing.size());
        assertTrue("expected list to contain tweet", containing.contains(tweet3));
    }
    
    @Test
    public void testContainingMultipleResults() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("kpop", "talk"));
        
        assertEquals("expected list with two elements", 2, containing.size());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 1, containing.indexOf(tweet2));
    }
    
    // Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
}
