create table cards
(
    card_id         int auto_increment
        primary key,
    holder          varchar(30)  not null,
    number          int(16)      not null,
    cvv             int(3)       not null,
    expiration_date varchar(255) null
);

create table currencies
(
    currency_id   int auto_increment
        primary key,
    currency_code varchar(10) not null
);

create table roles
(
    role_id   int auto_increment
        primary key,
    role_type varchar(64) not null
);

create table transaction_directions
(
    direction_id int auto_increment
        primary key,
    direction    varchar(15) null
);

create table users
(
    user_id      int auto_increment
        primary key,
    username     varchar(50) not null,
    password     varchar(50) not null,
    first_name   varchar(32) not null,
    last_name    varchar(32) not null,
    email        varchar(50) not null,
    phone_number varchar(50) not null,
    constraint users_pk
        unique (username),
    constraint users_pk2
        unique (email),
    constraint users_pk3
        unique (phone_number)
);

create table profile_photos
(
    profile_photo_id int auto_increment
        primary key,
    user_id          int          not null,
    profile_photo    varchar(640) not null,
    constraint profile_photos_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table users_roles
(
    user_id int not null,
    role_id int not null,
    constraint users_roles_roles_role_id_fk
        foreign key (role_id) references roles (role_id),
    constraint users_roles_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table wallets
(
    wallet_id int auto_increment
        primary key,
    holder    int    not null,
    holdings  double not null,
    currency  int    not null,
    constraint wallets_fk2
        foreign key (currency) references currencies (currency_id),
    constraint wallets_holder_fk
        foreign key (holder) references users (user_id)
);

create table join_wallets_users
(
    wallet_id int not null,
    user_id   int not null,
    constraint join_wallets_users_users_users_user_id_fk
        foreign key (user_id) references users (user_id),
    constraint join_wallets_users_wallets_wallet_id_fk
        foreign key (wallet_id) references wallets (wallet_id)
);

create table transactions
(
    transaction_id int auto_increment
        primary key,
    sender         int         not null,
    receiver       int         not null,
    wallet         int         not null,
    amount         mediumtext  not null,
    direction      int         not null,
    date           varchar(30) null,
    constraint transactions_sender_fk
        foreign key (sender) references users (user_id),
    constraint transactions_sender_fk2
        foreign key (receiver) references users (user_id),
    constraint transactions_sender_fk4
        foreign key (wallet) references wallets (wallet_id),
    constraint transactions_sender_fk5
        foreign key (direction) references transaction_directions (direction_id)
);

