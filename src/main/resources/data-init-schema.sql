create table if not exists admins
(
    chatId varchar(64) primary key not null
);

create table if not exists requests
(
    id        uuid primary key         not null,
    chatId    varchar(64)              not null,
    status    varchar(64)              not null,
    text      varchar(512)             not null,
    createdAt timestamp with time zone not null
);

create table if not exists requestfiles
(
    id        varchar(64) primary key not null,
    requestId uuid                    not null,
    foreign key (requestId) references requests (id)
);