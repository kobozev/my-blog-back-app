package ru.practicum.kobozevva.blog.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.kobozevva.blog.model.Tag;

public interface TagRepository extends CrudRepository<Tag, Long>, TagRepositoryCustom {
}