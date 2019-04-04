package Contri;

/*
*@author MirageLyu
*
 */

import filecontributesupport.CodeFile;
import filecontributesupport.LOC;

public interface FileContributor {
    public void insertFileMap(String cf, LOC loc);

    public FileContributor changeFilePathNameInMap(String oldpath, String newpath);

    public boolean isInsideMap(String filename);
}
