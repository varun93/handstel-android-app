package org.i2india.swaraksha;

import android.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {


	public CustomCursorAdapter(Context context, Cursor c, boolean autoRequery) {

		super(context, c, autoRequery);

		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.list_item, parent, false);

		return retView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// here we are setting our data
		// that means, take the data from the cursor and put it in views

		TextView device_name = (TextView) view.findViewById(R.id.title);
		device_name.setText(cursor.getString(cursor.getColumnIndex("deviceName")));

		ImageView im = (ImageView) view.findViewById(R.id.imageview);
		im.setImageBitmap(getRoundedShape(decodeFile(context,R.drawable.user),200));


		ImageView checked = (ImageView) view.findViewById(R.id.checked);


	}

	private  Bitmap decodeFile(Context context,int resId) {
		Context mcontext;
		try {
			// decode image size
			mcontext=context;
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(mcontext.getResources(), resId, o);
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 200;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeResource(mcontext.getResources(), resId, o2);
		} catch (Exception e) {
		}
		return null;
	}


	private  Bitmap getRoundedShape(Bitmap scaleBitmapImage,int width) {
		// TODO Auto-generated method stub
		int targetWidth = width;
		int targetHeight = width;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
				targetHeight,Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth),
						((float) targetHeight)) / 2),
						Path.Direction.CCW);
		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap,
				new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight()),
						new Rect(0, 0, targetWidth,
								targetHeight), null);
		return targetBitmap;
	}
}
