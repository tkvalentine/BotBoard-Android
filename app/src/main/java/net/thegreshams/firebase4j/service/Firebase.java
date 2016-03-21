package net.thegreshams.firebase4j.service;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.util.JacksonUtility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
//import org.apache.log4j.Logger;


public class Firebase {
	public static final String TAG = Firebase.class.getSimpleName();
	
//	protected static final Logger 	LOGGER = Logger.getRootLogger();
	
	public static final String		FIREBASE_API_JSON_EXTENSION = ".json";

///////////////////////////////////////////////////////////////////////////////
//
// PROPERTIES & CONSTRUCTORS
//
///////////////////////////////////////////////////////////////////////////////

	private final String baseUrl;
	private String secureToken = null;
	private List<NameValuePair> query;
	
	
	public Firebase( String baseUrl ) throws FirebaseException {

		if( baseUrl == null || baseUrl.trim().isEmpty() ) {
			String msg = "baseUrl cannot be null or empty; was: '" + baseUrl + "'";
			Log.d(TAG, ">>>Firebase.constructor(url) : ENTER");
//			LOGGER.error( msg );
			throw new FirebaseException( msg );
		}
		this.baseUrl = baseUrl.trim();
		query = new ArrayList<NameValuePair>();
		Log.d(TAG, ">>>Firebase.constructor(url) : intialized with base-url: " + this.baseUrl);
//		LOGGER.info( "intialized with base-url: " + this.baseUrl );
	}
	
	public Firebase(String baseUrl, String secureToken) throws FirebaseException {
		Log.d(TAG, ">>>Firebase.constructor(url, token) : ENTER");
		if( baseUrl == null || baseUrl.trim().isEmpty() ) {
			String msg = "baseUrl cannot be null or empty; was: '" + baseUrl + "'";
			Log.d(TAG, ">>>Firebase.constructor(url, token) : ERROR : " + msg);
//			LOGGER.error( msg );
			throw new FirebaseException( msg );
		}
		this.secureToken = secureToken;
		this.baseUrl = baseUrl.trim();
		query = new ArrayList<NameValuePair>();
		Log.d(TAG, ">>>Firebase.constructor(url, token) : intialized with base-url: " + this.baseUrl);
//		LOGGER.info( "intialized with base-url: " + this.baseUrl );
	}

///////////////////////////////////////////////////////////////////////////////
//
// PUBLIC API
//
///////////////////////////////////////////////////////////////////////////////

	/**
	 * GETs data from the base-url.
	 * 
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link FirebaseException} 
	 */
	public FirebaseResponse get() throws FirebaseException, UnsupportedEncodingException {
		Log.d(TAG, ">>>Firebase.get() : ENTER");
		return this.get( null );
	}
	
	/**
	 * GETs data from the provided-path relative to the base-url.
	 * 
	 * @param path -- if null/empty, refers to the base-url
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link FirebaseException} 
	 */
	public FirebaseResponse get( String path ) throws FirebaseException, UnsupportedEncodingException {
		Log.d(TAG, ">>>Firebase.get(path) : ENTER");

		// make the request
		String url = this.buildFullUrlFromRelativePath( path );
//url = URLEncoder.encode(url, "UTF-8");
		HttpGet request = new HttpGet( url );
		HttpResponse httpResponse = this.makeRequest( request );
		
		// process the response
		FirebaseResponse response = this.processResponse( FirebaseRestMethod.GET, httpResponse );
		Log.d(TAG, ">>>Firebase.get() : response = " +response);

		return response;
	}
	
	/**
	 * PATCHs data to the base-url
	 * 
	 * @param data -- can be null/empty
	 * @return
	 * @throws {@link FirebaseException}
	 * @throws {@link JacksonUtilityException}
	 * @throws UnsupportedEncodingException
	 */
	
	public FirebaseResponse patch(Map<String, Object> data) throws FirebaseException, JacksonUtilityException, UnsupportedEncodingException {
		return this.patch(null, data);
	}
	
