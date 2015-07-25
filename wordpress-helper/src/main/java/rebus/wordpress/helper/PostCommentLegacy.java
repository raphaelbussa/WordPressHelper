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

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class PostCommentLegacy extends AsyncTask {

    private String URL_WORDPRESS;
    private String id;
    private String name;
    private String email;
    private String url;
    private String comment;
    private String commentParent;

    private ProgressDialog progressDialog;
    private String progressDialogTitle;
    private String progressDialogMessage;
    private Context context;
    private OnComplete onComplete;
    private Boolean showDialog = false;
    private boolean error;

    public PostCommentLegacy(String id, String URL_WORDPRESS, String name, String email, String url, String comment, String commentParent) {
        this.id = id;
        this.URL_WORDPRESS = URL_WORDPRESS;
        this.name = name;
        this.email = email;
        this.url = url;
        this.comment = comment;
        this.commentParent = commentParent;
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
        public void onFinish();
        public void onError();
    }

    public void onFinish (OnComplete onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Jsoup.connect(URL_WORDPRESS + "/wp-comments-post.php")
                    .data("author", name)
                    .data("email", email)
                    .data("url", url)
                    .data("comment", comment)
                    .data("comment_post_ID", id)
                    .data("comment_parent", commentParent)
                    .post();
        } catch (IOException e) {
            e.printStackTrace();
            error = true;
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
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (showDialog) {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        if (error) {
            onComplete.onError();
        } else {
            onComplete.onFinish();
        }
    }
}
