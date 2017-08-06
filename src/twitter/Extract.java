package twitter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Extract consists of methods that extract information from a list of tweets.

 */
public class Extract {
	private static final String validChars = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789-_";
	private static boolean emailCheck = false;
	
    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
    	Instant timestamp, start, end;
    	
    	if (tweets.isEmpty()) {
    		return new Timespan(Instant.EPOCH, Instant.EPOCH);
    	}
    	
    	start = tweets.get(0).getTimestamp();
    	end = start;
    	for (Tweet t: tweets) {
    		timestamp = t.getTimestamp();
    		
    		if (timestamp.isBefore(start)) { start = timestamp; }
    		else if (timestamp.isAfter(end)) { end = timestamp; }
    	}
    	
    	return new Timespan(start, end);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
    	Set<String> mentions = new TreeSet<String>();
    	String text, user;
    	char letter, prior;
    	
    	for (Tweet t: tweets) {
    		text = t.getText().toLowerCase();
    		
    	    for (int i=0; i<text.length(); i++) {
    	    	letter = text.charAt(i);	    	    	

    	    	if (letter == '@') {
    	    		// determines email verification
    	    		if (! (i == 0)) {
    	    			prior = text.charAt(i-1);
    	    			if (validChars.contains(Character.toString(prior))) {
    	    				emailCheck = true;
    	    			}
    	    		}
    	    		
    	    		// collects username
    	            user = getMention(text.substring(i+1));
    	            if (user.length() != 0) { mentions.add(user); }
    	        }
    	    }
    	}
    	
    	return mentions;
    }

    private static String getMention(String tweet) {
    	char letter;
    	
    	for (int i=0; i<tweet.length(); i++) {
    		letter = tweet.charAt(i);
    		if (! validChars.contains(Character.toString(letter))) {    			
    			if (emailCheck && letter == '.') {
    				emailCheck = false;
    				return "";
    			}
    			
    			return tweet.substring(0, i);
    		}
    	}
    	
    	return tweet;
    }
    
    // Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
}