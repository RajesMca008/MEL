package com.morgans_eletranic_ltd;

public class Constants {
	public static String KEY1 = "NAME";
	public static String KEY2 = "USERNAME";
	public static String KEY3 = "PASSWORD";
	public static String KEY4 = "NAME";
	public static String KEY5 = "COMMENT";
	public static String PREF_NAME = "SHARED_PREF_USER_LOGIN_STATUS";
	public static String KEY_UID = "Username";
	public static String KEY_PWD = "Password";
	public static String KEY_CB = "";
	public static final String BASE_URL = "http://Managejobservices.oorussoft.com/Service1.svc/";
	public static String LOGIN_URL = BASE_URL + "validateUserCredentials?";
	public static String JOBS_BY_DATE = BASE_URL
			+ "viewSupervisorJobsByDate?SupervisorID=";
	public static String MATERIALS_LIST = BASE_URL
			+ "searchProducts?ProductName";
	public static String GET_ALL_JOBS = BASE_URL
			+ "viewSupervisorAllJobs?SupervisorID=";

	public static String START_DAY = BASE_URL
			+ "saveStartDayTime?SupervisorID=";
	public static String VIEW_JOBS = BASE_URL + "viewJobNotes?JobID=";
	public static String VIEW_FILES = BASE_URL + "viewJobFiles?JobID=";

	public static String START_JOB = BASE_URL + "saveJobStartTime?JobID=";
	public static String END_JOB = BASE_URL + "saveJobEndTime?JobID=";
	public static String UPLOAD_NOTES = BASE_URL + "saveJobNotes?";
	public static boolean isEngineer = false;
	public static boolean isSupervisor = false;
	public static boolean LOGIN_STATUS = false;
	public static String USER_ID = "";
	public static String JOB_ID = "";
	public static String CONTENT_TYPE = "application/json";
	
	
	public static String SUPPLIERS_LIST = BASE_URL
			+ "searchSuppliersBySupplierName?SupplierName";
}
