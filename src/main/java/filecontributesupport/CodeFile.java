package filecontributesupport;

/*
@author MirageLyu
*
 */

public class CodeFile {
    private String name;
    private int lines;
    private Language language;

    CodeFile(String name, int lines, Language language){
        this.name = name;
        this.lines = lines;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public int getLines() {
        return lines;
    }

    public Language getLanguage() {
        return language;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }
}
