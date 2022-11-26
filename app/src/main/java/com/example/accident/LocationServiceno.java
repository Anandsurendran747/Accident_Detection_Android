package com.example.accident;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.core.app.NotificationCompat;

public class LocationServiceno extends Service {
	 private LocationManager locationManager;
	    private Boolean locationChanged;
	    
	    private Handler handler = new Handler();
	    public static Location curLocation;
	    public static boolean isService = true;
	    private File root;
	    private ArrayList<String> fileList = new ArrayList<String>();
	    
	    public static String lati="",logi="",place="";
	    String ip="";
	    String[] zone;
	    String pc="",url="";
	    SQLiteDatabase db;
	    String datemsg = "";
	    String imei="";
	    String encodedImage = null;
	    ArrayList<String> name,date,Time;


	TelephonyManager telemanager;
	    SharedPreferences sh;
	public static final String SHARED_PREFS = "shared_prefs";
	int NOTIFICATION_ID = 234;
	NotificationManager notificationManager;
	


	    
LocationListener locationListener = new LocationListener() {
	    		
	        @SuppressLint("MissingPermission")

			public void onLocationChanged(Location location) {
	            if (curLocation == null) {
	                curLocation = location;
	                locationChanged = true;
	            }
	            else if (curLocation.getLatitude() == location.getLatitude() && curLocation.getLongitude() == location.getLongitude()){
	                locationChanged = false;
	                return;
	            }
	            else
	                locationChanged = true;
	                curLocation = location;

	            if (locationChanged)
	                locationManager.removeUpdates(locationListener);
	        }
	        public void onProviderDisabled(String provider) {
	        }

	        public void onProviderEnabled(String provider) {
	        }
	                
			@Override
			public void onStatusChanged(String provider, int status,Bundle extras) {
				// TODO Auto-generated method stub
				  if (status == 0)// UnAvailable
		            {
		            } else if (status == 1)// Trying to Connect
		            {
		            } else if (status == 2) {// Available
		            }
			}		
	    };
	

