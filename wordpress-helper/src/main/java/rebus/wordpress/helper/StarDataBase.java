/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Raphael Bussa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package rebus.wordpress.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class StarDataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "wordpresshelper";

    private static final String TABLE_STAR = "star";

    private static final String KEY_TITLE = "title";
    private static final String KEY_PUBDATE = "pubDate";
    private static final String KEY_CREATOR = "creator";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_COMMENTRSS = "commentRss";
    private static final String KEY_COMMENTS = "comments";
    private static final String KEY_LINK = "link";
    private static final String KEY_GUID = "guid";
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";

    public StarDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STAR_TABLE = "CREATE TABLE " + TABLE_STAR + "(" +
                KEY_TITLE + " TEXT," +
                KEY_PUBDATE + " TEXT," +
                KEY_CREATOR + " TEXT," +
                KEY_CATEGORY + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_CONTENT + " TEXT," +
                KEY_COMMENTRSS + " TEXT," +
                KEY_COMMENTS + " TEXT," +
                KEY_LINK + " TEXT," +
                KEY_GUID + " TEXT," +
                KEY_ID + " TEXT," +
                KEY_IMAGE + " TEXT)";
        db.execSQL(CREATE_STAR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAR);
        onCreate(db);
    }

    public void addStar(FeedItem feedItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, feedItem.getTitle());
        values.put(KEY_PUBDATE, feedItem.getPubDate());
        values.put(KEY_CREATOR, feedItem.getCreator());
        values.put(KEY_CATEGORY, setCategoty(feedItem.getCategory()));
        values.put(KEY_DESCRIPTION, feedItem.getDescription());
        values.put(KEY_CONTENT, feedItem.getContent());
        values.put(KEY_COMMENTRSS, feedItem.getCommentRss());
        values.put(KEY_COMMENTS, feedItem.getComments());
        values.put(KEY_LINK, feedItem.getLink());
        values.put(KEY_GUID, feedItem.getGuid());
        values.put(KEY_ID, feedItem.getId());
        values.put(KEY_IMAGE, feedItem.getImage());
        db.insert(TABLE_STAR, null, values);
        db.close();
    }

    public ArrayList<FeedItem> getStar() {
        ArrayList<FeedItem> feedItems = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_STAR;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FeedItem feedItem = new FeedItem();
                feedItem.setTitle(cursor.getString(0));
                feedItem.setPubDate(cursor.getString(1));
                feedItem.setCreator(cursor.getString(2));
                feedItem.setCategory(getCategory(cursor.getString(3)));
                feedItem.setDescription(cursor.getString(4));
                feedItem.setContent(cursor.getString(5));
                feedItem.setCommentRss(cursor.getString(6));
                feedItem.setComments(cursor.getString(7));
                feedItem.setLink(cursor.getString(8));
                feedItem.setGuid(cursor.getString(9));
                feedItem.setId(cursor.getString(10));
                feedItem.setImage(cursor.getString(11));
                feedItems.add(feedItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return feedItems;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STAR, null, null);
        db.close();
    }

    public void deleteStar(FeedItem feedItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STAR, KEY_ID + " = ?", new String[] { String.valueOf(feedItem.getId()) });
        db.close();
    }

    public int getStarCount() {
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_STAR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public boolean isStar (FeedItem feedItem) {
        boolean isPresent;
        String isStarQuery = "SELECT  * FROM " + TABLE_STAR + " WHERE " + KEY_ID + " = " + feedItem.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(isStarQuery, null);
        if(cursor.getCount() <= 0){
            isPresent = false;
        } else {
            isPresent = true;
        }
        cursor.close();
        db.close();
        return isPresent;
    }

    private String setCategoty(ArrayList<String> category) {
        String cat = "";
        for (int i = 0; i < category.size(); i++) {
            cat += category.get(i) + ",";
        }
        return cat;
    }

    private ArrayList<String> getCategory(String cat) {
        ArrayList<String> category = new ArrayList<>();
        String catPars[] = cat.split(",");
        for (int i = 0; i < catPars.length; i++) {
            category.add(catPars[i]);
        }
        return category;
    }

}
