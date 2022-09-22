## Веб-приложение "Кинотеатр"

Приложение представляет собой сервис для покупки билетов в кинотеатр.

Стэк технологий технологий:
- Spring boot
- Spring Security
- Thymeleaf
- Bootstrap
- PostgreSQL
- JQuery

Для запуска требуется:
- Java 17
- Maven
- PostgreSQL

Порядок запуска:
1. Создать базу данных
````
CREATE DATABASE cinema
````
2. Запустить проект
````
mvn spring-boot:run
````
При первом запуске в базе создаются:
- Жанры.
- 3 кинозала.
- Пользователь с правами администратора.
````
login: admin password: 123456
````
3. На главной странице перейти к форме авторизации.
   ![main page](src/main/resources/screenshots/login.png)
4. Ввести указаные выше логин и пароль.
   ![authorization](src/main/resources/screenshots/authorization.png)
5. В выпадающем меню выбрать пункт "Фильмы", после перехода в кписку фильмов - перейти к добавлению нового фильма.
   ![movies](src/main/resources/screenshots/add_movie.png)
6. Заполнить данные о фильме, выбрать постер, сохранить.
   ![adding movie](src/main/resources/screenshots/movie_data.png)
7. В выпадающем меню выбрать пункт "Сеансы", на странице списка сеансов - перейти к добавлению нового сеанса.
   ![sessions](src/main/resources/screenshots/add_session.png)
8. Заполнить данные: выбрать фильм, кинозал, дату начала сеанса, сохранить.
   ![add session](src/main/resources/screenshots/save_session.png)

### Покупка билетов пользователем 
1. Переход к регистрации.
   ![reg](src/main/resources/screenshots/reg.png)
2. Форма регистрации.
   ![reg_form](src/main/resources/screenshots/reg_form.png)
3. После входа на главной странице выбрать фильм.
   ![chose_movie](src/main/resources/screenshots/chose_movie.png)
4. Выбрать время сеанса.
   ![chose_session](src/main/resources/screenshots/session_time.png)
5. Выбрать места, выполнить покупку, нажав кнопку "Купить"
   ![chose_seats](src/main/resources/screenshots/chose_seats.png)
   - Если места доступны, то пользовтаель получит сообщение об успешной покупке, и перенаправлен на страницу с билетами:
     ![chosen_seats](src/main/resources/screenshots/chose_seats.png)
     ![success_buy](src/main/resources/screenshots/succes_buy.png)
     ![tickets](src/main/resources/screenshots/users_tickets.png)
   - Если в процессе выбора, выбранные места успел купить другой пользователь, то будет выведено сообщение об ошибке, и страница обновится.
     ![fail_buy](src/main/resources/screenshots/fail_buy.png)
   
