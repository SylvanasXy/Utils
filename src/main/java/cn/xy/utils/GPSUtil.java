package cn.xy.utils;

import java.text.DecimalFormat;


/**
 * GPS工具类
 * @author xy
 */
public final class GPSUtil {
    private GPSUtil() {

    }

    /**
     * GPS实体类, 这里只是最简单的model
     * 可以根据实际需求修改该类
     */
    static class GPSModel {
        private double latitude; //纬度
        private double longitude; //经度

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    /**
     * WGS84转GCJ-02坐标系 算法来源于互联网，与官方转换接口相比较精度还不错
     * @param data GPS实体类
     * @return 转换后的数据实体
     */
    public static GPSModel convertToGCJ02(GPSModel data) {
        DecimalFormat df = new DecimalFormat("#.######");
        double wgLat = data.getLatitude();
        double wgLon = data.getLongitude();
        double mgLat;
        double mgLon;
        if (outOfChina(wgLat, wgLon)) {
            mgLat = wgLat;
            mgLon = wgLon;

        } else {
            double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
            double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
            double radLat = wgLat / 180.0 * pi;
            double magic = Math.sin(radLat);
            magic = 1 - ee * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
            dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
            mgLat = wgLat + dLat;
            mgLon = wgLon + dLon;
        }
        mgLat = Double.parseDouble(df.format(mgLat));
        mgLon = Double.parseDouble(df.format(mgLon));
        GPSModel result = new GPSModel();
        result.setLatitude(mgLat);
        result.setLongitude(mgLon);
        return result;
    }

    /**
     * WGS84转百度坐标系
     * 这里没写方法体, 调用百度转换api即可
     * 参考链接:  http://lbsyun.baidu.com/index.php?title=webapi/guide/changeposition
     * @param data GPS实体类
     * @return 转换后的数据实体
     */
    public static GPSModel convertToBaiduGps(GPSModel data) {
        return null;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return
     */
    public static double getPositionDistance(double lng1, double lat1, double lng2, double lat2) {
        if (lng1 == lng2 && lat1 == lat2) {
            return 0;
        }
        double f = rad((lat1 + lat2) / 2);
        double g = rad((lat1 - lat2) / 2);
        double l = rad((lng1 - lng2) / 2);
        double sg = Math.sin(g);
        double sl = Math.sin(l);
        double sf = Math.sin(f);
        double s, c, w, r, d, h1, h2;
        double fl = 1 / 298.257;//扁率
        int alx = 1;
        sg = sg * sg;
        sl = sl * sl;
        sf = sf * sf;

        s = sg * (1 - sl) * alx + (1 - sf) * sl * alx;
        c = (1 - sg) * (1 - sl) * alx + sf * sl * alx;
        w = Math.atan(Math.sqrt(s / c));
        r = Math.sqrt(s * c) / w;
        d = 2 * w * EARTH_RADIUS;
        h1 = (3 * r - 1) / 2 / c;
        h2 = (3 * r + 1) / 2 / s;
        return d * (1 + fl * (h1 * sf * (1 - sg) - h2 * (1 - sf) * sg));
    }


    static double pi = 3.14159265358979324;
    static double a = 6378245.0;
    static double ee = 0.00669342162296594323;

    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    private static double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

}
