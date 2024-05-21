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
import java.net.HttpCookie;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ZUCC extends ObjectOJ {
    public static String account;
    public static String password;
    public static String contestID;
    public static String apiAddress = "https://jiudge-api.zuccacm.top/v1";
    public static String session;
    public static String token;
    @Override
    public void init() {
        BasicInfo.logger.sendInfo("请输入你的ZUCCACM账号：");
        account = Main.readInput();
        BasicInfo.logger.sendInfo("请输入你的密码：");
        password = Main.readInput();

        JSONObject request = new JSONObject();
        request.put("username",account);
        request.put("password",password);
        JSONObject reply = getReturnData(apiAddress+"/session",request,true,true);
        if (reply == null) {
            BasicInfo.logger.sendWarn("无法连接到OI！请检查网络状态！");
        } else {
            BasicInfo.logger.sendInfo(reply.getString("msg"));
        }

        assert session != null;
        assert token != null;
        reply = getReturnData(apiAddress+"/contests?page=1&page_size=100&order=%7B%22id%22:%22desc%22%7D",null,false,false);
        assert reply != null;
        JSONArray array = reply.getJSONArray("data");
        int count = 0;
        for (Object o : array) {
            JSONObject contest = JSON.parseObject(o.toString());
            if (contest.getString("state").equals("ENDED")) continue;
            count++;
            BasicInfo.logger.sendInfo(contest.getString("id")+" | "+contest.getString("contest_name")+"["+contest.getString("state")+"]");
        }
        BasicInfo.logger.sendInfo("共获取到"+count+"个非结束比赛！");
        BasicInfo.logger.sendInfo("请输入比赛ID：");
        contestID = Main.readInput();
        BasicInfo.logger.sendInfo("你选择了比赛："+contestID);
    }

    @Override
    public void update() {
        JSONObject reply = getReturnData(apiAddress+"/contest/"+contestID,null,false,false);
        assert reply != null;
        JSONObject data = reply.getJSONObject("data");
        JudgeData.updateData("start",0,data.getString("state").equals("RUNNING"));

        reply = getReturnData(apiAddress+"/contest/"+contestID+"/status?username="+account+"&page_size=100",null,false,false);
        assert reply != null;
        data = reply.getJSONObject("data");
        JSONArray commits = data.getJSONArray("data");
        int WA = 0,AC = 0;
        for (Object o : commits) {
            JSONObject commit = JSON.parseObject(o.toString());
            switch (commit.getString("view_result")) {
                case "AC":
                    AC++;
                    break;
                case "WA":
                case "TLE":
                case "MLE":
                    WA++;
                    break;
                default:
                    break;
            }
        }
        JudgeData.updateData("ACTime",AC,false);
        JudgeData.updateData("WATime",WA,false);

        reply = getReturnData(apiAddress+"/contest/"+contestID+"/scoreboard",null,false,false);
        assert reply != null;
        JSONArray scoreboard = reply.getJSONArray("scoreboard");
        int rank = 0;
        for (Object o : scoreboard) {
            JSONObject user = JSON.parseObject(o.toString());
            if (!user.getJSONObject("user").getString("username").equals(account)) continue;
            rank = user.getInteger("rank");
        }
        JudgeData.updateData("rank",rank,false);
    }

    public static JSONObject getReturnData(String address,JSONObject data,boolean saveCookie,boolean post){
        try {
            URL targetURL = new URL(address);
            HttpsURLConnection connection = (HttpsURLConnection) targetURL.openConnection();
            if (post) {
                connection.setRequestMethod("POST");
            } else {
                connection.setRequestMethod("GET");
            }
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0");
            if (!saveCookie) connection.setRequestProperty("Cookie","remember_token="+token+"; session="+session);
            connection.setDoOutput(true);

            if (data != null) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
                writer.write(data.toString());
                writer.flush();
                writer.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder readInfo = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) readInfo.append(inputLine);
            reader.close();

            if (saveCookie) {
                List<String> cookieStrings = connection.getHeaderFields().get("Set-Cookie");
                for (String cookieString : cookieStrings) {
                    HttpCookie cookie = HttpCookie.parse(cookieString).get(0);
                    String name = cookie.getName();
                    String value = cookie.getValue();
                    if (name.equals("session")) session = value;
                    if (name.equals("remember_token")) token = value;
                }
            }
            return JSONObject.parseObject(readInfo.toString());
        } catch (Exception e) {
            BasicInfo.logger.sendWarn("连接API失败："+e.getMessage());
            return null;
        }
    }
}
