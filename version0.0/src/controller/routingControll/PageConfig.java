package controller.routingControll;

public enum PageConfig {
    HOMEPAGE("/view/DashboardZen/homepage.fxml", true),
    JURNAL_ARCHIVE("/view/DashboardZen/journalArchive.fxml", true),
    STRES_STATISTIC("/view/DashboardZen/stressStatistic.fxml", true),
    ZEN_BOT_AI("/view/aiView.fxml", false),
    FIND_KONSELOR("/view/DashboardZen/findKonselor.fxml", false),
    RELAX_MUSIC("/view/DashboardZen/musicPage.fxml", false),
    COMMUNITY("/view/NotFindPage/nfp.fxml", true),
    HISTORY("/view/DashboardZen/RelaxContent.fxml", false),
    SETTING("/view/DashboardZen/Setting.fxml", false),
    FAQ("/view/DashboardZen/faq.fxml", false);

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