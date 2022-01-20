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
        if (post.getId() == 0) {
          postList.put(idGenerator.incrementAndGet(), new Post(idGenerator.get(), post.getContent()));
          return postList.get(idGenerator.get());
        }
        // если id !=0, то изменяем имеющийся пост
        else {
            if (containsKey(post.getId())) {
                postList.put(post.getId(), post);
                return postList.get(post.getId());
            }
            else {
                throw new NotFoundException();
            }
        }
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