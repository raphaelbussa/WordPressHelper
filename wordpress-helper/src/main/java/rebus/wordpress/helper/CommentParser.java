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

/**
 * Created by Raphael on 11/02/2015.
 */
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
