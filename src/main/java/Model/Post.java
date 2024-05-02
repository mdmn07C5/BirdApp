package Model;


public class Post {
    private int post_id;
    private int posted_by;
    private String post_content;
    private long time_posted_epoch;

    public Post(){
    }
    /**
     * Two arg constructor for when the DB will be generating the id
     * @param posted_by
     * @param post_content
     * @param time_posted_epoch
     */
    public Post(int posted_by, String post_content, long time_posted_epoch) {
        this.posted_by = posted_by;
        this.post_content = post_content;
        this.time_posted_epoch = time_posted_epoch;
    }
    /**
     * All field constructor for posts returned from the DB
     * @param post_id
     * @param posted_by
     * @param post_content
     * @param time_posted_epoch
     */
    public Post(int post_id, int posted_by, String post_content, long time_posted_epoch) {
        this.post_id = post_id;
        this.posted_by = posted_by;
        this.post_content = post_content;
        this.time_posted_epoch = time_posted_epoch;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(int posted_by) {
        this.posted_by = posted_by;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public long getTime_posted_epoch() {
        return time_posted_epoch;
    }

    public void setTime_posted_epoch(long time_posted_epoch) {
        this.time_posted_epoch = time_posted_epoch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post message = (Post) o;
        return post_id == message.post_id && posted_by == message.posted_by
                && time_posted_epoch == message.time_posted_epoch && post_content.equals(message.post_content);
    }
    /**
     * For easy debugging.
     * @return a String representation of this class.
     */
    @Override
    public String toString() {
        return "Post{" +
                "post_id=" + post_id +
                ", posted_by=" + posted_by +
                ", post_content='" + post_content + '\'' +
                ", time_posted_epoch=" + time_posted_epoch +
                '}';
    }
}