	/**
	 * PATCHs data on the provided-path relative to the base-url.
	 * 
	 * @param path -- if null/empty, refers to the base-url
	 * @param data -- can be null/empty
	 * @return {@link FirebaseResponse}
	 * @throws {@link FirebaseException}
	 * @throws {@link JacksonUtilityException}
	 * @throws UnsupportedEncodingException
	 */
	
	public FirebaseResponse patch(String path, Map<String, Object> data) throws FirebaseException, JacksonUtilityException, UnsupportedEncodingException {
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );
		//HttpPut request = new HttpPut( url );
		HttpPatch request = new HttpPatch(url);
		request.setEntity( this.buildEntityFromDataMap( data ) );
		HttpResponse httpResponse = this.makeRequest( request );
				
		// process the response
		FirebaseResponse response = this.processResponse( FirebaseRestMethod.PATCH, httpResponse );
				
		return response;
	}
	
	/**
	 * 
	 * @param jsonData
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws FirebaseException
	 */
	
	public FirebaseResponse patch(String jsonData) throws UnsupportedEncodingException, FirebaseException {
		return this.patch(null, jsonData);
	}
	
	/**
	 * 
	 * @param path
	 * @param jsonData
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws FirebaseException
	 */
	
	public FirebaseResponse patch(String path, String jsonData) throws UnsupportedEncodingException, FirebaseException {
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );
		HttpPatch request = new HttpPatch( url );
		request.setEntity( this.buildEntityFromJsonData( jsonData ) );
		HttpResponse httpResponse = this.makeRequest( request );
				
		// process the response
		FirebaseResponse response = this.processResponse( FirebaseRestMethod.PATCH, httpResponse );
				
		return response;		
	}
	
	/**
	 * PUTs data to the base-url (ie: creates or overwrites).
	 * If there is already data at the base-url, this data overwrites it.
	 * If data is null/empty, any data existing at the base-url is deleted.
	 * 
	 * @param data -- can be null/empty
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link JacksonUtilityException}
	 * @throws {@link FirebaseException}
	 */
	public FirebaseResponse put( Map<String, Object> data ) throws JacksonUtilityException, FirebaseException, UnsupportedEncodingException {
		return this.put( null, data );
	}
	
	/**
	 * PUTs data to the provided-path relative to the base-url (ie: creates or overwrites).
	 * If there is already data at the path, this data overwrites it.
	 * If data is null/empty, any data existing at the path is deleted.
	 * 
	 * @param path -- if null/empty, refers to base-url
	 * @param data -- can be null/empty
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link JacksonUtilityException}
	 * @throws {@link FirebaseException}
	 */
	public FirebaseResponse put( String path, Map<String, Object> data ) throws JacksonUtilityException, FirebaseException, UnsupportedEncodingException {
		
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );
		HttpPut request = new HttpPut( url );
		request.setEntity( this.buildEntityFromDataMap( data ) );
		HttpResponse httpResponse = this.makeRequest( request );
		
		// process the response
		FirebaseResponse response = this.processResponse( FirebaseRestMethod.PUT, httpResponse );
		
		return response;
	}
	
	/**
	 * PUTs data to the provided-path relative to the base-url (ie: creates or overwrites).
	 * If there is already data at the path, this data overwrites it.
	 * If data is null/empty, any data existing at the path is deleted.
	 * 
	 * @param jsonData -- can be null/empty
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link FirebaseException}
	 */
	public FirebaseResponse put( String jsonData ) throws FirebaseException, UnsupportedEncodingException {
		return this.put( null, jsonData );
	}

	/**
	 * PUTs data to the provided-path relative to the base-url (ie: creates or overwrites).
	 * If there is already data at the path, this data overwrites it.
	 * If data is null/empty, any data existing at the path is deleted.
	 * 
	 * @param path -- if null/empty, refers to base-url
	 * @param jsonData -- can be null/empty
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link FirebaseException}
	 */
	public FirebaseResponse put( String path, String jsonData ) throws FirebaseException, UnsupportedEncodingException {

		// make the request
		String url = this.buildFullUrlFromRelativePath( path );
		HttpPut request = new HttpPut( url );
		request.setEntity( this.buildEntityFromJsonData( jsonData ) );
		HttpResponse httpResponse = this.makeRequest( request );
		
		// process the response
		FirebaseResponse response = this.processResponse( FirebaseRestMethod.PUT, httpResponse );
		
		return response;		
	}
	
	/**
	 * POSTs data to the base-url (ie: creates).
	 * 
	 * NOTE: the Firebase API does not treat this method in the conventional way, but instead defines it
	 * as 'PUSH'; the API will insert this data under the base-url but associated with a Firebase-
	 * generated key; thus, every use of this method will result in a new insert even if the data already 
	 * exists.
	 * 
	 * @param data -- can be null/empty but will result in no data being POSTed
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link JacksonUtilityException}
	 * @throws {@link FirebaseException}
	 */
	public FirebaseResponse post( Map<String, Object> data ) throws JacksonUtilityException, FirebaseException, UnsupportedEncodingException {
		return this.post( null, data );
	}
	
	/**
	 * POSTs data to the provided-path relative to the base-url (ie: creates).
	 * 
	 * NOTE: the Firebase API does not treat this method in the conventional way, but instead defines it
	 * as 'PUSH'; the API will insert this data under the provided path but associated with a Firebase-
	 * generated key; thus, every use of this method will result in a new insert even if the provided path
	 * and data already exist.
	 * 
	 * @param path -- if null/empty, refers to base-url
	 * @param data -- can be null/empty but will result in no data being POSTed
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link JacksonUtilityException}
	 * @throws {@link FirebaseException}
	 */
	public FirebaseResponse post( String path, Map<String, Object> data ) throws JacksonUtilityException, FirebaseException, UnsupportedEncodingException {
		
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );
		HttpPost request = new HttpPost( url );
		request.setEntity( this.buildEntityFromDataMap( data ) );
		HttpResponse httpResponse = this.makeRequest( request );
		
		// process the response
		FirebaseResponse response = this.processResponse( FirebaseRestMethod.POST, httpResponse );
		
		return response;
	}
	
	/**
	 * POSTs data to the base-url (ie: creates).
	 * 
	 * NOTE: the Firebase API does not treat this method in the conventional way, but instead defines it
	 * as 'PUSH'; the API will insert this data under the base-url but associated with a Firebase-
	 * generated key; thus, every use of this method will result in a new insert even if the provided data 
	 * already exists.
	 * 
	 * @param jsonData -- can be null/empty but will result in no data being POSTed
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link FirebaseException}
	 */
	public FirebaseResponse post( String jsonData ) throws FirebaseException, UnsupportedEncodingException {
		return this.post( null, jsonData );
	}
	
	/**
	 * POSTs data to the provided-path relative to the base-url (ie: creates).
	 * 
	 * NOTE: the Firebase API does not treat this method in the conventional way, but instead defines it
	 * as 'PUSH'; the API will insert this data under the provided path but associated with a Firebase-
	 * generated key; thus, every use of this method will result in a new insert even if the provided path
	 * and data already exist.
	 * 
	 * @param path -- if null/empty, refers to base-url
	 * @param jsonData -- can be null/empty but will result in no data being POSTed
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link FirebaseException}
	 */
	public FirebaseResponse post( String path, String jsonData ) throws FirebaseException, UnsupportedEncodingException {
		
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );
		HttpPost request = new HttpPost( url );
		request.setEntity( this.buildEntityFromJsonData( jsonData ) );
		HttpResponse httpResponse = this.makeRequest( request );
		
		// process the response
		FirebaseResponse response = this.processResponse( FirebaseRestMethod.POST, httpResponse );
		
		return response;
	}
	
	/**
	 * Append a query to the request.
	 * 
	 * @param query -- Query string based on Firebase REST API
	 * @param parameter -- Query parameter
	 * @return Firebase -- return this Firebase object
	 */
	
	public Firebase addQuery(String query, String parameter) {
		this.query.add(new BasicNameValuePair(query, parameter));
		return this;
	}
	
	/**
	 * DELETEs data from the base-url.
	 * 
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link FirebaseException}
	 */
	public FirebaseResponse delete() throws FirebaseException, UnsupportedEncodingException {
		return this.delete( null );
	}

	/**
	 * DELETEs data from the provided-path relative to the base-url.
	 * 
	 * @param path -- if null/empty, refers to the base-url
	 * @return {@link FirebaseResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link FirebaseException}
	 */
	public FirebaseResponse delete( String path ) throws FirebaseException, UnsupportedEncodingException {
		
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );
		HttpDelete request = new HttpDelete( url );
		HttpResponse httpResponse = this.makeRequest( request );
		
		// process the response
		FirebaseResponse response = this.processResponse( FirebaseRestMethod.DELETE, httpResponse );
		
		return response;
	}

