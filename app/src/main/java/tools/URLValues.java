package tools;

/**
 * Created by Administrator on 2016/5/11.
 */
public class URLValues {
    public static final String ipAddress="http://192.168.43.144";
    public static final String serverURL=ipAddress+":8080/SchoolSmart";
    public static final String imgPath=ipAddress+"/school_mart/";
    public static final String loginURL=serverURL+"/AdminLoginServlet";
    public static final String getGoodsURL=serverURL+"/GetGoodsServlet";
    public static final String getGoodURL=serverURL+"/GetGoodServlet";
    public static final String deleteGood=serverURL+"/DeleteGoodServlet";
    public static final String addGoodURL=serverURL+"/AddGoodServlet";
}
