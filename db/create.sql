create or replace table currencies
(
    currency_id   int auto_increment
        primary key,
    currency_code varchar(10) not null
);

create or replace table transfer_directions
(
    direction_id int auto_increment
        primary key,
    direction    varchar(15) null
);

create or replace table users
(
    user_id      int auto_increment
        primary key,
    username     varchar(50) not null,
    password     varchar(50) not null,
    first_name   varchar(32) not null,
    last_name    varchar(32) not null,
    email        varchar(50) not null,
    phone_number varchar(50) not null,
    is_blocked   tinyint     not null,
    is_admin     tinyint     not null,
    constraint users_pk
        unique (username),
    constraint users_pk2
        unique (email),
    constraint users_pk3
        unique (phone_number)
);

create or replace table cards
(
    card_id         int auto_increment
        primary key,
    holder          int          not null,
    number          mediumtext   not null,
    cvv             int          not null,
    expiration_date varchar(255) null,
    constraint cards_holder_fk
        foreign key (holder) references users (user_id)
);

create or replace table profile_photos
(
    profile_photo_id int auto_increment
        primary key,
    user_id          int          not null,
    profile_photo    varchar(640) not null,
    constraint profile_photos_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create or replace table wallets
(
    wallet_id int auto_increment
        primary key,
    holder    int        not null,
    holdings  mediumtext not null,
    currency  int        not null,
    constraint wallets_fk2
        foreign key (currency) references currencies (currency_id),
    constraint wallets_holder_fk
        foreign key (holder) references users (user_id)
);

create or replace table transfers
(
    transfer_id int auto_increment
        primary key,
    sender      int         not null,
    receiver    int         not null,
    card        int         not null,
    wallet      int         not null,
    amount      mediumtext  not null,
    direction   int         not null,
    date        varchar(30) null,
    constraint transfers_sender_fk
        foreign key (sender) references users (user_id),
    constraint transfers_sender_fk2
        foreign key (receiver) references users (user_id),
    constraint transfers_sender_fk3
        foreign key (card) references cards (card_id),
    constraint transfers_sender_fk4
        foreign key (wallet) references wallets (wallet_id),
    constraint transfers_sender_fk5
        foreign key (direction) references transfer_directions (direction_id)
);

