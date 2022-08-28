package com.easyapp.m3u;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class M3UParse{

    public static final String 
    TAG_FIRST = "#EXTM3U",
    TAG_INFO = "#EXTINF:",
    TAG_COMMENT = "#";

    private static M3U create(String data, String url){
        Pattern pattern = Pattern.compile("[\\w-]+=\".*?\"");
        HashMap<String, String> map = new HashMap<>();
        String description = null;
        Matcher matcher = null;
        if(data.contains(",")){
            final int index = data.lastIndexOf(",");
            description = data.substring(index + 1);
            matcher = pattern.matcher(data.substring(0, index));
        }else{
            matcher = pattern.matcher(data);
        }
        while(matcher.find()){
            String group = matcher.group();
            int separate = group.indexOf("=");
            String key = group.substring(0, separate);
            String value = group.substring(separate + 1, group.length()).replace("\"", "");
            map.put(key, value);
        }
        return new M3U(map, url, description);
    }

    public static List<M3U> createList(InputStream inputStream) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        final List<M3U> list = new ArrayList<>();
        String line = null, lastLine = null;

        line = br.readLine();

        if(!line.startsWith(TAG_FIRST)){
            throw new IllegalArgumentException("The content is not an m3u.");
        }

        while((line = br.readLine()) != null){
            if(lastLine != null && !lastLine.isEmpty() && lastLine.startsWith(TAG_INFO)){
                if(line.startsWith(TAG_COMMENT)) continue;
                else list.add(create(lastLine, line));
            }
            lastLine = line;        
        }
        return list;
    }

    public static List<M3U> createList(File file) throws IOException{
        return createList(new FileInputStream(file));
    }

    public static List<M3U> createList(String filePath) throws IOException{
        return createList(new File(filePath));
    }

}
