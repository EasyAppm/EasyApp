package com.easyapp.net.http;

import android.net.Uri;
import com.easyapp.core.TypeValidator;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.LinkedHashMap;

public abstract class URLHandler {

    private final Map<String, String> queries = new LinkedHashMap<>();
    private final List<String> paths = new ArrayList<>();
    private final String url;
    private final boolean keepPathSeparate;


    protected URLHandler(String url) {
        queries.clear();
        paths.clear();
        Uri uri = Uri.parse(url);
        for (String queryKey : uri.getQueryParameterNames()) {
            putQuery(queryKey, uri.getQueryParameter(queryKey));
        }
        paths.addAll(uri.getPathSegments());
        String encodePath = uri.getEncodedPath();
        keepPathSeparate = encodePath.endsWith("/");
        this.url = Uri.parse(TypeValidator.argumentNonNull(url, "url cannot be null.").replace(encodePath, "")).buildUpon().clearQuery().toString();
    }


    /**Query**/

    public boolean containsQuery() {
        return !queries.isEmpty();
    }

    public boolean containsKeyQuery(String key) {
        return queries.containsKey(key);
    }

    public boolean containsValueQuery(String value) {
        return queries.containsValue(value);
    }

    public int countQueries() {
        return queries.size();
    }

    public Set<String> getKeysQuery() {
        return queries.keySet();
    }

    public String getQuery(String key) {
        return queries.get(key);
    }

    public boolean putQuery(String key, String value) {
        if (key == null || value == null || key.isEmpty() || value.isEmpty()) {
            return false;
        }
        queries.put(key, value);
        return true;
    }

    public boolean putQuery(String keyAndValue) {
        final String separate = ":";
        if (!keyAndValue.contains(separate)) {
            return false;
        }
        String[] array = keyAndValue.split(separate);
        return putQuery(array[0].trim(), array[1].trim());
    }

    public String removeQuery(String key) {
        return queries.remove(key);
    }

    public void clearQueries() {
        queries.clear();
    }

    /**Path**/

    public boolean containsPaths() {
        return !paths.isEmpty();
    }

    public boolean containsPath(String path) {
        return paths.contains(path);
    }

    public int countPaths() {
        return paths.size();
    }

    public String getPath(int position) {
        return paths.get(position);
    }

    public List<String> getPaths() {
        return paths;
    }

    public void addPath(String path) {
        if (path.contains("/")) {
            for (String pathSepare : path.split("/")) addPath(pathSepare);
        } else {
            if (!path.trim().isEmpty()) paths.add(path.trim());
        }
    }

    public boolean removePath(String path) {
        return paths.remove(path);
    }

    public String removePath(int position) {
        return paths.remove(position);
    }

    public void clearPaths() {
        paths.clear();
    }

    @Override
    public String toString() {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        
        if(keepPathSeparate){
            builder.appendPath("");
        }
        
        for (String key : queries.keySet()) {
            builder.appendQueryParameter(key, queries.get(key));
        }
        try {
            return URLDecoder.decode(builder.build().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
