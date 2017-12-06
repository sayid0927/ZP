package com.zhengpu.aiuilibrary.iflytekbean;

/**
 * sayid ....
 * Created by wengmf on 2017/12/1.
 */

public class FlightBean extends BaseBean {


    /**
     * rc : 0
     * semantic : [{"intent":"QUERY","slots":[{"name":"endLoc.city","value":"长沙市","normValue":"长沙市"},{"name":"endLoc.cityAddr","value":"长沙","normValue":"长沙"},{"name":"endLoc.type","value":"LOC_BASIC","normValue":"LOC_BASIC"},{"name":"startLoc.city","value":"深圳市","normValue":"深圳市"},{"name":"startLoc.cityAddr","value":"深圳","normValue":"深圳"},{"name":"startLoc.type","value":"LOC_BASIC","normValue":"LOC_BASIC"}]}]
     * service : flight
     * state : {"fg::flight::default::default":{"state":"default"}}
     * text : 帮我订单深圳飞长沙的机票
     * uuid : atn01c24755@ch2eca0d7c126c6f2601
     * used_state : {"state_key":"fg::flight::default::default","state":"default"}
     * answer : {"text":"您想什么时候出发呢？"}
     * dialog_stat : dataInvalid
     * save_history : true
     * sid : atn01c24755@ch2eca0d7c126c6f2601
     */

    private int rc;
    private String service;
    private String text;
    private String uuid;
    private AnswerBean answer;
    private String dialog_stat;
    private boolean save_history;
    private String sid;


    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public String getDialog_stat() {
        return dialog_stat;
    }

    public void setDialog_stat(String dialog_stat) {
        this.dialog_stat = dialog_stat;
    }

    public boolean isSave_history() {
        return save_history;
    }

    public void setSave_history(boolean save_history) {
        this.save_history = save_history;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public static class AnswerBean {

        private String text;


        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }


}
