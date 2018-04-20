package com.example.knutt.myproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class FriendTimelineActivity extends AppCompatActivity {

    private TextView textname;
    private ArrayList<String> addMessage = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private Database db;
    private Database2 db2;
    private Database3 db3;
    private DatabaseRealtime databaseRealtime;
    private DatabaseCheckin databaseCheckin;
    private ArrayList<String> allPostsStory = new ArrayList<String>();
    private ArrayList<String> allPostsMessages = new ArrayList<String>();
    private ArrayList<String> allPostsMessagesPos = new ArrayList<String>();
    private ArrayList<String> allPostsMessagesNege = new ArrayList<String>();
    private ArrayList<String> allPostsMessagesNeural = new ArrayList<String>();
    private ArrayList<String> allPostsMessagesNot = new ArrayList<String>();

    ArrayList<String> strword = new ArrayList<>() ;
    HashSet<String> emo = new HashSet<>();
    private ArrayList<String> listemo = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_timeline);

        db = new Database(getApplicationContext());
        db2 = new Database2(getApplicationContext());
        db3 = new Database3(getApplicationContext());
        databaseRealtime = new DatabaseRealtime(getApplicationContext());
        databaseCheckin = new DatabaseCheckin(getApplicationContext());

        //time current
        Calendar check = Calendar.getInstance();
        check.add(Calendar.DATE, 0);
        SimpleDateFormat realdatecurrent = new SimpleDateFormat("yyyy-MM-dd");
        String datecurrent = realdatecurrent.format(check.getTime());
        //

        final ArrayList<HashMap<String, String>> attitude = db.getAttitudeList();
        final ArrayList<HashMap<String, String>> emotion = db.getEmotionList();
        final ArrayList<HashMap<String, String>> emoshortcut = db.getEmoticon();
        final ArrayList<HashMap<String, String>> attitude2 = db2.getAttitudeList2();
        final ArrayList<HashMap<String, String>> attitude3 = db3.getAttitudeList3();
        final ArrayList<HashMap<String,String>> storyTimeline = databaseCheckin.getCheckinWord();

        final String pattern2 = "([a-zA-Z0-9!@#$&()-`|.+',/\"]{2})";
        final String pattern3 = "([a-zA-Z0-9!@#$&()-`|.+',/\"]{3})";
        final String pattern4 = "([a-zA-Z0-9!@#$&()-`|.+',/\"]{4})";
        final String pattern5 = "([0-8]{1})";

        final Pattern regex2 = Pattern.compile(pattern2);
        final Pattern regex3 = Pattern.compile(pattern3);
        final Pattern regex4 = Pattern.compile(pattern4);


        SimpleDateFormat df;
        SimpleDateFormat df2;
        SimpleDateFormat df3;
        SimpleDateFormat df4;
        SimpleDateFormat df5;
        SimpleDateFormat df6;
        SimpleDateFormat df7;
        SimpleDateFormat df8;

        final String formattedDate;
        final String formattedDate2;
        final String formattedDate3;
        final String formattedDate4;
        final String formattedDate5;
        final String formattedDate6;
        final String formattedDate7;
        final String formattedDate8;


        boolean Date1;
        boolean Date2;
        boolean Date3;
        boolean Date4;
        boolean Date5;
        boolean Date6;
        boolean Date7;
        boolean Date8;


        final ArrayList<HashMap<String, String>> datetime = databaseRealtime.getDaterealtime();


        //original
        final String datecheck1 = datetime.get(0).get("Date");
        final String datecheck2 = datetime.get(1).get("Date");
        final String datecheck3 = datetime.get(2).get("Date");
        final String datecheck4 = datetime.get(3).get("Date");
        final String datecheck5 = datetime.get(4).get("Date");
        final String datecheck6 = datetime.get(5).get("Date");
        final String datecheck7 = datetime.get(6).get("Date");
        final String datecheck8 = datetime.get(7).get("Date");

        //setting1


        final String pattern = "([0-9-]{10})";

        final Pattern regex = Pattern.compile(pattern);



        textname = (TextView) findViewById(R.id.textnametm);

        listView = (ListView)findViewById(R.id.listviewTM);


        SharedPreferences sp = getSharedPreferences("friendname", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");

        SharedPreferences sp2 = getSharedPreferences("friendname2", Context.MODE_PRIVATE);
        String id = sp2.getString("id", "");

        SharedPreferences sp7 = getSharedPreferences("App save7", Context.MODE_PRIVATE);

        final int checkboxstate7 = sp7.getInt("checked7",0);

        SharedPreferences sp8 = getSharedPreferences("App save8", Context.MODE_PRIVATE);
        final int checkboxstate8 = sp8.getInt("checked8",0);

        SharedPreferences sp9 = getSharedPreferences("App save9", Context.MODE_PRIVATE);
        final int checkboxstate9 = sp9.getInt("checked9",0);

        ImageView profilePic = (ImageView) findViewById(R.id.profilefriend);

        Picasso.get()
                .load("https://graph.facebook.com/v2.2/" + id + "/picture?height=120&type=normal") //extract as User instance method
                .transform(new CropCircleTransformation())
                .resize(120, 120)
                .into(profilePic);


        textname.setText(name);


        if (!(datecurrent.contains(datecheck8))) {


            GraphRequest request = GraphRequest.newGraphPathRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + id + "/feed?limit=500",
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {


                            // JSON GETS THE DATA
                            JSONObject jsonData = response.getJSONObject();

                            try {


                                JSONArray postsData = jsonData.getJSONArray("data");
                                if (postsData != null) {
                                    allPostsMessagesNege.clear();
                                    allPostsMessagesNeural.clear();
                                    allPostsMessagesNot.clear();
                                    allPostsMessagesPos.clear();
                                    allPostsMessages.clear();

                                    for (int i = 0; i < postsData.length(); i++) {
                                        JSONObject story = postsData.getJSONObject(i);
                                        strword.clear();
                                        emo.clear();
                                        listemo.clear();

                                        String KeepStory = "";
                                        String keepCreatetime = "";
                                        String keepMessage = "";


                                        int countchexkemo = 0;
                                        int count = 0;
                                        int checkifemo = 0;
                                        int checkifword = 0;

                                        String CreateTime = "";

                                        if (story.has("created_time")) {

                                            String timeMessage = story.getString("created_time");
                                            //allPostsMessages.add(timeMessage);
                                            Matcher m = regex.matcher(timeMessage);
                                            if (m.find()) {
                                                //allPostsMessages.add(m.group(0));

                                                if (m.group(0).contains(datecheck1)) {
                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");

                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+ "\n";



//                                                        allPostsMessages.add("โพสต์เมื่อ : " + m.group(0) + "\n" + "ข้อความที่โพสต์ : " + postMessage+"\n"+"เรื่องราว : "+MessagePost);

                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }

                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );

                                                } else if (m.group(0).contains(datecheck2)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+ "\n";


                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";





                                                    }
                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );

                                                } else if (m.group(0).contains(datecheck3)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage =  "ข้อความที่โพสต์ : " + postMessage+"\n";
                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }


                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );


                                                } else if (m.group(0).contains(datecheck4)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+"\n";
                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }


                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );


                                                } else if (m.group(0).contains(datecheck5)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+"\n";
                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }



                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );

                                                } else if (m.group(0).contains(datecheck6)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage +"\n";



                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";






                                                    }

                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );



                                                } else if (m.group(0).contains(datecheck7)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }


                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+"\n" ;






                                                    }


                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }

                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";





                                                    }

                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );


                                                }
                                            }
                                            //Toast.makeText(TimelineActivity.this,timeMessage,Toast.LENGTH_SHORT).show();
                                        }





                                    }


                                }



                                ArrayList<String> data1 = new ArrayList<>();
                                ArrayList<String> data2 = new ArrayList<>();
                                ArrayList<String> data3 = new ArrayList<>();
                                ArrayList<String> data4 = new ArrayList<>();

                                data1.clear();
                                data2.clear();
                                data3.clear();
                                data4.clear();





                                if(checkboxstate7 == 1&& checkboxstate8 == 1&& checkboxstate9 == 1){
                                    for(int d1 = 0;d1<allPostsMessagesPos.size();d1++){
                                        data1.add(allPostsMessagesPos.get(d1));
                                    }
                                    for(int d1 = 0;d1<allPostsMessagesNege.size();d1++){
                                        data1.add(allPostsMessagesNege.get(d1));
                                    }
                                    for(int d1 = 0;d1<allPostsMessagesNeural.size();d1++){
                                        data1.add(allPostsMessagesNeural.get(d1));
                                    }


                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, data1);
                                    listView.setAdapter(adapter);

                                }else if(checkboxstate7 == 1 && checkboxstate8 == 1){
                                    for(int d1 = 0;d1<allPostsMessagesPos.size();d1++){
                                        data2.add(allPostsMessagesPos.get(d1));
                                    }
                                    for(int d1 = 0;d1<allPostsMessagesNeural.size();d1++){
                                        data2.add(allPostsMessagesNeural.get(d1));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, data2);
                                    listView.setAdapter(adapter);

                                }else if(checkboxstate7 == 1 && checkboxstate9 == 1){

                                    for(int d1 = 0;d1<allPostsMessagesPos.size();d1++){
                                        data3.add(allPostsMessagesPos.get(d1));
                                    }
                                    for(int d1 = 0;d1<allPostsMessagesNege.size();d1++){
                                        data3.add(allPostsMessagesNege.get(d1));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, data3);
                                    listView.setAdapter(adapter);

                                }else if(checkboxstate8 == 1 && checkboxstate9 == 1){

                                    for(int d1 = 0;d1<allPostsMessagesNeural.size();d1++){
                                        data4.add(allPostsMessagesNeural.get(d1));
                                    }

                                    for(int d1 = 0;d1<allPostsMessagesNege.size();d1++){
                                        data4.add(allPostsMessagesNege.get(d1));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, data4);
                                    listView.setAdapter(adapter);
                                }else if(checkboxstate7 == 1){

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, allPostsMessagesPos);
                                    listView.setAdapter(adapter);

                                }else  if(checkboxstate8 == 1){

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, allPostsMessagesNeural);
                                    listView.setAdapter(adapter);
                                }else if(checkboxstate9 == 1){

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend,allPostsMessagesNege);
                                    listView.setAdapter(adapter);
                                }else {

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, allPostsMessages);
                                    listView.setAdapter(adapter);
                                }
                                for(int k = 0;k<allPostsMessages.size();k++){
                                    Toast.makeText(getApplicationContext(),allPostsMessages.get(k),Toast.LENGTH_SHORT).show();
                                }






                            } catch (Exception e) {
                                Log.d("JSON", "error" + e.toString());
                            }

                        }
                    });


            Bundle parameters = new Bundle();
            parameters.putString("fields", "created_time,message,story");
            request.setParameters(parameters);
            request.executeAsync();


        }else{
            Calendar c = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            Calendar c3 = Calendar.getInstance();
            Calendar c4 = Calendar.getInstance();
            Calendar c5 = Calendar.getInstance();
            Calendar c6 = Calendar.getInstance();
            Calendar c7 = Calendar.getInstance();
            Calendar c8 = Calendar.getInstance();

            c.add(Calendar.DATE, 0);
            c2.add(Calendar.DATE, +1);
            c3.add(Calendar.DATE, +2);
            c4.add(Calendar.DATE, +3);
            c5.add(Calendar.DATE, +4);
            c6.add(Calendar.DATE, +5);
            c7.add(Calendar.DATE, +6);
            c8.add(Calendar.DATE, +7);


            df = new SimpleDateFormat("yyyy-MM-dd");
            df2 = new SimpleDateFormat("yyyy-MM-dd");
            df3 = new SimpleDateFormat("yyyy-MM-dd");
            df4 = new SimpleDateFormat("yyyy-MM-dd");
            df5 = new SimpleDateFormat("yyyy-MM-dd");
            df6 = new SimpleDateFormat("yyyy-MM-dd");
            df7 = new SimpleDateFormat("yyyy-MM-dd");
            df8 = new SimpleDateFormat("yyyy-MM-dd");

            formattedDate = df.format(c.getTime());
            formattedDate2 = df2.format(c2.getTime());
            formattedDate3 = df3.format(c3.getTime());
            formattedDate4 = df4.format(c4.getTime());
            formattedDate5 = df5.format(c5.getTime());
            formattedDate6 = df6.format(c6.getTime());
            formattedDate7 = df7.format(c7.getTime());
            formattedDate8 = df8.format(c8.getTime());

            Date1 = databaseRealtime.updateData("1", formattedDate);
            Date2 = databaseRealtime.updateData("2", formattedDate2);
            Date3 = databaseRealtime.updateData("3", formattedDate3);
            Date4 = databaseRealtime.updateData("4", formattedDate4);
            Date5 = databaseRealtime.updateData("5", formattedDate5);
            Date6 = databaseRealtime.updateData("6", formattedDate6);
            Date7 = databaseRealtime.updateData("7", formattedDate7);
            Date8 = databaseRealtime.updateData("8", formattedDate8);

            final String datecheck11 = datetime.get(0).get("Date");
            final String datecheck22 = datetime.get(1).get("Date");
            final String datecheck33 = datetime.get(2).get("Date");
            final String datecheck44 = datetime.get(3).get("Date");
            final String datecheck55 = datetime.get(4).get("Date");
            final String datecheck66 = datetime.get(5).get("Date");
            final String datecheck77 = datetime.get(6).get("Date");
            final String datecheck88 = datetime.get(7).get("Date");

            GraphRequest request = GraphRequest.newGraphPathRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + id + "/feed?limit=500",
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {

                            // JSON GETS THE DATA
                            JSONObject jsonData = response.getJSONObject();

                            try {

                                JSONArray postsData = jsonData.getJSONArray("data");
                                if (postsData != null) {
                                    ArrayList<HashMap<String,String>> storyTimeline = databaseCheckin.getCheckinWord();

                                    for (int i = 0; i < postsData.length(); i++) {

                                        strword.clear();
                                        emo.clear();
                                        listemo.clear();


                                        String KeepStory = "";
                                        String keepCreatetime = "";
                                        String keepMessage = "";

                                        int countchexkemo = 0;
                                        int count = 0;
                                        int checkifemo = 0;
                                        int checkifword = 0;

                                        JSONObject story = postsData.getJSONObject(i);

//                                    if (story.has("message")) {
//                                        String postMessage = story.getString("message");
//
//                                        allPostsMessages.add(postMessage);
//
//
//                                        //Toast.makeText(TimelineActivity.this,postMessage,Toast.LENGTH_SHORT).show();
//                                    }
                                        if (story.has("created_time")) {
                                            String timeMessage = story.getString("created_time");
                                            //allPostsMessages.add(timeMessage);
                                            Matcher m = regex.matcher(timeMessage);
                                            if (m.find()) {
                                                //allPostsMessages.add(m.group(0));

                                                if (m.group(0).contains(datecheck11)) {
                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+ "\n";


                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }
                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );

                                                } else if (m.group(0).contains(datecheck22)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }


                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+ "\n";

                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }
                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );


                                                } else if (m.group(0).contains(datecheck33)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+ "\n";


                                                    }
                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }
                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );

                                                } else if (m.group(0).contains(datecheck44)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }


                                                        keepMessage =  "ข้อความที่โพสต์ : " + postMessage+ "\n";

                                                    }
                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }
                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );



                                                } else if (m.group(0).contains(datecheck55)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+ "\n";


                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }
                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );


                                                } else if (m.group(0).contains(datecheck66)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }

                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+ "\n";
                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }
                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );


                                                } else if (m.group(0).contains(datecheck77)) {

                                                    if (story.has("message")) {

                                                        String postMessage = story.getString("message");
                                                        Matcher m22 = regex2.matcher(postMessage);
                                                        Matcher m33 = regex3.matcher(postMessage);
                                                        Matcher m44 = regex4.matcher(postMessage);

                                                        while (m22.find()) {
                                                            listemo.add(m22.group(0));
                                                        }

                                                        while (m33.find()) {
                                                            listemo.add(m33.group(0));

                                                        }

                                                        while (m44.find()) {
                                                            listemo.add(m44.group(0));

                                                        }
                                                        //check word
                                                        Locale thaiLocale = new Locale("th");


                                                        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);

                                                        boundary.setText(postMessage);


                                                        int start = boundary.first();
                                                        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {

                                                            strword.add(postMessage.substring(start, end));


                                                        }

                                                        for (String s : listemo) {
                                                            for (int k = 0; k < emoshortcut.size(); k++) {
                                                                if (s.equals(emoshortcut.get(k).get("EmoticonShortcut"))) {
                                                                    String rank = emoshortcut.get(k).get("EmoticonRank");
                                                                    int countrank = Integer.parseInt(rank);
                                                                    countchexkemo = countchexkemo + countrank;
                                                                    checkifemo++;
                                                                }
                                                            }

                                                        }

                                                        for (int h = 0; h < strword.size(); h++) {
                                                            int icount = h+1;
                                                            String str2 = "";
                                                            String str3 = "";

                                                            if(icount <= strword.size()-1){
                                                                str2 = strword.get(icount);
                                                                for(int k = icount;k<=strword.size()-1;k++){
                                                                    if(str2.contains(" ")){
                                                                        str2 = strword.get(k);
                                                                    }else{
                                                                        break;
                                                                    }
                                                                }


                                                            }

                                                            String str = strword.get(h);


                                                            int counttarget = 0;

                                                            if(str.contains("ๆ")){
                                                                for(int a = 0;a<attitude.size();a++){
                                                                    String checkwordbyregular = attitude.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                                for(int a = 0;a<attitude2.size();a++){
                                                                    String checkwordbyregular = attitude2.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            if((counttarget != 1)){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else{
                                                                                str3 = str+"ๆ";
                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                                for(int a = 0;a<attitude3.size();a++){
                                                                    String checkwordbyregular = attitude3.get(a).get("AttitudeWord");
                                                                    Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                                    Matcher mcheck = regexcheck.matcher(str);
                                                                    if(mcheck.find()){
                                                                        str = mcheck.group(0);
                                                                        if(str2.contains("ๆ")) {

                                                                            for (int b = 0; b < attitude.size(); b++) {
                                                                                String checkwordbyregular2 = attitude.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude2.size(); b++) {
                                                                                String checkwordbyregular2 = attitude2.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }
                                                                            for (int b = 0; b < attitude3.size(); b++) {
                                                                                String checkwordbyregular2 = attitude3.get(b).get("AttitudeWord");
                                                                                Pattern regexcheck2 = Pattern.compile(checkwordbyregular2);
                                                                                Matcher mcheck2 = regexcheck2.matcher(str2);
                                                                                if (mcheck2.find()) {
                                                                                    counttarget++;
                                                                                }
                                                                            }

                                                                            if(counttarget != 1){
                                                                                str3 = str+"ๆ"+str2;

                                                                            }else {
                                                                                str3 = str + "ๆ";
                                                                            }
                                                                        }


                                                                    }
                                                                }



                                                            }else{
                                                                if(str2.contains("ๆ")){
                                                                    str3 = str2;

                                                                }



                                                            }


                                                            // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                                                            for (int j = 0; j < attitude.size(); j++) {
                                                                if (str.equals(attitude.get(j).get("AttitudeWord"))) {

                                                                    String rank = attitude.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);

                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                            for (int j = 0; j < attitude2.size(); j++) {
                                                                if (str.equals(attitude2.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude2.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }

                                                            for (int j = 0; j < attitude3.size(); j++) {
                                                                if (str.equals(attitude3.get(j).get("AttitudeWord"))) {
                                                                    String rank = attitude3.get(j).get("AttitudeRank");
                                                                    int countrank = Integer.parseInt(rank);


                                                                    count = count + countrank;
                                                                    String frequen ="([ๆ]{1})";
                                                                    Pattern regexfre = Pattern.compile(frequen);
                                                                    Matcher mfre = regexfre.matcher(str3);

                                                                    while (mfre.find()){
                                                                        count = count + countrank;

                                                                    }
                                                                    checkifword++;


                                                                }
                                                            }


                                                        }


                                                        keepMessage = "ข้อความที่โพสต์ : " + postMessage+ "\n";

                                                    }

                                                    if(story.has("story")){
                                                        String PostStory = story.getString("story");
                                                        for(int n = 0;n<storyTimeline.size();n++){

                                                            String checkwordbyregular = storyTimeline.get(n).get("CheckinWord");
                                                            Pattern regexcheck = Pattern.compile(checkwordbyregular);
                                                            Matcher mcheck = regexcheck.matcher(PostStory);
                                                            String rank = storyTimeline.get(n).get("CheckinRank");
                                                            int countrank = Integer.parseInt(rank);

                                                            if (mcheck.find()){
                                                                count = count+countrank;
                                                                checkifword++;
                                                            }

                                                        }


                                                        KeepStory = "เรื่องราว : "+PostStory+"\n";


                                                    }
                                                    keepCreatetime = "โพสต์เมื่อ : " + m.group(0)+"\n";


                                                    if (checkifemo > 0 && countchexkemo > 0) {
                                                        allPostsMessagesPos.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo == 0) {
                                                        allPostsMessagesNeural.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else if (checkifemo > 0 && countchexkemo < 0) {
                                                        allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                    } else {
                                                        if (checkifword > 0 && count > 0) {
                                                            allPostsMessagesPos.add(keepCreatetime+KeepStory+keepMessage );
                                                        } else if (checkifword > 0 && count == 0) {
                                                            allPostsMessagesNeural.add(keepCreatetime+KeepStory+keepMessage );

                                                        } else if (checkifword > 0 && count < 0) {
                                                            allPostsMessagesNege.add(keepCreatetime +KeepStory+keepMessage );

                                                        } else {
                                                            allPostsMessagesNot.add(keepCreatetime +KeepStory+keepMessage );

                                                        }
                                                    }

                                                    allPostsMessages.add(keepCreatetime +KeepStory+keepMessage );


                                                }
                                            }
                                            //Toast.makeText(TimelineActivity.this,timeMessage,Toast.LENGTH_SHORT).show();
                                        }


                                    }


                                    //Toast.makeText(TimelineActivity.this,allPostsMessages.size(),Toast.LENGTH_SHORT).show();

//                                for(int i = 0;i<allPostsMessages.size();i++){
//
//                                }


                                }

                                ArrayList<String> data1 = new ArrayList<>();
                                ArrayList<String> data2 = new ArrayList<>();
                                ArrayList<String> data3 = new ArrayList<>();
                                ArrayList<String> data4 = new ArrayList<>();

                                data1.clear();
                                data2.clear();
                                data3.clear();
                                data4.clear();





                                if(checkboxstate7 == 1&& checkboxstate8 == 1&& checkboxstate9 == 1){
                                    for(int d1 = 0;d1<allPostsMessagesPos.size();d1++){
                                        data1.add(allPostsMessagesPos.get(d1));
                                    }
                                    for(int d1 = 0;d1<allPostsMessagesNege.size();d1++){
                                        data1.add(allPostsMessagesNege.get(d1));
                                    }
                                    for(int d1 = 0;d1<allPostsMessagesNeural.size();d1++){
                                        data1.add(allPostsMessagesNeural.get(d1));
                                    }


                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, data1);
                                    listView.setAdapter(adapter);

                                }else if(checkboxstate7 == 1 && checkboxstate8 == 1){
                                    for(int d1 = 0;d1<allPostsMessagesPos.size();d1++){
                                        data2.add(allPostsMessagesPos.get(d1));
                                    }
                                    for(int d1 = 0;d1<allPostsMessagesNeural.size();d1++){
                                        data2.add(allPostsMessagesNeural.get(d1));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, data2);
                                    listView.setAdapter(adapter);
                                }else if(checkboxstate7 == 1 && checkboxstate9 == 1){

                                    for(int d1 = 0;d1<allPostsMessagesPos.size();d1++){
                                        data3.add(allPostsMessagesPos.get(d1));
                                    }
                                    for(int d1 = 0;d1<allPostsMessagesNege.size();d1++){
                                        data3.add(allPostsMessagesNege.get(d1));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, data3);
                                    listView.setAdapter(adapter);

                                }else if(checkboxstate8 == 1 && checkboxstate9 == 1){

                                    for(int d1 = 0;d1<allPostsMessagesNeural.size();d1++){
                                        data4.add(allPostsMessagesNeural.get(d1));
                                    }

                                    for(int d1 = 0;d1<allPostsMessagesNege.size();d1++){
                                        data4.add(allPostsMessagesNege.get(d1));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, data4);
                                    listView.setAdapter(adapter);
                                }else if(checkboxstate7 == 1){

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, allPostsMessagesPos);
                                    listView.setAdapter(adapter);

                                }else  if(checkboxstate8 == 1){

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, allPostsMessagesNeural);
                                    listView.setAdapter(adapter);
                                }else if(checkboxstate9 == 1){

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, allPostsMessagesNege);
                                    listView.setAdapter(adapter);
                                }else {

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendTimelineActivity.this, R.layout.index_timelinefriend, allPostsMessages);
                                    listView.setAdapter(adapter);
                                }




                                Log.d("Array of Stories", "" + allPostsStory);
                                Log.d("Array of Messages", "" + allPostsMessages);
                            } catch (Exception e) {
                                Log.d("JSON", "error" + e.toString());
                            }

                        }
                    }
            );
            Bundle parameters = new Bundle();
            parameters.putString("fields", "created_time,message,story");
            request.setParameters(parameters);
            request.executeAsync();

        }
    }
}

