package LaunchFunction.DataConvert;

import java.sql.Connection;
import java.util.List;

public interface Converter {

    // convert original data to sql format. The sql variables should be fields of “Converter”.
    public void convert(String csvfilepath, Connection connection, String repoID, boolean Update, List<String> filter) throws Exception;

}
