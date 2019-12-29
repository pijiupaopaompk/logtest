package com.zkys.pad.launcher.Rxbus.bean;

/**
 * Created by mingpk on 2018-11-14.
 */

public class RxRefreshMainTeam {
    public int teamId;
    public String teamName;
    public String hosipitalName;
    public String depName;

    public RxRefreshMainTeam(int teamId, String teamName, String hosipitalName, String depName) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.hosipitalName = hosipitalName;
        this.depName = depName;
    }
}
