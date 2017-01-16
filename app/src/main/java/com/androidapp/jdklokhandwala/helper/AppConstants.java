package com.androidapp.jdklokhandwala.helper;

/**
 * Created by sagartahelyani on 29-12-2016.
 */

public class AppConstants {

    public static final int SHARE_PRODUCT = 1;
    public static final int BOOKMARK_PRODUCT = 2;
    public static final int ADD_CART_PRODUCT = 3;
    public static final int DELETE_CART_PRODUCT = 4;
    public static final int EDIT_CART_PRODUCT = 5;

    public static final String WEB_SERVICE_URL = "http://ws-srv-net.in.webmyne.com/Applications/JDK_V01/Service/API/";

    public static final String GetCategoryUrl = "Dashboard/GetCategory";

    public static final String GetAboutUsUrl = "Dashboard/GetAboutUs";

    public static final String GetContactUsUrl = "Dashboard/GetContactUs";

    public static final String GetProductsUrl = "Product/GetProducts/{CategoryID}";

    public static final String GetProfileUrl = "User/GetProfile/{UserID}";

    public static final String RegistrationUrl = "User/Registration";

    public static final String LoginUrl = "User/Login";

    public static final String ChangePasswordUrl = "User/ChangePassword";

    public static final String UpdateUserUrl = "User/UpdateProfile";

    public static final String PlaceQuotationOrOrderUrl = "Order/PlaceQuotationOrOrder";

    public static final String GetUSerIdentityTypeUrl = "User/GetUSerIdentityType";

    public static final String GetCityByPinUrl = "User/GetCityByPin";

    public static final String GetHistoryUrl = "Order/GetHistory";

    public static final String GET_NOTIFICATION = "Notification/GetNotifications";

    public static final String GetOrderDetailUrl = "Order/GetOrderDetail/{OrderID}";

    public static final String QuotationAcceptRejectUrl = "Order/QuotationAcceptReject";

    public static final String LOGGED_IN = "is_logged_in";
    public static final String FCM_TOKEN = "fcm_token";
    public static String isPlaceOrder="isPlaceOrder";
    public static String paymentMethodID="paymentMethodID";
    public static String isInquiry="isInquiry";

    /*15 Days=20
    Weekly=19
    Monthly=21*/
}
