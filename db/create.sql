create schema virtual_wallet;

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

create table virtual_wallet.cards (
    card_id int auto_increment
        primary key,
    holder int not null ,
    constraint cards_holder_fk
        foreign key (holder) references users (user_id),
    number long not null,
    cvv int not null,
    expiration_date varchar (255)
);

create table virtual_wallet.currencies(
    currency_id int auto_increment primary key,
    currency_code varchar(10) not null
);

create table virtual_wallet.transfers (
    transfer_id int auto_increment
                       primary key,
    sender int not null,
    constraint transfers_sender_fk
                       foreign key (sender) references users (user_id),
    receiver int not null,
    constraint transfers_sender_fk2
        foreign key (receiver) references users (user_id),
    card int not null,
    constraint transfers_sender_fk3
        foreign key (card) references cards (card_id),
    amount long not null,
    direction int not null
);

create table virtual_wallet.wallets (
    wallet_id int auto_increment primary key,
    holder int not null ,
    constraint wallets_holder_fk
        foreign key (holder) references users (user_id),
    holdings long not null,
    currency  int not null,
    constraint wallets_fk2
        foreign key (currency) references currencies (currency_id)
);

