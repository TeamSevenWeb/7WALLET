INSERT INTO virtual_wallet.users (user_id, username, password, first_name, last_name, email, phone_number) VALUES (1, 'alex.m', 'ForumSeven', 'Alexander', 'Mechkarov', 'alex.m@abv.bg', '0863333331');
INSERT INTO virtual_wallet.users (user_id, username, password, first_name, last_name, email, phone_number) VALUES (2, 'georgi.i', 'ForumSeven', 'Georgi', 'Iliev', 'georgi.i@abv.bg', '0863333332');
INSERT INTO virtual_wallet.users (user_id, username, password, first_name, last_name, email, phone_number) VALUES (3, 'svetlio.d', 'ForumSeven', 'Svetoslav', 'Dimitrov', 'svetlio.d@abv.bg', '0863333333');
INSERT INTO virtual_wallet.users (user_id, username, password, first_name, last_name, email, phone_number) VALUES (4, 'hansgunter', 'ForumSeven', 'Hans', 'Gunter', 'hans.g@gmx.de', '0863333334');
INSERT INTO virtual_wallet.users (user_id, username, password, first_name, last_name, email, phone_number) VALUES (5, 'peter1996', 'ForumSeven', 'Peter', 'Smith', 'peter.s@gmail.com', '0863333335');
INSERT INTO virtual_wallet.users (user_id, username, password, first_name, last_name, email, phone_number) VALUES (6, 'todor.k', 'ForumSeven', 'Todor', 'Krustev', 'tosho.k@abv.bg', '0863333336');
INSERT INTO virtual_wallet.users (user_id, username, password, first_name, last_name, email, phone_number) VALUES (7, 'vanko02', 'ForumSeven', 'Ivan', 'Ivanov', 'ivan.i@gmail.com', '0863333337');
INSERT INTO virtual_wallet.users (user_id, username, password, first_name, last_name, email, phone_number) VALUES (8, 'stambata', 'ForumSeven', 'Stanimir', 'Hristov', 'stambata@mail.bg', '0863333338');
INSERT INTO virtual_wallet.users (user_id, username, password, first_name, last_name, email, phone_number) VALUES (9, 'johnson', 'ForumSeven', 'John', 'Smith', 'johnny@gmail.com', '0863333339');
INSERT INTO virtual_wallet.users (user_id, username, password, first_name, last_name, email, phone_number) VALUES (10, 'linuxenjoyer93', 'ForumSeven', 'Dragomir', 'Georgiev', 'dragolinux@yahoo.com', '0863333330');

INSERT INTO virtual_wallet.currencies(currency_id, currency_code, rating) VALUES (1,'BGN',1);
INSERT INTO virtual_wallet.currencies(currency_id, currency_code, rating) VALUES (2,'EUR',0.55);
INSERT INTO virtual_wallet.currencies(currency_id, currency_code, rating) VALUES (3,'USD',0.51);

INSERT INTO virtual_wallet.profile_photos (profile_photo_id, user_id, profile_photo) VALUES (29, 3, 'http://res.cloudinary.com/dykfvzq8y/image/upload/v1708322421/f8plknzgwh933vtxglek.png');
INSERT INTO virtual_wallet.profile_photos (profile_photo_id, user_id, profile_photo) VALUES (30, 4, 'http://res.cloudinary.com/dykfvzq8y/image/upload/v1708322664/s0o7t91zi8bkwjkg9bht.jpg');
INSERT INTO virtual_wallet.profile_photos (profile_photo_id, user_id, profile_photo) VALUES (31, 9, 'http://res.cloudinary.com/dykfvzq8y/image/upload/v1708322855/hjpstp4suaaoskqtpfhh.jpg');
INSERT INTO virtual_wallet.profile_photos (profile_photo_id, user_id, profile_photo) VALUES (32, 10, 'http://res.cloudinary.com/dykfvzq8y/image/upload/v1708322960/hbxjeeycbxnlkrc9arro.jpg');
INSERT INTO virtual_wallet.profile_photos (profile_photo_id, user_id, profile_photo) VALUES (33, 6, 'http://res.cloudinary.com/dykfvzq8y/image/upload/v1708323351/kdimcrkxfqbcw1dtervw.jpg');
INSERT INTO virtual_wallet.profile_photos (profile_photo_id, user_id, profile_photo) VALUES (34, 1, 'http://res.cloudinary.com/dykfvzq8y/image/upload/v1708323465/bnsz9z5ddpa3t3bjcelg.jpg');
INSERT INTO virtual_wallet.profile_photos (profile_photo_id, user_id, profile_photo) VALUES (35, 5, 'http://res.cloudinary.com/dykfvzq8y/image/upload/v1708323519/olfvdwbmcwrifnkdcqot.jpg');
INSERT INTO virtual_wallet.profile_photos (profile_photo_id, user_id, profile_photo) VALUES (36, 2, 'http://res.cloudinary.com/dykfvzq8y/image/upload/v1708324086/nief2yyitcixcayy128g.jpg');
INSERT INTO virtual_wallet.profile_photos (profile_photo_id, user_id, profile_photo) VALUES (37, 7, 'http://res.cloudinary.com/dykfvzq8y/image/upload/v1708324109/l38ovbs7iipzobjg3tjc.jpg');
INSERT INTO virtual_wallet.profile_photos (profile_photo_id, user_id, profile_photo) VALUES (38, 8, 'http://res.cloudinary.com/dykfvzq8y/image/upload/v1708324166/h4ifsvd6dw4y6m09pqk7.jpg');

INSERT INTO virtual_wallet.roles (role_id, role_type) VALUES (1, 'admin');
INSERT INTO virtual_wallet.roles (role_id, role_type) VALUES (2, 'blocked');
INSERT INTO virtual_wallet.roles (role_id, role_type) VALUES (3, 'regular');


INSERT INTO virtual_wallet.users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO virtual_wallet.users_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO virtual_wallet.users_roles (user_id, role_id) VALUES (3, 1);
INSERT INTO virtual_wallet.users_roles (user_id, role_id) VALUES (4, 3);
INSERT INTO virtual_wallet.users_roles (user_id, role_id) VALUES (5, 3);
INSERT INTO virtual_wallet.users_roles (user_id, role_id) VALUES (6, 3);
INSERT INTO virtual_wallet.users_roles (user_id, role_id) VALUES (7, 3);
INSERT INTO virtual_wallet.users_roles (user_id, role_id) VALUES (8, 3);
INSERT INTO virtual_wallet.users_roles (user_id, role_id) VALUES (9, 3);
INSERT INTO virtual_wallet.users_roles (user_id, role_id) VALUES (10, 2);

INSERT into virtual_wallet.cards (card_id, holder, number, cvv, expiration_date)  VALUES (1, 'Alexander','6703444444444449','933','03/30');
INSERT into virtual_wallet.cards (card_id, holder, number, cvv, expiration_date)  VALUES (2, 'Georgi','4360000001000005	','633','03/30');
INSERT into virtual_wallet.cards (card_id, holder, number, cvv, expiration_date)  VALUES (3, 'Svetoslav','8171999927660000','733','03/30');





