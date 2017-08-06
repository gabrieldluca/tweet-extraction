package twitter;

import java.util.*;

class ValueComparator implements Comparator<String> { 
	HashMap<String, Integer> map = new HashMap<String, Integer>();
 
	protected ValueComparator(Map<String, Integer> map) {
		this.map.putAll(map);
	}
 
	@Override
	public int compare(String s1, String s2) {
		if (map.get(s1) >= map.get(s2)) { return -1; }
		else { return 1; }
	}
}

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie @-mentions
     *         Bert in a tweet. All the Twitter usernames in the returned social
     *         network must be either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
    	Map<String, Set<String>> followGraph = new HashMap<String, Set<String>>();
    	Set<String> mentions, previous;
    	String user;
    	
    	for (Tweet twt: tweets) {
    		user = twt.getAuthor().toLowerCase();
    		mentions = Extract.getMentionedUsers(Arrays.asList(twt));
    		
    		if (! followGraph.containsKey(user)) {
    			followGraph.put(user, new HashSet<String>());
    		}
    		
    		while (mentions.contains(user)) { mentions.remove(user); }
    		
    		if (! mentions.isEmpty()) {
    			previous = followGraph.get(user);
    			previous.addAll(mentions);
    			followGraph.put(user, previous);
    		}
    	}
    	
    	return followGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
    	Map<String, Integer> influenceMap = new HashMap<String, Integer>();
    	Set<String> mentions;
    	String user;
    	
    	for (Map.Entry<String, Set<String>> entry : followsGraph.entrySet()) {
    		user = entry.getKey().toLowerCase();
    		mentions = entry.getValue();
    		
    		// add user entry
    		if (!influenceMap.containsKey(user)) {
                influenceMap.put(user, 0);
    		} else {
    			influenceMap.merge(user, 1, Integer::sum);
    		}
    		
    		// add mentions entries
    		for (String mention: mentions) {
    			mention = mention.toLowerCase();
                if (!influenceMap.containsKey(mention)) {
                    influenceMap.put(mention, 0);
                } else {
                    influenceMap.merge(mention, 1, Integer::sum);
                }
    		}
    	}
    
    	influenceMap = sortMapByValue(influenceMap);
        return new ArrayList<String>(influenceMap.keySet());
    }
    
    private static TreeMap<String, Integer> sortMapByValue(Map<String, Integer> map){
		Comparator<String> comparator = new ValueComparator(map);
		TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
		result.putAll(map);
		
		return result;
	}
    
    // Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
}