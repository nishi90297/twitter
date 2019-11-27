package com.twitter.dao;
 
import java.util.List;

import com.twitter.entity.TwitterEntity;

public interface TwitterDAO {

	public String createUser(TwitterEntity twitterEntity) throws Exception;
	public String loginUser(String emailId, String password) throws Exception;
	public String followUser(String emailId) throws Exception;
	public List<TwitterEntity> followersList() throws Exception;
	public int postTweet(String post) throws Exception;
}
