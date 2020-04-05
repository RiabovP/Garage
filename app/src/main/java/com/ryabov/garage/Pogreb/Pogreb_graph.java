package com.ryabov.garage.Pogreb;

public class Pogreb_graph extends PogrebokV1 {

    public String Request_json;

    public String temp_streetByDate[];
    public String date_temp[];



    public Pogreb_graph (int count, String jsonStr)
    {
        Request_json=jsonStr;

        temp_streetByDate = new String[count];
        date_temp=new String[count];
    }


}
