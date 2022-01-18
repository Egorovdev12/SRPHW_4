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

    public PostRepository() {
        postList = new ConcurrentHashMap<>();
        idGenerator = new AtomicLong(0);

        // Значения для проверки
        postList.put(10L, new Post(10, "First post"));
        postList.put(15L, new Post(15, "Second post"));
        postList.put(20L, new Post(20, "Weird post))))"));
        postList.get(20L).markAsRemoved();
    }

    private boolean containsKey(Long key) {
        return postList.containsKey(key);
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
            if (containsKey(id) && postList.get(id).getRemovedFlag() == false)
                return Optional.ofNullable(postList.get(id));
            else
                return Optional.empty();
    }

    public Optional<Post> save(Post post) {

        // если id = 0, то создаём новый пост
        Post freshPost = new Post();
        if (post.getId() == 0) {
            freshPost = new Post(idGenerator.incrementAndGet(), post.getContent());
            postList.put(idGenerator.get(), freshPost);
            return Optional.ofNullable(freshPost);
        }

        // если id !=0, то изменяем имеющийся пост
        if (post.getRemovedFlag() == false) {
            if (containsKey(post.getId())) {
                postList.put(post.getId(), post);
            } else {
                throw new NotFoundException();
            }
        } else {
            return Optional.empty();
        }
        return Optional.ofNullable(freshPost);
    }

    public void removeById(long id) {
        if (containsKey(id))
            postList.get(id).markAsRemoved();
        else
            throw new NotFoundException();
    }
}