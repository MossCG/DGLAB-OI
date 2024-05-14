package org.mossmc.mosscg.DGLABOI.Judge.OJ;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.mossmc.mosscg.DGLABOI.BasicInfo;
import org.mossmc.mosscg.DGLABOI.Judge.JudgeData;
import org.mossmc.mosscg.DGLABOI.Main;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CF extends ObjectOJ{
    public static String account;
    public static String contestID;
    public static String apiAddress = "https://codeforces.com/api";
    @Override
    public void init() {
        BasicInfo.logger.sendInfo("请输入你的Codeforces ID：");
        account = Main.readInput();
        JSONObject reply = getReturnData(apiAddress+"/contest.list",null);
        assert reply != null;
        JSONArray array = reply.getJSONArray("result");
        int count = 0;
        for (Object o : array) {
            JSONObject contest = JSON.parseObject(o.toString());
            if (contest.getString("phase").equals("FINISHED")) continue;
            count++;
            BasicInfo.logger.sendInfo(contest.getString("id")+" | "+contest.getString("name")+"["+contest.getString("phase")+"]");
        }
        BasicInfo.logger.sendInfo("共获取到"+count+"个非结束比赛！");
        BasicInfo.logger.sendInfo("请输入比赛ID：");
        contestID = Main.readInput();
        BasicInfo.logger.sendInfo("你选择了比赛："+contestID);
    }

    @Override
    public void update() {
        JSONObject reply = getReturnData(apiAddress+"/contest.standings?contestId="+contestID+"&handles="+account,null);
        assert reply != null;
        if (reply.getString("status").equals("FAILED") && reply.getString("comment").contains("not started")) return;
        JSONObject contestData = reply.getJSONObject("result").getJSONObject("contest");
        JudgeData.updateData("start",0,contestData.getString("phase").equals("CODING"));

        JSONArray array = reply.getJSONObject("result").getJSONArray("rows");
        if (array.size() == 0) return;
        JSONObject userData = JSON.parseObject(array.get(0).toString());
        JudgeData.updateData("rank",userData.getInteger("rank"),false);

        JSONArray problemArray = userData.getJSONArray("problemResults");
        int WA=0,AC=0;
        for (Object o : problemArray) {
            JSONObject problem = JSONObject.parseObject(o.toString());
            WA+=problem.getInteger("rejectedAttemptCount");
            if (problem.containsKey("bestSubmissionTimeSeconds")) AC++;
        }
        JudgeData.updateData("ACTime",AC,false);
        JudgeData.updateData("WATime",WA,false);
    }

    public static JSONObject getReturnData(String address, JSONObject data){
        try {
            URL targetURL = new URL(address);
            HttpsURLConnection connection = (HttpsURLConnection) targetURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0");
            connection.setDoOutput(true);

            if (data != null) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
                writer.write(data.toString());
                writer.flush();
                writer.close();
            }

            BufferedReader reader;
            if (connection.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            String inputLine;
            StringBuilder readInfo = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) readInfo.append(inputLine);
            reader.close();

            return JSONObject.parseObject(readInfo.toString());
        } catch (Exception e) {
            BasicInfo.logger.sendWarn("连接API失败："+e.getMessage());
            return null;
        }
    }
}
