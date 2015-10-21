package com.maxpro.iguard.utility;

import android.os.Environment;

/**
 * Created by Ketan on 3/22/2015.
 */
public class Var {
    public static final String PARSE_APPID="7w9xBGtm0FJoPwp8znAOhanYH2PCUzWRZIJJaPt1";
    public static final String PARSE_CLIENTKEY="s905bffGDD4lbVDHFDFs3nlxjnEmvZLy43OiqW05";
    public static final int REQ_CODE_CAPTURE=100;

    public static final String APP_FOLDER= Environment.getExternalStorageDirectory()+"/iguard";
    public static final String IMAGE_FOLDER=APP_FOLDER+"/images";
    public static final String VIDEO_FOLDER=APP_FOLDER+"/videos";
    public static final String TYPE_ATTEND="type_attend";
    public static final String TYPE_ATTEND_IN="in";
    public static final String TYPE_ATTEND_OUT="out";
    public static final String ATTEND_IMAGE="attend_image";
    public static final String ATTEND_IN_TIME="attend_in_time";
    public static final String TYPE_LEAVE_A="A";
    public static final String TYPE_LEAVE_P="P";
    public static final String TYPE_LEAVE_W="W";

    public static final String DF_DATE="dd-MM-yyyy";
    public static final String DF_DATETIME="dd-MM-yyyy HH:mm:ss";
    public static final String DF_TIME="HH:mm:ss";
    public static final String Last_FetchDateTask="Last_FetchDateTask";
    public static final String Last_FetchDatePatrolling="Last_FetchDatePatrolling";
    public static final String Last_User="Last_User";
    /*intent extra*/
    public static final String IntentObjId="IntentObjId";
    public static final String IntentHeader="IntentHeader";
    public static final String IntentUrl="IntentUrl";

    public static final String REPLACEMENT_CS="CS";/*confirmed*/

}
