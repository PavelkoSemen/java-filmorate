<h1 align="center">Filmorate</h1>
<picture>
    <img src="База данных filmorate.png" alt="UML диаграмма" title="UML диаграмма"
    style="display: block; margin: 0 auto; object-position: center;">
</picture>

#### Таблицы
| Номер | Таблица       | Состав таблицы             |
|------:|---------------|:---------------------------|
|     1 | users         | Пользователи               |
|     2 | friends       | Таблица друзей             |
|     3 | mpa           | Рейтинг фильмов            |
|     4 | films         | Список фильмов             |
|     5 | genres        | Жанры                      |
|     6 | film_genre    | Связь фильм - жанр         |
|     7 | likes         | Связь фильм - пользователь |
|     8 | directors     | Режиссеры                  |
|     9 | film_director | Связь фильм - режиссеры    |
|    10 | reviews       | Ревью                      |
|    11 | review_likes  | Связь ревью - пользователь |
|    12 | events        | События                    |


#### Групповой проект, задача - исполнитель
| Номер задачи | Название задачи                  | Исполнитель                  |
|-------------:|----------------------------------|:-----------------------------|
|            1 | Отзывы                           | Давлетгареев Тимур           |
|            2 | Поиск                            | Ростислав Ходяков            |
|            3 | Общие фильмы                     | Семен Павелко                |
|            4 | Рекомендации                     | Дмитрий Осипец               |
|            5 | Лента событий                    | Семен Павелко                |
|            6 | Удаление фильмов и пользователей | Дмитрий Осипец               |
|            7 | Фильмы по режиссёрам             | Рустам Наниев                |
|            8 | Популярные фильмы                | Ирина Бирюлева               |

<details>
<summary>Запросы</summary>

1. Запрос с сортиров по количеству лайков:
```no-highlight
SELECT f.*
     , m.*
     , g.*
FROM films f
         LEFT JOIN (SELECT film_id
                         , COUNT(user_id) as count_likes
                    FROM likes
                    GROUP BY film_id) cf
                   ON cf.film_id = f.film_id
         LEFT JOIN mpa m
                   ON f.mpa_id = m.mpa_id
         LEFT JOIN film_genre fg
                   ON f.film_id = fg.film_id
         LEFT JOIN genres g
                   ON g.genre_id = fg.genre_id
ORDER BY count_likes;
```
2. Запрос для заполнения фильмов
```no-highlight
SELECT f.*
     , m.*
     , g.*
FROM films f
         LEFT JOIN mpa m
                   ON f.mpa_id = m.mpa_id
         LEFT JOIN film_genre fg
                   ON f.film_id = fg.film_id
         LEFT JOIN genres g
                   ON g.genre_id = fg.genre_id
WHERE f.film_id = ?
```
3. Запрос на получение общих друзей
```no-highlight
SELECT u.*
FROM friends fi
         INNER JOIN friends fo
                    ON fi.friend_id = fo.friend_id
                        AND fi.user_id = ?
                        AND fo.user_id = ?
         INNER JOIN users u
                    ON u.user_id = fo.friend_id
```
4. Запрос на получение друзей 
```no-highlight
SELECT u.*
FROM users u
         INNER JOIN (SELECT * 
                     FROM friends where user_id = ?) f
                    ON u.user_id = f.friend_id
```
</details>