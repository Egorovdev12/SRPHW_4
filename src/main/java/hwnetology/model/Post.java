package hwnetology.model;

// Модель, описывающая пост
public class Post {

    private long id;
    private String content;
    private transient boolean removedFlag;

    public Post() {

    }

    public Post(long id, String content) {
        this.id = id;
        this.content = content;
        setRemovedFlag(false);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void markAsRemoved() {
        this.removedFlag = true;
    }

    public void restore() {
        this.removedFlag = false;
    }

    public boolean getRemovedFlag() {
        return this.removedFlag;
    }

    public void setRemovedFlag(boolean value) {
        this.removedFlag = value;
    }
}