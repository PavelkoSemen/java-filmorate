package ru.yandex.practicum.filmorate.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.EventRepository;
import ru.yandex.practicum.filmorate.error.SaveEventException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.utils.customannotations.EventFeed;

import java.lang.reflect.Method;
import java.time.Instant;

@Component
@Aspect
public class EventFeedAspect {

    private final EventRepository eventRepository;


    @Autowired
    public EventFeedAspect(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Pointcut("@annotation(ru.yandex.practicum.filmorate.utils.customannotations.EventFeed) && " +
            "within(ru.yandex.practicum.filmorate.service.filmservice.*)")
    public void evenFilms() {
    }

    @Pointcut("@annotation(ru.yandex.practicum.filmorate.utils.customannotations.EventFeed) && " +
            "within(ru.yandex.practicum.filmorate.service.userservice.*)")
    public void evenUsers() {
    }


    @Pointcut("@annotation(ru.yandex.practicum.filmorate.utils.customannotations.EventFeed) && " +
            "within(ru.yandex.practicum.filmorate.service.reviewservice.*)")
    public void evenReview() {
    }


    @AfterReturning(pointcut = "evenFilms()", returning = "isDone")
    public void insertEventFilms(JoinPoint jp, Object isDone) {
//        if (!(Boolean) isDone)
//            return;
        Object[] args = jp.getArgs();
        long entityId = (long) args[0];
        long userId = (long) args[1];

        saveEvent(jp, userId, entityId);
    }

    @AfterReturning(pointcut = "evenUsers()", returning = "isDone")
    public void insertEventUsers(JoinPoint jp, Object isDone) {
//        if (!(Boolean) isDone)
//            return;
        Object[] args = jp.getArgs();
        long userId = (long) args[0];
        long entityId = (long) args[1];

        saveEvent(jp, userId, entityId);

    }

    @AfterReturning(pointcut = "evenReview()", returning = "review")
    public void insertEventReview(JoinPoint jp, Object review) {

        Review returnReview = (Review) review;

        long entityId = returnReview.getReviewId();
        long userId = returnReview.getUserId();

        saveEvent(jp, userId, entityId);
    }

    private void saveEvent(JoinPoint jp, long userId, long entityId) {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        EventFeed eventOperation = method.getAnnotation(EventFeed.class);

        Event event = Event.builder()
                .userId(userId)
                .entityId(entityId)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(eventOperation.type())
                .operation(eventOperation.operation())
                .build();

        eventRepository.save(event).orElseThrow(() ->
                new SaveEventException("Не удалось сохранить событие"));
    }
}