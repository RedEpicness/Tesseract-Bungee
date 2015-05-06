package me.redepicness.bungee.database;

import java.util.Calendar;

public class Infraction {

    public static Infraction getNewBan(String issuer, String offender, String reason){
        return new Infraction(issuer, offender, -1, "BAN", reason);
    }

    public static Infraction getNewTempBan(String issuer, String offender, int duration, String reason){
        return new Infraction(issuer, offender, duration, "TEMP_BAN", reason);
    }

    public static Infraction getNewMute(String issuer, String offender, String reason){
        return new Infraction(issuer, offender, -1, "MUTE", reason);
    }

    public static Infraction getNewTempMute(String issuer, String offender, int duration, String reason){
        return new Infraction(issuer, offender, duration, "TEMP_MUTE", reason);
    }

    public static Infraction getNewKick(String issuer, String offender, String reason){
        return new Infraction(issuer, offender, -1, "KICK", reason);
    }

    private String issuer;
    private String offender;
    private long when;
    private int duration;
    private InfractionType type;
    private String reason;
    private int ID;
    private boolean expired;
    private String whoExpired;
    private long whenExpired;

    public Infraction(String issuer, String offender, long when, int duration, String type, String reason, int id, String whoExpired, long whenExpired) {
        this.issuer = issuer;
        this.offender = offender;
        this.when = when;
        this.duration = duration;
        this.type = InfractionType.valueOf(type);
        this.reason = reason;
        this.expired = true;
        this.whenExpired = whenExpired;
        this.whoExpired = whoExpired;
        ID = id;
    }

    public Infraction(String issuer, String offender, long when, int duration, String type, String reason, int id) {
        this.issuer = issuer;
        this.offender = offender;
        this.when = when;
        this.duration = duration;
        this.type = InfractionType.valueOf(type);
        this.reason = reason;
        ID = id;
    }

    public Infraction(String issuer, String offender, int duration, String type, String reason) {
        this.issuer = issuer;
        this.offender = offender;
        this.when = Calendar.getInstance().getTimeInMillis();
        this.type = InfractionType.valueOf(type);
        this.duration = duration;
        this.reason = reason;
        ID = -1;
        Database.insertInfraction(this);
    }

    public void expire(String who){
        expired = true;
        whoExpired = who;
        whenExpired = Calendar.getInstance().getTimeInMillis();
        Database.expireInfraction(this);
    }

    public boolean isExpired() {
        if(type.equals(InfractionType.BAN) || type.equals(InfractionType.MUTE) || type.equals(InfractionType.KICK))
            return expired;
        if(!expired && when + duration*1000 < Calendar.getInstance().getTimeInMillis()){
            expire("Auto");
        }
        return expired;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getOffender() {
        return offender;
    }

    public long getWhen() {
        return when;
    }

    public int getDuration() {
        return duration;
    }

    public InfractionType getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public int getID() {
        return ID;
    }

    public String getWhoExpired() {
        return whoExpired;
    }

    public long getWhenExpired() {
        return whenExpired;
    }

    public enum InfractionType {

        BAN, TEMP_BAN, MUTE, TEMP_MUTE, KICK;

        public static boolean isValid(String name){
            try{
                InfractionType.valueOf(name);
            }
            catch (IllegalArgumentException e){
                return false;
            }
            return true;
        }

    }

}
