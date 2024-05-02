package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Post;
import Util.ConnectionUtil;

public class PostDAO {
    

    /**
     * Inserts a new row into the post table
     * @param post the post to be inserted
     * @return the newly inserted post, or null in case of failure
     */
    public Post insertPost(Post post) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO post "
                +  "(posted_by, post_content, time_posted_epoch) "
                + " VALUES (?, ?, ?)";
            PreparedStatement preparedStatement 
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, post.getPosted_by());
            preparedStatement.setString(2, post.getPost_content());
            preparedStatement.setLong(3, post.getTime_posted_epoch());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return new Post(
                    (int) resultSet.getLong("post_id"), 
                    post.getPosted_by(),
                    post.getPost_content(),
                    post.getTime_posted_epoch()
                );
            }
        } catch (SQLException e) {
            // TODO: logging
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieve all posts
     * @return a list of posts, can be empty
     */
    public List<Post> getPosts() {
        Connection conn = ConnectionUtil.getConnection();
        List<Post> posts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM post";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Post post = new Post(
                    (int) resultSet.getLong("post_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("post_content"),
                    resultSet.getLong("time_posted_epoch")
                );
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
}
