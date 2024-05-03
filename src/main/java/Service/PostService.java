package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.PostDAO;
import Model.Post;

public class PostService {
    private AccountDAO accountDAO;
    private PostDAO postDAO;

    public PostService() {
        this.accountDAO = new AccountDAO();
        this.postDAO = new PostDAO();
    }

     /**
     * Attempts to add post to the DB
     * @param post 
     * @return A newly created post, or null in the case of failure
     */
    public Post addPost(Post post) {
        if (post.getPost_content().equals("")) {
            return null;
        }
        if (accountDAO.getAccountById(post.getPosted_by()) == null) {
            return null;
        }
        if (post.getPost_content().length() > 140) {
            return null;
        }
        return this.postDAO.insertPost(post);
    }

    public List<Post> getPosts() {
        return this.postDAO.getPosts();
    }

    /**
     * retrieves post by it's id
     * @param id
     * @return post object or null
     */
    public Post getPostById(int id) {
        return this.postDAO.getPostById(id);
    }

    public Post deletePostById(int id) {
        Post deletedPost = this.postDAO.getPostById(id);
        if (deletedPost != null) {
            this.postDAO.deletePostById(id);
        }
        return deletedPost;
    }

    /**
     * Sets message with id to contain newMessage
     * @param id id of the message
     * @param newMessage new message to set
     * @return Message object containing new message, or null on failure
     */
    public Post updatePost(int id, String newContent) {
        if (newContent.equals("")) {
            return null;
        }
        if (newContent.length() > 140) {
            return null;
        }
        if (this.postDAO.getPostById(id) != null) {
            this.postDAO.updatePostContent(id, newContent);
            return this.postDAO.getPostById(id);
        }
        return null;
    }
}
