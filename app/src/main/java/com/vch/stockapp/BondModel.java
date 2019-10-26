package com.vch.stockapp;

/**
 * 可转债对象
 *
 * @author VC_H
 * @date 2019-10-26
 */
public class BondModel {
    String bondcode;
    //债券简称
    String sname;
    //申购日期
    String startdate;
    //申购代码
    String correscode;
    //正股代码
    String swapscode;
    //正股简称
    String securityshortname;

    public BondModel(String bondcode, String sname, String startdate, String correscode, String swapscode, String securityshortname) {
        this.bondcode = bondcode;
        this.sname = sname;
        this.startdate = startdate;
        this.correscode = correscode;
        this.swapscode = swapscode;
        this.securityshortname = securityshortname;
    }

    public String getBondcode() {
        return bondcode;
    }

    public void setBondcode(String bondcode) {
        this.bondcode = bondcode;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getCorrescode() {
        return correscode;
    }

    public void setCorrescode(String correscode) {
        this.correscode = correscode;
    }

    public String getSwapscode() {
        return swapscode;
    }

    public void setSwapscode(String swapscode) {
        this.swapscode = swapscode;
    }

    public String getSecurityshortname() {
        return securityshortname;
    }

    public void setSecurityshortname(String securityshortname) {
        this.securityshortname = securityshortname;
    }
}
