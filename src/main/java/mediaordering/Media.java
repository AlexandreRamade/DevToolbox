package mediaordering;


public class Media implements Comparable<Media> {
    private final String ORDERED_TITLE_REGEX = "^[0-9]+-[a-zA-Z0-9]+.*";
    private final int UNORDERED_INDEX = -1;

    private int order;
    private String title;
    private String fileTitle;
    private String content;

    public Media(int order, String title, String fileTitle, String content) {
        this.order = order;
        this.title = title;
        this.fileTitle = fileTitle;
        this.content = content;
    }

    public Media(String mediaFileTitle) {
        if(mediaFileTitle.matches(ORDERED_TITLE_REGEX)) {
            this.order = Integer.parseInt(mediaFileTitle.substring(0, mediaFileTitle.indexOf("-")));
            this.title = mediaFileTitle.substring(mediaFileTitle.indexOf("-") + 1);
        } else {
            this.order = UNORDERED_INDEX;
            this.title = mediaFileTitle;
        }
        this.fileTitle = mediaFileTitle;
        this.content = "";
    }

    public boolean isOrdered() {
        return this.order > UNORDERED_INDEX;
    }

    public void removeOrder() {
        this.order = UNORDERED_INDEX;
    }

    public String getOrderedTitle() {
        if(isOrdered()) {
            return this.order + "-" + this.title;
        }
        return this.title;
    }

    public boolean toBeRenamed() {
        return !this.getOrderedTitle().equals(this.fileTitle);
    }

    @Override
    public int compareTo(Media o) {
        if(this.order == o.order) {
            return this.title.compareTo(o.title);
        }
        return order - o.order;
    }

    @Override
    public String toString() {
        return "Media{" +
                "ORDERED_TITLE_REGEX='" + ORDERED_TITLE_REGEX + '\'' +
                ", order=" + order +
                ", title='" + title + '\'' +
                ", fileTitle='" + fileTitle + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public int getOrder() {
        return order;
    }

    public String getTitle() { return title; }

    public String getFileTitle() {
        return fileTitle;
    }

    public String getContent() {
        return content;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
