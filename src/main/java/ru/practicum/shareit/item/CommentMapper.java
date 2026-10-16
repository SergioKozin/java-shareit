package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;

@UtilityClass
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public Comment toCommentEntity(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItem(),
                null,
                commentDto.getCreated()
        );
    }
}
