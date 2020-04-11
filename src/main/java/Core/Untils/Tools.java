package Core.Untils;

import Core.Device.DeviceAPI;
import Core.NettyServer;
import io.netty.channel.Channel;

import java.io.*;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {

    public static void messageFomat(String msg){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String message = "["+sdf.format(new Date())+"] "+msg;
        try {
            FileWriter logFileWrite = new FileWriter(NettyServer.logFile,true);
            logFileWrite.write("\r\n");
            logFileWrite.write(message);
            logFileWrite.flush();
            logFileWrite.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("["+sdf.format(new Date())+"] "+msg);
    }

    public static String getStringMD5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest((message+"::18277229825::").getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int sign = b & 0xff;
                String str = Integer.toHexString(sign);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String calcToken(Channel ctx){
        return getStringMD5(getStringMD5(ctx.hashCode()+"::Token::"));
    }

    public static boolean verifyToken(String token, Channel ctx){
        if(token != null) {
            return DeviceAPI.findTargetDevice(ctx).getToken().equalsIgnoreCase(token);
        }
        return false;
    }
}
