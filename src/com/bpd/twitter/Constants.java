package com.bpd.twitter;


public class Constants {

	public static final String CONSUMER_KEY = "GMlGXTXf4WvSGSuydoOaQ";//"vIo3pSDiLcBL0cCgAaqOQ";
	public static final String CONSUMER_SECRET= "V9FNz3tH7r4OS2JOsodNqBKALwnVdeGBMCTT2TCRuAQ";//"mjS9LiFSK2wg0JMZK6HonGJmsTtcP23r7bVucjcWQT8";
	
	public static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "https://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize?force_login=true";
	
	public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow-twitter";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

}

