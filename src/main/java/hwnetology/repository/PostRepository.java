package hwnetology.repository;

import hwnetology.exceptions.NotFoundException;
import hwnetology.model.Post;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Хранилище наших данных
@Repository
public class PostRepository {

    private Map<Long, Post> postList;
    private static AtomicLong idGenerator;
    private final long DELTA_ID = 1;

    public PostRepository() {
        postList = new ConcurrentHashMap<>();
        idGenerator = new AtomicLong(0);

        // Значения для проверки
        postList.put(10L, new Post(10, "First post"));
        postList.put(15L, new Post(15, "Second post"));
        postList.put(20L, new Post(20, "Weird post))))"));
        postList.get(20L).markAsRemoved();
    }

    public List<Post> all() {
        // Создаём новый лист, который будет содержать только не отмеченные на удаление посты
        List<Post> actualList = new LinkedList<>();
        for (Post currentPost : postList.values()) {
            if (currentPost.getRemovedFlag() == false) {
                actualList.add(currentPost);
            }
        }
        return actualList;
    }

    public Optional<Post> getById(long id) {
        for (Post currentPost : postList.values()) {
            if (currentPost.getId() == id) {
                return Optional.of(currentPost);
            }
        }
        return Optional.empty();
    }

    public Optional<Post> save(Post post) {
        // если id = 0, то создаём новый пост
        Post freshPost = new Post();
        if (post.getId() == 0) {
            freshPost = new Post(idGenerator.addAndGet(DELTA_ID), post.getContent());
            postList.put(idGenerator.get(), freshPost);
        }

        // если id !=0, то изменяем имеющийся пост
        if (post.getRemovedFlag() == false) {
            if (post.getId() != 0) {
                for (Post currentPost : postList.values()) {
                    if (currentPost.getId() == post.getId()) {
                        currentPost.setContent(post.getContent());
                        break;
                    } else {
                        throw new NotFoundException("There is no post with id = " + post.getId());
                    }
                }
            }
        }
        else {
            return Optional.empty();
        }

        return Optional.of(freshPost);
    }

    public void removeById(long id) {
        for (Post currentPost : postList.values()) {
            if (currentPost.getId() == id) {
                currentPost.markAsRemoved();
            }
        }
    }
}