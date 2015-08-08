package com.maxpro.iguard.utility;

/**
 * Created by Ketan on 4/16/2015.
 */
public class Key {
    public static class User{
        public static final String NAME = "_User";
        public static final String username = "username";
        public static final String password = "password";
        public static final String authDate = "authDate";
        public static final String emailVerified = "emailVerified";
        public static final String email = "email";
        public static final String userType = "userType";
        public static final String DOB = "DOB";
        public static final String userId = "userId";
        public static final String supervisor = "supervisor";
        public static final String registrationPhoto = "registrationPhoto";
        public static final String status = "status";
        public static final String company = "company";
        public static final String car = "car";
        public static final String cars = "cars";
        public static final String branch = "branch";
        public static final String shift = "shift";
        public static final String site = "site";
        public static final String fullName = "fullName";
        public static final String address = "address";
        public static final String post = "post";

        public static final String contact="contact";
    }
public static class Attendance{
    public static final String NAME = "attendance";
    public static final String submitTime="submitTime";
    public static final String submitTimeGeoPoint="submitTimeGeoPoint";
    public static final String submitType="submitType";
    public static final String submitTimePhoto="submitTimePhoto";
    public static final String usersPointer="usersPointer";
    public static final String branch="branch";
    public static final String company="company";
    public static final String shift="shift";
    public static final String supervisor="supervisor";
    public static final String post="post";
    public static final String site="site";
}
    public static class Alert{
        public static final String NAME = "alert";
        public static final String remark="remark";
        public static final String userPointer="userPointer";
        public static final String usersPointer="usersPointer";
        public static final String alertGeoPoint="alertGeoPoint";
        public static final String branch = "branch";
        public static final String company = "company";
        public static final String supervisor = "supervisor";
        public static final String status="status";
        public static final String read="read";
        public static final String site="site";
        public static final String post = "post";
    }
    public static class Patrolling{
        public static final String NAME = "patrolling";
        public static final String userDetial = "userDetial";
        public static final String patrollingDateTime = "patrollingDateTime";
        public static final String patrollingPhoto = "patrollingPhoto";
        public static final String usersPointer = "usersPointer";
        public static final String branch = "branch";
        public static final String company = "company";
        public static final String supervisor = "supervisor";
        public static final String status = "status";
        public static final String ocrId="ocrId";
        public static final String site="site";
        public static final String post = "post";
        public static final String patrollingPoint = "patrollingPoint";
        public static final String autoPatrolling="autoPatrolling";
    }
    public static class AutoPatrolling{
        public static final String NAME = "autopatrolling";
        public static final String company = "company";
        public static final String branch = "branch";
        public static final String supervisor = "supervisor";
        public static final String type = "type";
        public static final String patrollingDate = "patrollingDate";
        public static final String patrollingTime = "patrollingTime";
        public static final String userPointer = "userPointer";
        public static final String shift = "shift";
        public static final String patrollingEndTime = "patrollingEndTime";
        public static final String deleted="deleted";
    }
    public static class Task{
        public static final String NAME = "Tasks";
        public static final String taskName = "taskName";
        public static final String taskDescription = "taskDescription";
        public static final String taskDateTime = "taskDateTime";
        public static final String company = "company";
        public static final String site = "site";
        public static final String branch = "branch";
        public static final String userPointer = "userPointer";
        public static final String supervisor = "supervisor";
        public static final String post = "post";
        public static final String status = "status";
        public static final String shift= "shift";
        public static final String deleted="deleted";
    }
    public static class TaskReport{
        public static final String NAME = "TaskReport";
        public static final String taskName = "taskName";
        public static final String taskDescription = "taskDescription";
        public static final String taskDateTime = "taskDateTime";
        public static final String company = "company";
        public static final String site = "site";
        public static final String post = "post";
        public static final String branch = "branch";
        public static final String userPointer = "userPointer";
        public static final String supervisor = "supervisor";
        public static final String tastPointer = "task";

    }

    public static class Chats{
        public static final String NAME="Chats";
        public static final String message="message";
        public static final String messageTime="messageTime";
        public static final String supervisorReplyMessage="supervisorReplyMesssage";
        public static final String supervisor="supervisor";
        public static final String company="company";
        public static final String branch="branch";
        public static final String usersPointer="usersPointer";
        public static final String messageDate="messageDate";
        public static final String read="read";
        public static final String post="post";
        public static final String site="site";
        public static final String shift="shift";
    }

    public static class Leave{
        public static final String NAME = "Leave";
        public static final String leaveDate = "leaveDate";
        public static final String leaveTime = "leaveTime";
        public static final String leaveDateTime = "leaveDateTime";
        public static final String applyDateTime = "applyDateTime";
        public static final String leaveReason = "leaveReason";
        public static final String status = "status";
        public static final String usersPointer = "usersPointer";
        public static final String branch="branch";
        public static final String company="company";
        public static final String assignSupervisor = "assignSupervisor";
        public static final String post="post";
        public static final String site="site";
        public static final String shift="shift";
    }

    public static class MsgBoard{
        public static final String NAME = "MessageBoard";
        public static final String messageTitle = "messageTitle";
        public static final String messageDescription = "messageDescription";
        public static final String messageImage = "messageImage";
        public static final String userPointer = "userPointer";
        public static final String branch="branch";
        public static final String company="company";
        public static final String supervisor = "supervisor";
        public static final String deleted="deleted";
    }
    public static class Cars{
        public static final String NAME = "Cars";
        public static final String objectId = "objectId";
        public static final String carName = "carName";
        public static final String vehicleType = "vehicleType";
        public static final String contactNumber = "contactNumber";

        public static final String branch="branch";
        public static final String company="company";
        public static final String deleted="deleted";
    }

    public static class Visits{
        public static final String NAME = "Visits";
        public static final String dateIn = "dateIn";
        public static final String dateOut = "dateOut";
        public static final String timeIn = "timeIn";
        public static final String timeOut = "timeOut";
        public static final String visitor = "visitor";
        public static final String purpose = "purpose";
        public static final String peopleCount = "peopleCount";
        public static final String vehicleNum = "vehicleNum";
        public static final String userPointer = "userPointer";
        public static final String branch="branch";
        public static final String company="company";
        public static final String supervisor = "supervisor";
        public static final String visitPhoto="visitPhoto";
        public static final String post="post";
        public static final String site="site";
        public static final String shift="shift";
    }

    public static class Replacement {
        public static final String NAME = "replacement";
        public static final String userPointer = "userPointer";
        public static final String replacedUserPointer = "replacedUserPointer";
        public static final String replacedDate = "replacedDate";
        public static final String replacedTime = "replacedTime";
        public static final String branch="branch";
        public static final String company="company";
        public static final String supervisor = "supervisor";
        public static final String site="site";
        public static final String reason="reason";
        public static final String status = "status";
        public static final String sign="sign";
        public static final String deleted="deleted";
    }

    public static class Video{
        public static final String NAME = "video";
        public static final String branch="branch";
        public static final String company="company";
        public static final String deleted="deleted";

    }
}
