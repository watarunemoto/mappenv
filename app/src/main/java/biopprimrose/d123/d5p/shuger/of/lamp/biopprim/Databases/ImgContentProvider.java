package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Databases;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ImgContentProvider extends ContentProvider {

	public static final String AUTHORITY =
			"ImgContentProvider";
	public static final Uri CONTENT_URI =
			Uri.parse("content://" + AUTHORITY + "/" + ImgContract.Images.TABLE_NAME);

	//UriMacher
	private static final int IMGS = 1;
	private static final int IMG_ITEM = 2;
	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, ImgContract.Images.TABLE_NAME, IMGS);
		uriMatcher.addURI(AUTHORITY, ImgContract.Images.TABLE_NAME + "/#", IMG_ITEM);
	}

	private ImgOpenHelper imgOpenHelper;


	public ImgContentProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (uriMatcher.match(uri) != IMG_ITEM) {
			throw new IllegalArgumentException("Invalid URI: " + uri);
		}
		SQLiteDatabase db = imgOpenHelper.getWritableDatabase();
		int deletedCount = db.delete(
				ImgContract.Images.TABLE_NAME,
				selection,
				selectionArgs
		);
		getContext().getContentResolver().notifyChange(uri, null);
		return deletedCount;
	}

	@Override
	public String getType(Uri uri) {
		// TODO: Implement this to handle requests for the MIME type of the data
		// at the given URI.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (uriMatcher.match(uri) != IMGS) {
			throw new IllegalArgumentException("Invalid URI: " + uri);
		}
		SQLiteDatabase db = imgOpenHelper.getWritableDatabase();
		long newId = db.insert(
				ImgContract.Images.TABLE_NAME,
				null,
				values
		);
		Uri newUri = ContentUris.withAppendedId(
				ImgContentProvider.CONTENT_URI,
				newId
		);
		getContext().getContentResolver().notifyChange(newUri, null);
		return newUri;
	}

	@Override
	public boolean onCreate() {
		imgOpenHelper = new ImgOpenHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(
			Uri uri,
			String[] projection,
			String selection,
			String[] selectionArgs,
			String sortOrder
	) {
		switch (uriMatcher.match(uri)) {
			case IMGS:
			case IMG_ITEM:
				break;
			default:
				throw new IllegalArgumentException("Invalid URI: " + uri);
		}
		SQLiteDatabase db = imgOpenHelper.getReadableDatabase();
		Cursor c = db.query(
				ImgContract.Images.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				sortOrder
		);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
					  String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
