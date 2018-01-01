package apextechies.gkquiz_hm.utilz;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


public class Utility {
	
	private static HttpClient mHttpClient;
	private static final int HTTP_TIMEOUT = 60 * 1000; // milliseconds
	
	private static HttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();
			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}
		return mHttpClient;
		
	}
	
	
	public static String executeHttpPost(String url,
                                         ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			// HttpPost request = new HttpPost(url);
			
			HttpPost request = new HttpPost();
			request.setURI(new URI(url));
			
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
			    postParameters);
			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
			    .getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line).append(NL);
			}
			in.close();
			
			return sb.toString();
			//  return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static String executeHttpPostJson(String url,
                                             String postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost();
			request.setURI(new URI(url));
			request.addHeader("Content-type", "application/json");
			request.setEntity(new StringEntity(postParameters));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
			    .getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line).append(NL);
			}
			in.close();
			
			return sb.toString();
			//  return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String executeHttpGet(String url) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
			    .getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line).append(NL);
			}
			in.close();
			
			return sb.toString();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static String executeHttpPut(String url,
                                        ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			// HttpPost request = new HttpPost(url);
			
			HttpPut request = new HttpPut();
			request.setURI(new URI(url));
			
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
			    postParameters);
			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
			    .getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line).append(NL);
			}
			in.close();
			
			return sb.toString();
			//  return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String executeHttpDelete(String url, String postParameters) throws Exception {
		BufferedReader in = null;
		try {
			URL url1 = new URL(url + "?" + postParameters);
			HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("DELETE");
			connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setUseCaches (false);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line, responseText = "";
			while ((line = br.readLine()) != null) {
				System.out.println("LINE: "+line);
				responseText += line;
			}
			br.close();
			connection.disconnect();
			
			return responseText.toString();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static boolean isNetworkAvailable(Context context) {
		
		ConnectivityManager connectivity = null;
		boolean isNetworkAvail = false;
		
		try {
			connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				
				if (info != null) {
					for (NetworkInfo anInfo : info) {
						if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
							
							return true;
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connectivity != null) {
				connectivity = null;
			}
		}
		return false;
	}
	
	public static boolean isValidEmail1(CharSequence target) {
		return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
	
	private static ProgressDialog dialog;
	
	public static void showDailog(Context c, String msg) {
		dialog = new ProgressDialog(c);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setMessage(msg);
		dialog.show();
	}
	/*public static void showDailog(Context c, String msg, Integer[] value) {
		dialog = new ProgressDialog(c);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage(msg);
		dialog.setProgress(value[0].intValue());
		dialog.show();
	}*/
	
	public static void closeDialog() {
		if (dialog != null)
			dialog.cancel();
	}
	

	

	public static String getUniqueImageName(){
		//will generate a random num
		//between 15-10000
		Random r = new Random();
		int num = r.nextInt(10000 - 15) + 15;
		String fileName = "img_"+num+".png";
		return  fileName;
	}
	
	public static int round(Double total, int number) {
		if (number < 0) throw new IllegalArgumentException();
		
		long factor = (long) Math.pow(10, number);
		total = total * factor;
		long tmp = Math.round(total);
		return (int)(tmp / factor);
	}
	
	
}