	@Override
	public void onCreate() {
		   super.onCreate();
		notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		curLocation = getBestLocation();
	      
	        if (curLocation == null){
	        	System.out.println("starting problem.........3...");
	        	Toast.makeText(this, "GPS problem..........", Toast.LENGTH_SHORT).show();
	       }
	        else{
	         	// Log.d("ssssssssssss", String.valueOf("latitude2.........."+curLocation.getLatitude()));        	 
	        }
	        isService =  true;
	    }    
	    final String TAG="LocationService";    
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	    	return super.onStartCommand(intent, flags, startId);
	   }
	   @Override
	   
	   public void onLowMemory() {
	       super.onLowMemory();

	   }
	@Override
	public void onStart(Intent intent, int startId) {
		//  Toast.makeText(this, "Start services", Toast.LENGTH_SHORT).show();
		  telemanager  = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

	        sh=getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

		  String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		  if(!provider.contains("gps"))
	        { //if gps is disabled
	        	final Intent poke = new Intent();
	        	poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        	poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        	poke.setData(Uri.parse("3")); 
	        	sendBroadcast(poke);
	        }	  
		  
//		  SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//	      URL=preferences.getString("url", "");
//	      
	      handler.postDelayed(GpsFinder,10000);
	}
	
	@Override
	public void onDestroy() {
		 String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		   if(provider.contains("gps"))
		   { //if gps is enabled
		   final Intent poke = new Intent();
		   poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		   poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		   poke.setData(Uri.parse("3")); 
		   sendBroadcast(poke);
		   }
		   
		   handler.removeCallbacks(GpsFinder);
	       handler = null;
	       Toast.makeText(this, "Service Stopped..!!", Toast.LENGTH_SHORT).show();
	       isService = false;
	   }

	  
	  public Runnable GpsFinder = new Runnable(){
		  
		 
	    public void run(){
	    	

	    	
	    	String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	  	  if(!provider.contains("gps"))
	          { //if gps is disabled
	          	final Intent poke = new Intent();
	          	poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	          	poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	          	poke.setData(Uri.parse("3")); 
	          	sendBroadcast(poke);
	          }	  
	  	  
	  	 
	    
	  Location tempLoc = getBestLocation();
	    	
	        if(tempLoc!=null)
	        {        	
	        	
	    		//Toast.makeText(getApplicationContext(), phoneid, Toast.LENGTH_LONG).show();
	    
	        	curLocation = tempLoc;            
	           // Log.d("MyService", String.valueOf("latitude"+curLocation.getLatitude()));            
	            
	            lati=String.valueOf(curLocation.getLatitude());
	            logi=String.valueOf(curLocation.getLongitude());    
	            getnotify();
	           

//	            
//	            db=new completedboperation(getApplicationContext());
//	            db.location(lati, logi);
	            
	            
	            
	            
	           // Toast.makeText(getApplicationContext(),URL+" received", Toast.LENGTH_SHORT).show();
	            Toast.makeText(getApplicationContext(),"\nlat.. and longi.."+ lati+"..."+logi, Toast.LENGTH_SHORT).show();
	    	  		
      
		    	        
	   	String loc="";
	    	String address = "";
	        Geocoder geoCoder = new Geocoder( getBaseContext(), Locale.getDefault());      
	          try
	          {    	
	            List<Address> addresses = geoCoder.getFromLocation(curLocation.getLatitude(), curLocation.getLongitude(), 1);
	            if (addresses.size() > 0)
	            {        	  
	            	for (int index = 0;index < addresses.get(0).getMaxAddressLineIndex(); index++)
	            		address += addresses.get(0).getAddressLine(index) + " ";
	            	//Log.d("get loc...", address);
	            	
	            	 place=addresses.get(0).getFeatureName().toString();
	            	 
	            	
	            //	 loc= addresses.get(0).getLocality().toString();
	            //	Toast.makeText(getBaseContext(),address , Toast.LENGTH_SHORT).show();
	            //	Toast.makeText(getBaseContext(),ff , Toast.LENGTH_SHORT).show();
	            }
	            else
	            {
	          	  //Toast.makeText(getBaseContext(), "noooooooo", Toast.LENGTH_SHORT).show();
	            }      	
	          }
	          catch (IOException e) 
	          {        
	            e.printStackTrace();
	          }
	          
	    Toast.makeText(getBaseContext(), "locality-"+place, Toast.LENGTH_SHORT).show();
	     

     }
      handler.postDelayed(GpsFinder,55000);// register again to start after 20 seconds...        
	    }


	  };












	private  void getnotify() {
//		RequestQueue queue1 = Volley.newRequestQueue(LocationServiceno.this);
//		String url1 = "http://" + sh.getString("ip", "") + ":5000/service";
//
//		// Request a string response from the provided URL.
//		StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				// Display the response string.
//				Log.d("+++++++++++++++++", response);
//				try {
//					JSONObject json = new JSONObject(response);
//					String res = json.getString("task1");
//
//
////                            pDialog.hide();
//					if (res.equalsIgnoreCase("yes")) {
//						notificationCheck();
//
//
//						Toast.makeText(getApplicationContext(), "Carefull some distruptions are there", Toast.LENGTH_LONG).show();
//
//
//					} else {
////						Toast.makeText(getApplicationContext(), "Route is clear", Toast.LENGTH_LONG).show();
//
//
//					}
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//
//
//			}
//		}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//
////                        pDialog.hide();
//				Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
//			}
//		}) {
//			@Override
//			protected Map<String, String> getParams() {
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("lati", ""+LocationService.lati);
//				params.put("longi", ""+LocationService.logi);
//
//				return params;
//			}
//		};
//		// Add the request to the RequestQueue.
//		queue1.add(stringRequest1);








//		RequestQueue queue3 = Volley.newRequestQueue(LocationServiceno.this);
//		String url3 = "http://" + sh.getString("ip", "") + ":5000/checkservice";
//
//		// Request a string response from the provided URL.
//		StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				// Display the response string.
//				Log.d("+++++++++++++++++", response);
//				try {
//					JSONObject json = new JSONObject(response);
//					String res = json.getString("task");
//
//
////                            pDialog.hide();
//					if (res.equalsIgnoreCase("yes")) {
//						notificationChecknew();
//
//
//						Toast.makeText(getApplicationContext(), "Be carefull You are in danger", Toast.LENGTH_LONG).show();
//
//
//					} else {
////						Toast.makeText(getApplicationContext(), "Route is clear", Toast.LENGTH_LONG).show();
//
//
//					}
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//
//
//			}
//		}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//
////                        pDialog.hide();
//				Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
//			}
//		}) {
//			@Override
//			protected Map<String, String> getParams() {
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("lati", ""+LocationService.lati);
//				params.put("longi", ""+LocationService.logi);
//
//				return params;
//			}
//		};
//		// Add the request to the RequestQueue.
//		queue3.add(stringRequest3);
//




	}

	@SuppressLint("MissingPermission")
		private Location getBestLocation() {
	        Location gpslocation = null;
	        Location networkLocation = null;
	        if(locationManager==null){
	          locationManager = (LocationManager) getApplicationContext() .getSystemService(Context.LOCATION_SERVICE);
	        }
	        try 
	        {
	            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
	            {            	 
	            	 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000, 0, locationListener);// here you can set the 2nd argument time interval also that after how much time it will get the gps location
	                gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	             //  System.out.println("starting problem.......7.11....");
	              
	            }
	            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
	                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000, 0, locationListener);
	                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 
	            }
	        } catch (IllegalArgumentException e) {
	            Log.e("error", e.toString());
	        }
	        if(gpslocation==null && networkLocation==null)
	            return null;

	        if(gpslocation!=null && networkLocation!=null){
	            if(gpslocation.getTime() < networkLocation.getTime()){
	                gpslocation = null;
	                return networkLocation;
	            }else{
	                networkLocation = null;
	                return gpslocation;
	            }
	        }
	        if (gpslocation == null) {
	            return networkLocation;
	        }
	        if (networkLocation == null) {
	            return gpslocation;
	        }
	        return null;
	    }
		
	  	
	  	
	  	
		
		
		


	  	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void notificationCheck() {
		// TODO Auto-generated method stub
		try {


//				String msg="Have "+medname+" \n on   "+time+" ";
			String msg="Accident ocuured Emergency! ";


				notification_popup(msg);
			}


		catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}


	private void notificationChecknew() {
		// TODO Auto-generated method stub
		try {


//				String msg="Have "+medname+" \n on   "+time+" ";
			String msg="Be carefull You are in danger";


			notification_popup(msg);
		}


		catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}
	public void notification_popup(String msg) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			String CHANNEL_ID = "my_channel_01";
			CharSequence name = "my_channel";
			String Description = "This is my channel";
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
			mChannel.setDescription(Description);
			mChannel.enableLights(true);
			mChannel.setLightColor(Color.RED);
			mChannel.enableVibration(true);
			mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//       mChannel.setVibrationPattern(new long[]{0, 800, 200, 1200, 300, 2000, 400, 4000});
			mChannel.setShowBadge(false);
			notificationManager.createNotificationChannel(mChannel);
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "my_channel_01")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle("Notification")
				.setContentText(msg);
//    Intent resultIntent = new Intent(getApplicationContext(), Details.class);
//    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//    stackBuilder.addParentStack(MainActivity.class);
//    stackBuilder.addNextIntent(resultIntent);
//    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//    builder.setContentIntent(resultPendingIntent);
		notificationManager.notify(NOTIFICATION_ID, builder.build());
	}


}
