package Contri;

/*
*@author MirageLyu
*
 */

import filecontributesupport.CodeFile;
import filecontributesupport.LOC;

import java.util.HashMap;

public interface FileContributor {
    public void insertFileMap(String cf, LOC loc);

    public FileContributor changeFilePathNameInMap(String oldpath, String newpath);

    public boolean isInsideMap(String filename);

    public String getName();

    public HashMap<String, LOC> getMap();

    public int getAllLOC();
}
