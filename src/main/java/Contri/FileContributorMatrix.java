package Contri;

/*
@author MirageLyu
*
 */

import IOutils.MatrixSaveDelegate;
import filecontributesupport.LOC;

public interface FileContributorMatrix {
    public FileContributorMatrix compressMatrix(int height, int width);

    public void insertFileContributor(FileContributor fc);

    public void insertFileToFileList(String filename);
    public void removeFileFromFileList(String filename);

    public MatrixSaveDelegate getMatrixSaveDelegate();

    public FileContributorMatrix insertFileLOCPairToMap(String filename, LOC loc);
    public FileContributorMatrix renameFileNameInFileLOCMap(String oldpath, String newpath);
}
