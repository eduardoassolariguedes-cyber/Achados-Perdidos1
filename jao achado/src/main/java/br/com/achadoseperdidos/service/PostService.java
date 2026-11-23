package br.com.achadoseperdidos.service;

import br.com.achadoseperdidos.model.Comment;
import br.com.achadoseperdidos.model.LostFoundPost;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PostService {
    private final List<LostFoundPost> posts = new CopyOnWriteArrayList<>();
    private final AtomicLong nextId = new AtomicLong(2);

    public PostService() {
        posts.add(new LostFoundPost(
                2, "Pedro Almeida", "PA", "Garrafa térmica verde",
                "Acho que esqueci minha garrafa no laboratório de informática depois da aula.",
                "Bloco B", "SP", "São Paulo", "Vila Mariana", "perdido",
                "🧴", "tone-green", "há 1 h", null, List.of()));
        posts.add(new LostFoundPost(
                1, "Marina Costa", "MC", "Chaveiro com fita azul",
                "Encontrei esse chaveiro perto das mesas da biblioteca. Alguém reconhece?",
                "Biblioteca", "SP", "São Paulo", "Centro", "encontrado",
                "🔑", "tone-blue", "há 20 min", null,
                List.of(new Comment("Lucas", "Vou perguntar no grupo da minha sala!"))));
    }

    public List<LostFoundPost> findPosts(
            String status,
            String state,
            String city,
            String neighborhood,
            String query) {
        String normalizedQuery = normalize(query);
        return posts.stream()
                .filter(post -> status == null || status.isBlank() || "todos".equals(status) || post.getStatus().equals(status))
                .filter(post -> matches(post.getState(), state))
                .filter(post -> matches(post.getCity(), city))
                .filter(post -> matches(post.getNeighborhood(), neighborhood))
                .filter(post -> normalizedQuery.isBlank()
                        || normalize(String.join(" ", post.getTitle(), post.getDescription(), post.getLocation(),
                        post.getState(), post.getCity(), post.getNeighborhood())).contains(normalizedQuery))
                .sorted(Comparator.comparingLong(LostFoundPost::getId).reversed())
                .toList();
    }

    public List<String> findCities(String state) {
        return posts.stream()
                .filter(post -> matches(post.getState(), state))
                .map(LostFoundPost::getCity)
                .filter(value -> value != null && !value.isBlank())
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    public List<String> findNeighborhoods(String state, String city) {
        return posts.stream()
                .filter(post -> matches(post.getState(), state))
                .filter(post -> matches(post.getCity(), city))
                .map(LostFoundPost::getNeighborhood)
                .filter(value -> value != null && !value.isBlank())
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    public synchronized LostFoundPost createPost(
            String title,
            String description,
            String location,
            String state,
            String city,
            String neighborhood,
            String status,
            byte[] image) {
        long id = nextId.incrementAndGet();
        String emoji = "perdido".equals(status) ? "🔍" : "📦";
        String tone = "perdido".equals(status) ? "tone-peach" : "tone-green";
        LostFoundPost post = new LostFoundPost(
                id, "Você", "EU", title, description, location, state, city, neighborhood,
                status, emoji, tone, "agora", image, List.of());
        posts.add(0, post);
        return post;
    }

    public Optional<LostFoundPost> findById(long id) {
        return posts.stream().filter(post -> post.getId() == id).findFirst();
    }

    public boolean addComment(long id, String text) {
        Optional<LostFoundPost> post = findById(id);
        post.ifPresent(value -> value.addComment("Você", text));
        return post.isPresent();
    }

    private boolean matches(String actual, String expected) {
        return expected == null || expected.isBlank() || normalize(actual).equals(normalize(expected));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.forLanguageTag("pt-BR"));
    }
}
