-- auto-generated definition
create table role
(
    id          bigint auto_increment
        primary key,
    name        varchar(50)                        not null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint name
        unique (name)
)charset = utf8mb4;

create table permission
(
    id          bigint auto_increment
        primary key,
    name        varchar(20)                        null,
    permission  varchar(255)                       null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
)charset = utf8mb4;

create table user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
)charset = utf8mb4;

create table role_menu
(
    role_id bigint not null,
    menu_id bigint not null,
    primary key (role_id, menu_id)
)charset = utf8mb4;