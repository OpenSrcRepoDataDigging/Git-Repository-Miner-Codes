package cooperateInfo;

import Contri.Contributor;

import java.util.HashMap;

public class FileContributor extends Contributor {
    HashMap<CodeFile, LOC> FileMap;

    FileContributor(String authorname){
        super(authorname);
        Initialize();
    }

    void Initialize(){
        return;
    }
}
