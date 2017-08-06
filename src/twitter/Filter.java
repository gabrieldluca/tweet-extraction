package twitter;

import java.util.List;
import java.util.ArrayList;
import java.time.Instant;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
    	List<Tweet> result = new ArrayList<Tweet>();
    	String author;
    	
    	for (int i=0; i<tweets.size(); i++) {
    		author = tweets.get(i).getAuthor().toLowerCase();
    		
    		if (author.equals(username.toLowerCase())) {
    			result.add(tweets.get(i));
    		}
    	}
    	
    	return result;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
    	Instant time, start, end;
    	List<Tweet> result = new ArrayList<Tweet>();
    	
    	start = timespan.getStart();
    	end = timespan.getEnd();
    	for (int i=0; i<tweets.size(); i++) {
    		time = tweets.get(i).getTimestamp();
    		
    		if (time.isAfter(start) && time.isBefore(end)) {
    			result.add(tweets.get(i));
    		} else if (time.equals(start) || time.equals(end)) {
    			result.add(tweets.get(i));
    		}
    	}
    	
    	return result;
    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
    	List<Tweet> result = new ArrayList<Tweet>();
    	String word, text;
    	int pos;
    	
    	for (int i=0; i<tweets.size(); i++) {
    		text = tweets.get(i).getText().toLowerCase();
    		
    		for (int j=0; j<words.size(); j++) {
    			word = words.get(j).toLowerCase();
    			if (text.contains(word)) {
    				
    				// check whether word is proceeded by a space
    				pos = text.indexOf(word)+word.length();
        			try {
        				if (text.substring(pos, pos+1).equals(" ")) {
        					result.add(tweets.get(i));
                			break;
        				}
        			} catch (Exception IndexOutOfBoundsException) {
        				result.add(tweets.get(i));
        			}
    			}
    		}
    		
    	}
    	
    	return result;
    }

    // Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
}
