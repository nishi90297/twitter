package com.twitter.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.twitter.entity.TweetEntity;
import com.twitter.entity.TwitterEntity;
import com.twitter.repository.TweetRepository;
import com.twitter.repository.TwitterRepository;

@Repository(value = "twitterDAO")
public class TwitterDAOImpl implements TwitterDAO{

	private TwitterEntity currentUser;
	
	@Autowired
	private TwitterRepository twitterRepository;
	
	@Autowired
	private TweetRepository tweetRepository;
	
	@Override
	public String createUser(TwitterEntity twitterEntity) throws Exception {
		
		if(!twitterRepository.findById(twitterEntity.getEmailId()).isPresent()) {
			twitterRepository.save(twitterEntity);
			return "User Successfully Created !";
		}
		return "Email Id Already Existed.";
	}
	
	@Override
	public String followUser(String emailId) throws Exception{
		
		
		if(currentUser==null) {
			return "User not login !! ";
		}
		else if(!twitterRepository.findById(emailId).isPresent()) {
			return "No such user with " + emailId +" exists";
		}
		else if(currentUser.getFollowers().contains(twitterRepository.findById(emailId).get()) ){
			return emailId +" already exists";
		}
//		else if(currentUser.getFollowers().stream().filter(follower->
//		{
//			follower.getEmailId().equals(twitterRepository.findById(emailId).get().getEmailId());
//			
//		})){
//			return emailId +" already exists";
//		}
		else if(currentUser.getEmailId().equals(emailId) ){
			return "can't Follow Yourself";
		}
		else {
			System.out.println("currentUser.getFollowers() "+ currentUser.getFollowers());
			System.out.println("twitterRepository.findById(emailId) "+twitterRepository.findById(emailId).get());
			if(currentUser.getFollowers().isEmpty()) {
				List<TwitterEntity> followersList= new ArrayList<>();
				followersList.add(twitterRepository.findById(emailId).get());
				currentUser.setFollowers(followersList);
				twitterRepository.save(currentUser);
				return emailId +" is set into your followers.";
			}
			currentUser.getFollowers().add(twitterRepository.findById(emailId).get());
			twitterRepository.save(currentUser);
			return emailId +" is set into your followers.";
		}
	}
	
	@Override
	public List<TwitterEntity> followersList() throws Exception{
		
		if(currentUser!=null) {
			List<TwitterEntity> followersList=currentUser.getFollowers();
			return followersList;
		}
		else {
			return null ;
		}
	}
	
	@Override
	public int postTweet(String post) throws Exception{
		if(currentUser!=null) {
			TweetEntity tweetEntity= new TweetEntity();
			tweetEntity.setTweetedText(post);
			tweetRepository.save(tweetEntity);
			if(currentUser.getTweetsList().isEmpty()) {
				List<TweetEntity> tweetsList= new ArrayList<>();
				tweetsList.add(tweetEntity);
				currentUser.setTweetsList(tweetsList);
				twitterRepository.save(currentUser);
				
				return tweetEntity.getTweetId();
			}
			currentUser.getTweetsList().add(tweetEntity);
			twitterRepository.save(currentUser);
			return tweetEntity.getTweetId();
		}
		return 0;
	}
	
	@Override
	public String likeTweet(int tweetId) throws Exception{
		if(currentUser==null) {
			return "User not login !";
		}
		else if(!tweetRepository.findById(tweetId).isPresent()) {
			return "Tweet Do not exists!";
		}
		else {
			int likes=tweetRepository.findById(tweetId).get().getLikes();
			likes+=1;
			tweetRepository.findById(tweetId).get().setLikes(likes);
			tweetRepository.save(tweetRepository.findById(tweetId).get());
			return "You have successfully liked this post as "+ likes +"th user !";
		}
	}
	
	@Override
	public List<TweetEntity> getTweets() throws Exception {
		
		List<TweetEntity> top10List= new ArrayList<TweetEntity>();
		List<TweetEntity> tweetList=tweetRepository.findAll();
		
		int noOfTweets=tweetList.size();
		
		for(int i=noOfTweets-1;i>=noOfTweets-11;i--){
			top10List.add(tweetList.get(i));
		}
		return top10List;
//		List<TweetENtitr> tweetList = null;
//		Page<Tweet> tweetPage = tweetRepo.findAll(new PageRequest(0, 10));
//		if (tweetPage.hasContent()) {
//			tweetList = tweetPage.getContent();
//		}
//		return tweetList;
	}
}