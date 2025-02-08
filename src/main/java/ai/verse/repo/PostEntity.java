package ai.verse.repo;


import jakarta.persistence.*;


@Entity
@Table(name = "POSTS")
public class PostEntity {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    private String post;


    private String sentiment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}