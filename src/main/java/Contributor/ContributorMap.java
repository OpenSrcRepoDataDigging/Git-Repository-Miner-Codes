package Contributor;

import java.util.HashMap;

public class ContributorMap {
    private HashMap<String,Contributor> maps = new HashMap<String, Contributor>();

    public ContributorMap() {

    }

    public void insertContributor(String authorName,Contributor contributor){
        if(!maps.containsKey(authorName))
            maps.put(authorName,contributor);
    }

    public Contributor getContributor(String authorName){
        return maps.getOrDefault(authorName, null);
    }


}
