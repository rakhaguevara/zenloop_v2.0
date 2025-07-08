package controller;

public enum PageConfig {
    HOMEPAGE("/view/DashboardZen/homepage.fxml", true),
    JURNAL_ARCHIVE("/view/JurnalArchive/jurnalArchive.fxml", true),
    STRES_STATISTIC("/view/NotFindPage/nfp.fxml", false),
    ZEN_BOT_AI("/view/ZenBotAi/zenBotAi.fxml", false), // Hide rightbar
    FIND_KONSELOR("/view/FindYourKonselor/findYourKonselor.fxml", true),
    RELAX_MUSIC("/view/RelaxMusic/relaxMusic.fxml", false), // Hide rightbar
    COMMUNITY("/view/Community/community.fxml", true),
    HISTORY("/view/History/history.fxml", true),
    SETTING("/view/DashboardZen/Setting.fxml", false), // Hide rightbar
    FAQ("/view/FAQ/faq.fxml", true);

    private final String fxmlPath;
    private final boolean showRightbar;

    PageConfig(String fxmlPath, boolean showRightbar) {
        this.fxmlPath = fxmlPath;
        this.showRightbar = showRightbar;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public boolean shouldShowRightbar() {
        return showRightbar;
    }

    public static PageConfig getByPath(String fxmlPath) {
        for (PageConfig config : PageConfig.values()) {
            if (config.getFxmlPath().equals(fxmlPath)) {
                return config;
            }
        }
        return null;
    }
}