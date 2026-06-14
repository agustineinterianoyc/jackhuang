package com.hk.demo.api.enums.opinion;

/**
 * 业务动作审计动作编码。
 */
public final class OpinionActionLogAction {

    public static final String START_SURVEY = "START_SURVEY";
    public static final String DELETE_SURVEY = "DELETE_SURVEY";

    public static final String UNIT_SUBMIT = "UNIT_SUBMIT";
    public static final String UNIT_AUDIT_PASS = "UNIT_AUDIT_PASS";
    public static final String UNIT_AUDIT_REJECT = "UNIT_AUDIT_REJECT";
    public static final String OPS_REJECT_UNIT = "OPS_REJECT_UNIT";

    public static final String START_DEPT_FEEDBACK = "START_DEPT_FEEDBACK";
    public static final String DEPT_SUBMIT = "DEPT_SUBMIT";
    public static final String DEPT_AUDIT_PASS = "DEPT_AUDIT_PASS";
    public static final String DEPT_AUDIT_REJECT = "DEPT_AUDIT_REJECT";
    public static final String OPS_REJECT_DEPT = "OPS_REJECT_DEPT";

    public static final String SUMMARIZE = "SUMMARIZE";
    public static final String PUBLISH = "PUBLISH";
    public static final String AUTO_ZERO_REPORT = "AUTO_ZERO_REPORT";

    public static final String REMIND_BATCH = "REMIND_BATCH";
    public static final String REMIND_SINGLE = "REMIND_SINGLE";

    private OpinionActionLogAction() {
    }
}
