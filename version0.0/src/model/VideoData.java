package model;

public class VideoData {
    private String title;
    private String youtubeUrl;
    private String thumbnailPath;

    public VideoData(String title, String youtubeUrl, String thumbnailPath) {
        this.title = title;
        this.youtubeUrl = youtubeUrl;
        this.thumbnailPath = thumbnailPath;
    }

    public String getTitle() {
        return title;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    // Convert YouTube URL to embed format
    public String getEmbedUrl() {
        String videoId = null;

        try {
            if (youtubeUrl.contains("watch?v=")) {
                videoId = youtubeUrl.substring(youtubeUrl.indexOf("watch?v=") + 8);
                if (videoId.contains("&")) {
                    videoId = videoId.substring(0, videoId.indexOf("&"));
                }
            } else if (youtubeUrl.contains("youtu.be/")) {
                videoId = youtubeUrl.substring(youtubeUrl.indexOf("youtu.be/") + 9);
                if (videoId.contains("?")) {
                    videoId = videoId.substring(0, videoId.indexOf("?"));
                }
            }
        } catch (Exception e) {
            videoId = null; // fallback to original URL
        }

        if (videoId != null && !videoId.isEmpty()) {
            return "https://www.youtube.com/embed/" + videoId;
        } else {
            return youtubeUrl; // fallback if parsing fails
        }
    }
}
