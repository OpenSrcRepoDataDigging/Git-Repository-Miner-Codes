package Repository;

import org.junit.Test;

import java.util.*;

public class HashMapSortTest {
    @Test
    public void HashMapSortTest(){
        HashMap map_Data=new HashMap();
        map_Data.put("A", "98");
        map_Data.put("B", "50");
        map_Data.put("D", "50");
        map_Data.put("E", "25");
        map_Data.put("C", "85");
        System.out.println(map_Data);
        List<Map.Entry<String, String>> list_Data = new ArrayList<Map.Entry<String, String>>(map_Data.entrySet());
        System.out.println("Before Sort "+list_Data);
        Collections.sort(list_Data, new Comparator<Map.Entry<String, String>>()
        {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)
            {
                if(o2.getKey()!=null&&o1.getKey()!=null&&o2.getKey().compareTo(o1.getKey())>0){
                    return 1;
                }else{
                    return -1;
                }

            }
        });
        System.out.println("After Sort "+list_Data);

    }

}
