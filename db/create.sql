create table virtual_wallet.currencies
(
    currency_id   int auto_increment
        primary key,
    currency_code varchar(10) not null,
    rating        double      not null
);

create table virtual_wallet.roles
(
    role_id   int auto_increment
        primary key,
    role_type varchar(64) not null
);

create table virtual_wallet.transfer_directions
(
    direction_id int auto_increment
        primary key,
    direction    varchar(15) null
);

create table virtual_wallet.users
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

create table virtual_wallet.cards
(
    card_id         int auto_increment
        primary key,
    holder          int         not null,
    number          varchar(16) not null,
    cvv             varchar(3)  not null,
    expiration_date varchar(5)  not null,
    constraint cards_holder_fk
        foreign key (holder) references virtual_wallet.users (user_id)
);

create table virtual_wallet.profile_photos
(
    profile_photo_id int auto_increment
        primary key,
    user_id          int          not null,
    profile_photo    varchar(640) not null,
    constraint profile_photos_users_user_id_fk
        foreign key (user_id) references virtual_wallet.users (user_id)
);

create table virtual_wallet.transactions
(
    transaction_id  int auto_increment
        primary key,
    sender          int        not null,
    receiver        int        not null,
    amount          mediumtext not null,
    date            date       not null,
    is_confirmed    tinyint(1) not null,
    expiration_date date       not null,
    constraint transactions_sender_fk
        foreign key (sender) references virtual_wallet.users (user_id),
    constraint transactions_sender_fk2
        foreign key (receiver) references virtual_wallet.users (user_id)
);

create table virtual_wallet.users_roles
(
    user_id int not null,
    role_id int not null,
    constraint users_roles_roles_role_id_fk
        foreign key (role_id) references virtual_wallet.roles (role_id),
    constraint users_roles_users_user_id_fk
        foreign key (user_id) references virtual_wallet.users (user_id)
);

create table virtual_wallet.verification_codes
(
    verification_code_id int auto_increment
        primary key,
    user_id              int         not null,
    verification_code    varchar(10) not null,
    constraint verification_codes_users_user_id_fk
        foreign key (user_id) references virtual_wallet.users (user_id)
);

create table virtual_wallet.wallets
(
    wallet_id   int auto_increment
        primary key,
    wallet_type int         not null,
    name        varchar(30) not null,
    holder      int         not null,
    holdings    double      not null,
    currency    int         not null,
    constraint wallets_fk2
        foreign key (currency) references virtual_wallet.currencies (currency_id),
    constraint wallets_holder_fk
        foreign key (holder) references virtual_wallet.users (user_id)
);

create table virtual_wallet.join_wallets_users
(
    wallet_id int not null,
    user_id   int not null,
    constraint join_wallets_users_users_users_user_id_fk
        foreign key (user_id) references virtual_wallet.users (user_id),
    constraint join_wallets_users_wallets_wallet_id_fk
        foreign key (wallet_id) references virtual_wallet.wallets (wallet_id)
);

create table virtual_wallet.transaction_verification_codes
(
    transaction_verification_code_id int(64) auto_increment
        primary key,
    transaction_id                   int(64)    not null,
    verification_code                varchar(7) not null,
    sender_wallet                    int        not null,
    receiver_wallet                  int        not null,
    constraint transaction_verification_codes_transactions_transaction_id_fk
        foreign key (transaction_id) references virtual_wallet.transactions (transaction_id),
    constraint transaction_verification_codes_wallets_wallet_id_fk
        foreign key (sender_wallet) references virtual_wallet.wallets (wallet_id),
    constraint transaction_verification_codes_wallets_wallet_id_fk_2
        foreign key (receiver_wallet) references virtual_wallet.wallets (wallet_id)
);

create table virtual_wallet.transfers
(
    transfer_id int auto_increment
        primary key,
    wallet      int        not null,
    card        int        not null,
    amount      mediumtext not null,
    direction   int        not null,
    date        date       not null,
    constraint transfers_card_fk2
        foreign key (card) references virtual_wallet.cards (card_id) ON DELETE CASCADE,
    constraint transfers_direction_fk3
        foreign key (direction) references virtual_wallet.transfer_directions (direction_id),
    constraint transfers_wallet_fk
        foreign key (wallet) references virtual_wallet.wallets (wallet_id)
);

