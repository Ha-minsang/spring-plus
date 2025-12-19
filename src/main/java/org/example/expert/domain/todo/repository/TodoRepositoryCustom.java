package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.condition.TodoSearchCond;
import org.example.expert.domain.todo.dto.response.TodoSummaryDto;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TodoRepositoryCustom {

    Optional<Todo> findByIdWithUser(Long todoId);
    Page<TodoSummaryDto> searchTodos(Pageable pageable, TodoSearchCond condition);
}
