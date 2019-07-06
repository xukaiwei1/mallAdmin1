package com.jfinal.club.common.kit;



import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import java.util.concurrent.TimeUnit;


public class LockUtil {




    /**
     *
     * @param key
     * @param seconds
     * @return 锁定成功返回true
     */

    public  static boolean lock(String key,int seconds){
        Cache bbsCache = Redis.use("bbs");
        String assignStr = bbsCache.get(key);
        if(StrKit.isBlank(assignStr)){
            String  result=bbsCache.setex(key,seconds,"1");
            return result.equals("OK")? true:false;
        }
        return false;
    }

    public static void unLock(String key){
        Cache bbsCache = Redis.use("bbs");
        bbsCache.del(key);
    }


    public static String getJimKey(String key,int id) {
        return String.format(key, id);
    }


}
