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

    private final Map<Long, Post> postList = new ConcurrentHashMap<>();
    private static AtomicLong idGenerator;

    public PostRepository() {
        idGenerator = new AtomicLong(0);
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
        else if (containsKey(id) && postList.get(id).getRemovedFlag() == true)
            throw new NotFoundException();
        else
            return Optional.empty();
    }

    public Post save(Post post) {
        // если id = 0, то создаём новый пост
        if (post.getId() == 0) {
            postList.put(idGenerator.incrementAndGet(), new Post(idGenerator.get(), post.getContent()));
            return postList.get(idGenerator.get());
        }
        // если id !=0, то изменяем пост, если он имеется
        else {
            if ((containsKey(post.getId())) && (post.getRemovedFlag() == false)) {
                postList.put(post.getId(), post);
                return postList.get(post.getId());
            } else {
                throw new NotFoundException();
            }
        }
    }

    public void removeById(long id) {
        if ((containsKey(id)) && (postList.get(id).getRemovedFlag() == false))
            postList.get(id).markAsRemoved();
        else
            throw new NotFoundException();
    }
}