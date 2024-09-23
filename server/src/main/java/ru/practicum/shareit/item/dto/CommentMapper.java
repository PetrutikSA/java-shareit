package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    Comment commentCreateDtoToComment(CommentCreateDto commentCreateDto);

    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    CommentDto commentToCommentDto(Comment comment);
}
