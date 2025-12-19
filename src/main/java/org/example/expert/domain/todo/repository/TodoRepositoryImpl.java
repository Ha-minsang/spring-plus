package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.todo.condition.TodoSearchCond;
import org.example.expert.domain.todo.dto.response.TodoSummaryDto;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(todo)
                        .leftJoin(todo.user, user).fetchJoin()
                        .where(todo.id.eq(todoId))
                        .fetchOne()
        );
    }

    @Override
    public Page<TodoSummaryDto> searchTodos(Pageable pageable, TodoSearchCond condition) {
        QTodo todo = QTodo.todo;
        List<TodoSummaryDto> dtos = queryFactory
                .select(Projections.constructor(
                                TodoSummaryDto.class,
                                todo.title,
                                JPAExpressions
                                        .select(comment.count())
                                        .from(comment)
                                        .where(comment.todo.id.eq(todo.id)),
                                JPAExpressions
                                        .select(manager.count())
                                        .from(manager)
                                        .where(manager.todo.id.eq(todo.id))
                        )
                )
                .from(todo)
                .where(containsTitle(condition.getTitle()),
                        containsManagerNickname(condition.getManagerNickname()),
                        betweenCreatedAt(condition.getStartDate(), condition.getEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        containsTitle(condition.getTitle()),
                        containsManagerNickname(condition.getManagerNickname()),
                        betweenCreatedAt(condition.getStartDate(), condition.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(dtos, pageable, total == null ? 0 : total);
    }


private BooleanExpression containsTitle(String title) {
    return StringUtils.hasText(title)
            ? todo.title.contains(title)
            : null;
}

private BooleanExpression containsManagerNickname(String managerNickname) {
    return StringUtils.hasText(managerNickname)
            ? todo.managers.any().user.nickname.contains(managerNickname)
            : null;
}

private BooleanExpression betweenCreatedAt(
        LocalDate startDate,
        LocalDate endDate
) {
    if (startDate == null && endDate == null) {
        return null;
    }

    if (startDate != null && endDate != null) {
        return todo.createdAt.between(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );
    }

    if (startDate != null) {
        return todo.createdAt.goe(startDate.atStartOfDay());
    }

    return todo.createdAt.lt(endDate.plusDays(1).atStartOfDay());
}
}