///////////////////////////////////////////////////////////////////////////////
//
// PRIVATE API
//
///////////////////////////////////////////////////////////////////////////////

	private StringEntity buildEntityFromDataMap( Map<String, Object> dataMap ) throws FirebaseException, JacksonUtilityException {
		
		String jsonData = JacksonUtility.GET_JSON_STRING_FROM_MAP( dataMap );
		
		return this.buildEntityFromJsonData( jsonData );
	}
	
	private StringEntity buildEntityFromJsonData( String jsonData ) throws FirebaseException {
		Log.d(TAG, ">>>Firebase.buildEntityFromJsonData() : ENTER");

		StringEntity result = null;
		try {
			
			result = new StringEntity( jsonData );
			
		} catch( Throwable t ) {
			
			String msg = "unable to create entity from data; data was: " + jsonData;
			Log.d(TAG, ">>>Firebase.buildEntityFromJsonData() : " + msg);
//			LOGGER.error( msg );
			throw new FirebaseException( msg, t );
			
		}
		
		return result;
	}
	
	private String buildFullUrlFromRelativePath( String path ) throws UnsupportedEncodingException {
		Log.d(TAG, ">>>Firebase.buildFullUrlFromRelativePath() : ENTER");

		// massage the path (whether it's null, empty, or not) into a full URL
		if( path == null ) {
			path = "";
		}
		path = path.trim();
		if( !path.isEmpty() && !path.startsWith( "/" ) ) {
			path = "/" + path;
		}
		if(!path.startsWith( "/" ) && !this.baseUrl.endsWith( "/" )){
			path = "/" + path;
		}
		String url = this.baseUrl + path + Firebase.FIREBASE_API_JSON_EXTENSION;

		Log.d(TAG, ">>>Firebase.buildFullUrlFromRelativePath() : query = " + query.toString());
		if(query != null && query.size() > 0) {
			url += "?";
			Iterator<NameValuePair> it = query.iterator();
			NameValuePair e;
			while(it.hasNext()) {
				e = it.next();
				url += e.getName() + "=" + URLEncoder.encode(e.getValue(), "UTF-8") + "&";
			}
		}

		Log.d(TAG, ">>>Firebase.buildFullUrlFromRelativePath() : secureToken = " + secureToken);
		if(secureToken != null) {
			if(query != null && query.size() > 0) {
				url += "auth=" + secureToken;
			} else {
				url += "?auth=" + secureToken;
			}
		}
		
		if(url.lastIndexOf("&") == url.length()) {
			StringBuilder str = new StringBuilder(url);
			str.deleteCharAt(str.length());
			url = str.toString();
		}

		Log.d(TAG, ">>>Firebase.buildFullUrlFromRelativePath() : built full url to '" + url + "' using relative-path of '" + path + "'");
//		LOGGER.info( "built full url to '" + url + "' using relative-path of '" + path + "'" );
		
		return url;
	}

	private HttpResponse makeRequest( HttpRequestBase request ) throws FirebaseException {
		Log.d(TAG, ">>>Firebase.makeRequest() : ENTER");

		HttpResponse response = null;
		
		// sanity-check
		if( request == null ) {
			
			String msg = "request cannot be null";
			Log.d(TAG, ">>>Firebase.makeRequest() : " + msg);
			//LOGGER.error( msg );
			throw new FirebaseException( msg );
		}
		
		try {
			
//			HttpClient client = new DefaultHttpClient();
//			response = client.execute( request );
/////////////////////////////////////
HttpClient client = org.apache.http.impl.client.HttpClientBuilder.create().build();
			HttpGet request2 = new HttpGet(request.getURI());

			// add request header
			request.addHeader("User-Agent", "USER_AGENT");
//			HttpResponse response = client.execute(request);
//			response = client.execute(request);
			response = client.execute(request2);

			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			Log.d(TAG, ">>>Firebase.makeRequest() : result = " + result);
////////////////////////
		} catch( Throwable t ) {
		
			String msg = "unable to receive response from request(" + request.getMethod() +  ") @ " + request.getURI();
			Log.d(TAG, ">>>Firebase.makeRequest() : unable to receive response from request(" + request.getMethod() +  ") @ " + request.getURI());
			//LOGGER.error( msg );
			throw new FirebaseException( msg, t );
			
		}
			
		return response;
	}
	
	private FirebaseResponse processResponse( FirebaseRestMethod method, HttpResponse httpResponse ) throws FirebaseException {
		Log.d(TAG, ">>>Firebase.processResponse() : ENTER");

		FirebaseResponse response = null;

		// sanity-checks
		if( method == null ) {
			
			String msg = "method cannot be null";
			Log.d(TAG, ">>>Firebase.processResponse() : " + msg);
			//LOGGER.error( msg );
			throw new FirebaseException( msg );
		}
		if( httpResponse == null ) {
			
			String msg = "httpResponse cannot be null";
			Log.d(TAG, ">>>Firebase.processResponse() : " + msg);
			//LOGGER.error( msg );
			throw new FirebaseException( msg );
		}
		
		// get the response-entity
		HttpEntity entity = httpResponse.getEntity();
		
		// get the response-code
		int code = httpResponse.getStatusLine().getStatusCode();
		
		// set the response-success
		boolean success = false;
		switch( method ) {
			case DELETE:
				if( httpResponse.getStatusLine().getStatusCode() == 204
					&& "No Content".equalsIgnoreCase( httpResponse.getStatusLine().getReasonPhrase() ) )
				{
					success = true;
				}
				break;
			case PATCH:
			case PUT:
			case POST:
			case GET:
				if( httpResponse.getStatusLine().getStatusCode() == 200
					&& "OK".equalsIgnoreCase( httpResponse.getStatusLine().getReasonPhrase() ) )
				{
					success = true;
				}
				break;
			default:
				break;
				
		}
		
		// get the response-body
		Writer writer = new StringWriter();
		if( entity != null ) {
			
			try {
				
				InputStream is = entity.getContent();
				char[] buffer = new char[1024];
				Reader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
				int n;
				while( (n=reader.read(buffer)) != -1 ) {
					writer.write( buffer, 0, n );
				}
				
			} catch( Throwable t ) {
				
				String msg = "unable to read response-content; read up to this point: '" + writer.toString() + "'";
				writer = new StringWriter(); // don't want to later give jackson partial JSON it might choke on
				Log.d(TAG, ">>>Firebase.processResponse() : " + msg);
				//LOGGER.error( msg );
				throw new FirebaseException( msg, t );
				
			}
		}
		
		// convert response-body to map
		Map<String, Object> body = null;
		try {
			
			body = JacksonUtility.GET_JSON_STRING_AS_MAP( writer.toString() );
			
		} catch( JacksonUtilityException jue ) {
			
			String msg = "unable to convert response-body into map; response-body was: '" + writer.toString() + "'";
			Log.d(TAG, ">>>Firebase.processResponse() : " + msg);
			//LOGGER.error( msg );
			throw new FirebaseException( msg, jue );
		}
		
		// build the response
		response = new FirebaseResponse( success, code, body, writer.toString() );
		
		//clear the query
		query = null;
		
		return response;
	}

///////////////////////////////////////////////////////////////////////////////
//
// INTERNAL CLASSES
//
///////////////////////////////////////////////////////////////////////////////

	public enum FirebaseRestMethod {
		GET,
		PATCH,
		PUT,
		POST,
		DELETE;
	}
	
}





