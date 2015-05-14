package com.air.ball;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.*;
import android.widget.AdapterView.OnItemLongClickListener;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;


import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.sql.SQLClientInfoException;
import java.util.*;


/**
 * Created by KL on 2015/5/10.
 */
public class FriendFrag extends Fragment {
    private ListView list;
    private View view;
    private static final int REFRESH_COMPLETE = 0X110;
    private Handler mHandler = null;
    private SwipeRefreshLayout mSwipeLayout = null;
    private ArticleAdapter adapter = null;
    List<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.content, container, false);
        list = (ListView)view.findViewById(R.id.lists);
        list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub				
	            SimpleDraweeView img = (SimpleDraweeView)view.findViewById(R.id.item_img);
	            View root = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
	            SimpleDraweeView img2 = (SimpleDraweeView)root.findViewById(R.id.img);
	            img2.setImageBitmap(img.getDrawingCache());	           
	            final PopupWindow  dialog = new PopupWindow(100,100);
	           dialog.showAtLocation(view, Gravity.CENTER, 10, 10);
	           
	            root.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						return false;
					}					
			
				});
				return false;
			}
        	
		});
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);
        adapter = new ArticleAdapter(getActivity(),lists);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lists.removeAll(lists);
                mSwipeLayout.setRefreshing(false);
                FetchDataTask task = new FetchDataTask();
                int index = new Random().nextInt(150);
                task.execute(String.format("http://www.qiushibaike.com/hot/page/%d",index),"refresh");

            }
        });
        list.setAdapter(adapter);
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
//                        lists.addAll(Arrays.asList("Lucene", "Canvas", "Bitmap"));
                        adapter.notifyDataSetChanged();
                        break;

                }
            }
        };
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        FetchDataTask task = new FetchDataTask();
        task.execute("http://www.qiushibaike.com/","init");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class FetchDataTask extends AsyncTask<String,Integer,String[]>{

        @Override
        protected void onPostExecute(String  ... s) {
            try {
                Document document = Jsoup.parse(s[0]);
                Elements content = document.select(".article");
                String  s1  = "";
                lists.removeAll(lists);
                for (Element e : content) {
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    s1 = "        ";
                    s1 += e.select(".content").get(0).text();
                    s1 += "";
                    data.put("content",s1);
                    if(s1.trim().isEmpty())
                    	continue;
                    try {
                        Element imgs = e.select("div.thumb > a > img ").get(0).select("img[src]").get(0);
                        data.put("img",imgs.attr("src"));
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                    lists.add(data);                    
                }
                //if(s[1].equals("refresh"))
                mHandler.sendEmptyMessage(REFRESH_COMPLETE);               
            }
            catch (Exception e){
                e.printStackTrace();
            }


        }

        @Override
        protected String[]  doInBackground(String... params) {
            URL pm25 = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                pm25 = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                // Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.1.189", 8080));
                URLConnection conn = pm25.openConnection();
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36");
                conn.setRequestProperty("Host", "www.qiushibaike.com");
                conn.setRequestProperty("Accept-Encoding", "identity");
                //conn.setRequestProperty("http.keepAlive", "false");
               conn.connect();
                byte[] buffer = new byte[2048];
                InputStream in = conn.getInputStream();
                while(in.read(buffer) != -1){
                    baos.write(buffer);
                }

            } catch (IOException e) {
                e.printStackTrace();

            }catch (Exception e){
                e.printStackTrace();
            }
            String[]  results={ new String(baos.toByteArray()),params[1]};
            return results;
        }
    }

   
    public class ArticleAdapter extends BaseAdapter{
        private Context mContext;
        private LayoutInflater inflater;
        private List<HashMap<String,Object> > list;

        public ArticleAdapter(Context mContext,List<HashMap<String,Object> > list){
            this.mContext = mContext;
            this.list = list;
            this.inflater = LayoutInflater.from(mContext);
        }
        @Override
        public int getCount() {
            return list.isEmpty()? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
        	if(!list.isEmpty() && position < list.size()){
        		return list.get(position);
        	}else{
        		return null;
        	}
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {        	
        	HashMap<String, Object> item = (HashMap<String, Object>) getItem(position);
            View view = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);
            TextView txt = (TextView)view.findViewById(R.id.item_info);
            SimpleDraweeView img = (SimpleDraweeView)view.findViewById(R.id.item_img);           
            try{
            	img.setImageURI(Uri.parse((String) item.get("img")));
            	txt.setText((CharSequence)item.get("content"));
            }catch(Exception e){
            	img.setVisibility(View.GONE);
            	e.printStackTrace();
            }            
            return  view;
        }
        
        
    }

}
