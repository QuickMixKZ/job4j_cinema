insert into authorities(authority) values ('ROLE_USER');
insert into authorities(authority) values ('ROLE_ADMIN');

insert into
    users(username, email, phone, password, authority_id)
VALUES
    ('admin', 'admin@admins.com', '+7 777 777 77 77', '$2a$10$4AsEg2XW5BquYkQaCwfyoeaPMgUtOfxxNfNN7RiJwhOBhgc7owtUa', 2);

INSERT INTO cinema_hall(name, rows_number, seats_per_row) VALUES ('Малый', 10, 8);
INSERT INTO cinema_hall(name, rows_number, seats_per_row) VALUES ('Средний', 12, 14);
INSERT INTO cinema_hall(name, rows_number, seats_per_row) VALUES ('Большой', 20, 15);

INSERT INTO genre(name) VALUES ('Биографический');
INSERT INTO genre(name) VALUES ('Боевик');
INSERT INTO genre(name) VALUES ('Вестерн');
INSERT INTO genre(name) VALUES ('Военный');
INSERT INTO genre(name) VALUES ('Детектив');
INSERT INTO genre(name) VALUES ('Детский');
INSERT INTO genre(name) VALUES ('Драма');
INSERT INTO genre(name) VALUES ('Документальный');
INSERT INTO genre(name) VALUES ('Исторический');
INSERT INTO genre(name) VALUES ('Комедия');
INSERT INTO genre(name) VALUES ('Криминал');
INSERT INTO genre(name) VALUES ('Мистика');
INSERT INTO genre(name) VALUES ('Мультфильм');
INSERT INTO genre(name) VALUES ('Научный');
INSERT INTO genre(name) VALUES ('Приключения');
INSERT INTO genre(name) VALUES ('Семейный');
INSERT INTO genre(name) VALUES ('Спорт');
INSERT INTO genre(name) VALUES ('Триллер');
INSERT INTO genre(name) VALUES ('Ужасы');
INSERT INTO genre(name) VALUES ('Фантастика');
INSERT INTO genre(name) VALUES ('Фэнтези');
