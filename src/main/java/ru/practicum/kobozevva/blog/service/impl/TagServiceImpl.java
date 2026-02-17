package ru.practicum.kobozevva.blog.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.kobozevva.blog.model.Tag;
import ru.practicum.kobozevva.blog.repository.PostRepository;
import ru.practicum.kobozevva.blog.repository.TagRepository;
import ru.practicum.kobozevva.blog.service.TagService;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public TagServiceImpl(TagRepository tagRepository,
                          PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Tag> getTagsByPost(long postId) {
        ensurePostExists(postId);
        return tagRepository.findByPostId(postId);
    }

    @Transactional
    @Override
    public void replacePostTags(long postId, List<String> tagNames) {
        ensurePostExists(postId);
        tagRepository.deleteBindingsByPostId(postId);

        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }

        List<Tag> tags = tagRepository.findOrCreate(tagNames);
        tagRepository.bindTagsToPost(postId, tags);
    }

    private void ensurePostExists(long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }
    }
}