package br.com.achadoseperdidos.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LostFoundPost {
    private final long id;
    private final String author;
    private final String initials;
    private final String title;
    private final String description;
    private final String location;
    private final String state;
    private final String city;
    private final String neighborhood;
    private final String status;
    private final String emoji;
    private final String tone;
    private final String createdAt;
    private final byte[] image;
    private final List<Comment> comments = new CopyOnWriteArrayList<>();

    public LostFoundPost(
            long id,
            String author,
            String initials,
            String title,
            String description,
            String location,
            String state,
            String city,
            String neighborhood,
            String status,
            String emoji,
            String tone,
            String createdAt,
            byte[] image,
            List<Comment> initialComments) {
        this.id = id;
        this.author = author;
        this.initials = initials;
        this.title = title;
        this.description = description;
        this.location = location;
        this.state = state;
        this.city = city;
        this.neighborhood = neighborhood;
        this.status = status;
        this.emoji = emoji;
        this.tone = tone;
        this.createdAt = createdAt;
        this.image = image == null ? new byte[0] : image.clone();
        this.comments.addAll(initialComments);
    }

    public void addComment(String author, String text) {
        comments.add(new Comment(author, text));
    }

    public boolean hasImage() {
        return image.length > 0;
    }

    public long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getInitials() {
        return initials;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getStatus() {
        return status;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getTone() {
        return tone;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public byte[] getImage() {
        return image.clone();
    }

    public List<Comment> getComments() {
        return List.copyOf(comments);
    }
}
