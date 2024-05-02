package Service;

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
        if (post.getPost_content().length() > 255) {
            return null;
        }
        return this.postDAO.insertPost(post);
    }
}