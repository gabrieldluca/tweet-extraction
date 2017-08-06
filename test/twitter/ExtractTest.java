package twitter;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

/**
 * Testing strategy
 *
 * 1) @getTimespan
 * 
 * 		Partitioned the inputs as follows:
 * 			@param tweets
 * 			- Same Timespan
 * 			- Different Timespan (incl. min/max)
 * 			- No Timespan
 * 
 * 
 * 2) @getMentionedUsers
 * 
 * 		Partitioned the inputs as follows:
 * 			@param tweets
 * 			- No user mentioned (incl. invalid)
 * 			- Same user mentioned in the same tweet
 * 			- Same user mentioned in different tweets
 * 			- Different users mentioned in the same tweet
 * 			- Different users mentioned in different tweets
 */
public class ExtractTest {    
    private static final Instant d1 = Instant.parse("2017-01-01T23:59:59Z");
    private static final Instant d2 = Instant.parse("2017-12-31T00:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "valid is@@ it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes testing@email.com #nouser", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "@ invalid @/invalid", d1);
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "@_valid-ish.", Instant.MIN);
    private static final Tweet tweet5 = new Tweet(5, "bbitdiddle", "@oneuser @ONEUSER valid @@@", Instant.MAX);
    private static final Tweet tweet6 = new Tweet(6, "bbitdiddle", "valid @@twodifferent @users @/! @oneuser", d2);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // enable assertions with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanEmpty() {
        Timespan timespan = Extract.getTimespan(Arrays.asList());
        
        assertEquals("expected start == end", timespan.getStart(), timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet4, tweet5, tweet2));
        
        assertEquals("expected start", Instant.MIN, timespan.getStart());
        assertEquals("expected end", Instant.MAX, timespan.getEnd());
    }
    
    @Test
    public void testGetSameTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet3));
        
        assertEquals("expected start == end", d1, timespan.getStart());
        assertEquals("expected start == end", d1, timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1, tweet2, tweet3));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersUnderline() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));

        assertTrue("expected one mention", mentionedUsers.size() == 1);
    }
    
    @Test
    public void testGetMentionedUserCaps() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
    
        assertTrue("expected one mention", mentionedUsers.size() == 1);
    }
    
    @Test
    public void testGetMultipleMentionedUsers() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6));
        
        assertTrue("expected two mentions", mentionedUsers.size() == 3);
    }

    @Test
    public void testGetMentionsMultipleTweets() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5, tweet6));
        
        assertTrue("expected three mentions", mentionedUsers.size() == 3);
    }

    // Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
}