package com.santex;

import com.santex.models.http.CompAndTeamsResp;
import com.santex.utils.MapperUtils;
import org.junit.Test;

public class Sarasa {

    @Test
    public void test() {
        String json = "{\n" +
                "    \"count\": 79,\n" +
                "    \"filters\": {},\n" +
                "    \"competition\": {\n" +
                "        \"id\": 2001,\n" +
                "        \"area\": {\n" +
                "            \"id\": 2077,\n" +
                "            \"name\": \"Europe\"\n" +
                "        },\n" +
                "        \"name\": \"UEFA Champions League\",\n" +
                "        \"code\": \"CL\",\n" +
                "        \"plan\": \"TIER_ONE\",\n" +
                "        \"lastUpdated\": \"2019-08-21T23:59:53Z\"\n" +
                "    },\n" +
                "    \"season\": {\n" +
                "        \"id\": 495,\n" +
                "        \"startDate\": \"2019-06-25\",\n" +
                "        \"endDate\": \"2020-05-30\",\n" +
                "        \"currentMatchday\": null,\n" +
                "        \"winner\": null\n" +
                "    },\n" +
                "    \"teams\": [\n" +
                "        {\n" +
                "            \"id\": 3,\n" +
                "            \"area\": {\n" +
                "                \"id\": 2088,\n" +
                "                \"name\": \"Germany\"\n" +
                "            },\n" +
                "            \"name\": \"Bayer 04 Leverkusen\",\n" +
                "            \"shortName\": \"Leverkusen\",\n" +
                "            \"tla\": \"B04\",\n" +
                "            \"crestUrl\": \"https://upload.wikimedia.org/wikipedia/en/5/59/Bayer_04_Leverkusen_logo.svg\",\n" +
                "            \"address\": \"Bismarckstr. 122-124 Leverkusen 51373\",\n" +
                "            \"phone\": \"+49 (01805) 040404\",\n" +
                "            \"website\": \"http://www.bayer04.de\",\n" +
                "            \"email\": \"stefan.kusche.sk@bayer04.de\",\n" +
                "            \"founded\": 1904,\n" +
                "            \"clubColors\": \"Red / White / Black\",\n" +
                "            \"venue\": \"BayArena\",\n" +
                "            \"lastUpdated\": \"2019-08-22T02:34:18Z\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 8166,\n" +
                "            \"area\": {\n" +
                "                \"id\": 2078,\n" +
                "                \"name\": \"Faroe Islands\"\n" +
                "            },\n" +
                "            \"name\": \"HB\",\n" +
                "            \"shortName\": \"HB\",\n" +
                "            \"tla\": \"HB\",\n" +
                "            \"crestUrl\": null,\n" +
                "            \"address\": \"Postsmoga 1333 Tórshavn 110\",\n" +
                "            \"phone\": \"+298 314046\",\n" +
                "            \"website\": \"http://www.hb.fo\",\n" +
                "            \"email\": \"hb@hb.fo\",\n" +
                "            \"founded\": 1904,\n" +
                "            \"clubColors\": \"Red / Black\",\n" +
                "            \"venue\": \"Gundadalur\",\n" +
                "            \"lastUpdated\": \"2019-08-22T02:48:27Z\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        CompAndTeamsResp resp = MapperUtils.toObject(json, CompAndTeamsResp.class);
    }

}
