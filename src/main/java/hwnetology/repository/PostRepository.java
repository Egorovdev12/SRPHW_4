package hwnetology.repository;

import hwnetology.exceptions.NotFoundException;
import hwnetology.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Хранилище наших данных
@Repository
public class PostRepository {

    private final Map<Long, Post> postList;
    private static AtomicLong idGenerator;

    public PostRepository() {
        postList = new ConcurrentHashMap<>();
        idGenerator = new AtomicLong(0);
    }

    private boolean containsKey(Long key) {
        return postList.containsKey(key);
    }

    public List<Post> all() {
        return postList.values().stream().toList();
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(postList.get(id));
    }

    public Post save(Post post) {
        // если id = 0, то создаём новый пост
        Post freshPost = new Post();
        if (post.getId() == 0) {
            freshPost = new Post(idGenerator.incrementAndGet(), post.getContent());
            postList.put(idGenerator.get(), freshPost);
        }

        // если id !=0, то изменяем имеющийся пост
        if (post.getId() != 0) {
            if (containsKey(post.getId())) {
                postList.put(post.getId(), post);
            }
            else {
                throw new NotFoundException();
            }
        }
        return freshPost;
    }

    public void removeById(long id) {
        if (containsKey(id)) {
            postList.remove(id);
        }
        else {
            throw new NotFoundException();
        }
    }
}