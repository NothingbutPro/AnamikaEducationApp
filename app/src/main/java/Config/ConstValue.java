package Config;

/**
 * Created by LENOVO on 4/20/2016.
 */
public class ConstValue {
    public static String BASE_URL = "https://anamikaeducation.com/education_portal";
   // public static String BASE_URL = "http://infocentroid.us/education_portal";
   // public static String BASE_URL = "https://www.spellclasses.co.in/education_demo";
    public static String LOGIN_URL = BASE_URL + "/index.php/api/login";
    public static String STUDENT_PROFILE_URL = BASE_URL + "/index.php/api/get_student_profile";
    public static String STUDENT_ATTENDENCE_URL = BASE_URL + "/index.php/api/get_student_attendence";
    public static String EXAMS_URL = BASE_URL + "/index.php/api/get_exams";
    public static String RESULTS_URL = BASE_URL + "/index.php/api/get_results";
    public static String EVENT_URL = BASE_URL + "/index.php/api/get_school_event";
    public static String TEACHERS_URL = BASE_URL + "/index.php/api/get_school_teacher";
    public static String TOP_STUDENT_URL = BASE_URL + "/index.php/api/get_top_student";
    public static String HOLIDAY_URL = BASE_URL + "/index.php/api/get_holidays";
    public static String NOTICEBOARD_URL = BASE_URL + "/index.php/api/get_school_noticeboard";
    public static String GROWTH_URL = BASE_URL + "/index.php/api/get_student_growth";
    public static String RESULTS_REPORT_URL = BASE_URL + "/index.php/api/get_result_report";
    public static String ENQUIRY_URL = BASE_URL + "/index.php/api/get_enquiry";
    public static String SEND_ENQUIRY_URL = BASE_URL + "/index.php/api/send_enquiry";
    public static String FCM_REGISTER_URL = BASE_URL + "/index.php/api/register_fcm";
    public static String SCHOOL_PROFILE_URL = BASE_URL + "/index.php/api/get_school_profile";
    public static String TIME_TABLE_URL = BASE_URL + "/index.php/api/timetable";
    public static String GET_SUBJECT_URL = BASE_URL + "/index.php/api/get_subject_list";
    public static String GET_QUESTION_URL = BASE_URL + "/index.php/api/get_question_by_subject";
    public static String SET_RESULT_URL = BASE_URL + "/index.php/api/set_quiz_result";
    public static String GET_QUIZ_RESULT_URL = BASE_URL + "/index.php/api/get_quiz_report";
    public static String GET_FEES_URL = BASE_URL + "/index.php/api/list_student_fees_by_student";
    public static String GET_BOOK_URL = BASE_URL + "/index.php/api/get_book_by_standard";
    public static String IMG_BOOK_URL = BASE_URL + "/uploads/book_image/";
    public static String PDF_BOOK_URL = BASE_URL + "/uploads/book_pdf/";
    public static String GET_NOTIFICATION_URL = BASE_URL + "/index.php/api/notification_list";
    public static String IMG_NOTIFICATION_URL = BASE_URL + "/uploads/notification/";
    public static String GET_school_noticeboard = BASE_URL + "/index.php/api/get_school_noticeboard?school_id=";
    public static String GET_TIMETABLE = BASE_URL + "/index.php/api/timetable?standard_id=";


    public static String PREF_NAME = "education.pref";
    public static String COMMON_KEY = "student_id";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "education";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    public static String GCM_SENDER_ID = "881835876590";
}
