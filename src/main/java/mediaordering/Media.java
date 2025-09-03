package mediaordering;


public class Media implements Comparable<Media> {
    private final String ORDERED_TITLE_REGEX = "^[0-9]+-[a-zA-Z0-9]+.*";

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
            this.order = -1;
            this.title = mediaFileTitle;
        }
        this.fileTitle = mediaFileTitle;
        this.content = "";
    }

    public boolean isOrdered() {
        return this.order > -1;
    }

    public String getOrderedTitle() {
        if(this.order > -1) {
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
                "order=" + order +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public int getOrder() {
        return order;
    }

    public String getTitle() {
        return title;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public String getContent() {
        return content;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
