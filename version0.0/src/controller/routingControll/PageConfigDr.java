package controller.routingControll;

public enum PageConfigDr {
    HOMEPAGE_DR("/view/DashboardZen/homepage.fxml", true),
    JURNAL_ARCHIVE_DR("/view/homeDr/jurnalArchiveDr.fxml", true),
    FIND_KONSELOR_DR("/view/homeDr/findKonselorDr.fxml", true),
    ZEN_BOT_AI_DR("/view/homeDr/zenBotAiDr.fxml", false),
    SETTING_DR("/view/homeDr/settingDr.fxml", false),
    FAQ_DR("/view/homeDr/faqDr.fxml", true);

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

    // Utility method to get enum by path
    public static PageConfigDr getByPath(String path) {
        for (PageConfigDr config : values()) {
            if (config.getFxmlPath().equals(path)) {
                return config;
            }
        }
        return null;
    }
}