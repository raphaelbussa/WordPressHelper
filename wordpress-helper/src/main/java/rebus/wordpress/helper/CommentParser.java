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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class CommentParser extends AsyncTask {

    private String FEED_URL;
    private ProgressDialog progressDialog;
    private String progressDialogTitle;
    private String progressDialogMessage;
    private Context context;
    private OnComplete onComplete;
    private Boolean showDialog = false;
    private ArrayList<CommentItem> commentItems;

    public CommentParser(String FEED_URL) {
        this.FEED_URL = FEED_URL;
    }

    public void showDialog(Boolean showDialog, String progressDialogTitle, String progressDialogMessage, Context context) {
        this.showDialog = showDialog;
        this.progressDialogTitle = progressDialogTitle;
        this.progressDialogMessage = progressDialogMessage;
        this.context = context;
    }

    public void onPauseDialog() {
        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
    }

    public interface OnComplete {
        public void onFinish(ArrayList<CommentItem> commentItems);
        public void onError();
    }

    public void onFinish (OnComplete onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Document document = Jsoup.connect(FEED_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22")
                    .timeout(60000).ignoreContentType(true).get();
            Elements elements = document.getElementsByTag("item");
            for (Element element : elements) {
                CommentItem commentItem = new CommentItem();
                commentItem.setCreator(element.getElementsByTag("dc:creator").first().text());
                commentItem.setPubDate(element.getElementsByTag("pubDate").first().text());
                commentItem.setContent(element.getElementsByTag("content:encoded").first().text());
                commentItem.setLink(element.select("link").first().nextSibling().toString().trim());
                commentItem.setGuid(element.getElementsByTag("guid").first().text());
                String idPost[] = element.getElementsByTag("guid").first().text().split("#comment-");
                if (idPost.length  > 1) {
                    commentItem.setId(idPost[1]);
                    //add feeditem to arraylist
                    commentItems.add(commentItem);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showDialog) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle(progressDialogTitle);
            progressDialog.setMessage(progressDialogMessage);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        commentItems = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (showDialog) {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        if (commentItems.size() == 0) {
            onComplete.onError();
        } else {
            onComplete.onFinish(commentItems);
        }
    }
}
