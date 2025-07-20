package controller.routingControll;

public enum PageConfigDr {
    HOMEPAGE_DR("/view/NotFindPage/nfp.fxml", false),
    PATIENT_DASHBOARD_DR("/view/NotFindPage/nfp.fxml", false),
    YOUR_CLINIC("/view/NotFindPage/nfp.fxml", false),
    ZEN_BOT_AI_DR("/view/DashboardProf/chatbotDr.fxml", true),
    SETTING_DR("/view/DashboardProf/settingDr.fxml", false),
    FAQ_DR("/view/DashboardProf/faqDr.fxml", false);

    private final String fxmlPath;
    private final boolean showRightbar;

    PageConfigDr(String fxmlPath, boolean showRightbar) {
        this.fxmlPath = fxmlPath;
        this.showRightbar = showRightbar;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public boolean shouldShowRightbar() {
        return showRightbar;
    }

    public static PageConfigDr getByPath(String path) {
        for (PageConfigDr config : values()) {
            if (config.getFxmlPath().equals(path)) {
                return config;
            }
        }
        return null;
    }
}