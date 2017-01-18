package com.androidapp.jdklokhandwala.helper;

/**
 * Created by sagartahelyani on 29-12-2016.
 */

public class AppConstants {

    public static final int RESPONSE_SUCCESS = 1;

    // Adhar Card Regex : ^\d{4}\s\d{4}\s\d{4}$
    // e.g. 4552 6369 3654
    public static final String AADHAR_CARD = "^\\d{4}\\s\\d{4}\\s\\d{4}$";

    // PAN Card Regex : ^[A-Z]{5}\d{4}[A-Z]{1}$
    // e.g. CPFPP4441E
    public static final String PAN_CARD = "^[A-Z]{5}\\d{4}[A-Z]{1}$";

    // Driving License : ^[A-Z]{2}\d{2}\s\d{11}$
    // e.g. GJ17 20110004369
    public static final String DRIVING_LICENSE = "^[A-Z]{2}\\d{2}\\s\\d{11}$";

    // Voter ID : ^[A-Z]{3}\d{7}$
    // e.g. GJG1234567
    public static final String VOTER_ID = "^[A-Z]{3}\\d{7}$";

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

    public static final String NOTIFICATION_READ = "Notification/GetNotificationRead/{UserId}";

    public static final String GetOrderDetailUrl = "Order/GetOrderDetail/{OrderID}";

    public static final String QuotationAcceptRejectUrl = "Order/QuotationAcceptReject";

    public static final String LOGGED_IN = "is_logged_in";
    public static final String FCM_TOKEN = "fcm_token";
    public static String isPlaceOrder = "isPlaceOrder";
    public static String paymentMethodID = "paymentMethodID";
    public static String isInquiry = "isInquiry";
    public static String statusID = "statusID";
    public static String isAccept="isAccept";


    public enum NotificationTypeId {
        QuotationRequest(8, "QuotationRequest"),
        QuatationCancelledByAdmin(9, "QuatationCancelledByAdmin"),
        QuotationGenerated(10, "QuotationGenerated"),
        CancelledByUser(11, "CancelledByUser"),
        OrderPlaced(12, "OrderPlaced"),
        OrderCancelledByAdmin(13, "OrderCancelledByAdmin"),
        OrderDispatched(14, "OrderDispatched");


        Object[] values;

        NotificationTypeId(Object... _values) {
            this.values = _values;
        }

        public int getValue() {
            return (int) values[0];
        }

        public String getName() {
            return (String) values[1];
        }

        public static String getRequestTypeName(int code) {
            switch (code) {
                case 8:
                    return NotificationTypeId.QuotationRequest.getName();

                case 9:
                    return NotificationTypeId.QuatationCancelledByAdmin.getName();

                case 10:
                    return NotificationTypeId.QuotationGenerated.getName();

                case 11:
                    return NotificationTypeId.CancelledByUser.getName();

                case 12:
                    return NotificationTypeId.OrderPlaced.getName();

                case 13:
                    return NotificationTypeId.OrderCancelledByAdmin.getName();

                case 14:
                    return NotificationTypeId.OrderDispatched.getName();

                default:
                    return null;
            }
        }

        public static int getRequestTypeId(int code) {
            switch (code) {
                case 8:
                    return NotificationTypeId.QuotationRequest.getValue();

                case 9:
                    return NotificationTypeId.QuatationCancelledByAdmin.getValue();

                case 10:
                    return NotificationTypeId.QuotationGenerated.getValue();

                case 11:
                    return NotificationTypeId.CancelledByUser.getValue();

                case 12:
                    return NotificationTypeId.OrderPlaced.getValue();

                case 13:
                    return NotificationTypeId.OrderCancelledByAdmin.getValue();

                case 14:
                    return NotificationTypeId.OrderDispatched.getValue();

                default:
                    return -1;
            }
        }
    }


    /*15 Days=20
    Weekly=19
    Monthly=21*/

 /*   public enum OrderStatus{
        QuotationRequest = 8,
        QuatationCancelledByAdmin = 9,
        QuotationGenerated = 10,
        CancelledByUser = 11,
        OrderPlaced = 12,
        OrderCancelledByAdmin = 13,
        OrderDispatched = 14
    }*/


}
