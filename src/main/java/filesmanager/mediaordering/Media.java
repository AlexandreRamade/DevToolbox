package filesmanager.mediaordering;


public class Media implements Comparable<Media> {
    private final String ORDER_SEPARATOR = "_";
    private final String ORDERED_TITLE_REGEX = "^[0-9]{1,4}_[a-zA-Z0-9]+.*";
    private final int UNORDERED_INDEX = -1;

    private int order;
    private String title;
    private String fileTitle;
    private String content;

    public Media() {
    }

    public Media(int order, String title, String fileTitle, String content) {
        this.order = order;
        this.title = title;
        this.fileTitle = fileTitle;
        this.content = content;
    }

    public Media(String mediaFileTitle, String content) {
        if(mediaFileTitle.matches(ORDERED_TITLE_REGEX)) {
            this.order = Integer.parseInt(mediaFileTitle.substring(0, mediaFileTitle.indexOf(ORDER_SEPARATOR)));
            this.title = mediaFileTitle.substring(mediaFileTitle.indexOf(ORDER_SEPARATOR) + 1);
        } else {
            this.order = UNORDERED_INDEX;
            this.title = mediaFileTitle;
        }
        this.fileTitle = mediaFileTitle;
        this.content = content;
    }

    public boolean isOrdered() {
        return this.order != UNORDERED_INDEX;
    }

    public void removeOrder() {
        this.order = UNORDERED_INDEX;
    }

    public String getOrderedTitle() {
        return this.getOrderedTitle(1);
    }

    public String getOrderedTitle(int digitsNumber) {
        if(isOrdered()) {
            return getFormatedOrder(this.order, digitsNumber) + ORDER_SEPARATOR + this.title;
        }
        return this.title;
    }

    private String getFormatedOrder(int order, int digitsNumber) {
        StringBuilder formatedOrder = new StringBuilder();
        formatedOrder.append(order);
        while (formatedOrder.length() < digitsNumber) {
            formatedOrder.insert(0, "0");
        }
        return formatedOrder.toString();
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
