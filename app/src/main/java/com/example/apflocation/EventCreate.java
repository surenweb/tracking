package com.example.apflocation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fastLibrary.AdapterGrid;
import fastLibrary.AdapterSpinner;
import fastLibrary.ClassEvent;
import fastLibrary.ClassMetaData;
import fastLibrary.FastApp;
import fastLibrary.FastConfig;
import fastLibrary.FastGpsListener;
import fastLibrary.FastKeyValue;
import fastLibrary.FastLocation;
import fastLibrary.ClassImage;

public class EventCreate extends Activity  {

	// Activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;


	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "apf_report";

	private Uri fileUri; // file url to store image/video

	private Button btnTakePhoto;
	private ArrayList<ClassImage> imageItems ;
	private GridView gridView;
	private AdapterGrid gridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);


		final TextView txtEventLat = (TextView)findViewById(R.id.txtEventLat );
		final TextView txtEventLon = (TextView)findViewById(R.id.txtEventLon );

		txtEventLat.setText(FastLocation.Lat);
		txtEventLon.setText(FastLocation.Lon);

		//## Camara Image Grid
		imageItems = new ArrayList<ClassImage>();
		gridView = (GridView) findViewById(R.id.eventImageGrid);
		gridAdapter = new AdapterGrid(this, R.layout.layout_grid_item,imageItems );
		gridView.setAdapter(gridAdapter);


		//## Event Type Spinner
		List<FastKeyValue> lstEvent = new ClassMetaData().GetAllByTag("EventType");

		AdapterSpinner adapterEvent = new AdapterSpinner(FastApp.getContext(),
				R.layout.layout_spinner_item,
				lstEvent);
		adapterEvent.setDropDownViewResource(R.layout.layout_spinner_dropdown_item);

		Spinner spinEvent = (Spinner) findViewById(R.id.spinEvent);
		spinEvent.setAdapter(adapterEvent); // Set the custom adapter to the spinner


		/**
		 * Capture image button click event
		 */
		btnTakePhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// capture picture
				captureImage();
			}
		});


		// Checking camera availability
		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			finish();
		}

		Button btnEventAdd = (Button) findViewById(R.id.btnEventAdd);
		btnEventAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Spinner spinEvent = (Spinner)findViewById(R.id.spinEvent );
				EditText txtEventDetail = (EditText)findViewById(R.id.txtEventDetail) ;
				TextView txtEventLat = (TextView)findViewById(R.id.txtEventLat );
				TextView txtEventLon = (TextView)findViewById(R.id.txtEventLon );

				//Create New Patrol
				ClassEvent event = new ClassEvent();

				event.Event = ((FastKeyValue)spinEvent.getSelectedItem()).key;
				event.EventDetail = txtEventDetail.getText().toString();
				event.Lat = txtEventLat.getText().toString();
				event.Lon = txtEventLon.getText().toString();
				event.Accuracy = FastLocation.Accuracy ;

				event.CreatedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

				if(!event.Update())
				{
					Toast.makeText(FastApp.getContext(), "Event Not Created", Toast.LENGTH_LONG).show();
					return;  // Return
				}

				Integer last_id = event.TopInsertID();
				if(last_id < 0 )
				{
					Toast.makeText(FastApp.getContext(), "Event created without images : image failed to load ", Toast.LENGTH_LONG).show();
					finish();
				}

				//## ADD Images Data
				Integer totInsertedImage= 0 ;
				for ( ClassImage image :imageItems ) {
					image.EventID = last_id ;
					if(!image.Update())
						Log.d(FastConfig.appLogTag,"Can't add image : "+image.Title );
					else
						totInsertedImage= totInsertedImage+1;
				}

				Toast.makeText(FastApp.getContext(), "Event created with " + totInsertedImage+" images", Toast.LENGTH_LONG).show();
				finish();
			}
		});

		//## Location Changed Call Backs -Update UI
		FastApp.getLocation().setOnEventListener(new FastGpsListener() {
			public void onLocationChangedEvent() {

				txtEventLat.setText(FastLocation.Lat);
				txtEventLon.setText(FastLocation.Lon);
			}

			@Override
			public void onTrackAddedEvent() {

			}

		});

	}

	private void addImagesToGrid(ClassImage imageItem) {
		imageItems.add(imageItem);
		gridAdapter.notifyDataSetChanged();
	}



	/**
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }


        // startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}


	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode != CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			// failed to record video
			Toast.makeText(getApplicationContext(),
					"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
					.show();
			return ;
		}

		if (resultCode != RESULT_OK ) {
			Toast.makeText(getApplicationContext(),"User cancelled image capture", Toast.LENGTH_SHORT).show();
			return;

		}
		// successfully captured the image
		// display it in image view
		try {
			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 4;

			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
					options);

			ClassImage im = new ClassImage(bitmap,"IMG_"+(imageItems.size()+1),fileUri.getPath() );
			addImagesToGrid(im);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}


	/**
	 * ------------ Helper Methods ----------------------
	 * */

	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {

		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * returning image / video
	 */
	private File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}


	/**
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}


}
